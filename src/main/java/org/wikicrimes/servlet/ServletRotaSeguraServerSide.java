package org.wikicrimes.servlet;

import static org.wikicrimes.servlet.ServletRotaSeguraClientSide.corrigirPontasDaRota;
import static org.wikicrimes.servlet.ServletRotaSeguraClientSide.respostaErro;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.util.DataUtil;
import org.wikicrimes.util.ServletUtil;
import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.KernelMapUtil;
import org.wikicrimes.util.kernelMap.PropertiesLoader;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.googlemaps.DirectionsAPI;
import org.wikicrimes.util.rotaSegura.googlemaps.FormatoResposta;
import org.wikicrimes.util.rotaSegura.googlemaps.StatusGMDirections;
import org.wikicrimes.util.rotaSegura.logica.CalculoPerigo;
import org.wikicrimes.util.rotaSegura.logica.FilaRotasCandidatas;
import org.wikicrimes.util.rotaSegura.logica.LogicaRotaSegura;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaGM;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas.NaoTemCaminhoException;
import org.wikicrimes.util.rotaSegura.testes.TesteRotasImg;


@SuppressWarnings("serial")
public class ServletRotaSeguraServerSide extends HttpServlet{

	//PARAMETROS
	private static final int MAX_REQUISICOES_GM = PropertiesLoader.getInt("max_requisicoes_gm");
	private static final int PADRAO_NUM_ROTAS_RESPOSTA = PropertiesLoader.getInt("padrao_num_rotas_resposta");
	private static final int PADRAO_ZOOM = PropertiesLoader.getInt("padrao_zoom_rota_segura");
	private static final int PADRAO_ANOS_ATRAS = PropertiesLoader.getInt("rotaSegura.padrao.anosAtrasDataInicial");
	
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
		}catch(Throwable e){
			e.printStackTrace();
			if(!response.isCommitted()){
				respostaErro(request, response);
//				/*teste*/TesteCenariosRotas.setResult("erro", e.getClass() + " : " + e.getMessage());
//				/*teste*/TesteCenariosRotas.salvar();
			}
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
		LogicaRotaSegura logicaRota = getLogicaRotaSegura(request);
		GrafoRotas grafo = new GrafoRotas(rotaGoogleMaps);
		logicaRota.setGrafo(grafo);
		CalculoPerigo calcPerigo = logicaRota.getCalculoPerigo();
		StatusGMDirections status = StatusGMDirections.OK;
		Rota rotaIdeal = null;
		FilaRotasCandidatas rotasCandidatas = new FilaRotasCandidatas(calcPerigo, grafo);
		List<Rota> rotasNovas = new ArrayList<Rota>();
		JSONObject json = null;
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
			} catch (NaoTemCaminhoException e1) {
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
						json = getJSONDirectionsAPI(rotaIdeal, zoom);
						status = getStatus(json);
						if(status == StatusGMDirections.OK) {
							rotaGoogleMaps = getRota(json, zoom);
						}else {
							rotaGoogleMaps = null;
						}
					}catch(IOException e){
						status = StatusGMDirections.UNKNOWN_ERROR;
						rotaGoogleMaps = null;
					}
				}
			}
			
			if(terminado || rotasCandidatas.isEmpty()){
				List<Rota> rotaResposta = grafo.verticesKMenoresCaminhos(numRotasResposta);
				respostaFim(request, response, rotaResposta, formato);
				limpar(request);
				if(!terminado){
					System.err.println("***nao tem mais rotas alternativas");
					terminado = true;
				}
			}

//			/*teste*/if(terminado){
//			/*teste*/long t2 = System.currentTimeMillis();
//			/*teste*/long tempo = (t2-t1)/1000;
//			/*teste*/TesteCenariosRotas.setResult("tempo", tempo);
//			/*teste*/double distFin = melhorCaminho.distanciaPercorrida();
//			/*teste*/TesteCenariosRotas.setResult("distFin", distFin);
//			/*teste*/double qualiFin = logicaRota.getCalculoPerigo().perigo(melhorCaminho);
//			/*teste*/TesteCenariosRotas.setResult("qualiFin", qualiFin);
//			/*teste*/TesteCenariosRotas.setResult("reqGM", iteracao);
//			/*teste*/TesteCenariosRotas.salvar();
//			/*teste*/}
			
		}
	}
	
	private void recebeRota(int contReq, Rota rotaIdeal, Rota rotaGoogleMaps,  
			LogicaRotaSegura logicaRota, List<Rota> rotasNovas, FilaRotasCandidatas rotasCandidatas){
		GrafoRotas grafo = logicaRota.getGrafo();
		CalculoPerigo calcPerigo = logicaRota.getCalculoPerigo();
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
			grafo.inserirCaminho(rota, custo, perigo);
			rotasNovas.set(i, new RotaGM(rota, custo, perigo));
		}
