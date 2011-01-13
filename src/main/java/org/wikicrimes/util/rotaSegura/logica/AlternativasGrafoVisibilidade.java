package org.wikicrimes.util.rotaSegura.logica;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.wikicrimes.util.NumerosUtil;
import org.wikicrimes.util.rotaSegura.geometria.Poligono;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.geometria.Segmento;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaPromissora;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas.NaoTemCaminhoException;
import org.wikicrimes.util.rotaSegura.testes.TesteRotasImg;

public class AlternativasGrafoVisibilidade extends GeradorDeAlternativasAbstrato{

	private static final int DETALHAMENTO_POLIGONOS = 4; //qt maior, menos detalhe (mais vertices descartados)
	private static final double AJUSTE_PERIGO_MAXIMO = 1.7; //se tirar isso o metodo "todos caminhos" descarta tudo e retorna vazio (nao sei pq :/)
	
	public AlternativasGrafoVisibilidade(LogicaRotaSegura logica) {
		super(logica);
	}

	public Queue<Rota> getAlternativas(Rota rota){
		Queue<Rota> alternativas = new PriorityQueue<Rota>();
		try {
			Ponto i = rota.getInicio();
			Ponto f = rota.getFim();
			Rota rotaAntes = grafo.melhorCaminho(grafo.getOrigem(), i);
			Rota rotaDepois = grafo.melhorCaminho(f, grafo.getDestino());
			double perigoAntes = calcPerigo.perigo(rotaAntes);
			double perigoDepois = calcPerigo.perigo(rotaDepois);
			double limitePerigo = calcPerigo.perigo(rota);
			List<Rota> rotasPromissoras = calcularRotas(i, f, limitePerigo);
			for(Rota desvio : rotasPromissoras){
				double perigoDesvio = calcPerigo.perigo(desvio);
				double peso = perigoAntes + perigoDesvio + perigoDepois;
				RotaPromissora rotaPromissora = new RotaPromissora(desvio, peso);
				alternativas.offer(rotaPromissora);
			}
			return alternativas;
		} catch (NaoTemCaminhoException e) {
			return alternativas;
		}
	}
	
	
	private List<Rota> calcularRotas(Ponto origem, Ponto destino, double pesoMaximo){
		List<Rota> rotas = new ArrayList<Rota>();
		
		Rectangle area = getAreaLimite(origem, destino);
		List<Poligono> polis = getPoligonosHotspots(area);
		if(!new Segmento(origem, destino).intersectaAlgumPoli(polis))
			return rotas;
		Grafo<Ponto> g = getGrafoVisibilidade(polis, origem, destino);
		List<List<Ponto>> caminhos = g.todosCaminhos(origem, destino, pesoMaximo*AJUSTE_PERIGO_MAXIMO);
		
		for(List<Ponto> caminho : caminhos){
			rotas.add( new Rota(caminho) );
		}

//		/*TESTE*/TesteRotasImg teste = new TesteRotasImg(kernel);
//		/*TESTE*/teste.setTitulo("AlternativasGrafoVisibilidade");
//		/*TESTE*/teste.addGrafo(g, Color.GRAY);
//		/*TESTE*/teste.addPonto(origem, Color.BLACK);
//		/*TESTE*/teste.addPonto(destino, Color.BLACK);
//		/*TESTE*/for(Poligono p : polis)
//		/*TESTE*/	teste.addPoligono(p, new Color(200,0,0));
//		/*TESTE*/for(Rota r : rotas)
//		/*TESTE*/	teste.addRota(r, Color.BLUE);
//		/*TESTE*/teste.salvar();
		
		return rotas;
	}
	
