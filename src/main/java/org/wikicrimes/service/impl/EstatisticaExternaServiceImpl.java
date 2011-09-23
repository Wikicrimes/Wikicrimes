package org.wikicrimes.service.impl;

import java.util.List;

import org.wikicrimes.dao.*;

import org.wikicrimes.model.*;
import org.wikicrimes.service.EstatisticaExternaService;

public class EstatisticaExternaServiceImpl extends GenericCrudServiceImpl implements EstatisticaExternaService{
	private EstatisticaExternaDao estatisticaExternaDao;
		
	public EstatisticaExternaDao getEstatisticaExternaDao() {
		return estatisticaExternaDao;
	}

	public void setEstatisticaExternaDao(EstatisticaExternaDao estatisticaExternaDao) {
		this.estatisticaExternaDao = estatisticaExternaDao;
	}

	public EstatisticaExterna getEstatisticaExterna(String mes,Long dp,String  tipoCrime){
		return estatisticaExternaDao.getEstatisticaExterna(mes, dp, tipoCrime);
	}

	public double getTaxaCrescimento(EstatisticaExterna estatistica, Long dp){
		 
		return estatisticaExternaDao.getTaxaCrescimento(estatistica, dp);
	}
	
	public int getCrescimento(EstatisticaExterna estatistica, Long dp){
		return estatisticaExternaDao.getCrescimento(estatistica, dp);
	}
	
	public int getRankDp(EstatisticaExterna e){
		return estatisticaExternaDao.getRankDp(e);
	}
	
	public String getMesAnterior(String mes){
		return estatisticaExternaDao.getMesAnterior(mes);
	}
	
	public String getEstatisticaExternaResposta(String mes, double lng,double lat,String tipoCrime){
		//Resposta
		Long dp ;
		double taxa; //taxa do crime
		int crescimento; //1 se cresceu, -1 de baixou  e 0 se permaneceu a msm quantidade com relação ao mês anterior
		int rank; //posição no rank de crimes
		String mesComparacao;
		String mesAnterior;
		FonteExterna fonte= new FonteExterna();
		double latDp;
		double lngDp;
		String resposta="";
		//Obtém Delegacia através a latitude e longitude
		dp = estatisticaExternaDao.getDP(lat, lng);
		String nomeDP = estatisticaExternaDao.getDPNome(lat, lng);
		nomeDP=nomeDP.replace("Ocorrências Mensais -", " ");
		nomeDP=nomeDP.replace("2011"," ");
		//Monta objeto EstatisticaExterna conforme parâmetros
		EstatisticaExterna ee = getEstatisticaExterna(mes, dp, tipoCrime);
				
		latDp = ee.getFonte().getLatitude();
		lngDp = ee.getFonte().getLongitude();
		taxa = getTaxaCrescimento(ee,dp);
		crescimento = getCrescimento(ee, dp);
		rank = getRankDp(ee);
		mesComparacao = ee.getMes();
		mesAnterior = getMesAnterior(ee.getMes());
		if(ee.getTipo().isEmpty()){
			resposta = "{\"dp\":\""+nomeDP+"\", \"lat\":"+latDp+", \"lng\":"+lngDp+", \"taxa\":"+taxa+", \"crescimento\":"+crescimento+", \"rank\":"+rank+", \"mes\":\""+mesComparacao+"\", \"mesAnterior\":\""+mesAnterior+"\"}";
		}else{
			resposta = "{\"dp\":\""+nomeDP+"\", \"lat\":"+latDp+", \"lng\":"+lngDp+", \"taxa\":"+taxa+", \"crescimento\":"+crescimento+", \"rank\":"+rank+", \"mes\":\""+mesComparacao+"\", \"mesAnterior\":\""+mesAnterior+"\", \"tipo\":\""+ee.getTipo()+"\"}";	
		}
		return resposta;
	}

	@Override
	public List<FonteExterna> getDelegacias(double latitude, double longitude,double raio) {
		List<FonteExterna> fontesExternas = estatisticaExternaDao.getDelegacias(latitude, longitude, raio);
		
		return fontesExternas;
	}


	
}
