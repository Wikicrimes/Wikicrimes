package org.wikicrimes.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.FonteExterna;
import org.wikicrimes.service.EstatisticaExternaService;

public class ServletEstatisticaExterna extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private EstatisticaExternaService service;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		try{
			String acao = "";
			String resposta = "";
			acao = request.getParameter("acao");
			if(!acao.equals("ld")){	
				//Dados recebidos
				String mes = "";
				String tipoCrime = "";
				double lat = Double.parseDouble(request.getParameter("lat"));
				double lng = Double.parseDouble(request.getParameter("lng"));
				if(request.getParameter("mes") != null) mes = request.getParameter("mes");
				if(request.getParameter("tp") != null) tipoCrime = request.getParameter("tp");
				resposta = getService().getEstatisticaExternaResposta(mes,lng,lat,tipoCrime);
			}else{
				double lat = Double.parseDouble(request.getParameter("lat"));
				double lng = Double.parseDouble(request.getParameter("lng"));
				double raio = Double.parseDouble(request.getParameter("raio"));
				List<FonteExterna> fontesExternas = getService().getDelegacias(lat, lng, raio);
				resposta="{ \"delegacias\" : [";
				int cont = 0;
				for (FonteExterna fonteExterna : fontesExternas) {
					String nomeDp = fonteExterna.getNome();
					nomeDp=nomeDp.replace("Ocorrências Mensais -", " ");
					nomeDp=nomeDp.replace("2011"," ");
					resposta += ((cont!=0)?",":"")+"{\"nome\":\""+nomeDp+"\", \"lat\":"+fonteExterna.getLatitude()+", \"lng\":"+fonteExterna.getLongitude()+"}";	
					cont++;
				}
				resposta+="]}";
			}
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
