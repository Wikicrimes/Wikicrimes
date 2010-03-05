package org.wikicrimes.service.impl;

import java.util.List;

import org.wikicrimes.dao.ReputacaoDao;
import org.wikicrimes.model.Reputacao;
import org.wikicrimes.service.ReputacaoService;

public class ReputacaoServiceImpl extends GenericCrudServiceImpl implements
		ReputacaoService {

	private ReputacaoDao reputacaoDao;

	@Override
	public List<Reputacao> getListReputacao(Long idUsuario) {
		return reputacaoDao.getListReputacao(idUsuario);
	}

	
	/** Gets and Sets **/	
	public ReputacaoDao getReputacaoDao() {
		return reputacaoDao;
	}
	public void setReputacaoDao(ReputacaoDao reputacaoDao) {
		this.reputacaoDao = reputacaoDao;
	}

}
