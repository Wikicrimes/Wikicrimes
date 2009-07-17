package org.wikicrimes.service;

import org.wikicrimes.model.ConfirmacaoRelato;


public interface ConfirmacaoRelatoService extends GenericCrudService {
	
	public boolean getJaConfirmou(ConfirmacaoRelato confirmacao);
	
	public ConfirmacaoRelato getConfirmacao(Long id);
		
}
