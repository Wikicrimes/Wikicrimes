package org.wikicrimes.dao;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;

public interface CrimeDao {
	public List<BaseObject> filter(Map parameters);
	public void update(Crime crime);
	public List<BaseObject> getByUser(Long idUsuario);
	public List<BaseObject> GetCrimeById(String id);
	public List<BaseObject> getTotalCrimes();
	public Integer countTotalCrimes();
	public List<BaseObject> getCrimesConf();
	public Integer countCrimesConf();
	public List<BaseObject> getCrimesNConf();
	public Integer countCrimesNConf();
	public List<BaseObject> getCrimesConfP();
	public Integer countCrimesConfP();
	public List<BaseObject> getCrimesConfN();
	public Integer countCrimesConfN();
	public Integer countCrimes(Long idUsuario);
	public Integer countCrimesAtivos();
	public List<BaseObject> getCrimesAtivos();
	public Integer countCrimesInativos();
	public List<BaseObject> getCrimesInativos();
	public List<BaseObject> getCrimeSemEndereco();
}
