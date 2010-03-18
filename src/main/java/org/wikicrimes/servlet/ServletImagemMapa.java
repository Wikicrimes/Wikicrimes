package org.wikicrimes.servlet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.ImagemMapa;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.ImagemMapaService;
import org.wikicrimes.util.Util;
import org.wikicrimes.web.FiltroForm;
import org.wikicrimes.web.ImagemMapaForm;


/**
 * Trata requisições para gerar imagens do mapa. Fala com a Google Static Maps API.
 * 
 * @author victor
 */
public class ServletImagemMapa extends HttpServlet {

	private ImagemMapaService service;
	private CrimeService crimeService;
	private final int MARGEM = 10; //margem do polígono em relação à borda da imagem
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ImagemMapaService service = getService();
		HttpSession sessao = req.getSession();
		String acao = req.getParameter("acao");
		String id = req.getParameter("id");
		
		if(acao != null){
			
			if(acao.equals("pegaImagem")){
				
				//recupera os dados no banco (objeto ImagemMapa)
				ImagemMapa im = service.get(Integer.valueOf(id));
				
				//pega a imgaem (só o mapa) pela Google Static Maps API
				String urlGSM = constroiUrlGSM(im);
//				/*teste*/System.out.println("urlGSM: " + urlGSM);
				URL urlImagem = new URL(urlGSM);
				BufferedImage imagemMapa = requisicaoImagemGM(urlImagem);
				
				//pinta o poligono por cima
				pintaPoligono(im, imagemMapa);
				
				//pinta os marcadores por cima
				pintaMarcadores(im, imagemMapa, sessao);
				
				Util.enviarImagem(resp, imagemMapa);
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession sessao = req.getSession();
		String acao = req.getParameter("acao");
		
//		/*teste*/System.out.println("params: " + req.getParameterMap().keySet());
		
		if(acao.equals("novaImagem")){
			
			//dados pra identificar a imagem
			String centroLat = req.getParameter("centroLat");
			String centroLng = req.getParameter("centroLng");
			String zoom = req.getParameter("zoom");
			String width = req.getParameter("width");
			String height = req.getParameter("height");
			String poligono = req.getParameter("poligono");
			String north = req.getParameter("north");
			String south = req.getParameter("south");
			String east = req.getParameter("east");
			String west = req.getParameter("west");
			
			//persistencia
			long id = constroiImagemMapa(centroLat, centroLng, zoom, width, height, poligono, north, south, east, west, sessao);
			
			//envia o URL q dá acesso à imagem dentro dum html com outras informações
			PrintWriter out = resp.getWriter();
			String urlImagemMapa =  req.getScheme() + "://" + req.getServerName() + ":" 
									+ req.getServerPort() + req.getContextPath() + "/img.html?id=" + id; 
			out.write(urlImagemMapa);
//			/*teste*/System.out.println("urlImagemMapa: " + urlImagemMapa);
		}
	}
	
	private long constroiImagemMapa(String centroLat, String centroLng, String zoom, String width, String height, 
			String poligono, String north, String south, String east, String west, HttpSession sessao){
		ImagemMapaService service = getService();
		ImagemMapa im = new ImagemMapa();
		
		//centro, zoom, size
		PontoLatLng ponto = new PontoLatLng();
		ponto.setLatitude(Double.valueOf(centroLat));
		ponto.setLongitude(Double.valueOf(centroLng));
		service.save(ponto);
		im.setCentro(ponto);
		im.setZoom(Integer.valueOf(zoom));
		im.setWidth(Integer.valueOf(width));
		im.setHeight(Integer.valueOf(height));
		
		//políono
		List<PontoLatLng> vertices = im.setPoligonoAndReturn(poligono);
		service.save(vertices);
		
		//bounds
		im.setNorth(Double.valueOf(north));
		im.setSouth(Double.valueOf(south));
		im.setEast(Double.valueOf(east));
		im.setWest(Double.valueOf(west));
		
		//data-hora
		im.setDataHoraRegistro(new Date());
		
		//usuario
		Usuario usuario = (Usuario)sessao.getAttribute("usuario");
		im.setUsuario(usuario);
		
		//filtro crimes
		FiltroForm filtro = (FiltroForm)sessao.getAttribute("filtroForm");
		im.setFiltro(filtro.getMapaParametros());
//		/*teste*/ System.out.println("filtro setado: " + im.getFiltro());
		
		service.save(im);
		return im.getIdImagemMapa();
	}
	
	private String constroiUrlGSM(ImagemMapa im){
		//GSM = Google Static Maps
		PontoLatLng ponto = im.getCentro();
		String urlMapaLimpo = "http://maps.google.com/maps/api/staticmap?center=" + ponto.getLatitude() +","+ ponto.getLongitude() 
			+ "&zoom=" + im.getZoom() + "&size=" + (im.getWidth()+MARGEM*2) + "x" + (im.getHeight()+MARGEM*2) + "&sensor=false";
//			+ "&markers=" + latlngs;
		return urlMapaLimpo;
	}
	
	private BufferedImage requisicaoImagemGM(URL url){
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
			throw new AssertionError("não veio a imagem do Google Static Maps API.\n" + e.getMessage());
		}
		return imagem;
	}
	
