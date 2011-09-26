package org.wikicrimes.util.kernelmap.renderer;

public class ZoomDepenantKMR extends KMFunctionBender{

	private static final float ZOOM_MIN = 0;
	private static final float ZOOM_MAX = 19;
	private static final float MIN_BENDING = 0.2f;
	private static final float MAX_BENDING = 1.8f;
	private static final float BENDING_FACTOR = (MAX_BENDING-MIN_BENDING)/(ZOOM_MAX-ZOOM_MIN); //usado pra converter de zoom pra suavizacao
	
	public ZoomDepenantKMR(CellBasedKMR renderer, int zoom){
		super(renderer, zoomToBending(zoom));
	}
	
	private static float zoomToBending(float zoom){
		return BENDING_FACTOR * zoom + MIN_BENDING;
	}
	
}




