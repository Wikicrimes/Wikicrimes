package org.wikicrimes.util.rotaSegura;

import java.util.ArrayList;
import java.util.List;

import org.wikicrimes.util.kernelMap.Ponto;

public class Vertice{
	
	Ponto ponto;
	int id;
	
	List<Aresta> arestasSaindo;
	List<Aresta> arestasEntrando;

	Vertice(Ponto p, int id) {
		this.id = id;
		ponto = p;
		arestasSaindo = new ArrayList<Aresta>();
		arestasEntrando = new ArrayList<Aresta>();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Vertice){
			Vertice outro = (Vertice)obj;
			return this.ponto.equals(outro.ponto);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.ponto.hashCode();
	}
	
	@Override
	public String toString() {
		return String.valueOf(id);
	}

}