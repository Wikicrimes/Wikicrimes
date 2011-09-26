package org.wikicrimes.util.rotaSegura.logica;

import java.awt.Rectangle;
import java.util.PriorityQueue;
import java.util.Queue;

import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaPromissora;

public class AlternativasRandom extends GeradorDeAlternativasAbstrato{

	private static int numRoutes = 50;
	
	public AlternativasRandom(SafeRouteCalculator logica) {
		super(logica);
	}
	
	public Queue<Rota> getAlternativas(Rota rota){
		Perigo calcPerigo = logica.getCalculoPerigo();
		Ponto o = rota.getInicio();
		Ponto d = rota.getFim();
		Rectangle bounds = getAreaLimite(o, d);
		Queue<Rota> alternativas = new PriorityQueue<Rota>();
		for(int i=0; i<numRoutes; i++) {
			Ponto point = getRandomPoint(bounds);
			Rota route = new Rota(o, point, d);
			double perigo = calcPerigo.perigo(rota);
			route = new RotaPromissora(route, perigo);
			alternativas.offer(route);
		}
		return alternativas;
	}
	
	private Ponto getRandomPoint(Rectangle bounds) {
		double x = Math.random()*bounds.width + bounds.x;
		double y = Math.random()*bounds.height + bounds.y;
		return new Ponto(x,y);
	}
	
	public Rectangle getAreaLimite(Ponto origem, Ponto destino){
		double dist = Ponto.distancia(origem, destino);
		int inc = (int)dist/4 + 1;
		int x1 = Math.min(origem.x, destino.x) - inc;
		int x2 = Math.max(origem.x, destino.x) + inc;
		int y1 = Math.min(origem.y, destino.y) - inc;
		int y2 = Math.max(origem.y, destino.y) + inc;
		return new Rectangle(x1, y1, x2-x1, y2-y1);
	}
	
}
