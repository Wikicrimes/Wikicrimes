package org.wikicrimes.util.statistics;

import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.servlet.ConexaoBD;
import org.wikicrimes.util.kernelmap.LatLngBoundsGM;

public class WikimappsEventsRetriever extends EventsRetriever{

	
	private HttpServletRequest request;

	public WikimappsEventsRetriever(HttpServletRequest request) {
		super();
		this.request = request;
	}

	public List<Point> getPoints(){
		LatLngBoundsGM limitesLatLng = Param.getLimitesLatLng(request);
		int zoom = Param.getZoom(request);
		String url = request.getParameter("url");
		String tm = request.getParameter("tm");
		
		//recupera pontos no wikimapps de acordo com os limites e o tipo de marcador
		List<Point> pontos=new ArrayList<Point>();
		try {
			String query="select distinct(e.id),c.lt,c.lg from events e, coordinates c, apps a where e.apps_id=a.id and e.id = c.events_id and markers_id is not null and a.url='"+ url +"'";
			if(tm!=null && tm!="" && !tm.equals("undefined")){
				String[] tmArray=tm.split(",");
				for (String valor : tmArray){
				query+=" and e.markers_id!="+valor;
				}
			}
			//retorna todos os pontos dentro da southwest/northeast boundary
			if(limitesLatLng.leste > limitesLatLng.oeste){
				query+=" and (c.lg < " + limitesLatLng.leste + " or c.lg >= " + limitesLatLng.oeste + " ) and ( c.lt <= " + limitesLatLng.norte + " and c.lt >= " + limitesLatLng.sul + ")";
			}
			else {
				//retorna todos os pontos dentro da southwest/northeast boundary
				 //split over the meridian
				query+=" and (c.lg <= " + limitesLatLng.leste + " or c.lg >= " + limitesLatLng.oeste + " ) and ( c.lt <= " + limitesLatLng.norte + " and c.lt >= " + limitesLatLng.sul + ")";
			}
			ConexaoBD con=ConexaoBD.getConexaoBD();
			ResultSet rs =con.enviarConsulta(query);
			while(rs.next()){
				PontoLatLng p = new PontoLatLng(rs.getDouble(2),rs.getDouble(3));
				pontos.add(p.toPixel(zoom));
				
			}
			ConexaoBD.fechaConexao();
		
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return pontos;
	}
	
}
