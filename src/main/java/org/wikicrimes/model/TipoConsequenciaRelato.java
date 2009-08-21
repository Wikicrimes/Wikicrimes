package org.wikicrimes.model;


public class TipoConsequenciaRelato extends BaseObject {

	private static final long serialVersionUID = 673001761031673575L;
	
	private Long idTipoConsequenciaRelato;
	private String nome;
	private String descricao;
			
	public Long getIdTipoConsequenciaRelato() {
		return idTipoConsequenciaRelato;
	}
	public void setIdTipoConsequenciaRelato(Long idTipoConsequenciaRelato) {
		this.idTipoConsequenciaRelato = idTipoConsequenciaRelato;
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
