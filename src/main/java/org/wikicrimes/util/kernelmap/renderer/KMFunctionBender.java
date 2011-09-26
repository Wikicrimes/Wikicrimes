package org.wikicrimes.util.kernelmap.renderer;


import java.awt.Color;


/**
 * Distorce a função de densidade do "originalRenderer" pra aumentar ou diminuir 
 * a diferença entre as densidades altas e baixas.
 * 
 * Para "bending = 0", não ha distorção
 * Para "bending > 0", as densidades baixas sao enfatizadas  
 * Para "bending < 0", as densidades altas sao enfatizadas
 */
public class KMFunctionBender extends CellBasedKMR{

	
	private CellBasedKMR originalRenderer;
	private float bending;
	
	public KMFunctionBender(CellBasedKMR renderer, float bending){
		super(renderer.kernel);
		this.originalRenderer = renderer;
		this.bending = bending;
	}
	
	@Override
	public Color renderCell(float density) {
		float newDensity = bend(density);
		return originalRenderer.renderCell(newDensity);
	}
	
	private float bend(float density){
		return (float)Math.pow(density/MAX_DENSITY, bending)*MAX_DENSITY;
	}
	
}




