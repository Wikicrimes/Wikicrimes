package org.wikicrimes.model;


public class RelatoRazao extends BaseObject {
	
	private static final long serialVersionUID = -1237189143299013294L;

	private Long idRelatoRazao;
	
	private Relato relato;
	
	private Razao razao;

	public Long getIdRelatoRazao() {
		return idRelatoRazao;
	}

	public void setIdRelatoRazao(Long idRelatoRazao) {
		this.idRelatoRazao = idRelatoRazao;
	}

	public Relato getRelato() {
		return relato;
	}

	public void setRelato(Relato relato) {
		this.relato = relato;
	}

	public Razao getRazao() {
		return razao;
	}

	public void setRazao(Razao razao) {
		this.razao = razao;
	}

}
