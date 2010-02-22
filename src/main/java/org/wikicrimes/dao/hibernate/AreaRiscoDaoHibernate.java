package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.wikicrimes.dao.AreaRiscoDao;
import org.wikicrimes.model.AreaRisco;
import org.wikicrimes.model.Usuario;

public class AreaRiscoDaoHibernate extends GenericCrudDaoHibernate implements AreaRiscoDao{

	public AreaRiscoDaoHibernate() {
		setEntity(AreaRisco.class);
	}
	
	public List<AreaRisco> list(Usuario u){
		String query = "FROM AreaRisco a WHERE a.usuario.idUsuario =" + u.getIdUsuario();
		return getHibernateTemplate().find(query);
	}
	
}
