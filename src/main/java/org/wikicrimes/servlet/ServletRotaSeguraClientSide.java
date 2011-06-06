package org.wikicrimes.servlet;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.wikicrimes.util.kernelmap.KernelMap;
import org.wikicrimes.util.kernelmap.PropertiesLoader;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.googlemaps.StatusGM;
import org.wikicrimes.util.rotaSegura.logica.FilaRotasCandidatas;
import org.wikicrimes.util.rotaSegura.logica.Perigo;
import org.wikicrimes.util.rotaSegura.logica.SafeRouteCalculator;
import org.wikicrimes.util.rotaSegura.logica.exceptions.CantFindPath;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaGM;
import org.wikicrimes.util.rotaSegura.testes.TesteCenariosRotas;
import org.wikicrimes.util.rotaSegura.testes.TesteRotasImg;
import org.wikicrimes.util.statistics.SessionBuffer;

/**
 * Trata requisicoes HTTP para calcular rotas seguras.
 * Chama um metodo do ServletKernelMap para calcular mapa de kernel.
 * 
 * @author victor
 */
@SuppressWarnings("serial")
public class ServletRotaSeguraClientSide extends HttpServlet {
	
	//PARAMETROS
	private static final int MAX_REQUISICOES_GM = PropertiesLoader.getInt("saferoutes.max_gm_requests");
	private static final int PADRAO_NUM_ROTAS_RESPOSTA = PropertiesLoader.getInt("saferoutes.default_number_of_alternatives");
	
	//chaves para atributos de sessao
	public static final String LOGICA_ROTAS = "LOGICA_ROTAS";
	private static final String ULTIMA_RESPOSTA = "ULTIMA_RESPOSTA"; //ultima resposta deste servlet. Eh enviada como requisicao pro GoogleMaps
	private static final String ROTAS_CANDIDATAS = "ROTAS_CANDIDATAS"; //fila de requisicoes p/ fazer ao GoogleMaps (sao rotas)
//	private static final String LONG_ROUTE_REQUEST = "LONG_ROUTE_REQUEST"; //objeto q gerencia a requisicao repartida de rotas longas
	/*TESTE CENARIO*/private static final String TEMPO_INICIO = "TEMPO_INICIO";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*DEBUG*/System.out.println("keyset: " + request.getParameterMap().keySet() + 
				", #" + getContRevisoes(request) + ", " + getStatusGM(request));
		
		try{
			if(isPrimeiraVez(request))
				primeiraExecucao(request);
			metodoPrincipal(request, response);
		}catch(Throwable e){
			e.printStackTrace();
			if(!response.isCommitted()){
				respostaErro(response, e);
				/*TESTE CENARIO*/TesteCenariosRotas.setResult("erro", e.getClass() + " : " + e.getMessage());
				/*TESTE CENARIO*/TesteCenariosRotas.salvar();
			}
			limpar(request);
		}
	}
	
	private void primeiraExecucao(HttpServletRequest request) throws IOException, ServletException{
		
		//inicializacao dos objetos principais
		SafeRouteCalculator logicaRota = getLogicaRotaSegura(request);
		
		//inicializa o GRAFO com a rota original que vai da origem ate o destino
		Rota rotaGoogleMaps = getRota(request);
		Perigo calcPerigo = logicaRota.getCalculoPerigo();
		GrafoRotas grafo = new GrafoRotas(rotaGoogleMaps, calcPerigo);
		logicaRota.setGrafo(grafo);
		
		/*TESTE CENARIO*/request.getSession().setAttribute(TEMPO_INICIO, System.currentTimeMillis());
//		/*TESTE CENARIO*/logicaRota.getCalculoPerigo().FATOR_TOLERANCIA = TesteCenariosRotas.getFatTol();
	}
	
	private void metodoPrincipal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		//inicializacao dos objetos principais
		HttpSession sessao = request.getSession();
		int iteracao = getContRevisoes(request); 
		SafeRouteCalculator logicaRota = getLogicaRotaSegura(request);
		Perigo calcPerigo = logicaRota.getCalculoPerigo();
		StatusGM status = getStatusGM(request);
		GrafoRotas grafo = logicaRota.getGrafo();
		Rota ultimaResp = (Rota)sessao.getAttribute(ULTIMA_RESPOSTA);
		FilaRotasCandidatas rotasCandidatas = getRotasCandidatas(sessao, logicaRota);
		List<Rota> rotasNovas = null;
		
		//RECEBE rota do GoogleMaps, bota no grafo, trata erro
		switch(status){
		case SUCCESS:
			//recebe a rota obtida do GoogleMaps
			Rota rotaGoogleMaps = getRota(request);
//			LongRouteRequest lrr = manageLongRoute(request, response, rotaGoogleMaps);
//			if(lrr != null) {
//				if(lrr.isDone())
//					rotaGoogleMaps = lrr.getRealRoute();
//				else
//					return;
//			}
			if(isPrimeiraVez(request)){
//				/*TESTE TOLERANCIA*/double distMin = rotaGoogleMaps.distanciaRetaOD();
//				/*TESTE TOLERANCIA*/double densMedia = logicaRota.getKernel().getMediaDens();
//				/*TESTE TOLERANCIA*/double densMax = logicaRota.getKernel().getMaxDens();
//				/*TESTE TOLERANCIA*/double tol = calcPerigo.tolerancia(rotaGoogleMaps);
//				/*TESTE TOLERANCIA*/TesteCenariosRotas.writeToleranceToFile(tol, distMin, densMedia, densMax);
//				/*TESTE TOLERANCIA*/respostaErro(request, response); limpar(request); if(true)return;
				rotasNovas = new ArrayList<Rota>();
				rotasNovas.add(rotaGoogleMaps);
			}else{
				corrigirPontasDaRota(rotaGoogleMaps, ultimaResp); //caso o inicio e/ou fim nao coincidam exatamente (mas ainda sejam proximos)
				rotasNovas = rotaGoogleMaps.dividirAprox(ultimaResp.getPontosArray());
			}
			for(int i=0; i<rotasNovas.size(); i++){
				Rota rota = rotasNovas.get(i);
				double custo = rota.distanciaPercorrida(); 
				double perigo = calcPerigo.perigo(rota);
//				grafo.inserirRota(rota, custo, perigo);
				grafo.inserirRota(rota, custo);
				rotasNovas.set(i, new RotaGM(rota, custo, perigo));
			}
			rotasCandidatas.reponderar();
			break;
		case UNKNOWN_DIRECTIONS: //tinha um ponto no meio do mar, montanha, etc
			//obs: o case eh vazio mesmo, eh soh pra nao cair no else
			break;
		case TOO_MANY_QUERIES:
			respostaRepeteTrechoAnterior(request, response);
			return;
		case BAD_REQUEST:
//			/*DEBUG*/System.err.println("BAD_REQUEST, rota=" + ultimaResp);
			throw new AssertionError(status);
		default: //status nao previsto
			throw new AssertionError(status);
		}
		
