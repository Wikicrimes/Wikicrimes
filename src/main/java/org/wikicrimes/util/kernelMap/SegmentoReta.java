package org.wikicrimes.util.kernelMap;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Segmento de reta que compoe parte de uma rota
 * 
 * @author Mairon
 *
 */

public class SegmentoReta {
	private Ponto inicio;
	private Ponto fim;
	private static final double TOLERANCIA_PONTOS_IGUAIS = PropertiesLoader.getDouble("tol_ponto");
	
	public SegmentoReta(Ponto inicio, Ponto fim){
		this.inicio = inicio;
		this.fim = fim;
	}
	
	public SegmentoReta(int x1, int y1, int x2, int y2) {
		this.inicio = new Ponto(x1, y1);
		this.fim = new Ponto(x2, y2);
	}
	
	public SegmentoReta(Point ponto1, Point ponto2){
		this(new Ponto(ponto1), new Ponto(ponto2));
	}
	
	
	/**
	 * Um segmento de reta da origem até o fim de uma rota
	 */
	public SegmentoReta(Caminho rota){
		this(rota.getInicio(), rota.getFim());
	}
	
	
	/**
	 * Corrdenada do início deste segmento de reta
	 */
	public Ponto getInicio(){
		return inicio;
	}
	/**
	 * Corrdenada do fim deste segmento de reta
	 */
	public Ponto getFim(){
		return fim;
	}

	public double comprimento(){
		return Ponto.distancia(inicio, fim);
	}
	
	public List<Ponto> getPontos(){
		List<Ponto> pontos = new ArrayList<Ponto>();
		pontos.add(getInicio());
		pontos.add(getFim());
		return pontos;
	}
	
	/**
	 * Inverte o X e Y dos pontos deste SegmentoReta para gerar um novo. Obs: Não troca do início pelo fim. 
	 */
	public SegmentoReta invertido(){
		return new SegmentoReta(getInicio().invertido(), getFim().invertido());
	}
	
	public SegmentoReta rotacionado(double angulo){
		return new SegmentoReta(getInicio().rotacionado(angulo), getFim().rotacionado(angulo));
	}
	
	public double angulo(){
		double tg = coefA();
		double angulo = Math.atan(tg);
		angulo += Math.PI/2; //pra variar entre 0 e PI, em vez de -PI/2 e PI/2
		if(getInicio().getX() < getFim().getX())
			return angulo;
		else
			return angulo + Math.PI; //+180 graus, pq o fim ta antes do inicio 
	}
	
	/**
	 * O coeficiente A da equação reduzida da reta. A tangente do ângulo.
	 */
	public double coefA(){
		double catetoAdj = this.getFim().getX() - this.getInicio().getX();
		double catetoOposto = this.getFim().getY() - this.getInicio().getY();
		return catetoOposto/catetoAdj; 
	}
	
	/**
	 * O coeficiente B (termo independente) da equação reduzida da reta.
	 */
	public double coefB(){
		int x1 = getInicio().x;
		int x2 = getFim().x;
		int y1 = getInicio().y;
		int y2 = getFim().y;
		double numerador = (x2*y1 - x1*y2);
		double denominador = (x2 - x1); 
		if(denominador != 0)
			return numerador/denominador;
		else
			return (numerador>0)? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
	}
	
	public Ponto pontoMedio(){
		return Ponto.medio(inicio,fim);
	}
	
	/**
	 * Distância entre um ponto e a reta correspondente a este SegmentoReta. Não é do ponto ao segmento estritamente falando.
	 */
	public double distanciaPontoReta(Ponto p){
		//termos da equação gerl da reta
		double a = coefA();
		double b = -1;
		double c = coefB();
		
		//formula da distancia do ponto pra reta
		return Math.abs(a*p.x + b*p.y + c) / Math.sqrt(a*a + b*b);
	}
	
	public boolean passaPor(Ponto p){
		boolean retaContem = distanciaPontoReta(p) <= TOLERANCIA_PONTOS_IGUAIS;
		double comp = comprimento();
		boolean longeInicio = new SegmentoReta(inicio,p).comprimento() > comp;
		boolean longeFim = new SegmentoReta(p,fim).comprimento() > comp;
		return retaContem && !longeInicio && !longeFim;
	}
	
	public boolean passaPor(SegmentoReta segm){
		SegmentoReta inter = intersecaoParalelas(segm);
		if(inter == null)
			return false;
		boolean coincideIgual = inter.inicio.isPerto(segm.inicio) && inter.fim.isPerto(segm.fim);
		boolean coincideInvertido = inter.inicio.isPerto(segm.fim) && inter.fim.isPerto(segm.inicio); 
		return coincideIgual || coincideInvertido;
	}
	
	public Ponto intersecao(SegmentoReta segm){
		double a1 = coefA();
		double a2 = segm.coefA();
		if(a1==a2)
			return null; //paralelas
		double b1 = coefB();
		double b2 = segm.coefB();
		double x = (b2-b1) / (a1-a2);
		if(x > Integer.MAX_VALUE)
			return null; //interseção na reta muito longe (provavelmente fora do segmento)
		double y = a1*x + b1;
		Ponto p = new Ponto(x,y);
		double comp = comprimento();
		if(Ponto.distancia(p, inicio) > comp || Ponto.distancia(p, fim) > comp)
			return null; //fora do segmento "this"
		comp = segm.comprimento();
		if(Ponto.distancia(p, segm.inicio) > comp || Ponto.distancia(p, segm.fim) > comp)
			return null; //fora do segmento "segm"
		return p;
	}
	
	public SegmentoReta intersecaoParalelas(SegmentoReta segm){
		List<Ponto> ptsComum = new LinkedList<Ponto>();
		Ponto p1 = getInicio();
		Ponto p2 = getFim();
		Ponto q1 = segm.getInicio();
		Ponto q2 = segm.getFim();
		if(passaPor(q1))
			ptsComum.add(q1);
		if(passaPor(q2))
			ptsComum.add(q2);
		if(segm.passaPor(p1))
			ptsComum.add(p1);
		if(segm.passaPor(p2))
			ptsComum.add(p2);
		
		if(ptsComum.size() < 2){
			return null; //os segmentos nao coincidem
		}else if(ptsComum.size() > 2){
			if(comprimento() <= 2*TOLERANCIA_PONTOS_IGUAIS)
				return this;
			if(segm.comprimento() <= 2*TOLERANCIA_PONTOS_IGUAIS)
				return this;
			//exclui os pontos repetidos, tem q ficar só 2
			for(int i=0; i<ptsComum.size(); i++){
				for(int j=0; j<ptsComum.size(); j++){
					if(j==i) continue;
					if(Ponto.distancia( ptsComum.get(i), ptsComum.get(j) ) <= TOLERANCIA_PONTOS_IGUAIS){
						ptsComum.remove(j);
						i = 0;
						j = 0;
					}
				}
			}
		}
		if(ptsComum.size() != 2)
			throw new AssertionError();
		return new SegmentoReta(ptsComum.get(0), ptsComum.get(1));
	}
	
	@Override
	public String toString() {
		return getInicio() + "a" + getFim();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SegmentoReta){
			SegmentoReta segm = (SegmentoReta)obj;
			return this.getInicio().equals(segm.getInicio()) && this.getFim().equals(segm.getFim());
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return getInicio().hashCode() + getFim().hashCode();
	}
}
