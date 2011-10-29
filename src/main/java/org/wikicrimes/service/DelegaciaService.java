package org.wikicrimes.service;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Delegacia;

public interface DelegaciaService extends GenericCrudService {
	public List<BaseObject> filter(Map parameters);
	public List<BaseObject> getDelegaciaList();
	public Delegacia getDelegaciaPorChave(String chave);
	
}
