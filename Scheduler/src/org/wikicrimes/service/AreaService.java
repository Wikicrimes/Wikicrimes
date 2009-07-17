package org.wikicrimes.service;

import org.wikicrimes.model.Area;
import org.wikicrimes.model.Ponto;

public class AreaService {
	
	private static Area area;
	
	public static boolean pontoPertenceArea(Ponto ponto)
	{		
	        int j=0;
	        boolean oddNodes = false;
	        Double x = ponto.getLng();
	        Double y = ponto.getLat();
	        for (int i=0; i < area.getPontos().size(); i++) {
	          j++;
	          if (j == area.getPontos().size()) {j = 0;}
	          if (((area.getPontos().get(i).getLat() < y) && (area.getPontos().get(j).getLat() >= y))
	          || ((area.getPontos().get(j).getLat() < y) && (area.getPontos().get(i).getLat() >= y))) {
	            if ( area.getPontos().get(i).getLng() + (y - area.getPontos().get(i).getLat())
	            /  (area.getPontos().get(j).getLat()-area.getPontos().get(i).getLat())
	            *  (area.getPontos().get(j).getLng() - area.getPontos().get(i).getLng())<x ) {
	              oddNodes = !oddNodes;
	            }
	          }
	        }
	        return oddNodes;		
	}

	public static Area getArea() {
		return area;
	}

	public static void setArea(Area area) {
		Ponto ponto = new Ponto();
		ponto.setLat(area.getPontos().get(0).getLat());
		ponto.setLng(area.getPontos().get(0).getLng());
		area.getPontos().add(ponto);
		AreaService.area = area;
	}
}
