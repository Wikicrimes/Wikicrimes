package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.AreaRisco;
import org.wikicrimes.model.Usuario;


public interface AreaRiscoDao extends GenericCrudDao {

	public List<AreaRisco> list(Usuario u);
	
}
