package org.wikicrimes.service;

import java.util.List;

import org.wikicrimes.model.ImagemMapa;
import org.wikicrimes.model.PontoLatLng;

public interface ImagemMapaService extends GenericCrudService {
	
	public ImagemMapa get(int id);
	public boolean save(ImagemMapa im);
	public boolean save(PontoLatLng p);
	public boolean save(List<PontoLatLng> l);

}
