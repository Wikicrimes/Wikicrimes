package org.wikicrimes.util.statistics;

import java.awt.Rectangle;
import java.security.InvalidParameterException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.wikicrimes.util.kernelmap.LatLngBoundsGM;

import antlr.ActionTransInfo;

public class Param {

	public static class Keys{
		
		private static final String application = "app";
		private static final String actions = "actions";
		private static final String zoom = "zoom";
		
		private static class PixelBounds{
			private static final String north = "northPixel";
//			private static final String south = "southPixel";
//			private static final String east = "eastPixel";
			private static final String west = "westPixel";
			private static final String width = "widthPixel";
			private static final String height = "heightPixel";
		}

		private static class LatLngBounds{
			private static final String north = "northLatLng";
			private static final String south = "southLatLng";
			private static final String east = "eastLatLng";
			private static final String west = "westLatLng";
			private static final String width = "widthLatLng";
			private static final String height = "heightLatLng";
		}
		
		private static class CenteredEvent {
			private static final String id = "eventId";
			private static final String type = "eventType";
		}
		
	}
	
	public static enum Application{
		WIKICRIMES, WIKIMAPPS;
	}
	
	public static Application getApplication(HttpServletRequest request) {
		String app = request.getParameter(Keys.application);
		if(app == null)
			return Application.WIKICRIMES; //default
		if(app.equals("wikicrimes"))
			return Application.WIKICRIMES;
		else if(app.equals("wikimapps"))
			return Application.WIKIMAPPS;
		else
			throw new InvalidParameterException(app);
	}
	
	public static class Actions{
		
		private boolean kernel;
		private boolean charts;
		private boolean events;
		private boolean json;
		private boolean polygons;
		private boolean image;
		
		public boolean generateKernelMap() {
			return kernel;
		}
		
		public boolean generateCharts() {
			return charts;
		}
		
		public boolean generateBooleanGrid() {
			return polygons;
		}
		
		public boolean sendEvents() {
			return events;
		}
		
		public boolean getJson() {
			return json;
		}
		
		public boolean getKernelMapImage() {
			return image;
		}
		
		public boolean needEventsFromDB() {
			return kernel | charts | events; 
		}
		
	}
	
	public static Actions getActions(ServletRequest request) {
		String actionsStr = request.getParameter(Keys.actions);
		if(actionsStr == null) throw new InvalidParameterException(Keys.actions + " is empty");
		String[] actionStrArray = actionsStr.split("\\|");
		Actions actions = new Actions();
		for(int i=0; i<actionStrArray.length; i++) {
			if(actionStrArray[i].equalsIgnoreCase("kernel"))
				actions.kernel = true;
			else if(actionStrArray[i].equalsIgnoreCase("charts"))
				actions.charts = true;
			else if(actionStrArray[i].equalsIgnoreCase("events"))
				actions.events = true;
			else if(actionStrArray[i].equalsIgnoreCase("json"))
				actions.json = true;
			else if(actionStrArray[i].equalsIgnoreCase("image"))
				actions.image = true;
			else if(actionStrArray[i].equalsIgnoreCase("polygons"))
				actions.polygons = true;
			else
				throw new InvalidParameterException(Keys.actions + " = " + actionsStr);
		}
		return actions;
	}
	
	
	/**
	 * obs: O zoom do GoogleMaps varia de 0 a 19. Zero eh o mais de longe. 
	 */
	public static int getZoom(ServletRequest request){
		String zoomStr = request.getParameter(Keys.zoom);
		try{
			return Integer.parseInt(zoomStr);
		}catch(NumberFormatException e){
			throw new InvalidParameterException(zoomStr);
		}
	}
	
	public static class CenteredEvent {
		public final String id;
		public final EventType type;
		public final int viewportWidth;
		public final int viewportHeight;
		
		private CenteredEvent(String id, EventType type, int viewportWidth, int viewportHeight) {
			this.id = id;
			this.type = type;
			this.viewportWidth = viewportWidth;
			this.viewportHeight = viewportHeight;
		}
	}
	
