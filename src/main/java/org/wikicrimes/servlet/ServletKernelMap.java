package org.wikicrimes.servlet;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.KernelMapRenderer;
import org.wikicrimes.util.kernelMap.Ponto;
import org.wikicrimes.util.kernelMap.testes.TesteCenariosRotas;


/**
 * Trata requisições HTTP para calcular mapa de kernel e gerar imagem.
 * 
 * @author victor
 */
public class ServletKernelMap extends HttpServlet {
	
	final static String IMAGE_PATH = "IMAGE_PATH"; //endereço da imagem de mapa de kernel
	final static String IMAGEM_KERNEL = "IMAGEM_KERNEL"; //endereço da imagem de mapa de kernel
	
	public final static int GRID_NODE = 5;
	public final static int BANDWIDTH = 30;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String imagem = request.getParameter("imagem");//só a requisição pra deletar imagens tem este parâmetro
		if(imagem == null){
			//requisição pra calcular o mapa de kernel e criar imagens
			Rectangle bounds = getLimitesPixel(request);
			gerarKernelMap(bounds, request);
			escreverResposta(request,response);
//			/*teste: sem salvar arquivo*/escreverResposta2(request, response);
		}else{
			//requisição pra deletar as imagens
			KernelImageFilesManager.deletaImagens(request.getSession());
//			/*teste: sem salvar arquivo*/enviarImagem(request, response);
		}
	}
	
	public KernelMap gerarKernelMap(Rectangle bounds, HttpServletRequest request) throws ServletException, IOException {
		///*teste*/long ini = System.currentTimeMillis();
		HttpSession sessao = request.getSession();
		
		//extrai informação dos parâmetros de requisição
		List<Point> pontos = getPoints(request);
		
		//calcula as densidades
		KernelMap kernel = new KernelMap(GRID_NODE, BANDWIDTH, bounds, pontos);
//		/*teste*/System.out.println(kernel);
		/*teste*/TesteCenariosRotas.setResult("densMedia", kernel.getMediaDens());
		/*teste*/TesteCenariosRotas.setResult("densMax", kernel.getMaiorDensidade());
		
		//renderiza a imagem e salva em arquivo
		String imagePath = KernelImageFilesManager.criarImagem(kernel, sessao);
		sessao.setAttribute(IMAGE_PATH, imagePath);
		/*teste: sem salvar arquivo
		KernelMapRenderer kRend = new KernelMapRenderer(kernel);
		RenderedImage imagem = (RenderedImage)kRend.pintaKernel();
		sessao.setAttribute("IMAGEM_KERNEL", imagem);
		 */
		
		///*teste*/long fim = System.currentTimeMillis();
		///*teste*/System.out.println("TEMPO da geração do mapa de kernel: " + (fim-ini));
		
		return kernel;
	}
	
	private void escreverResposta(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//escreve a resposta da requisição
		String imagePath = (String)request.getSession().getAttribute(IMAGE_PATH);
		Rectangle bounds = getLimitesPixel(request);
		
		PrintWriter out = response.getWriter();
		out.print(bounds.x + "," + bounds.y + "," +  	 //bounds
				(bounds.x + bounds.width) + "," + (bounds.y + bounds.height) + "\n" +
				imagePath + "\n"); //caminho da imagem do mapa de kernel
		out.close();
	}
	
	/*teste: sem salvar arquivo*/
	private void escreverResposta2(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//escreve a resposta da requisição
		Rectangle bounds = getLimitesPixel(request);
		
		PrintWriter out = response.getWriter();
		out.print(bounds.x + "," + bounds.y + "," +  	 //bounds
				(bounds.x + bounds.width) + "," + (bounds.y + bounds.height) + "\n");
		out.close();
	}
	private void enviarImagem(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//manda imagem gerada pelo KernelMapRenderer
		response.setContentType("image/jpg");
		OutputStream out = response.getOutputStream();
		HttpSession sessao = request.getSession();
		RenderedImage imagem = (RenderedImage)sessao.getAttribute(IMAGEM_KERNEL);
		ImageIO.write(imagem, "JPG", out);
	}
	/*fim teste: sem salvar arquivo*/
	
//	private void escreverResposta2(HttpServletResponse response, HttpServletRequest request) throws IOException{
//		Rectangle bounds = getLimitesPixel(request);
//		List<Point> pontos = getPoints(request);
//		
//		KernelMapCalc kCalc = new KernelMapCalc(5, 30);
//		double[][] dens = kCalc.calcDensidade(pontos, bounds);
//		KernelMapRenderer kRend = new KernelMapRenderer(bounds, kCalc);
//		RenderedImage image = (RenderedImage)kRend.pintaKernel(dens);
//		
//		//escreve a resposta da requisição
//		response.setContentType("image/jpeg");
//		OutputStream out = response.getOutputStream();
//		ImageIO.write(image, "JPG", out);
//		out.flush();
//	}
	
//	private List<Ponto> getPontos(ServletRequest request){
//		String pontosStr = request.getParameter("pontoXY");
//		List<Ponto> pontos = new LinkedList<Ponto>();
//		for(String pontoStr : pontosStr.split("a")){
//			pontos.add(new Ponto(pontoStr).invertido());  //FIXME invertida pq em algum momento posterior isto está sendo invertido novamente
//		}
//		return pontos;
//	}
	
	public static List<Point> getPoints(ServletRequest request){
		String pontosStr = request.getParameter("pontoXY");
		return getPoints(pontosStr);
	}
	
	public static List<Point> getPoints(String pontosStr){
		List<Point> pontos = new LinkedList<Point>();
		if(pontosStr.isEmpty())
			return pontos;
		for(String pontoStr : pontosStr.split("a")){
			pontos.add(new Ponto(pontoStr).invertido());  //FIXME desfazendo a inversão q foi feita lá no JavaScript
		}
		return pontos;
	}
	
	public static Rectangle getLimitesPixel(ServletRequest request){
		try{
			int north = Integer.parseInt(request.getParameter("northPixel"));
			int south = Integer.parseInt(request.getParameter("southPixel"));
			int east = Integer.parseInt(request.getParameter("eastPixel"));
			int west = Integer.parseInt(request.getParameter("westPixel"));
			return new Rectangle(west, north, east-west, south-north);
		}catch(NumberFormatException e){
			throw new AssertionError("parametro faltando no getLimitesPixel");
		}
	}
	
//	public static Rectangle getLimitesLatLng(ServletRequest request){
//		double north = Double.parseDouble(request.getParameter("north"));
//		double south = Double.parseDouble(request.getParameter("south"));
//		double east = Double.parseDouble(request.getParameter("east"));
//		double west = Double.parseDouble(request.getParameter("west"));
//		return new Rectangle(west, north, east-west, south-north);
//	}
	
}
	