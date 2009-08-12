package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.wikicrimes.dao.ConfirmacaoDao;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.TipoConfirmacao;
import org.wikicrimes.model.Usuario;

/**
 * 
 */
public class ConfirmacaoDaoHibernate extends GenericCrudDaoHibernate implements
		ConfirmacaoDao {

	public ConfirmacaoDaoHibernate() {
		setEntity(Confirmacao.class);
	}
	public boolean getConfirmacaoExistente(Confirmacao confirmacao){
		String query = "from Confirmacao where " +
				"cri_idcrime = " +confirmacao.getCrime().getIdCrime() + " and usu_idusuario=" + confirmacao.getUsuario().getIdUsuario();
		List list = getHibernateTemplate().find(query);
		if (list.size()>0)
			return true;
		else
			return false;
	}
	@Override
	public List<TipoConfirmacao> getTipoConfirmacoes(boolean positivas) {
		String query = "from TipoConfirmacao where positiva = " + positivas;
		return (List<TipoConfirmacao>) getHibernateTemplate().find(query);
	}
	@Override
	public TipoConfirmacao getTipoConfirmacao(Long id) {
		String query = "from TipoConfirmacao where idTipoConfirmacao = " + id;
		 List result= getHibernateTemplate().find(query);
		 if (result!=null)
			 return (TipoConfirmacao) result.get(0);
		 else
			 return null;
	}
	
	public Boolean verificaSeJaIndicou(Crime c, String email){
		String query = "from Confirmacao conf "+
		"where conf.usuario.email like '"+email+"' and conf.crime.idCrime ="+c.getIdCrime();
		
		List<Confirmacao> result= getHibernateTemplate().find(query);
		if(result.size()>0)
			return true;
		else
			return false;
	}

	
}
