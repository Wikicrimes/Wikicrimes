package org.wikicrimes.service.impl;

import org.hibernate.Hibernate;
import org.wikicrimes.dao.ConfirmacaoRelatoDao;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.ConfirmacaoRelato;
import org.wikicrimes.service.ConfirmacaoRelatoService;

public class ConfirmacaoRelatoServiceImpl extends GenericCrudServiceImpl implements
		ConfirmacaoRelatoService {
	
	private ConfirmacaoRelatoDao confirmacaoRelatoDao;
	
	public ConfirmacaoRelatoDao getConfirmacaoRelatoDao() {
		return confirmacaoRelatoDao;
	}

	public void setConfirmacaoRelatoDao(ConfirmacaoRelatoDao confirmacaoRelatoDao) {
		this.confirmacaoRelatoDao = confirmacaoRelatoDao;
	}

	public boolean getJaConfirmou(ConfirmacaoRelato confirmacao) {
		
		return confirmacaoRelatoDao.getConfirmacaoExistente(confirmacao);
	}
		
	public ConfirmacaoRelato getConfirmacao(Long id){
		ConfirmacaoRelato temp = (ConfirmacaoRelato)this.getDao().get(id);	
		Hibernate.initialize(temp.getRelato());
		return temp;
	}
}
