package org.wikicrimes.dao.hibernate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.wikicrimes.dao.LogDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Log;
import org.wikicrimes.model.Usuario;


@Component("logDaoHibernate")
public class LogDaoImpl extends HibernateDaoGenerico<Log> implements
		LogDao {

	@Override
	protected Class<Log> getClasseEntidade() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void update(Log log){
		super.atualizarEntidade(log);
	}
	
	public void insert(Log log){
		super.inserirEntidade(log);
	}
	
	public List<BaseObject> getLogsUser(String id){
		String query= "from Log l where l.idObj = '"+id+"' and l.tipoLog.idTipoLog= '2'";
		return getHibernateTemplate().find(query);
	}
	
	public List<BaseObject> getLogsCrime(String id){
		String query= "from Log l where l.idObj = '"+id+"' and l.tipoLog.idTipoLog= '1'";
		return getHibernateTemplate().find(query);
	}
	
	public List<BaseObject> filter(Map parameters){
		
		String primeiroNome = (String)parameters.get("primeiroNome");
		Date data = (Date) parameters.get("data");
		String campo = (String)parameters.get("campo");
		String idObj = (String) parameters.get("idObj");
		
		String query = "from Log l where ";
		query+="l.idObj = '"+idObj+"' ";
		if ( primeiroNome != null && !primeiroNome.equalsIgnoreCase("")){
			query +="and l.usuario.primeiroNome like '%"+primeiroNome+"%'";
		}
		if ( data != null ){
			Timestamp dt = new Timestamp(data.getTime());
			query+=" and l.data = '"+ dt+"'";
		}
		if ( campo != null && !campo.equalsIgnoreCase("")){
			query +=" and l.campo like '%"+campo+"%'";
		}
		query=query.replaceAll("where and", "where");
		return getHibernateTemplate().find(query);
	
	}
	
}
