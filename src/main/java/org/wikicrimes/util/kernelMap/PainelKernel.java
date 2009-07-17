package org.wikicrimes.util.kernelMap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * 
 * @author mairon
 *
 */
public class PainelKernel {//extends JPanel implements MouseListener, MouseMotionListener{

	//Numero de camadas de cor
	private int numCamadasCor;
	
	//Tamanho X da matriz
	private int tamX;
	
	//Tamanho Y da matriz
	private int tamY;

	//X e Y minimo
	private int xMin;
	private int yMin;
	
	//X e Y máximos
	int xMax;
	int yMax;
	
	//Largura da banda	
	private int larguraBanda;
	
	//Armazena todos as posicoes X as celulas da matriz
	private ArrayList <Integer> arrayListX = new ArrayList<Integer>();
	
	//Armazena todos as posicoes Y as celulas da matriz
	private ArrayList <Integer> arrayListY = new ArrayList<Integer>();
	
	//Estrutura lógica
	private MyMapKernel myMapKernel;
	
	//
	private boolean imprime = false;
	
	//Matriz de todos as celulas
	public Celula [][] matrizCelulas;
	
	/** Creates a new instance of PainelDesenho */
	public PainelKernel(MyMapKernel myMapKernel) {
		/*
		//Interface
		//Cor de fundo
		this.setBackground(Color.WHITE);

		//Add the MouseListener to this panel
		addMouseListener(this);
		
		//Add the MouseMotionListener to this panel
		addMouseMotionListener(this);
		
		*/
		//Estrutura lógica MyMapKernel 
		this.myMapKernel = myMapKernel;
		matrizCelulas = myMapKernel.getMatrizCelulas();
	}

	private Graphics graphics;
	
	public void paint(Graphics g) {
		
		//super.paint(g);
		
		this.desenhaCelula(g);
//		this.desenhaGrade(g);
		/*if (imprime)
			this.desenhaPonto(g);*/
		
		graphics = g.create();
		
		
//		System.out.println();
//		System.out.println();
//		imprimeCelulas();
	}
	
	public void criaImage(Graphics2D g) {
		this.desenhaCelula(g);
		//this.desenhaPonto(g);

	}

	private void imprimeCelulas() {
		for (int i = 0; i < tamX; i ++){
			for (int j = 0; j < tamY; j ++){
				System.out.println("Celula ["+i+"]" + "[" + j +"]: " + matrizCelulas[i][j].getPosicao());
			}
		}		
	}

	public void calculaLimites() {
		boolean Xpar = true;
		boolean primeira = true;	
		
		for (int x = 0; x < tamX ; x++) {
			
			for (int y = 0 ; y < tamY; y++){
				//System.out.println("Pontos da tabela: " + arrayListX.get(x) + " - " + arrayListY.get(y));
				
				if (Xpar){
					matrizCelulas[x][y].setYCima(arrayListY.get(y));
					matrizCelulas[x][y].setXEsq(arrayListX.get(x));
				}else{
					matrizCelulas[x][y].setYCima(arrayListY.get(y));
					matrizCelulas[x][y].setXEsq(arrayListX.get(x));
				}
				
				//Ultimo caso, analisa ultimo x
				if(x == arrayListX.size() - 1){
					matrizCelulas[x][y].setXDir((arrayListX.get(x) + (Util.tamCelulaPixel * MyMapKernel.incrementoCoordenadaX)));
					matrizCelulas[x][y].setYBaixo((arrayListY.get(y) + (Util.tamCelulaPixel * MyMapKernel.incrementoCoordenadaY)));
					
					//Analisa x anterior ao ultimo
					matrizCelulas[x - 1][y].setXDir(arrayListX.get(x));
					matrizCelulas[x - 1][y].setYBaixo((arrayListY.get(y) + (Util.tamCelulaPixel * MyMapKernel.incrementoCoordenadaY)));
				}else{
					//Só não analisa o primeiro x
					if (x != 0){
						//Coloca a dir e baixo do x anterior
						matrizCelulas[x - 1][y].setXDir(arrayListX.get(x));
						matrizCelulas[x - 1][y].setYBaixo((arrayListY.get(y) + (Util.tamCelulaPixel * MyMapKernel.incrementoCoordenadaY)));
					}						
				}				
			}
			
			if (Xpar){
				Xpar = !Xpar;	
			}else{
				Xpar = !Xpar;
			}		
		}
		
		//this.grid[(arrayListX.size() - 1) -1][(arrayListY.size() - 1) -1].setXDir(arrayListX.size() - 1);
		//this.grid[(arrayListX.size() - 1) -1][(arrayListY.size() - 1) -1].setYBaixo(arrayListY.size() - 1);
	}
	
