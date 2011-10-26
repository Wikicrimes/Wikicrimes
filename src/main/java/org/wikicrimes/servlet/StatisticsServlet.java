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
import org.wikicrimes.util.statistics.ChartRequestHandler;
import org.wikicrimes.util.statistics.CrimeStringBuilder;
import org.wikicrimes.util.statistics.EventsRetriever;
import org.wikicrimes.util.statistics.KernelMapRequestHandler;
import org.wikicrimes.util.statistics.Param;
import org.wikicrimes.util.statistics.Param.Actions;
import org.wikicrimes.util.statistics.SessionBuffer;
import org.wikicrimes.util.statistics.WikiCrimesEventsRetriever;

import br.com.wikinova.heatmaps.KernelMap;

@SuppressWarnings("serial")
public class StatisticsServlet extends HttpServlet{

	/*
	 * url de exemplo:
	 * http://200.19.188.105:5152/wikicrimes/statistics?actions=kernel|image&northPixel=2019&southPixel=2405&eastPixel=1950&westPixel=963&widthPixel=987&heightPixel=386&northLatLng=2.5479878714713835&southLatLng=-29.91685223307016&eastLatLng=-8.61328125&westLatLng=-95.361328125&widthLatLng=86.748046875&heightLatLng=32.46484010454154&zoom=4&tc=0&tv=&tl=&di=&df=11,10,2011&hi=-1&hf=-1&z=4&ec=-1&cp=&id=undefined
	 */
	
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
//			/*DEBUG*/TimeTest.saveInstant("init");
			
			if(actions.needsEventsFromDB()) {
				sessionBuffer.clear();
				EventsRetriever<?> events = Param.getEventsRetriever(request, context);
				sessionBuffer.saveTotalEvents(events.getTotalEvents());
//				/*DEBUG*/TimeTest.saveInstant("consulta BD");
			
				if(actions.asksGenerateKernelMap()) {
					KernelMapRequestHandler kernelHandler = new KernelMapRequestHandler(request, events.getPoints());
					if(actions.asksGenerateBooleanGrid()) {
						kernelHandler.generateBooleanGrid();
					}
					sessionBuffer.saveKernelMap(kernelHandler.getKernel(), kernelHandler.getImage(), kernelHandler.getBooleanGrid());
//					/*DEBUG*/TimeTest.saveInstant("kernel");
				}
				
				if(actions.asksCreateCharts() && events instanceof WikiCrimesEventsRetriever) {
					ChartRequestHandler chartHandler = new ChartRequestHandler((WikiCrimesEventsRetriever)events);
					sessionBuffer.saveChartsUrls(chartHandler.getTypesChart(), chartHandler.getReasonsChart());
//					/*DEBUG*/TimeTest.saveInstant("charts");
				}
				
				if(actions.asksSendEvents() && events instanceof WikiCrimesEventsRetriever) {
					String crimesString = CrimeStringBuilder.buildString(((WikiCrimesEventsRetriever)events).getEvents());
					sessionBuffer.saveEvents(crimesString);
//					/*DEBUG*/TimeTest.saveInstant("events string");
				}
			}
			
			if(actions.asksSendJson()) {
				JSONObject json = generateJSON(sessionBuffer);
//				/*DEBUG*/TimeTest.saveInstant("generate json");
				ServletUtil.sendJson(response, json);
//				/*DEBUG*/TimeTest.saveInstant("send json");
			}else if(actions.asksSendKernelMapImage()){
				Image image = sessionBuffer.getKernelMapImage();
				if(image != null) {
					ServletUtil.sendImage(response, image);
				}else {
					//ignora: o usuario mexeu o mapa rapidamente, causando requisicoes sobrepostas
					//image == null, ocorre nas primeiras requisicoes, mas, neste caso, apenas a ultima importa 
				}
//				/*DEBUG*/TimeTest.saveInstant("send image");
			}
			
//			/*DEBUG*/TimeTest.print();
			
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
			//kernelObj.put("grid", kernel.getDensityGrid());
			//kernelObj.put("image", ses.getKernelMapImage());

			String grid = ses.getKernelMapBooleanGrid(); 
			if(grid != null) {
				kernelObj.put("booleanGrid", grid);
			}
			
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
