package org.wikicrimes.util.rotaSegura.googlemaps;

/**
 * Status da requisição de rota à api do GoogleMaps, inclusive possíveis erros.
 * 
 * @author victor
 * @link http://code.google.com/apis/maps/documentation/reference.html#GGeoStatusCode
 */
public enum StatusGMDirections {

	/**
	 * indicates the response contains a valid result.
	 */
	OK,

	/**
	 * indicates at least one of the locations specified in the requests's origin, destination, or waypoints could not be geocoded.
	 */
	NOT_FOUND,

	/**
	 * indicates no route could be found between the origin and destination.
	 */
	ZERO_RESULTS,

	/**
	 * indicates that too many waypointss were provided in the request The maximum allowed waypoints is 8, plus the origin, and destination. ( Google Maps Premier customers may contain requests with up to 23 waypoints.)
	 */
	MAX_WAYPOINTS_EXCEEDED,

	/**
	 * indicates that the provided request was invalid.
	 */
	INVALID_REQUEST,

	/**
	 * indicates the service has received too many requests from your application within the allowed time period.
	 */
	OVER_QUERY_LIMIT,

	/**
	 * indicates that the service denied use of the directions service by your application.
	 */
	REQUEST_DENIED,

	/**
	 * indicates a directions request could not be processed due to a server error. The request may succeed if you try again.
	 */
	UNKNOWN_ERROR;


	public static StatusGMDirections getStatus(String str) {
		return valueOf(str);
	}

}
