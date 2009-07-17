package org.wikicrimes.business.impl;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wikicrimes.business.LogBusiness;
import org.wikicrimes.dao.LogDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Log;
import org.wikicrimes.model.Usuario;


	@Component("logBusiness")
	@Transactional(readOnly = true)
	public class LogBusinessImpl implements LogBusiness {

		@Autowired
		@Qualifier("logDaoHibernate")
		private LogDao logDao;

		@Transactional(readOnly=false)
		public void update(Log log){
			logDao.update(log);
		}
		
		@Transactional(readOnly=false)
		public void inserir(Log log){
			logDao.insert(log);
		}
		
		public List<BaseObject> getLogsUser(String id){
			return logDao.getLogsUser(id);
		}
		
		public List<BaseObject> getLogsCrime(String id){
			return logDao.getLogsCrime(id);
		}
		
		public List<BaseObject> filter(Map parameters){
			return logDao.filter(parameters);
		}
}
