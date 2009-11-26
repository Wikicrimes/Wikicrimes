package org.wikicrimes.util;

import java.util.HashMap;
import java.util.Map;

public class GoogleMapsData {
	private double latitude,longitude;
	private String endereco;

	private String acentuado = "çÇáéíóúıÁÉÍÓÚİàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ";
    private String semAcento = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";
    
    private int tamanhoVetor=256;
    private Map<Character, Character> tabelaMudanca;
	
	public GoogleMapsData() {
		this(0, 0);
	}
	
	public GoogleMapsData(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		tabelaMudanca = new HashMap<Character, Character>();
		
		inicializaTabela();
	}
	
	private void inicializaTabela(){
        for (int i = 0; i < tamanhoVetor; ++i) {
            tabelaMudanca.put((char) i, (char) i);
        }
        for (int i = 0; i < acentuado.length(); ++i) {
        	Character ch = tabelaMudanca.remove((char)acentuado.charAt(i));
        	tabelaMudanca.put(ch, (char)semAcento.charAt(i));
        }
        semAcento=acentuado=null;
        System.gc();
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = removeAcentos(endereco);
	}
	
	private String removeAcentos(final String s){
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < s.length(); ++i) {
             char ch = s.charAt (i);
             if (ch < tamanhoVetor) {
                 sb.append (tabelaMudanca.get(ch));
             } else {
                 sb.append (ch);
             }
         }
         return sb.toString();
    }
}
