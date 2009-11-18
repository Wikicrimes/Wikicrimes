package org.wikicrimes.util.kernelMap;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class KernelMapRenderer {

	KernelMap kernel;
	Image buffer;
	double DENSIDADE_MAX = 0.025; //valor de densidade correspondente ao vermelho mais forte
	
	
	public KernelMapRenderer(KernelMap kernel){
		this.kernel = kernel;
		this.DENSIDADE_MAX = kernel.maxDens;
	}
	
		
	public Image pintaKernel(){
		
		//pinta no buffer
		buffer = new BufferedImage(kernel.getWidth(), kernel.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = buffer.getGraphics();
		int node = kernel.gridNode;
		int cols = kernel.getCols();
		int rows = kernel.getRows();
		for(int i=0; i<cols; i++){
			for(int j=0; j<rows; j++){
				g.setColor(cor2(kernel.densidade[i][j]));
				g.fillRect(i*node, j*node, node, node);
			}
		}
		
		return buffer;

	}
	
	//opaco
	private Color cor(double densidade){
		//vermelho vivo = 1,0,0
		//vermelho mais claro = 1,X,X
		float greenBlue = (float)(1- densidade/DENSIDADE_MAX); //valor entre 0 e 1, inversamente proporcional a densidade
		if(greenBlue<0) greenBlue = 0;
		if(greenBlue>1) greenBlue = 1; //se estrapolar, assume o valor maior
		return new Color(1,greenBlue,greenBlue);
	}
	
	//transparente
	private Color cor2(double densidade){
		//vermelho opaco = 1,0,0,1
		//vermelho mais tansparente = 1,0,0,0.X
		float alpha = (float)(densidade/DENSIDADE_MAX); //valor entre 0 e 1 proporcional a densidade 
		if(alpha<0) alpha = 0;
		if(alpha>1) alpha = 1; //se estrapolar, assume o valor maior
//		return new Color(1,0,0,alpha);
		return new Color(1,0,0,alpha);
	}
	
	//transparente, com conjunto de cores predefinido
	private Color cor3(double densidade){
		double intervalo = DENSIDADE_MAX/CORES.length;  //intervalo entre os níveis de densidade que terao cores diferentes (para dividir a densidade em faixas que terao cores diferentes)
		int faixa = (int)(densidade/intervalo); //em que faixa se encontra esta densidade (o valor da faixa corresponde a uma posicao do vetor de cores)
		if(faixa >= CORES.length) faixa = CORES.length-1; //se estrapolar, assume o valor maior
		return CORES[faixa];
	}
	
	//cores predefinidas (somente para o metodo cor3())
	final Color[] CORES = {	new Color(1,0,0,0.0f), //vermelho mais transparente 
							new Color(1,0,0,0.1f), 
							new Color(1,0,0,0.2f), 
							new Color(1,0,0,0.3f), 
							new Color(1,0,0,0.4f),
							new Color(1,0,0,0.5f),
							new Color(1,0,0,0.6f),
							new Color(1,0,0,0.7f),
							new Color(1,0,0,0.8f),
							new Color(1,0,0,0.9f),
							new Color(1,0,0,1.0f)  //vermelho mais opaco
						};
	
}




