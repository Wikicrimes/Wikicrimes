package org.wikicrimes.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Log;
import org.wikicrimes.model.Usuario;

public interface LogDao {
	
	public void update(Log log);
	public void insert(Log log);
	public List<BaseObject> getLogsUser(String id);
	public List<BaseObject> getLogsCrime(String id);
	public List<BaseObject> filter(Map parameters);
	
}
