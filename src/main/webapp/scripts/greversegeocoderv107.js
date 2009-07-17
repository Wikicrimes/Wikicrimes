/*
   Copyright 2007-2008 Nico Goeminne (nicogoeminne@gmail.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/ 
 
/*
 * Class GReverseGeocoder v1.0.7
 *
 * This class is used to obtain reverse geocodes (addresses) for user specified points.
 * It does not use any caching mechanisme and is limited to 10 000 request per day.
 * It uses both GDirections as GClientGeocoder. The default country is set to "Belguim".
 * The country could be set using the GReverseGeocoder.setBaseCountrybefore method 
 * before any reverse geocoding is attempted.
 *
 * All data is obtained by use of the Google Maps API only! 
 * Therefore Reverse Geocoding is only supported for those countries in which Google Maps
 * supports Geocoding (GClientGeocoder) and Driving Directions (GDirections).
 * As a result only street level results are returned. (No house numbers)
 */

 /* Change Log 12/07/08
  *
  * - Removed extracting street name form html description, instead use the GDirection.getGeocode method.
  * - The getStreet() function is still needed to parse "street/number" or "number/street" streets.
  *
  */


 /* Change Log 5/01/08
  *
  * - Changed getStreet() function
  *     Now numbered roads are parsed correctly 
  * - Added the getPlacemarkProperty(placemark,propertyname) function
  *     The helper function is useful to investigate the resulting placemark.
  *     Since the placemark structure depends on the used data provider, the structure
  *     and property names may be different. This method searches the placemark for a certain
  *	property name. For example, if one needs the postal code:
  *
  *          GEvent.addListener(reversegeocoder, "load", 
  *            function(placemark) {
  *              var postalcodenumber = reversegeocoder.getPlacemarkProperty(placemark,"PostalCodeNumber");
  *              if (postalcodenumber != null) alert("Postal Code Number: " + postalcodenumber);
  *              else alert("Postal Code Number Unknown");
  *            }
  *          ); 
  *             
  */
 
 /* Change Log 18/11/07
  *
  * - Removed setBaseCountry() function
  *     The user of the script doesn't have to set the country anymore,
  *     instead the geocoder is provided by a bounding box (works in the 2.x api - fixed by google)
  * - Reported lng bug to google
  */
    
 
/*
 * Creates a new instance of a reversegeocoder.
 * Note that the reverseGeocode() method initiates a new query, 
 * which in turn triggers a "load" event once the query has finished loading.
 *
 * Additionally, the object contains thwo event listeners which you can intercept: 
 *  "load":  This event is triggered when the results of a reverse geocode query issued via 
 *           GReverseGeocoder.reverseGeocode() are available. Note that the reverseGeocode() method
 *           initiates a new query, which in turn triggers a "load" event once the query has finished
 *           loading. 
 *  "error": This event is triggered if a reverse geocode request results in an error. 
 *           Callers can use GReverseGeocoder.getStatus() to get more information about the error.
 *           In GReverseGeocoder v1.0 the getStatus() method proxies the GDirections.getStatus() behavior.
 */
function GReverseGeocoder(map) {
  // we don't actually need the map variable but to be sure the Google Map API
  // is loaded
  this.map=map;
  this.gdirections = new GDirections();
  this.geocoder = new GClientGeocoder();
  this.lastpoint=null;
  this.closestonroad=null;
  this.experimental=false;
  this.ad="";
  this.step=10;
  this.start=1;
  this.gdirectionsrefine = new GDirections();  
  GEvent.bind(this.gdirections, "error", this, this.handleError);
  GEvent.bind(this.gdirections, "load", this, this.processDirection);
  GEvent.bind(this.gdirectionsrefine, "error", this, this.handleError);
  GEvent.bind(this.gdirectionsrefine, "load", this, this.processDirectionRefine);
}

/*
 * This method issues a new reverse geocode query.
 * The parameter is a GLatLng point. 
 * If successful the Placemark object is passed to the user-specified 
 * listener. 
 *
 * E.g. var listener = GEvent.addListener(reversegeocoder,"load",
 *        function(placemark){
 *          alert("The reverse geocoded address is " + placemark.address);                               
 *        }
 *      );
 */
GReverseGeocoder.prototype.reverseGeocode = function(point){
  this.lastpoint = point;
  this.closestonroad = null;
  this.gdirections.clear();
  this.gdirections.loadFromWaypoints([point.toUrlValue(6),point.toUrlValue(6)],{getSteps: true, locale: "GB", getPolyline:true});
}

/*
 * Returns the status of the reverse geocode request.
 * In GReverseGeocoder v1.0 the getStatus() method proxies the GDirections.getStatus() behavior.
 * The returned object has the following form: {   code: 200   request: "directions" } 
 * The status code can take any of the values defined in GGeoStatusCode. (Since Google Map API 2.81)
 */
