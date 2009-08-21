package org.wikicrimes.model;


public class TipoViolenciaEscolaRelato extends BaseObject {

	private static final long serialVersionUID = 2079315649648160853L;
	
	private Long idTipoViolenciaEscolaRelato;
	private String nome;
	private String descricao;
		
	public Long getIdTipoViolenciaEscolaRelato() {
		return idTipoViolenciaEscolaRelato;
	}
	public void setIdTipoViolenciaEscolaRelato(Long idTipoViolenciaEscolaRelato) {
		this.idTipoViolenciaEscolaRelato = idTipoViolenciaEscolaRelato;
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
