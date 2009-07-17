package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Component;
import org.wikicrimes.dao.RelatoDao;
import org.wikicrimes.model.Relato;

@Component("relatoDaoHibernate")
public class RelatoDaoImpl extends HibernateDaoGenerico<Relato> implements RelatoDao{

	@Override
	protected Class<Relato> getClasseEntidade() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Método que retorna os relatos sem endereços.
	 */
	public List<Relato> getRelatosSemEndereco() {

		String query = "from Relato r where r.pais is null and r.estado is null and r.cidade is null and r.endereco is null order by r.idRelato desc";

		List<Relato> relatos = getHibernateTemplate().find(query);

		return relatos;
	}

	@Override
	public Relato getRelatoById(Long id) {
		String query = "from Relato r where r.idRelato = "+id;

		return (Relato)getHibernateTemplate().find(query).get(0);
	}

	@Override
	public void update(Relato r) {
		super.atualizarEntidade(r);
	}
}
