package org.wikicrimes.dao.hibernate;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;
import org.wikicrimes.dao.CrimeDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Usuario;


@Component("crimeDaoHibernate")
public class CrimeDaoImpl extends HibernateDaoGenerico<Crime> implements
		CrimeDao {

	@Override
	protected Class<Crime> getClasseEntidade() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
		public List<BaseObject> filter(final Map parameters) {					
			
			String idCrime = (String)parameters.get("idCrime");
			String chave = (String)parameters.get("chave");
			String idTipoCrime = (String)parameters.get("idTipoCrime");
			String idTipoArma = (String) parameters.get("idTipoArma");
			String horario = (String)parameters.get("horario");
			String horarioFinal = (String)parameters.get("horarioFinal");
			Date dataInicial = (Date) parameters.get("dataInicial");
			Date dataFinal = (Date) parameters.get("dataFinal");
			String confirmacoes = (String) parameters.get("confirmacoes");
			String descricao = (String) parameters.get("descricao");
			Usuario usuarioCrime = (Usuario) parameters.get("usuarioCrime");
			
			String query = "select c.idCrime as idCrime, c.tipoCrime as tipoCrime, c.horario as horario, c.data as data, " +
			"c.confirmacoesPositivas as confirmacoesPositivas, c.latitude as latitude, c.longitude as longitude, c.tipoArmaUsada as tipoArmaUsada, c.descricao as descricao, " +
			"c.tipoPapel as tipoPapel from Crime c where ";
			
			boolean entrouNocriterio = false;
			
			if ( idCrime != null ){
				query +="and c.idCrime = "+idCrime+" ";
				entrouNocriterio = true;
			}
			
			if ( chave != null ){
				query +="and c.chave = '"+chave+"' ";
				entrouNocriterio = true;
			}
			
			if ( usuarioCrime != null ){
				query +=" and c.usuario.idUsuario = '"+usuarioCrime.getIdUsuario()+"' ";
				entrouNocriterio = true;
			}
			if ( idTipoCrime != null && !idTipoCrime.equalsIgnoreCase("")){
				query +="and c.tipoCrime.idTipoCrime = "+Long.parseLong(idTipoCrime)+" ";
				entrouNocriterio = true;
			}
			if (idTipoArma != null && !idTipoArma.equalsIgnoreCase("")){ 
				query +="and c.tipoArmaUsada.idTipoArmaUsada = "+Long.parseLong(idTipoArma)+" ";
				entrouNocriterio = true;
			}
			if (descricao != null && !descricao.equalsIgnoreCase("")){
				query+="and c.descricao like '%"+descricao+"%'" ;
				entrouNocriterio = true;
			}
			if (horario != null && !horario.equalsIgnoreCase("")){
				query+="and c.horario >= "+Long.parseLong(horario)+" " ;
				entrouNocriterio = true;
			}
			if (horarioFinal != null && !horarioFinal.equalsIgnoreCase("")){
				query+="and c.horario <= "+Long.parseLong(horarioFinal)+" " ;
				entrouNocriterio = true;
			}
			if (confirmacoes != null && !confirmacoes.equalsIgnoreCase("")){
				query+="and c.confirmacoesPositivas = "+Long.parseLong(confirmacoes)+" " ;
				entrouNocriterio = true;
			}
			if (dataInicial != null ){
				Timestamp dtInicial = new Timestamp(dataInicial.getTime());
				query+="and c.data >= '"+ dtInicial+"' ";
				entrouNocriterio = true;
			}
			if (dataFinal != null ){
				Timestamp dtFinal = new Timestamp(dataFinal.getTime());
				query+="and c.data <= '"+ dtFinal+"' ";
				entrouNocriterio = true;
			}
			if(!entrouNocriterio){
				query=query.replaceAll("from Crime c where ", "from Crime c ");
			}
			query=query.replaceAll("where and", "where");
			
			List<BaseObject> crimes=getHibernateTemplate().getSessionFactory().openSession().
			createQuery(query)
			.setResultTransformer(Transformers.aliasToBean(Crime.class)).list();
		
			for (Iterator iterator = crimes.iterator(); iterator.hasNext();) {
				Crime crime = (Crime) iterator.next();
				Hibernate.initialize(crime.getTipoArmaUsada());
				Hibernate.initialize(crime.getTipoPapel());
				Hibernate.initialize(crime.getTipoCrime());
			}
			return crimes;
	}
		
		public void update(Crime crime){
			super.atualizarEntidade(crime);
		}
		
		public List<BaseObject> getByUser(Long idUsuario) {
			String query = "from Crime ";

			if (idUsuario != null) {
				query += "where usuario = " + idUsuario;
			}
			List<BaseObject> crimes = getHibernateTemplate().find(query);
			for (Iterator iterator = crimes.iterator(); iterator.hasNext();) {
				Crime crime = (Crime) iterator.next();
				Hibernate.initialize(crime.getTipoArmaUsada());
				Hibernate.initialize(crime.getTipoPapel());
				Hibernate.initialize(crime.getTipoCrime());
			}
			return crimes;
		}
		
		public List<BaseObject> GetCrimeById(String id){
			String query = "from Crime c "+
			"where c.idCrime like '"+id+"'";
			List<BaseObject> crimes = getHibernateTemplate().find(query);
			for (Iterator iterator = crimes.iterator(); iterator.hasNext();) {
				Crime crime = (Crime) iterator.next();
				Hibernate.initialize(crime.getTipoArmaUsada());
				Hibernate.initialize(crime.getTipoPapel());
				Hibernate.initialize(crime.getTipoCrime());
			}
			return crimes;
			}
		
		public List<BaseObject> getTotalCrimes(){
			List<BaseObject> crimes=getHibernateTemplate().getSessionFactory().openSession().
			createQuery("select c.idCrime as idCrime, c.tipoCrime as tipoCrime, c.horario as horario, c.data as data, " +
			"c.confirmacoesPositivas as confirmacoesPositivas, c.latitude as latitude, c.longitude as longitude, c.tipoArmaUsada as tipoArmaUsada, c.descricao as descricao, " +
			"c.tipoPapel as tipoPapel from Crime c")
			.setResultTransformer(Transformers.aliasToBean(Crime.class)).list();
		
			for (Iterator iterator = crimes.iterator(); iterator.hasNext();) {
				Crime crime = (Crime) iterator.next();
				Hibernate.initialize(crime.getTipoArmaUsada());
				Hibernate.initialize(crime.getTipoPapel());
				Hibernate.initialize(crime.getTipoCrime());
			}
			return crimes;
		}
		
		public Integer countTotalCrimes(){
			String query = "select count(*) from Crime c ";
			Long cont = (Long) getHibernateTemplate().find(query).iterator().next();
			
			return cont.intValue();
		}
		
		public List<BaseObject> getCrimesAtivos(){
			List<BaseObject> crimes=getHibernateTemplate().getSessionFactory().openSession().
			createQuery("select c.idCrime as idCrime, c.tipoCrime as tipoCrime, c.horario as horario, c.data as data, " +
					"c.confirmacoesPositivas as confirmacoesPositivas, c.latitude as latitude, c.longitude as longitude, c.tipoArmaUsada as tipoArmaUsada, c.descricao as descricao, " +
					"c.tipoPapel as tipoPapel from Crime c" +
					" where c.status ='0'")
			.setResultTransformer(Transformers.aliasToBean(Crime.class)).list();
		
			for (Iterator iterator = crimes.iterator(); iterator.hasNext();) {
				Crime crime = (Crime) iterator.next();
				Hibernate.initialize(crime.getTipoArmaUsada());
				Hibernate.initialize(crime.getTipoPapel());
				Hibernate.initialize(crime.getTipoCrime());
			}
			return crimes;
		}
		
		public Integer countCrimesAtivos(){
			String query = "select count(*) from Crime c "+
			"where c.status ='0'";
			Long cont = (Long) getHibernateTemplate().find(query).iterator().next();
			
			return cont.intValue();
		}
		
		public List<BaseObject> getCrimesInativos(){
			List<BaseObject> crimes=getHibernateTemplate().getSessionFactory().openSession().
			createQuery("select c.idCrime as idCrime, c.tipoCrime as tipoCrime, c.horario as horario, c.data as data, " +
					"c.confirmacoesPositivas as confirmacoesPositivas, c.latitude as latitude, c.longitude as longitude, c.tipoArmaUsada as tipoArmaUsada, c.descricao as descricao, " +
					"c.tipoPapel as tipoPapel from Crime c" +
					" where c.status ='1'")
			.setResultTransformer(Transformers.aliasToBean(Crime.class)).list();
		
			for (Iterator iterator = crimes.iterator(); iterator.hasNext();) {
				Crime crime = (Crime) iterator.next();
				Hibernate.initialize(crime.getTipoArmaUsada());
				Hibernate.initialize(crime.getTipoPapel());
				Hibernate.initialize(crime.getTipoCrime());
			}
			return crimes;
		}
		
		public Integer countCrimesInativos(){
			String query = "select count(*) from Crime c "+
			"where c.status ='1'";
			Long cont = (Long) getHibernateTemplate().find(query).iterator().next();
			
			return cont.intValue();
		}
		
		public List<BaseObject> getCrimesConf(){
			List<BaseObject> crimes=getHibernateTemplate().getSessionFactory().openSession().
			createQuery("select c.idCrime as idCrime, c.tipoCrime as tipoCrime, c.horario as horario, c.data as data, " +
					"c.confirmacoesPositivas as confirmacoesPositivas, c.latitude as latitude, c.longitude as longitude, c.tipoArmaUsada as tipoArmaUsada, c.descricao as descricao, " +
					"c.tipoPapel as tipoPapel from Crime c" +
					" where c.confirmacoesPositivas != '0' " +
					"or c.confirmacoesNegativas != '0'")
			.setResultTransformer(Transformers.aliasToBean(Crime.class)).list();
		
			for (Iterator iterator = crimes.iterator(); iterator.hasNext();) {
				Crime crime = (Crime) iterator.next();
				Hibernate.initialize(crime.getTipoArmaUsada());
				Hibernate.initialize(crime.getTipoPapel());
				Hibernate.initialize(crime.getTipoCrime());
			}
			return crimes;
		}
		
		public Integer countCrimesConf(){
			String query = "select count(*) from Crime c "+
			"where c.confirmacoesPositivas != '0' " +
			"or c.confirmacoesNegativas != '0'";
			Long cont = (Long) getHibernateTemplate().find(query).iterator().next();
			
			return cont.intValue();
		}
		
		public List<BaseObject> getCrimesNConf(){
			List<BaseObject> crimes=getHibernateTemplate().getSessionFactory().openSession().
			createQuery("select c.idCrime as idCrime, c.tipoCrime as tipoCrime, c.horario as horario, c.data as data, " +
			"c.confirmacoesPositivas as confirmacoesPositivas, c.latitude as latitude, c.longitude as longitude, c.tipoArmaUsada as tipoArmaUsada, c.descricao as descricao, " +
			"c.tipoPapel as tipoPapel from Crime c" +
					" where c.confirmacoesPositivas = '0' " +
					"and c.confirmacoesNegativas = '0'")
			.setResultTransformer(Transformers.aliasToBean(Crime.class)).list();
		for (Iterator iterator = crimes.iterator(); iterator.hasNext();) {
				Crime crime = (Crime) iterator.next();
				Hibernate.initialize(crime.getTipoArmaUsada());
				Hibernate.initialize(crime.getTipoPapel());
				Hibernate.initialize(crime.getTipoCrime());
			}
			return crimes;
		}
		
		public Integer countCrimesNConf(){
			String query = "select count(*) from Crime c "+
			"where c.confirmacoesPositivas = '0' " +
			"and c.confirmacoesNegativas = '0'";
			Long cont = (Long) getHibernateTemplate().find(query).iterator().next();
			
			return cont.intValue();
		}
		
		public List<BaseObject> getCrimesConfP(){
			List<BaseObject> crimes=getHibernateTemplate().getSessionFactory().openSession().
			createQuery("select c.idCrime as idCrime, c.tipoCrime as tipoCrime, c.horario as horario, c.data as data, " +
					"c.confirmacoesPositivas as confirmacoesPositivas, c.latitude as latitude, c.longitude as longitude, c.tipoArmaUsada as tipoArmaUsada, c.descricao as descricao, " +
					"c.tipoPapel as tipoPapel from Crime c" +
					" where c.confirmacoesPositivas != '0'")
			.setResultTransformer(Transformers.aliasToBean(Crime.class)).list();
		for (Iterator iterator = crimes.iterator(); iterator.hasNext();) {
				Crime crime = (Crime) iterator.next();
				Hibernate.initialize(crime.getTipoArmaUsada());
				Hibernate.initialize(crime.getTipoPapel());
				Hibernate.initialize(crime.getTipoCrime());
			}
			return crimes;
		}
		
		public Integer countCrimesConfP(){
			String query = "select count(*) from Crime c "+
			"where c.confirmacoesPositivas != '0'";
			Long cont = (Long) getHibernateTemplate().find(query).iterator().next();
			
			return cont.intValue();
		}
		
		public List<BaseObject> getCrimesConfN(){
			List<BaseObject> crimes=getHibernateTemplate().getSessionFactory().openSession().
			createQuery("select c.idCrime as idCrime, c.tipoCrime as tipoCrime, c.horario as horario, c.data as data, " +
					"c.confirmacoesPositivas as confirmacoesPositivas, c.latitude as latitude, c.longitude as longitude, c.tipoArmaUsada as tipoArmaUsada, c.descricao as descricao, " +
					"c.tipoPapel as tipoPapel from Crime c" +
					" where c.confirmacoesNegativas != '0'")
			.setResultTransformer(Transformers.aliasToBean(Crime.class)).list();
		
			for (Iterator iterator = crimes.iterator(); iterator.hasNext();) {
				Crime crime = (Crime) iterator.next();
				Hibernate.initialize(crime.getTipoArmaUsada());
				Hibernate.initialize(crime.getTipoPapel());
				Hibernate.initialize(crime.getTipoCrime());
			}
			return crimes;
		}
		
		public Integer countCrimesConfN(){
			String query = "select count(*) from Crime c "+
			"where c.confirmacoesNegativas != '0'";
			Long cont = (Long) getHibernateTemplate().find(query).iterator().next();
			
			return cont.intValue();
		}
		
		public Integer countCrimes(final Long idUsuario){
			
			String query = "select count(*) from Crime c where c.usuario.idUsuario = "+idUsuario+"";
			Long cont = (Long) getHibernateTemplate().find(query).iterator().next();
			
			return cont.intValue();
			
//				return (Integer) getHibernateTemplate().execute(
//						new HibernateCallback() {
//							public Object doInHibernate(Session session)
//									throws HibernateException, SQLException {
//								Query query = session
//										.createQuery("select count(*) from Crime c where c.usuario.idUsuario = "+idUsuario+"");
//
//								return ((Long) query.iterate().next()).intValue();
//							}
//						});
		}


		/*
		 * Método que retorna os crimes sem endereços.
		 */
		
		public List<BaseObject> getCrimeSemEndereco() {

			String query = "from Crime c where c.status = 0 and c.pais is null and c.estado is null and c.cidade is null and c.endereco is null order by c.idCrime desc";

			List<BaseObject> crimes = getHibernateTemplate().find(query);

			for (Iterator iterator = crimes.iterator(); iterator.hasNext();) {
				Crime crime = (Crime) iterator.next();
				Hibernate.initialize(crime.getTipoArmaUsada());
				Hibernate.initialize(crime.getTipoPapel());
				Hibernate.initialize(crime.getTipoCrime());
				Hibernate.initialize(crime.getUsuario());
			}
			return crimes;
		}
		
		

}
