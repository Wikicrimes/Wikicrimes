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
import org.wikicrimes.util.statistics.Param.Action;

@SuppressWarnings("serial")
public class StatisticsServlet extends HttpServlet{

	//TODO o filtro ta trazendo 600 as vezes e 13000 outras vezes
	
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
			
			ServletUtil.noCache(response);
			ServletContext context = getServletContext();
			SessionBuffer sessionBuffer = new SessionBuffer(request);
			Action action = Param.getAction(request);
			
			switch(action) {
			case FULL_STATISTICS:
				WikiCrimesEventsRetriever events = new WikiCrimesEventsRetriever(request, context, true, true);
				KernelMapRequestHandler kernelHandler = new KernelMapRequestHandler(request, events.getPoints());
				ChartRequestHandler chartHandler = new ChartRequestHandler(events);
				sessionBuffer.saveTotalEvents(events.getTotalEvents());
				sessionBuffer.saveKernelMap(kernelHandler.getKernel(), kernelHandler.getImage());
				sessionBuffer.saveChartsUrl(chartHandler.getTypesChart(), chartHandler.getReasonsChart());
			case GET_JSON:
				JSONObject json = generateJSON(sessionBuffer);
				ServletUtil.sendJson(response, json);
				break;
			case KERNEL_MAP_ONLY:
				WikiCrimesEventsRetriever events2 = new WikiCrimesEventsRetriever(request, context, true, false);
				KernelMapRequestHandler kernelHandler2 = new KernelMapRequestHandler(request, events2.getPoints());
				sessionBuffer.saveKernelMap(kernelHandler2.getKernel(), kernelHandler2.getImage());
			case GET_IMAGE:
				Image image1 = sessionBuffer.getKernelMapImage();
				ServletUtil.sendImage(response, image1);
				break;
			case EVENTS:
				WikiCrimesEventsRetriever events3 = new WikiCrimesEventsRetriever(request, context, false, true);
				String crimesString = CrimeStringBuilder.buildString(events3.getEvents());
				ChartRequestHandler chartHandler2 = new ChartRequestHandler(events3);
				sessionBuffer.saveTotalEvents(events3.getTotalEvents());
				sessionBuffer.saveEvents(crimesString);
				sessionBuffer.saveChartsUrl(chartHandler2.getTypesChart(), chartHandler2.getReasonsChart());
				JSONObject json2 = generateJSON(sessionBuffer);
				ServletUtil.sendJson(response, json2);
				break;
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