//		/*TESTE*/logicaRota.getRotasPromissoras(grafo.getOrigem(), grafo.getDestino());
		
		//RESPOSTA
		boolean rotaEhToleravel;
		Rota melhorCaminho = null;
		try {
			melhorCaminho = grafo.melhorCaminho();
			/*TESTE*/TesteRotasImg teste = new TesteRotasImg(logicaRota.getKernel(), grafo);
//			/*TESTE*/teste.setTituloTesteCenarios(getContRevisoes(request) + ", " + status);
			/*TESTE*/teste.setTitulo(iteracao + ", " + status);
			/*TESTE*/teste.addRota(melhorCaminho, new Color(0,150,0));
			/*TESTE*/if(!isPrimeiraVez(request)) teste.addRota(new Rota(ultimaResp), new Color(100,100,255));
			/*TESTE*/if(status == StatusGM.SUCCESS) teste.addRota(getRota(request), Color.BLUE);
			/*TESTE*/teste.salvar();
			rotaEhToleravel = calcPerigo.isToleravel(melhorCaminho); 
		} catch (CantFindPath e1) {
			rotaEhToleravel = false;
		}
		
		if(!rotaEhToleravel && iteracao < MAX_REQUISICOES_GM){
			if(status == StatusGM.SUCCESS){
				for(Rota rota : rotasNovas){
					Queue<Rota> alternativas = logicaRota.criarAlternativas(rota, iteracao);
					rotasCandidatas.addAll(alternativas);
				}
			}
			if(!rotasCandidatas.isEmpty()){
				Rota novaRequisicao = rotasCandidatas.pop();
				respostaPedeNovoTrecho(request, response, novaRequisicao);
			}else{
//				double maxWeight = calcPerigo.perigo(grafo.getRotaOriginal());
				List<Rota> rotas = grafo.verticesKMenoresCaminhos(PADRAO_NUM_ROTAS_RESPOSTA);
				respostaFim(request, response, rotas);
				limpar(request);
				System.err.println("***nao tem mais rotas alternativas");
			}
			
			/*TESTE CENARIO*/long t1 = (Long)sessao.getAttribute(TEMPO_INICIO);
			/*TESTE CENARIO*/long t2 = System.currentTimeMillis();
			/*TESTE CENARIO*/long tempo = (t2-t1)/1000;
			/*TESTE CENARIO*/TesteCenariosRotas.setResult("tempo", tempo);
			/*TESTE CENARIO*/double distFin = melhorCaminho.distanciaPercorrida();
			/*TESTE CENARIO*/TesteCenariosRotas.setResult("distFin", distFin);
			/*TESTE CENARIO*/double qualiFin = calcPerigo.perigo(melhorCaminho);
			/*TESTE CENARIO*/TesteCenariosRotas.setResult("qualiFin", qualiFin);
			/*TESTE CENARIO*/TesteCenariosRotas.setResult("reqGM", getContRevisoes(request));
			/*TESTE CENARIO*/TesteCenariosRotas.salvar();
			
		}else{

			//			double maxWeight = calcPerigo.perigo(grafo.getRotaOriginal());
			List<Rota> rotas = grafo.verticesKMenoresCaminhos(PADRAO_NUM_ROTAS_RESPOSTA);
			/*TESTE CENARIO*/long t1 = (Long)sessao.getAttribute(TEMPO_INICIO);
			/*TESTE CENARIO*/long t2 = System.currentTimeMillis();
			/*TESTE CENARIO*/long tempo = (t2-t1)/1000;
			/*TESTE CENARIO*/TesteCenariosRotas.setResult("tempo", tempo);
			/*TESTE CENARIO*/double distFin = melhorCaminho.distanciaPercorrida();
			/*TESTE CENARIO*/TesteCenariosRotas.setResult("distFin", distFin);
			/*TESTE CENARIO*/double qualiFin = calcPerigo.perigo(melhorCaminho);
			/*TESTE CENARIO*/TesteCenariosRotas.setResult("qualiFin", qualiFin);
			/*TESTE CENARIO*/TesteCenariosRotas.setResult("reqGM", getContRevisoes(request));
			/*TESTE CENARIO*/TesteCenariosRotas.salvar();
			respostaFim(request, response, rotas);
			limpar(request);
		}
		
		
	}
	
