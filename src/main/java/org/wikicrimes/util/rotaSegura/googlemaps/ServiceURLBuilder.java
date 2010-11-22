package org.wikicrimes.util.rotaSegura.googlemaps;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.util.rotaSegura.geometria.Rota;

public class ServiceURLBuilder {

	private static URL getUrl(Rota rota, int zoom, String formato) throws MalformedURLException{
		List<PontoLatLng> pontos = PontoLatLng.fromPixel(rota.getPontos(), zoom);
		StringBuilder waypoints = new StringBuilder(); //do segundo ate o penultimo ponto
		for(int i=1; i<pontos.size()-1; i++)
			waypoints.append("(" + pontos.get(i).toString() + ")|");
		if(waypoints.length() > 0)
			waypoints.deleteCharAt(waypoints.length()-1);
		return new URL("http://maps.google.com/maps/api/directions/"+formato+"?" +
				"origin=(" + pontos.get(0) + ")&" +
				"destination=(" + pontos.get(pontos.size()-1) + ")&" +
				"waypoints=" + waypoints.toString() + "&" +
				"language=pt-BR&" +
				"sensor=false");
	}
	
	public static URL getUrlJSON(Rota rota, int zoom) throws MalformedURLException{
		return getUrl(rota, zoom, "json");
	}
	
	public static URL getUrlXML(Rota rota, int zoom) throws MalformedURLException{
		return getUrl(rota, zoom, "xml");
	}
	
	public static URL getUrlKML(Rota rota, int zoom) throws MalformedURLException{
		if(rota.size() < 2) throw new AssertionError("rota nao pode ter menos de 2 pontos");
		List<PontoLatLng> pontos = PontoLatLng.fromPixel(rota.getPontos(), zoom);
		StringBuilder to = new StringBuilder(); //do terceiro ate o ultimo ponto
		for(int i=2; i<pontos.size(); i++)
			to.append("+to:(" + pontos.get(i).toString() + ")");
//		URL url = new URL("http://maps.google.com/maps?f=d&source=s_d&mra=dpe&mrcr=0&mrsp=2&via=1,2&sll=-3.730653,-38.555832&sspn=0.078797,0.168056&ie=UTF8&t=h"
//				+ "&hl=pt-BR"
//				+ "&z=" + zoom + "&sz=" + 13
//				+ "&saddr=(" + pontos.get(0) + ")"
//				+ "&daddr=(" + pontos.get(1) + ")"
//				+ to.toString() + "&output=kml"); 
		URL url = new URL("http://maps.google.com/maps?" +
				"hl=pt-BR" +
				"&saddr=(" + pontos.get(0) + ")" +
				"&daddr=(" + pontos.get(1) + ")" + to +
				"&output=kml");
//		/*DEBUG*/System.out.println(url.toString());
		return url; 
	}
}
