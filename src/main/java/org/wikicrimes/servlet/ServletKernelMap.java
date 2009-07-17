package org.wikicrimes.servlet;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.wikicrimes.util.kernelMap.Dados;
import org.wikicrimes.util.kernelMap.MyMapKernel;
import org.wikicrimes.util.kernelMap.ThreadImage;
import org.wikicrimes.util.kernelMap.Transparent;
import org.wikicrimes.util.kernelMap.Util;

public class ServletKernelMap extends HttpServlet {
	
	//Auto incremento da imagem
	public static int idImage = 2;
	public static Semaphore semaphore = new Semaphore(1);
	private static String barra = "/";	

	public void init() throws ServletException {
		idImage = 2;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//executaServlet(request, response);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		executaServlet(request, response);		
	}
	
	

	private void executaServlet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/plain");
		
		//Recupera o realContextPath
		PrintWriter out = response.getWriter();
		HttpSession httpSession = request.getSession();
		ServletContext context = httpSession.getServletContext();
		String realContextPath = context.getRealPath("//");
		realContextPath += barra + "images" + barra + "KernelMap"+ barra; 
		//System.out.println(realContextPath);

		
		String imagemDiretorioAleatorio = request.getParameter("imagem");
		//Verifica se a imagem já chegou no cliente
		if(imagemDiretorioAleatorio != null){
			Thread thread = new ThreadImage(realContextPath + imagemDiretorioAleatorio);
			thread.start();
			return;	
		}	
		
		//String path = request.getRealPath("//");
		
/*		double north = Double.parseDouble(request.getParameter("north"));
		double south = Double.parseDouble(request.getParameter("south"));
		double east = Double.parseDouble(request.getParameter("east"));
		double west = Double.parseDouble(request.getParameter("west"));
*/		
//		double pontoX = Double.parseDouble(request.getParameter("pontoX"));
//		double pontoY = Double.parseDouble(request.getParameter("pontoY"));
		
		int northPixel = Integer.parseInt(request.getParameter("northPixel"));
		int southPixel = Integer.parseInt(request.getParameter("southPixel"));
		int eastPixel = Integer.parseInt(request.getParameter("eastPixel"));
		int westPixel = Integer.parseInt(request.getParameter("westPixel"));
		
		/*long idUsuarioMapaKernel = Integer.parseInt(request.getParameter("idUsuarioMapaKernel"));
		String emailUsuarioMapaKernel = request.getParameter("emailUsuarioMapaKernel");*/
		
		/*int zoomKernel = Integer.parseInt(request.getParameter("zoomKernel"));
		
		int pontoXPixel = Integer.parseInt(request.getParameter("pontoXPixel"));
		int pontoYPixel = Integer.parseInt(request.getParameter("pontoYPixel"));
		
		System.out.println(northPixel + " / " + southPixel + " / " + eastPixel + " / " + westPixel);
		System.out.println("Ponto clicado: " + pontoXPixel + " / " + pontoYPixel);
		
		out.println("Servlet: " + north + " / " + south + " / " + east + " / " + west);*/

			
		
		
		//Wikicrimes
		//mapKernel.getFrame().dispose();
		Util util = new Util();
		util.tamXMatriz = (Integer.parseInt(request.getParameter("width"))/Util.tamCelulaPixel);
		util.tamYMatriz = (Integer.parseInt(request.getParameter("height"))/Util.tamCelulaPixel);		
		//Instancia o algoritmo de desidade de kernel
		MyMapKernel mapKernel = new MyMapKernel(util);
		
//		mapKernel.setLongitudeMin(west); 
//		mapKernel.setLongitudeMax(east); 
//		mapKernel.setLatitudeMin(south);
//		mapKernel.setLatitudeMax(north);
		
		//Passa os limites da tela do wikicrimes
		mapKernel.setXMax(eastPixel);
		mapKernel.setXMin(westPixel); 
		mapKernel.setYMax(southPixel);
		mapKernel.setYMin(northPixel);
		
		
		//Adiciona os crimes no algoritmo do mapa de kernel
		Dados dados = mapKernel.getDados();
		dados.limpaDados();
		String pontoXY = request.getParameter("pontoXY");
		String [] pontos = pontoXY.split("a");

		for (String ponto : pontos){
			if(!ponto.equalsIgnoreCase("")){
				String coordenadas [] = ponto.split(",");
				dados.adicionaPontoWiki(Integer.parseInt(coordenadas[1]), Integer.parseInt(coordenadas[0]));
			}
		}
		
		
		//Inicia o calculo da estimação de densidades de kernel
		mapKernel.inicializaCalculo();
		
		//Cria e mostra a interface grafica
		//mapKernel.mostrar();
		
		//Id únivo para o nome da imagem
		int idImage = 0;
		
		try {
			semaphore.acquire();
		
			 ServletKernelMap.idImage++;
			
			if( ServletKernelMap.idImage == 100){
				 ServletKernelMap.idImage = 1;
			}
			
			idImage = ServletKernelMap.idImage;
			
			semaphore.release();
			
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
			
		//System.out.println("idImage: " + idImage);

		int numRandomico = getNumeroRandomico();
		numRandomico += idImage;
		
		//mapKernel.criaImage(path);
		String fileName = "Img1.png";
		//realContextPath = "c:";
		//realContextPath += "\\images\\KernelMap\\" + idUsuarioMapaKernel + "@" + emailUsuarioMapaKernel;
		realContextPath += numRandomico;
		
		File file = new File(realContextPath);
		deleteDir(file);
		file.mkdirs();	
		
		//Cria a imagem de fundo preto
		//Cria a imagem de fundo preto
		mapKernel.criaImage(Integer.parseInt(request.getParameter("width")),Integer.parseInt(request.getParameter("height")),realContextPath + barra, fileName);
		
		//Retira o background e torna-o transparente
		Image image = Transparent.makeColorTransparent(Toolkit.getDefaultToolkit().getImage(realContextPath + barra +fileName) , Color.black);
		BufferedImage bufferedImage = Transparent.toBufferedImage(image);
		
		try {
			Transparent.writeImageToJPG(new File(realContextPath + barra + "Img" + idImage + ".png"), bufferedImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println("Apagou: " + "\\Img" + (idImage - 1) + ".png  :" + (new File(realContextPath + "\\Img" + (idImage - 1) + ".png")).delete());
		
		//Envia informações resultates para o cliente
		out.println(mapKernel.getTopLeft().x + "," + mapKernel.getTopLeft().y + "\n" + 
				mapKernel.getBottomRight().x + "," + mapKernel.getBottomRight().y + "\n" +
				idImage + "\n" +
				numRandomico + "\n" +
				"NADA");
			
		

		out.close();
		
	}
	
	private int getNumeroRandomico() {		
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(100000000);
	}

	public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    }	
}
