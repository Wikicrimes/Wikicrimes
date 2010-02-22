package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.PontoLatLngDao;
import org.wikicrimes.model.PontoLatLng;

public class PontoLatLngDaoHibernate extends GenericCrudDaoHibernate implements
		PontoLatLngDao {
	
	public PontoLatLngDaoHibernate() {
		setEntity(PontoLatLng.class);
	}

}
