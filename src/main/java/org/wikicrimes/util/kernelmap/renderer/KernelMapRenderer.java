package org.wikicrimes.util.kernelmap.renderer;

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.wikicrimes.util.ValueCompression;
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
		float max = threshold();
		StringBuilder s = new StringBuilder();
		for(short[] col : kernel.getDensityGrid()){
			for(short comprDens : col){
				float dens = ValueCompression.uncompressShort(comprDens);
				s.append(dens > max? "1" : "0");
			}
			s.append("\n");
		}
		return s.toString();
	}
	
	protected float threshold() {
		return kernel.getMaxDens() * 0.5f;
	}
	
}
