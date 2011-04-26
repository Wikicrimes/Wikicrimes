package org.wikicrimes.util.rotaSegura.googlemaps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.util.ServletUtil;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;

public class JSONRouteHandler {

	public static JSONObject getJSON(Rota rota, int zoom) throws IOException{
		URL url = ServiceURLBuilder.getUrlJSON(rota, zoom);
		String jsonStr = ServletUtil.requestText(url);
		JSONObject json = JSONObject.fromObject(jsonStr); 
		return json;
	}
	
	public static String getJSON(List<Rota> rotas, int zoom) {
		try {
		
			//cria um json q vai reunir todas as rotas
			JSONObject resJson = new JSONObject();
			resJson.put("status", "OK");
			JSONArray resRoutes = new JSONArray();
			
			for(Rota rota : rotas) {
				
				//pega JSON de cada rota (requisicao ao GM)
				URL urlGoogleAPI = ServiceURLBuilder.getUrlJSON(rota, zoom);
				String jsonStr = ServletUtil.requestText(urlGoogleAPI);
				
				//pega o 'route' e adiciona no json q reune todas as rotas
				JSONObject json = JSONObject.fromObject(jsonStr);

				//testa status da requisicao a Directions API
				StatusGMDirections status = getStatus(json); 
				if(status != StatusGMDirections.OK) {
					throw new DirectionsAPIRequestException(status);
				}
				
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
	
	public static Rota getRoute(JSONObject json, int zoom) throws DirectionsAPIRequestException{
		
		StatusGMDirections status = getStatus(json); 
		if(status != StatusGMDirections.OK) {
			throw new DirectionsAPIRequestException(status);
		}
		
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
	
	public static String convertToKML(JSONObject json) {
		
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
	
}
