package org.wikicrimes.util.kernelMap;

/**
 * 
 * @author Mairon
 *
 */

public class Coordenada {
	private long latitudeYPixel;
	private long longitudeXPixel;
	
	public Coordenada(long longitudeXPixel, long latitudeYPixel) {
		this.latitudeYPixel = latitudeYPixel;
		this.longitudeXPixel = longitudeXPixel;
	}
	
	public long getLatitudeYPixel() {
		return latitudeYPixel;
	}
	public void setLatitudeYPixel(long latitudeYPixel) {
		this.latitudeYPixel = latitudeYPixel;
	}
	public long getLongitudeXPixel() {
		return longitudeXPixel;
	}
	public void setLongitudeXPixel(long longitudeXPixel) {
		this.longitudeXPixel = longitudeXPixel;
	}
	
	
	
	
	
	
}
