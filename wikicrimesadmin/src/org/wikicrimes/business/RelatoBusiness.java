package org.wikicrimes.business;

import java.util.List;

import org.wikicrimes.model.Relato;

public interface RelatoBusiness {
	public List<Relato> getRelatosSemEndereco();
	public Relato getRelatoById(Long id);
	public void update(Relato r);
}
