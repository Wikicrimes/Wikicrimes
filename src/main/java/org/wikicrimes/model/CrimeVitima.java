package org.wikicrimes.model;

public class CrimeVitima extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5052275238366575211L;

	private TipoCrime tipoCrime;
	private TipoVitima tipoVitima;

	public CrimeVitima(TipoCrime tipoCrime, TipoVitima tipoVitima) {
		this.tipoCrime = tipoCrime;
		this.tipoVitima = tipoVitima;
	}
	
	public CrimeVitima() {}

  	public TipoCrime getTipoCrime() {
		return tipoCrime;
	}

	public void setTipoCrime(TipoCrime tipoCrime) {
		this.tipoCrime = tipoCrime;
	}

	public TipoVitima getTipoVitima() {
		return tipoVitima;
	}

	public void setTipoVitima(TipoVitima tipoVitima) {
		this.tipoVitima = tipoVitima;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof CrimeVitima)) {
			return false;
		} else {
			CrimeVitima crimeVitima = (CrimeVitima) obj;
			return crimeVitima.getTipoCrime().equals( this.getTipoCrime() ) && crimeVitima.getTipoVitima().equals( this.getTipoVitima() ) ;
		}

	}
	
	public int hashCode()
	{
		int hash = 7;
		hash = 31 * hash + (null == tipoCrime ? 0 : tipoCrime.hashCode());
		hash = 31 * hash + (null == tipoVitima ? 0 : tipoVitima.hashCode());
		return hash;
	}
	
}
