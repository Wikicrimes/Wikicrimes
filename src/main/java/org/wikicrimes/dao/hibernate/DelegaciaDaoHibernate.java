package org.wikicrimes.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.wikicrimes.dao.DelegaciaDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Delegacia;
/**
 * 
 */
public class DelegaciaDaoHibernate extends GenericCrudDaoHibernate implements
		DelegaciaDao {
	
	
	public DelegaciaDaoHibernate() {
		setEntity(Delegacia.class);
	}

				

	public List<BaseObject> filter(Map parameters) {
		String consulta = "from Delegacia as delegacia where ";
		if (parameters.get("norte")!=null && parameters.get("sul")!=null && parameters.get("leste")!=null && parameters.get("oeste")!=null){
			
			if (Double.parseDouble(parameters.get("leste").toString())> Double.parseDouble(parameters.get("oeste").toString())) {
				//retorna todos os crimes dentro da southwest/northeast boundary
				consulta+=" (delegacia.longitude< " + parameters.get("leste") + " and delegacia.longitude> " + parameters.get("oeste") + ") and (delegacia.latitude<= " + parameters.get("norte") + " and delegacia.latitude>= " + parameters.get("sul") + ")";
			}
			else {
				 //retorna todos os crimes dentro da southwest/northeast boundary
				 //split over the meridian
				consulta+=" (delegacia.longitude<= " + parameters.get("leste") + " or delegacia.longitude>= " + parameters.get("oeste") + ") and (delegacia.latitude<= " + parameters.get("norte") + " and delegacia.latitude>= " + parameters.get("sul") + ")";
			}
		}
		
		return (List<BaseObject>) getHibernateTemplate().find(consulta);
	}



	public Delegacia getDelegaciaPorChave(String chave) {
		String consulta = "from Delegacia as delegacia where chave = '" + chave+"'";
		List<BaseObject> delegaciaList = (List<BaseObject>) getHibernateTemplate().find(consulta);
		return (Delegacia) delegaciaList.get(0); 
	}

}
