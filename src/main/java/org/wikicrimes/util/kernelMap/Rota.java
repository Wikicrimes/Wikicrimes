package org.wikicrimes.util.kernelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Conjunto de coordenadas ou conjunto de segmentos de reta ligados. 
 * 
 * @author Victor E.
 */
public class Rota {

	private List<Ponto> pontos;
	
	public Rota(){
		pontos = new LinkedList<Ponto>();
	}
	
	public Rota(List<Ponto> pontos){
		this();
		for(Ponto ponto : pontos){
			if(ponto != null)
				add(ponto);
		}
	}
	
	public Rota(Ponto ...ponto){
		this(Arrays.asList(ponto));
	}

	public Rota(SegmentoReta... segm){ //esse boolean é so pra a assinatura do construtor ficar diferente da outra acima
		this();
		for(SegmentoReta s : segm){
			if(s != null)
				add(s);
		}
	}
	
	public Rota(Rota rota){
		pontos = new LinkedList<Ponto>(rota.pontos);
	}
	
	/**
	 * @param string : uma String no formato xxx,xxxaxxx,xxxaxxx,xxx (pontos do formato xxx,xxx separados por 'a')
	 */
	public Rota(String string){
		pontos = new LinkedList<Ponto>();
		for(String str : string.split("a")){
			pontos.add(new Ponto(str));
		}
	}
	
	/**
	 * Adiciona uma Coordenada no fim desta Rota
	 */
	public void add(Ponto ponto){
		if(pontos.contains(ponto)){
			//System.err.println("coordenada repetida sendo adicionada numa Rota");
			pontos.add(ponto.clone()); //nao pode ter objetos iguais, senao vai dar problemas pra identificar
		}else{
			pontos.add(ponto);
		}
	}
	
	/**
	 * Adiciona uma Coordenada no fim desta Rota
	 */
	public void addInicio(Ponto ponto){
		if(pontos.contains(ponto)){
			//System.err.println("coordenada repetida sendo adicionada numa Rota");
			pontos.add(0,ponto.clone());
		}else{
			pontos.add(0,ponto);
		}
	}
	
	public void addDepois(Ponto pontoAnterior, Ponto novoPonto){
		int index = pontos.indexOf(pontoAnterior);
		if(pontos.contains(novoPonto)){
			//System.err.println("coordenada repetida sendo adicionada numa Rota");
			pontos.add(index+1, novoPonto.clone());
		}else{
			pontos.add(index+1, novoPonto);
		}
	}
	
	/**
	 * Adiciona todas as Coordenadas da rota passada como parâmetro no fim desta Rota
	 */
	public void add(Rota rota){
		for(Ponto coord : rota.getPontos()){
			add(coord);
		}
	}
	
	public void addInicio(Rota rota){
		List<Ponto> pts = rota.getPontos();
		for(int i=pts.size()-1; i>=0; i--){
			addInicio(pts.get(i));
		}
	}
	
	public void add(SegmentoReta segm){
		if(pontos.isEmpty()){
			pontos.add(segm.getInicio());
			pontos.add(segm.getFim());
		}else if(segm.getInicio().equals(this.getFim())){
			pontos.add(segm.getFim());
		}else{
			throw new RuntimeException("tentou-se adicionar um SegmentoReta a uma Rota, " +
					"mas o início do SegmentoReta é diferente do fim da rota");
		}
	}
	
	public boolean remove(Ponto p){
		return pontos.remove(p);
	}
	
	public Ponto pontoAnterior(Ponto p){
		if(contains(p) && !isFirst(p)){
			int index = pontos.indexOf(p);
			return pontos.get(index-1);
		}else{
			return null;
		}
	}
	
	public Ponto pontoPosterior(Ponto p){
		if(contains(p) && !isLast(p)){
			int index = pontos.indexOf(p);
			return pontos.get(index+1);
		}else{
			return null;
		}
	}
	
	/**
	 * @return as Coordenadas que compôem a Rota
	 */
	public List<Ponto> getPontos(){
		return pontos;
	}
	
	public int size(){
		return pontos.size();
	}
	
	public boolean isLast(Ponto p){
		return pontos.lastIndexOf(p) == pontos.size()-1;
	}
	
	public boolean isFirst(Ponto p){
		return pontos.indexOf(p) == 0;
	}
	
	public boolean contains(Ponto p){
		return pontos.contains(p);
	}
	
	/**
	 * Verifica se esta rota contém uma coordenada próxima desta passada por parâmetro.<br>
	 * Motivação desta aproximação: Precisa-se comparar a rota segura gerada pelo server com a 
	 * rota gerada pelo googlemaps (a partir da primeira). Esta segunda rota deveria conter pontos idênticos
	 * a todos os pontos daquela primeira rota, mas isto não acontece (se um dos pontos não cair dentro de uma rua, por ex.).
	 */
	public boolean containsAproximado(Ponto p, int distMax){
		return getPontoAproximado(p, distMax) != null;
	}
	
