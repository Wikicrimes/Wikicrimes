package org.wikicrimes.util.kernelMap.renderer;

import java.awt.Color;

import org.wikicrimes.util.kernelMap.KernelMap;

/**
 * Varia de branco para vermelho
 *
 */
public class WhiteToRed extends CellBasedRenderer{

	
	private final double MAX_DENSITY; 
	
	public WhiteToRed(KernelMap kernel){
		super(kernel);
		this.MAX_DENSITY = kernel.getMaxDens();
	}
	
	public Color render(double density){
//		if(density == 0)
//			return new Color(1,1,1,0);
		float greenBlue = (float)(1- density/MAX_DENSITY); //valor entre 0 e 1, inversamente proporcional a densidade
		if(greenBlue<0) greenBlue = 0;
		if(greenBlue>1) greenBlue = 1; //se extrapolar, assume o valor maior
		return new Color(1,greenBlue,greenBlue);
	}
	
}
