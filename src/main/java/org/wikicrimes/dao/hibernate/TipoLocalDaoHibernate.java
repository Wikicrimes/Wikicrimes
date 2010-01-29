package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.wikicrimes.dao.TipoLocalDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.TipoLocal;

/**
 * 
 */
public class TipoLocalDaoHibernate extends GenericCrudDaoHibernate implements
		TipoLocalDao {

    public TipoLocalDaoHibernate() {
	setEntity(TipoLocal.class);
    }

    public List<BaseObject> findTipoLocalByTipoVitima(Long idTipoVitima) {
	String query = "from TipoLocal tipoLocal ";

	if (idTipoVitima != null) {
	    query += "where tipoLocal.tipoVitima.idTipoVitima = " + idTipoVitima + "order by descricao";
	}

	return getHibernateTemplate().find(query);
    }

}
