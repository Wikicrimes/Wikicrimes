package org.wikicrimes.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cripto {
	private static MessageDigest md;
	
	static {
		
		 try {
				md= MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		
	}
	
	private static char[] hexCodes(byte[] text) {

        char[] hexOutput = new char[text.length * 2];

        String hexString;

 

        for (int i = 0; i < text.length; i++) {

            hexString = "00" + Integer.toHexString(text[i]);

            hexString.toUpperCase().getChars(hexString.length() - 2,

                                    hexString.length(), hexOutput, i * 2);

        }

        return hexOutput;

	}
	public static String criptografar(String texto) {

        if (md != null) {

            return new String(hexCodes(md.digest(texto.getBytes())));

        }

        return null;

}

}
