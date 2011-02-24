package org.wikicrimes.util.rotaSegura.logica;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import org.wikicrimes.util.kernelMap.PropertiesLoader;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.logica.exceptions.CantFindPath;
import org.wikicrimes.util.rotaSegura.logica.exceptions.VertexNotInGraph;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaPromissora;
import org.wikicrimes.util.rotaSegura.testes.TesteRotasImg;

public class AlternativasPontoDesvio extends GeradorDeAlternativasAbstrato{

	private static final int DETALHE_PONTOS_PROMISSORES = PropertiesLoader.getInt("saferoutes.promising_points_grid_resolution");
	
	public AlternativasPontoDesvio(LogicaRotaSegura logica) {
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
			double perigoMaximo = calcPerigo.perigo(rota);
			Queue<Ponto> pontosPromissoresOD = getPontosPromissores(i, f, perigoMaximo);
			while(!pontosPromissoresOD.isEmpty()){
				Ponto p = pontosPromissoresOD.poll();
				if(p.isPerto(i) || p.isPerto(f))
					continue;
				Rota desvio = new Rota(i,p,f);
				double peso = perigoAntes + calcPerigo.perigo(desvio) + perigoDepois;
				RotaPromissora rotaPromissora = new RotaPromissora(desvio, peso);
				alternativas.offer(rotaPromissora);
			}
			return alternativas;
		} catch (CantFindPath e) {
			return alternativas;
		} catch (VertexNotInGraph e) {
//			/*DEBUG*/System.err.println(e.getMessage());
//			/*DEBUG*/TesteRotasImg teste = new TesteRotasImg(logicaRota.getKernel(), grafo);
//			/*DEBUG*/teste.addRota(rota, Color.ORANGE);
//			/*DEBUG*/teste.addPonto(e.vertex, Color.MAGENTA);
//			/*DEBUG*/teste.salvar();
			return alternativas;
		}
		
	}
	
	private Queue<Ponto> getPontosPromissores(Ponto origem, Ponto destino, double maximo){
		
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
					if(!q.contains(p) && p.valor < maximo)
						q.add(p);
				}
			}
		}
		
//		/*TESTE*/TesteRotasImg teste = new TesteRotasImg(kernel);
//		/*TESTE*/teste.setTitulo("AlternativasPontoDesvio");
//		/*TESTE*/teste.addPonto(origem, Color.BLACK);
//		/*TESTE*/teste.addPonto(destino, Color.BLACK);
//		/*TESTE*/int i = 1;
//		/*TESTE*/for(Ponto p : q){
//		/*TESTE*/	teste.addLabel("PontoDesvio"+i,String.valueOf(i), p.x, p.y, Color.BLUE);
//		/*TESTE*/i++;
//		/*TESTE*/}
//		/*TESTE*/teste.salvar();
		
		return q;
	}
	

	private double quaoPromissor(Ponto p, Ponto origem, Ponto destino){
		Rota opd = new Rota(origem, p, destino);
		return calcPerigo.perigo(opd);
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
		}
		
		if(posMenor.x == x && posMenor.y == y)
			min[x][y] = pos; //nenhum vizinho eh menor, etao este eh um minimo local
		else{
			min[x][y] = buscarMinimo(grid, min, tamX, tamY, posMenor); //seguir o vizinho menor
		}
		return min[x][y];
	}
	
	@SuppressWarnings("serial")
	private class PontoPromissor extends Ponto{
		double valor;
		public PontoPromissor(int x, int y, double valor) {
			super(x,y);
			this.valor = valor;
		}
		public PontoPromissor(Ponto p, double valor){
			this(p.x, p.y, valor);
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
