package org.wikicrimes.servlet;

import static org.wikicrimes.servlet.ServletKernelMap.KERNEL;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.wikicrimes.util.MapaChaveDupla;
import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.PropertiesLoader;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.geometria.Segmento;
import org.wikicrimes.util.rotaSegura.googlemaps.StatusGM;
import org.wikicrimes.util.rotaSegura.logica.CalculoPerigo;
import org.wikicrimes.util.rotaSegura.logica.LogicaRotaSegura;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaGM;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaPromissora;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas.NaoTemCaminhoException;
import org.wikicrimes.util.rotaSegura.testes.TesteCenariosRotas;
import org.wikicrimes.util.rotaSegura.testes.TesteRotasImg;

/**
 * Trata requisições HTTP para calcular rotas seguras.
 * Chama um método do ServletKernelMap para calcular mapa de kernel.
 * 
 * @author victor
 */
@SuppressWarnings("serial")
public class ServletRotaSeguraClientSide extends HttpServlet {
	
	//PARAMETROS
	private static final int MAX_REQUISICOES_GM = PropertiesLoader.getInt("max_requisicoes_gm");
	private static final int NUM_ROTAS_RESPOSTA = PropertiesLoader.getInt("num_rotas_resposta");
	
	//chaves para atributos de sessao
	public static final String LOGICA_ROTAS = "LOGICA_ROTAS";
	private static final String ULTIMA_RESPOSTA = "ULTIMA_RESPOSTA"; //ultima resposta deste servlet. Eh enviada como requisicao pro GoogleMaps
	private static final String ROTAS_PROMISSORAS = "ROTAS_PROMISSORAS"; //fila de requisições p/ fazer ao GoogleMaps (são rotas)
	/*teste*/private static final String TEMPO_INICIO = "TEMPO_INICIO";
	
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
				respostaErro(request, response);
				/*teste*/TesteCenariosRotas.setResult("erro", e.getClass() + " : " + e.getMessage());
				/*teste*/TesteCenariosRotas.salvar();
			}
			limpar(request);
		}
	}
	
	private void primeiraExecucao(HttpServletRequest request) throws IOException, ServletException{
		
		//inicialização dos objetos principais
		HttpSession sessao = request.getSession();
		LogicaRotaSegura logicaRota = getLogicaRotaSegura(request);
		sessao.setAttribute(ROTAS_PROMISSORAS, new PriorityQueue<RotaPromissora>());
		
		//inicializa o GRAFO com a rota original que vai da origem até o destino
		Rota rotaGoogleMaps = getRota(request);
		GrafoRotas grafo = new GrafoRotas(rotaGoogleMaps);
		logicaRota.setGrafo(grafo);
		
		/*teste*/sessao.setAttribute(TEMPO_INICIO, System.currentTimeMillis());
		/*teste*/logicaRota.getCalculoPerigo().FATOR_TOLERANCIA = TesteCenariosRotas.getFatTol();
	}
	
	@SuppressWarnings("unchecked")
	private void metodoPrincipal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		//inicialização dos objetos principais
		HttpSession sessao = request.getSession();
		int iteracao = getContRevisoes(request); 
		LogicaRotaSegura logicaRota = getLogicaRotaSegura(request);
		CalculoPerigo calcPerigo = logicaRota.getCalculoPerigo();
		StatusGM status = getStatusGM(request);
		GrafoRotas grafo = logicaRota.getGrafo();
		Rota ultimaResp = (Rota)sessao.getAttribute(ULTIMA_RESPOSTA);
		Queue<Rota> rotasPromissoras = (Queue<Rota>)sessao.getAttribute(ROTAS_PROMISSORAS);
		List<Rota> rotasNovas = null;
		
		//RECEBE rota do GoogleMaps, bota no grafo, trata erro
		switch(status){
		case SUCCESS:
			//recebe a rota obtida do GoogleMaps
			Rota rotaGoogleMaps = getRota(request);
			if(isPrimeiraVez(request)){
				rotasNovas = new ArrayList<Rota>();
				rotasNovas.add(rotaGoogleMaps);
			}else{
				corrigirRota(rotaGoogleMaps, ultimaResp); //caso o inicio e/ou fim nao coincidam exatamente (mas ainda sejam proximos)
				rotasNovas = rotaGoogleMaps.dividirAprox(ultimaResp.getPontosArray());
			}
			for(int i=0; i<rotasNovas.size(); i++){
				Rota rota = rotasNovas.get(i);
				double custo = rota.distanciaPercorrida(); 
				double perigo = calcPerigo.perigo(rota);
				grafo.inserirCaminho(rota, custo, perigo);
				rotasNovas.set(i, new RotaGM(rota, custo, perigo));
			}
			atualizaFilaRotasPromissoras(rotasPromissoras, calcPerigo, grafo);
			break;
		case UNKNOWN_DIRECTIONS: //tinha um ponto no meio do mar, montanha, etc
			//obs: o case eh vazio mesmo, eh soh pra nao cair no else
			break;
		case TOO_MANY_QUERIES:
			respostaRepeteTrechoAnterior(request, response);
			return;
		case BAD_REQUEST:
			/*DEBUG*/System.err.println("BAD_REQUEST, rota=" + ultimaResp);
			throw new AssertionError(status);
		default: //status nao previsto
			throw new AssertionError(status);
		}
		
