package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.wikicrimes.dao.TipoVitimaDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.CrimeVitima;
import org.wikicrimes.model.TipoVitima;

/**
 * 
 */
public class TipoVitimaDaoHibernate extends GenericCrudDaoHibernate implements
		TipoVitimaDao {

	public TipoVitimaDaoHibernate() {
		setEntity(TipoVitima.class);
	}
	
	public List<BaseObject> findTipoVitimaBytTipoCrime(CrimeVitima crimeVitima) {
		return getHibernateTemplate()
				.find(
						"select tipoVitima " +
						"from CrimeVitima as crimeVitima, " +
						"TipoCrime tipoCrime, " +
						"TipoVitima tipoVitima " +
						"where " +
						"crimeVitima.tipoCrime = tipoCrime and " +
						"crimeVitima.tipoVitima = tipoVitima and " +
						"tipoCrime.idTipoCrime = " + crimeVitima.getTipoCrime().getIdTipoCrime()
				);
	}

}
