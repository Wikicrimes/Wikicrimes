package org.wikicrimes.util.rotaSegura.googlemaps;

import java.util.ArrayList;
import java.util.List;

import org.wikicrimes.util.rotaSegura.geometria.Rota;

public class LongRouteRequest {

	private Rota potentialRoute;
	private List<Rota> parts;
	private int currentPart;
	private Rota realRoute;
	
	public LongRouteRequest(Rota route) {
		super();
		this.potentialRoute = route;
		this.parts = divideLongRoute(route);
		this.currentPart = -1;
		this.realRoute = new Rota();
	}
	
	private List<Rota> divideLongRoute(Rota r) {
		List<Rota> rotasDivididas = new ArrayList<Rota>();
		int tamanho = r.size();
		int max = ConstantesGM.DIRECTIONS_MAX_WAYPOINTS;
		if(tamanho > max){
			int numPedacos = tamanho / max;
			if(tamanho%max > 0) numPedacos++;
			for(int i=0; i<numPedacos; i+=max-1){
				Rota pedaco = new Rota(r.getPontos().subList(i, i+max));
				rotasDivididas.add(new Rota(pedaco));
			}
		}else{
			rotasDivididas.add(r);
		}
		return rotasDivididas;
	}
	
	public boolean isDone() {
		return currentPart == parts.size();
	}
	
	public void haveResponse(Rota realRoutePart) {
		realRoute.addFim(realRoutePart);
	}
	
	public Rota nextPart() {
		if(isDone())
			throw new AssertionError("it's done. there's no next part");
		currentPart++;
		return parts.get(currentPart);
	}
	
	public Rota getPotentialRoute() {
		return potentialRoute;
	}
	
	public Rota getRealRoute() {
		if(!isDone())
			throw new AssertionError("still has some parts to request");
		return realRoute;
	}
	
	public static boolean isTooLong(Rota route) {
		return route.size() > ConstantesGM.DIRECTIONS_MAX_WAYPOINTS;
	}
}
