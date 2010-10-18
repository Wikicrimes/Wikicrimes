package org.wikicrimes.util.rotaSegura.logica;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.wikicrimes.util.rotaSegura.logica.modelo.ArestaRotas;
import org.wikicrimes.util.rotaSegura.logica.modelo.VerticeRotas;
import org.wikicrimes.util.rotaSegura.testes.TesteRotasImg;

/**
 * @author victor
 *
 * Grafo de propósito geral.
 * Implementado com lista de adjascências.
 * Direcionado (usar apenas addArco) ou não (usar apenas addAresta).
 */
public class Grafo<T> {

	Set<T> vertices;
	Map<T,No> adjascencias;
	
	public Grafo(){
		vertices = new HashSet<T>();
		adjascencias = new HashMap<T,No>();
	}
	
	public Grafo(Collection<T> vertices){
		this.vertices = new HashSet<T>(vertices);
		adjascencias = new HashMap<T,No>();
	}
	
	public void addVertice(T elm){
		vertices.add(elm);
	}
	
	public void addAresta(T v, T w, double peso){
		addArco(v, w, peso);
		addArco(w, v, peso);
	}
	
	public void addArco(T v, T w, double peso){
		No a = adjascencias.get(v);
		if(a == null){
			adjascencias.put(v, new No(w, peso));
		}else{
			if(a.elm.equals(w)) return;
			while(a.prox != null){
				a = a.prox;
				if(a.elm.equals(w)) return;
			}
			a.prox = new No(w, peso);
		}
	}
	
	public Set<T> getVertices(){
		return new HashSet<T>(vertices);
	}
	
	public Set<T> getVizinhos(T vertice){
		Set<T> adj = new HashSet<T>(); 
		for(No n : getNosVizinhos(vertice))
			adj.add(n.elm);
		return adj;
	}
	
	private Set<No> getNosVizinhos(T vertice){
		No no = adjascencias.get(vertice);
		Set<No> adj = new HashSet<No>(); 
		if(no != null){
			adj.add(no);
			while(no.prox != null){
				no = no.prox;
				adj.add(no);
			}
		}
		return adj;
	}
	
	private class No{
		T elm;
		double peso;
		No prox;
		No(T elm, double peso){
			this.elm = elm;
			this.peso = peso;
		}
	}
	
	//CALCULO DE MENOR CAMINHO (Dijkstra)
	public List<T> menorCaminho(T origem, T destino){

		//inicializar fila de prioridade Q
		PriorityQueue<NoMC> q = new PriorityQueue<NoMC>();
		Map<T, NoMC> mapa = new HashMap<T, NoMC>(); //mapa pra obter os NoMC apartir dos vertices
		for(T v: vertices){
			double peso = v.equals(origem)? 0 : Double.POSITIVE_INFINITY;
			NoMC w = new NoMC(v, peso);
			q.add(w);
			mapa.put(v, w);
		}
		
		//loop em Q
		while(!q.isEmpty()){
			NoMC v = q.poll();
			for(No noW : getNosVizinhos(v.elm)){
				NoMC w = mapa.get(noW.elm);
				
				//relaxamento
				if(w.custo > v.custo + noW.peso){
					w.custo = v.custo + noW.peso;
					w.pai = v;
					q.remove(w); //remove e adiciona pra atualizar a posição
					q.add(w);
				}
			}
		}

		//recuperar menor caminho
		T v = destino;
		List<T> caminho = new ArrayList<T>();
		caminho.add(destino);
		while(!v.equals(origem)){
			NoMC vmc = mapa.get(v);
			NoMC pai = vmc.pai;
			if(pai == null)
				throw new AssertionError("nao existe caminho entre "+origem+" e "+destino); 
			v = pai.elm;
			caminho.add(0, v);
		}

		return caminho;
	}
	
	private class NoMC implements Comparable<NoMC>{
		T elm;
		double custo;
		NoMC pai;
		NoMC(T elm, double custo){
			this.elm = elm;
			this.custo = custo;
		}
		@Override
		public int compareTo(NoMC o) {
			if(custo > o.custo)
				return 1;
			else if(custo < o.custo)
				return -1;
			else
				return 0;
		}
	}

	public List<List<T>> todosCaminhos(T origem, T destino, double pesoMaximo){
		this.origemTC = origem;
		this.destinoTC = destino;
		this.pesoMaximo = pesoMaximo;
		List<T> raiz = new ArrayList<T>();
		raiz.add(origem);
		List<List<T>> caminhos = todosCaminhosSubrotina(origem, destino, raiz, 0);
		this.origemTC = this.destinoTC = null;
		this.pesoMaximo = Double.NaN;
		return caminhos;
	}
	private T origemTC, destinoTC;
	private double pesoMaximo = Double.NaN;
	private List<List<T>> todosCaminhosSubrotina(T origem, T destino, List<T> caminhoAteOrigem, double pesoAteOrigem){
		List<List<T>> caminhos = new ArrayList<List<T>>();

		//condição de parada: qd chega no destino
		if(origem.equals(destino)){
			caminhos.add(caminhoAteOrigem);
			return caminhos;	
		}
			
		//ramificação
		for(No no : getNosVizinhos(origem)){
			T v = no.elm;
			if(caminhoAteOrigem.contains(v)) continue;
			List<T> caminhoAteV = new ArrayList<T>(caminhoAteOrigem);
			caminhoAteV.add(v);
			double pesoAteV = pesoAteOrigem + no.peso;
			if(pesoAteV > pesoMaximo) continue;
			caminhos.addAll(todosCaminhosSubrotina(v, destino, caminhoAteV, pesoAteV));
		}
		return caminhos;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(T v : vertices){
			s.append(v + ": ");
			for(T w : getVizinhos(v)){
				s.append(w + ", ");
			}
			s.append("\n");
		}
		return s.toString();
	}
	
}