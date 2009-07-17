package org.wikicrimes.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.wikicrimes.dao.OpensocialDao;
import org.wikicrimes.dao.hibernate.OpensocialDaoHibernate;
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
import org.wikicrimes.service.OpensocialService;

public class OpensocialServiceImpl extends GenericCrudServiceImpl implements OpensocialService{	
	
	private OpensocialDao opensocialDao;
	
	public List<Crime> getCrimes(List<Usuario> usuarios) {
		opensocialDao = (OpensocialDaoHibernate)getDao();
		return opensocialDao.getCrimes(usuarios);
	}

	public Boolean idCadastrado(UsuarioRedeSocial urs) {
		// TODO Auto-generated method stub
		opensocialDao = (OpensocialDaoHibernate)getDao();
		return opensocialDao.idCadastrado(urs);		
	}
	
	public Usuario getUsuario(UsuarioRedeSocial urs){
		return opensocialDao.getUsuario(urs);
	}
	
	public void registrarBaseObject(BaseObject bo){
		opensocialDao.save(bo);
	}
	
	public List<BaseObject> getBaseObjects(BaseObject bo){
		return opensocialDao.find(bo);
	}
	
	public List<RepasseRelato> getRelatos(Long idRedeSocial,UsuarioRedeSocial urs){
		return opensocialDao.getRelatos(idRedeSocial,urs);
	}
	
	public List<ComentarioRelato> getComentarios(Relato relato){
		return opensocialDao.getComentarios(relato);
	}
	
	public void registrarRepasses(String []arrayAmigos, RedeSocial rs, UsuarioRedeSocial ursAux, Relato relato, UsuarioRedeSocial usuarioEnvio){
		for (int i = 0; i < arrayAmigos.length; i++) {
			UsuarioRedeSocial urs = new UsuarioRedeSocial();
			urs.setIdUsuarioDentroRedeSocial(arrayAmigos[i]);				
			urs.setRedeSocial(rs);
			List<UsuarioRedeSocial> list = this.getUsuarioRedeSocial(urs);
			if(list!=null)
				ursAux = list.get(0);
			if(ursAux != null){
				RepasseRelato rp = new RepasseRelato();
				rp.setRelato(relato);
				rp.setUsuarioRecebimento(ursAux);
				rp.setUsuarioEnvio(usuarioEnvio);
				rp.setDataHoraRegistro(new Date());
				if(!this.verificaSeRepasseFoiRegistrado(rp)){
					this.registrarBaseObject(rp);
				}
			}
			ursAux = null;
		}
	}
	
	@Override
	public void registrarRepassesCrime(String []arrayAmigos, RedeSocial rs, UsuarioRedeSocial ursAux, Crime crime, UsuarioRedeSocial usuarioEnvio){
		for (int i = 0; i < arrayAmigos.length; i++) {
			UsuarioRedeSocial urs = new UsuarioRedeSocial();
			urs.setIdUsuarioDentroRedeSocial(arrayAmigos[i]);				
			urs.setRedeSocial(rs);
			List<UsuarioRedeSocial> list = this.getUsuarioRedeSocial(urs);
			if(list!=null)
				ursAux = list.get(0);
			if(ursAux != null){
				RepasseRelato rp = new RepasseRelato();
				rp.setCrime(crime);
				rp.setUsuarioRecebimento(ursAux);
				rp.setUsuarioEnvio(usuarioEnvio);
				rp.setDataHoraRegistro(new Date());
				if(!this.verificaSeRepasseFoiRegistrado(rp)){
					this.registrarBaseObject(rp);
				}
			}
			ursAux = null;
		}
		
	}
	
	public void registrarRepassesPrimeiraVez(List<RepasseRelato> repasses, UsuarioRedeSocial usuarioRecebimento){
		for (Iterator<RepasseRelato> iterator = repasses.iterator(); iterator.hasNext();) {
			RepasseRelato repasseRelato = (RepasseRelato) iterator.next();
			repasseRelato.setUsuarioRecebimento(usuarioRecebimento);
			repasseRelato.setIdRepasseRelato(null);
			repasseRelato.setDataHoraRegistro(new Date());			
			this.registrarBaseObject(repasseRelato);
		}		
	}
	
	public List<UsuarioRedeSocial> getUsuarioRedeSocial(UsuarioRedeSocial urs) {
		opensocialDao = (OpensocialDaoHibernate)getDao();
		return opensocialDao.getUsuarioRedeSocial(urs);
	}
	
	public Relato buscaRelato(Relato r){
		return (Relato)((List<BaseObject>)opensocialDao.find(r)).get(0);
	}

	public boolean verificaConfirmacao(ConfirmacaoRelato cr) {
		// TODO Auto-generated method stub
		return opensocialDao.verificaConfirmacao(cr);
	}

	public void registrarRelato(Relato r, List<RelatoRazao> razoes) {
		registrarBaseObject(r);
		for (RelatoRazao relatoRazao : razoes) {
			registrarBaseObject(relatoRazao);
		}
		
	}
	
	@Override
	public void registrarCrime(Crime c, List<CrimeRazao> razoes) {
		registrarBaseObject(c);
		for (CrimeRazao crimeRazao : razoes) {
			registrarBaseObject(crimeRazao);
		}
	}
	
	public boolean verificaSeRepasseFoiRegistrado(RepasseRelato rp){
		return opensocialDao.verificaSeRepasseFoiRegistrado(rp);
	}

	@Override
	public Crime buscaCrime(Crime c) {
		// TODO Auto-generated method stub
		return (Crime)((List<BaseObject>)opensocialDao.find(c)).get(0);
	}

	@Override
	public boolean verificaConfirmacao(Confirmacao c) {
		// TODO Auto-generated method stub		
		return opensocialDao.verificaConfirmacao(c);
	}

	@Override
	public List<Comentario> getComentarios(Crime crime) {
		// TODO Auto-generated method stub
		return opensocialDao.getComentarios(crime);
	}

	@Override
	public List<RepasseRelato> getRepasses(List<UsuarioRedeSocial> usuarios) {
		// TODO Auto-generated method stub
		return opensocialDao.getRepasses(usuarios);
	}

}
