package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.TipoRegistroDao;
import org.wikicrimes.model.TipoRegistro;

/**
 * 
 */
public class TipoRegistroDaoHibernate extends GenericCrudDaoHibernate implements
		TipoRegistroDao {

	public TipoRegistroDaoHibernate() {
		setEntity(TipoRegistro.class);
	}

}
