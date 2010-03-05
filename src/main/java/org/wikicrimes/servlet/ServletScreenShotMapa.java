package org.wikicrimes.servlet;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.ImagemMapaService;


/**
 * Gera uma imagem de uma área do mapa com um crime central e crimes adjacentes sem filtro de data.
 * Realize uma requisição no seguinte molde: http://localhost:8080/wikicrimes/ServletScreenShotMapa?chaveCrime=(chave) 
 * onde (chave) é a chave do crime central.
 * Comunica-se com a Google Static Maps API(http://code.google.com/intl/pt-BR/apis/maps/documentation/staticmaps/).
 * @author André
 */

public class ServletScreenShotMapa extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ImagemMapaService imagemMapaService;
	private CrimeService crimeService;
	
	//Razão entre pixel e lat e lng para o zoom 18
	private int razaoWidth = 186413;
	private int razaoHeight = 186808;
	
	//Limites da imagem em latlng
	private double north, south, east, west;
	
	//Tamanho da imagem em pixels
	private double imageWidth = 256;
	private double imageHeight = 256;

	//Zoom usado para criar a iamgem
	private int zoom = 18;
	
	private Crime crimeCentral;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//Recupera a chave do crime central
		String chaveCrime = request.getParameter("chaveCrime");
		
		imagemMapaService = getService();
		crimeService = getCrimeService();
		
		//Recupera o crime central através da chave
		crimeCentral = crimeService.getCrime(chaveCrime);
		
		String urlGSM = constroiUrlGSM(crimeCentral);
		
		URL urlImagem = new URL(urlGSM);
		BufferedImage imagemMapa = requisitarImagemGM(urlImagem);
		
		//Calcula os valores limite da imagem em latlng
		north = crimeCentral.getLatitude() + (imageHeight/2)/razaoHeight;
		south = crimeCentral.getLatitude() - (imageHeight/2)/razaoHeight;
		east = crimeCentral.getLongitude() + (imageWidth/2)/razaoWidth;
		west = crimeCentral.getLongitude() - (imageWidth/2)/razaoWidth;
		
		//Pinta os marcadores na imagem
		pintaMarcadores(imagemMapa);
		
		response.setContentType("image/png");
		OutputStream out = response.getOutputStream();
		ImageIO.write(imagemMapa, "PNG", out);
	}
	
	/**
	 * @param centro - Ponto central do mapa
	 * @return URL preparada para requisitar imagem do Google Static Maps
	 */
	private String constroiUrlGSM(Crime crimeCentral){
		/* 
		 * Para mais informações 
		 * sobre a URL ver: http://code.google.com/intl/pt-BR/apis/maps/documentation/staticmaps/
		 */
		
		String urlMapaLimpo = 
			"http://maps.google.com/maps/api/staticmap?center="
			+ crimeCentral.getLatitude() +"," + crimeCentral.getLongitude()
			+ "&zoom=" + zoom + "&size=" + (int)imageWidth + "x" + (int)imageHeight + "&sensor=false";
		
		return urlMapaLimpo;
	}
	
	
	/**
	 * Método que retorna uma imagem com um trecho do mapa. Para mais informações ver: http://code.google.com/intl/pt-BR/apis/maps/documentation/staticmaps/
	 * @param url - URL de request para o Google Static Maps
	 * @return Imagem do mapa desejada de acordo com os parametros da requisição
	 */
	private BufferedImage requisitarImagemGM(URL url){
		BufferedImage imagem = null;
		HttpURLConnection con = null;
		try {
		    //Acessa a url
		    con = (HttpURLConnection)url.openConnection();
		    con.connect();
		    
		    //Recebe a resposta
		    ImageInputStream input = new MemoryCacheImageInputStream(con.getInputStream()); 
		    imagem = ImageIO.read(input);

		} catch (IOException e) {
			e.printStackTrace();
			throw new AssertionError("Impossível recuperar a imagem do Google Static Maps API.\n" + e.getMessage());
		} finally {
			con.disconnect();
		}
		return imagem;
	}
	
	/**
	 * Pinta na imagem os marcadores do tipo crime que se encontram nos limites da imagem.
	 * @param imagem - Imagem que que terá os marcadores pintados.
	 * @throws IOException
	 */
	private void pintaMarcadores(Image imagem) throws IOException{
		List<BaseObject> crimes = getCrimesNaArea();
		Graphics2D g = (Graphics2D)imagem.getGraphics();
		
		for(BaseObject o : crimes)
			if(o instanceof Crime){
				Crime c = (Crime)o;
				String tipo = c.getTipoCrime().getNome();
				File marcadorFile = null;

				if(c.getChave().equals(crimeCentral.getChave())) {
					if(tipo.equals("tipocrime.roubo") || tipo.equals("tipocrime.tentativaderoubo"))
						marcadorFile = new File("webapps/wikicrimes/images/baloes/vermelhoDestaque.png");
					else if(tipo.equals("tipocrime.furto") || tipo.equals("tipocrime.tentativadefurto"))
						marcadorFile = new File("webapps/wikicrimes/images/baloes/azulDestaque.png");
					else
						marcadorFile = new File("webapps/wikicrimes/images/baloes/laranjaDestaque.png");
				} else {
					if(tipo.equals("tipocrime.roubo") || tipo.equals("tipocrime.tentativaderoubo"))
						marcadorFile = new File("webapps/wikicrimes/images/baloes/vermelho.png");
					else if(tipo.equals("tipocrime.furto") || tipo.equals("tipocrime.tentativadefurto"))
						marcadorFile = new File("webapps/wikicrimes/images/baloes/novoMarcadorAzul.png");
					else
						marcadorFile = new File("webapps/wikicrimes/images/baloes/novoMarcadorLaranja.png");
				}
				Image marcador = ImageIO.read(marcadorFile);
				int height = marcador.getHeight(null);
				PontoLatLng latlng = new PontoLatLng(c.getLatitude(), c.getLongitude());
				Point p = toPixelInZoom18(latlng);
				if(c.getChave().equals(crimeCentral.getChave()))
					g.drawImage(marcador, p.x - 13, p.y - (height - 2), null);
				else 
					g.drawImage(marcador, p.x, p.y - height, null);
			}
	}
	
	/**
	 * Transforma um ponto latlong em pixel
	 * @param pontoLatLong
	 * @return Ponto em pixel equivalente
	 */
	private Point toPixelInZoom18(PontoLatLng pontoLatLong){
		int x = (int)((pontoLatLong.getLongitude() - west) * razaoWidth);
		int y = (int)((north - pontoLatLong.getLatitude()) * razaoHeight);
		return new Point(x,y);
	}

	/**
	 * @param ponto1 - Limite superior esquerdo
	 * @param ponto2 - Limite inferior direito
	 * @return Crimes dentro da área
	 */
	@SuppressWarnings("unchecked")
	private List<BaseObject> getCrimesNaArea() {
		Map parameters = new HashMap();
		
		parameters.put("norte", north);
		parameters.put("sul", south);
		parameters.put("oeste", west);
		parameters.put("leste", east);
		
		List<BaseObject> result = crimeService.filter(parameters);
		
		return result;
	}
	
	private ImagemMapaService getService(){
		if(imagemMapaService == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			imagemMapaService = (ImagemMapaService)springContext.getBean("imagemMapaService");
		}
		return imagemMapaService;
	}
	
	private CrimeService getCrimeService(){
		if(crimeService == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			crimeService = (CrimeService)springContext.getBean("crimeService");
		}
		return crimeService;
	}
	
}
	