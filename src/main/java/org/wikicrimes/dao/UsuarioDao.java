package org.wikicrimes.dao;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.AreaObservacao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Usuario;

public interface UsuarioDao extends GenericCrudDao {
	
	public Usuario getByEmail(String email);
	public List<BaseObject> filter(Map parameters);
	public List<BaseObject> getUsuariosConfirmados();	
	public List<AreaObservacao> getAreas(Usuario u);
}
