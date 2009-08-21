package org.wikicrimes.model;


public class TipoReportRelato extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -455801374026885292L;
	
	private Long idTipoReportRelato;
	private String nome;
	private String descricao;
	public Long getIdTipoReportRelato() {
		return idTipoReportRelato;
	}
	public void setIdTipoReportRelato(Long idTipoReportRelato) {
		this.idTipoReportRelato = idTipoReportRelato;
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
