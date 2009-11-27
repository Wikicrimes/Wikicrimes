package org.wikicrimes.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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
        GoogleMapsData coor = processaDadoRequisicao(googleConnection.getInputStream());
		return coor;
	}
	
	public GoogleMapsData consultaRuaCoordenadas(String coordenadas) throws IOException, XMLStreamException, FactoryConfigurationError{
		URL googleService = new URL("http://maps.google.com/maps/geo?q=" + coordenadas + "&output=xml&key=ABQIAAAAFMKMB22peYEk-DnRBRjxZhRk8R8qbs4FIOFRgqzDQO3UVKhx7BTRb8DofYsZ7bJrhTwSbePuBeOFBQ");
		
        URLConnection googleConnection = googleService.openConnection();
        
        GoogleMapsData coor = processaDadoRequisicaoEndereco(googleConnection.getInputStream()); 
        return coor;
	}
	
	private GoogleMapsData processaDadoRequisicaoEndereco(InputStream isr) throws XMLStreamException, FactoryConfigurationError{
		GoogleMapsData gDados = new GoogleMapsData();
		
		XMLEventReader leitor = XMLInputFactory.newInstance().createXMLEventReader(isr);
		while(leitor.hasNext()){
			XMLEvent evento = leitor.nextEvent();
			
			if(evento.isStartElement()){
				if(evento.asStartElement().getName().getLocalPart().equalsIgnoreCase("ThoroughfareName")){
					evento = leitor.nextEvent();
					String str = evento.asCharacters().getData();
					
					gDados.setEndereco(str);
					break;
				}
			}
		}
		return gDados;
	}
	
	private GoogleMapsData processaDadoRequisicao(InputStream isr) throws XMLStreamException, FactoryConfigurationError{
		GoogleMapsData gDados = new GoogleMapsData();
			
		XMLEventReader leitor = XMLInputFactory.newInstance().createXMLEventReader(isr);
		while(leitor.hasNext()){
			XMLEvent evento = leitor.nextEvent();
			
			if(evento.isStartElement()){
				if(evento.asStartElement().getName().getLocalPart().equalsIgnoreCase("ThoroughfareName")){
					evento = leitor.nextEvent();
					String str = evento.asCharacters().getData();
					gDados.setEndereco(str);
					continue;//volta pro while
				}
				if(evento.asStartElement().getName().getLocalPart().equalsIgnoreCase("coordinates")){
					evento = leitor.nextEvent();
					String str = evento.asCharacters().getData();
					
					String vetor[] = str.split(",");
					
					gDados.setLongitude(Double.parseDouble(vetor[0]));
					gDados.setLatitude(Double.parseDouble(vetor[1]));
					break;
				}
			}
		}
		return gDados;
	}
}
