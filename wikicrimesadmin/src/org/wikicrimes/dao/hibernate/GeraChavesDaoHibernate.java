package org.wikicrimes.dao.hibernate;

import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Component;
import org.wikicrimes.dao.GeraChavesDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.ChavesCriptografia;

@Component("geraChavesDaoHibernate")
public class GeraChavesDaoHibernate 
extends HibernateDaoGenerico 
implements GeraChavesDao{
	
	private Class entity;
	
	public GeraChavesDaoHibernate() {
		entity = BaseObject.class;
	}
	
	@Override
	protected Class getClasseEntidade() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<ChavesCriptografia> consultaDominio(String dominio){
		List<ChavesCriptografia> lista=null;String query=null;
		
		//System.out.println(dominio);
		
		if(dominio==null || dominio.equalsIgnoreCase("")){
			//a formatação da pagina nao estah dentro do requisitado
			query = "from ChavesCriptografia";
		}else{
			//essa query estah desenvolvida de forma errada
			query = "from ChavesCriptografia cc where cc.site like '%"+dominio+"%'";
		}
		lista=getHibernateTemplate().find(query);
		return lista;
	}
	
	public void adicionaChavesGeradas(ChavesCriptografia cc){
		getHibernateTemplate().saveOrUpdate(cc);
	}
	
	public boolean verificaChaveGerada(ChavesCriptografia cc){
		
		StringTokenizer st = new StringTokenizer(cc.getChavePublica());
		String publica = st.nextToken();
		st = new StringTokenizer(cc.getChavePrivada());
		String privado = st.nextToken();
		
		String query = "from ChavesCriptografia cc where cc.chavePrivada like '"
			+privado+"%' or cc.chavePublica like '"+publica+"%'";
		
		List<ChavesCriptografia> lista=getHibernateTemplate().find(query);
		return lista.size()!=0;
	}
}