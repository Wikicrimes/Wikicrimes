package org.wikicrimes.service;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.Acidente;

public interface AcidenteService extends GenericCrudService {	
	public List<Acidente> filter(Map<String, Object> parameters);
	public boolean insert(Acidente aci);
	public Acidente getAcidente(String chave);
	public Acidente get(Long idAcidente);
}
