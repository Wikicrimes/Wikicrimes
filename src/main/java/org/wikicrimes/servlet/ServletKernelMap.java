package org.wikicrimes.servlet;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.dao.hibernate.PontoLatLngDaoHibernate;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.model.Relato;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.util.Util;
import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.KernelMapRenderer;
import org.wikicrimes.util.kernelMap.LatLngBoundsGM;
import org.wikicrimes.util.kernelMap.Ponto;
import org.wikicrimes.util.kernelMap.PropertiesLoader;
import org.wikicrimes.util.kernelMap.testes.TesteCenariosRotas;
import org.wikicrimes.web.FiltroForm;

/**
 * Trata requisições HTTP para calcular mapa de kernel e gerar imagem.
 * 
 * @author victor
 */
@SuppressWarnings("serial")
public class ServletKernelMap extends HttpServlet {
	
	final static String IMAGEM_KERNEL = "IMAGEM_KERNEL"; //imagem renderizada do mapa de kernel do wikicrimes
	final static String IMAGEM_KERNEL_WIKIMAPPS = "IMAGEM_KERNEL_WIKIMAPPS"; //imagem renderizada do mapa de kernel do wikimapps
	final static String DENSIDADES = "DENSIDADES"; //matriz de densidades do mapa de kernel
	final static String KERNEL = "KERNEL"; //objeto MapaKernel
	
	public final static int GRID_NODE = PropertiesLoader.getInt("node_size");
	public final static int BANDWIDTH = PropertiesLoader.getInt("bandwidth");
	
	private CrimeService crimeService;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession sessao = request.getSession();
		response.setContentType("text/plain");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setCharacterEncoding("iso-8859-1");
		
//		/*teste*/System.out.println(request.getParameterMap().keySet() + " - pontoXY:" + request.getParameter("pontoXY"));
		
		String acao = request.getParameter("acao");
		String app = request.getParameter("app");
		if(acao != null)
		if(acao.equals("geraKernel")){
			//calcular o mapa de kernel e criar imagens			
			
			if(app!= null && app.equals("wikimapps")) {
				Util.enviarImagem(response, gerarMapaKernel2(request));
			}
			else {
				gerarMapaKernel2(request);
			}
	
		}else if(acao.equals("pegaImagem")){
			RenderedImage imagem;			
			//verifica a aplicacao que acionou o servico. o default é wikicrimes
			if(app!= null && app.equals("wikimapps")) {
				imagem= (RenderedImage)sessao.getAttribute(IMAGEM_KERNEL_WIKIMAPPS);
			}
			else {
				imagem= (RenderedImage)sessao.getAttribute(IMAGEM_KERNEL);
			}
			
			if(imagem != null)
				Util.enviarImagem(response, imagem);
		}else if(acao.equals("pegaInfo")){
			KernelMap kernel = (KernelMap)sessao.getAttribute(KERNEL);
			double[][] dens = kernel.getDensidadeGrid();
			enviarInfo(request, response, dens);
		}
		
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	//a diferença entre o gerarMapaKernel() e o gerarMapaKernel2() é q o primeiro pega os crimes do cliente, o segundo pega do servidor   
	private static void gerarMapaKernel(HttpServletRequest request) throws ServletException, IOException {
		///*teste*/long ini = System.currentTimeMillis();
		HttpSession sessao = request.getSession();
		Rectangle bounds = getLimitesPixel(request);
		int zoom = getZoom(request);
		
		//extrai informação dos parâmetros de requisição
		List<Point> pontos = getPoints(request);
		/*teste*/TesteCenariosRotas.setResult("numCrimes", pontos.size());
		
		//calcula as densidades
		KernelMap kernel = new KernelMap(GRID_NODE, BANDWIDTH, bounds, pontos);
		sessao.setAttribute(KERNEL, kernel);
//		/*teste*/System.out.println(kernel);
		/*teste*/TesteCenariosRotas.setResult("densMedia", kernel.getMediaDens());
		/*teste*/TesteCenariosRotas.setResult("densMax", kernel.getMaxDens());
		
//		/*teste*/long fim = System.currentTimeMillis();
//		/*teste*/System.out.println("TEMPO da geração do mapa de kernel: " + (fim-ini));
		
		KernelMapRenderer kRend = new KernelMapRenderer(kernel);
//		RenderedImage imagem = (zoom > 10)? (RenderedImage)kRend.pintaKernel() : (RenderedImage)kRend.pintaKernel(true);
		boolean isIE = Util.isClientUsingIE(request); 
		RenderedImage imagem = (RenderedImage)kRend.pintaKernel(zoom, isIE);
		sessao.setAttribute(IMAGEM_KERNEL, imagem);
		
		///*teste*/testeArquivo(imagem);
	}
	
