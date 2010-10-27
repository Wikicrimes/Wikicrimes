package org.wikicrimes.util.rotaSegura.logica;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.wikicrimes.util.NumerosUtil;
import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.PropertiesLoader;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.geometria.Segmento;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaGM;

public class CalculoPerigo {

	private KernelMap kernel;
	private Double baseTolerancia;
	
	//PARAMETROS
	static final double FATOR_TOLERANCIA = PropertiesLoader.getDouble("fator_tolerancia");
//	/*TESTE CENARIO*/public double FATOR_TOLERANCIA = 0.0; //setado pelo TesteCenarioRotas
	static final double INFLUENCIA_DISTANCIA = PropertiesLoader.getDouble("influencia_dist"); //entre 0 e 1
	
	public CalculoPerigo(LogicaRotaSegura logica) {
		this.kernel = logica.getKernel();
		this.baseTolerancia = (1-INFLUENCIA_DISTANCIA) * kernel.getMediaDens()/kernel.getMaxDens();
	}
	
	public boolean isToleravel(Rota rota){
		double parcelaDistancia = INFLUENCIA_DISTANCIA * rota.distanciaRetaOD(); 
		double tolerancia = (baseTolerancia + parcelaDistancia) * FATOR_TOLERANCIA;
		return perigo(rota) <= tolerancia;
	}

	public double perigo(RotaGM rota){
		return rota.getPerigo();
	}
	
	public double perigo(Rota rota){
		
		if(rota instanceof RotaGM)
			return perigo((RotaGM)rota);
		
		if(rota.distanciaPercorrida() == 0)
			return 0;
		
		double valor = 0;
		for(Segmento segm : rota.getSegmentosReta()){
			valor += perigo(segm); 
		}
		
		return valor;
	}
	
	/**
	 * Soma o valor de perigo de cada célula cortada pelo Segmento.
	 * Cada valor é multiplicado (ponderado) pelo tamanho da parte do segmento que passa em cima da célula
	 */
	public double perigo(Segmento segm){
		
		Rectangle bounds = kernel.getBounds();
		double[][] dens = kernel.getDensidadeGrid();
		int node = kernel.getNodeSize();

		//largura e altura do mapa de kernel
		int kernelCols = kernel.getCols();
		int kernelRows = kernel.getRows();
		
		//limita o intervalo de celulas que participarao do calculo
		int xIni = (int)((segm.getInicio().x - bounds.x) / node); //coluna na matriz de densidades onde se encontra o ponto inicial do segmento
		int xFim = (int)((segm.getFim().x - bounds.x) / node); //coluna do ponto final
		int yIni = (int)((segm.getInicio().y - bounds.y) / node); //linha do ponto inicial
		int yFim = (int)((segm.getFim().y - bounds.y) / node); //linha do ponto final
		//as 4 variaveis acima deleimitam um retangulo que contem o segmento
		//(mais celulas serao descartadas no codigo mais adiante)
		
		//se o fim for maior q o inicio, inverte
		if(xIni > xFim){ int aux=xIni; xIni=xFim; xFim=aux;}
		if(yIni > yFim){ int aux=yIni; yIni=yFim; yFim=aux;}
		
		//corrigir o caso em q o segm extrapola o mapa de kernel (rota saíndo do viewport do wikicrimes)
		//TODO talvez seja melhor impedir q isso aconteça, ver depois
		if(xIni < 0) xIni = 0;
		if(xFim >= kernelCols) xFim = kernelCols-1;
		if(yIni < 0) yIni = 0;
		if(yFim >= kernelRows) yFim = kernelRows-1;
		
		//largura e altura do retangulo que contem o segmento
		int segmCols = xFim-xIni+1;
		int segmRows = yFim-yIni+1;
		double razao = (double)segmRows/segmCols;

		/*TESTE*/List<Rectangle> cels = new ArrayList<Rectangle>();
		
		//somar os valores de perigo (ponderados) de cada célula
		double valor = 0;
		if(xIni==xFim || yIni==yFim){ //passa em todas as celulas do retangulo
			//um dos 2 fors a seguir so roda uma vez 
			for(int i=xIni; i<=xFim; i++){
				for(int j=yIni; j<=yFim; j++){
					Rectangle cel = new Rectangle(bounds.x + i*node, bounds.y + j*node, node, node);
					double peso = tamanhoSegmCortandoQuadrado(segm, cel);
					double d = dens[i][j];
					valor += d*peso;
					/*TESTE*/cels.add(cel);
				}
			}
		}else{ //evita as celulas que estao longe da diagonal do retangulo
			for(int i=xIni; i<=xFim; i++){
				double deslocamento = (i-xIni)*razao;
				double meio = segm.isCrescente()? yIni + deslocamento : yFim - deslocamento;
				int jIni = (int)Math.floor(meio - razao);
				int jFim = (int)Math.ceil(meio + razao);
				jIni = NumerosUtil.limitar(jIni, yIni, yFim);
				jFim = NumerosUtil.limitar(jFim, yIni, yFim);
				for(int j=jIni; j<=jFim; j++){
					Rectangle cel = new Rectangle(bounds.x + i*node, bounds.y + j*node, node, node);
					double peso = tamanhoSegmCortandoQuadrado(segm, cel);
					double d = dens[i][j];
					valor += d*peso;
					/*TESTE*/cels.add(cel);
				}
			}
		}
		
//		/*TESTE*/TesteRotas teste = new TesteRotas(kernel, grafo);
//		/*TESTE*/teste.addRetangulo(new Rectangle(bounds.x+xIni*node, bounds.y+yIni*node, (segmRows+1)*node, (segmCols+1)*node), Color.RED);
//		/*TESTE*/for(Rectangle r : cels)
//		/*TESTE*/	teste.addRetangulo(r, Color.BLUE);
//		/*TESTE*/teste.addSegmento(segm, Color.GREEN);
//		/*TESTE*/teste.refresh();
		
		//dosar a influencia do perigo e da distancia
		double parcelaPerigo = (1-INFLUENCIA_DISTANCIA)*valor/kernel.getMaxDens();
		double parcelaDistancia = INFLUENCIA_DISTANCIA*segm.comprimento();
		valor = parcelaPerigo + parcelaDistancia;
		
		return valor;
	}
	
