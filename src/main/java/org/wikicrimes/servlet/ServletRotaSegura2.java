package org.wikicrimes.servlet;

import static org.wikicrimes.servlet.ServletKernelMap.KERNEL;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.wikicrimes.util.kernelMap.Caminho;
import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.Ponto;
import org.wikicrimes.util.kernelMap.PropertiesLoader;
import org.wikicrimes.util.kernelMap.StatusGM;
import org.wikicrimes.util.kernelMap.testes.TesteCenariosRotas;
import org.wikicrimes.util.rotaSegura.Grafo;
import org.wikicrimes.util.rotaSegura.LogicaRotaSegura;
import org.wikicrimes.util.rotaSegura.ParPontos;
import org.wikicrimes.util.rotaSegura.testes.TesteRotas;

/**
 * Trata requisições HTTP para calcular rotas seguras.
 * Chama um método do ServletKernelMap para calcular mapa de kernel.
 * 
 * @author victor
 */
public class ServletRotaSegura2 extends HttpServlet {
	
	//chaves para atributos de sessao
	private static final String LOGICA_ROTAS = "LOGICA_ROTAS";
	private static final String ULTIMA_RESPOSTA = "ULTIMA_RESPOSTA"; //ultima resposta deste servlet. Ela sera uma requisicao pro GoogleMaps
	private static final String FILA_CAMINHOS = "FILA_CAMINHOS"; //fila de requisições p/ fazer ao GoogleMaps (são rotas)
	private static final String FILAS_PONTOS = "FILAS_PONTOS"; //mapa de filas de PONTOS PROMISSORES para pares origem/destino de subproblemas
	
	//PARAMETROS
	private static final int MAX_REQUISICOES_GM = PropertiesLoader.getInt("max_requisicoes_gm");
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*debug*/System.out.println("keyset: " + request.getParameterMap().keySet() + ", status: " + getStatusGM(request));
		
		try{
			if(isPrimeiraVez(request))
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
		
		//inicialização dos objetos principais
		HttpSession sessao = request.getSession();
		LogicaRotaSegura logicaRota = getLogicaRotaSegura(request, response);
		sessao.setAttribute(FILA_CAMINHOS, new LinkedList<Caminho>());
		sessao.setAttribute(FILAS_PONTOS, new HashMap<ParPontos, Queue<Ponto>>());
		
		//inicializa o GRAFO com a rota original que vai da origem até o destino
		Caminho rotaGoogleMaps = getRota(request);
		Grafo grafo = new Grafo(rotaGoogleMaps.getInicio(), rotaGoogleMaps.getFim());
		logicaRota.setGrafo(grafo);
		
	}
	
	private void metodoPrincipal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		//inicialização dos objetos principais
		HttpSession sessao = request.getSession();
		LogicaRotaSegura logicaRota = getLogicaRotaSegura(request, response);
		StatusGM status = getStatusGM(request);
		Grafo grafo = logicaRota.getGrafo();
		Caminho ultimaResp = (Caminho)sessao.getAttribute(ULTIMA_RESPOSTA);
		Queue<Caminho> filaCaminhos = (Queue<Caminho>)sessao.getAttribute(FILA_CAMINHOS);
		Map<ParPontos, Queue<Ponto>> mapaFilasPontos = (Map<ParPontos, Queue<Ponto>>)sessao.getAttribute(FILAS_PONTOS);
		Caminho rotaGoogleMaps = null; 
		double perigo = 0;
		
