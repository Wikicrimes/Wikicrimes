package org.wikicrimes.model;

public class Ponto {
	
	private Long id;
	private Double lat;
	private Double lng;
	private String ordemCriacao;
	private Long idArea;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getOrdemCriacao() {
		return ordemCriacao;
	}

	public void setOrdemCriacao(String ordemCriacao) {
		this.ordemCriacao = ordemCriacao;
	}

	public Long getIdArea() {
		return idArea;
	}

	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	
}