	public static CenteredEvent getCenteredEvent(ServletRequest request){
		String id = request.getParameter(Keys.CenteredEvent.id);
		EventType type = EventType.from(request);
		/*TEMP*/if(id == null) {
		/*TEMP*/	id = request.getParameter("idcrime");
		/*TEMP*/	type = EventType.CRIME;
		/*TEMP*/}
		/*TEMP*/if(id == null) {
		/*TEMP*/	id = request.getParameter("idrelato");
		/*TEMP*/	type = EventType.REPORT;
		/*TEMP*/}
		if(id == null) return null;
		int width = Integer.parseInt(request.getParameter(Keys.PixelBounds.width));
		int height = Integer.parseInt(request.getParameter(Keys.PixelBounds.height));
		return new CenteredEvent(id, type, width, height);
	}
	
	public static enum EventType{
		CRIME, REPORT, POLICE_STATION;
		
		private static EventType from(ServletRequest request){
			String type = request.getParameter(Keys.CenteredEvent.type);
			if(type == null)
				return null;
			if(type.equals("crime"))
				return CRIME;
			if(type.equals("relato"))
				return REPORT;
			else if(type.equals("delegacia"))
				return POLICE_STATION;
			else
				return null;
		}
	}
	
	public static Rectangle getPixelBounds(ServletRequest request){
		//obs: width = east-west nao funciona no caso em q a emenda do mapa esta aparecendo na tela (a linha entre Japao e EUA) 
		//width seria negativo ja q west>east
		//por isso o width e o height estao sendo calculado no javascript, usando as coordenadas do centro da tela
		String northStr = request.getParameter(Keys.PixelBounds.north);
//		String southStr = request.getParameter("southPixel");
//		String eastStr = request.getParameter("eastPixel");
		String westStr = request.getParameter(Keys.PixelBounds.west);
		String widthStr = request.getParameter(Keys.PixelBounds.width);
		String heightStr = request.getParameter(Keys.PixelBounds.height);
		try{
			int north = Integer.parseInt(northStr);
//			int south = Integer.parseInt(southStr);
//			int east = Integer.parseInt(eastStr);
			int west = Integer.parseInt(westStr);
			int width = Integer.parseInt(widthStr);
			int height = Integer.parseInt(heightStr);
			return new Rectangle(west, north, width, height);
		}catch(NumberFormatException e){
			throw new InvalidParameterException("northPixel: " + northStr + ", westPixel: "
					+ westStr + ", width: " + widthStr + ", height: " + heightStr);
		}
	}
	
	public static LatLngBoundsGM getLatLngBounds(ServletRequest request){
		//obs: width = east-west nao funciona no caso em q a emenda do mapa esta aparecendo na tela (a linha entre Japao e EUA) 
		//width seria negativo ja q west>east
		//por isso o width e o height estao sendo calculado no javascript, usando as coordenadas do centro da tela
		String northStr = request.getParameter(Keys.LatLngBounds.north);
		String southStr = request.getParameter(Keys.LatLngBounds.south);
		String eastStr = request.getParameter(Keys.LatLngBounds.east);
		String westStr = request.getParameter(Keys.LatLngBounds.west);
		String widthStr = request.getParameter(Keys.LatLngBounds.width);
		String heightStr = request.getParameter(Keys.LatLngBounds.height);
		try{
			double north = Double.parseDouble(northStr);
			double south = Double.parseDouble(southStr);
			double east = Double.parseDouble(eastStr);
			double west = Double.parseDouble(westStr);
			double width = Double.parseDouble(widthStr);
			double height = Double.parseDouble(heightStr);
			return new LatLngBoundsGM(north, south, east, west, width, height);
		}catch(NumberFormatException e){
//			throw new InvalidParameterException("parametro faltando no getLimitesPixel. " +
//					"northLatLng: " + northStr + ", southLatLng: " + southStr + ", eastLatLng: "
//					+ eastStr + ", westLatLng: " + westStr + ", widthLatLng: " + widthStr
//					+ ", heightLatLng: " + heightStr );
			return null;
		}
	}
	
	
	
}
