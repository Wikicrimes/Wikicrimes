package org.wikicrimes.model;

import java.util.Date;

public class Log extends BaseObject {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 3807936410156851078L;

	private Long idLog;
	
	private TipoLog tipoLog;
	
	private Usuario usuario;
	
	private Date data;
	
	private Long idObj;

	private String campo;
	
	private String campoAntigo;
	
	private String campoNovo;
	
	public Log() {
			}

	public Long getIdLog() {
		return idLog;
	}

	public void setIdLog(Long idLog) {
		this.idLog = idLog;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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

	public TipoLog getTipoLog() {
		return tipoLog;
	}

	public void setTipoLog(TipoLog tipoLog) {
		this.tipoLog = tipoLog;
	}

	public Long getIdObj() {
		return idObj;
	}

	public void setIdObj(Long idObj) {
		this.idObj = idObj;
	}

	



}
