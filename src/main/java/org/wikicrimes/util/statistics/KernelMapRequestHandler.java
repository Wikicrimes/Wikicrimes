package org.wikicrimes.util.statistics;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.util.kernelmap.PropertiesLoader;

import br.com.wikinova.heatmaps.KernelMap;
import br.com.wikinova.heatmaps.renderer.DiscreteRainbowKMR;
import br.com.wikinova.heatmaps.renderer.KernelMapRenderer;

public class KernelMapRequestHandler {

	private final static int DEFAULT_NODE_SIZE = PropertiesLoader.getInt("kernelmap.nodesize");
	private final static int DEFAULT_BANDWIDTH = PropertiesLoader.getInt("kernelmap.bandwidth");

	private HttpServletRequest request;
	private List<Point> points;
	private Rectangle pixelBounds;
	
	private KernelMap kernel;
	private KernelMapRenderer renderer;
	private Image image;
	private String booleanGrid;
	
	private KernelMapRequestHandler(HttpServletRequest request) {
		this.request = request;
	}
	
	public KernelMapRequestHandler(HttpServletRequest request, List<Point> points, Rectangle pixelBounds) {
		this(request);
		this.points = points;
		this.pixelBounds = pixelBounds;
		generateKernelMap();
	}
	
	public KernelMapRequestHandler(HttpServletRequest request, List<Point> points) {
//		this(request, points, Param.getPixelBounds(request));
		this(request, points, Param.getLatLngBounds(request).toPixel(Param.getZoom(request)));
	}
	
	private void generateKernelMap(){
//		int zoom = Param.getZoom(request);
//		float bandwidth = getZoomDependantBandwidth(zoom);
		float bandwidth = DEFAULT_BANDWIDTH;
		List<Point> fixedPoints = fixPointsFor180DegreesMeridian(points, pixelBounds, request);
		kernel = new KernelMap(DEFAULT_NODE_SIZE, bandwidth, pixelBounds, fixedPoints);
//		boolean isIE = ServletUtil.isClientUsingIE(request); 
//		renderer = KMRFactory.getDefaultRenderer(kernel, isIE);
		renderer = new DiscreteRainbowKMR(kernel, 0.65f);
		image = renderer.renderImage();
//		/*TESTE CENARIO*/TesteCenariosRotas.setResult("numCrimes", points.size());
//		/*TESTE CENARIO*/TesteCenariosRotas.setResult("densMedia", kernel.getMediaDens());
//		/*TESTE CENARIO*/TesteCenariosRotas.setResult("densMax", kernel.getMaxDens());
	}
	
	public void generateBooleanGrid() {
//		String bigString = renderer.booleanGrid();
//		booleanGrid = Util.compress(bigString.toString());
		booleanGrid = renderer.booleanGrid();
	}
	
	@SuppressWarnings("unused")
	private double getZoomDependantBandwidth(int zoom) {
		//zoom do googlemaps vai de 0 (longe) a 19 (perto)
//		double bandwidthKm = 1;
//		double bandwidth = PontoLatLng.distanceKmToPixels(bandwidthKm, zoom);
//		return Math.max(DEFAULT_BANDWIDTH, DEFAULT_BANDWIDTH + (2*zoom-20));
		int closeLimit = 10;
		int farLimit = 4;
		if(zoom > closeLimit) {
			return DEFAULT_BANDWIDTH;
		}else if(zoom < farLimit) {
			return 2*DEFAULT_BANDWIDTH;
		}else {
			double maxZoom = closeLimit;
			double minZoom = farLimit;
			double maxFactor = 2;
			double minFactor = 1;
			double factor = maxFactor - (maxFactor-minFactor)*(zoom-minZoom)/(maxZoom-minZoom);
			return factor*DEFAULT_BANDWIDTH;
		}
	}

	public KernelMap getKernel() {
		return kernel;
	}

	public Image getImage() {
		return image;
	}

	public String getBooleanGrid() {
		return booleanGrid;
	}
	
	private static List<Point> fixPointsFor180DegreesMeridian(List<Point> points, Rectangle bounds, HttpServletRequest request){
		if(bounds.x >= 0)
			return points;
		int zoom = Param.getZoom(request);
		int maxX = new PontoLatLng(0,180).toPixel(zoom).x; //x correspondente a 180 graus de longitude
		List<Point> fixedList = new ArrayList<Point>();
		int east = bounds.x + bounds.width;
		for(Point p : points){
			Point fixedPoint = p;
			if(p.x > east){
				int fixedX = p.x - maxX;
				fixedPoint = new Point(fixedX, p.y);
			}
			fixedList.add(fixedPoint);
		}
		return fixedList;
	}
	
}
