package org.wikicrimes.util;

public class GoogleMapsData {
	private double latitude,longitude;
	private String endereco;

	private String acentuado = "çÇáéíóúıÁÉÍÓÚİàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ";
    private String semAcento = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";
    private char[] tabela;
	
	public GoogleMapsData() {
		latitude=longitude=0;
		inicializaTabela();
	}
	
	public GoogleMapsData(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		inicializaTabela();
	}
	
	private void inicializaTabela(){
		tabela = new char[256];
        for (int i = 0; i < tabela.length; ++i) {
            tabela [i] = (char) i;
        }
        for (int i = 0; i < acentuado.length(); ++i) {
            tabela [acentuado.charAt(i)] = semAcento.charAt(i);
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
             if (ch < 256) {
                 sb.append (tabela [ch]);
             } else {
                 sb.append (ch);
             }
         }
         return sb.toString();
    }
}
