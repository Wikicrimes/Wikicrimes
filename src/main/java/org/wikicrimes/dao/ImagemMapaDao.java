package org.wikicrimes.dao;

import org.wikicrimes.model.ImagemMapa;

public interface ImagemMapaDao extends GenericCrudDao {
	
	public ImagemMapa get(long id);
	
}
