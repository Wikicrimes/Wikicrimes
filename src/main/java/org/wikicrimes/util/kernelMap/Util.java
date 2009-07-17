package org.wikicrimes.util.kernelMap;
import java.awt.Point;
import java.util.ArrayList;

/**
 * 
 * @author mairon
 *
 */
public class Util {

	//SIMPOL
	//Tamanho X da matriz	
//	public static final int tamXMatriz = 180; //Simpol	
	//Tamanho Y da matriz
//	public static final int tamYMatriz = 180; //Simpol
	
	
	//Wikicrimes
//	Tamanho X da matriz	
	public int tamXMatriz ;//= 341;//152;//253; //Real = 760 (divide por tamCelulaPixel)
	
	//Tamanho Y da matriz
	public int tamYMatriz ;//= 125;//110;//183; //Real = 550 (divide por tamCelulaPixel)
	
	//Largura da banda (raio)
	public static final int larguraBanda = 30;
	
	//Tamanha em pixel da celula
	public static final int tamCelulaPixel = 3;
	
	//Numero de camadas de cor
	public static final int numCamadasCor = 5;
	
	//Armazena todos os pontos
	public static ArrayList <Point> arrayListPontos = new ArrayList<Point>();
	
	//Regioes da janela 
	public static final String CIMA = "CIMA";
	public static final String BAIXO= "BAIXO";
	public static final String ESQ= "ESQ";
	public static final String DIR = "DIR";
	
}
