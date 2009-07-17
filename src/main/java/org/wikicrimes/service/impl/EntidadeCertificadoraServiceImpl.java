package org.wikicrimes.service.impl;

import org.wikicrimes.dao.EntidadeCertificadoraDao;
import org.wikicrimes.service.EntidadeCertificadoraService;


public class EntidadeCertificadoraServiceImpl extends GenericCrudServiceImpl implements
EntidadeCertificadoraService {
	
	EntidadeCertificadoraDao entidadeCertificadoraDao;

	public EntidadeCertificadoraDao getEntidadeCertificadoraDao() {
		return entidadeCertificadoraDao;
	}

	public void setEntidadeCertificadoraDao(
			EntidadeCertificadoraDao entidadeCertificadoraDao) {
		this.entidadeCertificadoraDao = entidadeCertificadoraDao;
	}

	
	
		
}