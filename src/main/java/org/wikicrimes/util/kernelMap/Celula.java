package org.wikicrimes.util.kernelMap;

import java.awt.Color;
import java.awt.Point;

/**
 * 
 * @author mairon
 *
 */
public class Celula {
	private int XEsq;

	private int XDir;

	private int YCima;

	private int YBaixo;

	private Color cor;
	
	private double estimativa;
	
	private Point [] arrayPontos;
	
	private int regiaoCodigo = 0;
	
	//X da posicao na matriz
	private int posicaoXMatriz;

	//Y da posicao na matriz
	private int posicaoYMatriz;
	
	/*private double longitude;	
	private double latitude;*/	
	
	//Matriz de todos as celulas
	public Celula [][] matrizCelulas;
	
	public String getPosicao(){
		return ("Limites da posição Clicada: XEsq: " + XEsq + " XDir: " + XDir + " YBaixo: " + YBaixo + " YCima: " + YCima );
	}
	
	public Celula(Color cor, int posicaoXMatriz, int posicaoYMatriz,Celula [][] matrizCelulas) {
		super();
		this.cor = cor;
		arrayPontos = new Point[0];
		this.posicaoXMatriz = posicaoXMatriz;
		this.posicaoYMatriz = posicaoYMatriz;
		this.matrizCelulas = matrizCelulas;
	}

	public int getXDir() {
		return XDir;
	}

	public void setXDir(int dir) {
		XDir = dir;
	}

	public int getXEsq() {
		return XEsq;
	}

	public void setXEsq(int esq) {
		/*longitude = MyMapKernel.convertePixelParaLongitude(esq);*/
		XEsq = esq;
	}

	public int getYBaixo() {
		return YBaixo;
	}

	public void setYBaixo(int baixo) {
		YBaixo = baixo;
	}

	public int getYCima() {
		return YCima;
	}

	public void setYCima(int cima) {
		/*latitude = MyMapKernel.convertePixelParaLatitude(cima);*/
		YCima = cima;		
	}

	public Color getCor() {
		return cor;
	}

	public void setCor(Color cor) {
		this.cor = cor;
	}
	
	public boolean isThere(int posicaoX, int posicaoY){
		if(XDir >= posicaoX && XEsq <= posicaoX && YBaixo >= posicaoY && YCima <= posicaoY){
			//cor = Color.BLUE;
//			getCentro();
			
			//pintaAoRedor();
			
//			System.out.println();
//			System.out.println();
//			
//			System.out.println(getPosicao());
//			
//			System.out.println();
//			System.out.println();
			return true;
		}else{
			return false;
		}		
	}
	
	private void pintaAoRedor() {
//		Pinta ao redor 
		if(getCelulaBaixo(1) != null)
			getCelulaBaixo(1).setCor(Color.CYAN);
		if(getCelulaCima(1) != null)
			getCelulaCima(1).setCor(Color.CYAN);
		if(getCelulaDir(1) != null)
			getCelulaDir(1).setCor(Color.CYAN);
		if(getCelulaEsq(1) != null)
			getCelulaEsq(1).setCor(Color.CYAN);
		if(getCelulaNordeste(1) != null)
			getCelulaNordeste(1).setCor(Color.CYAN);
		if(getCelulaNoroeste(1) != null)
			getCelulaNoroeste(1).setCor(Color.CYAN);
		if(getCelulaSudeste(1) != null)
			getCelulaSudeste(1).setCor(Color.CYAN);
		if(getCelulaSudoeste(1) != null)
			getCelulaSudoeste(1).setCor(Color.CYAN);
	}

	public Celula getThis(){
		//System.out.println("Centro: " + (XDir-((PainelKernel.pixelTamCelula * PainelKernel.incrementoCoordenadaX) / 2)) + " - " + (YBaixo-((PainelKernel.pixelTamCelula * PainelKernel.incrementoCoordenadaY ) / 2)));
		return this;
	}
	
	
	public Point getCentro(){
		return new Point(XDir-((Util.tamCelulaPixel * MyMapKernel.incrementoCoordenadaX) / 2), YBaixo-((Util.tamCelulaPixel * MyMapKernel.incrementoCoordenadaY ) / 2));
	}
	
	
	public void addPontos(int posicaoX, int posicaoY) {
		
		Point [] arrayPontoTemp = new Point [arrayPontos.length+1];
		for (int i = 0; i < arrayPontos.length; i++){
			arrayPontoTemp[i] = new Point(arrayPontos[i]);
		}
		
		//Adiciona o ultimo elemento
		arrayPontoTemp[arrayPontoTemp.length -1] = new Point(posicaoX,posicaoY);
		arrayPontos = arrayPontoTemp;
		
//		for(int i = 0; i < arrayPontos.length; i++){
//			System.out.println("Pontos na celula: "+arrayPontos[i].getX() + " - " + arrayPontos[i].getY());
//		}

	}
	
