package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.ChavesCriptografia;

public interface GeraChavesDao {
	public List<ChavesCriptografia> consultaDominio(String dominio);
	public void adicionaChavesGeradas(ChavesCriptografia cc);
	public boolean verificaChaveGerada(ChavesCriptografia cc);
}
