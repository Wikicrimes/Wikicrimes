package org.wikicrimes.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.wikicrimes.util.kernelmap.LatLngBoundsGM;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;

@SuppressWarnings("serial")
public class PontoLatLng extends BaseObject {
	
	private Long idPonto;
	public Double lat;
	public Double lng;
	
	private static final double KM_POR_GRAU = 110.5; //aproximacao do valor medio pra distancia em 1 grau de latitude ou longitude
	
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
	
	public Ponto toPixel(int zoom){
		double offset = 256 << (zoom-1);
		double lat = this.getLatitude();
		double lng = this.getLongitude();
	    int x = (int)Math.round(offset + offset*lng/180);
	    int y = (int)Math.round(offset - offset/Math.PI * Math.log((1 + Math.sin(lat * Math.PI / 180)) / (1 - Math.sin(lat * Math.PI / 180))) / 2);
		return new Ponto(x,y);
	}
	
	//hotspots ficam deslocados no zoom alto (o outro metodo toPixel acima faz direito)
	public Ponto toPixel(LatLngBoundsGM boundsLatlng, Rectangle boundsPixel){
		double razaoWidth = boundsPixel.width/boundsLatlng.width;
		double razaoHeight = boundsPixel.height/boundsLatlng.height;
		int x = boundsPixel.x + (int)((this.getLongitude()-boundsLatlng.oeste) * razaoWidth);
		int y = boundsPixel.y + (int)((boundsLatlng.norte-this.getLatitude()) * razaoHeight);
		return new Ponto(x,y);
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
	
	public static List<Ponto> toPixel(List<PontoLatLng> latlng, int zoom){
		List<Ponto> pixel = new ArrayList<Ponto>();
		for(PontoLatLng p : latlng)
			pixel.add(p.toPixel(zoom));
		return pixel;
	}
	
	/**
	 * @param dx: kms para leste (negativo vai pra oeste)
	 * @param dy: kms para sul (negativo vai pra norte)
	 */
	public PontoLatLng transladarKm(double dx, double dy) {
		double lat = this.lat - dy/KM_POR_GRAU;
		double lng = this.lng + dx/KM_POR_GRAU;
		//TODO tratar qd o lat passa de +-90 graus ou o lng passa de +-180
		return new PontoLatLng(lat, lng);
	}
	
	public PontoLatLng transladarPixel(int dx, int dy, int zoom) {
		Point p = this.toPixel(zoom);
		p.translate(dx, dy);
		return PontoLatLng.fromPixel(p, zoom);
	}
	
	public static double distanceKM(PontoLatLng p, PontoLatLng q) {
		double distanciaLatLng = Point.distance(p.lng, p.lat, q.lng, q.lat);
		return distanciaLatLng * KM_POR_GRAU;
	}
	
	public static double distanceKmToPixels(double km, int zoom) {
		PontoLatLng l1 = new PontoLatLng(0,0);
		PontoLatLng l2 = l1.transladarKm(km, 0);
		Ponto p1 = l1.toPixel(zoom);
		Ponto p2 = l2.toPixel(zoom);
		return Ponto.distancia(p1, p2);
	}
	
	public static PontoLatLng medio(PontoLatLng p, PontoLatLng q) {
		double lng = (p.lng + q.lng) / 2;
		double lat = (p.lat + q.lat) / 2;
		return new PontoLatLng(lat,lng);
	}
	
	@Override
	public String toString() {
		return lat + "," + lng;
	}
}
