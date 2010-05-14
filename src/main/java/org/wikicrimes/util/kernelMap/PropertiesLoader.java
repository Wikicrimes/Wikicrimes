package org.wikicrimes.util.kernelMap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {

	private static Properties prop;
	
	private static void init(){
		prop = new Properties();
		try {
			prop.load(PropertiesLoader.class.getClassLoader().getResourceAsStream("kernelrotas.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int getInt(String s){
		if(prop == null) 
			init();
		String v = prop.getProperty(s);
		return Integer.valueOf(v);
	}
	
	public static double getDouble(String s){
		if(prop == null) 
			init();
		String v = prop.getProperty(s);
		return Double.valueOf(v);
	}
	
}
