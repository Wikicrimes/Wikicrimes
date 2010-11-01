package org.wikicrimes.util.rotaSegura.googlemaps;


public class DirectionsAPIRequestException extends RuntimeException{

	private StatusGMDirections status;

	public DirectionsAPIRequestException(StatusGMDirections status) {
		super("Erro na requisicao à api Directions do GoogleMaps. Status=" + status);
		this.status = status;
	}
	
	public StatusGMDirections getStatus() {
		return status;
	}
	
}