	public double perigo(Ponto p){
		Rectangle bounds = kernel.getBounds();
		double[][] dens = kernel.getDensidadeGrid();
		int node = kernel.getNodeSize();
		
		int x = (p.x-bounds.x)/node;
		int y = (p.y-bounds.y)/node;
		return dens[x][y];
	}
	
	private double tamanhoSegmCortandoQuadrado(Segmento segm, Rectangle celula){
		
		List<Ponto> intersecoes = listaIntersecoes(segm, celula);
		
		if(intersecoes.size() == 1) 
			return 0.0; //o segm toca no canto da célula mas não entra
		
		double tam = 0.0;
		Ponto ini = segm.getInicio();
		Ponto fim = segm.getFim();
		if(!intersecoes.isEmpty()){
			//tem 2 pontos de interseção
			
			if(celula.contains(ini) && celula.contains(fim)){
				//o segmento está completamente contido na célula
				tam = segm.comprimento();
			}else{
				
				if(celula.contains(ini) || celula.contains(fim)){
					//um dos limites do segmento está dentro da célula
					Ponto dentro = null, fora = null;
					if(celula.contains(ini)){
						dentro = ini;
						fora = fim;
					}else{
						dentro = fim;
						fora = ini;
					}
						
					if(new Segmento(intersecoes.get(0), fora).comprimento() 
							> new Segmento(intersecoes.get(1), fora).comprimento())
						intersecoes.remove(0); //remove o q tá mais longe do FORA
					else
						intersecoes.remove(1);
					
					intersecoes.add(dentro); //coloca o DENTRO no lugar do q foi removido
				}
				
				tam = new Segmento(intersecoes.get(0), intersecoes.get(1)).comprimento();
			
			}
		}
		
		return tam;
	}
	
	/**
	 * Lista os pontos de interseção de uma reta em um retângulo.
	 */
	private List<Ponto> listaIntersecoes(Segmento reta, Rectangle retangulo){
		Set<Ponto> intersecoes = new HashSet<Ponto>(); //usando um Set pra descartar os pontos idênticos (se a reta passar no canto da célula, vão ter 2 idênticos, ex: NORTE e OESTE, se for no canto superior esquerdo)
		
		double a = reta.coefA();
		double b = reta.coefB();
		if(!Double.isInfinite(a) && !Double.isInfinite(b)){
			double corte;
			//norte
			corte = (retangulo.y - b) / a; //o valor de X do ponto onde a reta corta o limite norte da célula (que é uma outra reta)
			if(corte >= retangulo.x && corte <= retangulo.x+retangulo.width)
				intersecoes.add(new Ponto((int)Math.round(corte), retangulo.y));
			//sul
			corte = (retangulo.y+retangulo.height - b) / a;
			if(corte >= retangulo.x && corte <= retangulo.x+retangulo.width)
				intersecoes.add(new Ponto((int)Math.round(corte), retangulo.y+retangulo.height));
			//oeste
			corte = a*retangulo.x + b; //o valor de Y do ponto onde a reta corta o limite oeste da célula
			if(corte >= retangulo.y && corte <= retangulo.y+retangulo.height)
				intersecoes.add(new Ponto(retangulo.x, (int)Math.round(corte)));
			//leste
			corte = a*(retangulo.x+retangulo.width) + b;
			if(corte >= retangulo.y && corte <= retangulo.y+retangulo.height)
				intersecoes.add(new Ponto(retangulo.x+retangulo.width, (int)Math.round(corte)));
		}else{
			int x = reta.getInicio().x;
			intersecoes.add(new Ponto(x, retangulo.y));
			intersecoes.add(new Ponto(x, retangulo.y+retangulo.height));
		}
		
		/*teste*/
		if(intersecoes.size()>2){
			throw new AssertionError(intersecoes.size()+" pontos. Só pode ter 0, 1 ou 2 pontos de interseção");
		}
		/*teste*/
		
		return new ArrayList<Ponto>(intersecoes);
	}
	
}
