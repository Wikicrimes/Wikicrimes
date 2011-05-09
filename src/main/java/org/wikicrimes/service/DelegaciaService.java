package org.wikicrimes.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Delegacia;
import org.wikicrimes.model.EntidadeCertificadora;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.TipoArmaUsada;
import org.wikicrimes.model.TipoCrime;
import org.wikicrimes.model.TipoLocal;
import org.wikicrimes.model.TipoPapel;
import org.wikicrimes.model.TipoRegistro;
import org.wikicrimes.model.TipoTransporte;
import org.wikicrimes.model.TipoVitima;

public interface DelegaciaService extends GenericCrudService {
	public List<BaseObject> filter(Map parameters);
	public List<BaseObject> getDelegaciaList();
	public Delegacia getDelegaciaPorChave(String chave);
	
}
