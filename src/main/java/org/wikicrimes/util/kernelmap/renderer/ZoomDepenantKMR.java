package org.wikicrimes.util.kernelmap.renderer;

public class ZoomDepenantKMR extends KMFunctionBender{

	private static final double ZOOM_MIN = 0;
	private static final double ZOOM_MAX = 19;
	private static final double MIN_BENDING = 0.2;
	private static final double MAX_BENDING = 1.8;
	private static final double BENDING_FACTOR = (MAX_BENDING-MIN_BENDING)/(ZOOM_MAX-ZOOM_MIN); //usado pra converter de zoom pra suavizacao
	
	public ZoomDepenantKMR(CellBasedKMR renderer, int zoom){
		super(renderer, zoomToBending(zoom));
	}
	
	private static double zoomToBending(double zoom){
		return BENDING_FACTOR * zoom + MIN_BENDING;
	}
	
}




