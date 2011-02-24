package org.wikicrimes.util.rotaSegura.logica.exceptions;

import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas;
import org.wikicrimes.util.rotaSegura.logica.modelo.VerticeRotas;

@SuppressWarnings("serial")
public class CantFindPath extends Exception{
	
	public GrafoRotas graph;
	public VerticeRotas start, end, orphan;

	public CantFindPath(GrafoRotas graph, VerticeRotas start, VerticeRotas end, VerticeRotas orphan) {
		super();
		this.graph = graph;
		this.start = start;
		this.end = end;
		this.orphan = orphan;
	}
}
