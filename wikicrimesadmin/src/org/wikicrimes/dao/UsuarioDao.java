package org.wikicrimes.dao;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Usuario;

public interface UsuarioDao {
	
	public List<BaseObject> autenticar(Usuario usuario);
	public void update(Usuario usuario);
	public List<BaseObject> filter(Map parameters);
	public List<BaseObject> GetUserById(String id);
	public List<BaseObject> getTotalUsuarios();
	public Integer countTotalUsuarios();
	public List<BaseObject> getUsuariosConf();
	public Integer countUsuariosConf();
	public List<BaseObject> getUsuariosNConf();
	public Integer countUsuariosNConf();
	public List<BaseObject> getUsuariosConv();
	public Integer countUsuariosConv();
	public List<BaseObject> getUsuariosAdm();
	public Integer countUsuariosAdmin();
	public List<BaseObject> getUsuariosArea();
	public Integer countUsuariosArea();
			
}