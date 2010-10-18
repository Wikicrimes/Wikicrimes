package org.wikicrimes.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.wikicrimes.util.kernelMap.LatLngBoundsGM;

public class PontoLatLng extends BaseObject {
	
	private static final long serialVersionUID = 1L;

	private Long idPonto;
	public Double lat;
	public Double lng;
	
	public PontoLatLng(){
		super();
	}
	
	public PontoLatLng(String str){
		String[] s = str.split(",");
		this.lat = Double.valueOf(s[0]);
		this.lng = Double.valueOf(s[1]);
	}
	
	public PontoLatLng(double lat, double lng){
		this.lat = lat;
		this.lng = lng;
	}
	
	public Double getLatitude() {
		return lat;
	}
	public void setLatitude(Double latitude) {
		this.lat = latitude;
	}
	public Double getLongitude() {
		return lng;
	}
	public void setLongitude(Double longitude) {
		this.lng = longitude;
	}
	public Long getIdPonto() {
		return idPonto;
	}
	public void setIdPonto(Long idPonto) {
		this.idPonto = idPonto;
	}
	
	public Point toPixel(int zoom){
		double offset = 256 << (zoom-1);
		double lat = this.getLatitude();
		double lng = this.getLongitude();
	    int x = (int)Math.round(offset + offset*lng/180);
	    int y = (int)Math.round(offset - offset/Math.PI * Math.log((1 + Math.sin(lat * Math.PI / 180)) / (1 - Math.sin(lat * Math.PI / 180))) / 2);
		return new Point(x,y);
	}
	
	//hotspots ficam deslocados no zoom alto (o outro metodo toPixel acima faz direito)
	public Point toPixel(LatLngBoundsGM boundsLatlng, Rectangle boundsPixel){
		double razaoWidth = boundsPixel.width/boundsLatlng.width;
		double razaoHeight = boundsPixel.height/boundsLatlng.height;
		int x = boundsPixel.x + (int)((this.getLongitude()-boundsLatlng.oeste) * razaoWidth);
		int y = boundsPixel.y + (int)((boundsLatlng.norte-this.getLatitude()) * razaoHeight);
		return new Point(x,y);
	}
	
	//fonte: http://home.provide.net/~bratliff/adjust.js
	public static PontoLatLng fromPixel(Point pixel, int zoom){
		double offset = 256 << (zoom-1);
		double lng = (pixel.x/offset-1)*180;
		double lat = (Math.PI/2-2*Math.atan(Math.exp((pixel.y-offset)/(offset/Math.PI))))*180/Math.PI;
		return new PontoLatLng(lat,lng);
	}
	
	public static List<PontoLatLng> fromPixel(List<? extends Point> pixel, int zoom){
		List<PontoLatLng> latlng = new ArrayList<PontoLatLng>();
		for(Point p : pixel)
			latlng.add(fromPixel(p, zoom));
		return latlng;
	}
	
	public static List<Point> toPixel(List<PontoLatLng> latlng, int zoom){
		List<Point> pixel = new ArrayList<Point>();
		for(PontoLatLng p : latlng)
			pixel.add(p.toPixel(zoom));
		return pixel;
	}
	
	@Override
	public String toString() {
		return lat + "," + lng;
	}
}
