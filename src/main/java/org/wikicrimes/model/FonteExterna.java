package org.wikicrimes.model;

import java.util.Date;

public class FonteExterna extends BaseObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3023285442650549170L;
	private Long idFonteExterna;
	private Date dataHoraRegistro;
	private String nome;
	private Double latitude;
	private Double longitude;
	
	public Long getIdFonteExterna() {
		return idFonteExterna;
	}
	public void setIdFonteExterna(Long idFonteExterna) {
		this.idFonteExterna = idFonteExterna;
	}
	public Date getDataHoraRegistro() {
		return dataHoraRegistro;
	}
	public void setDataHoraRegistro(Date dataHoraRegistro) {
		this.dataHoraRegistro = dataHoraRegistro;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
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
	
}
