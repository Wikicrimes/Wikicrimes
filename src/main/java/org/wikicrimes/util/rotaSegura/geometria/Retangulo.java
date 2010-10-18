package org.wikicrimes.util.rotaSegura.geometria;

import java.awt.Rectangle;

import com.sun.java.swing.plaf.gtk.GTKConstants.WidgetType;

public class Retangulo extends Poligono {

	public Retangulo(Rectangle r){
		Ponto nw = new Ponto(r.x, r.y);
		Ponto ne = new Ponto(r.x + r.width, r.y);
		Ponto se = new Ponto(r.x + r.width, r.y + r.height);
		Ponto sw = new Ponto(r.x, r.y + r.height);
		pontos = new Ponto[]{nw, ne, se, sw};
	}
	
	public int norte(){
		return pontos[0].y;
	}
	
	public int sul(){
		return pontos[2].y;
	}
	
	public int leste(){
		return pontos[2].x;
	}
	
	public int oeste(){
		return pontos[0].x;
	}
	
	public Rectangle getRectangle(){
		int x = oeste();
		int y = norte();
		int width = leste()-oeste();
		int height = sul()-norte();
		return new Rectangle(x, y, width, height);
	}
	
	public void expandir(int tamanho){
		pontos[0].x -= tamanho;
		pontos[0].y -= tamanho;
		pontos[1].x += tamanho;
		pontos[1].y -= tamanho;
		pontos[2].x += tamanho;
		pontos[2].y += tamanho;
		pontos[3].x -= tamanho;
		pontos[3].y += tamanho;
	}
	
	public static Rectangle expandir(Rectangle retangulo, int tamanho){
		Retangulo r = new Retangulo(retangulo);
		r.expandir(tamanho);
		return r.getRectangle();
	}
	
}
