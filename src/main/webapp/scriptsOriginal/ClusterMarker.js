/*
	ClusterMarker Version 1.0

	A marker manager for the Google Maps API
	http://googlemapsapi.martinpearman.co.uk/clustermarker
	
	Copyright Martin Pearman 2008.
	Last updated 14th January 2008

	This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

	You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
	
*/

function ClusterMarker($map, $options) {
	this._map=$map;
	this._mapMarkers=[];
	this._clusterMarkers=[];
	this._clusterMarkersEventListeners=[];
	if ($options) {
		if ($options.markers) {
			this.addMarkers($options.markers);
		}
		this.borderPadding=($options.borderPadding)?$options.borderPadding:256;
		this.intersectPadding=($options.intersectPadding)?$options.intersectPadding:0;
		this.clusteringEnabled=($options.clusteringEnabled===false)?false:true;
		if ($options.clusterMarkerIcon) {
			this._clusterMarker.icon=$options.clusterMarkerIcon;
		} else {
			this._clusterMarker.icon=new GIcon();
			this._clusterMarker.icon.image='images/arrow.png';
			this._clusterMarker.shadow='images/arrow_shadow.png';
			this._clusterMarker.shadowSize=new GSize(39, 34);
			this._clusterMarker.icon.iconSize=new GSize(39, 34);
			this._clusterMarker.icon.iconAnchor=new GPoint(9, 33);
		}
	}
	GEvent.bind(this._map, "moveend", this, this.refresh);
	GEvent.bind(this._map, "zoomend", this, this.refresh);
};

ClusterMarker.prototype.addMarkers=function($markers) {
	for (var i=$markers.length-1; i>=0; i--) {
		$markers[i]._isVisible=false;
	}
	this._mapMarkers=$markers;
	this._mapMarkersIconBounds=[];
};


ClusterMarker.prototype.removeMarkers=function() {
	for (var i=this._mapMarkers.length-1; i>=0; i--) {
		if (this._mapMarkers[i]. _isVisible) {
			this._map.removeOverlay(this._mapMarkers[i]);
		}
		delete this._mapMarkers[i]. _isVisible;
		delete this._mapMarkers[i]._makeVisible;
	}
	this._removeClusterMarkers();
	this._mapMarkers=[];
	this._mapMarkersIconBounds=[];
};

ClusterMarker.prototype._clusterMarker=function($clusterGroupIndexes) {
	var $clusterGroupBounds, i, $clusterMarker, $map;
	$clusterGroupBounds=new GLatLngBounds();
	for (i=$clusterGroupIndexes.length-1; i>=0; i--) {
		$clusterGroupBounds.extend(this._mapMarkers[$clusterGroupIndexes[i]].getLatLng());
	}
	$clusterMarker=new GMarker($clusterGroupBounds.getCenter(), {icon:this._clusterMarker.icon, title:'Click to zoom in and see '+$clusterGroupIndexes.length+' markers.'});
	
	//	copy of this._map needed to obtain function closure in click event
	$map=this._map;
	this._clusterMarkersEventListeners.push(GEvent.addListener($clusterMarker, 'click', function() {
		//	on cluster marker click center and zoom map to contain all map markers in cluster group
		$map.setCenter($clusterGroupBounds.getCenter(), $map.getBoundsZoomLevel($clusterGroupBounds));	//	make default action user-configurable?
	}));
	return $clusterMarker;
};

ClusterMarker.prototype._filterContainedMapMarkers=function() {
	var $borderPadding, $mapZoomLevel, $mapProjection, $mapBounds, $mapPointSw, $activeAreaPointSw, $activeAreaLatLngSw, $mapPointNe, $activeAreaPointNe, $activeAreaLatLngNe, $activeAreaBounds, i;
	//	iterate thru markers array
	//	set makeVisible to FALSE if marker not within map bounds + borderPadding
	$borderPadding=this.borderPadding;
	
	$mapZoomLevel=this._map.getZoom();
	$mapProjection=this._map.getCurrentMapType().getProjection();
	$mapBounds=this._map.getBounds();
	
	$mapPointSw=$mapProjection.fromLatLngToPixel($mapBounds.getSouthWest(), $mapZoomLevel);
	$activeAreaPointSw=new GPoint($mapPointSw.x-$borderPadding, $mapPointSw.y+$borderPadding);
	$activeAreaLatLngSw=$mapProjection.fromPixelToLatLng($activeAreaPointSw, $mapZoomLevel, true);
	
	$mapPointNe=$mapProjection.fromLatLngToPixel($mapBounds.getNorthEast(), $mapZoomLevel);
	$activeAreaPointNe=new GPoint($mapPointNe.x+$borderPadding, $mapPointNe.y-$borderPadding);
	$activeAreaLatLngNe=$mapProjection.fromPixelToLatLng($activeAreaPointNe, $mapZoomLevel, true);	//	check unbounded value
	
	$activeAreaBounds=new GLatLngBounds($activeAreaLatLngSw, $activeAreaLatLngNe);
	
	for (i=this._mapMarkers.length-1; i>=0; i--) {
		this._mapMarkers[i]._makeVisible=$activeAreaBounds.containsLatLng(this._mapMarkers[i].getLatLng())?true:false;
	}
};

