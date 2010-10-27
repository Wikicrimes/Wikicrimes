package org.wikicrimes.util.kernelMap;

import org.wikicrimes.model.PontoLatLng;

/**
 * Representa um "bounds" em lat-long vindo do GoogleMaps.
 * Precisa disso pq Rectangle soh aceita inteiros.
 */
public class LatLngBoundsGM {

	public double norte;
	public double sul;
	public double leste;
	public double oeste;
	
	//isso não é redundante!
	//Considere quando a emenda do mapa estiver visível, então o valor de oeste pode ser maior que leste. Neste caso, (leste-oeste) daria um valor negativo, diferente da largura
	//Considere ainda se, num nível de zoom baixo, estiverem visíveis várias emendas (vários mapas do mundo inteiro pequenos)
	public double width;
	public double height;
	
	
	public LatLngBoundsGM(double norte, double sul, double leste, double oeste, double width, double height) {
		this.norte = norte;
		this.sul = sul;
		this.leste = leste;
		this.oeste = oeste;
		this.width = width;
		this.height = height;
	}
	
	public LatLngBoundsGM(PontoLatLng superiorEsquerdo, PontoLatLng inferiorDireito, double width, double height){
		this(superiorEsquerdo.getLatitude(), inferiorDireito.getLatitude(), 
				inferiorDireito.getLongitude(), superiorEsquerdo.getLongitude(), width, height);
	}
	
	public LatLngBoundsGM(double norte, double sul, double leste, double oeste) {
		this.norte = norte;
		this.sul = sul;
		this.leste = leste;
		this.oeste = oeste;
		this.width = norte-sul;
		this.height = leste-oeste;
	}
	
	public LatLngBoundsGM(PontoLatLng superiorEsquerdo, PontoLatLng inferiorDireito){
		this(superiorEsquerdo.getLatitude(), inferiorDireito.getLatitude(), 
				inferiorDireito.getLongitude(), superiorEsquerdo.getLongitude());
	}
	
	@Override
	public String toString() {
		return "n:"+ norte +", s:"+ sul +", l:"+ leste +", o:"+ oeste +", w:"+ width +", h:"+ height;
	}
	
}
