package org.wikicrimes.service.impl;

import java.util.List;
import java.util.Map;

import org.wikicrimes.dao.RazaoDao;
import org.wikicrimes.dao.RelatoDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Relato;
import org.wikicrimes.service.RazaoService;
import org.wikicrimes.service.RelatoService;
import org.wikicrimes.util.Cripto;

public class RazaoServiceImpl extends GenericCrudServiceImpl implements RazaoService {
	
	private RazaoDao razaoDao;

	public List<BaseObject> listarRazoes() {
		// TODO Auto-generated method stub
		return razaoDao.listarRazoes();
	}

	public RazaoDao getRazaoDao() {
		return razaoDao;
	}

	public void setRazaoDao(RazaoDao razaoDao) {
		this.razaoDao = razaoDao;
	}

	
}
