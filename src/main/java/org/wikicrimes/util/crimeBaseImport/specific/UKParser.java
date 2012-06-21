package org.wikicrimes.util.crimeBaseImport.specific;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.util.crimeBaseImport.IgnoredCrimeException;
import org.wikicrimes.util.crimeBaseImport.Model;
import org.wikicrimes.util.crimeBaseImport.Parser;

/**
 * Varre o mapa fazendo requisicoes a api da policia de UK. Usa o metodo "street level crimes" que retorna crimes num raio de 1 milha do lat lng especificado.
 * 
 * http://policeapi2.rkh.co.uk/api/docs/method/crime-street/
 * 
 * @author victor
 * 
 * TODO: nao ta funcionando, pq n consegui fazer a requisicao autenticada 
 */
public class UKParser extends Parser {

	final static double STEP = 1.609344*Math.sqrt(2); //1.609344 eh a qtdade de qilometros em uma milha
													  //raiz de 2 eh a razao entre o raio do circulo e o lado do quadrado inscrito
	final static double N = 59.265881;
	final static double S = 49.781264;
	final static double E = 2.021484;
	final static double W = -9.316406;
	
	final static String USER = "wezaf43";
	final static String PASS = "f3e4a85f0739fc7ad4c8a77433aa2d83";
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		PontoLatLng p = new PontoLatLng(N, W);
		for(int i=0; p.lat > S; i++){
			p = new PontoLatLng(N, W);
			p = p.transladarKm(STEP/2, STEP/2);
			p = p.transladarKm(0, i*STEP);
			while(p.lng < E){
				requestCrimes(p);
				p = p.transladarKm(STEP, 0);
			}
		}
	}
	
	private static void requestCrimes(PontoLatLng p) throws MalformedURLException, IOException{
//		String url = "http://" + USER + ":" + PASS + "@policeapi2.rkh.co.uk/api/crimes-street/all-crime?lat=" + p.lat + "&lng=" + p.lng;
//		String url = "http://policeapi2.rkh.co.uk/api/crimes-street/all-crime?lat=" + p.lat + "&lng=" + p.lng;
//		String json = requestText(new URL(url));
		String json = apacheCommons(p);
		//TODO parsar json, escrever em arquivo
		System.out.println(json);
	}
	
	public static String requestText(URL url) throws IOException{
		System.out.println(url.getUserInfo());
		StringBuilder str = new StringBuilder();
		URLConnection con;
		try {
			con = url.openConnection();
			con.addRequestProperty("content-type", "text/html; charset=UTF-8");
			String crypto = md5(USER+PASS);
			System.out.println(crypto);
			con.addRequestProperty("Authorization", "Basic " + crypto);
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
	
	public static String apacheCommons(PontoLatLng p) throws HttpException, IOException{
		HttpClient client = new HttpClient();

		// pass our credentials to HttpClient, they will only be used for
		// authenticating to servers with realm "realm" on the host
		// "www.verisign.com", to authenticate against an arbitrary realm
		// or host change the appropriate argument to null.
		client.getState().setCredentials(new AuthScope("policeapi2.rkh.co.uk", 80, null), new UsernamePasswordCredentials(USER, PASS));

		// create a GET method that reads a file over HTTPS,
		// we're assuming that this file requires basic
		// authentication using the realm above.
		GetMethod get = new GetMethod("http://policeapi2.rkh.co.uk/api/crimes-street/all-crime?lat=" + p.lat + "&lng=" + p.lng);

		// Tell the GET method to automatically handle authentication. The
		// method will use any appropriate credentials to handle basic
		// authentication requests. Setting this value to false will cause
		// any request for authentication to return with a status of 401.
		// It will then be up to the client to handle the authentication.
		get.setDoAuthentication(true);

		// execute the GET
		int status = client.executeMethod(get);
		System.err.println("STATUS: " + status);

		// print the status and response
		String response = get.getResponseBodyAsString();
		get.releaseConnection();

		return response;
	}
	
	public static String md5(String senha) {
		String sen = "";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
		sen = hash.toString(16);
		return sen;
	}

	@Override
	protected String getBaseName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getBaseUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Model specificConvert(String rawData) throws ParseException, IgnoredCrimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String next() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
