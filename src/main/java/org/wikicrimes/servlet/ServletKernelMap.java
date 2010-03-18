package org.wikicrimes.servlet;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.wikicrimes.util.Util;
import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.KernelMapRenderer;
import org.wikicrimes.util.kernelMap.Ponto;
import org.wikicrimes.util.kernelMap.testes.TesteCenariosRotas;

/**
 * Trata requisições HTTP para calcular mapa de kernel e gerar imagem.
 * 
 * @author victor
 */
@SuppressWarnings("serial")
public class ServletKernelMap extends HttpServlet {
	
	final static String IMAGEM_KERNEL = "IMAGEM_KERNEL"; //imagem renderizada do mapa de kernel
	final static String DENSIDADES = "DENSIDADES"; //matriz de densidades do mapa de kernel
	final static String KERNEL = "KERNEL"; //objeto MapaKernel
	
	public final static int GRID_NODE = 5;
	public final static int BANDWIDTH = 30;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession sessao = request.getSession();
		
//		/*teste*/System.out.println(request.getParameterMap().keySet() + " - pontoXY:" + request.getParameter("pontoXY"));
		
		String acao = request.getParameter("acao");
		if(acao != null)
		if(acao.equals("geraKernel")){
			//calcular o mapa de kernel e criar imagens
			gerarKernelMap(request);
		}else if(acao.equals("pegaImagem")){
			RenderedImage imagem = (RenderedImage)sessao.getAttribute(IMAGEM_KERNEL);
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
	
	private static void gerarKernelMap(HttpServletRequest request) throws ServletException, IOException {
		///*teste*/long ini = System.currentTimeMillis();
		HttpSession sessao = request.getSession();
		Rectangle bounds = getLimitesPixel(request);
		int zoom = getZoom(request);
//		/*teste*/System.out.println("zoom: " + zoom);
		
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
		boolean ie = Util.isClientUsingIE(request); 
		RenderedImage imagem = (RenderedImage)kRend.pintaKernel(zoom, ie);
		sessao.setAttribute(IMAGEM_KERNEL, imagem);
//		/*teste*/KernelImageFilesManager.criarImagem(kernel, request.getSession()); //teste pra ver pq a imagem n aparece as vezes
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
	
	public static Rectangle getLimitesPixel(ServletRequest request){
		//obs: width = east-west não funciona no caso em q a emenda do mapa está aparecendo na tela (a linha entre Japão e EUA) 
		//width seria negativo já q west>east
		//por isso o width e o height estão sendo calculado no javascript, usando as coordenadas do centro da tela
		String northStr = request.getParameter("northPixel");
//		String southStr = request.getParameter("southPixel");
//		String eastStr = request.getParameter("eastPixel");
		String westStr = request.getParameter("westPixel");
		String widthStr = request.getParameter("width");
		String heightStr = request.getParameter("height");
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
	
//	public static Rectangle getLimitesLatLng(ServletRequest request){
//		double north = Double.parseDouble(request.getParameter("north"));
//		double south = Double.parseDouble(request.getParameter("south"));
//		double east = Double.parseDouble(request.getParameter("east"));
//		double west = Double.parseDouble(request.getParameter("west"));
//		return new Rectangle(west, north, east-west, south-north);
//	}
	
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
}
	