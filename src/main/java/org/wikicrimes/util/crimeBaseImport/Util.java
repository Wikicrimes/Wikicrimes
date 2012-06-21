package org.wikicrimes.util.crimeBaseImport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import net.sf.json.JSONArray;

public class Util {

	public static JSONArray requestJson(URL url) throws IOException{
		StringBuilder strBuilder = new StringBuilder();
		URLConnection con = url.openConnection();
		con.addRequestProperty("content-type", "text/html; charset=UTF-8");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String linha = null;
		while((linha = in.readLine()) != null)
			strBuilder.append(linha);
		String str = strBuilder.toString();
		str = str.substring(2, strBuilder.length()-2);
		return JSONArray.fromObject(str);
	}
	
}
