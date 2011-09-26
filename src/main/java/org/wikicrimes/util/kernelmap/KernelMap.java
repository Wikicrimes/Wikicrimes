package org.wikicrimes.util.kernelmap;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.wikicrimes.util.rotaSegura.geometria.Ponto;

import org.wikicrimes.util.ValueCompression;

@SuppressWarnings("serial")
public class KernelMap implements Serializable {

	protected final int nodeSize;
	protected final float bandwidth;
	protected final Rectangle bounds;
	
	protected short[][] densityGrid;
	protected float maxDens = Float.NaN;
	protected float avgDens = Float.NaN;
	protected float minDens = Float.NaN;
	
	protected static final int defaultNodeSize = 5;
	protected static final int defaultBandwidth = 20;
	
	/**
	 * @param <b>nodeSize</b> Will determines pixel size.
	 * <br>
	 * @param <b>bandwidth</b> Radius of influence for each point.
	 * The bigger the bandwidth, the variations will be smoother and hotspots will spread out.
	 */
	public KernelMap(int nodeSize, float bandwidth, Rectangle bounds, List<Point> points){
		this.nodeSize = nodeSize;
		this.bandwidth = bandwidth;
		this.bounds = bounds;
		this.densityGrid = evaluateDensities(points);
	}
	
	public KernelMap(Rectangle bounds, List<Point> pontos) {
		this(defaultNodeSize, defaultBandwidth, bounds, pontos);
	}
	
	public KernelMap(short[][] densities, int nodeSize, Rectangle bounds){
		densityGrid = densities;
		this.bounds = bounds;
		this.nodeSize = nodeSize;
		this.bandwidth = Float.NaN;
	}
	
	private short[][] evaluateDensities(List<Point> pontos){
		int cols = getCols();  
		int rows = getRows();
		short[][] densityArray = new short[cols][rows];
		
		//constantes calculadas uma unica vez fora do loop pra poupar operacoes
		int x1 = bounds.x + nodeSize/2; //coordenada X do centro da primeira celula (a do canto superior esquerdo)
		int y1 = bounds.y + nodeSize/2; //coordenada Y do centro da primeira celula (a do canto superior esquerdo)
		float r2 = (float)Math.pow(bandwidth,2); //bandwidth ao quadrado
		float threeOverPi = 3/(float)Math.PI; 
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
						double pInfluence = (threeOverPi/r2) * Math.pow(1-(h2/r2), 2);
						short cell = densityArray[i][j];
						float newValue = ValueCompression.uncompressShort(cell) + (float)pInfluence;
						densityArray[i][j] = ValueCompression.compressShort(newValue);
					}
				}
			}
			
		}
		return densityArray;
	}
	
	protected float findMaxDensity(){
		float maxDens = 0;
		for(short[] linha : densityGrid)
			for(short d : linha)
				maxDens = Math.max(maxDens, ValueCompression.uncompressShort(d));
		return maxDens;
	}
	
	protected float findAvgDensity(){
		float mediaDens = 0;
		for(short[] linha : densityGrid)
			for(short d : linha)
				mediaDens += ValueCompression.uncompressShort(d);
		return mediaDens/(getCols()*getRows());
	}
	
	protected float findMinDensity(){
		float minDens = Integer.MAX_VALUE;
		for(short[] linha : densityGrid)
			for(short d : linha)
				minDens = ValueCompression.uncompressShort(d);
		return minDens;
	}
	
	public short[][] getDensityGrid(){
		return densityGrid;
	}
	
	public float getMaxDens(){
		if(Float.isNaN(maxDens))
			maxDens = findMaxDensity();
		return maxDens;
	}
	
	public float getMediaDens() {
		if(Float.isNaN(avgDens))
			avgDens = findAvgDensity();
		return avgDens;
	}
	
	public float getMinDens(){
		if(Float.isNaN(minDens))
			minDens = findMinDensity();
		return minDens;
	}
	
	public float getDensity(Ponto pixel){
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
	
	public boolean taNoHotspot(Point celula, float threshold){
		if(celula.x < 0 || celula.x >= getCols() 
				|| celula.y < 0 || celula.y >= getRows())
			return false;
		return densityGrid[celula.x][celula.y] >= maxDens*threshold;
	}
	
	public boolean taNoHotspot(Point celula, float threshold, float maxDens){
		if(Float.isNaN(maxDens)) 
			return taNoHotspot(celula, threshold);
		if(celula.x < 0 || celula.x >= getCols() 
				|| celula.y < 0 || celula.y >= getRows())
			return false;
		return densityGrid[celula.x][celula.y] >= maxDens*threshold;
	}
	
	@Override
	public String toString() {
		short[][] dens = getDensityGrid();
		NumberFormat f = new DecimalFormat("0.000");
		
		StringBuilder s = new StringBuilder();
//		s.append("BANDWIDTH: " + bandwidth + "\n");
//		s.append("GRID NODE: " + nodeSize + "\n");
		s.append("DENSIDADE MAXIMA: " + maxDens + "\n");
		s.append("DENSIDADE MEDIA: " + avgDens + "\n");
		s.append("DENSIDADES:");
		for (int i = 0; i < dens.length; i++) {
			for (int j = 0; j < dens[i].length; j++) {
				float d = ValueCompression.uncompressShort(dens[i][j]);
				s.append(f.format(d) + "  ");
			}
			s.append('\n');
		}
		
		return s.toString();
	}
	
}

