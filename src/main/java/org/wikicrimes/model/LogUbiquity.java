package org.wikicrimes.model;

import java.util.Date;

public class LogUbiquity extends BaseObject {
		
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 3414465136442220287L;

	private Long idLog;
	
	private Integer idSIA;
	
	private Crime crime;
	
	private Date data;
	
	private String campo;
	
	private String campoAntigo;
	
	private String campoNovo;
	
	public LogUbiquity() {
			}

	public Long getIdLog() {
		return idLog;
	}

	public void setIdLog(Long idLog) {
		this.idLog = idLog;
	}

	public Integer getIdSIA() {
		return idSIA;
	}

	public void setIdSIA(Integer idSIA) {
		this.idSIA = idSIA;
	}

	public Crime getCrime() {
		return crime;
	}

	public void setCrime(Crime crime) {
		this.crime = crime;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getCampoAntigo() {
		return campoAntigo;
	}

	public void setCampoAntigo(String campoAntigo) {
		this.campoAntigo = campoAntigo;
	}

	public String getCampoNovo() {
		return campoNovo;
	}

	public void setCampoNovo(String campoNovo) {
		this.campoNovo = campoNovo;
	}
}