package org.wikicrimes.util.rotaSegura;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.wikicrimes.util.kernelMap.Caminho;
import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.Ponto;
import org.wikicrimes.util.kernelMap.PropertiesLoader;
import org.wikicrimes.util.kernelMap.SegmentoReta;

/**
 * @author victor
 *	Agrupa a lógica para cálculo de rotas seguras. Um objeto LogicaRotaSegura calcula rotas para o MapaKernel associado a ele.
 */
public class LogicaRotaSegura {
	
	private KernelMap kernel;
	private Grafo grafo;
	
	//PARAMETROS
	private static final double FATOR_TOLERANCIA = PropertiesLoader.getDouble("fator_tol");
	private static final double INFLUENCIA_DISTANCIA = PropertiesLoader.getDouble("influencia_dist"); //entre 0 e 1 
	private static final int GRANULARIDADE_ROTAS = PropertiesLoader.getInt("granularidade_rotas");
	private static final int DETALHE_PONTOS_PROMISSORES = PropertiesLoader.getInt("detalhe_pts_promissores");
	
	public LogicaRotaSegura(KernelMap kernel){
		this.kernel = kernel;
	}
	
	public void limpar(){
		grafo = null;
	}
	
	public Grafo getGrafo() {
		return grafo;
	}
	public void setGrafo(Grafo grafoRotas) {
		this.grafo = grafoRotas;
	}
	public KernelMap getKernel() {
		return kernel;
	}
	
	
	/*---------------------------- CALCULO DE PERIGO PARA UMA ROTA ----------------------------*/
	
	
	public double perigo(Caminho rota){
		
		if(rota.distanciaPercorrida() == 0)
			return 0;
		
		double valor = 0;
		for(SegmentoReta segm : rota.getSegmentosReta()){
			valor += perigo(segm); 
		}
		
		return valor;
	}
	
