package org.wikicrimes.util.kernelmap.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import org.wikicrimes.util.kernelmap.KernelMap;

public abstract class CellBasedKMR extends KernelMapRenderer{

	protected final double MAX_DENSITY;
	
	public CellBasedKMR(KernelMap kernel){
		super(kernel);
		this.MAX_DENSITY = kernel.getMaxDens();
	}
	
	public CellBasedKMR(KernelMap kernel, double maxDensity){
		super(kernel);
		this.MAX_DENSITY = maxDensity;
	}
	
	@Override
	public Image renderImage(){
		
		Image buffer = new BufferedImage(kernel.getWidth(), kernel.getHeight(), imageType);
		Graphics g = buffer.getGraphics();
		
		if(kernel.getMaxDens() == 0) {
			g.setColor(new Color(0,0,0,0));
			g.fillRect(0, 0, kernel.getWidth(), kernel.getHeight());
			return buffer;
		}
		
		int node = kernel.getNodeSize();
		int cols = kernel.getCols();
		int rows = kernel.getRows();
		for(int i=0; i<cols; i++){
			for(int j=0; j<rows; j++){
				double density = kernel.getDensityGrid()[i][j];
				Color cor = renderCell(density);
				g.setColor(cor);
				g.fillRect(i*node, j*node, node, node);
			}
		}
		
		/*DEBUG*/System.out.println("CELULAS POR INTERVALO, " + Arrays.toString(count));
		return buffer;
	}
	/*DEBUG*/int[] count = new int[9];
	
	public abstract Color renderCell(double density);
	
}
