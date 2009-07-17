package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.wikicrimes.dao.CrimeVitimaDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.CrimeVitima;

/**
 * 
 */
public class CrimeVitimaDaoHibernate extends GenericCrudDaoHibernate implements
		CrimeVitimaDao {

	public CrimeVitimaDaoHibernate() {
		setEntity(CrimeVitima.class);
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
