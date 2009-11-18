package org.wikicrimes.util.kernelMap;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.wikicrimes.util.kernelMap.testes.TesteCenariosRotas;


public class KernelMap {

	final int gridNode;
	final double bandwidth;
	final Rectangle bounds;
	
	double[][] densidade;
	double maxDens;
	double mediaDens;
	
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
	public KernelMap(int gridNode, double bandwidth, Rectangle bounds, List<Point> pontos){
		this.gridNode = gridNode;
		this.bandwidth = bandwidth;
		this.bounds = bounds;
		this.densidade = calcDensidade(pontos);
		this.maxDens = encontraMaiorDensidade();
		this.mediaDens = encontraMediaDensidade();
	}
	
	private double[][] calcDensidade(List<Point> pontos){
		int cols = getCols();  
		int rows = getRows();
		double [][] densidades = new double[cols][rows];
		
		//itera nos pontos
		for(Point p : pontos){
			int x = p.x - bounds.x;
			int y = p.y - bounds.y;
			//limites para as células que são tocadas pelo círculo de centro P e raio BANDWIDTH
			int iniX = (int)Math.floor((x-bandwidth)/gridNode); //coluna à esquerda
			int fimX = (int)Math.ceil((x+bandwidth)/gridNode); //coluna à direita
			int iniY = (int)Math.floor((y-bandwidth)/gridNode); //linha acima
			int fimY = (int)Math.ceil((y+bandwidth)/gridNode); //linha abaixo
			if(iniX < 0) iniX = 0;
			if(iniY < 0) iniY = 0;
			if(fimX > cols-1) fimX = cols-1;
			if(fimY > rows-1) fimY = rows-1;
			
			for(int i=iniX; i<=fimX; i++){
				for(int j=iniY; j<=fimY; j++){
					//ponto central da janela circular (canto superior esquerdo do nï¿½)
					Point s = new Point(bounds.x + i*gridNode, bounds.y + j*gridNode); 
					
					//calclulo da densidade pra um no da grade
					if(p.distance(s) <= bandwidth){
						double h2 = Math.pow(p.distance(s), 2); // distancia do pt H, ao quadrado
						double r2 = Math.pow(bandwidth,2); // raio ao quadrado
						densidades[i][j] += 3/(Math.PI*r2) * Math.pow(1-(h2/r2), 2);
					}
				}
			}
			
		}
		return densidades;
	}
	
	private double encontraMaiorDensidade(){
		double maxDens = 0;
		for(double[] linha :densidade)
			for(double d : linha)
				maxDens = Math.max(maxDens, d);
		return maxDens;
	}
	
	private double encontraMediaDensidade(){
		double mediaDens = 0;
		for(double[] linha :densidade)
			for(double d : linha)
				mediaDens += d;
		return mediaDens/(getCols()*getRows());
	}
	
	public double[][] getDensidadeGrid(){
		return densidade;
	}
	
	public double getMaiorDensidade(){
		return maxDens;
	}
	
	public double getMediaDens() {
		return mediaDens;
	}

	public Rectangle getBounds(){
		return bounds;
	}
	
	public int getWidth(){
		return Math.abs(bounds.width);
	}
	
	public int getHeight(){
		return Math.abs(bounds.height);
	}
	
	public int getCols(){
		return getWidth()/gridNode + 1;
		//+1 pra preencher a sobra de espaÃ§o caso a area nao seja divisivel pelo tamanho do no
		//(caso a divisao tenha resto)
	}
	
	public int getRows(){
		return getHeight()/gridNode + 1;
	}
	
	@Override
	public String toString() {
		double[][] dens = getDensidadeGrid();
		NumberFormat f = new DecimalFormat("0.000");
		
		StringBuilder s = new StringBuilder();
		s.append("DENSIDADES:");
		for (int i = 0; i < dens.length; i++) {
			for (int j = 0; j < dens[i].length; j++) {
				s.append(f.format(dens[i][j]) + "  ");
			}
			s.append('\n');
		}
		s.append("BANDWIDTH: " + bandwidth + "\n");
		s.append("GRID NODE: " + gridNode + "\n");
		s.append("DENSIDADE MÁXIMA: " + maxDens + "\n");
		s.append("DENSIDADE MÉDIA: " + mediaDens + "\n");
		
		return s.toString();
	}
	
}
