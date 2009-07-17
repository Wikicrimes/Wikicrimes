package org.wikicrimes.business.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wikicrimes.business.CrimeBusiness;
import org.wikicrimes.dao.CrimeDao;
import org.wikicrimes.dao.CrimeRazaoDao;
import org.wikicrimes.dao.RazaoDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.CrimeRazao;


	@Component("crimeBusiness")
	@Transactional(readOnly = true)
	public class CrimeBusinessImpl implements CrimeBusiness {

		@Autowired
		@Qualifier("crimeDaoHibernate")
		private CrimeDao crimeDao;
		
		//faltou esse remapeamento
		@Autowired
		@Qualifier("razaoDaoHibernate")
		private RazaoDao razaoDao;
		
		@Autowired
		@Qualifier("crimeRazaoDaoHibernate")
		private CrimeRazaoDao crimeRazaoDao;
		
		private Set<CrimeRazao> razoesCrimes;

		/*
		 * Método que retorna os crimes sem endereços.
		 */
		public List<BaseObject> getCrimeSemEndereco(){
			return crimeDao.getCrimeSemEndereco();
		}
		
		public List<BaseObject> filter(Map parameters){
			return crimeDao.filter(parameters);
		}
		
		@Transactional(readOnly=false)
		public void update(Crime crime){
			crimeDao.update(crime);
		}
		
		@Transactional(readOnly=false)
		public void update(Crime crime,Set<CrimeRazao> crimeRazao){
			crimeRazaoDao.deleteCrimeRazao(crimeRazao);
			crimeDao.update(crime);
		}
		
		public List<BaseObject> getByUser(Long idUsuario) {
			return crimeDao.getByUser(idUsuario);
		}
				
		public List<BaseObject> getCrimeById(String id){
			return crimeDao.GetCrimeById(id);
		}
		
		public List<BaseObject> getTotalCrimes(){
			return crimeDao.getTotalCrimes();
		}
		public Integer countTotalCrimes(){
			return crimeDao.countTotalCrimes();
		}
		
		
		public List<BaseObject> getCrimesConf(){
			return crimeDao.getCrimesConf();
		}
		public Integer countCrimesConf(){
			return crimeDao.countCrimesConf();
		}
		
		
		public List<BaseObject> getCrimesNConf(){
			return crimeDao.getCrimesNConf();
		}
		public Integer countCrimesNConf(){
			return crimeDao.countCrimesNConf();
		}
		
		
		public List<BaseObject> getCrimesConfP(){
			return crimeDao.getCrimesConfP();
		}
		public Integer countCrimesConfP(){
			return crimeDao.countCrimesConfP();
		}
		
		public List<BaseObject> getCrimesConfN(){
			return crimeDao.getCrimesConfN();
		}
		public Integer countCrimesConfN(){
			return crimeDao.countCrimesConfN();
		}
				
		public Integer countCrimes(Long idUsuario){
			return crimeDao.countCrimes(idUsuario);
		}
		
		public Integer countCrimesAtivos(){
			return crimeDao.countCrimesAtivos();
		}
		
		public List<BaseObject> getCrimesAtivos(){
			return crimeDao.getCrimesAtivos();
		}
		
		public Integer countCrimesInativos(){
			return crimeDao.countCrimesInativos();
		}
		
		public List<BaseObject> getCrimesInativos(){
			return crimeDao.getCrimesInativos();
		}
		
		public List<BaseObject> listarRazoes() {
			return razaoDao.listarRazoes();
		}
		
}
