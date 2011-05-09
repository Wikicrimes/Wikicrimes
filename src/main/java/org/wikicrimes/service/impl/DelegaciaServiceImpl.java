package org.wikicrimes.service.impl;

import java.util.List;
import java.util.Map;

import org.wikicrimes.dao.DelegaciaDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Delegacia;
import org.wikicrimes.service.DelegaciaService;

public class DelegaciaServiceImpl extends GenericCrudServiceImpl implements
		DelegaciaService {
	private DelegaciaDao delegaciaDao;
	private Delegacia delegacia;
	private DelegaciaService service;
	
	@Override
	public List<BaseObject> getDelegaciaList() {
		
		return null;
	}
	
	public List<BaseObject> filter(Map parameters) {
		return delegaciaDao.filter(parameters);
	}

	public DelegaciaDao getDelegaciaDao() {
		return delegaciaDao;
	}

	public void setDelegaciaDao(DelegaciaDao delegaciaDao) {
		this.delegaciaDao = delegaciaDao;
	}
	
	public Delegacia getDelegaciaPorChave(String chave) {
		Delegacia d = (Delegacia)delegaciaDao.getDelegaciaPorChave(chave);
		return d;
	}
	
}
