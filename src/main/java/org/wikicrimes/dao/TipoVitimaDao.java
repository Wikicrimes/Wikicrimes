package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.CrimeVitima;

public interface TipoVitimaDao extends GenericCrudDao {

    public List<BaseObject> findTipoVitimaBytTipoCrime(CrimeVitima crimeVitima);
    
}
