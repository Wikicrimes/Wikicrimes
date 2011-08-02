package org.wikicrimes.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.service.EstatisticaExternaService;

public class ServletEstatisticaExterna extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private EstatisticaExternaService service;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		try{		
			//Dados recebidos
			String mes = "";
			String tipoCrime = "";
			double lat = Double.parseDouble(request.getParameter("lat"));
			double lng = Double.parseDouble(request.getParameter("lng"));
			if(request.getParameter("mes") != null) mes = request.getParameter("mes");
			if(request.getParameter("tp") != null) tipoCrime = request.getParameter("tp");
			String resposta = getService().getEstatisticaExternaResposta(mes,lng,lat,tipoCrime);
			
			response.getWriter().write(resposta);
		}catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	private EstatisticaExternaService getService(){
		if(service == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			service = (EstatisticaExternaService)springContext.getBean("estatisticaExternaService");
		}
		return service;
	}
	
}