	/**
	 * Obtém uma coordenada que pertence à rota e que é a mais próxima da coordenada passada por parâmetro.<br>
	 * Motivação desta aproximação: Precisa-se comparar a rota segura gerada pela lógica de rotas seguras com a 
	 * rota gerada pelo googlemaps (a partir da primeira). Esta segunda rota deveria conter pontos idênticos
	 * a todos os pontos daquela primeira rota, mas isto nem sempre acontece (por ex: se um dos pontos cair dentro de um quarteirão). 
	 */
	public Ponto getPontoAproximado(Ponto p, int distMax){
		
		if(p == null) 
			return null;
		
		//sobrescreve o método equals para esta Coordenada 
		PontoAproximado ptAproximado = new PontoAproximado(p.x,p.y);
		
		int index = -1;
		while(ptAproximado.proximidade<=distMax && index==-1) {
			index = pontos.indexOf(ptAproximado); //indice da coordenada próxima (a qual foi identificada pelo metodo equals)
												  //(todas as comparações feitas neste método indexOf usarão a implementação do equals de CoordenadaAproximada)
			ptAproximado.proximidade++;
		}
		
		if(index != -1)
			return pontos.get(index);
		else
			return null;
	}
	
	/**
	 * Obtém os pontos desta rota que correspondem a pontos aproximados aos de uma outra rota semelhante.
	 * Obs: espera-se que a "outraRota" tenha menos pontos do que esta 
	 */
	public Rota getRotaAproximada(Rota outraRota, int distMax){
		Rota rotaAproximada = new Rota();
		for(Ponto p: outraRota.getPontos()){
			Ponto aprox = this.getPontoAproximado(p, distMax);
			if(aprox != null)
				rotaAproximada.add(aprox);
		}
		return rotaAproximada;
	}
	
	/**
	 * @return os SegmentosReta que compôem a Rota. É equivalente ao getCoordenadas.
	 */
	public List<SegmentoReta> getSegmentosReta(){
		List<SegmentoReta> listSeg = new LinkedList<SegmentoReta>();
		for (int i = 0; i < pontos.size()-1; i++) {
			listSeg.add(new SegmentoReta(pontos.get(i),pontos.get(i+1)));
		}
		return listSeg;
	}
	
	public Ponto getInicio(){
		if(pontos.isEmpty())
			return null;
		return pontos.get(0);
	}
	
	public Ponto getFim(){
		if(pontos.isEmpty())
			return null;
		return pontos.get(pontos.size()-1);
	}
	
	public double getDistanciaPercorrida() {
		double distancia = 0;
		List<SegmentoReta> segms = getSegmentosReta();
		for(SegmentoReta segm : segms){
			distancia += segm.getComprimento();
		}
		return distancia;
	}
	
	/**
	 * @return distância (reta) entre a ORIGEM e o DESTINO
	 */
	public double getDistanciaRetaOD(){
		return new SegmentoReta(getInicio(), getFim()).getComprimento();
	}
	
	public Rota invertida(){
		Rota inv = new Rota();
		for(Ponto p: pontos){
			inv.add(p.invertido());
		}
		return inv;
	}
	
	public Rota rotacionada(double angulo){
		Rota rota = new Rota();
		for(Ponto p : this.getPontos()){
			rota.add(p.rotacionado(angulo));
		}
		return rota;
	}
	
	public static List<Rota> rotaciona(List<Rota> rotas, double angulo){
		List<Rota> rotas2 = new ArrayList<Rota>();
		for(Rota r : rotas){
			rotas2.add(r.rotacionada(angulo));
		}
		return rotas2;
	}
	
	@Override
	public String toString() {
		if(pontos.isEmpty()) 
			return "";
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pontos.size()-1; i++) {
			sb.append(pontos.get(i).toString());
			sb.append("a");
		}
		sb.append(pontos.get(pontos.size()-1).toString());
		return sb.toString();
	}
	
	/**
	 * O intuito desta classe é sobrescrever o método equals de Coordenada para ser usado
	 * nos métodos getCoordenadaAproximada() e containsAproximado(). Esta implementação do método equals
	 * reconhece Coordenadas próximas como se fossem iguais.
	 */
	private class PontoAproximado extends Ponto{
		public PontoAproximado(int x, int y) {
			super(x, y);
		}
		//método equals aproximado (basta estar perto pra ser considerado "igual")
		private int proximidade;
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Ponto){
				Ponto outra = (Ponto)obj;
				return super.isPerto(outra, proximidade);
			}else{
				return false;
			}
		}
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Rota){
			Rota rota = (Rota)obj;
			return getPontos().equals(rota.getPontos());
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int code = getInicio().hashCode() + getFim().hashCode();
		return code;
	}
	
	@Override
	protected Object clone(){
		Rota r = new Rota();
		for(Ponto p : this.pontos)
			r.add(p);
		return r;
	}
	
	public void limpar(){
		pontos.clear();
	}
}