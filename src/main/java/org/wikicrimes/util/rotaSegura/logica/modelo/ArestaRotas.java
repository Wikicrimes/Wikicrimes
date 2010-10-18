package org.wikicrimes.util.rotaSegura.logica.modelo;

import org.wikicrimes.util.rotaSegura.geometria.Rota;


public class ArestaRotas {

	Rota rota;
	double custoGM;
	double perigo;
	
	VerticeRotas antecessor;
	VerticeRotas sucessor;
	
	public ArestaRotas(VerticeRotas antecessor, VerticeRotas sucessor, Rota rota, double custoGM, double perigo) {
		this.antecessor = antecessor;
		this.sucessor = sucessor;
		this.rota = rota;
		this.custoGM = custoGM;
		this.perigo = perigo;
	}
	
	@Override
	public String toString() {
		return "("+antecessor+","+sucessor+")";
	}

	public Rota getRota() {
		return rota;
	}

	public VerticeRotas getAntecessor() {
		return antecessor;
	}

	public VerticeRotas getSucessor() {
		return sucessor;
	}
	
}
