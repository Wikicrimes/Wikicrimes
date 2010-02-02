package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Relato;

public interface RazaoDao extends GenericCrudDao {
	public List<BaseObject> listarRazoes();
	public List<BaseObject> listarRazoesRelato(Relato relato);
}
