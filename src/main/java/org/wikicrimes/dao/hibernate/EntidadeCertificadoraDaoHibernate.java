package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.EntidadeCertificadoraDao;
import org.wikicrimes.model.EntidadeCertificadora;

/**
 * 
 */
public class EntidadeCertificadoraDaoHibernate extends GenericCrudDaoHibernate
		implements EntidadeCertificadoraDao {

	public EntidadeCertificadoraDaoHibernate() {
		setEntity(EntidadeCertificadora.class);
	}

}
