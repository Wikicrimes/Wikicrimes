package org.wikicrimes.util.kernelMap.renderer;

import java.awt.Color;

import org.wikicrimes.util.NumerosUtil;
import org.wikicrimes.util.kernelMap.KernelMap;

/**
 * Varia de branco para vermelho
 *
 */
public class WhiteToColor extends CellBasedRenderer{

	private final double maxDensity;
	private final float[] colorComponents;
	
	public WhiteToColor(KernelMap kernel, Color color){
		super(kernel);
		this.maxDensity = kernel.getMaxDens();
		colorComponents = color.getColorComponents(null);
	}
	
	public Color render(double density){
		if(density == 0)
			return new Color(1,1,1,0);
		float x = (float)(1-density/maxDensity);
		float r = colorComponents[0];
//		r = 1-x-r+r*x;
		r = x*(1-r)+r;
		float g = colorComponents[1];
//		g = 1-x-g+g*x;
		g = x*(1-g)+g;
		float b = colorComponents[2];
//		b = 1-x-b+b*x;
		b = x*(1-b)+b;
		r = (float)NumerosUtil.limitar(r, 0, 1);
		g = (float)NumerosUtil.limitar(g, 0, 1);
		b = (float)NumerosUtil.limitar(b, 0, 1);
		return new Color(r,g,b);
	}
	
}
