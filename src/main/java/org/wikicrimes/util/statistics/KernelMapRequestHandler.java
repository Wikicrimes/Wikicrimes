package org.wikicrimes.util.statistics;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.wikicrimes.util.ServletUtil;
import org.wikicrimes.util.kernelmap.KernelMap;
import org.wikicrimes.util.kernelmap.PropertiesLoader;
import org.wikicrimes.util.kernelmap.renderer.KMRFactory;
import org.wikicrimes.util.kernelmap.renderer.KernelMapRenderer;
import org.wikicrimes.util.statistics.Param.Application;

public class KernelMapRequestHandler {

	private final static String KERNEL_MAP_IMAGES = "KERNEL_MAP_IMAGES"; //imagem renderizada do mapa de kernel
	private final static String KERNEL_MAP_OBJECTS = "KERNEL_MAP_OBJECTS"; //objeto MapaKernel
	
	private final static int DEFAULT_NODE_SIZE = PropertiesLoader.getInt("kernelmap.nodesize");
	private final static int DEFAULT_BANDWIDTH = PropertiesLoader.getInt("kernelmap.bandwidth");
	
	private ServletContext context;
	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public KernelMapRequestHandler(HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		this.request = request;
		this.response = response;
		this.context = context;
		this.session = request.getSession();
	}
	
	public Image generateKernelMap(){
		Rectangle limitesPixel = Param.getLimitesPixel(request);

		EventsRetriever pointsRetriever = EventsRetriever.getEventsRetriever(request, context);
		List<Point> points = pointsRetriever.getPoints();
		int zoom = Param.getZoom(request);
		double bandwidth = getZoomDependantBandwidth(zoom);
//		/*DEBUG*/System.out.println("BANDWIDTH: " + bandwidth);
		KernelMap kernel = new KernelMap(DEFAULT_NODE_SIZE, bandwidth, limitesPixel, points);
		boolean isIE = ServletUtil.isClientUsingIE(request); 
		KernelMapRenderer renderer = KMRFactory.getDefaultRenderer(kernel, isIE);
		Image image = renderer.renderImage();

		saveInSession(kernel, image);

		return image;
	}
	
	private double getZoomDependantBandwidth(int zoom) {
//		double bandwidthKm = 1;
//		double bandwidth = PontoLatLng.distanceKmToPixels(bandwidthKm, zoom);
		return Math.max(DEFAULT_BANDWIDTH, DEFAULT_BANDWIDTH + (2*zoom-20));
	}
	
	@SuppressWarnings("unchecked")
	private void saveInSession(KernelMap kernel, Image image) {
		Application app = Param.getApplication(request);
		
		Map<Application,KernelMap> kernels = (Map<Application,KernelMap>)session.getAttribute(KERNEL_MAP_OBJECTS);
		if(kernels == null)
			kernels = new HashMap<Application, KernelMap>();
		kernels.put(app, kernel);
		session.setAttribute(KERNEL_MAP_OBJECTS, kernels);
		
		Map<Application,Image> images = (Map<Application,Image>)session.getAttribute(KERNEL_MAP_IMAGES);
		if(images == null)
			images = new HashMap<Application, Image>();
		images.put(app, image);
		session.setAttribute(KERNEL_MAP_IMAGES, images);
	}
	
	@SuppressWarnings("unchecked")
	public KernelMap getKernelMapFromSession() {
		Application app = Param.getApplication(request);
		Map<String,KernelMap> map = (Map<String,KernelMap>)session.getAttribute(KERNEL_MAP_OBJECTS);
		KernelMap kernel = map.get(app);
		return kernel;
	}
	
	@SuppressWarnings("unchecked")
	public Image getImageFromSession() {
		Application app = Param.getApplication(request);
		Map<String,Image> map = (Map<String,Image>)session.getAttribute(KERNEL_MAP_IMAGES);
		return map.get(app);
	}
	
	public void sendKernelMapInfo(double[][] dens) throws IOException{
		//manda a matriz de densidades gerada pelo KernelMap
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		
		//nodeSize
		out.println(DEFAULT_NODE_SIZE);
		out.println(DEFAULT_BANDWIDTH);

		//estatisticas
		KernelMap kernel = getKernelMapFromSession();
		if(kernel == null) {
			out.println("\n\n\n");
		}else {
			out.println(kernel.getMaxDens());
			out.println(kernel.getMediaDens());
			out.println(kernel.getMinDens()); 
		}
		
		//grid
		if(dens != null)
			for(double[] coluna : dens){
				for(double d : coluna){
					out.print(d + ",");
				}
				out.println();
			}
		out.close();
	}
	
}
