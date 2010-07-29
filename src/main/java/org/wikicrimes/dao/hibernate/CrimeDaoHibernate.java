package org.wikicrimes.dao.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.wikicrimes.dao.CrimeDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.EntidadeCertificadora;
import org.wikicrimes.model.TipoCrime;
import org.wikicrimes.model.TipoLocal;
import org.wikicrimes.model.TipoVitima;
import org.wikicrimes.model.Usuario;

/**
 * 
 */
public class CrimeDaoHibernate extends GenericCrudDaoHibernate implements
		CrimeDao {
	
	private final double RAIO_TERRA_KM=6378.137;
	
	public CrimeDaoHibernate() {
		setEntity(Crime.class);
	}

				
//	public List<BaseObject> filter(final Map parameters) {
//		
//		return (List<BaseObject>) getHibernateTemplate().execute(new HibernateCallback() {
//			public Object doInHibernate(Session session)
//					throws HibernateException {
//
//				Criteria criteria = session.createCriteria(getEntity());
//				if (parameters.get("tipoCrime") != null) {
//					criteria.add(Expression.eq("tipoCrime", parameters.get("tipoCrime")));
//				}
//				if (parameters.get("tipoLocal") != null) {
//					criteria.add(Expression.eq("tipoLocal", parameters.get("tipoLocal")));
//				}
//				if (parameters.get("tipoVitima") != null) {
////					criteria.createAlias("tipoLocal", "tpl").add( Restrictions.eqProperty("tpl.id", parameters.get("tipoVitima")) );
//					TipoCrime tipoCrime = (TipoCrime) parameters.get("tipoCrime");
//					if (parameters.get("tipoLocal") != null || (tipoCrime.getIdTipoCrime() != 5)) {
//						criteria.createCriteria("tipoLocal").add( Restrictions.like("tipoVitima", parameters.get("tipoVitima")) );
//					}
//					else {
//						criteria.add(Expression.eq("tipoVitima", parameters.get("tipoVitima")));
//					}
//				}
//				if (parameters.get("horarioInicial") != null) {
//					criteria.add(Expression.ge("horario", parameters.get("horarioInicial")));
//				}
//				if (parameters.get("horarioFinal") != null) {
//					criteria.add(Expression.le("horario", parameters.get("horarioFinal")));
//				}
//				if (parameters.get("dataInicial") != null) {
//					criteria.add(Expression.ge("data", parameters.get("dataInicial")));
//				}
//				if (parameters.get("dataFinal") != null) {
//					criteria.add(Expression.le("data", parameters.get("dataFinal")));
//				}
//				criteria.add(Expression.eq("status",Crime.ATIVO));
//				criteria.addOrder(Order.desc("data"));
//				if (parameters.get("maxResults") != null)
//					criteria.setMaxResults((Integer) parameters.get("maxResults"));
//				return criteria.list();
//			}
//		});
//	}

	// Método que realiza um filtro parametrizado de crimes
	// Usando HQL
	public List<BaseObject> filter(Map parameters) {
		boolean entrouTipoLocal = false;
		String consulta = "from Crime as crime where ";
		List<BaseObject> listaEntidadeCertificadora = (List<BaseObject>) parameters
		.get("entidadeCertificadora");
		
		if (listaEntidadeCertificadora!=null)
			consulta = "select crime from Crime crime join crime.confirmacoes as confirmacao where ";
		
		// Credibilidade
		if (parameters.get("credibilidadeInicial") != null && parameters.get("credibilidadeFinal") != null) 
		{
			consulta += "( crime.ultimaCredibilidade >= "+ parameters.get("credibilidadeInicial") + " and "+
					"crime.ultimaCredibilidade <= "+ parameters.get("credibilidadeFinal") + ") and ";
		}
		
		// TipoCrime
		if (parameters.get("tipoCrime") != null) {
			consulta += "(crime.tipoCrime.idTipoCrime = "
				+ ((TipoCrime) parameters.get("tipoCrime"))
					.getIdTipoCrime() + ") and ";
		}
		
		// Email Usuario(Traz somente crimes registrados por esse usuario)
		if (parameters.get("emailUsuario") != null) {
			consulta += "(crime.usuario.email like '"
					+ (parameters.get("emailUsuario"))
							+ "') and ";
		}

		// TipoLocal
		if (parameters.get("tipoLocal") != null) {
			consulta += "(crime.tipoVitima.idTipoVitima = "
					+ ((TipoVitima) parameters.get("tipoVitima"))
							.getIdTipoVitima() + ") and ";
			entrouTipoLocal = true;
		}

		// TipoVitima e TipoLocal
		if (parameters.get("tipoVitima") != null) {
			TipoCrime tipoCrime = (TipoCrime) parameters.get("tipoCrime");
			if (parameters.get("tipoLocal") != null	|| (tipoCrime.getIdTipoCrime() != 5)) {
				if (parameters.get("tipoLocal")!=null)
					consulta += "(crime.tipoLocal.idTipoLocal = "+ ((TipoLocal) parameters.get("tipoLocal")).getIdTipoLocal() + ") and ";
				else
					consulta += "(crime.tipoVitima.idTipoVitima = "	+ ((TipoVitima) parameters.get("tipoVitima")).getIdTipoVitima() + ") and "; 
			}
			if (!entrouTipoLocal)
				consulta += "(crime.tipoVitima.idTipoVitima = "	+ ((TipoVitima) parameters.get("tipoVitima")).getIdTipoVitima() + ") and ";

		}

		// DataInicial e DataFinal
		if (parameters.get("dataInicial") != null
				&& parameters.get("dataFinal") != null) {
			Date dataInicial = (Date) parameters.get("dataInicial");
			Date dataFinal = (Date) parameters.get("dataFinal");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String stringDataInicial = dateFormat.format(dataInicial);
			String stringDataFinal = dateFormat.format(dataFinal);

			consulta += "(CRI_DATA BETWEEN '" + stringDataInicial + "' and '"
					+ stringDataFinal + "'" + ") and ";
		}
		// HorarioInicial e HorarioFinal
		if (parameters.get("horarioInicial") != null
				&& parameters.get("horarioFinal") != null) {

			consulta += "(CRI_HORARIO >= "
					+ ((Long) parameters.get("horarioInicial")).longValue()
					+ " and CRI_HORARIO <= "
					+ ((Long) parameters.get("horarioFinal")).longValue()
					+ ") and ";

		}

		// Certificação de Crimes
		Boolean isCrimeConfirmadoPositivamente = (Boolean) parameters
				.get("crimeConfirmadoPositivamente");
		// Certificação por confirmação
		if (isCrimeConfirmadoPositivamente != null) {
			if (isCrimeConfirmadoPositivamente)
				consulta += "(crime.confirmacoesPositivas > crime.confirmacoesNegativas) and ";
		}
		
		// Certificação por Entidade Certificadora
		EntidadeCertificadora entidadeCertificadora = null;
		if (listaEntidadeCertificadora != null)
			if (listaEntidadeCertificadora.size()>0){
			for (int i = 0; i < listaEntidadeCertificadora.size(); i++) {
				entidadeCertificadora = (EntidadeCertificadora) listaEntidadeCertificadora
						.get(i);
				if (listaEntidadeCertificadora.size() == 1)
					consulta += "((crime.usuario.entidadeCertificadora.idEntidadeCertificadora = "
							+ entidadeCertificadora
									.getIdEntidadeCertificadora() + ") or (confirmacao.entidadeCertificadora.idEntidadeCertificadora = " 
									+ entidadeCertificadora
									.getIdEntidadeCertificadora() + ")) and ";
				else { //nao sei para que serve esse else
					if (i == 0)
						consulta += "(crime.usuario.entidadeCertificadora.idEntidadeCertificadora = "
								+ entidadeCertificadora
										.getIdEntidadeCertificadora();
					else if (i == listaEntidadeCertificadora.size() - 1)
						consulta += " or crime.usuario.entidadeCertificadora.idEntidadeCertificadora = "
								+ entidadeCertificadora
										.getIdEntidadeCertificadora()
								+ ") and ";
					else
						consulta += " or crime.usuario.entidadeCertificadora.idEntidadeCertificadora = "
								+ entidadeCertificadora
										.getIdEntidadeCertificadora();
				}

			 }
			}
		
			else {
				//Todas as entidades certificadoras
				consulta += "((crime.usuario.entidadeCertificadora.idEntidadeCertificadora is not null) or (confirmacao.entidadeCertificadora.idEntidadeCertificadora is not null)) and ";
				
			}
		
		//crimes no Viewport
		if (parameters.get("norte")!=null && parameters.get("sul")!=null && parameters.get("leste")!=null && parameters.get("oeste")!=null){
			
			if (Double.parseDouble(parameters.get("leste").toString())> Double.parseDouble(parameters.get("oeste").toString())) {
				//retorna todos os crimes dentro da southwest/northeast boundary
				consulta+=" (crime.longitude< " + parameters.get("leste") + " and crime.longitude> " + parameters.get("oeste") + ") and (crime.latitude<= " + parameters.get("norte") + " and crime.latitude>= " + parameters.get("sul") + ") and ";
			}
			else {
				 //retorna todos os crimes dentro da southwest/northeast boundary
				 //split over the meridian
				consulta+=" (crime.longitude<= " + parameters.get("leste") + " or crime.longitude>= " + parameters.get("oeste") + ") and (crime.latitude<= " + parameters.get("norte") + " and crime.latitude>= " + parameters.get("sul") + ") and ";
			}
			
			
		}
		
		//Status do crime = ATIVO
		consulta+= "(crime.status = 0) and ";
		if (consulta.indexOf("and") == -1)
			consulta = consulta.substring(0, consulta.indexOf("where"));
		else
			consulta = consulta.substring(0, consulta.length() - 4);
		//Ordenar por data de maneira decrescente
		consulta+= "order by CRI_DATA desc";
		if (parameters.get("maxResults") != null)
			getHibernateTemplate().setMaxResults(
					(Integer) parameters.get("maxResults"));
		else  
			getHibernateTemplate().setMaxResults(0);
					
		//System.err.println("\nConsulta: " + consulta + "\n");
		return (List<BaseObject>) getHibernateTemplate().find(consulta);

	}

	public void incrementaContador(Boolean tipo, Long idCrime) {
		String query = "update Crime set ";
		if (tipo) {
			query += "CRI_CONFIRMACOES_POSITIVAS=ifnull(CRI_CONFIRMACOES_POSITIVAS,0)+1 ";
		} else
			query += " CRI_CONFIRMACOES_NEGATIVAS=ifnull(CRI_CONFIRMACOES_NEGATIVAS,0)+1 ";
		query += " where idCrime=" + idCrime;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		session.createQuery(query).executeUpdate();
		session.close();

	}
	//Atualiza visualizacoes de um crime
	public void incrementaView( Long idCrime) {
		String query = "update Crime set ";
		query += "CRI_VIEW=ifnull(CRI_VIEW,0)+1 ";
		
		
		query += " where idCrime=" + idCrime;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		session.createQuery(query).executeUpdate();
		session.close();

	}

	public List<Crime> getByUser(Long idUsuario) {
		String query = "from Crime ";

		if (idUsuario != null) {
			query += "where usuario = " + idUsuario;
		}

		return getHibernateTemplate().find(query);
	}

	public Integer getQTDCrimesAtivos() {
		return (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.createQuery("select count(*) from Crime where status = 0");
						return ((Long) query.iterate().next()).intValue();
					}
				});
	}

	public Integer getQtdCrimesByDateInterval(final int tipoCrime,
			final String dataInicio, final String dataFim) {
		return (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.createQuery("select count(*) from Crime where tcr_idtipo_crime = "
										+ tipoCrime
										+ " and cri_data > '"
										+ dataInicio
										+ "' and cri_data < '"
										+ dataFim + "'");

						return ((Long) query.iterate().next()).intValue();
					}
				});
	}

	public Integer getQtdCrimesByDateIntervalPais(final int tipoCrime,
			final String dataInicio, final String dataFim,
			final String siglaPais) {
		return (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.createQuery("select count(*) from Crime where cri_pais = '"
										+ siglaPais
										+ "' and tcr_idtipo_crime = "
										+ tipoCrime
										+ " and cri_data > '"
										+ dataInicio
										+ "' and cri_data < '"
										+ dataFim + "'");

						return ((Long) query.iterate().next()).intValue();
					}
				});
	}

	public Integer getQtdCrimesByDateIntervalEstado(final int tipoCrime,
			final String dataInicio, final String dataFim,
			final String siglaEstado) {
		return (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.createQuery("select count(*) from Crime where cri_estado = '"
										+ siglaEstado
										+ "' and tcr_idtipo_crime = "
										+ tipoCrime
										+ " and cri_data > '"
										+ dataInicio
										+ "' and cri_data < '"
										+ dataFim + "'");

						return ((Long) query.iterate().next()).intValue();
					}
				});
	}

	public Integer getQtdCrimesByDateIntervalCidade(final int tipoCrime,
			final String dataInicio, final String dataFim,
			final String nomeCidade) {
		return (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.createQuery("select count(*) from Crime where cri_cidade = '"
										+ nomeCidade
										+ "' and tcr_idtipo_crime = "
										+ tipoCrime
										+ " and cri_data > '"
										+ dataInicio
										+ "' and cri_data < '"
										+ dataFim + "'");

						return ((Long) query.iterate().next()).intValue();
					}
				});
	}

	public List<Crime> getCrimesSemEstatisticas() {

		String query = "from Crime ";

		query += "where status =0 and cri_endereco is null and cri_cidade is null and cri_estado is null and cri_pais is null ";

		return getHibernateTemplate().find(query);

		/*
		 * Crime crime = new Crime(); crime.setStatus(Crime.ATIVO);
		 * crime.setEstado(""); crime.setCidade(""); crime.setPais("");
		 * crime.setEndereco("");
		 * 
		 * return (List<Crime>) getHibernateTemplate().findByExample(crime);
		 */

	}
	public List<Crime> getCrimesSemChave() {
    	
    	String query = "from Crime ";

    	
    	    query += "where cri_chave is null";
    	

    	return getHibernateTemplate().find(query);
    	  		
    	
    }
   /*
    * Metodo para retornar crimes dentro de Viewport
    */
	public List<Crime> getCrimesByViewPort(final double norte, final double sul, final double leste, final double oeste) {
		  List<Crime> crimes=null;
		  crimes = (List<Crime>) getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException { 
					
					Criteria criteria = session.createCriteria(getEntity());
					criteria.add(Expression.le("latitude", norte));
					criteria.add(Expression.ge("latitude", sul));
					criteria.add(Expression.le("longitude", leste));
					criteria.add(Expression.ge("longitude", oeste));
					
					return criteria.list();
				}});
		  
		return  crimes;
	}	
	
	public List<Crime> pesquisarCrime(Crime crime){
		String query = "from Crime c ";

    	
	    query += "where c.descricao like '%"+crime.getDescricao()+"%' and c.status <> 1 order by c.idCrime desc";
	    getHibernateTemplate().setMaxResults(20);

	    return getHibernateTemplate().find(query);
	}
	
	public List<Crime> getCrimesMaisVistos(){
		String query = "from Crime crime where crime.status <> 1 ";

	
	    query += "order by crime.visualizacoes desc";
	    getHibernateTemplate().setMaxResults(5);

	    return getHibernateTemplate().find(query);
	}


	@Override
	public List<Crime> getCrimesMaisComentados() {
		String query = "select c from Crime c , Comentario con" +
				" where c.idCrime = con.crime.idCrime and c.status <> 1";	
		
	    query += " group by c order by count(con) desc";
	    getHibernateTemplate().setMaxResults(5);

	    return getHibernateTemplate().find(query);
	}


	@Override
	public List<Crime> getCrimesMaisConfirmados() {
		String query = "from Crime c where c.status <> 1";		
    	
	    query += "order by c.confirmacoesPositivas desc";
	    getHibernateTemplate().setMaxResults(5);

	    return getHibernateTemplate().find(query);
	}


	@Override
	public void atualizaContadorCometarios(Long idCrime) {
		String query = "update Crime set ";
		query += "CRI_QTD_COMENTARIOS=ifnull(CRI_QTD_COMENTARIOS,0)+1 ";
		
		
		query += " where idCrime=" + idCrime;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		session.createQuery(query).executeUpdate();
		session.close();
		
	}	
	
	//by Philipp
	public Map <String,Integer> contaCrimesArea(double latitude, double longitude, double raio,long dataIni, long dataFim){
		Map <String,Integer> mapa = new HashMap<String, Integer>();
		
		java.sql.Date dIni = new java.sql.Date(dataIni);
		java.sql.Date dFim = new java.sql.Date(dataFim);
		
		String sqlSemViolencia = "SELECT count(*) as contador,tipo.tcr_descricao as descricao FROM tb_cri_crime as tcc inner join tb_tcr_tipo_crime as tipo on tipo.tcr_idtipo_crime=tcc.tcr_idtipo_crime and ("+RAIO_TERRA_KM+" * ACOS( (SIN(PI()* "+latitude+" /180)*SIN(PI() * tcc.cri_latitude/180)) + (COS(PI()* "+latitude+" /180)*cos(PI()*tcc.cri_latitude/180)*COS(PI() * tcc.cri_longitude/180-PI()* "+longitude+" /180))) < "+raio+") and tcc.cri_status=0 and (tcc.cri_data between '"+dIni.toString()+"' and '"+dFim.toString()+"') group by tipo.tcr_descricao";
		
		String sqlViolencia = "select tttv.tvi_descricao as descricao, count(*) as contador from tb_tvi_tipo_vitima as tttv inner join (SELECT tipo.tcr_descricao as descricao,tipo.tcr_idtipo_crime as tipo,tcc.tvi_idtipo_vitima as idVitima FROM tb_cri_crime as tcc inner join tb_tcr_tipo_crime as tipo on tipo.tcr_idtipo_crime=tcc.tcr_idtipo_crime and ("+RAIO_TERRA_KM+" * ACOS( (SIN(PI()* "+latitude+" /180)*SIN(PI() * tcc.cri_latitude/180)) + (COS(PI()* "+latitude+" /180)*cos(PI()*tcc.cri_latitude/180)*COS(PI() * tcc.cri_longitude/180-PI()* "+longitude+" /180))) < "+raio+") and tcc.cri_status=0 and tipo.tcr_idtipo_crime = 5 and (tcc.cri_data between '"+dIni.toString()+"' and '"+dFim.toString()+"')) as crime on tttv.tvi_idtipo_vitima = idVitima group by tttv.tvi_descricao";

		//usa essa URL de teste...
		//http://localhost:8080/wikicrimes/CrimeRatioServlet?lat=-3.72927166&long=-38.51398944&raio=1.25
		
		Session session = getHibernateTemplate().getSessionFactory().openSession();//abre a secao do hibernate
		
		//e trabalha normalmente atraves da conex do BD
		Connection connect = session.connection();
		
		try {
			Statement ps = connect.createStatement();
			ResultSet rs = ps.executeQuery(sqlSemViolencia);
			
			while(rs.next()){
				String desc = rs.getString("descricao");
				if(desc.equalsIgnoreCase("furto") || desc.equalsIgnoreCase("tentativa de furto")){
					Integer quant = mapa.get("Furto");
					if(quant!=null){
						quant+=new Integer(rs.getString("contador")).intValue();
						mapa.put("Furto", quant);
					}else{
						mapa.put("Furto", new Integer(rs.getString("contador")).intValue());
					}
				}
				if(desc.equalsIgnoreCase("roubo") || desc.equalsIgnoreCase("tentativa de roubo")){
					Integer quant = mapa.get("Roubo");
					if(quant!=null){
						quant+=new Integer(rs.getString("contador")).intValue();
						mapa.put("Roubo", quant);
					}else{
						mapa.put("Roubo", new Integer(rs.getString("contador")).intValue());
					}
				}
			}
			
			rs.close();
			
			rs = ps.executeQuery(sqlViolencia);
			while(rs.next()){
				String desc = rs.getString("descricao");
				
				if(desc.equalsIgnoreCase("homícidio") || desc.equalsIgnoreCase("tentativa de homicídio")){
					Integer quant = mapa.get("Homicídio");
					if(quant != null){
						quant+=new Integer(rs.getString("contador")).intValue();
						mapa.put("Homicídio",quant);
					}else{
						mapa.put("Homicídio", new Integer(rs.getString("contador")).intValue());
					}
				}else{
					if(desc.equalsIgnoreCase("latrocinio")){
						mapa.put("Latrocinio", new Integer(rs.getString("contador")).intValue());
					}else{
						Integer quant = mapa.get("Outros");
						if(quant != null){
							quant+=new Integer(rs.getString("contador")).intValue();
							mapa.put("Outros",quant);
						}else{
							mapa.put("Outros", new Integer(rs.getString("contador")).intValue());
						}
					}
				}
			}
			
			rs.close();
			ps.close();
			connect.close();
			session.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		return mapa;
	}
	
	public StringBuilder crimesArea(double latitude, double longitude, double raio,long dataIni, long dataFim){
		
		StringBuilder crimes = new StringBuilder(); 
		
		java.sql.Date dIni = new java.sql.Date(dataIni);
		java.sql.Date dFim = new java.sql.Date(dataFim);
		
		String sqlCrimes; //= "SELECT tcr_descricao AS tipoCrime, cri_data as dataCrime, cri_horario as horarioCrime, cri_descricao as descricaoCrime, cri_latitude as latitudeCrime, cri_longitude as logitudeCrime FROM tb_cri_crime as tcc inner join tb_tcr_tipo_crime as tipo on tipo.tcr_idtipo_crime=tcc.tcr_idtipo_crime and ("+RAIO_TERRA_KM+" * ACOS( (SIN(PI()* "+latitude+" /180)*SIN(PI() * tcc.cri_latitude/180)) + (COS(PI()* "+latitude+" /180)*cos(PI()*tcc.cri_latitude/180)*COS(PI() * tcc.cri_longitude/180-PI()* "+longitude+" /180))) < "+raio+") and tcc.cri_status=0 and (tcc.cri_data between '"+dIni.toString()+"' and '"+dFim.toString()+"') group by tipo.tcr_descricao";
		sqlCrimes = "SELECT cri_chave, tipo.tcr_idtipo_crime AS tipoCrime, cri_data as dataCrime, cri_horario as horarioCrime, cri_descricao as descricaoCrime, cri_latitude as latitudeCrime, cri_longitude as longitudeCrime FROM tb_cri_crime AS tcc INNER JOIN tb_tcr_tipo_crime AS tipo ON tipo.tcr_idtipo_crime=tcc.tcr_idtipo_crime AND ("+RAIO_TERRA_KM+" * ACOS( (SIN(PI()* "+latitude+" /180)*SIN(PI() * tcc.cri_latitude/180)) + (COS(PI()* "+latitude+" /180)*COS(PI()*tcc.cri_latitude/180)*COS(PI() * tcc.cri_longitude/180-PI()* "+longitude+" /180))) < "+raio+") AND tcc.cri_status=0 AND (tcc.cri_data BETWEEN "+dIni.toString()+" AND '"+dFim.toString()+"') ";
		
		//usa essa URL de teste...
		//http://localhost:8080/wikicrimes/CrimeRatioServlet?lat=-3.72927166&long=-38.51398944&raio=1.25
		
		Session session = getHibernateTemplate().getSessionFactory().openSession();//abre a secao do hibernate
		
		//e trabalha normalmente atraves da conex do BD
		Connection connect = session.connection();
		
			Statement ps;
			try {
				
				ps = connect.createStatement();
				ResultSet rs = ps.executeQuery(sqlCrimes);
			
				while(rs.next()){
	
					//crimes.append(rs.getString("tipoCrime") + "|" + rs.getString("dataCrime") + "|" + rs.getString("horarioCrime") + "|"+ rs.getString("descricaoCrime") + "|"+ rs.getString("latitudeCrime") + "|"+ rs.getString("longitudeCrime") + "\n");
					crimes.append(rs.getString("cri_chave") + "|" + rs.getString("latitudeCrime") + "|"+ rs.getString("longitudeCrime") + "|" + rs.getString("tipoCrime") + "\n");
					
				}
			
				rs.close();
				connect.close();
				session.close();
				
			} catch (SQLException e) {

				e.printStackTrace();
			}
		 
		return crimes;
	}

	@Override
	public boolean realizaAtivacao(String codApp) {
		
		//pesquisa o usuario que possui esse cod de aplicativo
		Usuario usuario = new Usuario();
		usuario.setMobileAppID(codApp);
		
		//consulta o usuario
		List<BaseObject> listBase = super.find(usuario);
		
		if(listBase==null || listBase.isEmpty()){
			return false;
		}
		
		Usuario user = (Usuario) listBase.get(0);
		
		//tento ativacao zero entao se ativa senao bloqueia o uso
		if(user.getMobileAppAtivacao()==0){
			
			user.setMobileAppAtivacao(1);
			user.setCountAtividadeMobile((long)0);
			super.save(user);
			
			return true;
		}else{
			return false;
		}
	}
}
