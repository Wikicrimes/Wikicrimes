package org.wikicrimes.util;

public class NumerosUtil {

	public static double roundToDecimals(double num, int casasDecimais) {
		int temp = (int)((num*Math.pow(10,casasDecimais)));
		return (((double)temp)/Math.pow(10,casasDecimais));
	}
	
	public static double milisegundosToSegundos(long milis, int casasDecimais){
		double seg = (double)milis/1000;
		return roundToDecimals(seg, casasDecimais);
	}


	/**
	 * Limita o "valor" entre "min" e "max". Se o valor for menor q min, retorna min. 
	 * Se for maior q max, retorna max. Caso contrário retorna "valor". 
	 */
	public static int limitar(int valor, int min, int max){
		return Math.min(max, Math.max(min, valor));
	}
	
	/**
	 * Limita o "valor" entre "min" e "max". Se o valor for menor q min, retorna min. 
	 * Se for maior q max, retorna max. Caso contrário retorna "valor". 
	 */
	public static double limitar(double valor, double min, double max){
		return Math.min(max, Math.max(min, valor));
	}
	
}
