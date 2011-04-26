package org.wikicrimes.util.statistics;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.CrimeRazao;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.RelatoRazao;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.util.Util;
import org.wikicrimes.util.kernelmap.LatLngBoundsGM;
import org.wikicrimes.web.FiltroForm;

public class WikiCrimesEventsRetriever extends EventsRetriever<BaseObject>{

	private Map<String,Integer> typeHistogram;
	private Map<String,Integer> reasonHistogram;
	private int totalEvents;
	private int totalReasons;
	
	private HttpServletRequest request;
	private CrimeService crimeService;
	
	private boolean includePoints;
	private boolean includeHistograms;

	public WikiCrimesEventsRetriever(HttpServletRequest request, ServletContext context) {
		super();
		this.request = request;
		this.crimeService = getCrimeService(context);
		load();
	}
	
	public WikiCrimesEventsRetriever(HttpServletRequest request, ServletContext context, boolean includePoints, boolean includeHistograms) {
		this(request, context);
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
			countTypesAndReasons(events);
		}
	}
	
	private List<BaseObject> retrieveEvents(boolean includeReasons){
		HttpSession sessao = request.getSession();
		
		LatLngBoundsGM limitesLatlng = Param.getLatLngBounds(request);
		
		FiltroForm filtroForm = (FiltroForm) sessao.getAttribute("filtroForm");
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
		
		List<BaseObject> events = null;
		
		if (filtroForm == null) {
			filtroForm = new FiltroForm();
			filtroForm.setCrimeService(crimeService);
			
		}
		
		if(includeReasons) {
			events = filtroForm.filterCrimesIncludeReasons(tipoCrime, tipoVitima,
					tipoLocal, horarioInicial, horarioFinal, dataInicial,
					dataFinal, entidadeCertificadora, confirmadosPositivamente,
					norte, sul, leste, oeste, ignoraData,null);
		}else {
			events = filtroForm.getCrimesFiltrados(tipoCrime, tipoVitima,
					tipoLocal, horarioInicial, horarioFinal, dataInicial,
					dataFinal, entidadeCertificadora, confirmadosPositivamente,
					norte, sul, leste, oeste, ignoraData,null);
		}
		
		return events;
	}
	
	public static List<Point> toPixel(List<BaseObject> crimes, int zoom){
		List<Point> pontos = new ArrayList<Point>();
		for(BaseObject o : crimes){
			PontoLatLng p = null;
			if(o instanceof Crime){
				Crime c = (Crime)o;
				p = new PontoLatLng(c.getLatitude(), c.getLongitude());
			}else if(o instanceof Relato){
				Relato r = (Relato)o;
				p = new PontoLatLng(r.getLatitude(), r.getLongitude());
			}else{
				continue;
			}
			pontos.add(p.toPixel(zoom));
		}
		return pontos;
	}
	
	private void countTypesAndReasons(List<BaseObject> events) {
		typeHistogram = new HashMap<String, Integer>();
		reasonHistogram = new HashMap<String, Integer>();
		for(BaseObject event : events) {
			String type;
			Set<String> reasons = new HashSet<String>();
			if(event instanceof Crime){
				Crime c = (Crime)event;
				type = c.getTipoCrime().getDescricao();
				for(CrimeRazao cr : c.getRazoes()) {
					reasons.add(cr.getRazao().getDescricao());
				}
			}else if(event instanceof Relato){
				Relato r = (Relato)event;
				type = r.getTipoRelato();
				for(RelatoRazao rr : r.getRazoes()) {
					reasons.add(rr.getRazao().getDescricao());
				}
			}else{
				throw new AssertionError("nao eh Crime nem Relato?");
			}
			
			Integer tc = typeHistogram.get(type);
			if(tc == null) {
				tc = 0;
				typeHistogram.put(type, tc);
			}
			typeHistogram.put(type, ++tc);
			for(String reason : reasons) {
				Integer rc = reasonHistogram.get(reason);
				if(rc == null) {
					rc = 0;
					reasonHistogram.put(reason, rc);
				}
				reasonHistogram.put(reason, ++rc);
			}
		}
		totalReasons = Util.sum(reasonHistogram.values());
	}
	
	private CrimeService getCrimeService(ServletContext context){
		if(crimeService == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(context);
			crimeService = (CrimeService)springContext.getBean("crimeService");
		}
		return crimeService;
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
