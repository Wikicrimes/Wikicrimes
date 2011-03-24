package org.wikicrimes.util.rotaSegura.logica;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.wikicrimes.util.NumerosUtil;
import org.wikicrimes.util.kernelmap.KernelMap;
import org.wikicrimes.util.kernelmap.PropertiesLoader;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.geometria.Segmento;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaGM;

public class CalculoPerigo {

	private KernelMap kernel;
	
	//PARAMETROS
	static final double FATOR_TOLERANCIA = PropertiesLoader.getDouble("saferoutes.tolerance_factor");
//	/*TESTE CENARIO*/public double FATOR_TOLERANCIA = 0.0; //setado pelo TesteCenarioRotas
	static final double INFLUENCIA_DISTANCIA = PropertiesLoader.getDouble("saferoutes.distance_influence"); //entre 0 e 1
	
	public CalculoPerigo(LogicaRotaSegura logica) {
		this.kernel = logica.getKernel();
	}
	
	
	/**
	 * Avalia se uma rota eh toleravel ou nao, levando em consideracao
	 * o valor de perigo e a tolerancia associados a ela.
	 * 
	 * Definicao:
	 * Sejam	p(r) :- perigo da rota r (ver metodo "perigo(Rota)")
	 * 			t(c) :- tolerancia do cenario c (ver metodo "tolerancia(Rota)")
	 * 
	 * Se r eh uma rota no cenario c (origem e destino de r coincidem com os de c), entao
	 * r eh toleravel se e s� se p(r) eh menor ou igual a t(c)
	 */
	public boolean isToleravel(Rota rota){
		return perigo(rota) <= tolerancia(rota);
	}
	
	/**
	 * Definicao:
	 * Sejam	c :- um cenario composto por origem, destino, area e mapa de kernel
	 * 			t(c) :- tolerancia do cenario c
	 * 			Lmin(c) :- distancia entre a origem e o destino de c
	 * 			Dm(c) :- densidade media no mapa de kernel de c
	 * 			Dmax(c) :- densidade maxima no mapa de kernel de c
	 * 			Dn(c) = Dm(c)/Dmax(c) :- densidade normalizada
	 * 			k :- constante que representa a influencia da distancia no resultado
	 * 			F :- fator de ajuste
	 * 
	 * t(c) = Lmin(c) * (k + Dn(c)) * F
	 */
	public double tolerancia(Rota rota) {
		double distMin = rota.distanciaRetaOD();
		double densMedia = kernel.getMediaDens();
		double densMax = kernel.getMaxDens();
		double ajuste = FATOR_TOLERANCIA;
		double inflDist = INFLUENCIA_DISTANCIA;
		return distMin*(inflDist+densMedia/densMax)*ajuste;
	}
	
	public double perigo(RotaGM rota){
		return rota.getPerigo();
	}
	
	/**
	 * Fun��o que avalia o perigo de uma rota, levando em considera��o
	 * a densidade dos locais por onde ela passa e a distancia total 
	 * percorrida.
	 * 
	 * Definicao:
	 * Sejam	p(s) :- perigo do segmento s (ver metodo "perigo(Segmento)")
	 * 			p(r) :- perigo da rota r
	 *   
	 * p(r) = Somatorio{p(s)} para cada s contido em r
	 */
	public double perigo(Rota rota){
		
		if(rota instanceof RotaGM)
			return perigo((RotaGM)rota);
		
		if(rota.distanciaPercorrida() == 0)
			return 0;
		
		double perigoTotal = 0;
		for(Segmento segm : rota.getSegmentosReta()){
			perigoTotal += perigo(segm); 
		}
		
		return perigoTotal;
	}
	
