package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.TipoArmaUsadaDao;
import org.wikicrimes.model.TipoArmaUsada;

/**
 * 
 */
public class TipoArmaUsadaDaoHibernate extends GenericCrudDaoHibernate
		implements TipoArmaUsadaDao {

	public TipoArmaUsadaDaoHibernate() {
		setEntity(TipoArmaUsada.class);
	}

}
