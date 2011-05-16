package org.wikicrimes.util.kernelmap.renderer;

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.wikicrimes.util.kernelmap.KernelMap;

public abstract class KernelMapRenderer {

	protected final KernelMap kernel;
	protected final int imageType = BufferedImage.TYPE_INT_ARGB_PRE;	
	
	public KernelMapRenderer(KernelMap kernel){
		this.kernel = kernel;
	}
	
	protected KernelMapRenderer(){
		kernel = null;
	}
	
	public abstract Image renderImage();
	
	public String booleanGrid() {
		double max = threshold();
		StringBuilder s = new StringBuilder();
		for(double[] col : kernel.getDensityGrid()){
			for(double d : col){
				s.append(d > max*0.5? "1" : "0");
			}
			s.append("\n");
		}
		return s.toString();
	}
	
	protected double threshold() {
		return kernel.getMaxDens();
	}
	
}
