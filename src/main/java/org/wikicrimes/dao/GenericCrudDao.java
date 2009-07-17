package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.BaseObject;

public interface GenericCrudDao extends Dao {

	public boolean save(BaseObject bo);

	public boolean delete(BaseObject bo);

	public BaseObject get(Long id);
	
	public List<BaseObject> getAll();

	public boolean exist(BaseObject bo);

	public List<BaseObject> find(BaseObject bo);

}