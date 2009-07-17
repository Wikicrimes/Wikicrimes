package org.wikicrimes.dao;

import java.io.BufferedReader;
import java.io.FileReader;

public class PropReader 
{
	private String texto;
	
	public PropReader(String url) throws Exception
	{
		texto = lerArquivo(url);
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
	
	public String consulta(String chave)
	{		
		int indiceFim;
		int indiceInicio = 0;
		boolean continua = true;
		
		do{
			indiceInicio = texto.indexOf(chave, indiceInicio);
			
			indiceFim = texto.indexOf( '=', indiceInicio )+1;
			
			if(texto.substring(indiceInicio, indiceFim-1).trim().equalsIgnoreCase(chave.trim()))
				continua = false;
			
			indiceInicio = indiceFim;
		}while( continua );
			
		return texto.substring( indiceFim, texto.indexOf(";*;", indiceFim) );
	}
}
