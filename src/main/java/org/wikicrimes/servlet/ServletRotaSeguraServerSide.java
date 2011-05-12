package org.wikicrimes.servlet;

import static org.wikicrimes.servlet.ServletRotaSeguraClientSide.corrigirPontasDaRota;
import static org.wikicrimes.servlet.ServletRotaSeguraClientSide.respostaErro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.util.DataUtil;
import org.wikicrimes.util.kernelmap.KernelMap;
import org.wikicrimes.util.kernelmap.KernelMapUtil;
import org.wikicrimes.util.kernelmap.PropertiesLoader;
import org.wikicrimes.util.rotaSegura.ParametroInvalidoRotaSeguraException;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.googlemaps.DirectionsAPIRequestException;
import org.wikicrimes.util.rotaSegura.googlemaps.FormatoResposta;
import org.wikicrimes.util.rotaSegura.googlemaps.JSONRouteHandler;
import org.wikicrimes.util.rotaSegura.googlemaps.KMLRouteHandler;
import org.wikicrimes.util.rotaSegura.googlemaps.StatusGMDirections;
import org.wikicrimes.util.rotaSegura.logica.FilaRotasCandidatas;
import org.wikicrimes.util.rotaSegura.logica.Perigo;
import org.wikicrimes.util.rotaSegura.logica.SafeRouteCalculator;
import org.wikicrimes.util.rotaSegura.logica.exceptions.CantFindPath;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaGM;


@SuppressWarnings("serial")
public class ServletRotaSeguraServerSide extends HttpServlet{

	//PARAMETROS
	private static final int MAX_REQUISICOES_GM = PropertiesLoader.getInt("saferoutes.max_gm_requests");
	private static final int PADRAO_NUM_ROTAS_RESPOSTA = PropertiesLoader.getInt("saferoutes.default_number_of_alternatives");
	private static final int PADRAO_ZOOM = PropertiesLoader.getInt("saferoutes.default_zoom");
	private static final int PADRAO_ANOS_ATRAS = PropertiesLoader.getInt("saferoutes.default_start_date");
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
//		/*DEBUG*/System.out.println("keyset: " + request.getParameterMap().keySet());
		
