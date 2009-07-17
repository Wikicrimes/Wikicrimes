package org.wikicrimes.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class Email
{
	//Dados importantes.
	public static final String USER = "alert@wikicrimes.org", PASSWORD = "w1k1cr1m3salert", NAME = "WikiCrimes";
	private static final int DELAY_SECONDS = 10;
	
	//Variáveis gerais
	private static SimpleEmail mail;
	
	/**
	 * Enviar um e-mail
	 * 
	 * @param emailDestinatario e-mail do destinatário
	 * @param nomeDestinatario nome do destinatário
	 * @param subject subject(título) do e-mail
	 * @param conteudoHTML conteudo do e-mail
	 */
	public static void enviarEmail(String emailDestinatario, String nomeDestinatario, String subject, String conteudoHTML) throws EmailException
	{
		mail = new SimpleEmail();
		mail.setHostName("smtp.gmail.com");
		mail.setTLS(true);
		mail.setFrom(USER,NAME);
		mail.setAuthentication(USER,PASSWORD);
				
		mail.addTo(emailDestinatario, nomeDestinatario);
		mail.setSubject(subject);
		mail.setContent(conteudoHTML, "text/html");

		mail.send();
		
		//Thread adormece para descaracterizar flood.
		try {
			Thread.sleep(DELAY_SECONDS * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
