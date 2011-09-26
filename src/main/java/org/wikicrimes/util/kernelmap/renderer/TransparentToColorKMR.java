package org.wikicrimes.util.kernelmap.renderer;

import java.awt.Color;

import org.wikicrimes.util.kernelmap.KernelMap;

/**
 * Varia de transparente pra a cor passada como parametro
 */
public class TransparentToColorKMR extends CellBasedKMR{

	private final Color color;
	
	public TransparentToColorKMR(KernelMap kernel, Color color){
		super(kernel);
		this.color = color;
	}
	
	public TransparentToColorKMR(KernelMap kernel, Color color, float maxDensity){
		super(kernel, maxDensity);
		this.color = color;
	}
	
	public Color renderCell(float densidade){
		float alpha = (float)(densidade/MAX_DENSITY);
		if(alpha<0) alpha = 0;
		if(alpha>1) alpha = 1;
		return new Color(color.getRed(),color.getGreen(),color.getBlue(),(int)(alpha*255));
	}
	
}
