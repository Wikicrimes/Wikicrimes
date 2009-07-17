package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.wikicrimes.dao.RazaoDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.Relato;

/**
 * 
 */
public class RazaoDaoHibernate extends GenericCrudDaoHibernate implements
		RazaoDao {

	public RazaoDaoHibernate() {
		setEntity(Razao.class);
	}

	public List<BaseObject> listarRazoes() {
		String query = "from Razao";		
		return getHibernateTemplate().find(query);
	}

	public List<BaseObject> listarRazoesRelato(Relato relato) {
		String query = "from Razao razao where razao.idRazao in (select relatoRazao.razao.idRazao from RelatoRazao relatoRazao where relatoRazao.relato.idRelato ="+relato.getIdRelato() + ")";
		return getHibernateTemplate().find(query);
	}
}
