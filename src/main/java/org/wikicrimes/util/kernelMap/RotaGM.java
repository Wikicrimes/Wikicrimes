package org.wikicrimes.util.kernelMap;


/**
 * @author victor
 *
 * Representa um trecho de rota que faz parte de um grafo de possíveis rotas. 
 * Foi gerado a partir de um SegmentoReta que foi enviado para o GoogleMaps, que retornou uma rota aproximada.
 */
public class RotaGM extends Rota{

	/**
	 * Custo deste trecho segundo o GoogleMaps
	 */
	private double custo;
	private double perigo;
	
	/**
	 * O GrafoRotas do qual este trecho faz parte
	 */
//	private GrafoRotas grafo;
	
	
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
	protected Object clone(){
		Rota r = (Rota)super.clone();
		RotaGM rgm = new RotaGM(r, this.custo, this.perigo);
		return rgm;
	}
	

//	public GrafoRotas getGrafo() {
//		return grafo;
//	}
//
//	void setGrafo(GrafoRotas grafo){
//		if(this.grafo == null || grafo == null)
//			this.grafo = grafo;
//		else
//			throw new InvalidParameterException("não pode sobrepor o GrafoRotas associado a um TrechoRotaGM");
//	}

}
