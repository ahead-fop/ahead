package fsats.measures;

import fsats.mathFunctions.GreatCircleFunctions;

/**
 * A Location type - latitude and longitude in decimal degrees and altitude in meters.
 *
 * Conventions:
 * 1. sign convention is the standard mathematical right hand coordinate system:
 *    with...: north latitudes and eastern longitudes positive and
 *             south latitudes and western longitudes negitive
 *
 * 2. The default value for an "unset" Altitude is 0 meters.
 */
public class Location
{
//=================
//Static methods
//=================
   /**
    * Return a null Location;
    */
   public static Location nullLocation()
   {
      return nullLoc;
   }


//=================
//Public methods
//=================
   /**
    * Test for null.
    * 
    * @return true if "null" Location, false otherwise
    */
   public boolean isNull()
   {
      return false;
   }
   
   /**
    * The latitude
    *
    * @return the location's latitude
    */
   public double latitude()
   {
      return lat;
   }
  
   /**
    * The longitude
    *
    * @return the location's longitude
    */
   public double longitude()
   {
      return lon;
   }

   /**
    * Test for null altitude.
    *
    * @return true if the location was constructed without an altitude,
    *         and false if constructed with an altitude.
    */
   public boolean isNullAltitude()
   {
      return nullAlt;
   }
   
   /**
    * The altitude. If the Location was constructed without an
    * altitude the value will default to 0.
    *
    * @return the location's altitude
    */
   public double altitude()
   {
      return alt;
   }

   /**
    * The datum. Default datum is WGS84.
    *
    * @return the location's datum
    */
   public Datum datum()
   {
      return d;
   }

  /**
   * Computes using Great Circle Navigation a location 
   * that is a distance of d (in meters) away in the theta 
   * (in degrees) direction. 
   *
   * @return Location the computed location.
   */ 
   public Location locationOf(
      double d, 
      double theta)
   {
      return GreatCircleFunctions.locationGivenRadialAndDistance(this, theta, d);
   }

  
   /**
    * Computes the UTMLocation w.r.t the native gridzone equivalent of this location.
    *
    * @return UTMLocation
    */
   public UTMLocation inUTM()
   {
      return GeoUTMConversion.LLtoUTM(this);
   }


   /**
    * Returns the location relative to the passed datum.
    *
    * @param datum the datum to shift the location to
    *
    * @return Location the datum shifted location
    */
   public Location shiftToDatum(
      Datum datum)
   {
      return this.d.shiftTo(datum,this);
   }

  
   /**
    * Returns a string representation of the location as the ordered 
    * quadruplet (lat,lon,alt,datum).
    *
    * @return String the location in the above format.
    */
   public String toString()
   {
      return "(lat,lon,alt,datum): ("+ lat+","+lon+","+alt+","+d.datumName+")";
   }
   
   /**
    * Returns the a string representation of the latitude, longitude, and datum
    * parts of the location as the ordered triplet (lat,lon,datum) where
    * the latitude and the longitude are in "degrees minutes seconds" 
    * followed by an North-South or East-West indicator.
    *
    * @return String the location in the format (### ## ##.## N|S, ### ## ##.## E|W, datum)
    */
   public String toDMS()
   {
      double l = Math.abs(lat);
      int latD = (int)(l%90.0);
      int latM = (int)( ((l-latD)*60.0)%60.0);
      double latS = (l - (latD + (latM/60.0)))*3600.0;
      char NS = lat<0.0 ? 'S' : 'N';

      l = Math.abs(lon);
      int lonD = (int)(l%180.0);
      int lonM = (int)( ((l-lonD)*60.0)%60.0);
      double lonS = (l - (lonD + (lonM/60.0)))*3600.0;
      char EW = lon<0.0 ? 'W' : 'E';

      return "(lat,lon,datum): ("+ latD+" "+latM+" "+latS+NS+", "+
                                   lonD+" "+lonM+" "+lonS+EW+", "+d.datumName+")";
   }
   
   /**
    * Constructs a newly allocated Location object with the given latitude and longitude.
    * The location's altitude value defaults to 0.0  meters and isNullAlt returns true.
    * The location's datum defaults to WGS84.
    *
    * @param latitude latitude of the location in decimal degrees
    * @param longitude longitude of the location in decimal degrees
    */
   public Location(
      double latitude,
      double longitude)
   {
      this(latitude,longitude,nullAltValue,Datum.WGS_84);
      nullAlt = true;
   }

   /**
    * Constructs a newly allocated Location object with the given latitude, longitude,
    * and datum.
    * The location's altitude value defaults to 0.0  meters and isNullAlt returns true.
    *
    * @param latitude latitude of the location in decimal degrees
    * @param longitude longitude of the location in decimal degrees
    * @param datum Datum that the location is relative to
    */
   public Location(
      double latitude,
      double longitude,
      Datum datum)
   {
      this(latitude,longitude,nullAltValue,datum);
      nullAlt = true;
   }

   /**
    * Constructs a newly allocated Location object with the given latitude, longitude,
    * and altitude.
    * isNullAlt returns false.
    * The location's datum defaults to WGS84.
    *
    * @param latitude latitude of the location in decimal degrees
    * @param longitude longitude of the location in decimal degrees
    * @param datum Datum that the location is relative to
    */
   public Location(
      double latitude,
      double longitude,
      double altitude)
   {
      this(latitude,longitude,altitude,Datum.WGS_84);
   }
   
   /**
    * Constructs a newly allocated Location object with the given latitude, longitude,
    * altitude and datum.
    * isNullAlt returns false.
    * The location's datum defaults to WGS84.
    *
    * @param latitude latitude of the location in decimal degrees
    * @param longitude longitude of the location in decimal degrees
    * @param altitude altitude of the location in meters.
    * @param datum Datum that the location is relative to
    */
   public Location(
      double latitude,
      double longitude,
      double altitude,
      Datum datum)
   {
      this.lat = latitude;
      this.lon = longitude;
      this.alt = altitude;
      this.d = datum;
      nullAlt = false;
   }
   
   
   //0.0 is the "hasn't been set" value for altitudes.
   private static final double nullAltValue = 0.0;

   //Double.MIN_VALUE is the "hasn't been set" value for latitude and longitudes.
   private double lat = Double.MIN_VALUE;
   private double lon = Double.MIN_VALUE;
   private double alt = nullAltValue;

   private boolean nullAlt = true;

   private Datum d = Datum.WGS_84;
   
   private static Location nullLoc = new NullLocation();

   public static void main(
      String[] args)
   {
   }
}

class NullLocation
   extends Location
{
   NullLocation()
   {
      super(Double.MIN_VALUE, Double.MIN_VALUE);
   }
   public boolean isNull()
   {
      return true;
   }
}
  
