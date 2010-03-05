package org.wikicrimes.service.impl;

import org.wikicrimes.dao.CredibilidadeDao;
import org.wikicrimes.service.CredibilidadeService;

public class CredibilidadeServiceImpl extends GenericCrudServiceImpl implements
		CredibilidadeService {

	private CredibilidadeDao credibilidadeDao;

	
	
	/** Gets and Sets **/
	public CredibilidadeDao getCredibilidadeDao() {
		return credibilidadeDao;
	}
	public void setCredibilidadeDao(CredibilidadeDao credibilidadeDao) {
		this.credibilidadeDao = credibilidadeDao;
	}
	
}
