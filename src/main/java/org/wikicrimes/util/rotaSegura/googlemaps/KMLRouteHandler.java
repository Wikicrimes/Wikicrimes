package org.wikicrimes.util.rotaSegura.googlemaps;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.util.ServletUtil;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;

public class KMLRouteHandler {

	public static Document getKML(Rota rota, int zoom) throws IOException{
		Document kml = null;
		try {
			URL url = ServiceURLBuilder.getUrlKML(rota, zoom);
			String kmlStr = ServletUtil.requestText(url);
			SAXBuilder builder = new SAXBuilder();
			kml = builder.build(new StringReader(kmlStr));
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return kml;
	}
	
	public static Document getKML(List<Rota> rotas, int zoom) {
		try {
		
			//pega o KML da primeira rota
			Rota primeiraRota = rotas.get(0);
			Document resDoc = getKML(primeiraRota, zoom);
			Element resRoot = resDoc.getRootElement();
			Namespace ns = resRoot.getNamespace();
			
			for(int i=1; i<rotas.size(); i++) {
				Rota rota = rotas.get(i);
				
				//pega KML de cada rota subsequente
				Document doc = getKML(rota, zoom);
				Element root = doc.getRootElement();
				Element tagDocument = root.getChild("Document", ns);
				
				//insere no KML da primeira rota
				tagDocument.detach();
				resRoot.addContent(tagDocument);
				
			}
			
			return resDoc;
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Rota getRoute(Document kml, int zoom) throws DirectionsAPIRequestException{

		StatusGMDirections status = getStatus(kml); 
		if(status != StatusGMDirections.OK) {
			throw new DirectionsAPIRequestException(status);
		}
		
		Element root = kml.getRootElement();
		Namespace ns = root.getNamespace();
		Element tagDocument = root.getChild("Document", ns);
		List<Element> placemarks = tagDocument.getChildren("Placemark", ns);
		
		for(Element placemark : placemarks) {
			Element name = placemark.getChild("name", ns);
			
			//acha placemark de nome "Trajeto"
			if(name.getText().equals("Trajeto")) {
				Element coordinates = placemark.getChild("GeometryCollection", ns).getChild("LineString", ns).getChild("coordinates", ns);
				String coordStr = coordinates.getText();
				
				//extrai coordenadas da string
				StringTokenizer st = new StringTokenizer(coordStr);
				List<PontoLatLng> latlngs = new ArrayList<PontoLatLng>();
				while(st.hasMoreTokens()) {
					String latlngStr = st.nextToken();
					String[] split = latlngStr.split(",");
					double lat = Double.valueOf(split[1]);
					double lng = Double.valueOf(split[0]);
					PontoLatLng p = new PontoLatLng(lat, lng);
					latlngs.add(p);
				}
				
				//constroi a rota a partir dos latlngs lidos
				List<Ponto> pontos = PontoLatLng.toPixel(latlngs, zoom);
				return new Rota(pontos);
				
			}
		}
		
		throw new AssertionError("nao achou 'Trajeto' no kml, mas o status estava OK");
		
	}
	
	public static StatusGMDirections getStatus(Document kml) {
		if(kml == null)
			return StatusGMDirections.UNKNOWN_ERROR;
		else
			return StatusGMDirections.OK;
	}
	
}
