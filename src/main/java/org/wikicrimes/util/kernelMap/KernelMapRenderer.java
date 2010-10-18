package org.wikicrimes.util.kernelMap;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;


public class KernelMapRenderer {

	private KernelMap kernel;
	private Image buffer;
	private double DENSIDADE_MAX;// = 0.025; //valor de densidade correspondente ao vermelho mais forte
	
	private final double ZOOM_MIN = 0;
	private final double ZOOM_MAX = 19;
	private final double SUAVIZACAO_MIN = 0.2; //quanto mais próximo de zero, maior é a suavização
	private final double SUAVIZACAO_MAX = 1.8; //quanto mais próximo de 1, menor é a suavização (menos se altera o mapa de kernel)
											   //valores maiores que 1 fazem efeito inverso: enfatiza as densidades maiores mais ainda 
	private final double FATOR_SUAVIZACAO = (SUAVIZACAO_MAX-SUAVIZACAO_MIN)/(ZOOM_MAX-ZOOM_MIN); //usado pra converter de zoom pra suavização
	
//	private final int imageType = BufferedImage.TYPE_4BYTE_ABGR;
	private final int imageType = BufferedImage.TYPE_INT_ARGB_PRE;
	
	public KernelMapRenderer(KernelMap kernel){
		this.kernel = kernel;
		this.DENSIDADE_MAX = kernel.getMaxDens();
	}
	
	/**
	 * Renderiza o mapa de kernel sem suavização.
	 */
	public Image pintaKernel(){
		return pintaKernelTransparente(1);
	}
	
	/**
	 * Renderiza o mapa de kernel com suavização.
	 */
	public Image pintaKernel(int zoom){
		return pintaKernelTransparente(zoomToSuavizacao(zoom));
	}
	
	/**
	 * Renderiza o mapa de kernel com suavização.
	 */
	public Image pintaKernel(int zoom, Color cor){
		return pintaKernelTransparente(zoomToSuavizacao(zoom), cor);
	}
	
	/**
	 * Renderiza o mapa de kernel com suavização.
	 * Pode ser opaco (necessário no IE q não suporta a transparência) 
	 */
	public Image pintaKernel(int zoom, boolean opaco){
		if(opaco)
			return pintaKernelOpaco(zoomToSuavizacao(zoom));
		else
			return pintaKernel(zoom);
	}
	
	/**
	 * Renderiza o mapa de kernel com suavização.
	 * Pode ser opaco (necessário no IE q não suporta a transparência) 
	 */
	public Image pintaKernel(int zoom, boolean opaco, Color cor){
		if(opaco)
			return pintaKernelOpaco(zoomToSuavizacao(zoom));
		else
			return pintaKernel(zoom,cor);
	}
	
	/**
	 * Renderiza o mapa de kernel "suavizado" enfatizando as densidades menores 
	 * @param suavAmount: valores próximos de 0 suavizam mais, 1 não altera nada, maior que 1 dá efeito inverso
	 */
	private Image pintaKernelTransparente(double suavAmount){
		//pinta no buffer
		buffer = new BufferedImage(kernel.getWidth(), kernel.getHeight(), imageType);
		Graphics g = buffer.getGraphics();
		int node = kernel.getNodeSize();
		int cols = kernel.getCols();
		int rows = kernel.getRows();
		for(int i=0; i<cols; i++){
			for(int j=0; j<rows; j++){
				double densSuavizada = suavizar(kernel.getDensidadeGrid()[i][j], suavAmount);
				g.setColor(cor2(densSuavizada));
//				g.setColor(cor4(kernel.getDensidadeGrid()[i][j]));
				g.fillRect(i*node, j*node, node, node);
			}
		}
		
		return buffer;
	}
	
	/**
	 * Renderiza o mapa de kernel "suavizado" enfatizando as densidades menores 
	 * @param suavAmount: valores próximos de 0 suavizam mais, 1 não altera nada, maior que 1 dá efeito inverso
	 */
	private Image pintaKernelTransparente(double suavAmount, Color cor){
		//pinta no buffer
		buffer = new BufferedImage(kernel.getWidth(), kernel.getHeight(), imageType);
		Graphics g = buffer.getGraphics();
		int node = kernel.getNodeSize();
		int cols = kernel.getCols();
		int rows = kernel.getRows();
		for(int i=0; i<cols; i++){
			for(int j=0; j<rows; j++){
				double densSuavizada = suavizar(kernel.getDensidadeGrid()[i][j], suavAmount);
				g.setColor(cor2(densSuavizada, cor));
				g.fillRect(i*node, j*node, node, node);
			}
		}
		
		return buffer;
	}
	