	public Celula getCelulaCima(int numCell){
		try{
			return matrizCelulas[posicaoXMatriz][posicaoYMatriz - numCell];	
		}catch (Exception e){
			return null;
		}
	}
	
	public Celula getCelulaBaixo(int numCell){
		try{
			return matrizCelulas[posicaoXMatriz][posicaoYMatriz + numCell];	
		}catch (Exception e){
			return null;
		}
	}
	
	public Celula getCelulaEsq(int numCell){
		try{
			return matrizCelulas[posicaoXMatriz - numCell][posicaoYMatriz];	
		}catch (Exception e){
			return null;
		}
	}
	
	public Celula getCelulaDir(int numCell){
		try{
			return matrizCelulas[posicaoXMatriz + numCell][posicaoYMatriz];	
		}catch (Exception e){
			return null;
		}
	}
	
	public Celula getCelulaNordeste(int numCell){
		try{
			return matrizCelulas[posicaoXMatriz + numCell][posicaoYMatriz - numCell];	
		}catch (Exception e){
			return null;
		}
	}
	
	public Celula getCelulaNoroeste(int numCell){
		try{
			return matrizCelulas[posicaoXMatriz - numCell][posicaoYMatriz - numCell];	
		}catch (Exception e){
			return null;
		}
	}
	
	public Celula getCelulaSudeste(int numCell){
		try{
			return matrizCelulas[posicaoXMatriz + numCell][posicaoYMatriz + numCell];	
		}catch (Exception e){
			return null;
		}
	}
	
	public Celula getCelulaSudoeste(int numCell){
		try{
			return matrizCelulas[posicaoXMatriz - numCell][posicaoYMatriz + numCell];	
		}catch (Exception e){
			return null;
		}
	}
	
	public int getPosicaoX() {
		return posicaoXMatriz;
	}

	public void setPosicaoX(int posicaoX) {
		this.posicaoXMatriz = posicaoX;
	}

	public int getPosicaoY() {
		return posicaoYMatriz;
	}

	public void setPosicaoY(int posicaoY) {
		this.posicaoYMatriz = posicaoY;
	}

	public Point[] getArrayPontos() {
		return arrayPontos;
	}

	public double getEstimativa() {
		return estimativa;
	}

	public void setEstimativa(double estimativa) {
		this.estimativa = estimativa;
	}

	public int getRegiaoCodigo() {
		return regiaoCodigo;
	}

	public void setRegiaoCodigo(int regiaoCodigo) {
		this.regiaoCodigo = regiaoCodigo;
	}

	/*public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}*/
	
	/*public static void main(String[] args) {
//		String s = Integer.toString(140,16);
//		String s2 = Integer.toHexString(15);

		Color color = Color.BLUE;
		System.out.println(color.getRGB());
		
		System.out.println(color.getRed());
		System.out.println(color.getGreen());
		System.out.println(color.getBlue());
		
		
		System.out.println("Hexa: " + Integer.toHexString(color.getRed()));
		System.out.println("Hexa: " + Integer.toHexString(color.getGreen()));
		System.out.println("Hexa: " + Integer.toHexString(color.getBlue()));
		
		System.out.println("\nHexa: " + convertColorParaHexa(0,0,255));
		
		
		
	}
	
	
	private static String convertColorParaHexa(int red, int green, int blue){
		String hexaRed = Integer.toHexString(red);
		String hexaGreen = Integer.toHexString(green);
		String hexaBlue = Integer.toHexString(blue);
		
		
		if(hexaRed.length() == 1){
			hexaRed = "0" + hexaRed;
		}

		if(hexaGreen.length() == 1){
			hexaGreen = "0" + hexaGreen;
		}
		
		if(hexaBlue.length() == 1){
			hexaBlue = "0" + hexaBlue;
		}
		
		return hexaRed + hexaGreen + hexaBlue;
	}*/
}
