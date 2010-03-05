package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.Reputacao;


public interface ReputacaoDao extends GenericCrudDao {
	
	public List<Reputacao> getListReputacao(Long idUsuario);
}
