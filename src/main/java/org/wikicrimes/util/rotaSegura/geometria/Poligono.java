package org.wikicrimes.util.rotaSegura.geometria;

import java.awt.Rectangle;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class Poligono {

	protected Ponto[] pontos;
	
	protected Poligono(){
		
	}
	
	public Poligono(Ponto... pontos){
		if(pontos.length < 3)
			throw new InvalidParameterException("Um poligono tem q ter no minimo 3 vertices. " 
					+ pontos.length + " foram passados como parametro");
		this.pontos = pontos;
	}
	
	public Poligono(Collection<Ponto> pontos){
		if(pontos.size() < 3)
			throw new InvalidParameterException("Um poligono tem q ter no minimo 3 vertices. " 
					+ pontos.size() + " foram passados como parametro");
		this.pontos = pontos.toArray(new Ponto[pontos.size()]);
	}
	
	public List<Ponto> getVertices(){
		return Arrays.asList(pontos);
	}
	
	public List<Segmento> getArestas(){
		List<Segmento> lista = new ArrayList<Segmento>();
		for(int i=1; i<pontos.length; i++)
			lista.add(new Segmento(pontos[i-1], pontos[i]));
		lista.add(new Segmento(pontos[pontos.length-1], pontos[0]));
		return lista;
	}
	
	public boolean contem(Ponto p){
	    if(!bounds().contains(p))
	        return false;
	   
	    int numPoints = pontos.length;
	    boolean inPoly = false;
	    int j = numPoints-1;
	    for(int i=0; i < numPoints; i++) {
	        Ponto v1 = pontos[i];
	        Ponto v2 = pontos[j];
	        if (v1.x < p.x && v2.x >= p.x || v2.x < p.x && v1.x >= p.x)
	            if (v1.y + (p.x - v1.x) / (v2.x - v1.x) * (v2.y - v1.y) < p.y)
	                inPoly = !inPoly;
	        j = i;
	    }
	    return inPoly;
	}
	
	public Rectangle bounds(){
		int xMin = Integer.MAX_VALUE;
		int xMax = Integer.MIN_VALUE;
		int yMin = Integer.MAX_VALUE;
		int yMax = Integer.MIN_VALUE;
		for(Ponto p : pontos){
			xMin = Math.min(xMin, p.x);
			xMax = Math.max(xMax, p.x);
			yMin = Math.min(yMin, p.y);
			yMax = Math.max(yMax, p.y);
		}
		return new Rectangle(xMin, yMin, xMax-xMin, yMax-yMin);
	}
}
