package org.wikicrimes.util.statistics;

import java.awt.Image;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wikicrimes.util.ServletUtil;
import org.wikicrimes.util.kernelmap.KernelMap;
import org.wikicrimes.util.statistics.Param.Action;

@SuppressWarnings("serial")
public class CrimeStatisticsServlet extends HttpServlet{

	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try{
			doPost(req, resp);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{

			ServletContext context = getServletContext();
			noCache(response);
			
			KernelMapRequestHandler kmHandler = new KernelMapRequestHandler(request, response, context);
			
			Action action = Param.getAction(request);
			switch(action) {
			case GERA_KERNEL:
				Image image = kmHandler.generateKernelMap();
				ServletUtil.sendImage(response, image);
				break;
			case PEGA_IMAGEM:
				Image image2 = kmHandler.getImageFromSession();
				ServletUtil.sendImage(response, image2);
				break;
			case PEGA_INFO:
				KernelMap kernel = kmHandler.getKernelMapFromSession();
				kmHandler.sendKernelMapInfo(kernel.getDensityGrid());
				break;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void noCache(HttpServletResponse response) {
		response.setContentType("text/plain");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setCharacterEncoding("iso-8859-1");
	}
	
	
}
