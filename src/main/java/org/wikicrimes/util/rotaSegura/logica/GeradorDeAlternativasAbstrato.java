package org.wikicrimes.util.rotaSegura.logica;

import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas;

import br.com.wikinova.heatmaps.KernelMap;

public abstract class GeradorDeAlternativasAbstrato {

	protected GrafoRotas grafo;
	protected KernelMap kernel;
	protected SafeRouteCalculator logica;
	protected Perigo calcPerigo;
	
	public GeradorDeAlternativasAbstrato(SafeRouteCalculator logica) {
		this.logica = logica;
		this.grafo = logica.getGrafo();
		this.kernel = logica.getKernel();
		this.calcPerigo = logica.getCalculoPerigo();
	}
	
}
