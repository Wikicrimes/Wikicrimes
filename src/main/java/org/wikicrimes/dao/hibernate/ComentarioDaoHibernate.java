package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.wikicrimes.dao.ComentarioDao;
import org.wikicrimes.model.Comentario;

/**
 * 
 */
public class ComentarioDaoHibernate extends GenericCrudDaoHibernate implements
		ComentarioDao {

	public ComentarioDaoHibernate() {
		setEntity(Comentario.class);
	}
	
	public List<Comentario> getComentariosByCrime(String idCrime) {
		String query = "from Comentario c ";
   	
    	    query += "where c.crime.chave = '" + idCrime+"'";
    	    query += " order by dataConfirmacao";

    	return getHibernateTemplate().find(query);
        
	}

	public void salvaComentario(Comentario comentario) {
		this.getHibernateTemplate().save(comentario);
		
	}

}
