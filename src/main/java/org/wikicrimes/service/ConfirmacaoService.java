package org.wikicrimes.service;

import java.util.List;

import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.TipoConfirmacao;


public interface ConfirmacaoService extends GenericCrudService {
	
	public boolean getJaConfirmou(Confirmacao confirmacao);
	
	public Confirmacao getConfirmacao(Long id);
	
	public List<TipoConfirmacao> getTipoConfirmacoes(boolean positivas);
	
	public TipoConfirmacao getTipoConfirmacao(Long id);
	
	public Boolean verificaSeJaIndicou(Crime c, String email);
	
	public Boolean verificaSeJaIndicou(Relato r, String email);
		
}
