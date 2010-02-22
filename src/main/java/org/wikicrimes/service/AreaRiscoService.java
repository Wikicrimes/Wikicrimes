package org.wikicrimes.service;

import java.util.List;

import org.wikicrimes.model.AreaRisco;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.model.Usuario;

public interface AreaRiscoService extends GenericCrudService {

	public List<AreaRisco> listAreas(Usuario u);
	public boolean save(AreaRisco p);
	public boolean save(PontoLatLng p);
	public boolean save(List<PontoLatLng> l);
	
}
