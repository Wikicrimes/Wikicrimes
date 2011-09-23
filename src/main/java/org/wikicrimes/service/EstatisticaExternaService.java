package org.wikicrimes.service;

import java.util.List;

import org.wikicrimes.model.EstatisticaExterna;
import org.wikicrimes.model.FonteExterna;

public interface EstatisticaExternaService extends GenericCrudService{
	public EstatisticaExterna getEstatisticaExterna(String mes,Long dp,String  tipoCrime);
	public double getTaxaCrescimento(EstatisticaExterna estatistica, Long dp);
	public int getCrescimento(EstatisticaExterna estatistica, Long dp);
	public int getRankDp(EstatisticaExterna e);
	public String getEstatisticaExternaResposta(String mes, double lng, double lat, String tipoCrime);
	List<FonteExterna> getDelegacias(double latitude, double longitude, double raio);
}
