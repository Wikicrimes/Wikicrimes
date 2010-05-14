package org.wikicrimes.servlet;


import static org.wikicrimes.servlet.ServletKernelMap.KERNEL;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.wikicrimes.util.kernelMap.GrafoRotas;
import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.LogicaRotaSegura;
import org.wikicrimes.util.kernelMap.PropertiesLoader;
import org.wikicrimes.util.kernelMap.Ponto;
import org.wikicrimes.util.kernelMap.Caminho;
import org.wikicrimes.util.kernelMap.RotaGM;
import org.wikicrimes.util.kernelMap.SegmentoReta;
import org.wikicrimes.util.kernelMap.StatusGM;
import org.wikicrimes.util.kernelMap.testes.TesteCenariosRotas;
import org.wikicrimes.util.kernelMap.testes.TesteRotas;

/**
 * Trata requisi��es HTTP para calcular rotas seguras.
 * Chama um m�todo do ServletKernelMap para calcular mapa de kernel.
 * 
 * @author victor
 */
public class ServletRotaSegura extends HttpServlet {
	
	//chaves para atributos de sess�o
	private static final String LOGICA_ROTAS = "LOGICA_ROTAS"; //cont�m o MapaKernel tamb�m
	private static final String FILA_TRECHOS_EXPANDIDOS = "FILA_TRECHOS_EXPANDIDOS"; //novos trechos gerados para enviar para o googlemaps
	private static final String TOLERANCIA = "TOLERANCIA";
	private static final String CONT_REFINAMENTOS = "CONT_REFINAMENTOS";
	private static final String RESPOSTA_ANTERIOR = "RESPOSTA_ANTERIOR";
	/*teste*/private static final String TEMPO_INICIO = "TEMPO_INICIO";
	
	//par�metros
	private static /*final*/ double FATOR_TOLERANCIA = PropertiesLoader.getInt("fator_tol");
	private static final int MAX_REFINAMENTOS = PropertiesLoader.getInt("max_refinamentos");
	private static final int MAX_REQUISICOES_GM = PropertiesLoader.getInt("max_requisicoes_gm");
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*debug*/System.out.println("keyset: " + request.getParameterMap().keySet() + ", status: " + getStatusGM(request));
		
