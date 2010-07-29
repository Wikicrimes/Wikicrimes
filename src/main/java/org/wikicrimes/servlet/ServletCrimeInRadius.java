package org.wikicrimes.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		try {
			StringBuilder crimes = getService().getCrimesArea(lat, lng, 0.6, dateFormat.parse("1999/10/10").getTime(), new Date().getTime());
			
			PrintWriter writer = response.getWriter();
			writer.println(crimes);
			writer.close();			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	
	private CrimeService getService(){
		if(service == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			service = (CrimeService)springContext.getBean("crimeService");
		}
		return service;
	}
}