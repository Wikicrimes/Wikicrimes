package org.wikicrimes.servlet;

import java.awt.Image;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.wikicrimes.util.ServletUtil;
import org.wikicrimes.util.kernelmap.KernelMap;
import org.wikicrimes.util.statistics.ChartRequestHandler;
import org.wikicrimes.util.statistics.CrimeStringBuilder;
import org.wikicrimes.util.statistics.KernelMapRequestHandler;
import org.wikicrimes.util.statistics.Param;
import org.wikicrimes.util.statistics.SessionBuffer;
import org.wikicrimes.util.statistics.WikiCrimesEventsRetriever;
import org.wikicrimes.util.statistics.Param.Actions;

@SuppressWarnings("serial")
public class StatisticsServlet extends HttpServlet{

	//TODO hierarquizar bottleneckfinder
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
			
			ServletUtil.noCache(response);
			ServletContext context = getServletContext();
			SessionBuffer sessionBuffer = new SessionBuffer(request);
			Actions actions = Param.getActions(request);
			
			if(actions.needEventsFromDB()) {
				boolean needPixel = actions.generateKernelMap();
				boolean needHistograms = actions.generateCharts();
				boolean needReports = actions.includeEventsInJson();
				WikiCrimesEventsRetriever events = new WikiCrimesEventsRetriever(request, context, needPixel, needHistograms, needReports);
				sessionBuffer.saveTotalEvents(events.getTotalEvents());
			
				if(actions.generateKernelMap()) {
					KernelMapRequestHandler kernelHandler = new KernelMapRequestHandler(request, events.getPoints());
					sessionBuffer.saveKernelMap(kernelHandler.getKernel(), kernelHandler.getImage());
				}
				
				if(actions.generateCharts()) {
					ChartRequestHandler chartHandler = new ChartRequestHandler(events);
					sessionBuffer.saveChartsUrls(chartHandler.getTypesChart(), chartHandler.getReasonsChart());
				}
				
				if(actions.includeEventsInJson()) {
					String crimesString = CrimeStringBuilder.buildString(events.getEvents());
					sessionBuffer.saveEvents(crimesString);
				}
			}
			
			if(actions.getJson()) {
				JSONObject json = generateJSON(sessionBuffer);
				ServletUtil.sendJson(response, json);
			}else if(actions.getKernelMapImage()){
				Image image = sessionBuffer.getKernelMapImage();
				ServletUtil.sendImage(response, image);
			}
			
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
		
		JSONObject kernelObj = new JSONObject();
		KernelMap kernel = ses.getKernelMap();
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

		json.put("events", ses.getEvents());
		
		return json;
	
	}
	
}
