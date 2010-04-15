package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.ReputacaoDao;
import org.wikicrimes.model.Reputacao;


public class ReputacaoDaoHibernate extends GenericCrudDaoHibernate implements
		ReputacaoDao {

	public ReputacaoDaoHibernate() {
		setEntity(Reputacao.class);
	}

}
