package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.wikicrimes.dao.ReputacaoDao;
import org.wikicrimes.model.Reputacao;

/**
 * 
 */
public class ReputacaoDaoHibernate extends GenericCrudDaoHibernate implements
		ReputacaoDao {

	public ReputacaoDaoHibernate() {
		setEntity(Reputacao.class);
	}

	@Override
	public List<Reputacao> getListReputacao(Long idUsuario) {
		String query = "FROM Reputacao rpt WHERE rpt.usuario.idUsuario = "+ idUsuario;		
		return getHibernateTemplate().find(query);
	}
	
}
