package org.wikicrimes.util.kernelmap;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.util.rotaSegura.ParametroInvalidoRotaSeguraException;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.statistics.AccidentEventsRetriever;
import org.wikicrimes.util.statistics.WikiCrimesEventsRetriever;

public class KernelMapUtil {

	public static KernelMap fazerKernelMap(PontoLatLng centro, double raioKm, int zoom, ServletContext context, String eventType, Date dataInicial) {
		Rectangle boundsPixel = getBoundsPixel(centro, raioKm, zoom); 
		List<Point> events = getEvents(boundsPixel, zoom, context, dataInicial, eventType);
		return new KernelMap(boundsPixel, events);
	}
	
	public static Rectangle getBoundsPixel(PontoLatLng centro, double raioKm, int zoom) {
		PontoLatLng no = centro.transladarKm(-raioKm, -raioKm);
		Ponto noPixel = no.toPixel(zoom);
		PontoLatLng se = centro.transladarKm(raioKm, raioKm);
		Ponto sePixel = se.toPixel(zoom);
		return new Rectangle(noPixel.x, noPixel.y, sePixel.x-noPixel.x, sePixel.y-noPixel.y);
	}
	
	public static Rectangle getBoundsPixel(PontoLatLng centro, int width, int height, int zoom) {
		Point centroPixel = centro.toPixel(zoom);
		Point cantoPixel = new Point(centroPixel.x - width/2, centroPixel.y - height/2);
		return new Rectangle(cantoPixel.x, cantoPixel.y, width, height);
	}
	
	public static List<Point> getEvents(Rectangle boundsPixel, int zoom, ServletContext context, Date dataInicial, String eventType) {
		List<Point> points;
		if(eventType == null || eventType.equals("") || eventType.equalsIgnoreCase("crime")) { //default
			WikiCrimesEventsRetriever er = new WikiCrimesEventsRetriever(boundsPixel, zoom, dataInicial, context);
			points = er.getPoints();
		}else if(eventType.equalsIgnoreCase("acidente")) {
			AccidentEventsRetriever er = new AccidentEventsRetriever(context, boundsPixel, zoom, dataInicial);
			points = er.getPoints();
		}else if(eventType.equalsIgnoreCase("crime+acidente")) {
			WikiCrimesEventsRetriever er1 = new WikiCrimesEventsRetriever(boundsPixel, zoom, dataInicial, context);
			AccidentEventsRetriever er2 = new AccidentEventsRetriever(context, boundsPixel, zoom, dataInicial);
			points = er1.getPoints();
			points.addAll(er2.getPoints());
		}else {
			throw new ParametroInvalidoRotaSeguraException("Parametro events invalido. Valores possiveis: crime, acidente e crime+acidente. Valor passado: " + eventType);
		}
		return points;
	}
	
}
