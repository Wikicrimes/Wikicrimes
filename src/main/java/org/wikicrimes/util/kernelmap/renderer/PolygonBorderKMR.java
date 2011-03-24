package org.wikicrimes.util.kernelmap.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import org.wikicrimes.util.kernelmap.HotspotContour;
import org.wikicrimes.util.kernelmap.KernelMap;
import org.wikicrimes.util.rotaSegura.geometria.Poligono;
import org.wikicrimes.util.rotaSegura.geometria.Segmento;

/**
 * Varia de transparente pra a cor passada como parametro
 */
public class PolygonBorderKMR extends KernelMapRenderer{

	
	private final Color color;
	private final HotspotContour contour;
	private final int strokeWidth;
	
	public PolygonBorderKMR(KernelMap kernel, Color color, double threshold, int strokeWidth){
		super(kernel);
		this.color = color;
		this.strokeWidth = strokeWidth;
		this.contour = new HotspotContour(kernel, threshold);
	}
	
	public PolygonBorderKMR(KernelMap kernel, Color color, double threshold, int strokeWidth, double maxDens){
		super(kernel);
		this.color = color;
		this.strokeWidth = strokeWidth;
		this.contour = new HotspotContour(kernel, threshold, maxDens);
	}
	
	@Override
	public Image renderImage(){
		//pinta no buffer
		Image buffer = new BufferedImage(kernel.getWidth(), kernel.getHeight(), imageType);
		Graphics2D g = (Graphics2D)buffer.getGraphics();
		g.setColor(color);
		Stroke stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g.setStroke(stroke);
		
		int xIni = kernel.getBounds().x;
		int yIni = kernel.getBounds().y;
		
		for(Poligono p : contour.getPolygons(kernel.getBounds())) {
			for(Segmento s : p.getArestas()) {
				int x1 = s.getInicio().x - xIni;
				int y1 = s.getInicio().y - yIni;
				int x2 = s.getFim().x - xIni;
				int y2 = s.getFim().y - yIni;
				g.drawLine(x1, y1, x2, y2);
			}
		}
		
		return buffer;
	}
	
}
