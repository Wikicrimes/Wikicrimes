package org.wikicrimes.util.rotaSegura.logica.modelo;

import org.wikicrimes.util.rotaSegura.geometria.Rota;

public class RotaPromissora extends Rota implements Comparable<RotaPromissora> {
	
	private double peso;

	public RotaPromissora(Rota rota, double peso) {
		super(rota);
		this.peso = peso;
	}
	
	public RotaPromissora(RotaPromissora rota){
		this(rota, rota.peso);
	}
	
	public Double getPeso(){
		return peso;
	}

	@Override
	public int compareTo(RotaPromissora o) {
		if (peso == o.peso)
			return 0;
		else if (peso < o.peso)
			return -1;
		else
			return 1;
	}
	
}