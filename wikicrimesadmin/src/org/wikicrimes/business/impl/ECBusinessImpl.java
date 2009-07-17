package org.wikicrimes.business.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wikicrimes.business.ECBusiness;
import org.wikicrimes.dao.ECDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.EntidadeCertificadora;

@Component("eCBusiness")
@Transactional(readOnly = true)
public class ECBusinessImpl implements ECBusiness {

	@Autowired
	@Qualifier("eCDaoHibernate")
	private ECDao ECDao;

	public ECDao getECDao() {
		return ECDao;
	}

	public void setECDao(ECDao dao) {
		ECDao = dao;
	}
	
	public List<BaseObject> getEntidadeCertificadoraAll() {
		return ECDao.getall();
	}
	
	public List<BaseObject> filter(Map parameters){
		return ECDao.filter(parameters);
	}
	@Transactional(readOnly=false)
	public void update(EntidadeCertificadora ec){
		ECDao.update(ec);
	}
	
	@Transactional(readOnly=false)
	public void cadastrar(EntidadeCertificadora ec){
		ECDao.cadastrar(ec);
	}
	
}
