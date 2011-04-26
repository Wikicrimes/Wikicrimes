package org.wikicrimes.util.kernelmap;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.wikicrimes.util.rotaSegura.geometria.Ponto;

@SuppressWarnings("serial")
public class KernelMap implements Serializable {

	protected final int nodeSize;
	protected final double bandwidth;
	protected final Rectangle bounds;
	
	protected double[][] densityGrid;
	protected double maxDens = Double.NaN;
	protected double avgDens = Double.NaN;
	protected double minDens = Double.NaN;
	
	protected static final int defaultNodeSize = 5;
	protected static final int defaultBandwidth = 20;
	
	/**
	 * @param <b>nodeSize</b> Will determines pixel size.
	 * <br>
	 * @param <b>bandwidth</b> Radius of influence for each point.
	 * The bigger the bandwidth, the variations will be smoother and hotspots will spread out.
	 */
	public KernelMap(int nodeSize, double bandwidth, Rectangle bounds, List<Point> points){
		this.nodeSize = nodeSize;
		this.bandwidth = bandwidth;
		this.bounds = bounds;
		this.densityGrid = evaluateDensities(points);
	}
	
	public KernelMap(double[][] densities, int nodeSize, Rectangle bounds){
		densityGrid = densities;
		this.bounds = bounds;
		this.nodeSize = nodeSize;
		this.bandwidth = Double.NaN;
	}
	
	public KernelMap(Rectangle bounds, List<Point> pontos) {
		this(defaultNodeSize, defaultBandwidth, bounds, pontos);
	}
	
	private double[][] evaluateDensities(List<Point> pontos){
		int cols = getCols();  
		int rows = getRows();
		double [][] densityArray = new double[cols][rows];
		
		//constantes calculadas uma unica vez fora do loop pra poupar operacoes
		int x1 = bounds.x + nodeSize/2; //coordenada X do centro da primeira celula (a do canto superior esquerdo)
		int y1 = bounds.y + nodeSize/2; //coordenada Y do centro da primeira celula (a do canto superior esquerdo)
		double r2 = Math.pow(bandwidth,2); //bandwidth ao quadrado
		double threeOverPi = 3/Math.PI; 
		int colsMinus1 = cols-1;
		int rowsMinus1 = rows-1;
		
		//itera nos pontos
		for(Point p : pontos){
			int x = p.x - bounds.x;
			int y = p.y - bounds.y;
			//limites para as celulas que sao tocadas pelo circulo de centro P e raio BANDWIDTH
			int iniX = (int)Math.floor((x-bandwidth)/nodeSize); //coluna a esquerda
			int fimX = (int)Math.ceil((x+bandwidth)/nodeSize); //coluna a direita
			int iniY = (int)Math.floor((y-bandwidth)/nodeSize); //linha acima
			int fimY = (int)Math.ceil((y+bandwidth)/nodeSize); //linha abaixo
			if(iniX < 0) iniX = 0;
			if(iniY < 0) iniY = 0;
			if(fimX > colsMinus1) fimX = colsMinus1;
			if(fimY > rowsMinus1) fimY = rowsMinus1;
			
			for(int i=iniX; i<=fimX; i++){
				for(int j=iniY; j<=fimY; j++){
					//ponto central da janela circular (canto superior esquerdo do no)
					Point s = new Point(x1 + i*nodeSize, y1 + j*nodeSize); 
					
					//calclulo da densidade pra um no da grade
					double distPS = p.distance(s);
					if(distPS <= bandwidth){
						double h2 = Math.pow(distPS, 2); // distancia do pt H, ao quadrado
						densityArray[i][j] += (threeOverPi/r2) * Math.pow(1-(h2/r2), 2);
					}
				}
			}
			
		}
		return densityArray;
	}
	
	protected double findMaxDensity(){
		double maxDens = 0;
		for(double[] linha : densityGrid)
			for(double d : linha)
				maxDens = Math.max(maxDens, d);
		return maxDens;
	}
	
	protected double findAvgDensity(){
		double mediaDens = 0;
		for(double[] linha : densityGrid)
			for(double d : linha)
				mediaDens += d;
		return mediaDens/(getCols()*getRows());
	}
	
	protected double findMinDensity(){
		double minDens = Integer.MAX_VALUE;
		for(double[] linha : densityGrid)
			for(double d : linha)
				minDens = Math.min(minDens, d);
		return minDens;
	}
	
	public double[][] getDensityGrid(){
		return densityGrid;
	}
	
	public double getMaxDens(){
		if(Double.isNaN(maxDens))
			maxDens = findMaxDensity();
		return maxDens;
	}
	
	public double getMediaDens() {
		if(Double.isNaN(avgDens))
			avgDens = findAvgDensity();
		return avgDens;
	}
	
	public double getMinDens(){
		if(Double.isNaN(minDens))
			minDens = findMinDensity();
		return minDens;
	}
	
	public double getDensity(Ponto pixel){
		int x = xPixelParaGrid(pixel.x);
		int y = yPixelParaGrid(pixel.y);
		return densityGrid[x][y];
	}

	public Rectangle getBounds(){
		return bounds;
	}
	
	public double getBandwidth() {
		return bandwidth;
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
		//+1 pra preencher a sobra de espaco caso a area nao seja divisivel pelo tamanho do no
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
		return taNoHotspot(celula, 0.5f);
	}
	
	public boolean taNoHotspot(Point celula, double threshold){
		if(celula.x < 0 || celula.x >= getCols() 
				|| celula.y < 0 || celula.y >= getRows())
			return false;
		return densityGrid[celula.x][celula.y] >= maxDens*threshold;
	}
	
	public boolean taNoHotspot(Point celula, double threshold, double maxDens){
		if(Double.isNaN(maxDens)) 
			return taNoHotspot(celula, threshold);
		if(celula.x < 0 || celula.x >= getCols() 
				|| celula.y < 0 || celula.y >= getRows())
			return false;
		return densityGrid[celula.x][celula.y] >= maxDens*threshold;
	}
	
	@Override
	public String toString() {
		double[][] dens = getDensityGrid();
		NumberFormat f = new DecimalFormat("0.000");
		
		StringBuilder s = new StringBuilder();
//		s.append("BANDWIDTH: " + bandwidth + "\n");
//		s.append("GRID NODE: " + nodeSize + "\n");
		s.append("DENSIDADE MÁXIMA: " + maxDens + "\n");
		s.append("DENSIDADE MÉDIA: " + avgDens + "\n");
		s.append("DENSIDADES:");
		for (int i = 0; i < dens.length; i++) {
			for (int j = 0; j < dens[i].length; j++) {
				s.append(f.format(dens[i][j]) + "  ");
			}
			s.append('\n');
		}
		
		return s.toString();
	}
	
	public String booleanGrid() {
		double max = getMaxDens();
		StringBuilder s = new StringBuilder();
		for(double[] col : densityGrid){
			for(double d : col){
				s.append(d > max*0.5? "1" : "0");
			}
			s.append("\n");
		}
		return s.toString();
	}
	
//	public int[][] booleanGrid() {
//		double avg = getMediaDens();
//		int cols = getCols();
//		int rows = getRows();
//		double[][] grid = getDensityGrid();
//		int[][] array = new int[cols][rows];
//		for (int i = 0; i < rows; i++) {
//			array[i] = new int[cols];
//			for (int j = 0; j < cols; j++) {
//				array[j][i] = grid[j][i] > avg? 1 : 0;
//			}
//		}
//		return array;
//	}
	
}

