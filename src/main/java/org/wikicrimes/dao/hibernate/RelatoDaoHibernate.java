package org.wikicrimes.dao.hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.wikicrimes.dao.RelatoDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Relato;

/**
 * 
 */
public class RelatoDaoHibernate extends GenericCrudDaoHibernate implements
		RelatoDao {

	public RelatoDaoHibernate() {
		setEntity(Relato.class);
	}

	public List<BaseObject> filter(Map parameters) {
		boolean entrouTipoLocal = false;
		String consulta = "from Relato as relato where ";
		
		//crimes no Viewport
		if (parameters.get("norte")!=null && parameters.get("sul")!=null && parameters.get("leste")!=null && parameters.get("oeste")!=null){
			
			if (Double.parseDouble(parameters.get("leste").toString())> Double.parseDouble(parameters.get("oeste").toString())) {
				//retorna todos os crimes dentro da southwest/northeast boundary
				consulta+=" (relato.longitude< " + parameters.get("leste") + " and relato.longitude> " + parameters.get("oeste") + ") and (relato.latitude<= " + parameters.get("norte") + " and relato.latitude>= " + parameters.get("sul") + ")";
			}
			else {
				 //retorna todos os crimes dentro da southwest/northeast boundary
				 //split over the meridian
				consulta+=" (relato.longitude<= " + parameters.get("leste") + " or relato.longitude>= " + parameters.get("oeste") + ") and (relato.latitude<= " + parameters.get("norte") + " and relato.latitude>= " + parameters.get("sul") + ")";
			}		
		}
		
		if (parameters.get("dataInicial") != null
				&& parameters.get("dataFinal") != null) {
			Date dataInicial = (Date) parameters.get("dataInicial");
			Date dataFinal = (Date) parameters.get("dataFinal");
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dataFinal);
			gc.add(GregorianCalendar.DAY_OF_MONTH, 1);
			dataFinal = gc.getTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String stringDataInicial = dateFormat.format(dataInicial);
			String stringDataFinal = dateFormat.format(dataFinal);

			consulta += " and (relato.dataHoraRegistro BETWEEN '" + stringDataInicial + "' and '"
					+ stringDataFinal + "'" + ") ";
		}
		
		//consulta+= "order by CRI_DATA desc";
		if (parameters.get("maxResults") != null)
			getHibernateTemplate().setMaxResults(
					(Integer) parameters.get("maxResults"));
		else  
			getHibernateTemplate().setMaxResults(0);
		consulta = consulta.replace("where  and", "where");			
		//System.err.println("\nConsulta: " + consulta + "\n");
		return (List<BaseObject>) getHibernateTemplate().find(consulta);
	}

	@Override
	public void increntaNumConfirmacoes(Relato relato, boolean tipo) {
		
		String query = "update Relato set ";
		if (tipo) {
			query += "CRI_QTD_POS=ifnull(CRI_QTD_POS,0)+1 ";
		} else
			query += " CRI_QTD_NEG=ifnull(CRI_QTD_NEG,0)+1 ";
		query += " where idRelato=" + relato.getIdRelato();
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		session.createQuery(query).executeUpdate();
		session.close();
		
		
	}
	
}
