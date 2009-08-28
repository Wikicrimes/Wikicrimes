package org.wikicrimes.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.service.CrimeService;
/**
 * Tendo uma coordenada de lat e long, vc pegara todos os crimes de um dado raio
 * passado... Obj gerar uma circunferencia virtual que mapei esses pontos...retorna um dado no formato XML!
 * 
 * @author philipp
 *
 */
public class CrimeRatioServlet extends HttpServlet {

	private static final long serialVersionUID = 3220671688900369774L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		
		contaCrimesArea(request,resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		contaCrimesArea(req,resp);
	}
	
	private void contaCrimesArea(HttpServletRequest request,HttpServletResponse response){
		try{
			double latitude = Double.parseDouble(request.getParameter("lat"));
			double longitude = Double.parseDouble(request.getParameter("long"));
			double raio = Double.parseDouble(request.getParameter("raio"));
			
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			CrimeService crimeService = (CrimeService)springContext.getBean("crimeService");
			
			Map <String,Integer> mapa = crimeService.numeroCrimesArea(latitude, longitude, raio);
			
			parsingToXML(mapa,response);
			
			System.out.println(mapa);
			
		}catch(NumberFormatException e){
			
			StringBuilder error = new StringBuilder();
			error.append("<?xml version='1.0' encoding='UTF-8'?>");
			error.append("<crimes>");
			error.append("<errors>Error</errors>");
			error.append("</crimes>");
			
			PrintWriter out;
			try {
				out = response.getWriter();
				out.println(error.toString());
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parsingToXML(Map <String,Integer> mapa,HttpServletResponse response) throws IOException{
		
		response.setContentType("text/xml; charset=iso-8859-1");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setCharacterEncoding("iso-8859-1");
		
		StringBuilder sbXML = new StringBuilder();
		
		sbXML.append("<?xml version='1.0' encoding='UTF-8'?>");
		sbXML.append("<crimes>");
		sbXML.append("<furto>"+mapa.get("Furto")+"</furto>");
		sbXML.append("<roubo>"+mapa.get("Roubo")+"</roubo>");
		sbXML.append("<latrocinio>"+mapa.get("Latrocinio")+"</latrocinio>");
		sbXML.append("<hominicidio>"+mapa.get("Homicídio")+"</hominicidio>");
		sbXML.append("<outros>"+mapa.get("Outros")+"</outros>");
		sbXML.append("</crimes>");
		
		PrintWriter out = response.getWriter();
		out.println(sbXML.toString());
		out.close();
	}
}
