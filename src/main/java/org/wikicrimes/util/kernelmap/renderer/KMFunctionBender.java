package org.wikicrimes.util.kernelmap.renderer;


import java.awt.Color;


/**
 * Distorce a função de densidade do "originalRenderer" pra aumentar ou diminuir 
 * a diferença entre as densidades altas e baixas.
 * 
 * Para "bending = 0", não há distorção
 * Para "bending > 0", as densidades baixas são enfatizadas  
 * Para "bending < 0", as densidades altas são enfatizadas
 */
public class KMFunctionBender extends CellBasedKMR{

	
	private CellBasedKMR originalRenderer;
	private double bending;
	
	public KMFunctionBender(CellBasedKMR renderer, double bending){
		super(renderer.kernel);
		this.originalRenderer = renderer;
		this.bending = bending;
	}
	
	@Override
	public Color renderCell(double density) {
		double newDensity = bend(density);
		return originalRenderer.renderCell(newDensity);
	}
	
	private double bend(double density){
		return Math.pow(density/MAX_DENSITY, bending)*MAX_DENSITY;
	}
	
}




