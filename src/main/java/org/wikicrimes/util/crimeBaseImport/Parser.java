package org.wikicrimes.util.crimeBaseImport;

import java.io.IOException;
import java.text.ParseException;

public abstract class Parser {
	
	protected Long baseId;
	
	protected void setBaseId(long id){
		this.baseId = id;
	}
	
	public Long getBaseId(){
		return baseId;
	}
	
	protected abstract String getBaseName();
	
	protected abstract String getBaseUrl();
	
	public Model convert(String rawData) throws ParseException, IgnoredCrimeException{
		try{
			Model crime = specificConvert(rawData);
			crime.CRI_ID_BASE_ORIGEM = getBaseId();
			crime.setDerivedFields();
			return crime;
		}catch(IgnoredCrimeException e){
			e.setConverter(this);
			e.setRawData(rawData);
			throw e;
		}
	}
	
	protected abstract Model specificConvert(String rawData) throws ParseException, IgnoredCrimeException;
	
	public abstract String next() throws IOException;
	
}
