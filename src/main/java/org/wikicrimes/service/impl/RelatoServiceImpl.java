package org.wikicrimes.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.hibernate.Hibernate;
import org.wikicrimes.dao.RazaoDao;
import org.wikicrimes.dao.RelatoDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.ConfirmacaoRelato;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.RelatoRazao;
import org.wikicrimes.service.ConfirmacaoService;
import org.wikicrimes.service.EmailService;
import org.wikicrimes.service.RelatoService;
import org.wikicrimes.util.Cripto;

public class RelatoServiceImpl extends GenericCrudServiceImpl implements RelatoService {
	
	private RelatoDao relatoDao;
	private RazaoDao razaoDao;
	
	private ConfirmacaoService confirmacaoService;	
	private EmailService emailService;
	
	public List<BaseObject> filter(Map parameters) {
		return relatoDao.filter(parameters);
	}

	public RelatoDao getRelatoDao() {
		return relatoDao;
	}

	public void setRelatoDao(RelatoDao relatoDao) {
		this.relatoDao = relatoDao;
	}
	
	public boolean insert(BaseObject bo) {
		Relato relato = (Relato) bo;

		if (getDao().save(relato)) {
			relato.setChave(Cripto.criptografar(relato.getIdRelato().toString()+relato.getDataHoraRegistro().toString()));
			getDao().save(relato);
			return true;
		}
		
		return false;
	}
	
	public boolean insert(BaseObject bo, List<Razao> razoes) {
		Relato relato = (Relato) bo;

		if (getDao().save(relato)) {
			relato.setChave(Cripto.criptografar(relato.getIdRelato().toString()+relato.getDataHoraRegistro().toString()));
			if(relato.getUsuario().getPerfil().getIdPerfil().equals(new Long(6))){
				relato.setQtdConfPositivas(new Long(1));
			}
			getDao().save(relato);

			for (Razao razao : razoes) {
				RelatoRazao cr = new RelatoRazao();
				cr.setRazao(razao);
				cr.setRelato((Relato)bo);
				razaoDao.save(cr);
			}			
			
			Set<ConfirmacaoRelato> confirmacoes = relato.getConfirmacoes();
			for (ConfirmacaoRelato confirmacao : confirmacoes) {
				confirmacao.setRelato(relato);
				if(relato.getUsuario().getPerfil().getIdPerfil().equals(new Long(6))){
					confirmacao.setConfirma(true);								
				}
				confirmacaoService.insert(confirmacao);
			}
			if(!relato.getUsuario().getPerfil().getIdPerfil().equals(new Long(6))){
				emailService.sendMailConfirmation(relato,FacesContext.getCurrentInstance().getViewRoot().getLocale().toString());			
			}
			return true;
		}		

		return false;
	}
	
	public RazaoDao getRazaoDao() {
		return razaoDao;
	}

	public void setRazaoDao(RazaoDao razaoDao) {
		this.razaoDao = razaoDao;
	}

	public List<BaseObject> listarRazoesSelecionadas(Relato relato) {
		// TODO Auto-generated method stub
		return razaoDao.listarRazoesRelato(relato);
	}	
	
	public List<BaseObject> listarRazoes() {
		// TODO Auto-generated method stub
		return razaoDao.listarRazoes();
	}

	public ConfirmacaoService getConfirmacaoService() {
		return confirmacaoService;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public void setConfirmacaoService(ConfirmacaoService confirmacaoService) {
		this.confirmacaoService = confirmacaoService;
	}
	public Relato get(Long id){
		Relato temp=(Relato) this.getDao().get(id);
		Hibernate.initialize(temp.getUsuario());
		//Hibernate.initialize(temp.getUsuario().getIdiomaPreferencial());
		return temp;
	}
	public Relato getRelato(String chave) {
		Relato c= new Relato();
		c.setChave(chave);
		List<BaseObject> list =this.getDao().find(c);
		if (list.size() != 0){
			Relato temp= (Relato) list.get(0);
			Hibernate.initialize(temp.getUsuario());
			if(temp.getUsuario()!=null)
				Hibernate.initialize(temp.getUsuario().getIdiomaPreferencial());
			return temp;
		}
		else
			return null;
	
}

	@Override
	public void increntaNumConfirmacoes(Relato relato,boolean tipo) {
		// TODO Auto-generated method stub
		relatoDao.increntaNumConfirmacoes(relato,tipo);
	}

	@Override
	public void update(Relato relato, Set<ConfirmacaoRelato> confirmacoes) {
		// TODO Auto-generated method stub
		update(relato);
		for(ConfirmacaoRelato confirmacao:confirmacoes){
			super.update(confirmacao);
		}
		relato.setConfirmacoes(confirmacoes);
		emailService.sendMailConfirmation(relato, FacesContext.getCurrentInstance().getViewRoot().getLocale().toString());
	}
}
