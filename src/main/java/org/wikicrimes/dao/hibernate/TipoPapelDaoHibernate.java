package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.TipoPapelDao;
import org.wikicrimes.model.TipoPapel;

/**
 * 
 */
public class TipoPapelDaoHibernate extends GenericCrudDaoHibernate implements
		TipoPapelDao {

	public TipoPapelDaoHibernate() {
		setEntity(TipoPapel.class);
	}

}
