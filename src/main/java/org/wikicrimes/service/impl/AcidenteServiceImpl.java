package org.wikicrimes.service.impl;

import java.util.List;
import java.util.Map;

import org.wikicrimes.dao.AcidenteDao;
import org.wikicrimes.model.Acidente;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.service.AcidenteService;
import org.wikicrimes.util.Cripto;

public class AcidenteServiceImpl extends GenericCrudServiceImpl implements AcidenteService {
	
	private AcidenteDao acidenteDao;
	
	public List<Acidente> filter(Map<String, Object> parameters) {
		return acidenteDao.filter(parameters);
	}
		
	public boolean insert(Acidente aci) {
		
		if (getDao().save(aci)) {
			aci.setChave(Cripto.criptografar(aci.getIdAcidente().toString()+aci.getDataHoraRegistro().toString()));
			getDao().save(aci);
			return true;
		}
		
		return false;
	}
	
	
	public Acidente get(Long id){
		Acidente temp=(Acidente) this.getDao().get(id);
		return temp;
	}
	
	public Acidente getAcidente(String chave) {
		Acidente aci= new Acidente();
		aci.setChave(chave);
		List<BaseObject> list =this.getDao().find(aci);
		if (list.size() != 0){
			
			return aci;
		}
		else
			return null;
	
	}

	public AcidenteDao getAcidenteDao() {
		return acidenteDao;
	}

	public void setAcidenteDao(AcidenteDao acidenteDao) {
		this.acidenteDao = acidenteDao;
	}
	
}
