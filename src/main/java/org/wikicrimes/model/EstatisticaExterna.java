package org.wikicrimes.model;

import java.util.Date;

public class EstatisticaExterna extends BaseObject {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5546362870240758604L;
	
	private Long idEstatisticaExterna;
	private Date dataHoraRegistro;
	private String mes;
	private Integer numVitimas;
	private String tipo;
	private String delegacia;
	private FonteExterna fonte;
	public Long getIdEstatisticaExterna() {
		return idEstatisticaExterna;
	}
	public void setIdEstatisticaExterna(Long idEstatisticaExterna) {
		this.idEstatisticaExterna = idEstatisticaExterna;
	}
	public Date getDataHoraRegistro() {
		return dataHoraRegistro;
	}
	public void setDataHoraRegistro(Date dataHoraRegistro) {
		this.dataHoraRegistro = dataHoraRegistro;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public Integer getNumVitimas() {
		return numVitimas;
	}
	public void setNumVitimas(Integer numVitimas) {
		this.numVitimas = numVitimas;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDelegacia() {
		return delegacia;
	}
	public void setDelegacia(String delegacia) {
		this.delegacia = delegacia;
	}
	public FonteExterna getFonte() {
		return fonte;
	}
	public void setFonte(FonteExterna fonte) {
		this.fonte = fonte;
	}
	
}
