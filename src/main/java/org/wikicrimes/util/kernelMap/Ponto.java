package org.wikicrimes.util.kernelMap;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * x: longitude
 * y: latitude 
 */

public class Ponto extends Point{
	
	public Ponto(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Ponto(String string){
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
	
	public boolean isPerto(Ponto outra, int proximidade){
		double distancia = new SegmentoReta(this,outra).getComprimento();
		return distancia <= proximidade;
	}
	
	public static Ponto medio(Ponto ponto1, Ponto ponto2) {
		int x = (ponto1.x + ponto2.x) / 2;
		int y = (ponto1.y + ponto2.y) / 2;
		return new Ponto(x,y);
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
