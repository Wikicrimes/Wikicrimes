package org.wikicrimes.dao;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Delegacia;

public interface DelegaciaDao extends GenericCrudDao {

	public List<BaseObject> filter(Map parameters);
	public Delegacia getDelegaciaPorChave(String chave);
	
}
