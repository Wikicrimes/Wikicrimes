package org.wikicrimes.model;

/**
 * Superclasse pra tds BaseObject com posicao geografica.
 * @author victor
 */
@SuppressWarnings("serial")
public abstract class GeoEvent extends BaseObject{

	public abstract Double getLatitude();
	public abstract Double getLongitude();
	public abstract Long getId();
	public abstract String getChave();
	
	@Override
	public boolean equals(Object obj) {
//		return ((GeoEvent)obj).getId().equals(getId());
		return ((GeoEvent)obj).getChave().equals(getChave());
	}
	
	public PontoLatLng getLatLng() {
		return new PontoLatLng(getLatitude(), getLongitude());
	}
	
}
