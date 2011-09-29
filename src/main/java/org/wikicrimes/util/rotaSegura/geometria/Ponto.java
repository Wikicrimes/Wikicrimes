package org.wikicrimes.util.rotaSegura.geometria;

import java.awt.Point;
import java.awt.Rectangle;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.wikicrimes.util.GeneralHashFunctionLibrary;
import org.wikicrimes.util.kernelmap.PropertiesLoader;

/**
 * x: longitude
 * y: latitude 
 */

@SuppressWarnings("serial")
public class Ponto extends Point{

	private static final double TOLERANCIA_PONTOS_IGUAIS = PropertiesLoader.getDouble("geometry.point_approximation");
	
	public Ponto(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Ponto(double x, double y){
		if(x > Integer.MAX_VALUE || x < Integer.MIN_VALUE || java.lang.Double.isNaN(x))
			throw new InvalidParameterException("X ("+ x +") nao pode ser representado num INT");
		if(y > Integer.MAX_VALUE || y < Integer.MIN_VALUE || java.lang.Double.isNaN(y))
			throw new InvalidParameterException("Y ("+ y +") nao pode ser representado num INT");
		
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
		GeneralHashFunctionLibrary hash = new GeneralHashFunctionLibrary();
		return (int)hash.JSHash(toString());
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
	
	public static boolean estaoAlinhados(Ponto p1, Ponto p2, Ponto p3){
		return p1.x*p2.y + p2.x*p3.y + p3.x*p1.y - p1.x*p3.y - p2.x*p1.y - p3.x*p2.y == 0;
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
	
	public static final Comparator<Ponto> ordemDeX = new Comparator<Ponto>(){
		public int compare(Ponto o1, Ponto o2) {
			return o1.x-o2.x;
		}
	};
	
	public static final Comparator<Ponto> ordemDeY = new Comparator<Ponto>(){
		public int compare(Ponto o1, Ponto o2) {
			return o1.y-o2.y;
		}
	};
	
	public static Rectangle getBounds(Collection<Ponto> pontos) {
		int menorX = Integer.MAX_VALUE;
		int menorY = Integer.MAX_VALUE;
		int maiorX = Integer.MIN_VALUE;
		int maiorY = Integer.MIN_VALUE;
		for(Ponto p: pontos){
			menorX = Math.min(menorX, (int)p.getX());
			menorY = Math.min(menorY, (int)p.getY());
			maiorX = Math.max(maiorX, (int)p.getX());
			maiorY = Math.max(maiorY, (int)p.getY());
		}
		return new Rectangle(menorX, menorY, maiorX - menorX, maiorY - menorY);
	}
}
