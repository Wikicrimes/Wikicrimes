package org.wikicrimes.model;


public class TipoLocalizacaoRelato extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8262058275877841795L;
	private Long idTipoLocalizacaoRelato;
	private String nome;
	private String descricao;
			
	public Long getIdTipoLocalizacaoRelato() {
		return idTipoLocalizacaoRelato;
	}
	public void setIdTipoLocalizacaoRelato(Long idTipoLocalizacaoRelato) {
		this.idTipoLocalizacaoRelato = idTipoLocalizacaoRelato;
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
