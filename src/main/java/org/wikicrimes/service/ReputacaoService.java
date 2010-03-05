package org.wikicrimes.service;

import java.util.List;

import org.wikicrimes.model.Reputacao;



public interface ReputacaoService extends GenericCrudService {
	
	public List<Reputacao> getListReputacao(Long idUsuario);
	
}
