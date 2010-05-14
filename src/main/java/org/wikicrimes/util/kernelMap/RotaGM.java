package org.wikicrimes.util.kernelMap;


/**
 * @author victor
 *
 * Representa um trecho de rota que faz parte de um grafo de possíveis rotas. 
 * Foi gerado a partir de um SegmentoReta que foi enviado para o GoogleMaps, que retornou uma rota aproximada.
 */
public class RotaGM extends Caminho{

	/**
	 * Custo deste trecho segundo o GoogleMaps
	 */
	private double custo;
	private double perigo;
	
	public RotaGM(Caminho rota, double custo, double perigo) {
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
	public Object clone(){
		Caminho r = (Caminho)super.clone();
		RotaGM rgm = new RotaGM(r, this.custo, this.perigo);
		return rgm;
	}
	
}
