package org.wikicrimes.model;

import java.util.Date;

public class Credibilidade extends BaseObject{
	
	private static final long serialVersionUID = 132239747965134434L;
	
	
	/** Variables **/
	private Long idCredibilidade;
	private Double credibilidade;
	private Date periodo;
	private Crime crime;

	
	
	/** Constructors **/
	public Credibilidade() { }
	
	public Credibilidade(Long idCredibilidade) {
		this.idCredibilidade = idCredibilidade;
	}
	
	public Credibilidade(Long idCredibilidade, Double credibilidade, Date periodo, Crime crime) 
	{
		this.idCredibilidade = idCredibilidade;
		this.credibilidade = credibilidade;
		this.periodo = periodo;
		this.crime = crime;
	}
	
	
	
	/** Gets and Sets **/
	public Long getIdCredibilidade() {
		return idCredibilidade;
	}
	public void setIdCredibilidade(Long idCredibilidade) {
		this.idCredibilidade = idCredibilidade;
	}
	
	public Double getCredibilidade() {
		return credibilidade;
	}
	public void setCredibilidade(Double credibilidade) {
		this.credibilidade = credibilidade;
	}

	public void setCrime(Crime crime) {
		this.crime = crime;
	}
	public Crime getCrime() {
		return crime;
	}

	public void setPeriodo(Date periodo) {
		this.periodo = periodo;
	}

	public Date getPeriodo() {
		return periodo;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof Credibilidade)) {
			return false;
		} else {
			Credibilidade credibilidade = (Credibilidade) obj;
			return credibilidade.getIdCredibilidade().equals( this.getIdCredibilidade() );
		}

	}
	
}