//	rewrite loops to count down to zero
ClusterMarker.prototype._filterIntersectingMapMarkers=function() {
	var $clusterGroup, i, j, $mapZoomLevel=this._map.getZoom();
	if (typeof(this._mapMarkersIconBounds[$mapZoomLevel])=='undefined') {
		this._mapMarkersIconBounds[$mapZoomLevel]=this._mapMarkersBoundsArray();
	}
	for (i=0; i<this._mapMarkers.length; i++)
	{
		if (this._mapMarkers[i]._makeVisible) {
			$clusterGroup=[];
			for (j=i+1; j<this._mapMarkers.length; j++) {
				if (this._mapMarkers[j]._makeVisible && this._mapMarkersIconBounds[$mapZoomLevel][i].intersects(this._mapMarkersIconBounds[$mapZoomLevel][j])) {
					$clusterGroup.push(j);
				}
			}
			if ($clusterGroup.length!==0) {
				for (j=0; j<$clusterGroup.length; j++) {
					this._mapMarkers[$clusterGroup[j]]._makeVisible=false;
				}
				$clusterGroup.push(i);
				this._mapMarkers[i]._makeVisible=false;
				this._clusterMarkers.push(this._clusterMarker($clusterGroup));
			}
		}
	}
};

ClusterMarker.prototype._mapMarkersBoundsArray=function() {
	//	returns single dimension array of icon bounds for map's current zoom level
	var $mapProjection, $mapZoomLevel, i, $marker, $iconSize, $iconWidth, $iconHeight, $markerPoint, $markerPointX, $markerPointY, $iconAnchorPointOffset, $iconAnchorPointOffsetX, $iconAnchorPointOffsetY, $iconBoundsPointSw, $iconBoundsPointNe, $iconBoundsLatLngSw, $iconBoundsLatLngNe, $iconBounds=[], $intersectPadding;
	$intersectPadding=this.intersectPadding;
	$mapProjection=this._map.getCurrentMapType().getProjection();
	$mapZoomLevel=this._map.getZoom();
	for (i=this._mapMarkers.length-1; i>=0; i--) {
		$marker=this._mapMarkers[i];
		
		$iconSize=$marker.getIcon().iconSize;
		$iconWidth=$iconSize.width;
		$iconHeight=$iconSize.height;
		
		$markerPoint=$mapProjection.fromLatLngToPixel($marker.getLatLng(), $mapZoomLevel);
		$markerPointX=$markerPoint.x;
		$markerPointY=$markerPoint.y;
		
		$iconAnchorPointOffset=$marker.getIcon().iconAnchor;
		$iconAnchorPointOffsetX=$iconAnchorPointOffset.x;
		$iconAnchorPointOffsetY=$iconAnchorPointOffset.y;
		
		$iconBoundsPointSw=new GPoint($markerPointX-$iconAnchorPointOffsetX-$intersectPadding, $markerPointY-$iconAnchorPointOffsetY+$iconHeight+$intersectPadding);
		$iconBoundsPointNe=new GPoint($markerPointX-$iconAnchorPointOffsetX+$iconWidth+$intersectPadding, $markerPointY-$iconAnchorPointOffsetY-$intersectPadding);
		$iconBoundsLatLngSw=$mapProjection.fromPixelToLatLng($iconBoundsPointSw, $mapZoomLevel, false);	//	check unbounded is correct
		$iconBoundsLatLngNe=$mapProjection.fromPixelToLatLng($iconBoundsPointNe, $mapZoomLevel, false);
		
		$iconBounds[i]=new GLatLngBounds($iconBoundsLatLngSw, $iconBoundsLatLngNe);
	}
	return $iconBounds;
};

ClusterMarker.prototype.refresh=function() {
	//	remove any cluster markers and event listeners then clear both cluster marker and event arrays
	//	use array pop method instead if quicker
	var i,j;
	
	this._removeClusterMarkers();
	
	this._filterContainedMapMarkers();
	if (this.clusteringEnabled) {
		this._filterIntersectingMapMarkers();
	}
	
	//	add or remove map markers
	for (i=this._mapMarkers.length-1; i>=0; i--) {
		j=this._mapMarkers[i];
		if (!j._isVisible && j._makeVisible) {
			this._map.addOverlay(j);
			j._isVisible=true;
		}
		if (j._isVisible && !j._makeVisible) {
			this._map.removeOverlay(j);
			j._isVisible=false;
		}
	}
	
	//	add any cluster markers
	for (i=this._clusterMarkers.length-1; i>=0; i--) {
		this._map.addOverlay(this._clusterMarkers[i]);
	}
	
};

ClusterMarker.prototype._removeClusterMarkers=function() {
	for (var i=this._clusterMarkers.length-1; i>=0; i--) {
		this._map.removeOverlay(this._clusterMarkers[i]);
	}
	for (i=this._clusterMarkersEventListeners.length-1; i>=0; i--) {
		GEvent.removeListener(this._clusterMarkersEventListeners[i]);
	}
	this._clusterMarkers=[];
	this._clusterMarkersEventListeners=[];
};