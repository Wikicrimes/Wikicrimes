package org.wikicrimes.util.kernelMap;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * @author Mairon
 *
 */

public class Hotspot {
	private ArrayList<Celula> arrayListCelulas = new ArrayList<Celula>();
	private ArrayList<SegmentoReta> arrayListRotas = new ArrayList<SegmentoReta>();
	private ArrayList<Coordenada> arrayListVerticesTangencia = new ArrayList<Coordenada>();
	private ArrayList<Coordenada> arrayListVerticesTangenciaFolga = new ArrayList<Coordenada>();
	private ArrayList<Coordenada> arrayListVerticesNaoTangencia = new ArrayList<Coordenada>();
	private Coordenada [] coordenadasEntraSaiHotspot = new Coordenada[2];
	int numPixelFolga = 3;
	
	public Hotspot() {}
	
	public void adicionaCelula(Celula celula){
		this.arrayListCelulas.add(celula);
	}

	public void adicionaRota(SegmentoReta rotaSegura){
		this.arrayListRotas.add(rotaSegura);
	}
	
	public void adicionaVerticesTangencia(Coordenada coordenada){
		this.arrayListVerticesTangencia.add(coordenada);
	}	
	
	public ArrayList<Celula> getArrayListCelulas() {
		return arrayListCelulas;
	}
	
	public void encontraVerticesTangentes(){
		for (Celula celula : arrayListCelulas) {
			int norte = celula.getYCima();
			int sul = celula.getYBaixo();
			int oeste = celula.getXEsq();
			int leste = celula.getXDir();
			
			if (celula.getCelulaCima(1).getRegiaoCodigo() == 0 && celula.getCelulaDir(1).getRegiaoCodigo() == 0){
				arrayListVerticesTangencia.add(new Coordenada(leste, norte));
				arrayListVerticesTangenciaFolga.add(new Coordenada(leste + numPixelFolga, norte - numPixelFolga));
			}
			if (celula.getCelulaDir(1).getRegiaoCodigo() == 0 && celula.getCelulaBaixo(1).getRegiaoCodigo() == 0){
				arrayListVerticesTangencia.add(new Coordenada(leste, sul));
				arrayListVerticesTangenciaFolga.add(new Coordenada(leste + numPixelFolga, sul + numPixelFolga ));
			}
			if (celula.getCelulaBaixo(1).getRegiaoCodigo() == 0 && celula.getCelulaEsq(1).getRegiaoCodigo() == 0){
				arrayListVerticesTangencia.add(new Coordenada(oeste, sul));
				arrayListVerticesTangenciaFolga.add(new Coordenada(oeste - numPixelFolga, sul + numPixelFolga));
			}
			if (celula.getCelulaEsq(1).getRegiaoCodigo() == 0 && celula.getCelulaCima(1).getRegiaoCodigo() == 0){
				arrayListVerticesTangencia.add(new Coordenada(oeste, norte));
				arrayListVerticesTangenciaFolga.add(new Coordenada(oeste - numPixelFolga, norte - numPixelFolga));
			}
			
			if(celula.getCelulaNordeste(1).getRegiaoCodigo() == 0 &&
					celula.getCelulaCima(1).getRegiaoCodigo() != 0 &&
					celula.getCelulaDir(1).getRegiaoCodigo() != 0){
				arrayListVerticesNaoTangencia.add(new Coordenada(leste, norte));
			}
			if(celula.getCelulaSudeste(1).getRegiaoCodigo() == 0 &&
					celula.getCelulaDir(1).getRegiaoCodigo() != 0 &&
					celula.getCelulaBaixo(1).getRegiaoCodigo() != 0){
				arrayListVerticesNaoTangencia.add(new Coordenada(leste, sul));
			}
			if(celula.getCelulaSudoeste(1).getRegiaoCodigo() == 0 &&
					celula.getCelulaBaixo(1).getRegiaoCodigo() != 0 &&
					celula.getCelulaEsq(1).getRegiaoCodigo() != 0){
				arrayListVerticesNaoTangencia.add(new Coordenada(oeste, sul));
			}
			if(celula.getCelulaNoroeste(1).getRegiaoCodigo() == 0 &&
					celula.getCelulaEsq(1).getRegiaoCodigo() != 0 &&
					celula.getCelulaCima(1).getRegiaoCodigo() != 0){
				arrayListVerticesNaoTangencia.add(new Coordenada(oeste, norte));
			}
		}
	}

	public ArrayList<Coordenada> getArrayListVerticesTangencia() {
		return arrayListVerticesTangencia;
	}

	public ArrayList<Coordenada> getArrayListVerticesNaoTangencia() {
		return arrayListVerticesNaoTangencia;
	}

	public ArrayList<SegmentoReta> getArrayListRotas() {
		return arrayListRotas;
	}
	
	public Coordenada[] getCoordenadasEntraSaiHotspot() {
		return coordenadasEntraSaiHotspot;
	}

	public void setCoordenadasEntraSaiHotspot(
			Coordenada[] coordenadasEntraSaiHotspot) {
		this.coordenadasEntraSaiHotspot = coordenadasEntraSaiHotspot;
	}

	public ArrayList<Coordenada> getArrayListVerticesTangenciaFolga() {
		return arrayListVerticesTangenciaFolga;
	}
}
