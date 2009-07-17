package org.wikicrimes.business;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Log;
import org.wikicrimes.model.Usuario;

public interface LogBusiness {
	
	public void update(Log log);
	public void inserir(Log log);
	public List<BaseObject> getLogsUser(String id);
	public List<BaseObject> getLogsCrime(String id);
	public List<BaseObject> filter(Map parameters);
	
}
