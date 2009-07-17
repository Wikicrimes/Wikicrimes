package org.wikicrimes.service;

import java.util.List;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Comentario;
import org.wikicrimes.model.ComentarioRelato;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.ConfirmacaoRelato;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.CrimeRazao;
import org.wikicrimes.model.RedeSocial;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.RelatoRazao;
import org.wikicrimes.model.RepasseRelato;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.model.UsuarioRedeSocial;

public interface OpensocialService {
	
	public List<Crime> getCrimes(List<Usuario> usuarios);
	
	public void registrarRepassesPrimeiraVez(List<RepasseRelato> repasses, UsuarioRedeSocial usuarioRecebimento);
	
	public List<RepasseRelato> getRepasses(List<UsuarioRedeSocial> usuarios);

	public Boolean idCadastrado(UsuarioRedeSocial urs);
	
	public List<UsuarioRedeSocial> getUsuarioRedeSocial(UsuarioRedeSocial urs);
	
	public Usuario getUsuario(UsuarioRedeSocial urs);
	
	public void registrarBaseObject(BaseObject bo);
	
	public List<RepasseRelato> getRelatos(Long idRedeSocial,UsuarioRedeSocial urs);
	
	public List<ComentarioRelato> getComentarios(Relato relato);
	
	public List<Comentario> getComentarios(Crime crime);
	
	public Relato buscaRelato(Relato r);
	
	public Crime buscaCrime(Crime c);
	
	public List<BaseObject> getBaseObjects(BaseObject bo);
	
	public boolean verificaConfirmacao(ConfirmacaoRelato cr);
	
	public boolean verificaConfirmacao(Confirmacao c);
	
	public void registrarRelato(Relato r, List<RelatoRazao> razoes);
	
	public void registrarCrime(Crime c, List<CrimeRazao> razoes);
	
	public boolean verificaSeRepasseFoiRegistrado(RepasseRelato rp);
	
	public void registrarRepasses(String []arrayAmigos, RedeSocial rs, UsuarioRedeSocial ursAux, Relato relato, UsuarioRedeSocial usuarioEnvio);
	
	public void registrarRepassesCrime(String []arrayAmigos, RedeSocial rs, UsuarioRedeSocial ursAux, Crime crime, UsuarioRedeSocial usuarioEnvio);
	
		
}