		try{
			metodoPrincipal(request, response);
		}catch(ParametroInvalidoRotaSeguraException e){
			e.printStackTrace();
			respostaErro(request, response, e.getMessage());
		}catch(DirectionsAPIRequestException e){
			e.printStackTrace();
			respostaErro(request, response, e.getMessage());
		}catch(Throwable e){
			e.printStackTrace();
			if(!response.isCommitted()){
				respostaErro(request, response);
//				/*teste*/TesteCenariosRotas.setResult("erro", e.getClass() + " : " + e.getMessage());
//				/*teste*/TesteCenariosRotas.salvar();
			}
		}finally {
			limpar(request);
		}
	}
	
	private void metodoPrincipal(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//INICIALIZACAO dos objetos principais
		int zoom = getZoom(request);
		int numRotasResposta = getNumRotasResposta(request);
		Rota rotaGoogleMaps = getRota(request);
		FormatoResposta formato = FormatoResposta.getFormatoResposta(request.getParameter("output"));
		SafeRouteCalculator logicaRota = getLogicaRotaSegura(request);
		Perigo calcPerigo = logicaRota.getCalculoPerigo();
		GrafoRotas grafo = new GrafoRotas(rotaGoogleMaps, calcPerigo);
		logicaRota.setGrafo(grafo);
		StatusGMDirections status = StatusGMDirections.OK;
		Rota rotaIdeal = null;
		FilaRotasCandidatas rotasCandidatas = new FilaRotasCandidatas(calcPerigo, grafo);
		List<Rota> rotasNovas = new ArrayList<Rota>();
		boolean terminado = false;
		boolean forcarTermino = false;

		
//		/*teste*/long t1 = System.currentTimeMillis();
//		/*teste*/calcPerigo.FATOR_TOLERANCIA = TesteCenariosRotas.getFatTol();
		
		for(int iteracao=0; !terminado; iteracao++){
			
//			/*DEBUG*/System.out.println("#" + iteracao + ", " + status);
			
			//RECEBE rota do GoogleMaps, bota no grafo, trata erro
			switch(status){
			case OK:
				recebeRota(iteracao, rotaIdeal, rotaGoogleMaps, logicaRota, rotasNovas, rotasCandidatas);
				break;
			case UNKNOWN_ERROR: //o ponto promissor era no meio do mar, por exemplo
				//nao recebe a rota, apenas passa pro proximo ponto promissor
				break;
			case OVER_QUERY_LIMIT:
				forcarTermino = true;
				break;
			default: //status nao previsto
				throw new AssertionError(status);
			}
		
			boolean rotaEhToleravel;
			try {
				Rota melhorCaminho = grafo.melhorCaminho();
//				/*TESTE*/TesteRotasImg teste = new TesteRotasImg(logicaRota.getKernel(), grafo);
//	//			/*TESTE*/teste.setTituloTesteCenarios(contReq + ", " + status);
//				/*TESTE*/teste.setTitulo(iteracao + ", " + status);
//				/*TESTE*/teste.addRota(melhorCaminho, new Color(0,150,0));
//				/*TESTE*/teste.addRota(rotaGoogleMaps, Color.BLUE);
//				/*TESTE*/if(iteracao != 0) teste.addRota(rotaIdeal, new Color(100,100,255));
//				/*TESTE*/teste.salvar();
				rotaEhToleravel = calcPerigo.isToleravel(melhorCaminho);
			} catch (CantFindPath e1) {
				rotaEhToleravel = false;
			}
			terminado = rotaEhToleravel || iteracao >= MAX_REQUISICOES_GM || forcarTermino;
			
			if(!terminado){
				if(status == StatusGMDirections.OK) {
					for(Rota rota : rotasNovas) {
						Queue<Rota> alternativas = logicaRota.criarAlternativas(rota, iteracao);
						rotasCandidatas.addAll(alternativas);
					}
				}
				if(!rotasCandidatas.isEmpty()){
					rotaIdeal = rotasCandidatas.pop();
					try{
//						JSONObject json = getJSONDirectionsAPI(rotaIdeal, zoom);
//						status = getStatus(json);
//						rotaGoogleMaps = getRota(json, zoom);
						Document kml = KMLRouteHandler.getKML(rotaIdeal, zoom);
						status = KMLRouteHandler.getStatus(kml);
						rotaGoogleMaps = KMLRouteHandler.getRoute(kml, zoom);
					}catch(IOException e){
						status = StatusGMDirections.UNKNOWN_ERROR;
						rotaGoogleMaps = null;
					}catch(DirectionsAPIRequestException e2) {
						rotaGoogleMaps = null;
					}
				}
			}
			
			if(terminado || rotasCandidatas.isEmpty()){
//				double maxWeight = calcPerigo.perigo(grafo.getRotaOriginal());
				List<Rota> rotaResposta = grafo.verticesKMenoresCaminhos(numRotasResposta);
//				/*TESTE CENARIO*/if(terminado){
//				/*TESTE CENARIO*/long t2 = System.currentTimeMillis();
//				/*TESTE CENARIO*/long tempo = (t2-t1)/1000;
//				/*TESTE CENARIO*/TesteCenariosRotas.setResult("tempo", tempo);
//				/*TESTE CENARIO*/double distFin = melhorCaminho.distanciaPercorrida();
//				/*TESTE CENARIO*/TesteCenariosRotas.setResult("distFin", distFin);
//				/*TESTE CENARIO*/double qualiFin = logicaRota.getCalculoPerigo().perigo(melhorCaminho);
//				/*TESTE CENARIO*/TesteCenariosRotas.setResult("qualiFin", qualiFin);
//				/*TESTE CENARIO*/TesteCenariosRotas.setResult("reqGM", iteracao);
//				/*TESTE CENARIO*/TesteCenariosRotas.salvar();
//				/*TESTE CENARIO*/}
				respostaFim(request, response, rotaResposta, formato);
				if(!terminado){
					System.err.println("***nao tem mais rotas alternativas");
					terminado = true;
				}
			}

			
		}
	}
	
	private void recebeRota(int contReq, Rota rotaIdeal, Rota rotaGoogleMaps,  
			SafeRouteCalculator logicaRota, List<Rota> rotasNovas, FilaRotasCandidatas rotasCandidatas){
		GrafoRotas grafo = logicaRota.getGrafo();
		Perigo calcPerigo = logicaRota.getCalculoPerigo();
		rotasNovas.clear();
		if(contReq == 0){
			rotasNovas.add(rotaGoogleMaps);
		}else{
			corrigirPontasDaRota(rotaGoogleMaps, rotaIdeal); //caso o inicio e/ou fim nao coincidam exatamente (mas sao proximos)
			List<Rota> rotaGoogleMapsDividida = rotaGoogleMaps.dividirAprox(rotaIdeal.getPontosArray());
			rotasNovas.addAll(rotaGoogleMapsDividida);
		}
		for(int i=0; i<rotasNovas.size(); i++){
			Rota rota = rotasNovas.get(i);
			double custo = rota.distanciaPercorrida(); 
			double perigo = calcPerigo.perigo(rota);
			grafo.inserirRota(rota, custo, perigo);
			rotasNovas.set(i, new RotaGM(rota, custo, perigo));
		}
		rotasCandidatas.reponderar();
	}
	
	private int getNumRotasResposta(HttpServletRequest request) throws ServletException, IOException{
		String nrotas = request.getParameter("nrotas");
		if(nrotas != null) {
			int num = Integer.valueOf(nrotas);
			if(num > 0) {
				return num;
			}
		}
		return PADRAO_NUM_ROTAS_RESPOSTA;
	}
	
	public static int getZoom(HttpServletRequest request) throws ServletException, IOException{
		String zoom = request.getParameter("zoom");
		if(zoom != null) {
			int num = Integer.valueOf(zoom);
			//TODO testar maximo e minimo (botar esses valores no ConstantesGM)
			return num;
		}
		return PADRAO_ZOOM;
	}
	
	public static Rota getRota(HttpServletRequest request) throws ServletException, IOException{
		
		//chamada vinda do wikicrimes (lista de pontos em pixel)
		if(request.getParameter("rota") != null){
			return ServletRotaSeguraClientSide.getRota(request);
			
		//chamada externa ao servico de rotas (origem e destino em latlng)
		}else if(request.getParameter("origem") != null && request.getParameter("destino") != null){
			int zoom = getZoom(request);
			Rota retaOD;
			try {
				Ponto origem = new PontoLatLng(request.getParameter("origem")).toPixel(zoom);
				Ponto destino = new PontoLatLng(request.getParameter("destino")).toPixel(zoom);
				retaOD = new Rota(origem, destino);
			}catch (Exception e) {
				throw new ParametroInvalidoRotaSeguraException("Valores invalidos em pelo menos um dos parametros 'origem' e 'destino'");
			}
			Document kml = KMLRouteHandler.getKML(retaOD, zoom);
			return KMLRouteHandler.getRoute(kml, zoom);
		
		//sem parametro de rota
		}else{
			throw new ParametroInvalidoRotaSeguraException("A requisicao nao tem os parametros 'origem' e 'destino'");
		}
	}
	
	public SafeRouteCalculator getLogicaRotaSegura(HttpServletRequest request) throws ServletException, IOException{
		
		//chamada vinda do wikicrimes
		FormatoResposta formato = FormatoResposta.getFormatoResposta(request.getParameter("output")); 
		if(formato == FormatoResposta.LATLNGS) {
			return ServletRotaSeguraClientSide.getLogicaRotaSegura(request);
			
		//chamada externa ao servico de rotas 
		}else {
			int zoom = getZoom(request);
			PontoLatLng origem = new PontoLatLng(request.getParameter("origem"));
			PontoLatLng destino = new PontoLatLng(request.getParameter("destino"));
			double distancia = PontoLatLng.distanceKM(origem,destino);
			PontoLatLng centro = PontoLatLng.medio(origem, destino);
			Date dataInicial = DataUtil.moveData(new Date(), 0, 0, -PADRAO_ANOS_ATRAS);
			String eventos = request.getParameter("eventos");
			KernelMap kernel = KernelMapUtil.fazerKernelMap(centro, distancia*2, zoom, getServletContext(), eventos, dataInicial);
			SafeRouteCalculator logicaRotas = new SafeRouteCalculator(kernel);
			return logicaRotas;
		}
	}
	
	public static void respostaFim(HttpServletRequest request, HttpServletResponse response, 
			List<Rota> rotas, FormatoResposta formato) throws IOException, ServletException{
		/*DEBUG*/if(rotas == null) throw new AssertionError("rotas == null");
		/*DEBUG*/if(rotas.isEmpty()) throw new AssertionError("rotas == empty");
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		
		switch(formato){
		case LATLNGS:
			ServletRotaSeguraClientSide.respostaFim(request, response, rotas);
			return;
		case JSON:
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			int zoom = getZoom(request);
			String json = JSONRouteHandler.getJSON(rotas, zoom);
			out.println(json);
			return;
		case KML:
//			response.setContentType("text/xml");
			response.setContentType("application/vnd.google-earth.kml+xml");
			zoom = getZoom(request);
			Document kml = KMLRouteHandler.getKML(rotas, zoom);
			XMLOutputter xmlOut = new XMLOutputter();
			xmlOut.output(kml, response.getWriter());
			return;
		}
		
//		/*DEBUG*/System.out.println("FIM");
	}
	
	private void limpar(HttpServletRequest request) throws IOException, ServletException{
		HttpSession sessao = request.getSession();
		SafeRouteCalculator logicaRotas = getLogicaRotaSegura(request);
		logicaRotas.limpar();
		sessao.removeAttribute(ServletRotaSeguraClientSide.LOGICA_ROTAS);
//		/*TESTE*/TesteRotasImg.limpar();
		System.gc();
	}
	
}
