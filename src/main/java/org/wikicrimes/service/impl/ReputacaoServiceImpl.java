package org.wikicrimes.service.impl;

import org.wikicrimes.dao.ReputacaoDao;
import org.wikicrimes.service.ReputacaoService;

public class ReputacaoServiceImpl extends GenericCrudServiceImpl implements
		ReputacaoService {

	private ReputacaoDao reputacaoDao;

	
	/** Gets and Sets **/	
	public ReputacaoDao getReputacaoDao() {
		return reputacaoDao;
	}
	public void setReputacaoDao(ReputacaoDao reputacaoDao) {
		this.reputacaoDao = reputacaoDao;
	}

}
