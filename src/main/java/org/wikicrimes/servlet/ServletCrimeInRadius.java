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
import org.wikicrimes.service.CrimeService;

public class ServletCrimeInRadius extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CrimeService service;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lng = Double.parseDouble(request.getParameter("lng"));
		long initDateMilliSeconds = Long.parseLong(request.getParameter("initDate"));
		double radius = Double.parseDouble(request.getParameter("radius"));
		
		StringBuilder crimes = getService().getCrimesArea(lat, lng, radius, initDateMilliSeconds, new Date().getTime());

		PrintWriter writer = response.getWriter();
		writer.println(crimes);
		writer.close();			
	}
	
	private CrimeService getService(){
		if(service == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			service = (CrimeService)springContext.getBean("crimeService");
		}
		return service;
	}
}