package org.wikicrimes.util.kernelMap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.wikicrimes.util.NumerosUtil;
import org.wikicrimes.util.rotaSegura.geometria.Poligono;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Segmento;

public class HotspotContour{

	private static final int DETALHAMENTO_POLIGONOS = 0; //qt maior, menos detalhe (mais vertices descartados)
	private final KernelMap kernel;
	private final List<Poligono> poligonos;
	private final float threshold; //varia entre 0 e 1, onde 1 eh a densidade maxima
	
	public HotspotContour(KernelMap kernelMap, double threshold) {
		this.kernel = kernelMap;
		if(threshold < 0 || threshold > 1)
			throw new InvalidParameterException("Threshold must be between 0 and 1. Given: " + threshold); 
		this.threshold = (float)threshold;
		this.poligonos = calcular(kernelMap.getBounds());
	}
	
	public List<Poligono> getPolygons(Rectangle bounds){
		return poligonos;
	}
	
	//FIXME tem um problema
	//em cada passo da caminhada pela borda, n tao sendo processadas só as 3 células como na ilustracao 1 (como foi planejado)
	//ta sendo processado como na ilustracao 2, pq não ta "voltando" pro "x" antes de ir pra cima e pra esquerda (erro de implementação mesmo)  
	//só q, ao corrigir isso, pára de funcionar 
	//pq acontece com mais frequencia de nao achar "celula fora do hotspot" procurando na area menor (ilustracao 1)
	//
	//  1 			     2       < ^
	//        ^		    	   < v < ^
	//      < x >              V > x >
	//                           V > 
	private List<Poligono> calcular(Rectangle bounds){
		
		//obs: tem um flood fill soh pra marcar as celulas dos hotspots como vistas
		//     mas pra encontrar o contorno tem q ser caminhando pela borda
		//     pq o flood nao pega a ordem
		
		List<Poligono> polis = new ArrayList<Poligono>();
		
		//mapa de kernel todo branco
		if(kernel.getMaxDens() == 0)
			return polis;
		
		final int perto = 1; //fecha o poligono msm se o inicio e fim msm nao tiverem se encontrado, se estiverem a esta distância
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
				if(!kernel.taNoHotspot(celula, threshold))
					continue;
				flood(visto, celula);
				
				List<Ponto> contorno = new ArrayList<Ponto>();
				Direcao direcao = Direcao.NORTE;
				Point primeira = celulaVizinha(celula, direcao); //usado na condicao de parada
				int cont = 0;
				
				//caminha pela borda ate fechar um 'ciclo' (voltar pra origem)
				do{
					direcao = direcao.oposta(); //direcao oposta (de onde veio)
					for(int k=0; k<3;k++){
						//primeiro pra direita, depois pra frente, depois pra esquerda 
						direcao = direcao.prox();
						celula = celulaVizinha(celula, direcao);
						if(!kernel.taNoHotspot(celula, threshold)){
							Ponto novoPonto = kernel.gridParaPixel(celula); 
							if(contorno.size() >= 2){ //descartar pontos alinhados
								Ponto penultimoPonto = contorno.get(contorno.size()-2);
								Ponto ultimoPonto = contorno.get(contorno.size()-1);
								if(Ponto.estaoAlinhados(penultimoPonto, ultimoPonto, novoPonto)){
									contorno.remove(contorno.size()-1);
								}
							}
							contorno.add(novoPonto);
							break;
						}
					}
					
					cont++;
					if(cont>200){ 
						
						
//						/*DEBUG*/TesteRotasImg teste = new TesteRotasImg(kernel);
//						/*DEBUG*/teste.setTitulo("falha getPoligonosHotspots()");
//						/*DEBUG*/teste.addPoligono(new Poligono(contorno), Color.RED);
//						/*DEBUG*/teste.salvar();
						
						//acontece de entrar em loop infinito e encontrar um contorno nada a ver
						System.err.println("***falha na classe LogicaRotaSegura, metodo getPoligonosHotspots()");
						continue forCelula;
					}
					
				}while(celula.x - primeira.x > perto || celula.y - primeira.y > perto || cont < minimoIteracoes );
				
				
				/*TESTE reduzir a qtdade de vertices*/
//				final int num = 5;
//				if(contorno.size() > num){
//					int passo = (int)Math.floor((float)contorno.size()/num);
					int passo = 1+DETALHAMENTO_POLIGONOS;
					List<Ponto> escolhidos = new ArrayList<Ponto>();
					for(int k=0; k<contorno.size(); k+=passo)
						escolhidos.add(contorno.get(k));
					contorno = escolhidos;	
//				}
				/*TESTE*/
				
				if(contorno.size() > 2)
					polis.add(new Poligono(contorno));
			}
		}
		return polis;
	}
	
	private void flood(boolean[][] visto, Point celula){
		if(celula.x >= visto.length || celula.y >= visto[0].length || celula.x < 0 || celula.y < 0)
			return;
		if(visto[celula.x][celula.y] || !kernel.taNoHotspot(celula, threshold))
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

}
