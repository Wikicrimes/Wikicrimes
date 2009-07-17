package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.Comentario;
import org.wikicrimes.model.ComentarioRelato;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.ConfirmacaoRelato;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.RepasseRelato;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.model.UsuarioRedeSocial;

public interface OpensocialDao extends GenericCrudDao {
	
	public Boolean idCadastrado(UsuarioRedeSocial usuario);
	
	public List<Crime> getCrimes(List<Usuario> usuarios);
	
	public List<RepasseRelato> getRepasses(List<UsuarioRedeSocial> usuarios);
	
	public Usuario getUsuario(UsuarioRedeSocial urs);	
	
	public List<org.wikicrimes.model.RepasseRelato> getRelatos(Long idRedeSocial,UsuarioRedeSocial urs);
	
	public List<ComentarioRelato> getComentarios(Relato relato);
	
	public List<Comentario> getComentarios(Crime crime);
	
	public List<UsuarioRedeSocial> getUsuarioRedeSocial(UsuarioRedeSocial urs);
	
	public boolean verificaConfirmacao(ConfirmacaoRelato cr);
	
	public boolean verificaConfirmacao(Confirmacao c);
	
	public boolean verificaSeRepasseFoiRegistrado(RepasseRelato rp);
	
}
