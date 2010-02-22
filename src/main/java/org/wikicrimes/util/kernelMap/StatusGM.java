package org.wikicrimes.util.kernelMap;

/**
 * Status da requisição de rota à api do GoogleMaps, inclusive possíveis erros.
 * 
 * @author victor
 * @link http://code.google.com/apis/maps/documentation/reference.html#GGeoStatusCode
 */
public enum StatusGM {

	/**
	 * No errors occurred; the address was successfully parsed and its geocode
	 * has been returned
	 */
	SUCCESS(200),

	/**
	 * A directions request could not be successfully parsed. For example, the
	 * request may have been rejected if it contained more than the maximum
	 * number of waypoints allowed.
	 */
	BAD_REQUEST(400),

	/**
	 * A geocoding, directions or maximum zoom level request could not be
	 * successfully processed, yet the exact reason for the failure is not
	 * known.
	 */
	SERVER_ERROR(500),

	/**
	 * The HTTP q parameter was either missing or had no value. For geocoding
	 * requests, this means that an empty address was specified as input. For
	 * directions requests, this means that no query was specified in the input.
	 */
	MISSING_QUERY(601),

	/**
	 * Synonym for MISSING_QUERY.
	 */
	MISSING_ADDRESS(601),

	/**
	 * No corresponding geographic location could be found for the specified
	 * address. This may be due to the fact that the address is relatively new,
	 * or it may be incorrect.
	 */
	UNKNOWN_ADDRESS(602),

	/**
	 * The geocode for the given address or the route for the given directions
	 * query cannot be returned due to legal or contractual reasons.
	 */
	UNAVAILABLE_ADDRESS(603),

	/**
	 * The GDirections object could not compute directions between the points
	 * mentioned in the query. This is usually because there is no route
	 * available between the two points, or because we do not have data for
	 * routing in that region.
	 */
	UNKNOWN_DIRECTIONS(604),

	/**
	 * The given key is either invalid or does not match the domain for which it
	 * was given.
	 */
	BAD_KEY(610),

	/**
	 * The given key has gone over the requests limit in the 24 hour period or
	 * has submitted too many requests in too short a period of time. If you're
	 * sending multiple requests in parallel or in a tight loop, use a timer or
	 * pause in your code to make sure you don't send the requests too quickly.
	 */
	TOO_MANY_QUERIES(620);

	private int cod;

	private StatusGM(int cod) {
		this.cod = cod;
	}

	public int getCod() {
		return cod;
	}

	public static StatusGM getStatus(int cod) {
		switch (cod) {
		case 200:
			return SUCCESS;
		case 400:
			return BAD_REQUEST;
		case 500:
			return SERVER_ERROR;
		case 601:
			return MISSING_QUERY;
		case 602:
			return UNKNOWN_ADDRESS;
		case 603:
			return UNAVAILABLE_ADDRESS;
		case 604:
			return UNKNOWN_DIRECTIONS;
		case 610:
			return BAD_KEY;
		case 620:
			return TOO_MANY_QUERIES;
		default:
			return null;
		}
	}

}
