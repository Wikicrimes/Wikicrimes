package org.wikicrimes.util.statistics;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.model.Relato;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.util.kernelmap.LatLngBoundsGM;
import org.wikicrimes.web.FiltroForm;

public class CrimeEventsRetriever extends EventsRetriever{

	
	private HttpServletRequest request;
	private CrimeService crimeService;

	public CrimeEventsRetriever(HttpServletRequest request, ServletContext context) {
		super();
		this.request = request;
		this.crimeService = getCrimeService(context);
	}

	public List<Point> getPoints(){
		
		HttpSession sessao = request.getSession();
		
		LatLngBoundsGM limitesLatlng = Param.getLimitesLatLng(request);
		int zoom = Param.getZoom(request);
		
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
		
		List<BaseObject> crimes = null;
		
		if (filtroForm == null) {
			filtroForm = new FiltroForm();
			filtroForm.setCrimeService(crimeService);
			
		} 
		crimes = filtroForm.getCrimesFiltrados(tipoCrime, tipoVitima,
					tipoLocal, horarioInicial, horarioFinal, dataInicial,
					dataFinal, entidadeCertificadora, confirmadosPositivamente,
					norte, sul, leste, oeste, ignoraData,null);		
		
//		/*TESTE*/params.put("maxResults", 100);
		List<Point> points = toPixel(crimes, zoom);
		
		return points;
	}
	
	protected CrimeService getCrimeService(ServletContext context){
		if(crimeService == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(context);
			crimeService = (CrimeService)springContext.getBean("crimeService");
		}
		return crimeService;
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
	
}
