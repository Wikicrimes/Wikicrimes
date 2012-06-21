package org.wikicrimes.util.crimeBaseImport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public abstract class FileParser extends Parser{

	BufferedReader br;
	
	public FileParser(File file) throws IOException {
		br = new BufferedReader(new FileReader(file));
		br.readLine(); //skip header
	}
	
	@Override
	public String next() throws IOException {
		return br.readLine();
	}
	
	public static String[] splitFields(String rawData, char separator, char quote){
		List<String> rawFields = new ArrayList<String>();
		int begin = 0;
		boolean insideQuotes = false;
		for(int i=0; i<rawData.length(); i++){
			char c = rawData.charAt(i);
			if(c == separator){
				if(!insideQuotes){
					String field = rawData.substring(begin, i);
					rawFields.add(field);
					begin = i+1;
				}
			}else if(c == quote){
				insideQuotes = !insideQuotes;
			}
		}
		rawFields.add(rawData.substring(begin));
		return rawFields.toArray(new String[rawFields.size()]);
	}

	@Override
	protected abstract String getBaseName();

	@Override
	protected abstract String getBaseUrl();

	@Override
	protected abstract Model specificConvert(String rawData) throws ParseException, IgnoredCrimeException;

}
