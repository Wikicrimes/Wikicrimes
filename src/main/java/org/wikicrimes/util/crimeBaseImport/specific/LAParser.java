package org.wikicrimes.util.crimeBaseImport.specific;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.wikicrimes.util.crimeBaseImport.IgnoredCrimeException;
import org.wikicrimes.util.crimeBaseImport.Model;
import org.wikicrimes.util.crimeBaseImport.Parser;
import org.wikicrimes.util.crimeBaseImport.Util;

/**
 * http://projects.latimes.com/mapping-la/crime/neighborhood/json/?hood=pico-robertson&start_date=2012-03-16&end_date=2012-04-16&callback=x
 * 
 * @author victor
 *
 * TODO: nao ta pronto ainda
 *
 */
public class LAParser extends Parser{

	
	
	private final Date firstDate = new Date();
	private final Date lastDate = new Date();
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		Date date1 = new Date();
		Date date2 = new Date();
		String url = "http://projects.latimes.com/mapping-la/crime/neighborhood/json/?hood=pico-robertson&start_date=" + date1 + "&end_date=" + date2 + "&callback=x";
		JSONArray arrayJson = Util.requestJson(new URL(url));
		
		for(int i=0; i<arrayJson.size(); i++){
			JSONObject crimeJson = arrayJson.getJSONObject(i);
			String title = crimeJson.getString("title");
			String dateStr = crimeJson.getString("start");
			String slug = crimeJson.getString("theft");
			String descHtml = crimeJson.getString("description");
			int descStart = descHtml.indexOf("<br>") + 5;
			int descEnd = descHtml.indexOf("<br>", descStart);
			String desc = descHtml.substring(descStart, descEnd);
			int idStart = descHtml.indexOf("href='/mapping-la/crime/report/") + 31;
			int idEnd = descHtml.indexOf("/", idStart);
			String id = descHtml.substring(idStart, idEnd);
			JSONObject pointJson = crimeJson.getJSONObject("point");
			double lat = pointJson.getDouble("lat");
			double lng = pointJson.getDouble("lng");
		}
		
	}
	
	protected LAParser() throws ClassNotFoundException, SQLException, IOException {
		super();
	}
	
	@Override
	protected String getBaseName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getBaseUrl() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String next() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Model specificConvert(String rawData) throws ParseException, IgnoredCrimeException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
