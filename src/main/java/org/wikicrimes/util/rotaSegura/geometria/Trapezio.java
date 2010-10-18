package org.wikicrimes.util.rotaSegura.geometria;

import java.security.InvalidParameterException;

public class Trapezio extends Poligono{

	public Trapezio(Ponto... vertices){
		if(vertices.length != 4)
			throw new InvalidParameterException("Um trapezio tem q ter 4 vertices. " + vertices.length + " foram passados como parametros");
	}
	
	public Ponto centro(){
		//TODO
		throw new UnsupportedOperationException();
	}
	
}
