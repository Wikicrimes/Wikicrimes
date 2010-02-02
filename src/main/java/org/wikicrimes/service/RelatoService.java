package org.wikicrimes.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.ConfirmacaoRelato;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.Relato;

public interface RelatoService extends GenericCrudService {	
	public List<BaseObject> filter(Map parameters);
	public List<BaseObject> listarRazoes();
	public List<BaseObject> listarRazoesSelecionadas(Relato relato);
	public boolean insert(BaseObject bo , List<Razao> razoes);
	public Relato getRelato(String chave);
	public Relato get(Long idRelato);
	public void increntaNumConfirmacoes(Relato relato,boolean tipo);
	public void update(Relato relato, Set<ConfirmacaoRelato> confirmacoes);
}
