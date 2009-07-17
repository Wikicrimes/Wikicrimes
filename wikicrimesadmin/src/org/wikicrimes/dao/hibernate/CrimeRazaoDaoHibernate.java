package org.wikicrimes.dao.hibernate;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.wikicrimes.dao.CrimeRazaoDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.CrimeRazao;

@Component("crimeRazaoDaoHibernate")
public class CrimeRazaoDaoHibernate extends HibernateDaoGenerico
implements CrimeRazaoDao{
	
	private Class entity;
	
	public CrimeRazaoDaoHibernate() {
		entity = BaseObject.class;
	}
	
	public void deleteCrimeRazao(Set<CrimeRazao> razoesCrimes){
		getHibernateTemplate().deleteAll(razoesCrimes);
		getHibernateTemplate().flush();
	}

	@Override
	protected Class getClasseEntidade() {
		// TODO Auto-generated method stub
		return null;
	}

}
