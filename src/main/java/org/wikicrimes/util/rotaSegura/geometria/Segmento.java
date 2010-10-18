package org.wikicrimes.util.rotaSegura.geometria;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.wikicrimes.util.kernelMap.PropertiesLoader;

/**
 * Segmento de reta que compoe parte de uma rota
 */

public class Segmento {
	private Ponto inicio;
	private Ponto fim;
	private static final double TOLERANCIA_PONTOS_IGUAIS = PropertiesLoader.getDouble("tol_ponto");
	
	public Segmento(Ponto inicio, Ponto fim){
		this.inicio = inicio;
		this.fim = fim;
	}
	
	public Segmento(int x1, int y1, int x2, int y2) {
		this.inicio = new Ponto(x1, y1);
		this.fim = new Ponto(x2, y2);
	}
	
	public Segmento(Point inicio, Point fim){
		this(new Ponto(inicio), new Ponto(fim));
	}
	
	
	/**
	 * Um segmento de reta da origem até o fim de uma rota
	 */
	public Segmento(Rota rota){
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
	public Segmento invertido(){
		return new Segmento(getInicio().invertido(), getFim().invertido());
	}
	
	public Segmento rotacionado(double angulo){
		return new Segmento(getInicio().rotacionado(angulo), getFim().rotacionado(angulo));
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
	 * O coeficiente B da equação reduzida da reta. O termo independente.
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
		boolean longeInicio = new Segmento(inicio,p).comprimento() > comp;
		boolean longeFim = new Segmento(p,fim).comprimento() > comp;
		return retaContem && !longeInicio && !longeFim;
	}
	
	public boolean passaPor(Segmento segm){
		Segmento inter = intersecaoParalelas(segm);
		if(inter == null)
			return false;
		boolean coincideIgual = inter.inicio.isPerto(segm.inicio) && inter.fim.isPerto(segm.fim);
		boolean coincideInvertido = inter.inicio.isPerto(segm.fim) && inter.fim.isPerto(segm.inicio); 
		return coincideIgual || coincideInvertido;
	}
	
//	public Ponto intersecao(SegmentoReta segm){
//		double a1 = coefA();
//		double a2 = segm.coefA();
//		if(a1==a2)
//			return null; //aproximadamente paralelas
//		double b1 = coefB();
//		double b2 = segm.coefB();
//		double x = (b2-b1) / (a1-a2);
//		if(x > Integer.MAX_VALUE)
//			return null; //aproximadamente paralelas
//		double y = a1*x + b1;
//		Ponto p = new Ponto(x,y);
//		double comp = comprimento();
//		if(Ponto.distancia(p, inicio) > comp || Ponto.distancia(p, fim) > comp)
//			return null; //fora do segmento "this"
//		comp = segm.comprimento();
//		if(Ponto.distancia(p, segm.inicio) > comp || Ponto.distancia(p, segm.fim) > comp)
//			return null; //fora do segmento "segm"
//		return p;
//	}
	
	public Ponto intersecao(Segmento segm){
		if(!intersecta(segm))
			return null;

		if((this.isVertical() && segm.isVertical()) || (this.isHorizontal() && segm.isHorizontal()))
			return null;
		
		double a1 = coefA();
		double a2 = segm.coefA();
		
		if(a1 == a2)
			return null;
			
		double b1 = coefB();
		double b2 = segm.coefB();
		
		double x,y;
		if(this.isVertical()){
			x = inicio.x;
			y =  a2*x + b2;
		}else if(segm.isVertical()){
			x = segm.inicio.x;
			y =  a1*x + b1;
		}else{
			x = (b2-b1) / (a1-a2); //igualando as 2 equacoes da reta
			if(Math.abs(a1) < Math.abs(a2))
				y = a1*x + b1; //aplicando a equacao da reta de this
			else
				y = a2*x + b2; //aplicando a equacao da reta de segm
		}
//		System.out.println("x=" + x + ", y=" + y + ", a1=" + a1 + ", a2=" + a2 + ", b1=" + b1 + ", b2=" + b2 + ", this=" + this + ", segm=" + segm);
		return new Ponto(x,y);
	}
	
	public List<Ponto> intersecoes(Collection<Segmento> segms){
		Set<Ponto> pts = new HashSet<Ponto>();
		for(Segmento s : segms){
			Ponto p = intersecao(s);
			if(p != null)
				pts.add(p);
		}
		return new ArrayList<Ponto>(pts);
	}
	
	public boolean intersecta(Segmento segm){
		return Line2D.linesIntersect(inicio.x, inicio.y, fim.x, fim.y, 
				segm.inicio.x, segm.inicio.y, segm.fim.x, segm.fim.y);
	}
	
	public boolean intersectaAlgum(Collection<Segmento> segms){
		boolean intersectaAlgum = false;
		for(Segmento a : segms)
			intersectaAlgum |= intersecta(a);
		return intersectaAlgum;
	}	
	
	/**
	 * Não conta se a interseção for entre o inicio ou fim de um Segmento e o inicio ou fim do outro. 
	 */
	public boolean intersectaAlgumForaPontas(Collection<Segmento> segms){
		boolean intersectaAlgum = false;
		for(Segmento a : segms){
			if(a.inicio.equals(inicio) || a.inicio.equals(fim) || a.fim.equals(inicio) || a.fim.equals(fim))
				continue;
			intersectaAlgum |= intersecta(a);
		}
		return intersectaAlgum;
	}
	
	public Segmento intersecaoParalelas(Segmento segm){
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
		return new Segmento(ptsComum.get(0), ptsComum.get(1));
	}
	
	public boolean intersecta(Poligono poli){
		boolean intersecta = false;
		for(Segmento aresta : poli.getArestas())
			if(intersecta(aresta))
				intersecta = true;
		return intersecta;
	}
	
	public boolean intersectaAlgumPoli(Collection<Poligono> polis){
		for(Poligono poli : polis)
			if(intersecta(poli))
				return true;
		return false;
	}
	
	public boolean isCrescente(){
		return (fim.x > inicio.x && fim.y > inicio.y) || (fim.x < inicio.x && fim.y < inicio.y); 
	}
	
	public boolean isDecrescente(){
		return (fim.x > inicio.x && fim.y < inicio.y) || (fim.x < inicio.x && fim.y > inicio.y); 
	}
	
	public boolean isVertical(){
		return inicio.x == fim.x; 
	}
	
	public boolean isHorizontal(){
		return inicio.y == fim.y; 
	}
	
	@Override
	public String toString() {
		return getInicio() + "a" + getFim();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Segmento){
			Segmento segm = (Segmento)obj;
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
