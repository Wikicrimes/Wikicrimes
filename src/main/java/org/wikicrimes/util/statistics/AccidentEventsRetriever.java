package org.wikicrimes.util.statistics;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.Acidente;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.service.AcidenteService;
import org.wikicrimes.util.kernelmap.LatLngBoundsGM;


public class AccidentEventsRetriever extends EventsRetriever<Acidente>{

	AcidenteService service;
	Rectangle bounds;
	int zoom;
	Date initialDate;
	List<Point> points;
	List<Acidente> events;
	
	public AccidentEventsRetriever(ServletContext context, Rectangle bounds, int zoom, Date initialDate) {
		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(context);
		service = (AcidenteService)springContext.getBean("acidenteService");
		this.bounds = bounds;
		this.zoom = zoom;
		this.initialDate = initialDate;
		load();
	}
	
	public AccidentEventsRetriever(HttpServletRequest request, ServletContext context) {
		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(context);
		service = (AcidenteService)springContext.getBean("acidenteService");
		zoom = Param.getZoom(request);
		LatLngBoundsGM latlngbounds = Param.getLatLngBounds(request);
		bounds = latlngbounds.toPixel(zoom);
		load();
	}
	
	private void load() {
		Point pixelNO = new Point((int)bounds.getMinX(), (int)bounds.getMinY());
		Point pixelSE = new Point((int)bounds.getMaxX(), (int)bounds.getMaxY());
		PontoLatLng latlngNO = PontoLatLng.fromPixel(pixelNO, zoom);
		PontoLatLng latlngSE = PontoLatLng.fromPixel(pixelSE, zoom);
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("norte", latlngNO.lat);
		params.put("sul", latlngSE.lat);
		params.put("leste", latlngSE.lng);
		params.put("oeste", latlngNO.lng);
		params.put("dataInicial", initialDate);
		params.put("dataFinal", new Date());
		events =  service.filter(params);
	}
	
	@Override
	public List<Acidente> getEvents() {
		return events;
	}
	
	@Override
	public List<Point> getPoints() {
		if(points == null) {
			points = toPixel(events, zoom);
		}
		return points;
	}
	
	@Override
	public int getTotalEvents() {
		return events.size();
	}
	
	public static List<Point> toPixel(List<Acidente> events, int zoom){
		List<Point> pontos = new ArrayList<Point>();
		for(Acidente o : events){
			PontoLatLng p = null;
			Acidente c = (Acidente)o;
			p = new PontoLatLng(c.getLatitude(), c.getLongitude());
			pontos.add(p.toPixel(zoom));
		}
		return pontos;
	}
	
}
