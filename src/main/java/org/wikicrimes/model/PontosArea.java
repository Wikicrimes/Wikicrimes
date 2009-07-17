package org.wikicrimes.model;

public class PontosArea extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8132827676701328492L;
	
	private Integer idPontosArea;	
	private Double latitude;
	private Double longitude;
	private Integer ordemCriacao;
	private AreaObservacao areaObservacao;
	
	
	public Integer getIdPontosArea() {
		return idPontosArea;
	}
	public void setIdPontosArea(Integer idPontosArea) {
		this.idPontosArea = idPontosArea;
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
	public Integer getOrdemCriacao() {
		return ordemCriacao;
	}
	public void setOrdemCriacao(Integer ordemCriacao) {
		this.ordemCriacao = ordemCriacao;
	}
	public AreaObservacao getAreaObservacao() {
		return areaObservacao;
	}
	public void setAreaObservacao(AreaObservacao areaObservacao) {
		this.areaObservacao = areaObservacao;
	}
}
