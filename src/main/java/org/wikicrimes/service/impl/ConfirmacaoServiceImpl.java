package org.wikicrimes.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.wikicrimes.dao.ConfirmacaoDao;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.TipoConfirmacao;
import org.wikicrimes.service.ConfirmacaoService;

public class ConfirmacaoServiceImpl extends GenericCrudServiceImpl implements
		ConfirmacaoService {

	public boolean getJaConfirmou(Confirmacao confirmacao) {
		
		return ((ConfirmacaoDao) this.getDao()).getConfirmacaoExistente(confirmacao);
	}
		
	public Confirmacao getConfirmacao(Long id){
		Confirmacao temp = (Confirmacao)this.getDao().get(id);
		Hibernate.initialize(temp.getCrime().getTipoCrime());
		Hibernate.initialize(temp.getCrime().getTipoCrime().getNome());
		Hibernate.initialize(temp.getCrime().getTipoCrime().getIdTipoCrime());
		Hibernate.initialize(temp.getCrime().getTipoVitima().getNome());
		Hibernate.initialize(temp.getCrime().getTipoArmaUsada());
		Hibernate.initialize(temp.getCrime().getTipoLocal());
		Hibernate.initialize(temp.getCrime().getTipoLocalRoubo());
		Hibernate.initialize(temp.getCrime().getTipoPapel());
		Hibernate.initialize(temp.getCrime().getTipoTransporte());
		Hibernate.initialize(temp.getCrime().getTipoRegistro());
		Hibernate.initialize(temp.getCrime().getUsuario());
		Hibernate.initialize(temp.getTipoConfirmacao());
		Hibernate.initialize(temp.getUsuario());
		
		return temp;
	}

	
	public List<TipoConfirmacao> getTipoConfirmacoes(boolean positivas) {
		return ((ConfirmacaoDao) this.getDao()).getTipoConfirmacoes(positivas);
		
	}
	
	public TipoConfirmacao getTipoConfirmacao(Long id){
		return ((ConfirmacaoDao) this.getDao()).getTipoConfirmacao(id);
	}
	
	public Boolean verificaSeJaIndicou(Crime c, String email){
		return ((ConfirmacaoDao) this.getDao()).verificaSeJaIndicou(c, email);
	}
	
	public Boolean verificaSeJaIndicou(Relato r, String email){
		return ((ConfirmacaoDao) this.getDao()).verificaSeJaIndicou(r, email);
	}
}