GReverseGeocoder.prototype.getStatus = function(){
  return this.gdirections.getStatus();
}


/*
 * Private implementation methods
 */

/*
 * This method is called when a GDirection error occurs,
 * or if the GReverseGeocoder does not find an address.
 */
GReverseGeocoder.prototype.handleError = function(){
  GEvent.trigger(this, "error");
}

/*
 * This method first gets the closest street using GDirections,
 * then tries to find addresses by combinating that street
 * with the supplied country using the GClientGeocoder. 
 * This can result in multiple addresses and is filtered 
 * by the GReverseGeocoder.getBestMatchingPlacemark() method.
 */
GReverseGeocoder.prototype.processDirection = function(){
  var source = this;
  // snap to road
  if ( this.gdirections.getPolyline() != null){
    this.closestonroad=this.gdirections.getPolyline().getVertex(0);
  }
  
  if (this.gdirections.getNumRoutes() != 0 ){
    var street = this.getStreet(this.gdirections.getGeocode(0).address);
    var sw = new GLatLng(Number(this.lastpoint.lat()) - 0.01, Number(this.lastpoint.lng()) - 0.01);
    var ne = new GLatLng(Number(this.lastpoint.lat()) + 0.01, Number(this.lastpoint.lng()) + 0.01);
    var bounds = new GLatLngBounds(sw, ne);
    this.geocoder.setViewport(bounds);
    
    this.geocoder.getLocations(street,
      function(response){
        var placemark = source.getBestMatchingPlacemark(response);
        if(placemark != null){
          if(source.experimental){
            source.ad = placemark.address;
            source.step=10;
            source.start=1;
            source.houseNumberSearch();
          }
          else{
            GEvent.trigger(source, "load", placemark);
          }
        }
        else{
          source.handleError();
        }
      }
    );
    
  }
}

/*
 * Finds the closest address towards the original point
 * form the resultset obtained by the GClientGeocoder request.
 * In GReverseGeocoder v1.0 an address is considered only if
 * it is within 1000m. If none of the addresses is in this range
 * the query will fail.
 */
 // TODO: the minimum Accuracy should be an optional parameter in
 // the GReverseGeocoder.
GReverseGeocoder.prototype.getBestMatchingPlacemark = function(response){
  if (!response || response.Status.code != 200) return null;
  var j = -1;
  var mindist = 1000000;
  for (var i = 0; i < response.Placemark.length; i++){
    var place = response.Placemark[i];
    var point = new GLatLng(place.Point.coordinates[1], place.Point.coordinates[0]);
    var temp = this.lastpoint.distanceFrom(point);
    if (temp < mindist) {
      j = i;
      mindist = temp;
    }
  }
  if(j < 0 ) return null;
  response.Placemark[j].RequestPoint = {
    "coordinates":[this.lastpoint.lng(),this.lastpoint.lat()]
  }
  response.Placemark[j].PointOnRoad = {
      "coordinates":[this.closestonroad.lng(),this.closestonroad.lat()]
  }
  response.Placemark[j].Distance=mindist;
  response.Placemark[j].DistanceOnRoad=this.closestonroad.distanceFrom(new GLatLng(response.Placemark[j].Point.coordinates[1], response.Placemark[j].Point.coordinates[0]));
  return response.Placemark[j];
}

   
 /*
 
 {	"id":"",
  	"address":"Binnenweg 41, 9050 Ledeberg, Gent, Belgium",
  	"AddressDetails":{
  		"Country":{
  			"CountryNameCode":"BE",
  			"AdministrativeArea":{
  				"AdministrativeAreaName":"Vlaams Gewest",
  				"SubAdministrativeArea":{
  					"SubAdministrativeAreaName":"Oost-Vlaanderen",
  					"Locality":{
  						"LocalityName":"Gent",
  						"DependentLocality":{
  							"DependentLocalityName":"Ledeberg",
  							"Thoroughfare":{
  								"ThoroughfareName":"Binnenweg 41"
  							},
  							"PostalCode":{
  								"PostalCodeNumber":"9050"
  							}
  						}
  					}
  				}
  			}
  		},
  		"Accuracy": 8
  	},
  	"Point":{
  		"coordinates":[3.739867,51.036155,0]
  	}
  }
 
 */