//		/*TESTE*/logicaRota.getRotasPromissoras(grafo.getOrigem(), grafo.getDestino());
		
		//RESPOSTA
		Rota melhorCaminho = grafo.melhorCaminho();
		
		/*TESTE*/TesteRotasImg teste = new TesteRotasImg(logicaRota.getKernel(), grafo);
//		/*TESTE*/teste.setTituloTesteCenarios(getContRevisoes(request) + ", " + status);
		/*TESTE*/teste.setTitulo(iteracao + ", " + status);
		/*TESTE*/teste.addRota(melhorCaminho, new Color(0,150,0));
		/*TESTE*/if(!isPrimeiraVez(request)) teste.addRota(new Rota(ultimaResp), new Color(100,100,255));
		/*TESTE*/teste.salvar();
		
		if(!calcPerigo.isToleravel(melhorCaminho) && iteracao < MAX_REQUISICOES_GM){
			if(status == StatusGM.SUCCESS){
				for(Rota rota : rotasNovas){
					Queue<Rota> alternativas = logicaRota.criarAlternativas(rota, iteracao); 
					rotasPromissoras.addAll(alternativas);
				}
			}
			if(!rotasPromissoras.isEmpty()){
				Rota novaRequisicao = rotasPromissoras.poll();
				respostaPedeNovoTrecho(request, response, novaRequisicao);
			}else{
				respostaFim(request, response, grafo.verticesKMenoresCaminhos(NUM_ROTAS_RESPOSTA));
				limpar(request);
				throw new AssertionError("nao tem mais rotas alternativas");
			}
		}else{
			respostaFim(request, response, grafo.verticesKMenoresCaminhos(NUM_ROTAS_RESPOSTA));
			limpar(request);
		}
		
		/*teste*/long t1 = (Long)sessao.getAttribute(TEMPO_INICIO);
		/*teste*/long t2 = System.currentTimeMillis();
		/*teste*/long tempo = (t2-t1)/1000;
		/*teste*/TesteCenariosRotas.setResult("tempo", tempo);
		/*teste*/double distFin = melhorCaminho.distanciaPercorrida();
		/*teste*/TesteCenariosRotas.setResult("distFin", distFin);
		/*teste*/double qualiFin = calcPerigo.perigo(melhorCaminho);
		/*teste*/TesteCenariosRotas.setResult("qualiFin", qualiFin);
		/*teste*/TesteCenariosRotas.setResult("reqGM", getContRevisoes(request));
		/*teste*/TesteCenariosRotas.salvar();
		
	}
	
	
	/**
	 * Faz uma limpeza nos objetos da sessão, preparando pra uma próxima série de requisições do mesmo usuário
	 */
	private void limpar(HttpServletRequest request) throws IOException, ServletException{
		HttpSession sessao = request.getSession();
		LogicaRotaSegura logicaRotas = getLogicaRotaSegura(request);
		logicaRotas.limpar();
		sessao.removeAttribute(LOGICA_ROTAS);
		sessao.removeAttribute(ULTIMA_RESPOSTA);
		sessao.removeAttribute(ROTAS_PROMISSORAS);
		/*TESTE*/TesteRotasImg.limpar();
		System.gc();
	}
	
	private void respostaPedeNovoTrecho(HttpServletRequest request, HttpServletResponse response, Rota trecho) throws IOException{
		/*DEBUG*/System.out.println("PEDE NOVO TRECHO, contRev=" + getContRevisoes(request));
		request.getSession().setAttribute(ULTIMA_RESPOSTA, trecho); //necessário caso de o erro TOO_MANY_QUERIES no GM
		PrintWriter out = response.getWriter();
		out.println("continua");
		out.println(trecho.invertida());	//rota segura gerada
		out.close();
	}
	
	private void respostaRepeteTrechoAnterior(HttpServletRequest request, HttpServletResponse response) throws IOException{
		/*DEBUG*/System.out.println("PEDE NOVO TRECHO, contRev=" + getContRevisoes(request));
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
		/*DEBUG*/System.out.println("FIM");
		out.close();
	}
	
	public static void respostaFim(HttpServletRequest request, HttpServletResponse response, List<Rota> rotas) throws IOException{
		/*DEBUG*/if(rotas == null) throw new AssertionError("rotas == null");
		/*DEBUG*/if(rotas.isEmpty()) throw new AssertionError("rotas == empty");
		PrintWriter out = response.getWriter();
		out.println("fim");							//sinal pro fim
		for(Rota rota : rotas)
			out.println(rota.invertida());			//rota segura gerada (cara rota em uma linha) 
		/*DEBUG*/System.out.println("FIM");
		out.close();
	}
	
	public static void respostaErro(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		out.println("erro");
		/*DEBUG*/System.out.println("FIM ERRO");
		out.close();
	}
	
	public static Rota getRota(HttpServletRequest request) throws ServletException, IOException {
		String stringRotas = null;
		try{
			stringRotas = request.getParameter("rotas");
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
	
	public static LogicaRotaSegura getLogicaRotaSegura(HttpServletRequest request) throws ServletException, IOException{
		HttpSession sessao = request.getSession();
		if(!isLogicaRotasNaSessao(request)){ 
			//primeira requisição, com a rota original
			KernelMap kernel = (KernelMap)sessao.getAttribute(KERNEL);
			if(kernel == null) throw new RuntimeException("kernel null");
			LogicaRotaSegura logicaRotas = new LogicaRotaSegura(kernel);
			sessao.setAttribute(LOGICA_ROTAS, logicaRotas);
		}
		LogicaRotaSegura logicaRotas = (LogicaRotaSegura)sessao.getAttribute(LOGICA_ROTAS);
		
		return logicaRotas;
	}
	
	public static boolean isLogicaRotasNaSessao(HttpServletRequest request){
		HttpSession sessao = request.getSession();
		return sessao.getAttribute(LOGICA_ROTAS) != null && sessao.getAttribute(LOGICA_ROTAS) instanceof LogicaRotaSegura;
	}
	
	public static StatusGM getStatusGM(HttpServletRequest request){
		String statusStr = request.getParameter("status");
		StatusGM status = null; 
		if(statusStr != null)
			status = StatusGM.getStatus(Integer.parseInt(statusStr));
		return status;
	}
	
	public static void atualizaFilaRotasPromissoras(Queue<Rota> q, CalculoPerigo calcPerigo, GrafoRotas grafo){
		List<Rota> temp = new LinkedList<Rota>();
		temp.addAll(q);
		q.clear();
		MapaChaveDupla<Ponto, Ponto, Double> memoizacao = new MapaChaveDupla<Ponto, Ponto, Double>();
		for(Rota r : temp){
			double pesoAtualizado = reponderarRotaPromissora(r, calcPerigo, grafo, memoizacao);
			RotaPromissora rotaAtualizada = new RotaPromissora(r, pesoAtualizado);
			q.add(rotaAtualizada);
		}
	}
	
	public static double reponderarRotaPromissora(Rota rota, CalculoPerigo calcPerigo, GrafoRotas grafo, 
			MapaChaveDupla<Ponto, Ponto, Double> memoizacao){
		Ponto o = grafo.getOrigem();
		Ponto d = grafo.getDestino();
		Ponto i = rota.getInicio();
		Ponto f = rota.getFim();
		Double perigoComeco = memoizacao.get(o, i);
		Double perigoFim = memoizacao.get(f, d);
		try{
			if(perigoComeco == null){
				if(o.equals(i)){
					perigoComeco = 0.0;
				}else{
					try{
						Rota comeco = grafo.melhorCaminho(o, i);
						perigoComeco = calcPerigo.perigo(comeco);
					}catch(InvalidParameterException e){
						perigoComeco = calcPerigo.perigo(new Segmento(o,i)); //FIXME Isto resolve bug com rotas quebradas por ter muitos waypoints.
																			 //Quando os pontos em q a rota foi dividida nao se ligam ao grafo
																			 //Problema: perde a informacao de perigo das outras parte da rota 
					}
				}
				memoizacao.put(o, i, perigoComeco);
			}
			if(perigoFim == null){
				if(f.equals(d)){
					perigoFim = 0.0;
				}else{
					try{
						Rota fim = grafo.melhorCaminho(f, d);
						perigoFim = calcPerigo.perigo(fim);
					}catch(InvalidParameterException e){
						perigoFim = calcPerigo.perigo(new Segmento(o,i)); //FIXME idem (ver fixme 15 linhas acima)
					}
				}
				memoizacao.put(f, d, perigoFim);
			}
			return perigoComeco + calcPerigo.perigo(rota) + perigoFim;
		}catch(NaoTemCaminhoException e){
			/*DEBUG*/System.out.println("***NaoTemCaminhoException, inicio: " + e.inicio + ", fim: "+ e.fim + ", vertice sem pai: "+ e.semPai);
			return Double.POSITIVE_INFINITY;
		}
	}
	
	/**
	 * caso o inicio e/ou fim dos 2 caminhos nao coincidam exatamente
	 */
	public static void corrigirRota(Rota resposta, Rota requisicao){
		Ponto iniReq = requisicao.getInicio();
		Ponto iniResp = resposta.getInicio();
		if(!iniReq.equals(iniResp) /*&& iniReq.isPerto(iniResp)*/){
			resposta.addInicio(iniReq);
		}
		Ponto fimReq = requisicao.getFim();
		Ponto fimResp = resposta.getFim();
		if(!fimReq.equals(fimResp) /*&& fimReq.isPerto(fimResp)*/){
			resposta.addFim(fimReq);
		}
	}
	
}

