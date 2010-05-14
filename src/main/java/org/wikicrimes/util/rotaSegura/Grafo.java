package org.wikicrimes.util.rotaSegura;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.wikicrimes.util.kernelMap.Caminho;
import org.wikicrimes.util.kernelMap.Ponto;
import org.wikicrimes.util.kernelMap.SegmentoReta;


/**
 * @author victor
 * Grafo  dos possíveis caminhos entre a origem e o destino especificados pelo usuário no GoogleMaps.
 * O menor caminho para todo vértice é computado na inserção e remoção de rotas.
 *	
 */
public class Grafo {

	private Map<Ponto,Vertice> vertices;
	private Ponto origem, destino;
	
	private int numVertices;
	
	public Grafo(Ponto origem, Ponto destino){
		this.origem = origem;
		this.destino = destino;
		vertices = new HashMap<Ponto,Vertice>();
		Vertice noO = new Vertice(origem, ++numVertices);
		Vertice noD = new Vertice(destino, ++numVertices);
		vertices.put(origem, noO);
		vertices.put(destino, noD);
		mapaVerticesMC = new HashMap<Ponto, VerticeMC>();
	}
	
	private Vertice inserirVertice(Ponto v){
		Vertice no = null;
		if(!vertices.containsKey(v)){
			no =  new Vertice(v, ++numVertices);
			vertices.put(v,no);
		}else{
			no = vertices.get(v); 
		}											 
		return no;
	}
		
	public void inserirCaminho(Caminho caminho, double custo, double perigo){
		//TODO problema: esse custo e perigo teria q dividir nos caminhos menores
		
		if(caminho == null)
			throw new InvalidParameterException();
		
		//TRATAR INTERSECOES
		
		//caso 1, caminhos cruzam: criar um vértice
		//TODO (precisa msm? normalmente o caminho do GM n tende a seguir um anterior, em vez de cruzar?)
		
		//caso 2, caminhos tem segmentos de reta em comum: unir a parte em comum em um só caminho e criar vértices nas bifurcações
		List<Caminho> partesIteracaoAnterior = new LinkedList<Caminho>();
		partesIteracaoAnterior.add(caminho);
		for(Caminho caminhoNoGrafo : getCaminhos()){
			List<Caminho> partesIteracaoAtual = new LinkedList<Caminho>();
			for(Caminho parte : partesIteracaoAnterior){
				partesIteracaoAtual.addAll(parte.subtracao(caminhoNoGrafo));
			}
			partesIteracaoAnterior = partesIteracaoAtual;
		}
		
		
		//INSERIR
		
		for(Caminho novoCaminho : partesIteracaoAnterior){
			inserirAresta(novoCaminho, custo, perigo);
			Ponto ini = novoCaminho.getInicio();
			Ponto fim = novoCaminho.getFim();

//			//TODO caso inicio e/ou fim estejam contidos em algum caminho q ja faz parte do grafo
//			for(Aresta aresta: getArestas()){
//				Caminho caminhoAresta = aresta.caminho; 
//				if(caminhoAresta.passaPor(ini)){
//					removerAresta(aresta);
//					Caminho subCaminho1 = caminhoAresta.subCaminho(caminhoAresta.getInicio(), ini);
//					inserirAresta(subCaminho1, custo, perigo);
//					Caminho subCaminho2 = caminhoAresta.subCaminho(ini, caminhoAresta.getFim());
//					inserirAresta(subCaminho2, custo, perigo);
//				}
//				if(caminhoAresta.passaPor(fim)){
//					removerAresta(aresta);
//					Caminho subCaminho1 = caminhoAresta.subCaminho(caminhoAresta.getInicio(), fim);
//					inserirAresta(subCaminho1, custo, perigo);
//					Caminho subCaminho2 = caminhoAresta.subCaminho(fim, caminhoAresta.getFim());
//					inserirAresta(subCaminho2, custo, perigo);
//				}
//			}
		}
	}
	
	private void inserirAresta(Caminho caminho, double custo, double perigo){
		Vertice ini = inserirVertice(caminho.getInicio());
		Vertice fim = inserirVertice(caminho.getFim());
		Aresta aresta = new Aresta(ini, fim, caminho, custo, perigo);
		ini.arestasSaindo.add(aresta);
		fim.arestasEntrando.add(aresta);
	}
	
	private void removerAresta(Aresta aresta){
		aresta.antecessor.arestasSaindo.remove(aresta);
		aresta.sucessor.arestasEntrando.remove(aresta);
	}
	
	public Ponto getOrigem() {
		return origem;
	}
	
