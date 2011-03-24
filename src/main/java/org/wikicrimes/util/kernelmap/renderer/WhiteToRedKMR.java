package org.wikicrimes.util.kernelmap.renderer;

import java.awt.Color;

import org.wikicrimes.util.kernelmap.KernelMap;

/**
 * Varia de branco para vermelho
 *
 */
public class WhiteToRedKMR extends CellBasedKMR{

	public WhiteToRedKMR(KernelMap kernel){
		super(kernel);
	}
	
	public WhiteToRedKMR(KernelMap kernel, double maxDensity){
		super(kernel, maxDensity);
	}
	
	public Color renderCell(double density){
//		if(density == 0)
//			return new Color(1,1,1,0);
		float greenBlue = (float)(1- density/MAX_DENSITY); //valor entre 0 e 1, inversamente proporcional a densidade
		if(greenBlue<0) greenBlue = 0;
		if(greenBlue>1) greenBlue = 1; //se extrapolar, assume o valor maior
		return new Color(1,greenBlue,greenBlue);
	}
	
}
