package org.wikicrimes.util.rotaSegura;

import org.wikicrimes.util.kernelMap.Caminho;

public class Aresta {

	Caminho caminho;
	double custoGM;
	double perigo;
	
	Vertice antecessor;
	Vertice sucessor;
	
	public Aresta(Vertice antecessor, Vertice sucessor, Caminho rota, double custoGM, double perigo) {
		this.antecessor = antecessor;
		this.sucessor = sucessor;
		this.caminho = rota;
		this.custoGM = custoGM;
		this.perigo = perigo;
	}
	
	@Override
	public String toString() {
		return "("+antecessor+","+sucessor+")";
	}
}
