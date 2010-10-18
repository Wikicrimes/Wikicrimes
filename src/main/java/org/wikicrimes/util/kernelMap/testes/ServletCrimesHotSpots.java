package org.wikicrimes.util.kernelMap.testes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Relato;
import org.wikicrimes.util.MapaChaveDupla;


/**
* Gera umas imagens de mapa de kernel grandes para cidades inteiras.
* Fiz pro Douglas.
* Não é pra ir pra produção.
* @author Victor
*/
public class ServletCrimesHotSpots extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

		response.setContentType("text/plain");
		OutputStream out = response.getOutputStream();
		
		String path = "/home/victor/Desktop/testes/rotas 2010.07.29/teste douglas/3/" + request.getParameter("cidade");

		// Lê os objetos serializados
		try {
			File f = new File(path);
			ObjectInputStream objectInputStream;
			objectInputStream = new ObjectInputStream(new FileInputStream(f));
			double[][] matriz = (double[][])objectInputStream.readObject();
			MapaChaveDupla<Integer, Integer, List<BaseObject>> mapa = (MapaChaveDupla<Integer, Integer, List<BaseObject>>)objectInputStream.readObject();
			
			String tamanho;
			
			double linhas = (matriz[0].length/45.0);
			double colunas = (matriz.length/45.0);
			
			int linhasInt = 0;
			int colunasInt = 0;
			
			if(linhas > (int)Math.floor(linhas)) 
				linhasInt = ((int)Math.floor(linhas)) + 1;
			
			if(colunas > (int)Math.floor(colunas)) 
				colunasInt = ((int)Math.floor(colunas)) + 1;
			
			out.write((request.getParameter("cidade") + " " + linhasInt +"x"+ colunasInt + "\n").getBytes());
			
			Date dateI;
			Date dateJ;
			for(int colunaQuadro = 0; colunaQuadro < matriz.length; colunaQuadro += 45) {
				for(int linhaQuadro = 0; linhaQuadro < matriz[colunaQuadro].length; linhaQuadro += 45) {
					out.write(("NOVO QUADRO\n").getBytes());
					
					List<List<BaseObject>> hotspots = getCrimesHotspotPorQuadro(mapa, matriz, colunaQuadro, linhaQuadro);
					
					for (List<BaseObject> list : hotspots) {
						
						for(int i = 0; i < list.size(); i++) {
							for(int j = 0; j < list.size(); j++) {
								
								if(list.get(i) instanceof Crime) {
									dateI = ((Crime)list.get(i)).getData();
								} else {
									dateI = ((Relato)list.get(i)).getDataHoraRegistro();
								}
								if(list.get(i) instanceof Crime) {
									dateJ = ((Crime)list.get(j)).getData();
								} else {
									dateJ = ((Relato)list.get(j)).getDataHoraRegistro();
								}
								
								if(dateJ.compareTo(dateI) > 0) {
									if(j < i) {
										BaseObject oI = list.remove(i);
										BaseObject oJ = list.remove(j);
										
										list.add(j, oI);
										list.add(i, oJ);
									}
								}
							}
						}
						
						String linha;
						Crime crime;
						Relato relato;
						
						for (BaseObject baseObject : list) {
							linha = "";
							if(baseObject instanceof Crime) {
								crime = (Crime)baseObject;
								if(crime.getUsuario() != null)
									linha = "CRIME|" + crime.getIdCrime() + "|" + crime.getChave() + "|" + crime.getData() + "|USUARIO_COMUM|" + crime.getUsuario().getIdUsuario();
								else
									linha = "CRIME|" + crime.getIdCrime() + "|" + crime.getChave() + "|" + crime.getData() + "|USUARIO_REDESOCIAL|" + crime.getUsuarioRedeSocial().getIdUsuarioRedeSocial();
								
							} else {
								relato = (Relato)baseObject;
								if(relato.getUsuario() != null)
									linha = "RELATO|" + relato.getIdRelato() + "|" + relato.getChave() + "|" + relato.getDataHoraRegistro() + "|USUARIO_COMUM|" + relato.getUsuario().getIdUsuario();
								else
									linha = "RELATO|" + relato.getIdRelato() + "|" + relato.getChave() + "|" + relato.getDataHoraRegistro() + "|USUARIO_REDESOCIAL|" + relato.getUsuarioRedeSocial().getIdUsuarioRedeSocial();
							}
							
							out.write((linha + "\n").getBytes());
						}
						
						if(list.size()>0)
							out.write(("\n").getBytes());
					}
					if(hotspots.size() == 0)
						out.write("\n".getBytes());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static List<List<BaseObject>> getCrimesHotspotPorQuadro(MapaChaveDupla<Integer, Integer, List<BaseObject>> mapa, double[][] matriz, int linhaInicial, int colunaInicial) {
		List<List<BaseObject>> hotspots = new ArrayList<List<BaseObject>>();
		boolean[][] matrizBooleana = new boolean[45][45];
		
		double somatorio = 0;
		int qtd = 0;
		
		for(int i = linhaInicial; i < linhaInicial+45; i++) {
			for(int j = colunaInicial; j < colunaInicial+45; j++) {
				if(i < matriz.length && j < matriz[i].length) {
					qtd++;
					somatorio += matriz[i][j];
				}
			}
		}
		
		double media = somatorio/qtd;
		
		for(int i = linhaInicial; i < linhaInicial+45; i++) {
			for(int j = colunaInicial; j < colunaInicial+45; j++) {
				if(i < matriz.length && j < matriz[i].length)
					if(matriz[i][j] >= media)
						matrizBooleana[i-linhaInicial][j-colunaInicial] = true;
			}
		}
		
		List<BaseObject> hotSpot;
		
		for(int i = 0; i < 45; i++) 
			for(int j = 0; j < 45; j++) 
				if(matrizBooleana[i][j] == true) {
					hotSpot = new ArrayList<BaseObject>();
					capturarHotspot(matrizBooleana, i, j, mapa, hotSpot, linhaInicial, colunaInicial);
					
					hotspots.add(hotSpot);
				}
		
		return hotspots;
	}
	
	public static void capturarHotspot(boolean[][] matriz, int i, int j, MapaChaveDupla<Integer, Integer, List<BaseObject>> mapa, List<BaseObject> hotSpot, int linhaInicial, int colunaInicial) {
		if(i >= 0 && i < matriz.length && j >= 0 && j < matriz[i].length)
			if(matriz[i][j] == true) {
				
				List<BaseObject> lista = mapa.get(i+linhaInicial,j+colunaInicial);
				if(lista != null)
					hotSpot.addAll(lista);
				
				matriz[i][j] = false;
				
				capturarHotspot(matriz, i+1, j, mapa, hotSpot,  linhaInicial,  colunaInicial);
				capturarHotspot(matriz, i-1, j, mapa, hotSpot,  linhaInicial,  colunaInicial);
				capturarHotspot(matriz, i, j+1, mapa, hotSpot,  linhaInicial,  colunaInicial);
				capturarHotspot(matriz, i, j-1, mapa, hotSpot,  linhaInicial,  colunaInicial);
			} 
	}
}