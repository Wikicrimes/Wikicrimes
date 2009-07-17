package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.BaseObject;

public interface TipoLocalDao extends GenericCrudDao {

    public List<BaseObject> findTipoLocalByTipoVitima(Long idTipoVitima);
    
}
