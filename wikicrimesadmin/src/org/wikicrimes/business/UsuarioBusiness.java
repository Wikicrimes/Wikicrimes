package org.wikicrimes.business;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Usuario;

public interface UsuarioBusiness {

	
	public List<BaseObject> autenticar(Usuario u);
	public void update(Usuario usuario);
	public List<BaseObject> filter(Map parameters);
	public List<BaseObject> getUserById(String id);
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
