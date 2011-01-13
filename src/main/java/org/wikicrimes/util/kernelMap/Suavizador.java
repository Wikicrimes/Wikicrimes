package org.wikicrimes.util.kernelMap;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.wikicrimes.util.kernelMap.renderer.CellBasedRenderer;


public class Suavizador {

	private KernelMap kernel;
	private double DENSIDADE_MAX;// = 0.025; //valor de densidade correspondente ao vermelho mais forte
	
	private final double ZOOM_MIN = 0;
	private final double ZOOM_MAX = 19;
	private final double SUAVIZACAO_MIN = 0.2; //quanto mais próximo de zero, maior é a suavização
	private final double SUAVIZACAO_MAX = 1.8; //quanto mais próximo de 1, menor é a suavização (menos se altera o mapa de kernel)
											   //valores maiores que 1 fazem efeito inverso: enfatiza as densidades maiores mais ainda 
	private final double FATOR_SUAVIZACAO = (SUAVIZACAO_MAX-SUAVIZACAO_MIN)/(ZOOM_MAX-ZOOM_MIN); //usado pra converter de zoom pra suavização
	
//	private final int imageType = BufferedImage.TYPE_4BYTE_ABGR;
	private final int imageType = BufferedImage.TYPE_INT_ARGB_PRE;
	
	public Suavizador(KernelMap kernel){
		this.kernel = kernel;
		this.DENSIDADE_MAX = kernel.getMaxDens();
	}
	
	public Image pintaKernel(CellBasedRenderer scheme){
		
		//pinta no buffer
		Image buffer = new BufferedImage(kernel.getWidth(), kernel.getHeight(), imageType);
		Graphics g = buffer.getGraphics();
		int node = kernel.getNodeSize();
		int cols = kernel.getCols();
		int rows = kernel.getRows();
		for(int i=0; i<cols; i++){
			for(int j=0; j<rows; j++){
				double density = kernel.getDensidadeGrid()[i][j];
				Color cor = scheme.render(density);
				g.setColor(cor);
				g.fillRect(i*node, j*node, node, node);
			}
		}
		
		return buffer;
	}
	
	/**
	 * Faz aparecer mais manchas no zoom alto. 
	 */
	public Image pintaKernelSuavizado(CellBasedRenderer scheme, int zoom){
		
		double suavizacao = zoomToSuavizacao(zoom);
		
		//pinta no buffer
		Image buffer = new BufferedImage(kernel.getWidth(), kernel.getHeight(), imageType);
		Graphics g = buffer.getGraphics();
		int node = kernel.getNodeSize();
		int cols = kernel.getCols();
		int rows = kernel.getRows();
		for(int i=0; i<cols; i++){
			for(int j=0; j<rows; j++){
				double density = kernel.getDensidadeGrid()[i][j];
				double densSuavizada = suavizar(density, suavizacao);
				Color color = scheme.render(densSuavizada);
				g.setColor(color);
				g.fillRect(i*node, j*node, node, node);
			}
		}
		
		return buffer;
	}
	
	private double suavizar(double densidade, double exp){
		return Math.pow(densidade/DENSIDADE_MAX, exp)*DENSIDADE_MAX;
	}
	
	private double zoomToSuavizacao(double zoom){
		return FATOR_SUAVIZACAO * zoom + SUAVIZACAO_MIN;
	}
	
}




