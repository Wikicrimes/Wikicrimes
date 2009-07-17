package org.wikicrimes.util.kernelMap;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A set of simple routines to provide information about google tiles (API v1).
 * These routines are not written for speed.
 * These routines assume a tile size of 256 pixels square.
 * Internally a sort of offset mercator projection is used, this places the origin (0,0)
 * at the top left and goes to +1,+1 at the bottom right.
 * I believe a proper mercator would have 0,0 in the middle and range from -0.5,-0.5 bottom left to +0.5,+0.5 top right.
 * I've used this system because it provides a better fit with a typical the pixel coordinate system.
 */
public class GoogleTileUtils {
   static int TILE_SIZE = 256;

   /**
    * Returns the pixel offset of a latitude and longitude within a single typical google tile.
    * @param lat
    * @param lng
    * @param zoom
    * @return
    */
   public static Point getPixelOffsetInTile(double lat, double lng, int zoom) {
      Point pixelCoords = toZoomedPixelCoords(lat, lng, zoom);

      return new Point(pixelCoords.x % TILE_SIZE, pixelCoords.y % TILE_SIZE);
   }

   /**
   * returns a Rectangle2D with x = lon, y = lat, width=lonSpan, height=latSpan
   * for an x,y,zoom as used by google.
   */
   public static Rectangle2D.Double getTileRect(int x, int y, int zoom) {
      int tilesAtThisZoom = 1 << (17 - zoom);
      double lngWidth     = 360.0 / tilesAtThisZoom; // width in degrees longitude
      double lng          = -180 + (x * lngWidth); // left edge in degrees longitude

      double latHeightMerc = 1.0 / tilesAtThisZoom; // height in "normalized" mercator 0,0 top left
      double topLatMerc    = y * latHeightMerc; // top edge in "normalized" mercator 0,0 top left
      double bottomLatMerc = topLatMerc + latHeightMerc;

      // convert top and bottom lat in mercator to degrees
      // note that in fact the coordinates go from about -85 to +85 not -90 to 90!
      double bottomLat = (180 / Math.PI) * ((2 * Math.atan(Math.exp(Math.PI * (1 - (2 * bottomLatMerc)))))
                         - (Math.PI / 2));

      double topLat = (180 / Math.PI) * ((2 * Math.atan(Math.exp(Math.PI * (1 - (2 * topLatMerc))))) - (Math.PI / 2));

      double latHeight = topLat - bottomLat;

      return new Rectangle2D.Double(lng, bottomLat, lngWidth, latHeight);
   }

   /**
       * returns the lat/lng as an "Offset Normalized Mercator" pixel coordinate,
       * this is a coordinate that runs from 0..1 in latitude and longitude with 0,0 being
       * top left. Normalizing means that this routine can be used at any zoom level and
       * then multiplied by a power of two to get actual pixel coordinates.
       * @param lat in degrees
       * @param lng in degrees
       * @return
       */
   public static Point2D toNormalisedPixelCoords(double lat, double lng) {
      // first convert to Mercator projection
      // first convert the lat lon to mercator coordintes.
      if (lng > 180) {
         lng -= 360;
      }

      lng /= 360;
      lng += 0.5;

      lat = 0.5 - ((Math.log(Math.tan((Math.PI / 4) + ((0.5 * Math.PI * lat) / 180))) / Math.PI) / 2.0);

      return new Point2D.Double(lng, lat);
   }

   /**
    * returns a point that is a google tile reference for the tile containing the lat/lng and at the zoom level.
    * @param lat
    * @param lng
    * @param zoom
    * @return
    */
   public static Point toTileXY(double lat, double lng, int zoom) {
      Point2D normalised = toNormalisedPixelCoords(lat, lng);
      int scale          = 1 << (17 - zoom);

      // can just truncate to integer, this looses the fractional "pixel offset"
      return new Point((int) (normalised.getX() * scale), (int) (normalised.getY() * scale));
   }

   /**
    * returns a point that is a google pixel reference for the particular lat/lng and zoom
    * assumes tiles are 256x256.
    * @param lat
    * @param lng
    * @param zoom
    * @return
    */
   public static Point toZoomedPixelCoords(double lat, double lng, int zoom) {
      Point2D normalised = toNormalisedPixelCoords(lat, lng);
      double scale       = (1 << (17 - zoom)) * TILE_SIZE;

      return new Point((int) (normalised.getX() * scale), (int) (normalised.getY() * scale));
   }

   /**
    * Returns a google maps satellite type string for the tile containing the lat and lng at the zoom level.
    * @param lat
    * @param lng
    * @param zoom
    * @return
    */
   private static String getSatelliteRef(double lat, double lng, int zoom) {
      Point tileXY = toTileXY(lat, lng, zoom);
      int invZoom  = 17 - zoom;
      int stepSize = 1 << (17 - zoom);
      int currentX = 0;
      int currentY = 0;

      StringBuffer satString = new StringBuffer(zoom);
      satString.append("t");

      for (int i = 0; i < invZoom; i++) {
         stepSize >>= 1;

         if ((currentY + stepSize) > tileXY.y) {
            if ((currentX + stepSize) > tileXY.x) {
               satString.append('q');
            }
            else {
               currentX += stepSize;
               satString.append('r');
            }
         }
         else {
            currentY += stepSize;

            if ((currentX + stepSize) > tileXY.x) {
               satString.append('t');
            }
            else {
               currentX += stepSize;
               satString.append('s');
            }
         }
      }

      return satString.toString();
   }

