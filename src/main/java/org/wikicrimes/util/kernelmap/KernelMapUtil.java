package org.wikicrimes.util.kernelmap;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.statistics.KernelMapRequestHandler;
import org.wikicrimes.util.statistics.WikiCrimesEventsRetriever;

public class KernelMapUtil {

	public static KernelMap fazerKernelMap(PontoLatLng centro, double raioKm, int zoom, CrimeService crimeService, Date dataInicial) {
		Rectangle boundsPixel = getBoundsPixel(centro, raioKm, zoom); 
		return fazerKernelMap(boundsPixel, zoom, crimeService, dataInicial);
	}
	
	public static KernelMap fazerKernelMap(Rectangle boundsPixel, int zoom, CrimeService crimeService, Date dataInicial) {
		List<Point> crimes = buscaCrimes(boundsPixel, zoom, crimeService, dataInicial);
		KernelMap kernel = new KernelMap(boundsPixel, crimes);
		return kernel;
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
	
	private static List<Point> buscaCrimes(Rectangle boundsPixel, int zoom, CrimeService crimeService, Date dataInicial){
		
		Point pixelNO = new Point((int)boundsPixel.getMinX(), (int)boundsPixel.getMinY());
		Point pixelSE = new Point((int)boundsPixel.getMaxX(), (int)boundsPixel.getMaxY());
		PontoLatLng latlngNO = PontoLatLng.fromPixel(pixelNO, zoom);
		PontoLatLng latlngSE = PontoLatLng.fromPixel(pixelSE, zoom);
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("norte", latlngNO.lat);
		params.put("sul", latlngSE.lat);
		params.put("leste", latlngSE.lng);
		params.put("oeste", latlngNO.lng);
		params.put("dataInicial", dataInicial);
		params.put("dataFinal", new Date());
		List<BaseObject> crimes = crimeService.filter(params);
		List<Point> crimesPixel = WikiCrimesEventsRetriever.toPixel(crimes, zoom);
		
		return crimesPixel;
	}
	
	
}