	private void pintaPoligono(ImagemMapa im, Image imagem){
		int nPoints = im.getPoligono().size();
		int[] xPoints = new int[nPoints];
		int[] yPoints = new int[nPoints];
		for(int i=0; i<nPoints; i++){
			PontoLatLng latlng = im.getPoligono().get(i);
			Point pixel = toPixel(latlng, im);
			xPoints[i] = pixel.x;
			yPoints[i] = pixel.y;
		}
		Graphics2D g = (Graphics2D)imagem.getGraphics();
		g.setColor(new Color(0,0,1,.25f));
		g.fillPolygon(xPoints, yPoints, nPoints);
		g.setColor(new Color(0,0,1,.5f));
		g.setStroke(new BasicStroke(5));
		g.drawPolygon(xPoints, yPoints, nPoints);
	}
	
	private void pintaMarcadores(ImagemMapa im, Image imagem, HttpSession sessao){
		try{
	//		/*teste*/ System.out.println("filtro: " + im.getFiltro());
			Map<String, Object> param = ImagemMapaForm.getParams(im);
			List<BaseObject> crimes = getCrimeService().filter(param);
			Graphics2D g = (Graphics2D)imagem.getGraphics();
			ServletContext ctx = sessao.getServletContext();
			
			for(BaseObject o : crimes){
				if(o instanceof Crime){
					Crime c = (Crime)o;
					String tipo = c.getTipoCrime().getNome();
					String filePath = null;
					if(tipo.equals("tipocrime.roubo") || tipo.equals("tipocrime.tentativaderoubo"))
//						marcadorFile = new File("webapps/wikicrimes/images/baloes/vermelho.png");
						filePath = ctx.getRealPath("images/baloes/vermelho.png");
					else if(tipo.equals("tipocrime.furto") || tipo.equals("tipocrime.tentativadefurto"))
						filePath = ctx.getRealPath("images/baloes/novoMarcadorAzul.png");
					else
						filePath = ctx.getRealPath("/images/baloes/novoMarcadorLaranja.png");
					File marcadorFile = new File(filePath);
					Image marcador = ImageIO.read(marcadorFile);
					int height = marcador.getHeight(null);
					PontoLatLng latlng = new PontoLatLng(c.getLatitude(), c.getLongitude());
					Point p = toPixel(latlng, im);
					g.drawImage(marcador, p.x, p.y - height, null);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private Point toPixel(PontoLatLng latlng, ImagemMapa im){
		int pixelWidth = im.getWidth();
		int pixelHeight = im.getHeight();
		double latlngWidth = im.getEast() - im.getWest();
		double latlngHeight = im.getSouth() - im.getNorth();
		int razaoWidth = (int)(pixelWidth/latlngWidth);
		int razaoHeight = (int)(pixelHeight/latlngHeight);
		int x = (int)((latlng.getLongitude()-im.getWest()) * razaoWidth + MARGEM);
		int y = (int)((latlng.getLatitude()-im.getNorth()) * razaoHeight + MARGEM);
		return new Point(x,y);
	}
	
	private ImagemMapaService getService(){
		if(service == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			service = (ImagemMapaService)springContext.getBean("imagemMapaService");
		}
		return service;
	}
	
	private CrimeService getCrimeService(){
		if(crimeService == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			crimeService = (CrimeService)springContext.getBean("crimeService");
		}
		return crimeService;
	}
	
}
	