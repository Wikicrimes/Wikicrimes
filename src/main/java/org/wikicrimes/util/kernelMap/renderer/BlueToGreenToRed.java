package org.wikicrimes.util.kernelMap.renderer;

import java.awt.Color;

import org.wikicrimes.util.kernelMap.KernelMap;

/**
 * transparente, colorido, tipo visao de calor
 * de azul, passando por verde, amarelo até vermelho
 */
public class BlueToGreenToRed extends CellBasedRenderer{

	
	private final double MAX_DENSITY; 
	
	public BlueToGreenToRed(KernelMap kernel, Color color){
		super(kernel);
		this.MAX_DENSITY = kernel.getMaxDens();
	}
	
	public Color render(double densidade){
//		if(densidade == 0)
//			return new Color(1,1,1,0);
		final float alpha = .3f;
		double quarto = MAX_DENSITY/4;
		if(densidade <= quarto){
			float g = (float)(densidade/quarto);
			return new Color(0,g,1,alpha);
		}else if(densidade <= 2*quarto){
			densidade -= quarto;
			float b = (float)(1 - densidade/quarto);
			return new Color(0,1,b,alpha);
		}else if(densidade <= 3*quarto){
			densidade -= 2*quarto;
			float r = (float)(densidade/quarto);
			return new Color(r,1,0,alpha);
		}else{
			densidade -= 3*quarto;
			float g = (float)(1 - densidade/quarto);
			return new Color(1,g,0,alpha);
		}
	}
	
	
	
}
