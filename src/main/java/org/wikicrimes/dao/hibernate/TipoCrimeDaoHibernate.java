package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.TipoCrimeDao;
import org.wikicrimes.model.TipoCrime;

/**
 * 
 */
public class TipoCrimeDaoHibernate extends GenericCrudDaoHibernate implements
		TipoCrimeDao {

	public TipoCrimeDaoHibernate() {
		setEntity(TipoCrime.class);
	}

}
