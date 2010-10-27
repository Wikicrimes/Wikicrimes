package org.wikicrimes.util.kernelMap;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.wikicrimes.util.rotaSegura.geometria.Ponto;

public class KernelMap {

	private final int nodeSize;
	private final double bandwidth;
	private final Rectangle bounds;
	
	private double[][] densidade;
	private double maxDens = Double.NaN;
	private double mediaDens = Double.NaN;
	private double minDens = Double.NaN;
	
	/**
	 * @param <b>gridNode</b> 
	 * Tamanho do no da grade.
	 * Qt menor o gridNode, mais fina sera a grade e maior serÃ¡ a resolucao do Mapa de Kernel
	 * <br>
	 * @param <b>bandwidth</b> 
	 * Distancia maxima entre ponto central S e os eventos vizinhos que contribuirao 
	 * no calculo da densidade de S. 
	 * Eh o raio da 'janela' onde eh feita a varredura por eventos vizinhos.
	 * Qt maior o bandwidth, mais 'suave' serÃ¡ o Mapa de Kernel.
	 */
	public KernelMap(int nodeSize, double bandwidth, Rectangle bounds, List<Point> pontos){
		this.nodeSize = nodeSize;
		this.bandwidth = bandwidth;
		this.bounds = bounds;
		this.densidade = calcDensidade(pontos);
	}
	
	public KernelMap(double[][] densidades, int nodeSize, Rectangle bounds){
		densidade = densidades;
		this.bounds = bounds;
		this.nodeSize = nodeSize;
		this.bandwidth = Double.NaN;
	}
	
	private double[][] calcDensidade(List<Point> pontos){
		int cols = getCols();  
		int rows = getRows();
		double [][] densidades = new double[cols][rows];
		
		//constantes calculadas uma única vez fora do loop pra poupar operações
		int x1 = bounds.x + nodeSize/2; //coordenada X do centro da primeira célula (a do canto superior esquerdo)
		int y1 = bounds.y + nodeSize/2; //coordenada Y do centro da primeira célula (a do canto superior esquerdo)
		double r2 = Math.pow(bandwidth,2); //bandwidth ao quadrado
		double threeOverPi = 3/Math.PI; 
		int colsMinus1 = cols-1;
		int rowsMinus1 = rows-1;
		
		//itera nos pontos
		for(Point p : pontos){
			int x = p.x - bounds.x;
			int y = p.y - bounds.y;
			//limites para as células que são tocadas pelo círculo de centro P e raio BANDWIDTH
			int iniX = (int)Math.floor((x-bandwidth)/nodeSize); //coluna à esquerda
			int fimX = (int)Math.ceil((x+bandwidth)/nodeSize); //coluna à direita
			int iniY = (int)Math.floor((y-bandwidth)/nodeSize); //linha acima
			int fimY = (int)Math.ceil((y+bandwidth)/nodeSize); //linha abaixo
			if(iniX < 0) iniX = 0;
			if(iniY < 0) iniY = 0;
			if(fimX > colsMinus1) fimX = colsMinus1;
			if(fimY > rowsMinus1) fimY = rowsMinus1;
			
			for(int i=iniX; i<=fimX; i++){
				for(int j=iniY; j<=fimY; j++){
					//ponto central da janela circular (canto superior esquerdo do nï¿½)
					Point s = new Point(x1 + i*nodeSize, y1 + j*nodeSize); 
					
					//calclulo da densidade pra um no da grade
					double distPS = p.distance(s);
					if(distPS <= bandwidth){
						double h2 = Math.pow(distPS, 2); // distancia do pt H, ao quadrado
						densidades[i][j] += (threeOverPi/r2) * Math.pow(1-(h2/r2), 2);
					}
				}
			}
			
		}
		return densidades;
	}
	
	private double encontraMaiorDensidade(){
		double maxDens = 0;
		for(double[] linha : densidade)
			for(double d : linha)
				maxDens = Math.max(maxDens, d);
		return maxDens;
	}
	
	private double encontraMediaDensidade(){
		double mediaDens = 0;
		for(double[] linha : densidade)
			for(double d : linha)
				mediaDens += d;
		return mediaDens/(getCols()*getRows());
	}
	
	private double encontraMenorDensidade(){
		double minDens = Integer.MAX_VALUE;
		for(double[] linha : densidade)
			for(double d : linha)
				minDens = Math.min(minDens, d);
		return minDens;
	}
	
	public double[][] getDensidadeGrid(){
		return densidade;
	}
	
	public double getMaxDens(){
		if(Double.isNaN(maxDens))
			maxDens = encontraMaiorDensidade();
		return maxDens;
	}
	
	public double getMediaDens() {
		if(Double.isNaN(mediaDens))
			mediaDens = encontraMediaDensidade();
		return mediaDens;
	}
	
	public double getMinDens(){
		if(Double.isNaN(minDens))
			minDens = encontraMenorDensidade();
		return minDens;
	}
	
	public double getDensidade(Ponto pixel){
		int x = xPixelParaGrid(pixel.x);
		int y = yPixelParaGrid(pixel.y);
		return densidade[x][y];
	}

	public Rectangle getBounds(){
		return bounds;
	}
	
	public int getNodeSize(){
		return nodeSize;
	}
	
	public int getWidth(){
		return Math.abs(bounds.width);
	}
	
	public int getHeight(){
		return Math.abs(bounds.height);
	}
	
	public int getCols(){
		return getWidth()/nodeSize + 1;
		//+1 pra preencher a sobra de espaÃ§o caso a area nao seja divisivel pelo tamanho do no
		//(caso a divisao tenha resto)
	}
	
	public int getRows(){
		return getHeight()/nodeSize + 1;
	}
	
	public Point pixelParaGrid(Ponto pixel){
		int x = xPixelParaGrid(pixel.x);
		int y = yPixelParaGrid(pixel.y);
		return new Point(x,y);
	}
	
	public int xPixelParaGrid(int xPixel){
		return (int)Math.round((xPixel-bounds.x)/nodeSize);
	}
	
	public int yPixelParaGrid(int yPixel){
		return (int)Math.round((yPixel-bounds.y)/nodeSize);
	}
	
	public Ponto gridParaPixel(Point celula){
		int x = xGridParaPixel(celula.x);
		int y = yGridParaPixel(celula.y);
		return new Ponto(x,y);
	}
	
	public int xGridParaPixel(int xGrid){
		return xGrid*nodeSize + bounds.x;
	}
	
	public int yGridParaPixel(int yGrid){
		return yGrid*nodeSize + bounds.y;
	}
	
	public boolean taNoHotspot(Point celula){
		if(celula.x >= getCols() || celula.y >= getRows()) return false;
		return densidade[celula.x][celula.y] >= maxDens/2;
	}
	
	@Override
	public String toString() {
		double[][] dens = getDensidadeGrid();
		NumberFormat f = new DecimalFormat("0.000");
		
		StringBuilder s = new StringBuilder();
//		s.append("BANDWIDTH: " + bandwidth + "\n");
//		s.append("GRID NODE: " + nodeSize + "\n");
		s.append("DENSIDADE MÁXIMA: " + maxDens + "\n");
		s.append("DENSIDADE MÉDIA: " + mediaDens + "\n");
		s.append("DENSIDADES:");
		for (int i = 0; i < dens.length; i++) {
			for (int j = 0; j < dens[i].length; j++) {
				s.append(f.format(dens[i][j]) + "  ");
			}
			s.append('\n');
		}
		
		return s.toString();
	}
	
}
