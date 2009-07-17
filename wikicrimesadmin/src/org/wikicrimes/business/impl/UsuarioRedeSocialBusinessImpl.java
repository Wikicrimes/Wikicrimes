package org.wikicrimes.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wikicrimes.business.UsuarioRedeSocialBusiness;
import org.wikicrimes.dao.UsuarioRedeSocialDao;


@Component("usuarioRedeSocialBusiness")
@Transactional(readOnly = true)
public class UsuarioRedeSocialBusinessImpl implements UsuarioRedeSocialBusiness {

	@Autowired
	@Qualifier("usuarioRedeSocialDaoHibernate")
	private UsuarioRedeSocialDao usuarioRedeSocialDao;
		
	public Integer countUsuariosRedeSocial(){
		return usuarioRedeSocialDao.countUsuariosRedeSocial();
	}
		
}
