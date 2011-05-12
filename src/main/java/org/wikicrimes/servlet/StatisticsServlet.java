package org.wikicrimes.servlet;

import java.awt.Image;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.util.ServletUtil;
import org.wikicrimes.util.TimeTest;
import org.wikicrimes.util.kernelmap.KernelMap;
import org.wikicrimes.util.statistics.ChartRequestHandler;
import org.wikicrimes.util.statistics.CrimeStringBuilder;
import org.wikicrimes.util.statistics.EventsRetriever;
import org.wikicrimes.util.statistics.KernelMapRequestHandler;
import org.wikicrimes.util.statistics.Param;
import org.wikicrimes.util.statistics.SessionBuffer;
import org.wikicrimes.util.statistics.WikiCrimesEventsRetriever;
import org.wikicrimes.util.statistics.Param.Actions;

@SuppressWarnings("serial")
public class StatisticsServlet extends HttpServlet{

	//TODO ajeitar bottleneckfinder (sem hierarquia)
	//TODO medir tempos denovo
	//TODO ajeitar contornos
	//TODO pegar do properties no chartrequesthandler
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		main(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		main(request, response);
	}
	
	private void main(HttpServletRequest request, HttpServletResponse response){
		try{
			
//			/*DEBUG*/TimeTest.start();
			
			ServletUtil.noCache(response);
			ServletContext context = getServletContext();
			SessionBuffer sessionBuffer = new SessionBuffer(request);
			Actions actions = Param.getActions(request);
//			/*DEBUG*/TimeTest.saveTime("init");
			
			if(actions.needEventsFromDB()) {
				boolean needHistograms = actions.generateCharts();
				boolean needReports = actions.sendEvents();
				EventsRetriever events = new WikiCrimesEventsRetriever(request, context, needHistograms, needReports);
//				EventsRetriever events = new AccidentEventsRetriever(request, context);
				sessionBuffer.saveTotalEvents(events.getTotalEvents());
//				/*DEBUG*/TimeTest.saveTime("consulta BD");
			
				if(actions.generateKernelMap()) {
					KernelMapRequestHandler kernelHandler = new KernelMapRequestHandler(request, events.getPoints());
					sessionBuffer.saveKernelMap(kernelHandler.getKernel(), kernelHandler.getImage());
//					/*DEBUG*/TimeTest.saveTime("kernel");
				}
				
				if(actions.generateCharts() && events instanceof WikiCrimesEventsRetriever) {
					ChartRequestHandler chartHandler = new ChartRequestHandler((WikiCrimesEventsRetriever)events);
					sessionBuffer.saveChartsUrls(chartHandler.getTypesChart(), chartHandler.getReasonsChart());
//					/*DEBUG*/TimeTest.saveTime("charts");
				}
				
				if(actions.sendEvents()) {
					String crimesString = CrimeStringBuilder.buildString(events.getEvents());
					sessionBuffer.saveEvents(crimesString);
//					/*DEBUG*/TimeTest.saveTime("events string");
				}
			}
			
			if(actions.getJson()) {
				JSONObject json = generateJSON(sessionBuffer);
				ServletUtil.sendJson(response, json);
//				/*DEBUG*/TimeTest.saveTime("send json");
			}else if(actions.getKernelMapImage()){
				Image image = sessionBuffer.getKernelMapImage();
				ServletUtil.sendImage(response, image);
//				/*DEBUG*/TimeTest.saveTime("send image");
			}
			
//			/*DEBUG*/TimeTest.printTimes();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JSONObject generateJSON(SessionBuffer ses) {

		JSONObject json = new JSONObject();
		json.put("totalEvents", ses.getTotalEvents());
		
		JSONObject chartsObj = new JSONObject();
		chartsObj.put("types", ses.getTypesChartUrl());
		chartsObj.put("reasons", ses.getReasonsChartUrl());
		json.put("charts", chartsObj);
		
		KernelMap kernel = ses.getKernelMap();
		if(kernel != null) {
			JSONObject kernelObj = new JSONObject();
			kernelObj.put("bandwidth", kernel.getBandwidth());
			kernelObj.put("nodeSize", kernel.getNodeSize());
			kernelObj.put("rows", kernel.getRows());
			kernelObj.put("cols", kernel.getCols());
			kernelObj.put("maxDensity", kernel.getMaxDens());
			kernelObj.put("avgDensity", kernel.getMediaDens());
			kernelObj.put("minDensity", kernel.getMinDens());
			kernelObj.put("booleanGrid", kernel.booleanGrid());
	//		kernelObj.put("grid", kernel.getDensityGrid());
	//		kernelObj.put("image", ses.getKernelMapImage());
			json.put("kernel", kernelObj);
		}

		if(ses.getCenter() != null) {
			JSONObject centerObj = new JSONObject();
			PontoLatLng center = ses.getCenter();
			centerObj.put("lat", center.lat.doubleValue());
			centerObj.put("lng", center.lng.doubleValue());
			json.put("center", centerObj);
		}
		
		json.put("events", ses.getEvents());
		
		return json;
	
	}
	
}
