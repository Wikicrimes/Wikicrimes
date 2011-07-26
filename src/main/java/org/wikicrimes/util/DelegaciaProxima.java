package org.wikicrimes.util;

public class DelegaciaProxima implements Comparable<DelegaciaProxima> {
	private Long idFonteExterna;
	private Double latitude;
	private Double longitude;
	private Double distancia;
	private String nome;
	
	
	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public DelegaciaProxima(Long idFonteExterna, Double latitude,
			Double longitude, Double distancia, String nome) {
		super();
		this.idFonteExterna = idFonteExterna;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distancia = distancia;
		this.nome = nome;
	}



	public DelegaciaProxima() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getIdFonteExterna() {
		return idFonteExterna;
	}

	public void setIdFonteExterna(Long long1) {
		this.idFonteExterna = long1;
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

	public Double getDistancia() {
		return distancia;
	}

	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}

	public int	compareTo(DelegaciaProxima d) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
		
	    if(this.distancia < d.distancia) return BEFORE;
	    
	    if(this.distancia > d.distancia) return AFTER;
	    
	    if(this.distancia == d.distancia) return EQUAL;
	    return EQUAL;
    }


	

}
