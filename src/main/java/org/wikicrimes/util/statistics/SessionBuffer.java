package org.wikicrimes.util.statistics;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.wikicrimes.util.kernelmap.KernelMap;
import org.wikicrimes.util.statistics.Param.Application;

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
	
	public void saveKernelMap(KernelMap obj, Image img) {
		res.kernelMapObj = obj;
		res.kernelmapImg = img;
	}
	
	public void saveChartsUrl(String typeChartUrl, String reasonChartUrl) {
		res.typesChartUrl = typeChartUrl;
		res.reasonsChartUrl = reasonChartUrl;
	}
	
	public void saveTotalEvents(int totalEvents) {
		res.totalEvents = totalEvents;
	}
	
	public void saveEvents(String events) {
		res.events = events;
	}
	
	public KernelMap getKernelMap() {
		return res.kernelMapObj;
	}
	
	public Image getKernelMapImage() {
		return res.kernelmapImg;
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
	
	public String getEvents() {
		return res.events;
	}
	
}

class StatReqResults{
	int totalEvents;
	Image kernelmapImg;
	KernelMap kernelMapObj;
	String typesChartUrl, reasonsChartUrl, events;
}
