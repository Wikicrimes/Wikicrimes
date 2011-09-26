package org.wikicrimes.util.kernelmap.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.wikicrimes.util.Util;
import org.wikicrimes.util.kernelmap.KernelMap;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;

/**
 * Varia de transparente pra a cor passada como parametro
 */
public class FloodBorderKMR extends KernelMapRenderer{
	
	private final Color color;

	private final float threshold;
	private final float maxDensity;
	
	public FloodBorderKMR(KernelMap kernel, Color color, float threshold, int strokeWidth){
		super(kernel);
		this.color = color;
		this.threshold = threshold;
		this.maxDensity = Float.NaN;
	}
	
	public FloodBorderKMR(KernelMap kernel, Color color, float threshold, int strokeWidth, float maxDens){
		super(kernel);
		this.color = color;
		this.threshold = threshold;
		this.maxDensity = maxDens;
	}
	
	@Override
	public Image renderImage(){
		
		Rectangle bounds = kernel.getBounds();
		Image buffer = new BufferedImage((int)bounds.getWidth(), (int)bounds.getHeight(), imageType);
		Graphics g = buffer.getGraphics();
		
		//mapa de kernel todo branco
		if(kernel.getMaxDens() == 0)
			return buffer;
		
		Ponto p1 = new Ponto(bounds.x, bounds.y);
		Ponto p2 = new Ponto(bounds.x + bounds.width, bounds.y + bounds.height);
		Point g1 = kernel.pixelParaGrid(p1);
		Point g2 = kernel.pixelParaGrid(p2);
		int cols = kernel.getCols();
		int rows = kernel.getRows();
		int xIni = Util.limit(g1.x, 0, cols);
		int xFim = Util.limit(g2.x, 0, cols);
		int yIni = Util.limit(g1.y, 0, rows);
		int yFim = Util.limit(g2.y, 0, rows);

		boolean[][] visto = new boolean[cols][rows];
		for(int i=xIni; i<xFim; i++){
			for(int j=yIni; j<yFim; j++){
				if(visto[i][j])
					continue;
				Point celula = new Point(i,j);
				if(!kernel.taNoHotspot(celula, threshold, maxDensity))
					continue;
				flood(visto, celula, g);
			}
		}
		
		return buffer;
	}
	
	private void flood(boolean[][] visto, Point celula, Graphics g){
		if(celula.x >= visto.length || celula.y >= visto[0].length || celula.x < 0 || celula.y < 0)
			return;
		if(visto[celula.x][celula.y])
			return;
		if(!kernel.taNoHotspot(celula, threshold, maxDensity)){
			fillCell(celula, g);
			return;
		}
		visto[celula.x][celula.y] = true;
		for(Direcao d : Direcao.values())
			flood(visto, celulaVizinha(celula, d), g);
	}
	
	private void fillCell(Point cell, Graphics g){
		int size = kernel.getNodeSize();
		Rectangle bounds = kernel.getBounds();
		int x = kernel.xGridParaPixel(cell.x) - bounds.x;
		int y = kernel.yGridParaPixel(cell.y) - bounds.y;
		g.setColor(color);
		g.fillRect(x, y, size, size);
	}
	
	private enum Direcao{
		NORTE, LESTE, SUL, OESTE;
	}
	
	private Point celulaVizinha(Point celula, Direcao dir){ 
		//celula vizinha na direcao passada como argumento
		switch(dir){
		case NORTE:
			return new Ponto(celula.x, celula.y-1);
		case SUL:
			return new Ponto(celula.x, celula.y+1);
		case LESTE:
			return new Ponto(celula.x-1, celula.y);
		case OESTE:
			return new Ponto(celula.x+1, celula.y);
		default:
			return null;
		}
	}
	
}
