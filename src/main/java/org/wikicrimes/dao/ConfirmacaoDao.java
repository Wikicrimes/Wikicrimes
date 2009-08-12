package org.wikicrimes.dao;



import java.util.List;

import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.TipoConfirmacao;



public interface ConfirmacaoDao extends GenericCrudDao {
	public boolean getConfirmacaoExistente(Confirmacao confirmacao);
	
	public List<TipoConfirmacao> getTipoConfirmacoes(boolean positivas);
	public TipoConfirmacao getTipoConfirmacao(Long id);
	public Boolean verificaSeJaIndicou(Crime c, String email);
		
}