	public Ponto getDestino() {
		return destino;
	}
	
	
	/**
	 * @return SegmentoReta que vai da ORIGEM ate o DESTINO
	 */
	public SegmentoReta retaOD(){
		return new SegmentoReta(getOrigem(), getDestino());
	}
	
	/**
	 * @return distância (reta) entre a origem e o destino
	 */
	public double distOD(){
		return retaOD().comprimento();
	}
	

	@Override
	public Object clone(){
		Grafo g = new Grafo(origem, destino);
		
		List<Vertice> nos = new ArrayList<Vertice>(); 
		nos.addAll(vertices.values());
		Collections.sort(nos, ordemId);
		
		for(Vertice v : nos){
			g.inserirVertice(v.ponto); //insere vertices na ordem pra manter a ordem dos IDs
		}
		for(Vertice v : nos){
			for(Aresta r : v.arestasSaindo){
				g.inserirCaminho((Caminho)r.caminho.clone(), r.custoGM, r.perigo);
			}
		}
		return g;
	}
	
	private static Comparator<Vertice> ordemId = new Comparator<Vertice>(){
		@Override
		public int compare(Vertice o1, Vertice o2) {
			return o1.id - o2.id;
		}
	};
	
	public Map<Integer,Ponto> getPontos(){
		Map<Integer,Ponto> pontos = new HashMap<Integer,Ponto>();
		for(Vertice v : vertices.values()){
			pontos.put(v.id,v.ponto);
		}
		return pontos;
	}
	
	public List<Caminho> getCaminhos(){
		List<Caminho> rotas = new ArrayList<Caminho>();
		for(Vertice v : vertices.values())
			for(Aresta a : v.arestasSaindo){
				rotas.add(a.caminho);
			}
		return rotas;
	}
	
	public List<Aresta> getArestas(){
		List<Aresta> rotas = new LinkedList<Aresta>();
		for(Vertice v : vertices.values())
			for(Aresta a : v.arestasSaindo){
				rotas.add(a);
			}
		return rotas;
	}
	
	public String getDetalhes(Ponto p){
		Vertice v = vertices.get(p);
		if(v != null){
			StringBuilder s = new StringBuilder();
			s.append("id = " + v.id + "\n");
			s.append("x = " + p.x + ", y = " + p.y + "\n");
			return s.toString();
		}else{
			return "null";
		}
	}
	
	
	//CALCULO DE MENOR CAMINHO (Dijkstra)
	public Caminho melhorCaminho(Ponto origem, Ponto destino){
		
		if(vertices.get(origem) == null) 
			throw new InvalidParameterException(origem + " nao eh um vertice do grafo");
		if(vertices.get(destino) == null)
			throw new InvalidParameterException(destino + " nao eh um vertice do grafo");
		
		//inicializar fila de prioridade Q
		PriorityQueue<VerticeMC> q = new PriorityQueue<VerticeMC>();
		for(Vertice v: vertices.values()){
			VerticeMC w = null; 
			if(v == vertices.get(origem))
				w = new VerticeMC(v, 0);
			else
				w = new VerticeMC(v, Double.POSITIVE_INFINITY);
			q.add(w);
			mapaVerticesMC.put(v.ponto, w);
		}
		
		//loop em Q
		while(!q.isEmpty()){
			VerticeMC v = q.poll();
			for(Aresta a : v.arestasSaindo){
				VerticeMC w = mapaVerticesMC.get(a.sucessor.ponto);
				
				//relaxamento
				if(w.custo > v.custo + a.perigo){
					w.custo = v.custo + a.perigo;
					w.pai = v;
				}
			}
		}
		
		//recuperar menor caminho
		Ponto p = destino;
		Caminho r = new Caminho(destino);
		while(p != origem){
			p = mapaVerticesMC.get(p).pai.ponto;
			r.addInicio(p);
		}
		return r;
	}
	
	public Caminho melhorCaminho(){
		return melhorCaminho(origem, destino);
	}
	
	private class VerticeMC extends Vertice implements Comparable<VerticeMC>{
		
		double custo;
		VerticeMC pai;

		VerticeMC(Vertice v, double custo){
			super(v.ponto,v.id);
			arestasSaindo = v.arestasSaindo;
			arestasEntrando = v.arestasEntrando;
			this.custo = custo;
		}
		
		public int compareTo(VerticeMC o) {
			if(custo == o.custo)
				return 0;
			else if(custo > o.custo)
				return 1;
			else
				return -1;
		}
	}

	private Map<Ponto, VerticeMC> mapaVerticesMC;
}