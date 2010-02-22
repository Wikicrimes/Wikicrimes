package org.wikicrimes.service.impl;

import java.util.List;
import java.util.Set;

import org.wikicrimes.dao.AreaRiscoDao;
import org.wikicrimes.dao.PontoLatLngDao;
import org.wikicrimes.model.AreaRisco;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.AreaRiscoService;

public class AreaRiscoServiceImpl extends GenericCrudServiceImpl implements AreaRiscoService {
	
	private AreaRiscoDao areaRiscoDao;
	private PontoLatLngDao pontoLatLngDao;

	public List<AreaRisco> listAreas(Usuario u) {
		return areaRiscoDao.list(u);
	}
	
	public boolean save(AreaRisco a){
		return areaRiscoDao.save(a);
	}
	
	public boolean save(PontoLatLng p) {
		return pontoLatLngDao.save(p);
	}

	public boolean save(List<PontoLatLng> set) {
		boolean bool = true;
		for(PontoLatLng p : set)
			bool &= save(p);
		return bool;
	}
	
	public AreaRiscoDao getAreaRiscoDao() {
		return areaRiscoDao;
	}
	public void setAreaRiscoDao(AreaRiscoDao areaRiscoDao) {
		this.areaRiscoDao = areaRiscoDao;
	}
	public PontoLatLngDao getPontoLatLngDao() {
		return pontoLatLngDao;
	}
	public void setPontoLatLngDao(PontoLatLngDao pontoLatLngDao) {
		this.pontoLatLngDao = pontoLatLngDao;
	}
	
	
}
