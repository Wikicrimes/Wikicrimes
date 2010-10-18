package org.wikicrimes.util.rotaSegura.logica.modelo;

import java.util.ArrayList;
import java.util.List;

import org.wikicrimes.util.rotaSegura.geometria.Ponto;


public class VerticeRotas{
	
	Ponto ponto;
	int id;
	
	List<ArestaRotas> arestasSaindo;
	List<ArestaRotas> arestasEntrando;

	VerticeRotas(Ponto p, int id) {
		this.id = id;
		ponto = p;
		arestasSaindo = new ArrayList<ArestaRotas>();
		arestasEntrando = new ArrayList<ArestaRotas>();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof VerticeRotas){
			VerticeRotas outro = (VerticeRotas)obj;
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

	public Ponto getPonto() {
		return ponto;
	}

	public List<ArestaRotas> getArestasSaindo() {
		return arestasSaindo;
	}

	public List<ArestaRotas> getArestasEntrando() {
		return arestasEntrando;
	}
	
	
	
}