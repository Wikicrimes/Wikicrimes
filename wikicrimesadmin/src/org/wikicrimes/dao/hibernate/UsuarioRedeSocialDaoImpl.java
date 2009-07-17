package org.wikicrimes.dao.hibernate;

import org.springframework.stereotype.Component;
import org.wikicrimes.dao.UsuarioRedeSocialDao;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.model.UsuarioRedeSocial;


@Component("usuarioRedeSocialDaoHibernate")
public class UsuarioRedeSocialDaoImpl extends HibernateDaoGenerico<UsuarioRedeSocial> implements
UsuarioRedeSocialDao {

	@Override
	protected Class<UsuarioRedeSocial> getClasseEntidade() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Integer countUsuariosRedeSocial(){
		String query = "select count(distinct u.idUsuarioRedeSocial) from UsuarioRedeSocial u";
		Long cont = (Long) getHibernateTemplate().find(query).iterator().next();

		return cont.intValue();
	}

}
