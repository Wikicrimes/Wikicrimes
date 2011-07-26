package org.wikicrimes.util;

public class DelegaciaOcorrencias {
	private long numTotalVitimas;
	private String delegacia;
	public DelegaciaOcorrencias(int numTotalVitimas, String delegacia) {
		super();
		this.numTotalVitimas = numTotalVitimas;
		this.delegacia = delegacia;
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
	
	
	

}
