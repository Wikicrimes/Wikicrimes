package org.wikicrimes.util.kernelmap.renderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.wikicrimes.util.ValueCompression;
import org.wikicrimes.util.kernelmap.KernelMap;

/**
 * Cores predefinidas: azul, verde, amarelo, laranja, vermelho (e intermediarias).
 * Celulas distribuidas nas cores de forma balanceada. (o amarelo, cor do meio, esta na mediana das densidades e nao na media)
 * 
 * Parametro "ratio" controla tamanho dos intervalos baixos vs. altos.
 * O tamanho dos intervalos faz uma PG, "ratio" eh a razao da PG.
 * Ratio = 1, intervalos iguais
 * Ratio < 1, intervalos baixos maiores (mais azul)
 * Ratio > 1, intervalos altos maiores  (mais vermelho)
 */
public class DiscreteRainbowKMR extends CellBasedKMR{

	private float alpha = 0.4f;
	private Color transparent = new Color(1,1,1,0); 
	private Color[] colors = {
			new Color(0,0,1,alpha),
			new Color(0,0.5f,1,alpha),
			new Color(0,1,1,alpha),
			new Color(0,1,0.5f,alpha),
			new Color(0,1,0,alpha),
			new Color(0.5f,1,0,alpha),
			new Color(1,1,0,alpha),
			new Color(1,0.5f,0,alpha),
			new Color(1,0,0,alpha)
	};
	private float[] limits;
	
	public DiscreteRainbowKMR(KernelMap kernel){
		super(kernel);
		initLimits(1);
	}
	
	public DiscreteRainbowKMR(KernelMap kernel, float ratio) {
		super(kernel);
		initLimits(ratio);
	}
	
	public DiscreteRainbowKMR(KernelMap kernel, float ratio, float maxDensity){
		super(kernel, maxDensity);
		initLimits(ratio);
	}
	
	private void initLimits(double ratio) {
		int nColors = colors.length;
		Short[] cells = sort(kernel.getDensityGrid());
		
		limits = new float[nColors];
		if(cells.length == 0) return;
		
		double intervalWidth = (double)cells.length/nColors;
		if(ratio != 1) intervalWidth *=  nColors * (ratio-1) / (Math.pow(ratio, nColors)-1);
		int pos = 0;
		for(int i=0; i<nColors; i++) {
			pos += intervalWidth;
			if(pos >= cells.length)	pos = cells.length-1;
			limits[i] = ValueCompression.uncompressShort(cells[pos]);
			if(ratio != 1) intervalWidth *= ratio;
		}
//		limits[0] = cells[(int)((double)cells.length/nColors)]; //fixed width for dark blue
//		limits[1] = cells[(int)((double)cells.length/nColors)]; //fixed width for dark blue
//		/*DEBUG*/System.out.println("maior: " + ByteUtil.uncompress(cells[cells.length-1]));
//		/*DEBUG*/System.out.println("grid: " + ValueCompression.toStringUncompress(cells));
//		/*DEBUG*/System.out.println("limites: " + Arrays.toString(limits));
	}
	
	private Short[] sort(short[][] matrix) {
		int cols = matrix[0].length;
		int rows = matrix.length;
		List<Short> list = new ArrayList<Short>();
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				if(ValueCompression.uncompressShort(matrix[i][j]) > 0)
					list.add(matrix[i][j]);
			}
		}
		
		Collections.sort(list, new Comparator<Short>() {
			@Override
			public int compare(Short b1, Short b2) {
				float f1 = ValueCompression.uncompressShort(b1);
				float f2 = ValueCompression.uncompressShort(b2);
				if(f1 < f2)	return -1;
				if(f1 > f2)	return 1;
				else return 0;
			}
		});
		return list.toArray(new Short[list.size()]);
	}
	
	public Color renderCell(float densidade){
		if(densidade == 0)
			return transparent;
		
		for(int i=0; i<colors.length; i++) {
			if(densidade <= limits[i]) {
				return colors[i];
			}
		}
		
		return colors[colors.length-1];
	}
	
	@Override
	protected float threshold() {
		return limits[limits.length-3];
	}

}
