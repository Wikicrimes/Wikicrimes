package org.wikicrimes.model;


public class Razao extends BaseObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6348445033739306856L;

	private Long idRazao;
	
	private String nome;
	
	private String descricao;

	public Long getIdRazao() {
		return idRazao;
	}

	public void setIdRazao(Long idRazao) {
		this.idRazao = idRazao;
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
