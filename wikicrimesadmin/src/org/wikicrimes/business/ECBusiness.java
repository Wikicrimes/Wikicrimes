package org.wikicrimes.business;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.EntidadeCertificadora;

public interface ECBusiness {
	
	public List<BaseObject> getEntidadeCertificadoraAll();
	public List<BaseObject> filter(Map parameters);
	public void update(EntidadeCertificadora ec);
	public void cadastrar(EntidadeCertificadora ec);
	

}
