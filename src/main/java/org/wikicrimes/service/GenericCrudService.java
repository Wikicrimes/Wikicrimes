package org.wikicrimes.service;

import java.util.List;

import org.wikicrimes.model.BaseObject;

public interface GenericCrudService extends Service {

	public boolean insert(BaseObject bo);
	
	public boolean update(BaseObject bo);

	public boolean delete(BaseObject bo);
	
	public List<BaseObject> find(BaseObject bo);

	public BaseObject get(Long id);

	public List<BaseObject> getAll();
	
	public boolean exist(BaseObject bo);
}
