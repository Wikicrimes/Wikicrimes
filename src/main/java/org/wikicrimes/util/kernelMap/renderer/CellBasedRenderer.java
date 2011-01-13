package org.wikicrimes.util.kernelMap.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.wikicrimes.util.kernelMap.KernelMap;

public abstract class CellBasedRenderer extends KernelMapRenderer{

	public CellBasedRenderer(KernelMap kernel){
		super(kernel);
	}
	
	@Override
	public Image renderImage(){
		
		Image buffer = new BufferedImage(kernel.getWidth(), kernel.getHeight(), imageType);
		Graphics g = buffer.getGraphics();
		int node = kernel.getNodeSize();
		int cols = kernel.getCols();
		int rows = kernel.getRows();
		for(int i=0; i<cols; i++){
			for(int j=0; j<rows; j++){
				double density = kernel.getDensidadeGrid()[i][j];
				Color cor = render(density);
				g.setColor(cor);
				g.fillRect(i*node, j*node, node, node);
			}
		}
		
		return buffer;
	}
	
	public abstract Color render(double density);
	
}
