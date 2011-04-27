package org.wikicrimes.util.statistics;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.Relato;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.RazaoService;
import org.wikicrimes.util.Util;
import org.wikicrimes.util.kernelmap.LatLngBoundsGM;
import org.wikicrimes.web.FiltroForm;

public class WikiCrimesEventsRetriever extends EventsRetriever<BaseObject>{

	private Map<String,Integer> typeHistogram;
	private Map<String,Integer> reasonHistogram;
	private int totalEvents;
	private int totalReasons;
	
	private HttpServletRequest request;
	private ServletContext context;
	private HttpSession session;
	
	private boolean includePoints;
	private boolean includeHistograms;

	public WikiCrimesEventsRetriever(HttpServletRequest request, ServletContext context) {
		this.request = request;
		this.context = context;
		this.session = request.getSession();
		load();
	}
	
	public WikiCrimesEventsRetriever(HttpServletRequest request, ServletContext context, boolean includePoints, boolean includeHistograms) {
		this.request = request;
		this.context = context;
		this.session = request.getSession();
		this.includePoints = includePoints;
		this.includeHistograms = includeHistograms;
		load();
	}
	
	private void load() {
		events = retrieveEvents(includeHistograms); 
		this.totalEvents = events.size();
		if(includePoints) {
			int zoom = Param.getZoom(request);
			this.points = toPixel(events, zoom);
		}
		if(includeHistograms) {
			buildHistograms();
		}
	}
	
	private List<BaseObject> retrieveEvents(boolean includeReasons){
		
		LatLngBoundsGM limitesLatlng = Param.getLatLngBounds(request);
		
		FiltroForm filtroForm = getFiltroForm();
		String tipoCrime = request.getParameter("tc");
		String tipoVitima = request.getParameter("tv");
		String tipoLocal = request.getParameter("tl"); // Data - Ex.: 01,01,2008
		String dataInicial = request.getParameter("di");
		String dataFinal = request.getParameter("df"); // Horario - Ex.: 5
		String horarioInicial = request.getParameter("hi");
		String horarioFinal = request.getParameter("hf");
		String entidadeCertificadora = request.getParameter("ec");
		String confirmadosPositivamente = request.getParameter("cp");
		String norte = limitesLatlng.norte+"";
		String sul = limitesLatlng.sul+"";
		String leste = limitesLatlng.leste+"";
		String oeste = limitesLatlng.oeste+"";
		String ignoraData = request.getParameter("id");
		
		List<BaseObject> events = filtroForm.getCrimesFiltrados(tipoCrime, tipoVitima,
				tipoLocal, horarioInicial, horarioFinal, dataInicial,
				dataFinal, entidadeCertificadora, confirmadosPositivamente,
				norte, sul, leste, oeste, ignoraData,null);
		
		return events;
	}
	
	public static List<Point> toPixel(List<BaseObject> crimes, int zoom){
		List<Point> pontos = new ArrayList<Point>();
		for(BaseObject o : crimes){
			PontoLatLng p = null;
			Crime c = (Crime)o;
			p = new PontoLatLng(c.getLatitude(), c.getLongitude());
			pontos.add(p.toPixel(zoom));
		}
		return pontos;
	}
	
	private void buildHistograms() {
		typeHistogram = new HashMap<String, Integer>();
		reasonHistogram = new HashMap<String, Integer>();
		countTypesAndReasons();
		totalReasons = Util.sum(reasonHistogram.values());
		replaceIdsByNames();
	}
	
	private void countTypesAndReasons() {
		for(BaseObject event : events) {
			
			Crime c = (Crime)event;
			String[] attrStr = c.getCacheEstatisticas().split("\\|");
			
			if(attrStr.length > 0) {
				String type = attrStr[0];
				increment(typeHistogram, type);
			}

			if(attrStr.length > 2) {
				String[] reasons = attrStr[2].split(",");
				for(String reason : reasons) {
					increment(reasonHistogram, reason);
				}
			}
		}
	}
	
	private void increment(Map<String,Integer> histogram, String key) {
		Integer count = histogram.get(key);
		if(count == null) {
			count = 0;
			histogram.put(key, count);
		}
		histogram.put(key, ++count);
	}
	
	private void replaceIdsByNames() {
		
		Map<Long,String> allTypes = getAllTypes();
		Map<String,Integer> newTypeHistogram = new HashMap<String, Integer>();
		for(String oldKey : typeHistogram.keySet()) {
			int count = typeHistogram.get(oldKey);
			Long id = Long.valueOf(oldKey);
			String name = allTypes.get(id);
			newTypeHistogram.put(name, count);
		}
		typeHistogram = newTypeHistogram;
		
		Map<Long,String> allReasons = getAllReasons();
		Map<String,Integer> newReasonHistogram = new HashMap<String, Integer>();
		for(String oldKey : reasonHistogram.keySet()) {
			int count = reasonHistogram.get(oldKey);
			Long id = Long.valueOf(oldKey);
			String name = allReasons.get(id);
			newReasonHistogram.put(name, count);
		}
		reasonHistogram = newReasonHistogram;
	}
	
	private Map<Long,String> getAllReasons(){
		Map<Long,String> map = new HashMap<Long, String>();
		RazaoService service = getRazaoSevice();
		for(BaseObject o : service.getAll()) {
			Razao r = (Razao)o;
			map.put(r.getIdRazao(), r.getDescricao());
		}
		return map;
	}
	
	private Map<Long,String> getAllTypes(){
		Map<Long,String> map = new HashMap<Long, String>();
		map.put(1L, "Tentativa de Roubo");
		map.put(2L, "Tentativa de Furto");
		map.put(3L, "Furto");
		map.put(4L, "Roubo");
		map.put(5L, "Violência");
		return map;
	}
	
	private FiltroForm getFiltroForm() {
		FiltroForm filtroForm = (FiltroForm) session.getAttribute("filtroForm");
		if (filtroForm == null) {
			filtroForm = new FiltroForm();
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(context);
			CrimeService service = (CrimeService)springContext.getBean("crimeService");
			filtroForm.setCrimeService(service);
		}
		return filtroForm;
	}
	
	private RazaoService getRazaoSevice() {
		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(context);
		RazaoService service = (RazaoService)springContext.getBean("razaoService");
		return service;
	}
	
	public List<Point> getPoints(){
		return points; 
	}
	
	public List<BaseObject> getEvents(){
		return events; 
	}

	public Map<String, Integer> getTypeHistogram() {
		return typeHistogram;
	}

	public Map<String, Integer> getReasonHistogram() {
		return reasonHistogram;
	}

	public int getTotalEvents() {
		return totalEvents;
	}
	
	public int getTotalReasons() {
		return totalReasons;
	}

}