//	private LongRouteRequest manageLongRoute(HttpServletRequest request, HttpServletResponse response, Rota realRoutePart) throws IOException {
//		LongRouteRequest lrr = (LongRouteRequest)request.getSession().getAttribute(LONG_ROUTE_REQUEST);
//		if(lrr == null)
//			return null;
//		lrr.haveResponse(realRoutePart);
//		if(lrr.isDone()) {
//			request.getSession().removeAttribute(LONG_ROUTE_REQUEST);
//			return lrr;
//		}else {
//			Rota nextPart = lrr.nextPart();
//			respostaPedeNovoTrecho(request, response, nextPart);
//			return lrr;
//		}
//	}
	
	
	/**
	 * Faz uma limpeza nos objetos da sessao, preparando pra uma proxima serie de requisicoes do mesmo usuario
	 */
	private void limpar(HttpServletRequest request) throws IOException, ServletException{
		HttpSession sessao = request.getSession();
		SafeRouteCalculator logicaRotas = getLogicaRotaSegura(request);
		logicaRotas.limpar();
		sessao.removeAttribute(LOGICA_ROTAS);
		sessao.removeAttribute(ULTIMA_RESPOSTA);
		sessao.removeAttribute(ROTAS_CANDIDATAS);
//		/*TESTE*/TesteRotasImg.limpar();
		System.gc();
	}
	
	private void respostaPedeNovoTrecho(HttpServletRequest request, HttpServletResponse response, Rota trecho) throws IOException{
//		/*DEBUG*/System.out.println("PEDE NOVO TRECHO, contRev=" + getContRevisoes(request));
		request.getSession().setAttribute(ULTIMA_RESPOSTA, trecho); //necessario caso de o erro TOO_MANY_QUERIES no GM
//		if(LongRouteRequest.isTooLong(trecho)) {
//			LongRouteRequest lrr = new LongRouteRequest(trecho);
//			trecho = lrr.nextPart();
//			request.getSession().setAttribute(LONG_ROUTE_REQUEST, lrr); 
//		}
		PrintWriter out = response.getWriter();
		out.println("continua");
		out.println(trecho.invertida());	//rota segura gerada
		out.close();
	}
	
	private void respostaRepeteTrechoAnterior(HttpServletRequest request, HttpServletResponse response) throws IOException{
//		/*DEBUG*/System.out.println("PEDE NOVO TRECHO, contRev=" + getContRevisoes(request));
		Rota trecho = (Rota)request.getSession().getAttribute(ULTIMA_RESPOSTA);
		PrintWriter out = response.getWriter();
		out.println("continua");
		out.println(trecho.invertida());	//rota segura gerada na vez anterior, qd deu TOO_MANY_QUERIES no GM
		out.close();
	}
	
	public static void respostaFim(HttpServletRequest request, HttpServletResponse response, Rota rota) throws IOException{
		/*DEBUG*/if(rota == null) throw new AssertionError("rota == null");
		PrintWriter out = response.getWriter();
		out.println("fim");			//sinal pro fim
		out.println(rota.invertida());			//rota segura gerada
//		/*DEBUG*/System.out.println("FIM");
		out.close();
	}
	
	public static void respostaFim(HttpServletRequest request, HttpServletResponse response, List<Rota> rotas) throws IOException{
		/*DEBUG*/if(rotas == null) throw new AssertionError("rotas == null");
		/*DEBUG*/if(rotas.isEmpty()) throw new AssertionError("rotas == empty");
		PrintWriter out = response.getWriter();
		out.println("fim");							//sinal pro fim
		for(Rota rota : rotas)
			out.println(rota.invertida());			//rota segura gerada (cara rota em uma linha) 
//		/*DEBUG*/System.out.println("FIM");
		out.close();
	}
	
	public static void respostaErro(HttpServletResponse response) throws IOException{
		respostaErro(response, (String)null);
	}
	
	public static void respostaErro(HttpServletResponse response, String details) throws IOException{
		PrintWriter out = response.getWriter();
		out.println("error");
		if(details != null) {
			out.println(details);
		}
//		/*DEBUG*/System.out.println("FIM ERRO");
		out.close();
	}
	
	public static void respostaErro(HttpServletResponse response, Throwable error) throws IOException{
		PrintWriter out = response.getWriter();
		error.printStackTrace(out);
		out.close();
	}
	
	public static Rota getRota(HttpServletRequest request) throws ServletException, IOException {
		String stringRotas = null;
		try{
			stringRotas = request.getParameter("rota");
			Rota rota = new Rota(stringRotas).invertida();
			return rota;
		}catch(RuntimeException e){
			e.printStackTrace();
			if(stringRotas == null)
				stringRotas = "null";
			else if(stringRotas.equals(""))
				stringRotas = "string vazia";
			throw new AssertionError("stringRotas: " + stringRotas);
		}
	}
	
	private static int getContRevisoes(HttpServletRequest request){
		String str = request.getParameter("contRevisao");
		if(str == null)
			return 0;
		else
			return Integer.valueOf(str);
	}
	
	private boolean isPrimeiraVez(HttpServletRequest request){
		return getContRevisoes(request)==0 || !isLogicaRotasNaSessao(request);
	}
	
	public static SafeRouteCalculator getLogicaRotaSegura(HttpServletRequest request) throws ServletException, IOException{
		HttpSession sessao = request.getSession();
		if(!isLogicaRotasNaSessao(request)){ 
			//primeira requisicao, com a rota original
//			KernelMap kernel = (KernelMap)sessao.getAttribute(KERNEL);
			SessionBuffer sessionBuffer = new SessionBuffer(request);
			KernelMap kernel = sessionBuffer.getKernelMap();
			if(kernel == null) throw new RuntimeException("kernel null");
			SafeRouteCalculator logicaRotas = new SafeRouteCalculator(kernel);
			sessao.setAttribute(LOGICA_ROTAS, logicaRotas);
		}
		SafeRouteCalculator logicaRotas = (SafeRouteCalculator)sessao.getAttribute(LOGICA_ROTAS);
		return logicaRotas;
	}
	
	public static boolean isLogicaRotasNaSessao(HttpServletRequest request){
		HttpSession sessao = request.getSession();
		return sessao.getAttribute(LOGICA_ROTAS) != null && sessao.getAttribute(LOGICA_ROTAS) instanceof SafeRouteCalculator;
	}
	
	private FilaRotasCandidatas getRotasCandidatas(HttpSession sessao, SafeRouteCalculator logicaRota) {
		FilaRotasCandidatas rotas = (FilaRotasCandidatas)sessao.getAttribute(ROTAS_CANDIDATAS);
		if(rotas == null) {
			rotas = new FilaRotasCandidatas(logicaRota.getCalculoPerigo(), logicaRota.getGrafo());
			sessao.setAttribute(ROTAS_CANDIDATAS, rotas);
		}
		return rotas;
	}
	
	public static StatusGM getStatusGM(HttpServletRequest request){
		String statusStr = request.getParameter("status");
		StatusGM status = null; 
		if(statusStr != null)
			status = StatusGM.getStatus(Integer.parseInt(statusStr));
		return status;
	}
	
	/**
	 * caso o inicio e/ou fim dos 2 caminhos nao coincidam exatamente
	 */
	public static void corrigirPontasDaRota(Rota resposta, Rota ideal){
		Ponto iniIdeal = ideal.getInicio();
		Ponto iniResposta = resposta.getInicio();
		if(!iniIdeal.equals(iniResposta) /*&& iniReq.isPerto(iniResp)*/){
			resposta.addInicio(iniIdeal);
		}
		Ponto fimIdeal = ideal.getFim();
		Ponto fimResposta = resposta.getFim();
		if(!fimIdeal.equals(fimResposta) /*&& fimReq.isPerto(fimResp)*/){
			resposta.addFim(fimIdeal);
		}
	}
	
}

