package org.wikicrimes.dao;

import java.io.BufferedReader;
import java.io.FileReader;

public class Menssagem 
{
	private String portugues, espanhol, ingles;
	
	public Menssagem() throws Exception
	{
		PropReader prop = new PropReader("./properties/idiomas.properties");
		
		portugues = lerArquivo(prop.consulta("messages_pt"));
		espanhol = lerArquivo(prop.consulta("messages_es"));
		ingles = lerArquivo(prop.consulta("messages_en"));
	}
	
	public String lerArquivo(String url) throws Exception
	{
		BufferedReader in = new BufferedReader(new FileReader(url));
        String texto = "";
        
        while(in.ready())
        {
        	texto += in.readLine()+";*;";
        }
        in.close();
        
        return texto;
	}
	
	public String consulta(String chave, String idioma)
	{
		String textoAux = "";
		
		if(idioma != null)
			if(idioma.equalsIgnoreCase("en"))
				textoAux = ingles;	
			else if(idioma.equalsIgnoreCase("es"))
				textoAux = espanhol;
			else
				textoAux = portugues;
		else
			textoAux = portugues;
		
		int indiceFim;
		int indiceInicio = 0;
		boolean continua = true;
		
		do{
			indiceInicio = textoAux.indexOf(chave, indiceInicio);
			
			indiceFim = textoAux.indexOf( '=', indiceInicio )+1;
			
			if(textoAux.substring(indiceInicio, indiceFim-1).trim().equalsIgnoreCase(chave.trim()))
				continua = false;
			
			indiceInicio = indiceFim;
		}while( continua );
			
		return textoAux.substring( indiceFim, textoAux.indexOf(";*;", indiceFim) );
	}
}