	//a diferença entre o gerarMapaKernel() e o gerarMapaKernel2() é q o primeiro pega os crimes do cliente, o segundo pega do servidor
	private RenderedImage gerarMapaKernel2(HttpServletRequest request){
		
		HttpSession sessao = request.getSession();
		String app = request.getParameter("app");
		
		//pega bounds e zoom dos parâmetros de requisição
		LatLngBoundsGM limitesLatlng = getLimitesLatLng(request);
		Rectangle limitesPixel = getLimitesPixel(request);
		int zoom = getZoom(request);
		//verifica a aplicacao que acionou o servico. o default é wikicrimes
		if(app!= null && app.equals("wikimapps")) {
			List<Point> pontos =recuperaPontosWikiMapps(limitesLatlng,request.getParameter("url"),request.getParameter("tm"),zoom);
			KernelMap kernel = new KernelMap(GRID_NODE, BANDWIDTH, limitesPixel, pontos);			
			KernelMapRenderer kRend = new KernelMapRenderer(kernel);
			boolean isIE = Util.isClientUsingIE(request); 
			RenderedImage imagem = (RenderedImage)kRend.pintaKernel(zoom, isIE);
			return imagem;//sessao.setAttribute(IMAGEM_KERNEL_WIKIMAPPS, imagem);			
		}else{
		//pega crimes
		FiltroForm filtro = (FiltroForm)sessao.getAttribute("filtroForm");
		Map<String,Object> params = filtro.getFiltroMap();
		params.put("norte", limitesLatlng.norte);
		params.put("sul", limitesLatlng.sul);
		params.put("leste", limitesLatlng.leste);
		params.put("oeste", limitesLatlng.oeste);
//		/*tsete*/params.put("maxResults", 100);
		List<BaseObject> crimes = getCrimeService().filter(params);
		List<Point> pontos = toPixel(crimes, zoom);
		/*teste*/TesteCenariosRotas.setResult("numCrimes", pontos.size());
		
		//calcula as densidades
		KernelMap kernel = new KernelMap(GRID_NODE, BANDWIDTH, limitesPixel, pontos);
		sessao.setAttribute(KERNEL, kernel);
		/*teste*/TesteCenariosRotas.setResult("densMedia", kernel.getMediaDens());
		/*teste*/TesteCenariosRotas.setResult("densMax", kernel.getMaxDens());
		
		KernelMapRenderer kRend = new KernelMapRenderer(kernel);
		boolean isIE = Util.isClientUsingIE(request); 
		RenderedImage imagem = (RenderedImage)kRend.pintaKernel(zoom, isIE);
		sessao.setAttribute(IMAGEM_KERNEL, imagem);
		return imagem;
		
		}
//		/*teste*/testeArquivo(imagem);
//		/*teste*/System.out.println("numPontos:"+pontos.size());
	}
	
	public static void enviarInfo(HttpServletRequest request, HttpServletResponse response, double[][] dens) throws IOException{
		//manda a matriz de densidades gerada pelo KernelMap
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		
		//nodeSize
		out.println(GRID_NODE);
		out.println(BANDWIDTH);

		//estatisticas
		if(request.getSession().getAttribute(KERNEL) instanceof KernelMap){
			KernelMap kernel = (KernelMap)request.getSession().getAttribute(KERNEL);
			out.println(kernel.getMaxDens());
			out.println(kernel.getMediaDens());
			out.println(0); //TODO calcular o minimo 
		}else{
			out.println("\n\n\n");
		}
		
		//grid
		if(dens != null)
			for(double[] coluna : dens){
				for(double d : coluna){
					out.print(d + ",");
				}
				out.println();
			}
		out.close();
	}
	
