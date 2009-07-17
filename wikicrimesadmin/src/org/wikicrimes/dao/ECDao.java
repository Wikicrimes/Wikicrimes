package org.wikicrimes.dao;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.EntidadeCertificadora;

public interface ECDao {
	
	public List<BaseObject> getall();
	public List<BaseObject> filter(Map parameters);
	public void update(EntidadeCertificadora ec);
	public void cadastrar(EntidadeCertificadora ec);

}
