package org.wikicrimes.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HTMLLogService 
{
	private static String conteudoHTML = "";
	private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    
	/**
	 * Envia o email diário de log
	 */
	public static void gravarHTML()
	{
		try
		{  
			String data = format.format(new Date());
			PrintWriter saida = new PrintWriter(new FileOutputStream("./logs/"+data+".html"));
		    conteudoHTML = "<html><head><title>" + data + "</title></head><body><h1>"+data+"</h1>" + conteudoHTML + "<body>";
		    saida.println(conteudoHTML);
		    conteudoHTML = "";
		    saida.flush();
			saida.close();
		}
		catch (FileNotFoundException e)
		{
		    e.printStackTrace();  
		}
	}
	
	/**
	 * Adiciona uma nova linha ao conteudo HTML
	 * 
	 * @param linha nova linha a ser adicionada no conteúdo html
	 */
	public static void addLinha(String linha)
	{
		conteudoHTML += linha + "<br/>";
	}
}
