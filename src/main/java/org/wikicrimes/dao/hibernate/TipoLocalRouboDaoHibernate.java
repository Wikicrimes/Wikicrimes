package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.TipoLocalRouboDao;
import org.wikicrimes.model.TipoLocalRoubo;

/**
 * 
 */
public class TipoLocalRouboDaoHibernate extends GenericCrudDaoHibernate implements
		TipoLocalRouboDao {

	public TipoLocalRouboDaoHibernate() {
		setEntity(TipoLocalRoubo.class);
	}

}
