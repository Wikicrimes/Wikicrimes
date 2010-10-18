package org.wikicrimes.util.rotaSegura.logica.modelo;

import java.security.InvalidParameterException;

import org.wikicrimes.util.rotaSegura.geometria.Rota;


/**
 * @author victor
 *
 * Representa um trecho de rota que faz parte de um grafo de possíveis rotas. 
 * Foi gerado a partir de um SegmentoReta que foi enviado para o GoogleMaps, que retornou uma rota aproximada.
 */
public class RotaGM extends Rota implements Comparable{

	/**
	 * Custo deste trecho segundo o GoogleMaps
	 */
	private double custo;
	private double perigo;
	
	public RotaGM(Rota rota, double custo, double perigo) {
		super(rota);
		this.custo = custo;
		this.perigo = perigo;
	}
	
	public double getCusto() {
		return custo;
	}

	public double getPerigo() {
		return perigo;
	}
	
	@Override
	public RotaGM clone(){
		Rota r = (Rota)super.clone();
		RotaGM rgm = new RotaGM(r, this.custo, this.perigo);
		return rgm;
	}

	@Override
	public int compareTo(Object o) {
		if(o instanceof RotaGM){
			RotaGM r = (RotaGM)o;
			if(perigo > r.perigo)
				return 1;
			else if(perigo < r.perigo)
				return -1;
			else
				return 0;
		}else{
			throw new InvalidParameterException();
		}
	}
	
}
