package org.wikicrimes.model;


public class TipoBemRoubadoRelato extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3272724038966751225L;
	
	private Long idTipoBemRoubadoRelato;
	private String nome;
	private String descricao;
		
	public Long getIdTipoBemRoubadoRelato() {
		return idTipoBemRoubadoRelato;
	}
	public void setIdTipoBemRoubadoRelato(Long idTipoBemRoubadoRelato) {
		this.idTipoBemRoubadoRelato = idTipoBemRoubadoRelato;
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
