package org.wikicrimes.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.MobileRequestLog;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.LogService;
import org.wikicrimes.util.kernelMap.AvaliacaoPerigo;

public class ServletCrimeInRadius extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CrimeService service;
	private LogService logService;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lng = Double.parseDouble(request.getParameter("lng"));
		long initDateMilliSeconds = Long.parseLong(request.getParameter("initDate"));
		long finalDateMilliSeconds = Long.parseLong(request.getParameter("finalDate"));
		double radius = Double.parseDouble(request.getParameter("radius"));
		
		StringBuilder crimes = getService().getCrimesInRadius(lat, lng, radius, initDateMilliSeconds, finalDateMilliSeconds);
		double perigo;
		if(radius > 0.8f) {
			AvaliacaoPerigo avaliacaoPerigo = new AvaliacaoPerigo(getService());
			perigo = avaliacaoPerigo.avaliarCirculo(new PontoLatLng(lat, lng), radius, new Date(initDateMilliSeconds));
		} else {
			perigo = -1;
		}
		
		String userAgent = request.getHeader("User-Agent");
		MobileRequestLog mobileRequestLog = new MobileRequestLog(new Date(System.currentTimeMillis()), userAgent);
		getLogService().save(mobileRequestLog);
		
		PrintWriter writer = response.getWriter();
		writer.println(perigo + ";;;" + crimes);
		writer.close();
	}
	
	private CrimeService getService(){
		if(service == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			service = (CrimeService)springContext.getBean("crimeService");
		}
		return service;
	}
	
	private LogService getLogService(){
		if(logService == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			logService = (LogService)springContext.getBean("logService");
		}
		return logService;
	}
}