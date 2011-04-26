package org.wikicrimes.util.rotaSegura.logica.exceptions;

import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas;

@SuppressWarnings("serial")
public class VertexNotInGraph extends RuntimeException {

	public Ponto vertex;
	public GrafoRotas grafo;

	public VertexNotInGraph(GrafoRotas grafo, Ponto vertex) {
		super();
		this.vertex = vertex;
		this.grafo = grafo;
	}
	
	@Override
	public String getMessage() {
		return vertex + " nao eh um vertice do grafo";
	}
	
	@Override
	public StackTraceElement[] getStackTrace() {
		// TODO Auto-generated method stub
		return super.getStackTrace();
	}
	
}