		try{
			boolean primeiraVez = getContRevisoes(request)==0 || !isLogicaRotasNaSessao(request);
			if(primeiraVez)
				primeiraExecucao(request, response);
			metodoPrincipal(request, response);
		}catch(Throwable e){
			e.printStackTrace();
			if(!response.isCommitted()){
				respostaErro(request, response);
				/*teste*/TesteCenariosRotas.setResult("erro", e.getClass() + " : " + e.getMessage());
				/*teste*/TesteCenariosRotas.salvar();
			}
			limpar(request,response);
		}
	}
	
	private void primeiraExecucao(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		/*teste*/FATOR_TOLERANCIA = TesteCenariosRotas.getFatTol();
		/*teste*/LogicaRotaSegura.loadParams();
		
		//inicializa��o dos objetos principais
		HttpSession sessao = request.getSession();
		/*teste*/sessao.setAttribute(TEMPO_INICIO, System.nanoTime());
		LogicaRotaSegura logicaRota = getLogicaRotaSegura(request, response);
		
		//pega a ROTA ORIGINAL que gerada pelo GoogleMaps, que vai da origem at� o destino
		Caminho rotaGoogleMaps = getRota(request);
		
		//inicializa o GRAFO com a rota original
		GrafoRotas grafo = new GrafoRotas(rotaGoogleMaps.getInicio(), rotaGoogleMaps.getFim(), logicaRota);
		logicaRota.setGrafoRotas(grafo);
		
		//inicializa a FILA DE PONTOS PARA EXPANDIR com a origem
		grafo.getFilaPontosParaExpandir().offer(grafo.getOrigem());
		
		//inicializa a FILA DE TRECHOS EXPANDIDOS
		Queue<SegmentoReta> filaTrechosExpandidos = new ArrayDeque<SegmentoReta>();
		sessao.setAttribute(FILA_TRECHOS_EXPANDIDOS, filaTrechosExpandidos);
		
		//define a TOLERANCIA
		double distReta = rotaGoogleMaps.distanciaRetaOD();
		double densMedia = logicaRota.getKernel().getMediaDens();
		double tolerancia = distReta * densMedia * FATOR_TOLERANCIA;
		sessao.setAttribute(TOLERANCIA, tolerancia);
		
		/*teste*///inicializa o teste (classe que exibe rotas, kernel, valores de perigo, etc) 
		/*teste*/TesteRotas.setLimitesGeral(grafo);
		/*teste*/TesteRotas.setLegendaGeral(tolerancia, distReta);
//		/*teste*/System.out.println("distReta: " + distReta);
		/*teste*/TesteCenariosRotas.setResult("distReta", distReta);
//		/*teste*/System.out.println("tolerancia: " + tolerancia);
		/*teste*/TesteCenariosRotas.setResult("tol", tolerancia);
	}
	
	private void metodoPrincipal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

			//inicializa��o dos objetos principais
			HttpSession sessao = request.getSession();
			LogicaRotaSegura logicaRota = getLogicaRotaSegura(request, response);
			StatusGM status = getStatusGM(request);
			GrafoRotas grafo = logicaRota.getGrafoRotas();
			Queue<Ponto> filaPontosParaExpandir = grafo.getFilaPontosParaExpandir(); //fila de pontos candidatos a expans�o
			Queue<SegmentoReta> filaTrechosExpandidos = (Queue<SegmentoReta>)sessao.getAttribute(FILA_TRECHOS_EXPANDIDOS); //fila de segmentos pra mandar pro GM (segmentos gerados por uma expans�o)
			double tolerancia = (Double)sessao.getAttribute(TOLERANCIA);
			
			//NOVO TRECHOGM E PONTO PRA EXPANDIR
			if(status == StatusGM.SUCCESS){
				//recebe a rota obtida do GoogleMaps
				Caminho rotaGoogleMaps = getRota(request);
				double custoGM = rotaGoogleMaps.distanciaPercorrida(); //TODO mudar isso, pegar o valor de tempo do GM
				double perigo = logicaRota.perigo(rotaGoogleMaps);
				RotaGM trechoGM = new RotaGM(rotaGoogleMaps, custoGM, perigo); 
				grafo.inserirRota(trechoGM);
				Ponto novoVertice = rotaGoogleMaps.getFim();
				if(!novoVertice.equals(grafo.getDestino()))
					filaPontosParaExpandir.offer(novoVertice);
				/*teste*/if(getContRevisoes(request) == 0 ){
				/*teste*/	double distIni = grafo.menorCaminho().distanciaPercorrida();
//				/*teste*/	System.out.println("dist�ncia inicial: " + distIni);
				/*teste*/	TesteCenariosRotas.setResult("distIni", distIni);
				/*teste*/	double qualiIni = grafo.desfav(grafo.getDestino());
//				/*teste*/	System.out.println("qualidade inicial: " + qualiIni);
				/*teste*/	TesteCenariosRotas.setResult("qualiIni", qualiIni);
				/*teste*/}
			}else if(status == StatusGM.TOO_MANY_QUERIES){
				respostaRepeteTrechoAnterior(request, response);
				return;
			}else{
				throw new AssertionError("requisicao ao GooglMaps: " + status);
			}
			
			//EXPANDIR
			double desfMelhorRota = grafo.desfav(grafo.getDestino()); //desfavorabilidade da menor rota encontrada at� agora
			boolean isToleravel = desfMelhorRota <= tolerancia;
			if( !isToleravel
//					&& !(filaPontosParaExpandir.isEmpty() && filaTrechosExpandidos.isEmpty())
					&& !filaPontosParaExpandir.isEmpty() 
					&& getContRevisoes(request) < MAX_REQUISICOES_GM){

				//EXPANDE novamente, se a fila de trechos estiver vazia
				/*teste*/boolean expandiu = false;
				while(filaTrechosExpandidos.isEmpty() && !filaPontosParaExpandir.isEmpty()){
					Ponto p = filaPontosParaExpandir.peek();
					/*teste*/if(p == null) throw new AssertionError("sem pontos pra expandir");

					//expande s� do v�rtice at� o destino primeiro
					SegmentoReta trecho = logicaRota.expandirProDestino(p);
					if(trecho != null){
						filaTrechosExpandidos.offer(trecho);
						/*teste*/expandiu = true;
					}
					
					//se a expans�o anterior n�o for novidade, ent�o expande para novos pontos 
					if(filaTrechosExpandidos.isEmpty()){
						p = filaPontosParaExpandir.poll();
						List<SegmentoReta> novosSegms = logicaRota.expandir(p);
						for(SegmentoReta novo : novosSegms){
							filaTrechosExpandidos.offer(novo);
							/*teste*/expandiu = true;
						}
					}
				}
				/*teste*/if(expandiu) TesteRotas.mostra("contrev: " + getContRevisoes(request), logicaRota.getKernel(), grafo);
			}
			
			//RESPOSTA
			if(!isToleravel){
				
				if(!filaTrechosExpandidos.isEmpty()){
					//continua calculando trechos
					SegmentoReta segm = filaTrechosExpandidos.poll();
					Caminho trecho = new Caminho(segm);
					/*teste*/if(segm == null) throw new AssertionError("trecho == null");
					respostaPedeNovoTrecho(request, response, trecho);
				}else{
					//entrou aqui pq a filaTrechosExpandidos era emtpy
					//TODO pode entrar aqui? (quase sempre entra)
					Error e = new AssertionError("nao tem mais pontos pra expandir"); 
					/*teste*/TesteCenariosRotas.setResult("erro", e.getClass() + " : " + e.getMessage());
					terminar(request, response);
					throw e;
				}
				
			}else{
				
//				//REFINA a rota, tirando pontas e arrodeios
//	//			Integer contRefinamentos = (Integer)sessao.getAttribute(CONT_REFINAMENTOS);
//	//			if(contRefinamentos == null) contRefinamentos = 0;
//				boolean precisouRefinar = false;
//	//			if(contRefinamentos < MAX_REFINAMENTOS)
//					precisouRefinar = LogicaRotaSegura.refinaRota(menorCaminhoVertices, menorCaminho);
//				if(precisouRefinar){
//					/*teste*/TesteRotas.mostra("Resposta refinada", logicaRota.getKernel(), menorCaminhoVertices);
//	//				sessao.setAttribute(CONT_REFINAMENTOS, contRefinamentos+1);
//	//				respostaPedeNovoTrecho(request, response, menorCaminhoVertices);
//				}else{
					terminar(request, response);
//				}
			}
			
	}
	
	private void terminar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		
		//inicializa��o dos objetos principais
		HttpSession sessao = request.getSession();
		LogicaRotaSegura logicaRota = getLogicaRotaSegura(request, response);
		GrafoRotas grafo = logicaRota.getGrafoRotas();
		
		//melhor rota segura encontrada
		Caminho menorCaminhoVertices = grafo.menorCaminhoVertices();
		Caminho menorCaminho = grafo.menorCaminho();
		/*teste*/TesteRotas.mostra("Resultado esperado", logicaRota.getKernel(), menorCaminho);
		/*teste*/TesteRotas.mostra("Resposta", logicaRota.getKernel(), menorCaminhoVertices);

		//manda a RESPOSTA FINAL e termina
		/*teste*/long t1 = (Long)sessao.getAttribute(TEMPO_INICIO);
		/*teste*/long t2 = System.nanoTime();
		/*teste*/double tempo = (t2-t1)/(Math.pow(10, 9));
