package org.wikicrimes.servlet;

import static org.wikicrimes.servlet.ServletRotaSeguraClientSide.atualizaFilaRotasPromissoras;
import static org.wikicrimes.servlet.ServletRotaSeguraClientSide.corrigirRota;
import static org.wikicrimes.servlet.ServletRotaSeguraClientSide.getLogicaRotaSegura;
import static org.wikicrimes.servlet.ServletRotaSeguraClientSide.respostaErro;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.util.ServletUtil;
import org.wikicrimes.util.kernelMap.PropertiesLoader;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.googlemaps.DirectionsAPI;
import org.wikicrimes.util.rotaSegura.googlemaps.FormatoResposta;
import org.wikicrimes.util.rotaSegura.googlemaps.StatusGMDirections;
import org.wikicrimes.util.rotaSegura.logica.CalculoPerigo;
import org.wikicrimes.util.rotaSegura.logica.LogicaRotaSegura;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaGM;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaPromissora;
import org.wikicrimes.util.rotaSegura.testes.TesteCenariosRotas;
import org.wikicrimes.util.rotaSegura.testes.TesteRotasImg;

@SuppressWarnings("serial")
public class ServletRotaSeguraServerSide extends HttpServlet{

	//PARAMETROS
	private static final int MAX_REQUISICOES_GM = PropertiesLoader.getInt("max_requisicoes_gm");
	private static final int NUM_ROTAS_RESPOSTA = PropertiesLoader.getInt("num_rotas_resposta");
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		/*DEBUG*/System.out.println("keyset: " + request.getParameterMap().keySet());
		
