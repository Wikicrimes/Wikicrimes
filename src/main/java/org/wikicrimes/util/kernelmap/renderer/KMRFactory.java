package org.wikicrimes.util.kernelmap.renderer;

import java.awt.Color;

import org.wikicrimes.util.kernelmap.KernelMap;

/**
 * 
 * PARAMETROS DE RENDERIZACAO (cor e estilo do mapa de kernel)
 *    - color (cor em hexa, de 0x000000 ateh 0xFFFFFF)
 *    - style (valores possiveis descritos a seguir)
 *         w: variando de branco ate uma cor
 *         t: variando de transparente ate uma cor
 *         b: apenas uma borda
 *         tb: estilos t e b sobrepostos
 *         wb: estilos w e b sobrepostos
 *    - param (parametro adicional usado no estilo b para limite da borda)
 * 
 * @author victor
 *
 */
public class KMRFactory {

	private static final float DEFAULT_BORDER_THRESHOLD = 0.3f; 
	
	public static KernelMapRenderer getRenderer(KernelMap kernel, Color color, String style) {
		try {
			float threshold = DEFAULT_BORDER_THRESHOLD;
			if(style.equals("w")) { //varia de branco pra cor
				return new WhiteToColorKMR(kernel, color);
			}else if(style.equals("t")) { //varia de transparente pra cor
				return new TransparentToColorKMR(kernel, color);
			}else if(style.equals("b")) { //bordas
				return new PolygonBorderKMR(kernel, color, threshold, 1);
			}else if(style.equals("tb")) { //merge do transparente com o bordas
				KernelMapRenderer r1 = new TransparentToColorKMR(kernel, color);
				KernelMapRenderer r2 = new PolygonBorderKMR(kernel, color, threshold, 1);
				return new MergedKMR(kernel, r1, r2);
			}else if(style.equals("wb")) { //merge do branco com o bordas
				KernelMapRenderer r1 = new WhiteToColorKMR(kernel, color);
				KernelMapRenderer r2 = new PolygonBorderKMR(kernel, color, threshold, 1);
				return new MergedKMR(kernel, r1, r2);
			}else {
				return new TransparentToColorKMR(kernel, Color.RED);
			}
		}catch(NullPointerException e) {
			return new TransparentToColorKMR(kernel, Color.RED);
		}
	}
	
	public static KernelMapRenderer getRenderer(KernelMap kernel, float maxDens, Color color, String style) {
		try {
			float threshold = DEFAULT_BORDER_THRESHOLD;
			if(style.equals("w")) { //varia de branco pra cor
				return new WhiteToColorKMR(kernel, color, maxDens);
			}else if(style.equals("t")) { //varia de transparente pra cor
				return new TransparentToColorKMR(kernel, color, maxDens);
			}else if(style.equals("b")) { //bordas
				return new PolygonBorderKMR(kernel, color, threshold, 1);
			}else if(style.equals("tb")) { //merge do transparente com o bordas
				KernelMapRenderer r1 = new TransparentToColorKMR(kernel, color, maxDens);
				KernelMapRenderer r2 = new PolygonBorderKMR(kernel, color, threshold, 1, maxDens);
				return new MergedKMR(kernel, r1, r2);
			}else if(style.equals("wb")) { //merge do branco com o bordas
				KernelMapRenderer r1 = new WhiteToColorKMR(kernel, color, maxDens);
				KernelMapRenderer r2 = new PolygonBorderKMR(kernel, color, threshold, 1, maxDens);
				return new MergedKMR(kernel, r1, r2);
			}else {
				return new TransparentToColorKMR(kernel, Color.RED, maxDens);
			}
		}catch(NullPointerException e) {
			return new TransparentToColorKMR(kernel, Color.RED, maxDens);
		}
	}
	
	public static KernelMapRenderer getDefaultRenderer(KernelMap kernel) {
		return new DiscreteRainbowKMR(kernel, 0.65f);
	}
	
	public static KernelMapRenderer getDefaultRenderer(KernelMap kernel, boolean isInternetExplorer) {
		return new DiscreteRainbowKMR(kernel, 0.65f);
	}
	
}
