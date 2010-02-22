package org.wikicrimes.model;

public class PontoLatLng extends BaseObject {
	
	private static final long serialVersionUID = 1L;

	private Long idPonto;
	private Double latitude;
	private Double longitude;
	
	public PontoLatLng(){
		super();
	}
	
	public PontoLatLng(String str){
		String[] s = str.split(",");
		this.latitude = Double.valueOf(s[0]);
		this.longitude = Double.valueOf(s[1]);
	}
	
	public PontoLatLng(double lat, double lng){
		this.latitude = lat;
		this.longitude = lng;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Long getIdPonto() {
		return idPonto;
	}
	public void setIdPonto(Long idPonto) {
		this.idPonto = idPonto;
	}
	
	@Override
	public String toString() {
		return latitude + "," + longitude;
	}
}
