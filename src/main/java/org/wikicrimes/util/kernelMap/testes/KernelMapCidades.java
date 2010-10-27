package org.wikicrimes.util.kernelMap.testes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.servlet.ServletKernelMap;
import org.wikicrimes.util.MapaChaveDupla;
import org.wikicrimes.util.kernelMap.AvaliacaoPerigo;
import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.KernelMapRenderer;
import org.wikicrimes.util.kernelMap.LatLngBoundsGM;

//pro Douglas, 04/08/2010
public class KernelMapCidades extends HttpServlet {
	
	private static final int NODE_SIZE = 5;
	private static final double BANDWIDTH = 30;
	private static final int ZOOM = 13;
	private final String dir = "/home/victor/Desktop/testes/rotas 2010.07.29/teste douglas/";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp){
		
		//teste
		AvaliacaoPerigo a = new AvaliacaoPerigo(getCrimeService());
		Calendar c = Calendar.getInstance();
		c.set(2005, 01, 01);
		a.avaliarCirculo(new PontoLatLng(-3.72647,-38.535748), 1000, c.getTime());
		if(true) return;
		//teste
		
		System.out.println("inicio");
		
		//largura e altura
		PontoLatLng latlngNO = new PontoLatLng(-3.683031,-38.667498);
		PontoLatLng latlngSE = new PontoLatLng(-3.722173,-38.594542);
		Rectangle boundsPixel = fazerBoundsPixel(latlngNO, latlngSE);
		int largura = boundsPixel.width;
		int altura = boundsPixel.height;

		//Fortaleza
		PontoLatLng latlng = new PontoLatLng(-3.683031,-38.667498);
		int telasLargura = 4;
		int telasAltura = 6;
		calcularDensidadesTelaUnica(latlng, largura, altura, telasLargura, telasAltura, "Fortaleza");
		
		//Belo Horizonte
		latlng = new PontoLatLng(-19.74659,-44.129851);
		telasLargura = 5;
		telasAltura = 7;
		calcularDensidadesTelaUnica(latlng, largura, altura, telasLargura, telasAltura, "Belo Horizonte");
		
		//Campina Grande
		latlng = new PontoLatLng(-7.182225,-35.95336);
		telasLargura = 2;
		telasAltura = 3;
		calcularDensidadesTelaUnica(latlng, largura, altura, telasLargura, telasAltura, "Campina Grande");
		
		//Sao Paulo
		latlng = new PontoLatLng(-23.437419,-46.926727);
		telasLargura = 10;
		telasAltura = 9;
		calcularDensidadesTelaUnica(latlng, largura, altura, telasLargura, telasAltura, "Sao Paulo");
		
		//Rio de Janeiro
		latlng = new PontoLatLng(-22.687518,-43.687134);
		telasLargura = 8;
		telasAltura = 10;
		calcularDensidadesTelaUnica(latlng, largura, altura, telasLargura, telasAltura, "Rio de Janeiro");
		
		//Curitiba
		latlng = new PontoLatLng(-25.299338,-49.408264);
		telasLargura = 5;
		telasAltura = 9;
		calcularDensidadesTelaUnica(latlng, largura, altura, telasLargura, telasAltura, "Curitiba");
		
		System.out.println("fim");
	}
	
	public Rectangle fazerBoundsPixel(PontoLatLng latlngNO, PontoLatLng latlngSE){
		Point pixelNO = latlngNO.toPixel(ZOOM);
		Point pixelSE = latlngSE.toPixel(ZOOM);
		return new Rectangle(Math.abs(pixelSE.x-pixelNO.x), Math.abs(pixelSE.y-pixelNO.y));
	}
	