	public void calculaXeY() {
		if (matrizCelulas != null) {
			for (int i = 0; i < tamX; i++) {
				int valorX = 0;
				valorX = (i * Util.tamCelulaPixel) * MyMapKernel.incrementoCoordenadaX + xMin; 
				arrayListX.add(new Integer(valorX));
			}
			
			for (int i = 0; i < tamY; i++) {
				int valorY = 0;
				valorY = (i * Util.tamCelulaPixel) * MyMapKernel.incrementoCoordenadaY + yMin;
				arrayListY.add(new Integer(valorY));
			}
		}
		
	}

	private void desenhaPonto(Graphics g) {
		g.setColor(Color.BLUE);
		for (Point p : Util.arrayListPontos){
			g.fillOval((int)p.getX(), (int) p.getY(), 3, 3);
		}	
	}

	public void desenhaGrade(Graphics g) {

		g.setColor(Color.BLACK );
		
		if (matrizCelulas != null) {
			for (int i = 0; i < tamX; i++) {
				g.drawLine(i * Util.tamCelulaPixel, 0, i * Util.tamCelulaPixel, tamY	* Util.tamCelulaPixel);
			}
			
			for (int i = 0; i < tamY; i++) {
				g.drawLine(0, i * Util.tamCelulaPixel, tamX * Util.tamCelulaPixel, i * Util.tamCelulaPixel);
			}
		}
		

		
//		for (Integer i : arrayListY) {
//			for (Integer j : arrayListX){
//				System.out.println("Pontos da tabela: " + i + " - " + j);
//			}
//		}
		
		
	}

	public void desenhaCelula(Graphics g) {
		if (matrizCelulas != null){
			for (int i = 0; i < matrizCelulas.length; i++){
				for (int j = 0; j < matrizCelulas[0].length; j++) {
					g.setColor(matrizCelulas[i][j].getCor()); 
					g.fillRect(i * Util.tamCelulaPixel, j * Util.tamCelulaPixel, Util.tamCelulaPixel, Util.tamCelulaPixel);
				}
			}
		}
	}
	
	/*
	//Interface
	
	//Nao usado....
	public void setGrid(Celula[][] grid) {
		matrizCelulas = grid;
		this.setPreferredSize(new Dimension(grid.length * Util.tamCelulaPixel, grid[0].length
				* Util.tamCelulaPixel));
	}

	
	public JPanel returnPanel() {
		return this;
	}

	*/
	public void mouseClicked(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
	
	}

	public void mousePressed(MouseEvent e) {
		//Adiciona um ponto e Pinta o mapa
		myMapKernel.adicionaUmPonto(e.getX(),e.getY());
	}

	public void setLarguraBanda(int larguraBanda) {
		this.larguraBanda = larguraBanda;		
	}
	
	public void mouseDragged(MouseEvent me) {
		mouseMoved(me);		
	}

	public void mouseMoved(MouseEvent me) {
		int posicaoX = (int) me.getPoint().getX();
		int posicaoY = (int) me.getPoint().getY();
		
		//System.out.println(mX + "  " + mY);
		
		myMapKernel.toolTipInformation(posicaoX, posicaoY);
	}	

	public void setNumCamadasCor(int numCamadasCor) {
		this.numCamadasCor = numCamadasCor;
	}

	public int getTamX() {
		return tamX;
	}

	public void setTamX(int tamX) {
		this.tamX = tamX;
	}

	public int getTamY() {
		return tamY;
	}

	public void setTamY(int tamY) {
		this.tamY = tamY;
	}

	public int getXMin() {
		return xMin;
	}

	public void setXMin(int min) {
		xMin = min;
	}

	public int getYMin() {
		return yMin;
	}

	public void setYMin(int min) {
		yMin = min;
	}

	public Graphics getGraphics() {
		return graphics;
	}
}
