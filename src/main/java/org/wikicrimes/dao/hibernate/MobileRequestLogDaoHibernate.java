package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.MobileRequestLogDao;
import org.wikicrimes.model.AreaRisco;

public class MobileRequestLogDaoHibernate extends GenericCrudDaoHibernate implements MobileRequestLogDao{

	public MobileRequestLogDaoHibernate() {
		setEntity(AreaRisco.class);
	}
	
}
