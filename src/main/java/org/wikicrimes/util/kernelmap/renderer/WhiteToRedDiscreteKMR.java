package org.wikicrimes.util.kernelmap.renderer;

import java.awt.Color;

import org.wikicrimes.util.kernelmap.KernelMap;

/**
 * Varia em 6 cores de branco para vermelho
 *
 */
public class WhiteToRedDiscreteKMR extends CellBasedKMR{

	
	//6 cores transparentes; branco -> vermelho
	private final static Color[] CORES6TR = {
		new Color(1,1,1,0f), //branco 
		new Color(1,.8f,.8f,.8f), 
		new Color(1,.6f,.6f,.8f),
		new Color(1,.4f,.4f,.8f),
		new Color(1,.2f,.2f,.8f),
		new Color(1f,0,0,.8f)  //vermelho
	};
	
	public WhiteToRedDiscreteKMR(KernelMap kernel, Color color){
		super(kernel);
	}
	
	public WhiteToRedDiscreteKMR(KernelMap kernel, float maxDensity){
		super(kernel, maxDensity);
	}
	
	//transparente, com conjunto de cores predefinido
	public Color renderCell(float densidade){
		double intervalo = MAX_DENSITY/CORES6TR.length;  //intervalo entre os níveis de densidade que terao cores diferentes (para dividir a densidade em faixas que terao cores diferentes)
		int faixa = (int)(densidade/intervalo); //em que faixa se encontra esta densidade (o valor da faixa corresponde a uma posicao do vetor de cores)
		if(faixa >= CORES6TR.length) faixa = CORES6TR.length-1; //se extrapolar, assume o valor maior
		return CORES6TR[faixa];
	}
	
	
	
}
