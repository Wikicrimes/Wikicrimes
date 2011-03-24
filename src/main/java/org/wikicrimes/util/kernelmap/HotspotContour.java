package org.wikicrimes.util.kernelmap;

import java.awt.Point;
import java.awt.Rectangle;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.wikicrimes.util.NumerosUtil;
import org.wikicrimes.util.rotaSegura.geometria.Poligono;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;

public class HotspotContour{

	private static final int VERTICES_DESCARTADOS = PropertiesLoader.getInt("hotspot_contour.dropped_vertices"); //qt maior, menos detalhe (mais vertices descartados)
	private static final double MINIMUM_POLYGON_AREA = PropertiesLoader.getInt("hotspot_contour.minimum_polygon_area"); //poligonos com area menor sao descartados
	private static final int MINIMUM_VERTICES= PropertiesLoader.getInt("hotspot_contour.minimum_vertices"); //poligonos com menos vertices sao descartados
	private final KernelMap kernel;
	private List<Poligono> poligonos;
	private final float threshold; //varia entre 0 e 1, onde 1 eh a densidade maxima
	private final double maxDens;
	
	public HotspotContour(KernelMap kernelMap, double threshold) {
		this.kernel = kernelMap;
		if(threshold < 0 || threshold > 1)
			throw new InvalidParameterException("Threshold must be between 0 and 1. Given: " + threshold); 
		this.threshold = (float)threshold;
		this.maxDens = Double.NaN;
	}
	
	public HotspotContour(KernelMap kernelMap, double threshold, double maxDensity) {
		this.kernel = kernelMap;
		if(threshold < 0 || threshold > 1)
			throw new InvalidParameterException("Threshold must be between 0 and 1. Given: " + threshold); 
		this.threshold = (float)threshold;
		this.maxDens = maxDensity;
	}
	
	public List<Poligono> getPolygons(Rectangle bounds){
		if(poligonos == null)
			this.poligonos = calcular(kernel.getBounds(), maxDens);
		return poligonos;
	}
	
	private List<Poligono> calcular(Rectangle bounds, double maxDens){
		
		List<Poligono> polis = new ArrayList<Poligono>();
		
		//mapa de kernel todo branco
		if(kernel.getMaxDens() == 0)
			return polis;
		
		final int minimoIteracoes = 10;
		Ponto p1 = new Ponto(bounds.x, bounds.y);
		Ponto p2 = new Ponto(bounds.x + bounds.width, bounds.y + bounds.height);
		Point g1 = kernel.pixelParaGrid(p1);
		Point g2 = kernel.pixelParaGrid(p2);
		int cols = kernel.getCols();
		int rows = kernel.getRows();
		int xIni = NumerosUtil.limitar(g1.x, 0, cols);
		int xFim = NumerosUtil.limitar(g2.x, 0, cols);
		int yIni = NumerosUtil.limitar(g1.y, 0, rows);
		int yFim = NumerosUtil.limitar(g2.y, 0, rows);

		boolean[][] visto = new boolean[cols][rows];
		for(int i=xIni; i<xFim; i++){
			forCelula:
			for(int j=yIni; j<yFim; j++){
				if(visto[i][j])
					continue;
				Point celula = new Point(i,j);
				if(!kernel.taNoHotspot(celula, threshold, maxDens))
					continue;
				flood(visto, celula);
				
				List<Ponto> contorno = new ArrayList<Ponto>();
				Direcao direcao = Direcao.NORTE;
				celula = celulaVizinha(celula, direcao);
				Point primeira = celula; //usado na condicao de parada
				int cont = 0;
				
				//caminha pela borda ate fechar um 'ciclo' (voltar pra origem)
				do{
					direcao = direcao.oposta(); //direcao oposta (de onde veio)
					Point anterior = celula;
					for(int k=0; k<3;k++){
						//primeiro pra direita, depois pra frente, depois pra esquerda 
						direcao = direcao.prox();
						celula = celulaVizinha(celula, direcao);
						if(!kernel.taNoHotspot(celula, threshold, maxDens)){
							Ponto novoPonto = kernel.gridParaPixel(celula); 
							if(contorno.size() >= 2)
								dropAlignedPoints(contorno, novoPonto);
							contorno.add(novoPonto);
							break;
						}else{
							celula = anterior;
						}
					}
					
					cont++;
					if(cont > 200){ 
						//acontece de entrar em loop infinito e encontrar um contorno nada a ver
						System.err.println("***falha na classe LogicaRotaSegura, metodo getPoligonosHotspots()");
						continue forCelula;
					}
					
				}while(celula.x != primeira.x || celula.y != primeira.y || cont < minimoIteracoes );
				
				contorno = reducePolygonVertices(contorno);	
				
				if(contorno.size() >= MINIMUM_VERTICES){
					Poligono poly = new Poligono(contorno);
					if(isPolygonBigEnough(poly))
						polis.add(poly);
				}
			}
		}
		return polis;
	}
	
	private void flood(boolean[][] visto, Point celula){
		if(celula.x >= visto.length || celula.y >= visto[0].length || celula.x < 0 || celula.y < 0)
			return;
		if(visto[celula.x][celula.y] || !kernel.taNoHotspot(celula, threshold, maxDens))
			return;
		visto[celula.x][celula.y] = true;
		for(Direcao d : Direcao.values())
			flood(visto, celulaVizinha(celula, d));
	}
	
	private enum Direcao{
		NORTE, LESTE, SUL, OESTE;
		
		private Direcao prox(){ 
			//proxima direcao, no sentido horario
			return Direcao.values()[(ordinal()+1)%4];
		}
		
		private Direcao oposta(){
			return Direcao.values()[(ordinal()+2)%4];
		}
	}
	
	private Point celulaVizinha(Point celula, Direcao dir){ 
		//celula vizinha na direcao passada como argumento
		switch(dir){
		case NORTE:
			return new Ponto(celula.x, celula.y-1);
		case SUL:
			return new Ponto(celula.x, celula.y+1);
		case LESTE:
			return new Ponto(celula.x-1, celula.y);
		case OESTE:
			return new Ponto(celula.x+1, celula.y);
		default:
			return null;
		}
	}
	
	private void dropAlignedPoints(List<Ponto> contorno, Ponto novoPonto){
		Ponto penultimoPonto = contorno.get(contorno.size()-2);
		Ponto ultimoPonto = contorno.get(contorno.size()-1);
		if(Ponto.estaoAlinhados(penultimoPonto, ultimoPonto, novoPonto)){
			contorno.remove(contorno.size()-1);
		}
	}

	private List<Ponto> reducePolygonVertices(List<Ponto> contorno){
		int passo = 1 + VERTICES_DESCARTADOS;
		List<Ponto> escolhidos = new ArrayList<Ponto>();
		for(int k=0; k<contorno.size(); k+=passo)
			escolhidos.add(contorno.get(k));
		return escolhidos;
	}
	
	private boolean isPolygonBigEnough(Poligono poly){
		Rectangle bounds = poly.bounds();
		double rectArea = bounds.getWidth()*bounds.getHeight();
		return rectArea >= MINIMUM_POLYGON_AREA;
	}
	
}
