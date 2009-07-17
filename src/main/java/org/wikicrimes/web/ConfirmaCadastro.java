package org.wikicrimes.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.UsuarioService;

public class ConfirmaCadastro extends GenericForm {
	private final Log log = LogFactory.getLog(ConfirmaCadastro.class);

	private String id;

	private Usuario usuario;

	private UsuarioService service;

	private String key;
	
	private boolean jaAtivou=false;

	public String getKey() {
			if (key!=null && id!=null && !jaAtivou){
				this.ativar();
				jaAtivou=true;
			}
		
		return key;
	}

	public void setKey(String chave) {
		this.key = chave;
	}

	public ConfirmaCadastro() {

		usuario = new Usuario();
		
	}

	public String ativar() {
		String returnPage = "failure";
		usuario.setEmail(id);
		usuario.setChaveConfirmacao(key);
		List list = service.find(getUsuario());
		if (list.size() > 0) {
			Usuario u =(Usuario) list.get(0);
			u.setConfirmacao(Usuario.TRUE);
			setUsuario(u);
			service.update(u);
			addMessage("usuario.confirmado", getUsuario().getEmail());

			returnPage= "success";
		}
		
		return returnPage;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getId() {
		
		return id;
	}

	public void setId(String email) {
		this.id = email;
	}

	public void setUsuarioService(UsuarioService service) {
		this.service = service;
	}

	
	

}
