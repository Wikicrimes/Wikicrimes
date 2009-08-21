package org.wikicrimes.model;


public class TipoAgressorRelato extends BaseObject {

	private static final long serialVersionUID = -457810221050710363L;
	private Long idTipoAgressorRelato;
	private String nome;
	private String descricao;
	public Long getIdTipoAgressorRelato() {
		return idTipoAgressorRelato;
	}
	public void setIdTipoAgressorRelato(Long idTipoAgressorRelato) {
		this.idTipoAgressorRelato = idTipoAgressorRelato;
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
