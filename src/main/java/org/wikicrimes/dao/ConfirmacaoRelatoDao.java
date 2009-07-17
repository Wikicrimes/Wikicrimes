package org.wikicrimes.dao;



import org.wikicrimes.model.ConfirmacaoRelato;



public interface ConfirmacaoRelatoDao extends GenericCrudDao {
	
	public boolean getConfirmacaoExistente(ConfirmacaoRelato confirmacaoRelato);	
}
