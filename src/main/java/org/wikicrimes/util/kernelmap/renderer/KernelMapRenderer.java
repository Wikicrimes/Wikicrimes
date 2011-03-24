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
	
}