GReverseGeocoder.prototype.processDirectionRefine = function(){
  var nrgeocodes = this.gdirectionsrefine.getNumGeocodes();
  var j = -1;
  var mindist = 100;
  for (var i = 1; i < nrgeocodes; i++){
    var place = this.gdirectionsrefine.getGeocode(i);
    var point = new GLatLng(place.Point.coordinates[1], place.Point.coordinates[0]);
    if (place.AddressDetails.Accuracy == 8){
      var temp = this.lastpoint.distanceFrom(point);
      if (temp < mindist) {
        j = i;
        mindist = temp;
      }	
    }
  }
  if(j < 0 ) {
    if ( this.start + (24 * this.step) < 2000){
    	this.start = this.start + (25 * this.step);
    	this.houseNumberSearch();
    }
    else {
    	// house number above 2000 give up.
    	this.handleError();
    }	
  }
  else {
    if (this.step == 1){
        var placemark = this.gdirectionsrefine.getGeocode(j);
        placemark.RequestPoint = {
          "coordinates":[this.lastpoint.lng(),this.lastpoint.lat()]
        }
        placemark.PointOnRoad = {
          "coordinates":[this.closestonroad.lng(),this.closestonroad.lat()]
        }
               
        placemark.Distance=mindist;
        placemark.DistanceOnRoad=this.closestonroad.distanceFrom(new GLatLng(placemark.Point.coordinates[1], placemark.Point.coordinates[0]));
     	GEvent.trigger(this, "load", placemark);
    }
    else{
    	var place = this.gdirectionsrefine.getGeocode(j);
    	var nr = this.start + (j * this.step);
    	
    	
    	// var nr = place.address.split(",",1)[0].split(" ");
    	
    	
	/* belguim format is <street><nr> while the us format is <nr><street>*/ 
	//if ( ('' + Number(nr[nr.length-1])) == 'NaN') {
	//  nr =nr[0];
	//}
	//else {
	//  nr = nr[nr.length-1];
	//}
	this.start = nr - 10;
	this.step = 1;
	this.houseNumberSearch();
    }
  }
}

GReverseGeocoder.prototype.houseNumberSearch = function(){
  this.gdirectionsrefine.clear();
  this.gdirectionsrefine.loadFromWaypoints([
  	("" + (this.start + (0 * this.step))  + " ") + this.ad,
  	("" + (this.start + (1 * this.step))  + " ") + this.ad,
  	("" + (this.start + (2 * this.step))  + " ") + this.ad,
  	("" + (this.start + (3 * this.step))  + " ") + this.ad,
  	("" + (this.start + (4 * this.step))  + " ") + this.ad,
  	("" + (this.start + (5 * this.step))  + " ") + this.ad,
  	("" + (this.start + (6 * this.step))  + " ") + this.ad,
  	("" + (this.start + (7 * this.step))  + " ") + this.ad,
  	("" + (this.start + (8 * this.step))  + " ") + this.ad,
  	("" + (this.start + (9 * this.step))  + " ") + this.ad,
  	("" + (this.start + (10 * this.step))  + " ") + this.ad,
  	("" + (this.start + (11 * this.step))  + " ") + this.ad,
  	("" + (this.start + (12 * this.step))  + " ") + this.ad,
  	("" + (this.start + (13 * this.step))  + " ") + this.ad,
  	("" + (this.start + (14 * this.step))  + " ") + this.ad,
  	("" + (this.start + (15 * this.step))  + " ") + this.ad,
  	("" + (this.start + (16 * this.step))  + " ") + this.ad,
  	("" + (this.start + (17 * this.step))  + " ") + this.ad,
  	("" + (this.start + (18 * this.step))  + " ") + this.ad,
  	("" + (this.start + (19 * this.step))  + " ") + this.ad,
  	("" + (this.start + (20 * this.step))  + " ") + this.ad,
  	("" + (this.start + (21 * this.step))  + " ") + this.ad,
  	("" + (this.start + (22 * this.step))  + " ") + this.ad,
  	("" + (this.start + (23 * this.step))  + " ") + this.ad,
  	("" + (this.start + (24 * this.step))  + " ") + this.ad
  ],{getSteps: true, locale: "GB"});
}

/*
 * Gets the street name out of "street/number" or "number/street".
 */
GReverseGeocoder.prototype.getStreet = function(street){
  if(street == null) return null;
  if((street != null) && (street.indexOf("/") > 0)) {
    street = street.split("/");
    if (isNaN(street[0].charAt(1)) ){
      street = street[0];
    }
    else{
      street = street[1];
    }
  }
  return street;
}

GReverseGeocoder.prototype.setExperimentalHouseNumber = function (setting){
  this.experimental = setting;
}

GReverseGeocoder.prototype.getPlacemarkProperty = function (placemark,propertyname){
  for (var property in placemark) {
    if((property == propertyname)) {
      return String(placemark[property]);
    } else if (typeof(placemark[property]) == 'object') {
      var r = this.getPlacemarkProperty(placemark[property], propertyname);
      if (r != null) return r;
    }
  }
  return null;
}
