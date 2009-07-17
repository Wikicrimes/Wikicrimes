package org.wikicrimes.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wikicrimes.business.RelatoBusiness;
import org.wikicrimes.dao.RelatoDao;
import org.wikicrimes.model.Relato;

@Component("relatoBusiness")
@Transactional(readOnly = true)
public class RelatoBusinessImpl implements RelatoBusiness {

	@Autowired
	@Qualifier("relatoDaoHibernate")
	private RelatoDao relatoDao;

	public RelatoDao getRelatoDao() {
		return relatoDao;
	}

	public void setRelatoDao(RelatoDao relatoDao) {
		this.relatoDao = relatoDao;
	}

	@Override
	public List<Relato> getRelatosSemEndereco() {
		return relatoDao.getRelatosSemEndereco();
	}

	@Override
	public Relato getRelatoById(Long id) {
		return relatoDao.getRelatoById(id);
	}

	@Transactional(readOnly=false)
	public void update(Relato r) {
		relatoDao.update(r);
	}

}