	/**
	 * Soma o valor de perigo de cada célula cortada pelo Segmento.
	 * Cada valor é multiplicado (ponderado) pelo tamanho da parte do segmento que passa em cima da célula
	 */
	public double perigo(SegmentoReta segm){
		
		Rectangle bounds = kernel.getBounds();
		double[][] dens = kernel.getDensidadeGrid();
		int node = kernel.getNodeSize();
		
		//limita o intervalo de celulas que participarao do calculo
		int xIni = (int)((segm.getInicio().x - bounds.x) / node); //coluna na matriz de densidades onde se encontra o ponto inicial do segmento
		int xFim = (int)((segm.getFim().x - bounds.x) / node); //coluna do ponto final
		int yIni = (int)((segm.getInicio().y - bounds.y) / node); //linha do ponto inicial
		int yFim = (int)((segm.getFim().y - bounds.y) / node); //linha do ponto final
		
		//se o fim for maior q o inicio, inverte
		if(xIni > xFim){ int aux=xIni; xIni=xFim; xFim=aux;}
		if(yIni > yFim){ int aux=yIni; yIni=yFim; yFim=aux;}
		
		//corrigir o caso em q o segm extrapola o mapa de kernel (rota saíndo do viewport do wikicrimes)
		//TODO talvez seja melhor impedir q isso aconteça, ver depois
		if(xIni < 0) xIni = 0;
		if(xFim > dens.length-1) xFim = dens.length-1;
		if(yIni < 0) yIni = 0;
		if(yFim > dens[0].length-1) yFim = dens[0].length-1;
		
		//somar os valores de perigo (ponderados) de cada célula
		double valor = 0;
		for(int i=xIni; i<=xFim; i++){
			for(int j=yIni; j<=yFim; j++){
				Rectangle cel = new Rectangle(bounds.x + i*node, bounds.y + j*node, node, node);
				
				/*teste*/
				double peso=0;
				try{
					peso = tamanhoSegmCortandoQuadrado(segm, cel);
				}catch(AssertionError e){
					//TODO ta dando "java.lang.AssertionError: 3 pontos. Soh pode ter 0, 1 ou 2 pontos de intersecao"
					System.out.println("i=" + i + "j=" + j);
					throw e;
				}
				/*teste*/
				//double peso = tamanhoSegmCortandoQuadrado(segm, cel);
				
				double d = dens[i][j];
				valor += d*peso;
			}
		}
		
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
	
	private double tamanhoSegmCortandoQuadrado(SegmentoReta segm, Rectangle celula){
		
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
						
					if(new SegmentoReta(intersecoes.get(0), fora).comprimento() 
							> new SegmentoReta(intersecoes.get(1), fora).comprimento())
						intersecoes.remove(0); //remove o q tá mais longe do FORA
					else
						intersecoes.remove(1);
					
					intersecoes.add(dentro); //coloca o DENTRO no lugar do q foi removido
				}
				
				tam = new SegmentoReta(intersecoes.get(0), intersecoes.get(1)).comprimento();
			
			}
		}
		
		return tam;
	}
	
	/**
	 * Lista os pontos de interseção de uma reta em um retângulo.
	 */
	private List<Ponto> listaIntersecoes(SegmentoReta reta, Rectangle retangulo){
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
	
	public boolean isToleravel(Caminho rota){
//		/*teste*/TesteRotas teste = new TesteRotas(kernel,grafo);
//		/*teste*/teste.setRotas(rota);
//		/*teste*/teste.refresh();
		double parcelaPerigo = (1-INFLUENCIA_DISTANCIA)*kernel.getMediaDens()/kernel.getMaxDens();
		double parcelaDistancia = INFLUENCIA_DISTANCIA*rota.distanciaRetaOD(); 
		double tolerancia = (parcelaPerigo + parcelaDistancia)* FATOR_TOLERANCIA;
		return perigo(rota) <= tolerancia;
	}
	
	/**
	 * Divide um caminho em partes toleráveis e partes não toleráveis.
	 * problema: se vc tem 2 caminhos toleráveis, ao juntar pode dar um caminho nao tolerável, já q a distancia percorrida aumenta
	 */
	public List<Caminho> separarCaminhosToleraveis(Caminho caminho){
		//TODO nao ta prestando pq qd c junta 2 pedaços toleraveis, o resultado pode nao ser
		List<Caminho> caminhos = new LinkedList<Caminho>();
		double tam = caminho.distanciaPercorrida();
		Ponto inicio = caminho.getInicio();
		Ponto fim = inicio;
		Caminho anterior = null;
		boolean eraToleravel = true;
		for(int i=1; i*GRANULARIDADE_ROTAS<=tam; i++){
			double dist = i*GRANULARIDADE_ROTAS;
			fim = caminho.buscarPonto(dist);
			Caminho c = caminho.subCaminho(inicio, fim);
			if(eraToleravel != isToleravel(c)){
				if(anterior != null){ //nao é a primeira iteração
					caminhos.add(anterior);
					inicio = anterior.getFim();
					c = caminho.subCaminho(inicio, fim);
				}
				eraToleravel = !eraToleravel;
			}
			anterior = c;
		}
		
		//ultima vez (o q restou da ultima iteracao até o fim do caminho)
		fim = caminho.getFim();
		Caminho c = caminho.subCaminho(inicio, fim);
		if(eraToleravel != isToleravel(c) && anterior != null){
			caminhos.add(anterior); //parte anterior
			c = caminho.subCaminho(anterior.getFim(), fim); //parte q mudou
		}
		caminhos.add(c); //parte q mudou (se entrou no if) OU parte anterior até o fim (se eraToleravel==isToleravel(c)) OU a rota inteira (se anterior==null)
		
		return caminhos;
	}
	
	/**
	 * Dividir um caminho entre partes que passam em áreas perigosas e partes que passam em áreas seguras.
	 * A distância não é considerada.
	 */
	public List<Caminho> separarPartesPerigosas(Caminho caminho){
		List<Caminho> caminhos = new LinkedList<Caminho>();
		double tol = kernel.getMediaDens()*FATOR_TOLERANCIA;
		double tam = caminho.distanciaPercorrida();
		Ponto inicio = caminho.getInicio();
		Ponto fim = inicio;
		boolean eraSeguro = true;
		for(int i=1; i*GRANULARIDADE_ROTAS<=tam; i++){
			double dist = i*GRANULARIDADE_ROTAS;
			fim = caminho.buscarPonto(dist);
			double dif = perigo(fim)-tol;
//			/*teste*/System.out.printf("%.10f\n",dif);
			if(eraSeguro != dif>0){
				Caminho c = caminho.subCaminho(inicio, fim);
				caminhos.add(c);
				inicio = fim;
				eraSeguro = !eraSeguro;
			}
		}
		Caminho c = caminho.subCaminho(inicio, caminho.getFim());
		caminhos.add(c); //ultima parte 
		return caminhos;
	}
	
	
	/*--------------------- ENCONTRAR PONTOS PROMISSORES ---------------------*/
	
	
	private double quaoPromissor(Ponto p, Ponto origem, Ponto destino){
		Caminho opd = new Caminho(origem, p, destino);
		return perigo(opd);
	}
	
	public Queue<Ponto> pontosPromissores(Ponto origem, Ponto destino, double minimo){
		Queue<Ponto> q = new PriorityQueue<Ponto>(1024,new PontoPromissorComparator());
		Rectangle bounds = kernel.getBounds();
		int passo = kernel.getNodeSize()*DETALHE_PONTOS_PROMISSORES;
		
		//calcula quao promissores sao tds os pontos em uma grid
		int tamX = bounds.width/passo;
		int tamY = bounds.height/passo;
		PontoPromissor[][] grid = new PontoPromissor[tamX][tamY];
		for(int y=0; y<tamY; y++){
			for(int x=0; x<tamX; x++){
				int posX = bounds.x + x*passo;
				int posY = bounds.y + y*passo;
				double valor = quaoPromissor(new Ponto(posX,posY), origem, destino);
				grid[x][y] = new PontoPromissor(posX,posY,valor);
			}
		}
		
		//encontra minimos locais
		Point[][] min = new Point[tamX][tamY];
		for(int y=0; y<tamY; y++){
			for(int x=0; x<tamX; x++){
				if(min[x][y] == null){
					Point posMin = buscarMinimo(grid, min, tamX, tamY, new Point(x,y));
					PontoPromissor p = grid[posMin.x][posMin.y];
					if(!q.contains(p) && p.valor < minimo)
						q.add(p);
				}
			}
		}
		
		/*teste*/
//		TesteRotas.limpar();
//		TesteRotas teste = new TesteRotas(kernel, grafo);
//		for(int i=0; !q.isEmpty(); i++){
//			Ponto p = q.poll();
//			teste.addLabel(String.valueOf(i), p.x, p.y, Color.BLUE);
//		}
//		teste.refresh();
		/*teste*/
		
		return q;
	}
	
	private Point buscarMinimo(PontoPromissor[][] grid, Point[][] min, int tamX, int tamY, Point pos){
		int x = pos.x;
		int y = pos.y;
		if(min[x][y] != null)
			return min[x][y];
		double menor = grid[x][y].valor;
		double vizinho = Double.NaN;
		Point posMenor = pos; //comeca com a posicao central
		if(y>0){
			vizinho = grid[x][y-1].valor; //norte 
			if(vizinho < menor){
				menor = vizinho;
				posMenor = new Point(x,y-1);
			}
//			if(x>0){
//				vizinho = grid[x-1][y-1].valor; //noroeste 
//				if(vizinho < menor){
//					menor = vizinho;
//					posMenor = new Point(x-1,y-1);
//				}
//			}
//			if(x<tamX-1){
//				vizinho = grid[x+1][y-1].valor; //nordeste 
//				if(vizinho < menor){
//					menor = vizinho;
//					posMenor = new Point(x+1,y-1);
//				}
//			}
		}
		if(x>0){
			vizinho = grid[x-1][y].valor; //oeste
			if(vizinho < menor){
				menor = vizinho;
				posMenor = new Point(x-1,y);
			}
		}
		if(x<tamX-1){
			vizinho = grid[x+1][y].valor; //leste
			if(vizinho < menor){
				menor = vizinho;
				posMenor = new Point(x+1,y);
			}
		}
		if(y<tamY-1){
			vizinho = grid[x][y+1].valor; //sul
			if(vizinho < menor){
				menor = vizinho;
				posMenor = new Point(x,y+1);
			}
//			if(x>0){
//				vizinho = grid[x-1][y+1].valor; //sudoeste
//				if(vizinho < menor){
//					menor = vizinho;
//					posMenor = new Point(x-1,y+1);
//				}
//			}
//			if(x<tamX-1){
//				vizinho = grid[x+1][y+1].valor; //sudeste
//				if(vizinho < menor){
//					menor = vizinho;
//					posMenor = new Point(x+1,y+1);
//				}
//			}
		}
		
		if(posMenor.x == x && posMenor.y == y)
			min[x][y] = pos; //nenhum vizinho eh menor, etao este eh um minimo local
		else{
			min[x][y] = buscarMinimo(grid, min, tamX, tamY, posMenor); //seguir o vizinho menor
		}
		return min[x][y];
	}
	
	private class PontoPromissor extends Ponto{
		double valor;
		public PontoPromissor(int x, int y, double valor) {
			super(x,y);
			this.valor = valor;
		}
	}
	
	private class PontoPromissorComparator implements Comparator<Ponto>{
		@Override
		public int compare(Ponto o1, Ponto o2) {
			if(!(o1 instanceof PontoPromissor) && !(o2 instanceof PontoPromissor))
				return 0;
			else if(!(o1 instanceof PontoPromissor))
				return -1;
			else if(!(o2 instanceof PontoPromissor))
				return 1;
			else{
				PontoPromissor p1 = (PontoPromissor)o1;
				PontoPromissor p2 = (PontoPromissor)o2;
				if(p1.valor == p2.valor)
					return 0;
				else if(p1.valor > p2.valor)
					return 1;
				else
					return -1;
			}
		}
	}
}