		//NOVO TRECHOGM E PONTO PRA EXPANDIR
		if(status == StatusGM.SUCCESS){
			//recebe a rota obtida do GoogleMaps
			rotaGoogleMaps = getRota(request);
			List<Caminho> rotas = null;
			if(isPrimeiraVez(request)){
				rotas = new LinkedList<Caminho>();
				rotas.add(rotaGoogleMaps);
			}else{
				rotas = rotaGoogleMaps.dividirAprox(ultimaResp.getPontosArray());
			}
//			if(!logicaRota.isToleravel(rotaGoogleMaps)){
//				List<Caminho> rotas = logicaRota.separarPartesPerigosas(rotaGoogleMaps);
//			}
			for(Caminho rota : rotas){
				double custo = rotaGoogleMaps.distanciaPercorrida(); 
				perigo = logicaRota.perigo(rotaGoogleMaps);
				grafo.inserirCaminho(rota, custo, perigo);
			}
			/*TESTE*/TesteRotas teste = new TesteRotas(logicaRota.getKernel(), grafo);
			/*TESTE*/if(!isPrimeiraVez(request)) teste.setRotas(new Caminho(ultimaResp));
			/*TESTE*/teste.refresh();
		}else if(status == StatusGM.TOO_MANY_QUERIES){
			respostaRepeteTrechoAnterior(request, response);
			return;
		}else{
			throw new AssertionError("requisicao ao GooglMaps: " + status);
		}
		
		//RESPOSTA
		Caminho melhorCaminho = grafo.melhorCaminho();
		if(!logicaRota.isToleravel(melhorCaminho) && getContRevisoes(request) < MAX_REQUISICOES_GM){
			
			Ponto o = rotaGoogleMaps.getInicio();
			Ponto d = rotaGoogleMaps.getFim();
			Queue<Ponto> filaPontos = logicaRota.pontosPromissores(o, d, perigo);
			mapaFilasPontos.put(new ParPontos(o,d), filaPontos);
			for(int i=0; !filaPontos.isEmpty(); i++){
				Ponto p = filaPontos.poll();
				Caminho novoCaminho = new Caminho(o,p,d);
				filaCaminhos.add(novoCaminho);
			}
			
			respostaPedeNovoTrecho(request, response, filaCaminhos.poll());
			
		}else{
			respostaFim(request, response, melhorCaminho);
			limpar(request, response);
		}
			
	}
	
	/**
	 * Faz uma limpeza nos objetos da sessão, preparando pra uma próxima série de requisições do mesmo usuário
	 */
	private void limpar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		HttpSession sessao = request.getSession();
		LogicaRotaSegura logicaRotas = getLogicaRotaSegura(request, response);
		sessao.removeAttribute(LOGICA_ROTAS);
		logicaRotas.limpar();
	}
	
	private void respostaPedeNovoTrecho(HttpServletRequest request, HttpServletResponse response, Caminho trecho) throws IOException{
		request.getSession().setAttribute(ULTIMA_RESPOSTA, trecho); //necessário caso de o erro TOO_MANY_QUERIES no GM
		trecho = trecho.invertida();
		PrintWriter out = response.getWriter();
		out.println(trecho);	//rota segura gerada
		out.println("de=respostaPedeNovoTrecho");
		out.close();
	}
	
	private void respostaRepeteTrechoAnterior(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Caminho trecho = (Caminho)request.getSession().getAttribute(ULTIMA_RESPOSTA);
		trecho = trecho.invertida();
		PrintWriter out = response.getWriter();
		out.println(trecho);	//rota segura gerada na vez anterior, qd deu TOO_MANY_QUERIES no GM
		out.println("de=respostaRepeteTrechoAnterior");
		out.close();
	}
	
	private void respostaFim(HttpServletRequest request, HttpServletResponse response, Caminho rota) throws IOException{
		/*teste*/if(rota == null) throw new AssertionError("rota == null");
		rota = rota.invertida();
		PrintWriter out = response.getWriter();
		out.println(rota);			//rota segura gerada
		out.println("fim");			//sinal pro fim
		/*teste*/System.out.println("FIM");
		out.println("de=respostaFim");
		out.close();
	}
	
	private void respostaErro(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		out.println("erro");			//rota segura gerada
		out.println("fim");			//sinal pro fim
		/*teste*/System.out.println("FIM ERRO");
		out.println("de=respostaErro");
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
	
	private boolean isPrimeiraVez(HttpServletRequest request){
		return getContRevisoes(request)==0 || !isLogicaRotasNaSessao(request);
	}
	
	private LogicaRotaSegura getLogicaRotaSegura(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
