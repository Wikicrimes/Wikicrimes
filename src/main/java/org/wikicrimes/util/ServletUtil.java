package org.wikicrimes.util;

import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtil {

	enum Browser{ 
		INTERNET_EXPLORER, NETSCAPE, UNKNOWN 
	}
	
	private static Browser getClientBrowser(HttpServletRequest req) {
		String s = req.getHeader("user-agent");
		if (s == null)
			return Browser.UNKNOWN;
		if (s.indexOf("MSIE") > -1)
			return Browser.INTERNET_EXPLORER;
		else if (s.indexOf("Netscape") > -1)
			return Browser.NETSCAPE;
		else
			return Browser.UNKNOWN;
		// TODO fazer os outros quando precisar...
	}
	
	public static boolean isClientUsingIE(HttpServletRequest req) {
		return getClientBrowser(req) == Browser.INTERNET_EXPLORER;
	}
	
	public static void enviarImagem(HttpServletResponse response, RenderedImage imagem) throws IOException{
		//manda a imagem gerada pelo KernelMapRenderer
		response.setContentType("image/png");
		OutputStream out = response.getOutputStream();
		ImageIO.write(imagem, "PNG", out);
	}
	
	public static String fazerRequisicao(URL url) throws IOException{
		StringBuilder str = new StringBuilder();
		URLConnection con;
		try {
			con = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String linha = null;
			while((linha = in.readLine()) != null)
				str.append(linha);
		} catch (IOException e) {
			/*DEBUG*/System.out.println("DEBUG: IOException, Util.fazerRequisicao(), url = " + url);
			throw e;
		}
		
		return str.toString();
	}
	
}