package org.wikicrimes.dao.hibernate;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.wikicrimes.dao.ECDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.EntidadeCertificadora;
import org.wikicrimes.model.Usuario;


@Component("eCDaoHibernate")
public class ECDaoImpl extends HibernateDaoGenerico<EntidadeCertificadora> implements
		ECDao {

	@Override
	protected Class<EntidadeCertificadora> getClasseEntidade() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<BaseObject> getall(){
//		List<EntidadeCertificadora> listTemp = super.selecionarTodos(); 
//		return listTemp;
		String query = "from EntidadeCertificadora";
		
		List<BaseObject> resultado = getHibernateTemplate().find(query);
		return resultado;
	}
	public List<BaseObject> filter(final Map parameters) {					
		
		String nomeEC = (String)parameters.get("nomeEC");
		String descricaoEC = (String) parameters.get("descricaoEC");
		String homepageEC = (String)parameters.get("homepageEC");
		
		
		String query = "from EntidadeCertificadora e where ";
		if ( nomeEC != null && !nomeEC.equalsIgnoreCase("")){
			query +="e.nome like '%"+nomeEC+"%' ";
		}
		if (descricaoEC != null && !descricaoEC.equalsIgnoreCase("")){ 
			query +="and e.descricao like '%"+descricaoEC+"%'";
		}
		if (homepageEC != null && !homepageEC.equalsIgnoreCase("")){
			query+="and e.homepage like '"+homepageEC+"' " ;
		}
		
		if(query.equalsIgnoreCase("from EntidadeCertificadora e where ")){
			query="from EntidadeCertificadora e";
		}
		query=query.replaceAll("where and", "where");
		return getHibernateTemplate().find(query);
	}
	
	public void update(EntidadeCertificadora ec){
		super.atualizarEntidade(ec);
	}
	
	public void cadastrar(EntidadeCertificadora ec){
		super.inserirEntidade(ec);
	}
			
}