	public static List<Point> getPoints(ServletRequest request){
		String pontosStr = request.getParameter("pontoXY");
		return getPoints(pontosStr);
	}
	
	public static List<Point> getPoints(String pontosStr){
		List<Point> pontos = new LinkedList<Point>();
		if(pontosStr == null || pontosStr.isEmpty())
			return pontos;
		for(String pontoStr : pontosStr.split("a")){
			pontos.add(new Ponto(pontoStr).invertido());  //FIXME desfazendo a inversão q foi feita lá no JavaScript
		}
		return pontos;
	}
	
//	private List<Ponto> getPontos(ServletRequest request){
//	String pontosStr = request.getParameter("pontoXY");
//	List<Ponto> pontos = new LinkedList<Ponto>();
//	for(String pontoStr : pontosStr.split("a")){
//		pontos.add(new Ponto(pontoStr).invertido());  //FIXME invertida pq em algum momento posterior isto está sendo invertido novamente
//	}
//	return pontos;
//}
	
	private static Rectangle getLimitesPixel(ServletRequest request){
		//obs: width = east-west não funciona no caso em q a emenda do mapa está aparecendo na tela (a linha entre Japão e EUA) 
		//width seria negativo já q west>east
		//por isso o width e o height estão sendo calculado no javascript, usando as coordenadas do centro da tela
		String northStr = request.getParameter("northPixel");
//		String southStr = request.getParameter("southPixel");
//		String eastStr = request.getParameter("eastPixel");
		String westStr = request.getParameter("westPixel");
		String widthStr = request.getParameter("widthPixel");
		String heightStr = request.getParameter("heightPixel");
		try{
			int north = Integer.parseInt(northStr);
//			int south = Integer.parseInt(southStr);
//			int east = Integer.parseInt(eastStr);
			int west = Integer.parseInt(westStr);
			int width = Integer.parseInt(widthStr);
			int height = Integer.parseInt(heightStr);
			return new Rectangle(west, north, width, height);
		}catch(NumberFormatException e){
//			throw new AssertionError("parametro faltando no getLimitesPixel. " +
//					"northPixel: " + northStr + ", southPixel: " + southStr + ", eastPixel: " + eastStr + ", westPixel: " + westStr );
			throw new AssertionError("parametro faltando no getLimitesPixel. " +
					"northPixel: " + northStr + ", westPixel: " + westStr + ", width: " + widthStr + ", height: " + heightStr);
		}
	}
	
	private static LatLngBoundsGM getLimitesLatLng(ServletRequest request){
		//obs: width = east-west não funciona no caso em q a emenda do mapa está aparecendo na tela (a linha entre Japão e EUA) 
		//width seria negativo já q west>east
		//por isso o width e o height estão sendo calculado no javascript, usando as coordenadas do centro da tela
		String northStr = request.getParameter("northLatLng");
		String southStr = request.getParameter("southLatLng");
		String eastStr = request.getParameter("eastLatLng");
		String westStr = request.getParameter("westLatLng");
		String widthStr = request.getParameter("widthLatLng");
		String heightStr = request.getParameter("heightLatLng");
		try{
			double north = Double.parseDouble(northStr);
			double south = Double.parseDouble(southStr);
			double east = Double.parseDouble(eastStr);
			double west = Double.parseDouble(westStr);
			double width = Double.parseDouble(widthStr);
			double height = Double.parseDouble(heightStr);
			return new LatLngBoundsGM(north, south, east, west, width, height);
		}catch(NumberFormatException e){
			throw new AssertionError("parametro faltando no getLimitesPixel. " +
					"northLatLng: " + northStr + ", southLatLng: " + southStr + ", eastLatLng: " + eastStr + ", westLatLng: " + westStr + 
					", widthLatLng: " + widthStr + ", heightLatLng: " + heightStr );
		}
	}
	
