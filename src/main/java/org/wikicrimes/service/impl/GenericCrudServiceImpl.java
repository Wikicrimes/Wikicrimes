package org.wikicrimes.service.impl;

import java.util.List;

import org.wikicrimes.dao.GenericCrudDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.service.GenericCrudService;

public abstract class GenericCrudServiceImpl implements GenericCrudService {

	private GenericCrudDao dao;
	
	public BaseObject get(Long id) {
		return dao.get(id);
	}
	
	public List<BaseObject> getAll() {
		return dao.getAll();
	}

	public boolean insert(BaseObject bo) {
		return dao.save(bo);
	}

	public boolean update(BaseObject bo) {
		return dao.save(bo);
	}
	
	public boolean delete(BaseObject bo) {
		return dao.delete(bo);
	}

	public GenericCrudDao getDao() {
		return dao;
	}

	public void setDao(GenericCrudDao dao) {
		this.dao = dao;
	}

	public boolean exist(BaseObject bo) {
		return getDao().exist(bo);
	}
	

	public List<BaseObject> find(BaseObject bo) {
	    return dao.find(bo);
	}
	
}
