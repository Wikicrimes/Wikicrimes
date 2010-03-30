package org.wikicrimes.service;

import org.wikicrimes.model.LogUbiquity;

public interface LogUbiquityService extends GenericCrudService{
	
	public void update(LogUbiquity log);
	public void inserir(LogUbiquity log);
}