	/**
	 * obs: O zoom do GoogleMaps varia de 0 a 19. Zero é o mais de longe. 
	 */
	public static int getZoom(ServletRequest request){
		String zoomStr = request.getParameter("zoom");
		try{
			return Integer.parseInt(zoomStr);
		}catch(NumberFormatException e){
			throw new AssertionError("problema com o parametro \"zoom\" na requisição. zoom: " + zoomStr);
		}
	}
	
	private static List<Point> toPixel(List<BaseObject> crimes, int zoom){
		List<Point> pontos = new ArrayList<Point>();
		for(BaseObject o : crimes){
			PontoLatLng p = null;
			if(o instanceof Crime){
				Crime c = (Crime)o;
				p = new PontoLatLng(c.getLatitude(), c.getLongitude());
			}else if(o instanceof Relato){
				Relato r = (Relato)o;
				p = new PontoLatLng(r.getLatitude(), r.getLongitude());
			}else{
				continue;
			}
			pontos.add(toPixel(p, zoom));
		}
		return pontos;
	}
	
	private static Point toPixel(PontoLatLng latlng, int zoom){
		double offset = 256 << (zoom-1);
		double lat = latlng.getLatitude();
		double lng = latlng.getLongitude();
	    int x = (int)Math.round(offset + (offset * lng / 180));
	    int y = (int)Math.round(offset - offset/Math.PI * Math.log((1 + Math.sin(lat * Math.PI / 180)) / (1 - Math.sin(lat * Math.PI / 180))) / 2);
		return new Point(x,y);
	}
	
	//hotspots ficam deslocados no zoom alto (o outro metodo toPixel acima faz direito)
	private static Point toPixel(PontoLatLng latlng, LatLngBoundsGM boundsLatlng, Rectangle boundsPixel){
		double razaoWidth = boundsPixel.width/boundsLatlng.width;
		double razaoHeight = boundsPixel.height/boundsLatlng.height;
		int x = boundsPixel.x + (int)((latlng.getLongitude()-boundsLatlng.oeste) * razaoWidth);
		int y = boundsPixel.y + (int)((boundsLatlng.norte-latlng.getLatitude()) * razaoHeight);
		return new Point(x,y);
	}
	
	
	private CrimeService getCrimeService(){
		if(crimeService == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			crimeService = (CrimeService)springContext.getBean("crimeService");
		}
		return crimeService;
	}
	
	public static void testeArquivo(RenderedImage imagem){
		//escrever a imagem em arquivo
		try {
			ImageIO.write(imagem, "PNG", new File("/home/leonardo/img324.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//recupera pontos no wikimapps de acordo com os limites e o tipo de marcador
	private List<Point> recuperaPontosWikiMapps(LatLngBoundsGM limitesLatLng, String url,String tm,int zoom){
		List<Point> pontos=new ArrayList<Point>();
		try {
			String query="select distinct(e.id),c.lt,c.lg from events e, coordinates c, apps a where e.apps_id=a.id and e.id = c.events_id and markers_id is not null and a.url='"+ url +"'";
			if(tm!=null && tm!="" && !tm.equals("undefined")){
				String[] tmArray=tm.split(",");
				for (String valor : tmArray){
				query+=" and e.markers_id!="+valor;
				}
			}
			//retorna todos os pontos dentro da southwest/northeast boundary
			if(limitesLatLng.leste > limitesLatLng.oeste){
				query+=" and (c.lg < " + limitesLatLng.leste + " or c.lg >= " + limitesLatLng.oeste + " ) and ( c.lt <= " + limitesLatLng.norte + " and c.lt >= " + limitesLatLng.sul + ")";
			}
			else {
				//retorna todos os pontos dentro da southwest/northeast boundary
				 //split over the meridian
				query+=" and (c.lg <= " + limitesLatLng.leste + " or c.lg >= " + limitesLatLng.oeste + " ) and ( c.lt <= " + limitesLatLng.norte + " and c.lt >= " + limitesLatLng.sul + ")";
			}
			ConexaoBD con=ConexaoBD.getConexaoBD();
			ResultSet rs =con.enviarConsulta(query);
			while(rs.next()){
				PontoLatLng p = new PontoLatLng(rs.getDouble(2),rs.getDouble(3));
				pontos.add(toPixel(p, zoom));
				
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return pontos;
	}
	
	
}
	