package org.wikicrimes.service;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.wikicrimes.model.AreaObservacao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.model.UsuarioRedeSocial;;

public interface UsuarioService extends GenericCrudService {

	public Usuario login(Usuario usario);
	public Resource getTxtTermoUso();
	public Resource getTxtTermoUso_EN();
	public Resource getTxtTermoUso_ES();
	public Usuario retornaUsuarioConfirmacao(String email, String idioma);
	public Usuario getUsuario(String email);
	public void cadastrarAreaObservacao(AreaObservacao area);
	public List<BaseObject> filter(Map parameters);
	public List<BaseObject> UsuariosConf();
	public boolean alterarUsuario(Usuario usuario);
	public void salvar(UsuarioRedeSocial urs);	
	public List<AreaObservacao> getAreas(Usuario u);
	public void excluirAreaObservacao(AreaObservacao area);
}
