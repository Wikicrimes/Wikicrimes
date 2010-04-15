package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.CredibilidadeDao;
import org.wikicrimes.model.Credibilidade;

public class CredibilidadeDaoHibernate extends GenericCrudDaoHibernate implements
		CredibilidadeDao {

	public CredibilidadeDaoHibernate() {
		setEntity(Credibilidade.class);
	}
	
}
