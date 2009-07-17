package org.wikicrimes.model;

public class RedeSocial extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9195674846860255605L;
	
	private Long idRedeSocial;
	
	private String dominioRedeSocial;
	
	private String nomeRedeSocial;
	
	private String descricaoRedeSocial;
	
	public RedeSocial(){}
	
	public RedeSocial(String dominioRedeSocial){
		this.setDominioRedeSocial(dominioRedeSocial);
	}

	public Long getIdRedeSocial() {
		return idRedeSocial;
	}

	public void setIdRedeSocial(Long idRedeSocial) {
		this.idRedeSocial = idRedeSocial;
	}

	public String getNomeRedeSocial() {
		return nomeRedeSocial;
	}

	public void setNomeRedeSocial(String nomeRedeSocial) {
		this.nomeRedeSocial = nomeRedeSocial;
	}

	public String getDescricaoRedeSocial() {
		return descricaoRedeSocial;
	}

	public void setDescricaoRedeSocial(String descricaoRedeSocial) {
		this.descricaoRedeSocial = descricaoRedeSocial;
	}

	public String getDominioRedeSocial() {
		return dominioRedeSocial;
	}

	public void setDominioRedeSocial(String dominioRedeSocial) {
		this.dominioRedeSocial = dominioRedeSocial;
	}
	
}
