package org.wikicrimes.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
	
	public static void noCache(HttpServletResponse response) {
		response.setContentType("text/plain");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setCharacterEncoding("iso-8859-1");
	}
	
	public static void sendText(HttpServletResponse response, String text) throws IOException{
		PrintWriter out = response.getWriter();
		out.print(text);
		out.flush();
	}
	
	public static void sendImage(HttpServletResponse response, Image imagem) throws IOException{
		//manda a imagem gerada pelo KernelMapRenderer
		response.setContentType("image/png");
		OutputStream out = response.getOutputStream();
		ImageIO.write((RenderedImage)imagem, "PNG", out);
	}
	
	public static void sendJson(HttpServletResponse response, JSONObject json) throws IOException{
		response.setContentType("application/json");
		json.write(response.getWriter());
	}
	
	public static String requestText(URL url) throws IOException{
		StringBuilder str = new StringBuilder();
		URLConnection con;
		try {
			con = url.openConnection();
			con.addRequestProperty("content-type", "text/html; charset=UTF-8");
//			/*DEBUG*/System.out.println( "content type: " + con.getContentType() );
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String linha = null;
			while((linha = in.readLine()) != null)
				str.append(linha);
		} catch (IOException e) {
			/*DEBUG*/System.out.println("DEBUG: IOException, Util.fazerRequisicao(), url = " + url);
			throw e;
		}
		
		return str.toString();
	}
	
	public static BufferedImage requestImage(URL url){
		BufferedImage imagem = null;
		try {
			
		    //acessar url
		    HttpURLConnection con = (HttpURLConnection)url.openConnection();
		    con.connect();
		    
		    //receber resposta
		    ImageInputStream input = new MemoryCacheImageInputStream(con.getInputStream()); 
		    imagem = ImageIO.read(input);
		    
		    con.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imagem;
	}
	
	public static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		}catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
	}
	
}