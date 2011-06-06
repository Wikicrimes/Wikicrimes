package org.wikicrimes.util.rotaSegura.googlemaps;


@SuppressWarnings("serial")
public class DirectionsAPIRequestException extends RuntimeException{

	private StatusGMDirections status;

	public DirectionsAPIRequestException(StatusGMDirections status) {
		super("Erro na requisicao a api Directions do GoogleMaps. Status=" + status);
		this.status = status;
	}
	
	public StatusGMDirections getStatus() {
		return status;
	}
	
}
