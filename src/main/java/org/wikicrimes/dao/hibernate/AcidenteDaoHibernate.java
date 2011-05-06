package org.wikicrimes.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.wikicrimes.dao.AcidenteDao;
import org.wikicrimes.model.Acidente;

/**
 * 
 */
public class AcidenteDaoHibernate extends GenericCrudDaoHibernate implements
		AcidenteDao {

	public AcidenteDaoHibernate() {
		setEntity(Acidente.class);
	}

	@SuppressWarnings("unchecked")
	public List<Acidente> filter(Map<String, Object> parameters) {
		String consulta = "from Acidente as aci where ";
		
		//crimes no Viewport
		if (parameters.get("norte")!=null && parameters.get("sul")!=null && parameters.get("leste")!=null && parameters.get("oeste")!=null){
			
			if (Double.parseDouble(parameters.get("leste").toString())> Double.parseDouble(parameters.get("oeste").toString())) {
				//retorna todos os crimes dentro da southwest/northeast boundary
				consulta+=" (aci.longitude< " + parameters.get("leste") + " and aci.longitude> " + parameters.get("oeste") + ") and (aci.latitude<= " + parameters.get("norte") + " and aci.latitude>= " + parameters.get("sul") + ")";
			}
			else {
				 //retorna todos os crimes dentro da southwest/northeast boundary
				 //split over the meridian
				consulta+=" (aci.longitude<= " + parameters.get("leste") + " or aci.longitude>= " + parameters.get("oeste") + ") and (aci.latitude<= " + parameters.get("norte") + " and aci.latitude>= " + parameters.get("sul") + ")";
			}		
		}
		
		return (List<Acidente>) getHibernateTemplate().find(consulta);
	}
	
	
}
