package org.wikicrimes.util.kernelmap.renderer;

import java.awt.Color;

import org.wikicrimes.util.kernelmap.KernelMap;

/**
 * transparente, colorido, tipo visao de calor
 * de azul, passando por verde, amarelo ate vermelho
 */
public class RainbowKMR extends CellBasedKMR{

	public RainbowKMR(KernelMap kernel){
		super(kernel);
	}
	
	public RainbowKMR(KernelMap kernel, float maxDensity){
		super(kernel, maxDensity);
	}
	
	public Color renderCell(float densidade){
		if(densidade == 0)
			return new Color(1,1,1,0);
		final float alpha = .5f;
		float quarto = MAX_DENSITY/4;
		if(densidade <= quarto){
			float g = densidade/quarto;
			return new Color(0,g,1,alpha);
		}else if(densidade <= 2*quarto){
			densidade -= quarto;
			float b = 1 - densidade/quarto;
			return new Color(0,1,b,alpha);
		}else if(densidade <= 3*quarto){
			densidade -= 2*quarto;
			float r = densidade/quarto;
			return new Color(r,1,0,alpha);
		}else{
			densidade -= 3*quarto;
			float g = 1 - densidade/quarto;
			return new Color(1,g,0,alpha);
		}
	}
		
}
