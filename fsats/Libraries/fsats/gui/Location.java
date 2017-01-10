package fsats.gui;

/**
 * Contains the specific coordinate positions.
 *
 * @author Bryan Tower (btower@arlut.utexas.edu)
 * @version 1.0, 04/16/99
 */
public class Location implements fsats.guiIf.Location, Cloneable {

  private double lat=0.0;
  private double lon=0.0;

  public Location() {}

  public Location(fsats.guiIf.Location location)
  {
      this(location.getLatitude(), location.getLongitude());
  }

  /**
   * Creates a new Location 
   *
   * @param double lat
   * @param double lon
   */
  public Location (double lat, double lon) 
  { 
    this.lat = lat;
    this.lon = lon;
  }
  
  public float getLatitude () { return (float)lat; }

  public float getLongitude () { return (float)lon; }
  
  public String toString() 
  {
    return getLatitudeDMS()+", "+getLongitudeDMS();
  }
 
  public Object clone () { return new Location(this); }
 
  public boolean equals (fsats.guiIf.Location location) 
  {
      return getLatitude()==location.getLatitude() 
          && getLongitude()==location.getLongitude();
  }
  
  /** An angle magnitude in format ddd mm'ss" */
  private static String toDMSString(double degrees)
  {
       double round = Math.abs(degrees)+1.0/7200.0;
       int ddd = (int)Math.floor(round);
       round = 60.0*(round - (double)ddd);
       int mm = (int)Math.floor(round);
       round = 60.0*(round - (double)mm);
       int ss = (int)Math.floor(round);
       return ""+ddd+" "+mm+"'"+ss+"\"";
  }

  /** Latitude in format NS ddd mm'ss" */
  public static String formatLatitude (double lat) 
  {
    // Assume north positive.
    return (lat >= 0 ? "N " : "S ") + toDMSString(lat);
  }    

  /** Longitude in format EW ddd mm'ss" */
  public static String formatLongitude (double lon) 
  {
    // Assume east positive.
    return (lon >= 0 ? "E " : "W ") + toDMSString(lon);
  }

  /** Latitude in format NS ddd mm'ss" */
  public String getLatitudeDMS() { return formatLatitude(lat); }

  /** Longitude in format EW ddd mm'ss" */
  public String getLongitudeDMS() { return formatLongitude(lon); }
}

