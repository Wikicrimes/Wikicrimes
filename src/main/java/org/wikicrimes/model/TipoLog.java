package org.wikicrimes.model;

public class TipoLog extends BaseObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6388158920015793516L;
	
	private Long idTipoLog;
	private String nome;
	private String descricao;

	public TipoLog() {}

	public Long getIdTipoLog() {
		return idTipoLog;
	}

	public void setIdTipoLog(Long idTipoLog) {
		this.idTipoLog = idTipoLog;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	

	
}
