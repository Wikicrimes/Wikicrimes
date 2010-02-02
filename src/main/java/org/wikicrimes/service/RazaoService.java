package org.wikicrimes.service;

import java.util.List;

import org.wikicrimes.model.BaseObject;

public interface RazaoService extends GenericCrudService {	
	public List<BaseObject> listarRazoes();
}
