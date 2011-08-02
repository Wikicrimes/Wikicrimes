package org.wikicrimes.service;

import org.wikicrimes.model.EstatisticaExterna;

public interface EstatisticaExternaService extends GenericCrudService{
	public EstatisticaExterna getEstatisticaExterna(String mes,String dp,String  tipoCrime);
	public double getTaxaCrescimento(EstatisticaExterna estatistica, String dp);
	public int getCrescimento(EstatisticaExterna estatistica, String dp);
	public int getRankDp(EstatisticaExterna e);
	public String getEstatisticaExternaResposta(String mes, double lng, double lat, String tipoCrime);
}
