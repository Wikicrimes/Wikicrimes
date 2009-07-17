package org.wikicrimes.business.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wikicrimes.business.UsuarioBusiness;
import org.wikicrimes.dao.UsuarioDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Usuario;


@Component("usuarioBusiness")
@Transactional(readOnly = true)
public class UsuarioBusinessImpl implements UsuarioBusiness {

	@Autowired
	@Qualifier("usuarioDaoHibernate")
	private UsuarioDao usuarioDao;

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public List<BaseObject> autenticar(Usuario u){
			
		return usuarioDao.autenticar(u);
	}

	
	public List<BaseObject> filter(Map parameters) {
		return usuarioDao.filter(parameters);
	}
	@Transactional(readOnly=false)
	public void update(Usuario usuario){
		usuarioDao.update(usuario);
	}
	
	public List<BaseObject> getUserById(String id){
		return usuarioDao.GetUserById(id);
	}
	
	public List<BaseObject> getTotalUsuarios(){
		return usuarioDao.getTotalUsuarios();
	}
	public Integer countTotalUsuarios(){
		return usuarioDao.countTotalUsuarios();
	}
	
	public List<BaseObject> getUsuariosConf(){
		return usuarioDao.getUsuariosConf();
	}
	public Integer countUsuariosConf(){
		return usuarioDao.countUsuariosConf();
	}
	
	public List<BaseObject> getUsuariosNConf(){
		return usuarioDao.getUsuariosNConf();
	}
	public Integer countUsuariosNConf(){
		return usuarioDao.countUsuariosNConf();
	}
	
	public List<BaseObject> getUsuariosConv(){
		return usuarioDao.getUsuariosConv();
	}
	public Integer countUsuariosConv(){
		return usuarioDao.countUsuariosConv();
	}
	
	public List<BaseObject> getUsuariosAdm(){
		return usuarioDao.getUsuariosAdm();
	}
	public Integer countUsuariosAdmin(){
		return usuarioDao.countUsuariosAdmin();
	}
	public List<BaseObject> getUsuariosArea(){
		return usuarioDao.getUsuariosArea();
	}
	public Integer countUsuariosArea(){
		return usuarioDao.countUsuariosArea();
	}
		
}