	/**
	 * Renderiza o mapa de kernel "suavizado" enfatizando as densidades menores 
	 * @param suavAmount: valores próximos de 0 suavizam mais, 1 não altera nada, maior que 1 dá efeito inverso
	 */
	private Image pintaKernelOpaco(double suavAmount){
		//pinta no buffer
		buffer = new BufferedImage(kernel.getWidth(), kernel.getHeight(), imageType);
		Graphics g = buffer.getGraphics();
		int node = kernel.getNodeSize();
		int cols = kernel.getCols();
		int rows = kernel.getRows();
		for(int i=0; i<cols; i++){
			for(int j=0; j<rows; j++){
				double densSuavizada = suavizar(kernel.getDensidadeGrid()[i][j], suavAmount);
				g.setColor(cor1(densSuavizada));
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
	
	//opaco
	//de branco pra vermelho
	private Color cor(double densidade){
		//vermelho vivo = 1,0,0
		//vermelho mais claro = 1,X,X
		float greenBlue = (float)(1- densidade/DENSIDADE_MAX); //valor entre 0 e 1, inversamente proporcional a densidade
		if(greenBlue<0) greenBlue = 0;
		if(greenBlue>1) greenBlue = 1; //se extrapolar, assume o valor maior
		return new Color(1,greenBlue,greenBlue);
	}
	
	//opaco, sem cor na densidade zero
	//de branco pra vermelho
	private Color cor1(double densidade){
		if(densidade == 0)
			return new Color(1,1,1,0);
		//vermelho vivo = 1,0,0
		//vermelho mais claro = 1,X,X
		float greenBlue = (float)(1- densidade/DENSIDADE_MAX); //valor entre 0 e 1, inversamente proporcional a densidade
		if(greenBlue<0) greenBlue = 0;
		if(greenBlue>1) greenBlue = 1; //se extrapolar, assume o valor maior
		return new Color(1,greenBlue,greenBlue);
	}
	
	//transparente
	//de transparente pra vermelho
	private Color cor2(double densidade){
		//vermelho opaco = 1,0,0,1
		//vermelho mais tansparente = 1,0,0,0.X
		float alpha = (float)(densidade/DENSIDADE_MAX); //valor entre 0 e 1 proporcional a densidade 
		if(alpha<0) alpha = 0;
		if(alpha>1) alpha = 1; //se extrapolar, assume o valor maior
		return new Color(1,0,0,alpha);
	}
	
	//transparente
	//de transparente pra cor do parametro
	private Color cor2(double densidade, Color cor){
		float alpha = (float)(densidade/DENSIDADE_MAX);
		if(alpha<0) alpha = 0;
		if(alpha>1) alpha = 1;
		return new Color(cor.getRed(),cor.getGreen(),cor.getBlue(),(int)(alpha*255));
	}
	
	//transparente, com conjunto de cores predefinido
	private Color cor3(double densidade){
		double intervalo = DENSIDADE_MAX/CORES6TR.length;  //intervalo entre os nÃ­veis de densidade que terao cores diferentes (para dividir a densidade em faixas que terao cores diferentes)
		int faixa = (int)(densidade/intervalo); //em que faixa se encontra esta densidade (o valor da faixa corresponde a uma posicao do vetor de cores)
		if(faixa >= CORES6TR.length) faixa = CORES6TR.length-1; //se extrapolar, assume o valor maior
		return CORES6TR[faixa];
	}
	
	//transparente, colorido, tipo visao de calor
	//de azul, passando por verde, amarelo até vermelho
	private Color cor4(double densidade){
//		if(densidade == 0)
//			return new Color(1,1,1,0);
		final float alpha = .3f;
		double quarto = DENSIDADE_MAX/4;
		if(densidade <= quarto){
			float g = (float)(densidade/quarto);
			return new Color(0,g,1,alpha);
		}else if(densidade <= 2*quarto){
			densidade -= quarto;
			float b = (float)(1 - densidade/quarto);
			return new Color(0,1,b,alpha);
		}else if(densidade <= 3*quarto){
			densidade -= 2*quarto;
			float r = (float)(densidade/quarto);
			return new Color(r,1,0,alpha);
		}else{
			densidade -= 3*quarto;
			float g = (float)(1 - densidade/quarto);
			return new Color(1,g,0,alpha);
		}
	}
	
	//cores predefinidas (somente para o metodo cor3())
	//11 cores; transparente -> vermelho
	final static Color[] CORES11 = {	new Color(1,0,0,0.0f), //transparente 
										new Color(1,0,0,.1f), 
										new Color(1,0,0,.2f), 
										new Color(1,0,0,.3f), 
										new Color(1,0,0,.4f),
										new Color(1,0,0,.5f),
										new Color(1,0,0,.6f),
										new Color(1,0,0,.7f),
										new Color(1,0,0,.8f),
										new Color(1,0,0,.9f),
										new Color(1,0,0,1f)  //vermelho opaco
									};
	
	//6 cores; transparente -> vermelho
	final static Color[] CORES6 = {	new Color(1,0,0,0f), //transparente 
									new Color(1,0,0,.2f), 
									new Color(1,0,0,.4f),
									new Color(1,0,0,.6f),
									new Color(1,0,0,.8f),
									new Color(1,0,0,1f)  //vermelho opaco
								};
	
	//6 cores opacas; branco -> vermelho
	final static Color[] CORES6OP = {	new Color(1,1,1,0f), //branco 
										new Color(1,.8f,.8f), 
										new Color(1,.6f,.6f),
										new Color(1,.4f,.4f),
										new Color(1,.2f,.2f),
										new Color(1f,0,0)  //vermelho
									};
	
	//6 cores transparentes; branco -> vermelho
	final static Color[] CORES6TR = {	new Color(1,1,1,0f), //branco 
										new Color(1,.8f,.8f,.8f), 
										new Color(1,.6f,.6f,.8f),
										new Color(1,.4f,.4f,.8f),
										new Color(1,.2f,.2f,.8f),
										new Color(1f,0,0,.8f)  //vermelho
									};
}