	public Rectangle getAreaLimite(Ponto origem, Ponto destino){
		double dist = Ponto.distancia(origem, destino);
		int inc = (int)dist/4 + 1;
		int x1 = Math.min(origem.x, destino.x) - inc;
		int x2 = Math.max(origem.x, destino.x) + inc;
		int y1 = Math.min(origem.y, destino.y) - inc;
		int y2 = Math.max(origem.y, destino.y) + inc;
		return new Rectangle(x1, y1, x2-x1, y2-y1);
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
	public List<Poligono> getPoligonosHotspots(Rectangle bounds, int verticesPulados){
		//TODO passar a usar a classe HotspotContour e tirar esse metodo daqui
		
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
				if(!kernel.taNoHotspot(celula))
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
						if(!kernel.taNoHotspot(celula)){
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
					int passo = verticesPulados+1;
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
	
	public List<Poligono> getPoligonosHotspots(Rectangle bounds){
		return getPoligonosHotspots(bounds, DETALHAMENTO_POLIGONOS);
	}
	
	private void flood(boolean[][] visto, Point celula){
		if(celula.x >= visto.length || celula.y >= visto[0].length || celula.x < 0 || celula.y < 0)
			return;
		if(visto[celula.x][celula.y] || !kernel.taNoHotspot(celula))
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
	
	/*
	//pra cada poligono, sao calculadas 2 linhas de visibilidade
	//pra isso eh feita uma busca em todos vertices do poligono
	//sao buscados o vertice q da o menor angulo (da reta q vai de P ate o vertice) e o de maior angulo
	//FIXME soh nao funciona no seguinte cenario: P dentro de uma concavidade de um hotspot  
	private List<SemiReta> getLinhasVisibilidade(List<Poligono> poligonos, Ponto p){
		List<SemiReta> semiretas = new ArrayList<SemiReta>();
		for(Poligono poli : poligonos){
			double coefMinDir = Double.POSITIVE_INFINITY;
			double coefMaxDir = Double.NEGATIVE_INFINITY;
			double coefMinEsq = Double.POSITIVE_INFINITY;
			double coefMaxEsq = Double.NEGATIVE_INFINITY;
			SemiReta minDir = null;
			SemiReta maxDir = null;
			SemiReta minEsq = null;
			SemiReta maxEsq = null;
			for(int i=0; i<poli.pontos.length; i++){
				Ponto q = poli.pontos[i];
				SemiReta semi = new SemiReta(p,q);
				double coef = semi.coefA();
				if(q.x > p.x){
					if(coef < coefMinDir){
						coefMinDir = coef;
						minDir = semi;
					}
					if(coef > coefMaxDir){
						coefMaxDir = coef;
						maxDir = semi;
					}
				}else{
					if(coef < coefMinEsq){
						coefMinEsq = coef;
						minEsq = semi;
					}
					if(coef > coefMaxEsq){
						coefMaxEsq = coef;
						maxEsq = semi;
					}
				}
			}
			if(minDir==null){ //o poligono esta todo a esquerda de P
				semiretas.add(minEsq);
				semiretas.add(maxEsq);
			}else if(minEsq==null){ //o poligono esta todo a direita de P
				semiretas.add(minDir);
				semiretas.add(maxDir);
			}else{ //o poligono esta acima ou abaixo de P, caso especial em q os "coef" variam ao redor de + e - infinito
				   //por isso precisou guardar os max e min pros 2 lados (esq e dir)
				int yQualquer = poli.pontos[0].y; //pressupoe q esse poligono ta totalmente acima ou abaixo de P
				if(yQualquer < p.y){ //acima
					semiretas.add(minEsq);
					semiretas.add(maxDir);
				}else{ //abaixo
					semiretas.add(minDir);
					semiretas.add(maxEsq);
				}
			}
		}
		return semiretas;
	}
	*/
	
	private Grafo<Ponto> getGrafoVisibilidade(List<Poligono> poligonos, Ponto... pontos){
		CalculoPerigo calcPerigo = new CalculoPerigo(logica);
		
		//vertices de poligonos + pontos
		List<Ponto> vertices = new ArrayList<Ponto>();
		for(Ponto p : pontos)
			vertices.add(p);
		for(Poligono pol : poligonos)
			vertices.addAll(pol.getVertices());
		
		Grafo<Ponto> g = new Grafo<Ponto>(vertices);
		
		//arestas de poligonos
		List<Segmento> arestas = new ArrayList<Segmento>();
		for(Poligono pol : poligonos)
			arestas.addAll(pol.getArestas());
		for(Segmento s : arestas)
			g.addAresta(s.getInicio(), s.getFim(), s.comprimento());
		
		//pontos x outros pontos
		for(int i=0; i<pontos.length; i++){
			for(int j=i+1; j<pontos.length; j++){
				Ponto p = pontos[i];
				Ponto q = pontos[j];
				Segmento s = new Segmento(p,q);
				if(!s.intersectaAlgumForaPontas(arestas))
//					g.addAresta(p, q, s.comprimento());
					g.addAresta(p, q, calcPerigo.perigo(s));
			}
		}
		
		//poligonos x ...
		for(int i=0; i<poligonos.size(); i++){
			for(Ponto p : poligonos.get(i).getVertices()){
				
				//pontos
				for(Ponto q : pontos){
					Segmento s = new Segmento(p,q);
					if(!s.intersectaAlgumForaPontas(arestas))
						g.addAresta(p, q, s.comprimento());
				}
				
				//outros poligonos
				for(int j=i+1; j<poligonos.size(); j++){
					for(Ponto q : poligonos.get(j).getVertices()){
						Segmento s = new Segmento(p,q);
						if(!s.intersectaAlgumForaPontas(arestas))
							g.addAresta(p, q, s.comprimento());
					}
				}
			}
		}
		
		return g;
	}
	
	
}
