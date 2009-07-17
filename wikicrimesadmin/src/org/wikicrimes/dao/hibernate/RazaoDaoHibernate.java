package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Component;
import org.wikicrimes.dao.RazaoDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Relato;

//faltou apenas esse componente Phil
@Component("razaoDaoHibernate")
public class RazaoDaoHibernate extends HibernateDaoGenerico implements
RazaoDao{
	
	private Class entity;
	
	public RazaoDaoHibernate() {
		entity = BaseObject.class;
	}

	public List<BaseObject> listarRazoes() {
		String query = "from Razao";		
		return getHibernateTemplate().find(query);
	}

	public List<BaseObject> listarRazoesRelato(Relato relato) {
		String query = "from Razao razao where razao.idRazao in (select relatoRazao.razao.idRazao from RelatoRazao relatoRazao where relatoRazao.relato.idRelato ="+relato.getIdRelato() + ")";
		return getHibernateTemplate().find(query);
	}

	@Override
	protected Class getClasseEntidade() {
		// TODO Auto-generated method stub
		return null;
	}
}
