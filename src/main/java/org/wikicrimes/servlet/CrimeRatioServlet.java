package org.wikicrimes.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.GoogleEnderecoService;
import org.wikicrimes.util.GoogleMapsData;
/**
 * Tendo uma coordenada de lat e long, vc pegara todos os crimes de um dado raio
 * passado... Obj gerar uma circunferencia virtual que mapei esses pontos...retorna um dado no formato XML!
 * 
 * @author philipp
 *
 */
public class CrimeRatioServlet extends HttpServlet {

	private static final long serialVersionUID = 3220671688900369774L;
	private GoogleEnderecoService ges = new GoogleEnderecoService();

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
			
			String serv = request.getParameter("serv");
			
			if(serv != null && serv.equalsIgnoreCase("disp")){
				return;
			}
			
			String latStr = request.getParameter("lat"),longStr = request.getParameter("long");
			String end = request.getParameter("end");
			
			double latitude = 0.0;
			double longitude = 0.0;
			
			if(latStr.equalsIgnoreCase("vaz") && longStr.equalsIgnoreCase("vaz")){
				GoogleMapsData coord = ges.consultaRuaURL(end);
				
				latitude=coord.getLatitude();
				longitude=coord.getLongitude();
				
			}else{
				//a lat existe
				latitude = Double.parseDouble(latStr);
				longitude = Double.parseDouble(longStr);
				
			}
			
			/*
			System.out.println("End: "+end);
			System.out.println("Lat: "+latitude);
			System.out.println("long: "+longitude);*/
			
			double raio = Double.parseDouble(request.getParameter("raio"));
			long dataIni = Long.parseLong(request.getParameter("dIni"));
			long dataFim = Long.parseLong(request.getParameter("dFim"));
			
			
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			CrimeService crimeService = (CrimeService)springContext.getBean("crimeService");
			
			Map <String,Integer> mapa = crimeService.numeroCrimesArea(latitude, longitude, raio,dataIni,dataFim);
			
			parsingToXML(mapa,response);
			
			System.out.println(mapa);
			
		}catch(NumberFormatException e){
			errorXML(response);
		} catch (IOException e) {
			errorXML(response);
		} catch (XMLStreamException e) {
			errorXML(response);
		} catch (FactoryConfigurationError e) {
			errorXML(response);
		}
	}
	
	private void errorXML(HttpServletResponse response){
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
		sbXML.append("<homicidio>"+mapa.get("Homicídio")+"</homicidio>");
		sbXML.append("<outros>"+mapa.get("Outros")+"</outros>");
		sbXML.append("</crimes>");
		
		PrintWriter out = response.getWriter();
		out.println(sbXML.toString());
		out.close();
	}
}
