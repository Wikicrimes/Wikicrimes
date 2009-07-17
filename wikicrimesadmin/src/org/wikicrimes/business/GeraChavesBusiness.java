package org.wikicrimes.business;

import java.util.List;

import org.wikicrimes.model.ChavesCriptografia;

public interface GeraChavesBusiness {
	public List<ChavesCriptografia> consultaDominio(String dominio);
	public void adicionaChavesGeradas(ChavesCriptografia cc);
	public boolean verificaChaveGerada(ChavesCriptografia cc);
	public ChavesCriptografia geraChaves(String site);
}
