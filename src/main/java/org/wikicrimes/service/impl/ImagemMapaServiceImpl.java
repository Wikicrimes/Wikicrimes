package org.wikicrimes.service.impl;

import java.util.List;

import org.wikicrimes.dao.ImagemMapaDao;
import org.wikicrimes.dao.PontoLatLngDao;
import org.wikicrimes.model.ImagemMapa;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.service.ImagemMapaService;

public class ImagemMapaServiceImpl extends GenericCrudServiceImpl implements ImagemMapaService{

	private ImagemMapaDao imagemMapaDao;
	private PontoLatLngDao pontoLatLngDao;
	
	public ImagemMapa get(int id) {
		return imagemMapaDao.get(id);
	}

	public boolean save(ImagemMapa im) {
		return imagemMapaDao.save(im);
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
	
	
	public ImagemMapaDao getImagemMapaDao() {
		return imagemMapaDao;
	}
	public void setImagemMapaDao(ImagemMapaDao imagemMapaDao) {
		this.imagemMapaDao = imagemMapaDao;
	}
	public PontoLatLngDao getPontoLatLngDao() {
		return pontoLatLngDao;
	}
	public void setPontoLatLngDao(PontoLatLngDao pontoLatLngDao) {
		this.pontoLatLngDao = pontoLatLngDao;
	}
	
}
