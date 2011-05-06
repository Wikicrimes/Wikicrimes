package org.wikicrimes.model;

import java.util.Date;

public class Acidente extends BaseObject {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8573195002038662570L;

	private Long idAcidente;
	
	private String descricao;
	
	private String chave;
	
	private Double latitude;
	
	private Double longitude;
			
    private Date dataHoraRegistro;

	public Long getIdAcidente() {
		return idAcidente;
	}

	public void setIdAcidente(Long idAcidente) {
		this.idAcidente = idAcidente;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
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

	public Date getDataHoraRegistro() {
		return dataHoraRegistro;
	}

	public void setDataHoraRegistro(Date dataHoraRegistro) {
		this.dataHoraRegistro = dataHoraRegistro;
	}    
	
}