   /** Returns an x,y for a satellite reference
   * @param string
   * @return
   */
   private static Point satelliteRefToTileXY(String satelliteRef) {
      // must start with "t"
      if ((satelliteRef == null) || (satelliteRef.length() == 0) || (satelliteRef.charAt(0) != 't')) {
         throw new RuntimeException("satellite string must start with 't'");
      }

      int x = 0; // x
      int y = 0;

      for (int i = 1; i < satelliteRef.length(); i++) {
         x <<= 1;
         y <<= 1;

         char c = satelliteRef.charAt(i);

         switch (c) {
            case 's':
               y += 1;
               x += 1;

               break;

            case 'r':
               x += 1;

               break;

            case 'q':
               y += 0;

               break;

            case 't':
               y += 1;

               break;

            default:
               throw new RuntimeException("satellite char '" + c + "' when decoding keyhole string.");
         }
      }

      return new Point(x, y);
   }

   /**
    * Returns the zoom level for the given satellite reference string
    * @param string
    * @return
    */
   private static int satelliteRefToZoom(String satRef) {
      return 18 - satRef.length();
   }

   /**
    * Test cases
    * @param args
    */
   public static void main(String[] args) {
      double eps = 1.0e-6;
      System.out.println("toNormalizedCoords");

      // center
      System.out.println("center, expect 0.5,0.5\t" + toNormalisedPixelCoords(0, 0));

      // topleft
      System.out.println("topLeft, expect 0,0\t" + toNormalisedPixelCoords(85.05113f, -180));

      // topright
      System.out.println("topRight, expect 1,0\t" + toNormalisedPixelCoords(85.05113f, 180 - eps));

      //bottomleft
      System.out.println("bottomLeft, expect 0,1\t" + toNormalisedPixelCoords(-85.05113f, -180));

      // bottomright
      System.out.println("bottomRight, expect 1,1\t" + toNormalisedPixelCoords(-85.05113f, 180 - eps));

      // my old home
      System.out.println("my old home, expect \t" + toNormalisedPixelCoords(53.427972089896805, -2.2671961784362793));

      System.out.println("getTileXY");
      System.out.println("center zoom=17, expect 0,0\t" + toTileXY(0, 0, 17));
      System.out.println("slightly up right of center zoom=16, expect 1,0\t" + toTileXY(eps, eps, 16));
      System.out.println("slightly up right of center zoom=15, expect 2,1\t" + toTileXY(eps, eps, 15));
      System.out.println("my old home zoom=0, expect 64710,42436\t"
                         + toTileXY(53.427972089896805, -2.2671961784362793, 0));

      System.out.println("toPixelCoords");
      System.out.println("center zoom=17, expect 127,127\t" + toZoomedPixelCoords(0, 0, 17));
      System.out.println("slightly up right of center zoom=16, expect 256,256\t" + toZoomedPixelCoords(eps, eps, 16));
      System.out.println("slightly up right of center zoom=15, expect 512,512\t" + toZoomedPixelCoords(eps, eps, 15));
      System.out.println("my old home zoom=0, expect 16565898,10863803\t"
                         + toZoomedPixelCoords(53.427972089896805, -2.2671961784362793, 0));

      System.out.println("getPixelOffsetInTile");
      System.out.println("center zoom=17, expect 127,127\t" + getPixelOffsetInTile(0, 0, 17));
      System.out.println("slightly up right of center zoom=16, expect 0,0\t" + getPixelOffsetInTile(eps, eps, 16));
      System.out.println("slightly up right of center zoom=15, expect 0,0\t" + getPixelOffsetInTile(eps, eps, 15));
      System.out.println("my old home zoom=0, expect 138,187\t"
                         + getPixelOffsetInTile(53.427972089896805, -2.2671961784362793, 0));

      System.out.println("getSatelliteRef");
      System.out.println("center zoom=17, expect t\t" + getSatelliteRef(0, 0, 17));
      System.out.println("slightly up right of center zoom=16, expect tr\t" + getSatelliteRef(eps, eps, 16));
      System.out.println("slightly up right of center zoom=15, expect trt\t" + getSatelliteRef(eps, eps, 15));
      System.out.println("my old home zoom=0, expect tqsrsrrsqtssqqqsrq\t"
                         + getSatelliteRef(53.427972089896805, -2.2671961784362793, 0));

      System.out.println();
      System.out.println();
      System.out.println("from google to lat/lng");
      System.out.println();
      System.out.println("satelliteRefToZoom");
      System.out.println("expect 17\t" + satelliteRefToZoom("t"));
      System.out.println("expect 16\t" + satelliteRefToZoom("tr"));
      System.out.println("expect 15\t" + satelliteRefToZoom("trt"));
      System.out.println("expect 0\t" + satelliteRefToZoom("tqsrsrrsqtssqqqsrq"));

      System.out.println("satelliteRefToTileXY");
      System.out.println("expect 0,0\t" + satelliteRefToTileXY("t"));
      System.out.println("expect 1,0\t" + satelliteRefToTileXY("tr"));
      System.out.println("expect 2,1\t" + satelliteRefToTileXY("trt"));
      System.out.println("expect 64710,42436\t" + satelliteRefToTileXY("tqsrsrrsqtssqqqsrq"));

      System.out.println("tileXYToRectangle");
      System.out.println("expect -180,-85,360,170\t" + getTileRect(0, 0, 17));
      System.out.println("expect 0,0,180,85\t" + getTileRect(1, 0, 16));
      System.out.println("expect 0,0,90,66.5\t" + getTileRect(2, 1, 15));
      System.out.println("expect -2.268677,53.42754, 0.002747,0.00163\t" + getTileRect(64710, 42436, 0));
   }

}