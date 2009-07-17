package org.wikicrimes.dao;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Relato;

public interface RelatoDao extends GenericCrudDao {
	public List<BaseObject> filter(Map parameters);
	public void increntaNumConfirmacoes(Relato relato,boolean tipo);
}
