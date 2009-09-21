package org.wikicrimes.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.wikicrimes.util.GoogleMapsData;

public class GoogleEnderecoService {
	public GoogleMapsData consultaRuaURL(String url) throws IOException, XMLStreamException, FactoryConfigurationError{
		url = url.replace(' ', '+');
		
		URL googleService = new URL("http://maps.google.com/maps/geo?q=" + url + "&output=xml&key=ABQIAAAAFMKMB22peYEk-DnRBRjxZhRk8R8qbs4FIOFRgqzDQO3UVKhx7BTRb8DofYsZ7bJrhTwSbePuBeOFBQ");
        URLConnection googleConnection = googleService.openConnection();
        GoogleMapsData coor = processaDadoRequisicao(new InputStreamReader(googleConnection.getInputStream()));
        //System.out.println(url);//para R. João Cordeiro tah dando uma lat e long errada!!!
		return coor;
	}
	
	private GoogleMapsData processaDadoRequisicao(InputStreamReader isr) throws IOException, XMLStreamException, FactoryConfigurationError{
		GoogleMapsData coordenada = new GoogleMapsData();
		
		XMLEventReader leitor = XMLInputFactory.newInstance().createXMLEventReader(isr);
		while(leitor.hasNext()){
			XMLEvent evento = leitor.nextEvent();
			if(evento.isStartElement()){
				if(evento.asStartElement().getName().getLocalPart().equalsIgnoreCase("coordinates")){
					evento = leitor.nextEvent();
					String str = evento.asCharacters().getData();
					
					String vetor[] = str.split(",");
					
					coordenada.setLongitude(Double.parseDouble(vetor[0]));
					coordenada.setLatitude(Double.parseDouble(vetor[1]));
				}
			}
		}
		return coordenada;
	}
}