	/**
	 * Fun��o que avalia o perigo de um segmento de reta, levando em considera��o
	 * a densidade das celulas do mapa de kernel cortadas por ele e seu comprimento.
	 * 
	 * Definicoes:
	 * Sejam	d(c) :- densidade normalizada da celula c (densidade de c / densidade maxima)
	 * 			t(s) :- tamanho do segmento s contido na celula c
	 * 			k :- constante que representa a influencia da distancia no resultado 
	 * 			p(c,s) :- contribuicao da celula c para o perigo do segmento s
	 * 			p(s) :- perigo do segmento s
	 *   
	 * p(c,s) = (k + d(c))*t(s,c)
	 * p(s) = Somatorio{p(ci,s)} para cada ci cortada por s
	 */
	public double perigo(Segmento segm){
		
		Rectangle bounds = kernel.getBounds();
		double[][] dens = kernel.getDensityGrid();
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
		
		//corrigir o caso em q o segm extrapola o mapa de kernel (rota sa�ndo do viewport do wikicrimes)
		//TODO talvez seja melhor impedir q isso aconte�a, ver depois
//		if(xIni < 0) xIni = 0;
//		if(xFim >= kernelCols) xFim = kernelCols-1;
//		if(yIni < 0) yIni = 0;
//		if(yFim >= kernelRows) yFim = kernelRows-1;
		xIni = NumerosUtil.limitar(xIni, 0, kernelCols-1);
		xFim = NumerosUtil.limitar(xFim, 0, kernelCols-1);
		yIni = NumerosUtil.limitar(yIni, 0, kernelRows-1);
		yFim = NumerosUtil.limitar(yFim, 0, kernelRows-1);
		
		//largura e altura do retangulo que contem o segmento
		int segmCols = xFim-xIni+1;
		int segmRows = yFim-yIni+1;
		double razao = (double)segmRows/segmCols;

//		/*TESTE*/List<Rectangle> cels = new ArrayList<Rectangle>();
		
		//somar os valores de perigo (ponderados) de cada c�lula
		double contribuicoesDeDensidade = 0;
		if(xIni==xFim || yIni==yFim){ //passa em todas as celulas do retangulo
			//um dos 2 fors a seguir so roda uma vez 
			for(int i=xIni; i<=xFim; i++){
				for(int j=yIni; j<=yFim; j++){
					Rectangle cel = new Rectangle(bounds.x + i*node, bounds.y + j*node, node, node);
					double peso = tamanhoSegmCortandoQuadrado(segm, cel);
					double d = dens[i][j];
					contribuicoesDeDensidade += d*peso;
//					/*TESTE*/cels.add(cel);
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
//				/*DEBUG*/System.out.println("jIni=" + jIni + ", jFim=" + jFim + ", yIni=" + yIni + ", yFim=" + yFim);
				for(int j=jIni; j<=jFim; j++){
					Rectangle cel = new Rectangle(bounds.x + i*node, bounds.y + j*node, node, node);
					double peso = tamanhoSegmCortandoQuadrado(segm, cel);
					double d = dens[i][j];
					contribuicoesDeDensidade += d*peso;
//					/*TESTE*/cels.add(cel);
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
		double parcelaPerigo = contribuicoesDeDensidade/kernel.getMaxDens();
		double parcelaDistancia = INFLUENCIA_DISTANCIA*segm.comprimento();
		double perigoDoSegmento = parcelaPerigo + parcelaDistancia;
		
		return perigoDoSegmento;
	}
	
	public double perigo(Ponto p){
		Rectangle bounds = kernel.getBounds();
		double[][] dens = kernel.getDensityGrid();
		int node = kernel.getNodeSize();
		
		int x = (p.x-bounds.x)/node;
		int y = (p.y-bounds.y)/node;
		return dens[x][y];
	}
	
	private double tamanhoSegmCortandoQuadrado(Segmento segm, Rectangle celula){
		
		List<Ponto> intersecoes = listaIntersecoes(segm, celula);
		
		if(intersecoes.size() == 1) 
			return 0.0; //o segm toca no canto da c�lula mas n�o entra
		
		double tam = 0.0;
		Ponto ini = segm.getInicio();
		Ponto fim = segm.getFim();
		if(!intersecoes.isEmpty()){
			//tem 2 pontos de interse��o
			
			if(celula.contains(ini) && celula.contains(fim)){
				//o segmento est� completamente contido na c�lula
				tam = segm.comprimento();
			}else{
				
				if(celula.contains(ini) || celula.contains(fim)){
					//um dos limites do segmento est� dentro da c�lula
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
						intersecoes.remove(0); //remove o q t� mais longe do FORA
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
	 * Lista os pontos de interse��o de uma reta em um ret�ngulo.
	 */
	private List<Ponto> listaIntersecoes(Segmento reta, Rectangle retangulo){
		Set<Ponto> intersecoes = new HashSet<Ponto>(); //usando um Set pra descartar os pontos id�nticos (se a reta passar no canto da c�lula, v�o ter 2 id�nticos, ex: NORTE e OESTE, se for no canto superior esquerdo)
		
		double a = reta.coefA();
		double b = reta.coefB();
		if(!Double.isInfinite(a) && !Double.isInfinite(b)){
			double corte;
			//norte
			corte = (retangulo.y - b) / a; //o valor de X do ponto onde a reta corta o limite norte da c�lula (que � uma outra reta)
			if(corte >= retangulo.x && corte <= retangulo.x+retangulo.width)
				intersecoes.add(new Ponto((int)Math.round(corte), retangulo.y));
			//sul
			corte = (retangulo.y+retangulo.height - b) / a;
			if(corte >= retangulo.x && corte <= retangulo.x+retangulo.width)
				intersecoes.add(new Ponto((int)Math.round(corte), retangulo.y+retangulo.height));
			//oeste
			corte = a*retangulo.x + b; //o valor de Y do ponto onde a reta corta o limite oeste da c�lula
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
			throw new AssertionError(intersecoes.size()+" pontos. S� pode ter 0, 1 ou 2 pontos de interse��o");
		}
		/*teste*/
		
		return new ArrayList<Ponto>(intersecoes);
	}
	
}
