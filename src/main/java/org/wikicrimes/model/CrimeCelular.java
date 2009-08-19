package org.wikicrimes.model;

import java.util.Date;


public class CrimeCelular extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6092736035904683586L;

	private Long idCrime;
	
	private String latitude;

	private String longitude;
	
	private String tipoCrime;
	
	private UsuarioCelular usuarioCelular;

	private String data;
	
	private Date dataHoraRegistro;
	
	private String descricao;
	
	private String turno;
	
	private Long jaImportado;
	
	public Long getIdCrime() {
		return idCrime;
	}

	public void setIdCrime(Long idCrime) {
		this.idCrime = idCrime;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public UsuarioCelular getUsuarioCelular() {
		return usuarioCelular;
	}

	public void setUsuarioCelular(UsuarioCelular usuarioCelular) {
		this.usuarioCelular = usuarioCelular;
	}
		
	public Date getDataHoraRegistro() {
		return dataHoraRegistro;
	}

	public void setDataHoraRegistro(Date dataHoraRegistro) {
		this.dataHoraRegistro = dataHoraRegistro;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getTipoCrime() {
		return tipoCrime;
	}

	public void setTipoCrime(String tipoCrime) {
		this.tipoCrime = tipoCrime;
	}

	public Long getJaImportado() {
		return jaImportado;
	}

	public void setJaImportado(Long jaImportado) {
		this.jaImportado = jaImportado;
	}
	
}
