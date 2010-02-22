package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.ImagemMapaDao;
import org.wikicrimes.model.ImagemMapa;

public class ImagemMapaDaoHibernate extends GenericCrudDaoHibernate implements
		ImagemMapaDao {

	public ImagemMapa get(long id) {
		String query = "FROM ImagemMapa i WHERE i.idImagemMapa =" + id;
		return (ImagemMapa)getHibernateTemplate().find(query).get(0);
	}
	
}
