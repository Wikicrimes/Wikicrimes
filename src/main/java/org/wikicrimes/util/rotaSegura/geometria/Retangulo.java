package org.wikicrimes.util.rotaSegura.geometria;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;

public class Retangulo extends Poligono {

	public Retangulo(Rectangle r){
		Ponto nw = new Ponto(r.x, r.y);
		Ponto ne = new Ponto(r.x + r.width, r.y);
		Ponto se = new Ponto(r.x + r.width, r.y + r.height);
		Ponto sw = new Ponto(r.x, r.y + r.height);
		pontos = new Ponto[]{nw, ne, se, sw};
	}
	
	public Retangulo(Collection<Ponto> pontos) {
		this(Ponto.getBounds(pontos));
	}
	
	public Retangulo(Ponto... pontos) {
		this(Arrays.asList(pontos));
	}
	
	public Retangulo(Ponto centro, int width, int height) {
		int w2 = width/2;
		int h2 = height/2;
		Ponto nw = new Ponto(centro.x-w2, centro.y-h2);
		Ponto ne = new Ponto(centro.x+w2, centro.y-h2);
		Ponto se = new Ponto(centro.x+w2, centro.y+h2);
		Ponto sw = new Ponto(centro.x-w2, centro.y+h2);
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
	
	public int largura(){
		return leste()-oeste();
	}
	
	public int altura(){
		return sul()-norte();
	}
	
	public int maiorLado() {
		return Math.max(largura(), altura());
	}
	
	public Rectangle getRectangle(){
		int x = oeste();
		int y = norte();
		int width = leste()-oeste();
		int height = sul()-norte();
		return new Rectangle(x, y, width, height);
	}
	
	public Retangulo expandir(int tamanho){
		Ponto no = new Ponto(oeste()-tamanho, norte()-tamanho);
		Ponto se = new Ponto(leste()+tamanho, sul()+tamanho);
		return new Retangulo(no,se);
	}
	
	public static Rectangle expandir(Rectangle retangulo, int tamanho){
		Retangulo r = new Retangulo(retangulo);
		return r.expandir(tamanho).getRectangle();
	}
	
}
