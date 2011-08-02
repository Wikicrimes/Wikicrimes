package org.wikicrimes.service.impl;

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

	public EstatisticaExterna getEstatisticaExterna(String mes,String dp,String  tipoCrime){
		return estatisticaExternaDao.getEstatisticaExterna(mes, dp, tipoCrime);
	}

	public double getTaxaCrescimento(EstatisticaExterna estatistica, String dp){
		 
		return estatisticaExternaDao.getTaxaCrescimento(estatistica, dp);
	}
	
	public int getCrescimento(EstatisticaExterna estatistica, String dp){
		return estatisticaExternaDao.getCrescimento(estatistica, dp);
	}
	
	public int getRankDp(String dp, String mes, String tipoCrime){
		return estatisticaExternaDao.getRankDp(dp, mes);
	}
	
	public String getMesAnterior(String mes){
		return estatisticaExternaDao.getMesAnterior(mes);
	}
	
	public FonteExterna getFonteExterna(String dp){
		return estatisticaExternaDao.getFonteExternaPorDp(dp);
	}
	
	public String getEstatisticaExternaResposta(String mes, double lng,double lat,String tipoCrime){
		//Resposta
		String dp ;
		double taxa; //taxa do crime
		int crescimento; //1 se cresceu, -1 de baixou  e 0 se permaneceu a msm quantidade com relação ao mês anterior
		int rank; //posição no rank de crimes
		String mesComparacao;
		String mesAnterior;
		FonteExterna fonte= new FonteExterna();
		double latDp;
		double lngDp;
		String tipo ="";
		String resposta="";
		
		dp = estatisticaExternaDao.getDP(lat, lng);
		String nomeDP = estatisticaExternaDao.getDPNome(lat, lng);
		EstatisticaExterna ee = getEstatisticaExterna(mes, dp, tipoCrime);
		
		if(tipoCrime.isEmpty()){
			fonte =getFonteExterna(dp);
		}else{
			tipo = ee.getTipo();
			fonte = ee.getFonte();
		}
		
		
		latDp = fonte.getLatitude();
		lngDp = fonte.getLongitude();
		taxa = getTaxaCrescimento(ee,dp);
		crescimento = getCrescimento(ee, dp);
		rank = getRankDp(dp,  mes, tipoCrime);
		mesComparacao = ee.getMes();
		mesAnterior = getMesAnterior(ee.getMes());
		if(tipo.isEmpty()){
			resposta = "{\"dp\":\""+nomeDP+"\", \"lat\":"+latDp+", \"lng\":"+lngDp+", \"taxa\":"+taxa+", \"crescimento\":"+crescimento+", \"rank\":"+rank+", \"mes\":\""+mesComparacao+"\", \"mesAnterior\":\""+mesAnterior+"\"}";
		}else{
			resposta = "{\"dp\":\""+nomeDP+"\", \"lat\":"+latDp+", \"lng\":"+lngDp+", \"taxa\":"+taxa+", \"crescimento\":"+crescimento+", \"rank\":"+rank+", \"mes\":\""+mesComparacao+"\", \"mesAnterior\":\""+mesAnterior+"\", \"tipo\":\""+tipo+"\"}";	
		}
		return resposta;
	}

	
}
