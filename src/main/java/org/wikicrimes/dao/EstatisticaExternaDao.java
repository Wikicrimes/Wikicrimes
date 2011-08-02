package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.EstatisticaExterna;
import org.wikicrimes.model.FonteExterna;
import org.wikicrimes.util.DelegaciaOcorrencias;

public interface EstatisticaExternaDao extends GenericCrudDao {
	
	public EstatisticaExterna getEstatisticaExterna(String mes, String dp, String tipoCrime);
	public String getDP(double lat,double lng);
	public String getDPNome(double lat, double lng);
	public List<DelegaciaOcorrencias> getTopDPs(String mes);
	public int getCrescimento(EstatisticaExterna estatistica, String dp); 
	public int getRankDp(String dp, String mes);
	public double getTaxaCrescimento(EstatisticaExterna estatistica, String dp);
	//public FonteExterna getFonteExterna(long idFonte);
	public String getMesAnterior(String mes);
	public FonteExterna getFonteExternaPorDp(String dp);
}
