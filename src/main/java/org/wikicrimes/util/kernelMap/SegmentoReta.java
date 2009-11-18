package org.wikicrimes.util.kernelMap;

import java.awt.Point;
import java.util.ArrayList;
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
	public SegmentoReta(Rota rota){
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

	public double getComprimento(){
		double quadradoCateto1 = Math.pow((getFim().getX() - getInicio().getX()), 2);
		double quadradoCateto2 = Math.pow((getFim().getY() - getInicio().getY()), 2);
		return Math.sqrt( quadradoCateto1 + quadradoCateto2 );
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
	
	public double getAngulo(){
		double tg = getCoefA();
		double angulo = Math.atan(tg);
		angulo += Math.PI/2; //pra variar entre 0 e PI, em vez de -PI/2 e PI/2
		if(getInicio().getX() < getFim().getX())
			return angulo;
		else
			return angulo + Math.PI; //+180 graus, pq o fim ta antes do inicio 
	}
	
	/**
	 * O coeficiente A da equação da reta. A tangente do ângulo.
	 */
	public double getCoefA(){
		double catetoAdj = this.getFim().getX() - this.getInicio().getX();
		double catetoOposto = this.getFim().getY() - this.getInicio().getY();
		return catetoOposto/catetoAdj; 
	}
	
	/**
	 * O coeficiente B (termo independente) da equação da reta.
	 */
	public double getCoefB(){
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