		try{
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
	
	@SuppressWarnings("unchecked")
	private void metodoPrincipal(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//INICIALIZACAO dos objetos principais
		Rota rotaGoogleMaps = ServletRotaSeguraClientSide.getRota(request);
		int zoom = Integer.valueOf(request.getParameter("zoom"));
		StatusGMDirections status = StatusGMDirections.OK;
		LogicaRotaSegura logicaRota = getLogicaRotaSegura(request);
		GrafoRotas grafo = new GrafoRotas(rotaGoogleMaps);
		logicaRota.setGrafo(grafo);
		CalculoPerigo calcPerigo = logicaRota.getCalculoPerigo();
		Rota rotaIdeal = null;
		Queue<Rota> rotasPromissoras = (Queue<Rota>)(Object)new PriorityQueue<RotaPromissora>();
		List<Rota> rotasNovas = new ArrayList<Rota>();
		JSONObject json = null;
		boolean terminado = false;
		boolean forcarTermino = false;

		
		/*teste*/long t1 = System.currentTimeMillis();
		/*teste*/calcPerigo.FATOR_TOLERANCIA = TesteCenariosRotas.getFatTol();
		
		for(int iteracao=0; !terminado; iteracao++){
			
			/*DEBUG*/System.out.println("#" + iteracao + ", " + status);
			
			//RECEBE rota do GoogleMaps, bota no grafo, trata erro
			switch(status){
			case OK:
				recebeRota(iteracao, rotaIdeal, rotaGoogleMaps, logicaRota, rotasNovas, rotasPromissoras);
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
		
			Rota melhorCaminho = grafo.melhorCaminho();
			
			/*TESTE*/TesteRotasImg teste = new TesteRotasImg(logicaRota.getKernel(), grafo);
//			/*TESTE*/teste.setTituloTesteCenarios(contReq + ", " + status);
			/*TESTE*/teste.setTitulo(iteracao + ", " + status);
			/*TESTE*/teste.addRota(melhorCaminho, new Color(0,150,0));
			/*TESTE*/teste.addRota(rotaGoogleMaps, Color.BLUE);
			/*TESTE*/if(iteracao != 0) teste.addRota(rotaIdeal, new Color(100,100,255));
			/*TESTE*/teste.salvar();
			
			terminado = calcPerigo.isToleravel(melhorCaminho) || iteracao >= MAX_REQUISICOES_GM || forcarTermino; 
			if(!terminado){
				if(status == StatusGMDirections.OK)
					for(Rota rota : rotasNovas)
						rotasPromissoras.addAll(logicaRota.criarAlternativas(rota, iteracao));
				if(!rotasPromissoras.isEmpty()){
					rotaIdeal = rotasPromissoras.poll();
					try{
						json = getJSONDirectionsAPI(rotaIdeal, zoom);
						status = getStatus(json);
						if(status == StatusGMDirections.OK)
							rotaGoogleMaps = getRota(json, zoom);
						else
							rotaGoogleMaps = null;
					}catch(IOException e){
						status = StatusGMDirections.UNKNOWN_ERROR;
						rotaGoogleMaps = null;
					}
				}
			}
			
			if(terminado || rotasPromissoras.isEmpty()){
				respostaFim(request, response, grafo.verticesKMenoresCaminhos(NUM_ROTAS_RESPOSTA), FormatoResposta.WIKICRIMES);
				limpar(request);
				if(!terminado){
					System.err.println("nao tem mais rotas alternativas");
					terminado = true;
				}
			}

			/*teste*/if(terminado){
			/*teste*/long t2 = System.currentTimeMillis();
			/*teste*/long tempo = (t2-t1)/1000;
			/*teste*/TesteCenariosRotas.setResult("tempo", tempo);
			/*teste*/double distFin = melhorCaminho.distanciaPercorrida();
			/*teste*/TesteCenariosRotas.setResult("distFin", distFin);
			/*teste*/double qualiFin = logicaRota.getCalculoPerigo().perigo(melhorCaminho);
			/*teste*/TesteCenariosRotas.setResult("qualiFin", qualiFin);
			/*teste*/TesteCenariosRotas.setResult("reqGM", iteracao);
			/*teste*/TesteCenariosRotas.salvar();
			/*teste*/}
			
		}
	}
	
	private void recebeRota(int contReq, Rota rotaIdeal, Rota rotaGoogleMaps,  
			LogicaRotaSegura logicaRota, List<Rota> rotasNovas, Queue<Rota> rotasPromissoras){
		GrafoRotas grafo = logicaRota.getGrafo();
		CalculoPerigo calcPerigo = logicaRota.getCalculoPerigo();
		
		if(contReq == 0){
			rotasNovas.add(rotaGoogleMaps);
		}else{
			corrigirRota(rotaGoogleMaps, rotaIdeal); //caso o inicio e/ou fim nao coincidam exatamente (mas sao proximos)
			rotasNovas.clear();
			rotasNovas.addAll(rotaGoogleMaps.dividirAprox(rotaIdeal.getPontosArray()));
		}
		for(int i=0; i<rotasNovas.size(); i++){
			Rota rota = rotasNovas.get(i);
			double custo = rota.distanciaPercorrida(); 
			double perigo = calcPerigo.perigo(rota);
			grafo.inserirCaminho(rota, custo, perigo);
			rotasNovas.set(i, new RotaGM(rota, custo, perigo));
		}
		atualizaFilaRotasPromissoras(rotasPromissoras, calcPerigo, grafo);
	}
	
	private JSONObject getJSONDirectionsAPI(Rota rota, int zoom) throws IOException{
		URL url = DirectionsAPI.getUrlJSON(rota, zoom);
		String json = ServletUtil.fazerRequisicao(url);
		return JSONObject.fromObject(json);
	}
	
	
	private Rota getRota(JSONObject json, int zoom){
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
		
		List<Point> pixels = PontoLatLng.toPixel(latlngs, zoom);
		return new Rota(pixels);
	}
	
	public static StatusGMDirections getStatus(JSONObject json){
		String statusStr = json.getString("status"); 
		return StatusGMDirections.getStatus(statusStr);
	}
	
	public static void respostaFim(HttpServletRequest request, HttpServletResponse response, 
			List<Rota> rotas, FormatoResposta formato) throws IOException{
		/*DEBUG*/if(rotas == null) throw new AssertionError("rotas == null");
		/*DEBUG*/if(rotas.isEmpty()) throw new AssertionError("rotas == empty");
		
		
		switch(formato){
		case WIKICRIMES:
			ServletRotaSeguraClientSide.respostaFim(request, response, rotas);
			return;
		case JSON:
			PrintWriter out = response.getWriter();
			int zoom = Integer.valueOf(request.getParameter("zoom"));
			Rota rota = rotas.get(0);
			URL urlGoogleAPI = DirectionsAPI.getUrlJSON(rota, zoom);
			String json = ServletUtil.fazerRequisicao(urlGoogleAPI);
			out.print(json);
			return;
		case KML:
			out = response.getWriter();
			zoom = Integer.valueOf(request.getParameter("zoom"));
			rota = rotas.get(0);
			urlGoogleAPI = DirectionsAPI.getUrlKML(rota, zoom);
			json = ServletUtil.fazerRequisicao(urlGoogleAPI);
			out.print(json);
			return;
		}
	}
	
//	private List<PontoLatLng> decodePoly(String encoded) {
//
//	    List<PontoLatLng> poly = new ArrayList<PontoLatLng>();
//	    int index = 0, len = encoded.length();
//	    int lat = 0, lng = 0;
//
//	    while (index < len) {
//	        int b, shift = 0, result = 0;
//	        do {
//	            b = encoded.charAt(index++) - 63;
//	            result |= (b & 0x1f) << shift;
//	            shift += 5;
//	        } while (b >= 0x20);
//	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//	        lat += dlat;
//
//	        shift = 0;
//	        result = 0;
//	        do {
//	            b = encoded.charAt(index++) - 63;
//	            result |= (b & 0x1f) << shift;
//	            shift += 5;
//	        } while (b >= 0x20);
//	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//	        lng += dlng;
//
//	        PontoLatLng p = new PontoLatLng(lat, lng);
//	        poly.add(p);
//	    }
//
//	    return poly;
//	}
	
	private void limpar(HttpServletRequest request) throws IOException, ServletException{
		HttpSession sessao = request.getSession();
		LogicaRotaSegura logicaRotas = getLogicaRotaSegura(request);
		logicaRotas.limpar();
		sessao.removeAttribute(ServletRotaSeguraClientSide.LOGICA_ROTAS);
		/*TESTE*/TesteRotasImg.limpar();
		System.gc();
	}
	
}
