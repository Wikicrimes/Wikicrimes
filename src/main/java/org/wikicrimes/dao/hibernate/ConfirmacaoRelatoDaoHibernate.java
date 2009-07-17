package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.wikicrimes.dao.ConfirmacaoRelatoDao;
import org.wikicrimes.model.ConfirmacaoRelato;

/**
 * 
 */
public class ConfirmacaoRelatoDaoHibernate extends GenericCrudDaoHibernate implements
		ConfirmacaoRelatoDao {

	public ConfirmacaoRelatoDaoHibernate() {
		setEntity(ConfirmacaoRelato.class);
	}
	

	public boolean getConfirmacaoExistente(ConfirmacaoRelato confirmacaoRelato){
		String query = "from ConfirmacaoRelato cr where cr.usuario.idUsuario = "+confirmacaoRelato.getUsuario().getIdUsuario()+" and cr.relato.idRelato = "+confirmacaoRelato.getRelato().getIdRelato();
				
		List list = getHibernateTemplate().find(query);
		if (list.size()>0)
			return true;
		else
			return false;
	}
	
	
}
