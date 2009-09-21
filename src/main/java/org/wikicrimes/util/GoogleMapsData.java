package org.wikicrimes.util;

public class GoogleMapsData {
	private double latitude,longitude;

	public GoogleMapsData() {
		latitude=longitude=0;
	}
	
	public GoogleMapsData(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}
