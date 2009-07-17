package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.Relato;

public interface RelatoDao {
	public List<Relato> getRelatosSemEndereco();
	public Relato getRelatoById(Long id);
	public void update(Relato r);
}
