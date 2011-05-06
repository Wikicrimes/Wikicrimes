package org.wikicrimes.dao;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.Acidente;

public interface AcidenteDao extends GenericCrudDao {
	public List<Acidente> filter(Map<String,Object> parameters);
}
