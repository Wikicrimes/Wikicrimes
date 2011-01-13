package org.wikicrimes.util.rotaSegura.testes.charts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.util.rotaSegura.logica.CalculoPerigo;

public class ResultsModel {

	public Map<Integer, Scenario> scenarios;
	
	static class Scenario{
		
		int id;
		String name;
		Definition definition;
		Results results;

		public Scenario(int id, String name) {
			this.id = id;
			this.name = name;
			results = new Results();
		}
		
		@Override
		public String toString() {
			return id + "-" + name;
		}
	}
	
	static class Definition{
		
		PontoLatLng center, origin, destine;
		int zoom;
		Date startDate, endDate;
		
		public Definition(PontoLatLng centro, PontoLatLng origem, PontoLatLng destino,
				int zoom, Date dataInicial, Date dataFinal) {
			this.center = centro;
			this.origin = origem;
			this.destine = destino;
			this.zoom = zoom;
			this.startDate = dataInicial;
			this.endDate = dataFinal;
		}
		
	}
	
	static class Results{
		
		List<Iteration> iterations = new ArrayList<Iteration>();
		Integer satisfactoryStop; //Primeira iteracao que gerou boa rota reconhecida pelo algoritmo.
		Integer firstImprovement; //Primeira iteracao gue gerou boa rota segundo avaliacao manual, e nao reconhecida pelo algoritmo.
		Double tolerance, minDistance, avgDensity, maxDensity;
		
	}
	
	static class Iteration{

		int idCenario, ordinality;
		double distanceSoFar, qualitySoFar, timeRun;
		double gmRequestsSoFar;

		public Iteration(int idCenario, double distanceSoFar, double qualitySoFar,
				double timeRun, double gmRequestsSoFar) {
			this.idCenario = idCenario;
			this.distanceSoFar = distanceSoFar;
			this.qualitySoFar = qualitySoFar;
			this.timeRun = timeRun;
			this.gmRequestsSoFar = gmRequestsSoFar;
		}
		
		@Override
		public String toString() {
			return idCenario + "-" + ordinality + "-" + qualitySoFar;
		}
		
	}
	
	static final Comparator<Scenario> fasterResultComparator = new Comparator<Scenario>(){
		@Override
		public int compare(Scenario a, Scenario b) {
			if(a.results.satisfactoryStop == null && b.results.satisfactoryStop == null)
				if(a.results.firstImprovement == null && b.results.firstImprovement == null)
					return 0;
				else if(a.results.firstImprovement == null)
					return +1;
				else if(b.results.firstImprovement == null)
					return -1;
				else
					return a.results.firstImprovement - b.results.firstImprovement;
			else if(a.results.satisfactoryStop == null)
				return +1;
			else if(b.results.satisfactoryStop == null)
				return -1;
			else
				return a.results.satisfactoryStop - b.results.satisfactoryStop;
		}
	};
	
	static final Comparator<Scenario> toleranceComparator = new Comparator<Scenario>(){
		@Override
		public int compare(Scenario a, Scenario b) {
			double diff = a.results.tolerance - b.results.tolerance;
			if(diff > 0)
				return +1;
			else if(diff < 0)
				return -1;
			else
				return 0;
		}
	};
	
	static final Comparator<Scenario> minDistanceComparator = new Comparator<Scenario>(){
		@Override
		public int compare(Scenario a, Scenario b) {
			double diff = a.results.minDistance - b.results.minDistance;
			if(diff > 0)
				return +1;
			else if(diff < 0)
				return -1;
			else
				return 0;
		}
	};
	
	static final Comparator<Scenario> dangerComparator = new Comparator<Scenario>(){
		@Override
		public int compare(Scenario a, Scenario b) {
			double dangerA = a.results.minDistance*a.results.avgDensity/a.results.maxDensity;
			double dangerB = b.results.minDistance*b.results.avgDensity/b.results.maxDensity;
			double diff = dangerA - dangerB;
			if(diff > 0)
				return +1;
			else if(diff < 0)
				return -1;
			else
				return 0;
		}
	};
	
	static final Comparator<Scenario> stopQualityComparator = new Comparator<Scenario>(){
		@Override
		public int compare(Scenario a, Scenario b) {
			int stopA = -1;
			int stopB = -1;
			if(a.results.satisfactoryStop == null && b.results.satisfactoryStop == null)
				if(a.results.firstImprovement == null && b.results.firstImprovement == null)
					return 0;
				else if(a.results.firstImprovement == null)
					return -1;
				else if(b.results.firstImprovement == null)
					return +1;
				else {
					stopA = a.results.firstImprovement; 
					stopB = b.results.firstImprovement;
				}
			else if(a.results.satisfactoryStop == null)
				return -1;
			else if(b.results.satisfactoryStop == null)
				return +1;
			else {
				stopA = a.results.satisfactoryStop;
				stopB = b.results.satisfactoryStop;
			}
			Iteration itA = a.results.iterations.get(stopA);
			Iteration itB = b.results.iterations.get(stopB);
			double diff = itA.qualitySoFar - itB.qualitySoFar;
			if(diff > 0)
				return +1;
			else if(diff < 0)
				return -1;
			else
				return 0;
		}
	};
	
	static final Comparator<Scenario> startQualityComparator = new Comparator<Scenario>(){
		@Override
		public int compare(Scenario a, Scenario b) {
			double aValue = a.results.iterations.get(0).qualitySoFar;
			double bValue = b.results.iterations.get(0).qualitySoFar;
			double diff = aValue - bValue;
			if(diff > 0)
				return +1;
			else if(diff < 0)
				return -1;
			else
				return 0;
		}
	};
	
}
