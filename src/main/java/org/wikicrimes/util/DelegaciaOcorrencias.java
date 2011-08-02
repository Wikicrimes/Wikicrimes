package org.wikicrimes.util;

public class DelegaciaOcorrencias {
	private long numTotalVitimas;
	private String delegacia;
	private Long idDp;
	public DelegaciaOcorrencias(int numTotalVitimas, String delegacia, Long idDp) {
		super();
		this.numTotalVitimas = numTotalVitimas;
		this.delegacia = delegacia;
		this.idDp = idDp;
	}
	public DelegaciaOcorrencias() {
		super();		
	}
	public long getNumTotalVitimas() {
		return numTotalVitimas;
	}
	public void setNumTotalVitimas(long numTotalVitimas) {
		this.numTotalVitimas = numTotalVitimas;
	}
	public String getDelegacia() {
		return delegacia;
	}
	public void setDelegacia(String delegacia) {
		this.delegacia = delegacia;
	}
	public Long getIdDp() {
		return idDp;
	}
	public void setIdDp(Long idDp) {
		this.idDp = idDp;
	}

}
