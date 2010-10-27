package org.wikicrimes.util.rotaSegura.googlemaps;

public enum FormatoResposta {

	JSON, KML, WIKICRIMES;
	
	public static FormatoResposta getFormatoResposta(String str){
		if(str != null){
			if(str.equalsIgnoreCase("json")){
				return JSON;
			}else if(str.equalsIgnoreCase("kml")){
				return KML;
			}
		}
		return WIKICRIMES;
	}
}
