package org.wikicrimes.service;

import org.wikicrimes.util.Email;

public class EmailErrorService
{
	private static String emailDestinatario = "support@wikicrimes.org",
		nomeDestinatario = "WikiCrimes", subject = "Schedule Exception!";
	
	/**
	 * Enviar um e-mail
	 * 
	 * @param e Exception a ser relatada
	 */
	public static void enviarEmail(Exception e)
	{
		try
		{
			String erro = e.toString();
			for(int i = 0; i<e.getStackTrace().length; i++)
			{
				erro += "<br/>" + e.getStackTrace()[i].toString();
			}
			
			Email.enviarEmail(emailDestinatario, nomeDestinatario, subject, erro);
			
			System.out.println(" *** email de erro enviado! ***");
		}
		catch(Exception er)
		{
			er.printStackTrace();
		}
	}
}
