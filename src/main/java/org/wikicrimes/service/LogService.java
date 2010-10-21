package org.wikicrimes.service;

import org.wikicrimes.model.MobileRequestLog;

public interface LogService extends GenericCrudService {

	
	public boolean save(MobileRequestLog log);
	
	
}
