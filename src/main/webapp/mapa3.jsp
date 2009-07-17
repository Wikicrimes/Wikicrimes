<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/taglibs.jsp"%>


<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=iso-8859-1"/>
    <script type="text/javascript" src="http://www.google.com/jsapi?key=ABCDEFG"></script>
    <script type="text/javascript">
        google.load("maps", "2", {"locale" : "pt_BR"});

        var map;
        var number = 0;

        function createMarker(point) {
            var marker = new GMarker(point, {draggable: true});
            marker.value = ++number;
            GEvent.addListener(marker, "click", function() {
	            var latitude = marker.getPoint().lat();
	            var longitude = marker.getPoint().lng();
                var myHtml = "<iframe src='/wikimapcrime/loadCrime.do?latitude="+latitude+"&longitude="+longitude+"' width=200 height=100></iframe>";
                marker.openInfoWindowHtml(myHtml);
            });
          
            GEvent.addListener(marker, "dragstart", function() {
                map.closeInfoWindow();
            });

            GEvent.addListener(marker, "dragend", function() {
                marker.openInfoWindowHtml("Just bouncing along...");
            });
            
            GEvent.addListener(marker, "dblclick", function() {
	            var latitude = marker.getPoint().lat();
	            var longitude = marker.getPoint().lng();
                var myHtml = "<iframe src='/wikimapcrime/removeCrime.do?latitude="+latitude+"&longitude="+longitude+"' width=200 height=100></iframe>";
                marker.openInfoWindowHtml(myHtml);
                marker.hide();
            });

            return marker;
        }

		function loadAll() {
			<c:forEach var="crime" items="${crimes}">
				map.addOverlay(
					createMarker(
						new GLatLng(<c:out value="${crime.latitude}" />,<c:out value="${crime.longitude}" />),
						{draggable: true}
					)
				);
			</c:forEach>
		}

        // Call this function when the page has been loaded
        function initialize() {
            map = new GMap2(document.getElementById("map"));
            map.setCenter(new GLatLng(-3.738189815174382, -38.52081298828125), 13);
        
            GEvent.addListener(map, "click", function(overlay,point) {
                map.addOverlay(createMarker(point));
            });

            map.addControl(new GLargeMapControl());
            map.addControl(new GMapTypeControl());
            map.addControl(new GScaleControl());
            map.addControl(new GOverviewMapControl());

			loadAll();
        }

        google.setOnLoadCallback(initialize);
    </script>
  </head>
  <body onunload="GUnload()">
    <table>
         <tr>
            <td>
                <div id="map" style="width: 600px; height: 400px"></div>
            </td>
        </tr>
        <tr>
            <td>
            	<div style="bgcolor=#CCCCCC"><a href="/wikimapcrime/loadAllCrime.do">Mostrar</a></div>
            </td>
        </tr>
    </table>
  </body>
</html>