//	public void calcularDensidadesMosaico(PontoLatLng latlngCanto, int larguraTela, int alturaTela, int telasLargura, int telasAltura, String nome){
//		Point pixelCanto = latlngCanto.toPixel(ZOOM); //canto superior esquerdo
//		
//		for(int i=0; i<telasLargura; i++){
//			for(int j=0; j<telasAltura; j++){
//				Point pixelNO = new Point(pixelCanto.x + i*larguraTela, pixelCanto.y + j*alturaTela);
//				Point pixelSE = new Point(pixelNO.x + larguraTela, pixelNO.y + alturaTela); //canto inferior direito
//				Rectangle boundsPixel = new Rectangle(pixelNO.x, pixelNO.y, Math.abs(pixelSE.x-pixelNO.x), Math.abs(pixelSE.y-pixelNO.y));
//				PontoLatLng latlngNO = PontoLatLng.fromPixel(pixelNO, ZOOM);
//				PontoLatLng latlngSE = PontoLatLng.fromPixel(pixelSE, ZOOM);
//				LatLngBoundsGM boundsLatLng = new LatLngBoundsGM(latlngNO, latlngSE, latlngSE.lng-latlngNO.lng, latlngSE.lat-latlngNO.lat);
//				
//				Map<String,Object> params = new HashMap<String, Object>();
//				params.put("norte", boundsLatLng.norte);
//				params.put("sul", boundsLatLng.sul);
//				params.put("leste", boundsLatLng.leste);
//				params.put("oeste", boundsLatLng.oeste);
//				List<BaseObject> crimes = getCrimeService().filter(params);
//				List<Point> crimesPixel = ServletKernelMap.toPixel(crimes, ZOOM);
//				
//				KernelMap kernel = new KernelMap(NODE_SIZE, BANDWIDTH, boundsPixel, crimesPixel);
//				salvaTexto(kernel, nome);
//				/*teste*/salvaImagem(kernel, nome + ", i=" + i + ", j=" + j);
//			}
//		}
//		
//	}
	
	public void calcularDensidadesTelaUnica(PontoLatLng latlngCanto, 
			int larguraTela, int alturaTela, int telasLargura, int telasAltura, String nome){
		
		System.out.print(nome + "\t\t, inicio");
		
		Point pixelCanto = latlngCanto.toPixel(ZOOM); //canto superior esquerdo
		
		int largura = larguraTela*telasLargura;
		int altura = alturaTela*telasAltura;
		
		Point pixelNO = new Point(pixelCanto.x, pixelCanto.y);
		Point pixelSE = new Point(pixelNO.x + largura, pixelNO.y + altura); //canto inferior direito
		Rectangle boundsPixel = new Rectangle(pixelNO.x, pixelNO.y, Math.abs(pixelSE.x-pixelNO.x), Math.abs(pixelSE.y-pixelNO.y));
		PontoLatLng latlngNO = PontoLatLng.fromPixel(pixelNO, ZOOM);
		PontoLatLng latlngSE = PontoLatLng.fromPixel(pixelSE, ZOOM);
		LatLngBoundsGM boundsLatLng = new LatLngBoundsGM(latlngNO, latlngSE, latlngSE.lng-latlngNO.lng, latlngSE.lat-latlngNO.lat);
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("norte", boundsLatLng.norte);
		params.put("sul", boundsLatLng.sul);
		params.put("leste", boundsLatLng.leste);
		params.put("oeste", boundsLatLng.oeste);
		List<BaseObject> crimes = getCrimeService().filter(params);
		List<Point> crimesPixel = ServletKernelMap.toPixel(crimes, ZOOM);
		System.out.print(", criando mapa de crimes para cada céula");
		MapaChaveDupla<Integer, Integer, List<BaseObject>> mapaCrimesDeCadaCelula = mapaCrimesDeCadaCelula(boundsPixel, NODE_SIZE, crimesPixel, crimes);
		System.out.print(", calculando densidades");
		KernelMap kernel = new KernelMap(NODE_SIZE, BANDWIDTH, boundsPixel, crimesPixel);
		
		System.out.print(", serializando");
		salvaSerializado(kernel, mapaCrimesDeCadaCelula, nome);
//		System.out.print(", salvando texto");
//		/*teste*/salvaTexto(kernel, nome);
		System.out.print(", salvando imagem");
		/*teste*/salvaImagem(kernel, nome);
		int tamanhoSetor = alturaTela - alturaTela%kernel.getNodeSize();
		/*teste*/salvaImagemDividida(kernel, tamanhoSetor, largura, altura, nome);
		System.out.print(", ok\n");
	}
	
	private MapaChaveDupla<Integer, Integer, List<BaseObject>> mapaCrimesDeCadaCelula(
			Rectangle boundsPixel, int nodeSize, List<Point> crimesPixel, List<BaseObject> crimes){
		MapaChaveDupla<Integer, Integer, List<BaseObject>> mapa = new MapaChaveDupla<Integer, Integer, List<BaseObject>>();
		for(int i=0; i<crimesPixel.size(); i++){
			Point p = crimesPixel.get(i);
			int xCelula = (p.x - boundsPixel.x)/nodeSize;
			int yCelula = (p.y - boundsPixel.y)/nodeSize;
			
			List<BaseObject> crimesNaCelula = mapa.get(xCelula, yCelula);
			if(crimesNaCelula == null){
				crimesNaCelula = new ArrayList<BaseObject>();
				mapa.put(xCelula, yCelula, crimesNaCelula);
			}
			crimesNaCelula.add(crimes.get(i));
		}
		return mapa;
	}
	
	private void salvaSerializado(KernelMap kernel, MapaChaveDupla<Integer, Integer, List<BaseObject>> mapa, String nome){
		try {
			File file = new File(dir,nome);
			file.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(kernel.getDensidadeGrid());
//			out.writeDouble(kernel.getMediaDens());
//			out.writeDouble(kernel.getMaxDens());
			out.writeObject(mapa);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void salvaTexto(KernelMap kernel, String nome){
		try {
			File file = new File(dir,nome + ".txt");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(kernel.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void salvaImagem(KernelMap kernel, String nome){
		KernelMapRenderer renderer = new KernelMapRenderer(kernel);
		try {
			ImageIO.write((RenderedImage)renderer.pintaKernel(), "PNG" , new File(dir,nome + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void salvaImagemDividida(KernelMap kernel, int tamanhoSetor, int largura, int altura, String nome){
		int nodeSize = kernel.getNodeSize();
		int celsSetor = tamanhoSetor/nodeSize;
		int setoresLargura = kernel.getCols()*nodeSize/tamanhoSetor + 1;
		int setoresAltura = kernel.getRows()*nodeSize/tamanhoSetor + 1;
		Image imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = imagem.getGraphics();
		double[][] gridInteira = kernel.getDensidadeGrid();
		for(int x=0; x<setoresLargura; x++){
			for(int y=0; y<setoresAltura; y++){
				
				//delimita setor
				double[][] gridSetor = new double[tamanhoSetor][tamanhoSetor];
				for(int i=0; i<celsSetor; i++){
					for(int j=0; j<celsSetor; j++){
						int offsetX = x*celsSetor;
						int offsetY = y*celsSetor;
						if(offsetX + i < kernel.getCols() && offsetY + j < kernel.getRows())
							gridSetor[i][j] = gridInteira[offsetX + i][offsetY + j];
					}	
				}
				
				//renderiza
				KernelMap kernelSetor = new KernelMap(gridSetor, kernel.getNodeSize(), new Rectangle(tamanhoSetor, tamanhoSetor)); 
				KernelMapRenderer renderer = new KernelMapRenderer(kernelSetor);
				Image img = renderer.pintaKernel();
				g.drawImage(img, x*tamanhoSetor, y*tamanhoSetor, tamanhoSetor, tamanhoSetor, null);
				g.setColor(Color.BLACK);
				g.drawRect(x*tamanhoSetor, y*tamanhoSetor, tamanhoSetor, tamanhoSetor);
			}	
		}
		
		//salva em arquivo
		try {
			ImageIO.write((RenderedImage)imagem, "PNG" , new File(dir,nome + "#.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private CrimeService getCrimeService(){
		if(crimeService == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			crimeService = (CrimeService)springContext.getBean("crimeService");
		}
		return crimeService;
	}
	private static CrimeService crimeService;
	
}
