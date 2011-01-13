package org.wikicrimes.util.rotaSegura.logica;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.wikicrimes.util.rotaSegura.geometria.Poligono;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Retangulo;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.geometria.Segmento;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaPromissora;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas.NaoTemCaminhoException;
import org.wikicrimes.util.rotaSegura.testes.TesteRotasImg;

public class AlternativasGrafoDeCaminhosLivres extends AlternativasGrafoVisibilidade{

	public AlternativasGrafoDeCaminhosLivres(LogicaRotaSegura logica) {
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
			List<Rota> desvios = calcularRotas(i, f, limitePerigo);
			for(Rota desvio : desvios){
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
	
//	/*TESTE*/static TesteRotasImg teste;
	
	private List<Rota> calcularRotas(Ponto origem, Ponto destino, double pesoMaximo){
		Rectangle area = getAreaLimite(origem, destino);
		List<Poligono> polis = getPoligonosHotspots(area);
//		/*TESTE*/teste = new TesteRotasImg(kernel);
//		/*TESTE*/teste.setTitulo("AlternativasGrafoDeCaminhosLivres");
		Grafo<Ponto> g = getGrafoCaminhosLivres(polis, area, origem, destino);
		List<List<Ponto>> caminhos = g.todosCaminhos(origem, destino, pesoMaximo*1.7);
		
		List<Rota> rotas = new ArrayList<Rota>();
		for(List<Ponto> caminho : caminhos){
			rotas.add( new Rota(caminho) );
		}

//		/*TESTE*/teste.addGrafo(g, Color.BLACK);
//		/*TESTE*/teste.addPonto(origem, Color.BLACK);
//		/*TESTE*/teste.addPonto(destino, Color.BLACK);
//		/*TESTE*/teste.addPoligonos(polis, Color.RED);
//		/*TESTE*/teste.addRotas(rotas, Color.BLUE);
//		/*TESTE*/teste.salvar();
		return rotas;
	}

	private Grafo<Ponto> getGrafoCaminhosLivres(List<Poligono> poligonos, Rectangle area, Ponto origem, Ponto destino) {
		
		//junta segmentos dos poligonos + retangulo externo
		List<Segmento> obstaculos = new ArrayList<Segmento>();
		for(Poligono poli : poligonos)
			obstaculos.addAll(poli.getArestas());
		Retangulo retangulo = new Retangulo(area);
		obstaculos.addAll(retangulo.getArestas());
		
		List<Ponto> pontosMedios = pontosMediosDasVerticais(poligonos, obstaculos, retangulo);
		Grafo<Ponto> g = construirGrafo(pontosMedios, obstaculos, origem, destino);
		return g;
	}
	
	private List<Ponto> pontosMediosDasVerticais(List<Poligono> poligonos, List<Segmento> obstaculos, Retangulo retangulo){
		List<Ponto> pontosMedios = new ArrayList<Ponto>();
//		/*TESTE*/teste.addRetangulo(retangulo.bounds(), Color.GRAY);
		for(Poligono poli : poligonos){
			for(Ponto v : poli.getVertices()){
				if(!retangulo.contem(v)) continue;
				
				//encontra todas as intersecoes com obstaculos na vertical 
				Segmento vertical = new Segmento(v.x, retangulo.norte(), v.x, retangulo.sul());
				List<Ponto> intersecoes = vertical.intersecoes(obstaculos);
				if(!intersecoes.contains(v))
					intersecoes.add(v);
				Collections.sort(intersecoes, Ponto.ordemDeY);
				int index = Collections.binarySearch(intersecoes, v, Ponto.ordemDeY);
				
				//lado de trapezio pra cima
				Segmento cima = new Segmento(v, intersecoes.get(index-1));
				Ponto medioCima = cima.pontoMedio();
				if(!poli.contem(medioCima)){
//					/*TESTE*/teste.addSegmento(cima, Color.GRAY);
//					/*TESTE*/teste.addPonto(medioCima, Color.GRAY);
					pontosMedios.add(medioCima);
				}
				
				//lado de trapezio pra baixo
				Segmento baixo = new Segmento(v, intersecoes.get(index+1));
				Ponto medioBaixo = baixo.pontoMedio();
				if(!poli.contem(medioBaixo)){
//					/*TESTE*/teste.addSegmento(baixo, Color.GRAY);
//					/*TESTE*/teste.addPonto(medioBaixo, Color.GRAY);
					pontosMedios.add(medioBaixo);
				}
			}
		}
		return pontosMedios;
	}
	
	private Grafo<Ponto> construirGrafo(List<Ponto> pontosMedios, List<Segmento> obstaculos, Ponto origem, Ponto destino){
		Collections.sort(pontosMedios, Ponto.ordemDeX);

		
		Grafo<Ponto> g = new Grafo<Ponto>(pontosMedios);
		
		//adiciona arestas entre os pontos medios
		for(int i=0; i<pontosMedios.size(); i++){
			Ponto p = pontosMedios.get(i);
			if(g.getVizinhos(p).isEmpty())
				ligaAoVizinhoEsquerdo(p, i-1, pontosMedios, obstaculos, g);
			ligaAoVizinhoDireito(p, i+1, pontosMedios, obstaculos, g);
		}
		
		//adiciona origem ao grafo e liga aos vizinhos
		ligaPontoAoGrafo(origem, pontosMedios, obstaculos, g);
		
		//destino
		ligaPontoAoGrafo(destino, pontosMedios, obstaculos, g);
		
		return g;
	}
	
	private void ligaPontoAoGrafo(Ponto p, List<Ponto> pontosMedios, List<Segmento> obstaculos, Grafo<Ponto> g){
		g.addVertice(p);
		int index = Collections.binarySearch(pontosMedios, p, Ponto.ordemDeX);
		if(index < 0){
			index = -index-1;
			ligaAoVizinhoEsquerdo(p, index-1, pontosMedios, obstaculos, g);
			ligaAoVizinhoDireito(p, index, pontosMedios, obstaculos, g);
		}else{
			ligaAoVizinhoEsquerdo(p, index-1, pontosMedios, obstaculos, g);
			ligaAoVizinhoDireito(p, index+1, pontosMedios, obstaculos, g);
		}
	}
	
	private void ligaAoVizinhoEsquerdo(Ponto p, int posIni, List<Ponto> pontosMedios, List<Segmento> obstaculos, Grafo<Ponto> g){
		if(posIni<0 || posIni>=pontosMedios.size()) return;
		Ponto vizinhoEsq = null;
		for(int j=posIni; vizinhoEsq==null && j>=0; j--){
			Ponto candidato = pontosMedios.get(j);
			Segmento segmPC = new Segmento(p, candidato); 
			if(!segmPC.intersectaAlgum(obstaculos))
				vizinhoEsq = candidato;
		}
		if(vizinhoEsq != null){
			double perigo = calcPerigo.perigo(new Segmento(p, vizinhoEsq));
			g.addAresta(p, vizinhoEsq, perigo);
		}
	}
	
	private void ligaAoVizinhoDireito(Ponto p, int posIni, List<Ponto> pontosMedios, List<Segmento> obstaculos, Grafo<Ponto> g){
		if(posIni<0 || posIni>=pontosMedios.size()) return;
		Ponto vizinhoDir = null;
		for(int j=posIni; vizinhoDir==null && j<pontosMedios.size(); j++){
			Ponto candidato = pontosMedios.get(j);
			Segmento segmPC = new Segmento(p, candidato);
			if(!segmPC.intersectaAlgum(obstaculos))
				vizinhoDir = candidato;
		}
		if(vizinhoDir != null){
			double perigo = calcPerigo.perigo(new Segmento(p, vizinhoDir));
			g.addAresta(p, vizinhoDir, perigo);
		}
	}
	
}
