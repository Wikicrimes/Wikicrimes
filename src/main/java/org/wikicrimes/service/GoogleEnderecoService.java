package org.wikicrimes.service;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import org.wikicrimes.util.GoogleMapsData;

public class GoogleEnderecoService {
	public GoogleMapsData consultaRuaURL(String url) throws Exception{
		url = url.replace(' ', '+');
		
		URL googleService = new URL("http://maps.google.com/maps/geo?q=" + url + "&output=xml&key=ABQIAAAAFMKMB22peYEk-DnRBRjxZhRk8R8qbs4FIOFRgqzDQO3UVKhx7BTRb8DofYsZ7bJrhTwSbePuBeOFBQ");
		
		System.out.println("http://maps.google.com/maps/geo?q=" + url + "&output=xml&key=ABQIAAAAFMKMB22peYEk-DnRBRjxZhRk8R8qbs4FIOFRgqzDQO3UVKhx7BTRb8DofYsZ7bJrhTwSbePuBeOFBQ");
		
        URLConnection googleConnection = googleService.openConnection();
        GoogleMapsData coor = processaDadoRequisicao(new InputStreamReader(googleConnection.getInputStream()));
		return coor;
	}
	
	public GoogleMapsData consultaRuaCoordenadas(String coordenadas) throws Exception{
		URL googleService = new URL("http://maps.google.com/maps/geo?q=" + coordenadas + "&output=xml&key=ABQIAAAAFMKMB22peYEk-DnRBRjxZhRk8R8qbs4FIOFRgqzDQO3UVKhx7BTRb8DofYsZ7bJrhTwSbePuBeOFBQ");
		System.out.println("http://maps.google.com/maps/geo?q=" + coordenadas + "&output=xml&key=ABQIAAAAFMKMB22peYEk-DnRBRjxZhRk8R8qbs4FIOFRgqzDQO3UVKhx7BTRb8DofYsZ7bJrhTwSbePuBeOFBQ");
		
        URLConnection googleConnection = googleService.openConnection();
        
        return processaDadoRequisicaoEndereco(new InputStreamReader(googleConnection.getInputStream()));
	}
	
	private GoogleMapsData processaDadoRequisicaoEndereco(InputStreamReader isr) throws Exception{
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
	
	private GoogleMapsData processaDadoRequisicao(InputStreamReader isr) throws Exception{
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
