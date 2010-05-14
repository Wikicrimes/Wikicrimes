package org.wikicrimes.util.kernelMap;

import java.awt.Point;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * x: longitude
 * y: latitude 
 */

public class Ponto extends Point{

	private static final double TOLERANCIA_PONTOS_IGUAIS = PropertiesLoader.getDouble("tol_ponto");
	
	public Ponto(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Ponto(double x, double y){
		if(x > Integer.MAX_VALUE)
			throw new InvalidParameterException("X ("+ x +") é muito grande e não cabe num INT");
		if(y > Integer.MAX_VALUE)
			throw new InvalidParameterException("Y ("+ y +") é muito grande e não cabe num INT");
		
		this.x = (int)Math.round(x);
		this.y = (int)Math.round(y);
	}
	
	public Ponto(String string){
//		System.out.println("novo ponto:" + string);
		String valores[] = string.split(",");
		x = Integer.parseInt(valores[0]);
		y = Integer.parseInt(valores[1]);
	}
	
	public Ponto(Point ponto){
		this(ponto.x, ponto.y);
	}
	
	public Ponto clone(){
		return new Ponto(this.x, this.y);
	}
	
	public Ponto invertido(){
		return new Ponto(this.y, this.x);
	}

	public String toString() {
		return x + "," + y;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Ponto){
			Ponto outra = (Ponto)obj;
			return x == outra.x && y == outra.y; 
		}
		return false;
	}
	@Override
	public int hashCode() {
		return x+y;
	}
	
	public boolean isPerto(Ponto outro, double max){
		return Ponto.distancia(this,outro) <= max;
	}
	
	public boolean isPerto(Ponto outro){
		return isPerto(outro, TOLERANCIA_PONTOS_IGUAIS);
	}
	
	public static Ponto medio(Ponto ponto1, Ponto ponto2) {
		int x = (ponto1.x + ponto2.x) / 2;
		int y = (ponto1.y + ponto2.y) / 2;
		return new Ponto(x,y);
	}
	
	public static double distancia(Ponto ponto1, Ponto ponto2){
		return Point.distance(ponto1.x, ponto1.y, ponto2.x, ponto2.y);
	}
	
	public Ponto rotacionado(double angulo){
		int x1 = this.x;
		int y1 = this.y;
		double x2 = x1*Math.cos(angulo) - y1*Math.sin(angulo);
		double y2 = x1*Math.sin(angulo) + y1*Math.cos(angulo);
		return new Ponto((int)x2, (int)y2);
	}
	
	public static List<Ponto> rotaciona(List<Ponto> pontos, double angulo){
		List<Ponto> pontos2 = new ArrayList<Ponto>();
		for(Ponto p : pontos){
			pontos2.add(p.rotacionado(angulo));
		}
		return pontos2;
	}
	
}
