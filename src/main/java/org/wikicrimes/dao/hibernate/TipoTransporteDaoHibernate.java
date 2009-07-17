package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.TipoTransporteDao;
import org.wikicrimes.model.TipoTransporte;

/**
 * 
 */
public class TipoTransporteDaoHibernate extends GenericCrudDaoHibernate implements
		TipoTransporteDao {

	public TipoTransporteDaoHibernate() {
		setEntity(TipoTransporte.class);
	}

}
