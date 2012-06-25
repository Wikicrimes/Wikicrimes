package org.wikicrimes.util.statistics;

import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.util.statistics.Param.Application;

import br.com.wikinova.heatmaps.KernelMap;

public class SessionBuffer {

	private StatReqResults res;
	private final String SESSION_KEY = "STATISTICS";
	
	
	@SuppressWarnings("unchecked")
	public SessionBuffer(HttpSession session, Application app) {
		Map<Application, StatReqResults> map = (Map<Application, StatReqResults>)session.getAttribute(SESSION_KEY);
		if(map == null) {
			map = new HashMap<Application, StatReqResults>();
			session.setAttribute(SESSION_KEY, map);
		}
		res = map.get(app);
		if(res == null) {
			res = new StatReqResults();
			map.put(app, res);
		}
	}
	
	public SessionBuffer(HttpServletRequest request) {
		this(request.getSession(),Param.getApplication(request));
	}
	
	public void saveKernelMap(KernelMap obj, Image img, String booleanGrid) {
		res.kernelmapObj = obj;
		res.kernelmapImg = img;
		res.kernelmapBooleanGrid = booleanGrid;
	}
	
	public void saveChartsUrls(String typeChartUrl, String reasonChartUrl) {
		res.typesChartUrl = typeChartUrl;
		res.reasonsChartUrl = reasonChartUrl;
	}
	
	public void saveTotalEvents(int totalEvents) {
		res.totalEvents = totalEvents;
	}
	
	public void saveStartDate(Date startDate){
		res.startDate = startDate;
	}
	
	public void saveEvents(String events) {
		res.events = events;
	}
	
	public void saveCenter(PontoLatLng center) {
		res.center = center;
	}
	
	public KernelMap getKernelMap() {
		return res.kernelmapObj;
	}
	
	public Image getKernelMapImage() {
		return res.kernelmapImg;
	}
	
	public String getKernelMapBooleanGrid() {
		return res.kernelmapBooleanGrid;
	}
	
	public String getTypesChartUrl() {
		return res.typesChartUrl;
	}
	
	public String getReasonsChartUrl() {
		return res.reasonsChartUrl;
	}
	
	public int getTotalEvents() {
		return res.totalEvents;
	}
	
	public String getStartDate(){
		if(res.startDate == null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(res.startDate);
	}
	
	public String getEvents() {
		return res.events;
	}
	
	public PontoLatLng getCenter() {
		return res.center;
	}
	
	public void clear() {
		res.totalEvents = 0;
		res.startDate = null;
		res.events = null;
		res.center = null;
		res.kernelmapImg = null;
		res.kernelmapObj = null;
		res.kernelmapBooleanGrid = null;
		res.typesChartUrl = null;
		res.reasonsChartUrl = null;
	}
}

class StatReqResults{
	int totalEvents;
	Image kernelmapImg;
	KernelMap kernelmapObj;
	String typesChartUrl, reasonsChartUrl, events, kernelmapBooleanGrid;
	Date startDate;
	PontoLatLng center;
	
}
