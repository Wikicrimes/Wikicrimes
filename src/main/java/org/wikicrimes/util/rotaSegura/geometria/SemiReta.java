package org.wikicrimes.util.rotaSegura.geometria;


/**
 * Tipo uma reta, mas tem fim em um dos lados.
 */
public class SemiReta {
	
	private Segmento segm;

	/**
	 * @param extremidade - A extremidade da semi-reta
	 * @param outroPonto - Um outro ponto qualquer pelo qual a semi-reta passa
	 */
	public SemiReta(Ponto extremidade, Ponto outroPonto) {
		segm = new Segmento(extremidade, outroPonto);
	}
	
	public Ponto intersecao(SemiReta s){
		double a1 = coefA();
		double a2 = s.coefA();
		if(a1==a2)
			return null; //aproximadamente paralelas
		double b1 = coefB();
		double b2 = s.coefB();
		double x = (b2-b1) / (a1-a2);
		if(x > Integer.MAX_VALUE)
			return null; //aproximadamente paralelas
		double y = a1*x + b1;
		Ponto i = new Ponto(x,y);
		if(Ponto.distancia(i, getExtremidade()) < Ponto.distancia(i, getP()))
			return null; //fora da semi-reta "this"
		if(Ponto.distancia(i, s.getExtremidade()) < Ponto.distancia(i, s.getP()))
			return null; //fora da semi-reta "segm"
		return i;
	}
	
	public double angulo(){
		return segm.angulo();
	}
	
	public double coefA(){
		return segm.coefA();
	}
	
	public double coefB(){
		return segm.coefB();
	}
	
	public Ponto getExtremidade(){
		return segm.getInicio();
	}
	
	/**
	 * @return um ponto pelo qual a semi-reta passa, aquele que foi passado pro construtor, sem ser o O (extremidade)  
	 */
	public Ponto getP(){
		return segm.getFim();
	}
	
	/**
	 * ta errado
	 */
	public Ponto getPonto(double distancia){
		double alfa = coefA();
		double beta = coefB();
		double x0 = getExtremidade().x;
		double y0 = getExtremidade().y;
		double dist = distancia;
		double a = alfa*alfa + 1;
		double b = 2*(alfa*y0 - alfa*beta - x0);
		double c = x0*x0 + y0*y0 + 2*beta*y0 + beta*beta - dist*dist;
		
		double delta = b*b - 4*a*c;
		double x = 0;
		if(delta < 0){
			throw new AssertionError("delta < 0");
		}else if(delta == 0){
			x = -b/2*a; 
		}else{
//			x = (-b + Math.sqrt(delta))/2*a; //uma raiz
			x = (-b - Math.sqrt(delta))/2*a; //outra raiz
		}
		double y = alfa*x + beta;
		return new Ponto(x,y);
	}
	
}
