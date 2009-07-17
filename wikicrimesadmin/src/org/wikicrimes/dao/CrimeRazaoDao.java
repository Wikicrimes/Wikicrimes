package org.wikicrimes.dao;

import java.util.Set;

import org.wikicrimes.model.CrimeRazao;

public interface CrimeRazaoDao {
	
	public void deleteCrimeRazao(Set<CrimeRazao> razoesCrimes);
	
}