//		/*DEBUG*/TesteRotasImg.teste(rotasPromissoras, "recebeRotas, rotasPromissoras antes do refresh", logicaRota);
		rotasCandidatas.reponderar();
//		/*DEBUG*/TesteRotasImg.teste(rotasPromissoras, "recebeRotas, rotasPromissoras depois do refresh", logicaRota);
	}
	
	private static JSONObject getJSONDirectionsAPI(Rota rota, int zoom) throws IOException{
		URL url = DirectionsAPI.getUrlJSON(rota, zoom);
		String json = ServletUtil.fazerRequisicao(url);
		return JSONObject.fromObject(json);
	}
	
	
	private static Rota getRota(JSONObject json, int zoom){
		List<PontoLatLng> latlngs = new ArrayList<PontoLatLng>();
		JSONArray routes = json.getJSONArray("routes");
		
		//primeiro ponto
		JSONObject start = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).
							getJSONArray("steps").getJSONObject(0).getJSONObject("start_location");
		PontoLatLng latlngStart = new PontoLatLng(start.getDouble("lat"), start.getDouble("lng")); 
		latlngs.add(latlngStart);
		
		//pontos restantes
		for(Object routeObj : routes){
			JSONObject route = (JSONObject)routeObj;
			JSONArray legs = route.getJSONArray("legs");
			for(Object legObj : legs){
				JSONObject leg = (JSONObject)legObj;
				JSONArray steps = leg.getJSONArray("steps");
				for(Object stepObj : steps){
					JSONObject step = (JSONObject)stepObj; 
					JSONObject end = step.getJSONObject("end_location");
					PontoLatLng latlngEnd = new PontoLatLng(end.getDouble("lat"), end.getDouble("lng")); 
					latlngs.add(latlngEnd);
				}
			}
		}
		
		List<Ponto> pixels = PontoLatLng.toPixel(latlngs, zoom);
		return new Rota(pixels);
	}
	
	public static StatusGMDirections getStatus(JSONObject json){
		String statusStr = json.getString("status"); 
		return StatusGMDirections.getStatus(statusStr);
	}
	
	public int getNumRotasResposta(HttpServletRequest request) throws ServletException, IOException{
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
			Ponto origem = new PontoLatLng(request.getParameter("origem")).toPixel(zoom);
			Ponto destino = new PontoLatLng(request.getParameter("destino")).toPixel(zoom);
			Rota retaOD = new Rota(origem, destino); 
			return getRota(getJSONDirectionsAPI(retaOD, zoom), zoom);
		
		//sem parametro de rota
		}else{
			throw new InvalidParameterException("a requisicao nao tem os parametros 'origem' e 'destino' ou nao tem o parametro 'rota'");
		}
	}
	
	public LogicaRotaSegura getLogicaRotaSegura(HttpServletRequest request) throws ServletException, IOException{
		
		//chamada vinda do wikicrimes
		FormatoResposta formato = FormatoResposta.getFormatoResposta(request.getParameter("output")); 
		if(formato == FormatoResposta.LATLNGS) {
			return ServletRotaSeguraClientSide.getLogicaRotaSegura(request);
			
		//chamada externa ao servico de rotas 
		}else {
			int zoom = getZoom(request);
			PontoLatLng origem = new PontoLatLng(request.getParameter("origem"));
			PontoLatLng destino = new PontoLatLng(request.getParameter("destino"));
			double distancia = PontoLatLng.distanciaKM(origem,destino);
			PontoLatLng centro = PontoLatLng.medio(origem, destino);
			Date dataInicial = DataUtil.moveData(new Date(), 0, 0, -PADRAO_ANOS_ATRAS);
			KernelMap kernel = KernelMapUtil.fazerKernelMap(centro, distancia*2, zoom, getCrimeService(), dataInicial);
			LogicaRotaSegura logicaRotas = new LogicaRotaSegura(kernel);
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
			String json = constroiJSONRotas(rotas, zoom);
			out.println(json);
			return;
		case KML:
//			response.setContentType("text/xml");
			response.setContentType("application/vnd.google-earth.kml+xml");
			out = response.getWriter();
			zoom = getZoom(request);
			String kml = constroiKMLRotas(rotas, zoom);
			out.println(kml);
			return;
		}
		
//		/*DEBUG*/System.out.println("FIM");
	}
	
	private static String constroiJSONRotas(List<Rota> rotas, int zoom) {
		try {
		
			//cria um json q vai reunir todas as rotas
			JSONObject resJson = new JSONObject();
			resJson.put("status", "OK");
			JSONArray resRoutes = new JSONArray();
			
			for(Rota rota : rotas) {
				
				//pega JSON de cada rota (requisicao ao GM)
				URL urlGoogleAPI = DirectionsAPI.getUrlJSON(rota, zoom);
				String jsonStr = ServletUtil.fazerRequisicao(urlGoogleAPI);
				
				//pega o 'route' e adiciona no json q reune todas as rotas
				JSONObject json = JSONObject.fromObject(jsonStr);
				JSONArray routes = json.getJSONArray("routes");
				JSONObject route = routes.getJSONObject(0);
				resRoutes.add(route);
			}
			
			resJson.put("routes", resRoutes);
			
			//retorna o json com todas as rotas 
			return resJson.toString();
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String constroiKMLRotas(List<Rota> rotas, int zoom) {
		try {
			
			StringBuilder s = new StringBuilder();
			s.append("<?xml version='1.0' encoding='UTF-8'?>");
			s.append("<kml xmlns='http://earth.google.com/kml/2.0'>");
			s.append("<Document>");
			s.append("<name></name>");
			s.append("<Style id='roadStyle'>");
			s.append("<LineStyle>");
			s.append("<color>7fcf0064</color>");
			s.append("<width>6</width>");
			s.append("</LineStyle>");
			s.append("</Style>");
			
			for(Rota rota : rotas) {
				URL urlGoogleAPI = DirectionsAPI.getUrlJSON(rota, zoom);
				String jsonStr = ServletUtil.fazerRequisicao(urlGoogleAPI);
				JSONObject json = JSONObject.fromObject(jsonStr);
				s.append(rotaJsonToKml(json));
			}
			
			s.append("</Document>");
			s.append("</kml>");
			
			return s.toString();
			
//		} catch (JDOMException e) {
//			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String rotaJsonToKml(JSONObject json) {
		
		StringBuilder s = new StringBuilder();
		s.append("<Placemark>");
		s.append("<name>Route</name>");
		s.append("<description></description>");
		s.append("<GeometryCollection>");
		s.append("<LineString>");
		s.append("<coordinates>");
		
		JSONArray routes = json.getJSONArray("routes");
		
		//primeiro ponto
		JSONObject start = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).
							getJSONArray("steps").getJSONObject(0).getJSONObject("start_location");
		s.append(start.getDouble("lng") + "," + start.getDouble("lat") + ",0.000000 ");
		
		//pontos restantes
		for(Object routeObj : routes){
			JSONObject route = (JSONObject)routeObj;
			JSONArray legs = route.getJSONArray("legs");
			for(Object legObj : legs){
				JSONObject leg = (JSONObject)legObj;
				JSONArray steps = leg.getJSONArray("steps");
				for(Object stepObj : steps){
					JSONObject step = (JSONObject)stepObj; 
					JSONObject end = step.getJSONObject("end_location");
					s.append(end.getDouble("lng") + "," + end.getDouble("lat") + ",0.000000 "); 
				}
			}
		}
		
		s.append("</coordinates>");
		s.append("</LineString>");
		s.append("</GeometryCollection>");
		s.append("<styleUrl>#roadStyle</styleUrl>");
		s.append("</Placemark>");

		return s.toString();
	}
	
	private void limpar(HttpServletRequest request) throws IOException, ServletException{
		HttpSession sessao = request.getSession();
		LogicaRotaSegura logicaRotas = getLogicaRotaSegura(request);
		logicaRotas.limpar();
		sessao.removeAttribute(ServletRotaSeguraClientSide.LOGICA_ROTAS);
//		/*TESTE*/TesteRotasImg.limpar();
		System.gc();
	}

	private CrimeService crimeService;
	protected CrimeService getCrimeService(){
		if(crimeService == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			crimeService = (CrimeService)springContext.getBean("crimeService");
		}
		return crimeService;
	}
	
}
