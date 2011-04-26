package org.wikicrimes.util.rotaSegura.logica;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.wikicrimes.util.MapaChaveDupla;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.geometria.Segmento;
import org.wikicrimes.util.rotaSegura.logica.exceptions.CantFindPath;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaPromissora;


public class FilaRotasCandidatas{

	private PriorityQueue<Rota> rotasCandidatas;
//	/*TESTE*/private Queue<Rota> rotasCandidatas;
	private List<Rota> rotasJaUsadas;
	
	private Perigo calcPerigo;
	private GrafoRotas grafo;
	
	public FilaRotasCandidatas(Perigo calcPerigo, GrafoRotas grafo) {
		rotasCandidatas = new PriorityQueue<Rota>();
//		/*TESTE*/rotasCandidatas = new ArrayDeque<Rota>();
		rotasJaUsadas = new ArrayList<Rota>();
		this.calcPerigo = calcPerigo;
		this.grafo = grafo;
	}
	
//	public void add(Rota rota) {
//		rotasCandidatas.add(rota);
//	}
	
	public void addAll(Collection<Rota> rotas) {
		rotasCandidatas.addAll(rotas);
	}
	
	public Rota pop() {
		limpaUsadasNoTopo();
		Rota top = rotasCandidatas.poll();
		rotasJaUsadas.add(top);
		return top;
	}
	
	public boolean isEmpty() {
		limpaUsadasNoTopo();
		return rotasCandidatas.isEmpty();
	}
	
	private void limpaUsadasNoTopo() {
		Rota topo = rotasCandidatas.peek();
		while(topo != null && rotasJaUsadas.contains(topo)) {
			rotasCandidatas.poll();
			topo = rotasCandidatas.peek();
		}
	}
	
	
	public void reponderar(){
		List<Rota> temp = new LinkedList<Rota>();
		temp.addAll(rotasCandidatas);
		rotasCandidatas.clear();
		MapaChaveDupla<Ponto, Ponto, Double> memoizacao = new MapaChaveDupla<Ponto, Ponto, Double>();
		for(Rota r : temp){
			double pesoAtualizado = reponderarRotaPromissora(r, memoizacao);
			RotaPromissora rotaAtualizada = new RotaPromissora(r, pesoAtualizado);
			rotasCandidatas.add(rotaAtualizada);
		}
	}
	
	private double reponderarRotaPromissora(Rota rota, MapaChaveDupla<Ponto, Ponto, Double> memoizacao){
		Ponto o = grafo.getOrigem();
		Ponto d = grafo.getDestino();
		Ponto i = rota.getInicio();
		Ponto f = rota.getFim();
		Double perigoComeco = memoizacao.get(o, i);
		Double perigoFim = memoizacao.get(f, d);
		try{
			if(perigoComeco == null){
				if(o.equals(i)){
					perigoComeco = 0.0;
				}else{
					try{
						Rota comeco = grafo.melhorCaminho(o, i);
						perigoComeco = calcPerigo.perigo(comeco);
					}catch(InvalidParameterException e){
						perigoComeco = calcPerigo.perigo(new Segmento(o,i)); //FIXME Isto resolve bug com rotas quebradas por ter muitos waypoints.
																			 //Quando os pontos em q a rota foi dividida nao se ligam ao grafo
																			 //Problema: perde a informacao de perigo das outras partes da rota 
					}
				}
				memoizacao.put(o, i, perigoComeco);
			}
			if(perigoFim == null){
				if(f.equals(d)){
					perigoFim = 0.0;
				}else{
					try{
						Rota fim = grafo.melhorCaminho(f, d);
						perigoFim = calcPerigo.perigo(fim);
					}catch(InvalidParameterException e){
						perigoFim = calcPerigo.perigo(new Segmento(o,i)); //FIXME idem (ver fixme 15 linhas acima)
					}
				}
				memoizacao.put(f, d, perigoFim);
			}
			return perigoComeco + calcPerigo.perigo(rota) + perigoFim;
		}catch(CantFindPath e){
			/*DEBUG*/System.out.println("***NaoTemCaminhoException, inicio: " + e.start + ", fim: "+ e.end + ", vertice sem pai: "+ e.orphan);
			return Double.POSITIVE_INFINITY;
		}
	}
	
}
