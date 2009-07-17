package org.wikicrimes.model;


public class CrimeRazao extends BaseObject {
	
	private static final long serialVersionUID = -1237189143299013294L;

	private Long idCrimeRazao;
	
	private Crime crime;
	
	private Razao razao;

	public Long getIdCrimeRazao() {
		return idCrimeRazao;
	}

	public void setIdCrimeRazao(Long idCrimeRazao) {
		this.idCrimeRazao = idCrimeRazao;
	}

	public Crime getCrime() {
		return crime;
	}

	public void setCrime(Crime crime) {
		this.crime = crime;
	}

	public Razao getRazao() {
		return razao;
	}

	public void setRazao(Razao razao) {
		this.razao = razao;
	}

}
