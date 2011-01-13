package org.wikicrimes.util.kernelMap.renderer;

import java.awt.Color;

import org.wikicrimes.util.kernelMap.KernelMap;

/**
 * Varia de transparente pra a cor passada como parametro
 */
public class TransparentToColor extends CellBasedRenderer{

	
	private final double MAX_DENSITY;
	private final Color color;
	
	public TransparentToColor(KernelMap kernel, Color color){
		super(kernel);
		this.MAX_DENSITY = kernel.getMaxDens();
		this.color = color;
	}
	
	public Color render(double densidade){
		float alpha = (float)(densidade/MAX_DENSITY);
		if(alpha<0) alpha = 0;
		if(alpha>1) alpha = 1;
		return new Color(color.getRed(),color.getGreen(),color.getBlue(),(int)(alpha*255));
	}
	
}