//			/*teste*/System.out.println("TEMPO para calcular a rota: " + tempo + "s");
		/*teste*/TesteCenariosRotas.setResult("tempo", tempo);
		/*teste*/TesteRotas.mostra("grafo final", logicaRota.getKernel(), grafo);
//			/*teste*/SwingUtilities.invokeLater(new Runnable(){public void run() {
		limpar(request, response);
//			/*teste*/}});
		/*teste*/double distFin = grafo.menorCaminho().distanciaPercorrida();
//			/*teste*/System.out.println("dist�ncia final: " + distFin);
		/*teste*/TesteCenariosRotas.setResult("distFin", distFin);
		/*teste*/double qualiFin = grafo.desfav(grafo.getDestino());
//			/*teste*/System.out.println("qualidade final: " + qualiFin);
		/*teste*/TesteCenariosRotas.setResult("qualiFin", qualiFin);
//			/*teste*/System.out.println("requisicoes ao googlemaps: " + getContRevisoes(request));
		/*teste*/TesteCenariosRotas.setResult("reqGM", getContRevisoes(request));
		respostaFim(request, response, menorCaminhoVertices);
		/*teste*/TesteCenariosRotas.salvar();
		
	}
	
	/**
	 * Faz uma limpeza nos objetos da sess�o, preparando pra uma pr�xima s�rie de requisi��es do mesmo usu�rio
	 */
	private void limpar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		HttpSession sessao = request.getSession();
		LogicaRotaSegura logicaRotas = getLogicaRotaSegura(request, response);
		sessao.removeAttribute(TOLERANCIA);
		sessao.removeAttribute(FILA_TRECHOS_EXPANDIDOS);
		sessao.removeAttribute(LOGICA_ROTAS);
		logicaRotas.limpar();
		/*teste*/sessao.removeAttribute(CONT_REFINAMENTOS);
		/*teste*/sessao.removeAttribute(TEMPO_INICIO);
		/*teste*/TesteRotas.limpar();
	}
	
	private void respostaPedeNovoTrecho(HttpServletRequest request, HttpServletResponse response, Caminho trecho) throws IOException{
		trecho = trecho.invertida();
		request.getSession().setAttribute(RESPOSTA_ANTERIOR, trecho); //necess�rio caso de o erro TOO_MANY_QUERIES no GM
//		/*teste*/System.out.println("TRECHO: " + trecho);
		PrintWriter out = response.getWriter();
		out.println(trecho);	//rota segura gerada
		/*teste*/out.println("de=respostaPedeNovoTrecho");
		out.close();
	}
	
	private void respostaRepeteTrechoAnterior(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Caminho trecho = (Caminho)request.getSession().getAttribute(RESPOSTA_ANTERIOR);
		PrintWriter out = response.getWriter();
		out.println(trecho);	//rota segura gerada na vez anterior, qd deu TOO_MANY_QUERIES no GM
		/*teste*/out.println("de=respostaRepeteTrechoAnterior");
		out.close();
	}
	
	private void respostaFim(HttpServletRequest request, HttpServletResponse response, Caminho rota) throws IOException{
		/*teste*/if(rota == null) throw new AssertionError("rota == null");
		rota = rota.invertida();
		PrintWriter out = response.getWriter();
		out.println(rota);			//rota segura gerada
		out.println("fim");			//sinal pro fim
		/*teste*/System.out.println("FIM");
		/*teste*/out.println("de=respostaFim");
		out.close();
	}
	
	private void respostaErro(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		out.println("erro");			//rota segura gerada
		out.println("fim");			//sinal pro fim
		/*teste*/System.out.println("FIM ERRO");
		/*teste*/out.println("de=respostaErro");
		out.close();
	}
	
	private Caminho getRota(HttpServletRequest request) throws ServletException, IOException {
		String stringRotas = null;
		try{
			stringRotas = request.getParameter("rotas");
			Caminho rota = new Caminho(stringRotas).invertida();
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
	
	private int getContRevisoes(HttpServletRequest request){
		String str = request.getParameter("contRevisao");
		if(str == null)
			return 0;
		else
			return Integer.valueOf(str);
	}
	
	private LogicaRotaSegura getLogicaRotaSegura(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession sessao = request.getSession();
		if(!isLogicaRotasNaSessao(request)){ 
			//primeira requisi��o, com a rota original
//			Rectangle bounds = ServletKernelMap.getLimitesPixel(request);
//			KernelMap kernel = new ServletKernelMap().gerarKernelMap(bounds, request);
			KernelMap kernel = (KernelMap)sessao.getAttribute(KERNEL);
			if(kernel == null) throw new RuntimeException("kernel null");
			LogicaRotaSegura logicaRotas = new LogicaRotaSegura(kernel);
			sessao.setAttribute(LOGICA_ROTAS, logicaRotas);
		}
		LogicaRotaSegura logicaRotas = (LogicaRotaSegura)sessao.getAttribute(LOGICA_ROTAS);
		
		return logicaRotas;
	}
	
	private boolean isLogicaRotasNaSessao(HttpServletRequest request){
		HttpSession sessao = request.getSession();
		return sessao.getAttribute(LOGICA_ROTAS) != null && sessao.getAttribute(LOGICA_ROTAS) instanceof LogicaRotaSegura;
	}
	
	private StatusGM getStatusGM(HttpServletRequest request){
		String erro = request.getParameter("status");
		StatusGM status = null; 
		if(erro != null)
			status = StatusGM.getStatus(Integer.parseInt(erro));
		return status;
	}
}
