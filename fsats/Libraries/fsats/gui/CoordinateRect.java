package fsats.gui;

/**
 * This class holds a pair of latitude/longitude values that define a
 * rectangular area.
 *
 * @author Daniel Rogers (drogers@arlut.utexas.edu)
 * @version 1.5, 05/26/99
 */
public class CoordinateRect implements Cloneable {

  /**
   * Indicates that a given lat/long is in range.
   */
  public static final int IN_RANGE = 0;

  /**
   * Indicates that the given latitude is out of range.
   */
  public static final int LAT_OUT_OF_RANGE = 1;

  /**
   * Indicates that the given longitude is out of range.
   */
  public static final int LONG_OUT_OF_RANGE = 2;

  /**
   * Indicates that both latitude and longitude are out of range.
   */
  public static final int BOTH_OUT_OF_RANGE = 4;

  private double lat1;
  private double lat2;
  private double long1;
  private double long2;
  
  /**
   * Constructs a new empty CoordinateRect object.
   */
  public CoordinateRect () {
  }
  
  /**
   * Constructs a new CoordinateRect with the given latitude/longitude
   * pair.
   *
   * @param lat1 double latitude 1
   * @param long1 double longitude 1
   * @param lat2 double longitude 2
   * @param long2 double longitude 2
   */
  public CoordinateRect (double lat1, double long1,
                         double lat2, double long2) {
    
    this.lat1  = lat1;
    this.long1 = long1;
    this.lat2  = lat2;
    this.long2 = long2;
    
  }
 
  /**
   * Returns a copy of this guy.
   *
   * @return cloned CoordinateRect
   */
  public Object clone () {
    return new CoordinateRect(lat1, long1, lat2, long2);
  }
 
  /**
   * Returns the first latitude.
   *
   * @return double latitude
   */
  public double getLat1 () {
    return lat1;
  }
  
  /**
   * Sets the first latitude.
   *
   * @param lat1 double latitude
   */
  public void setLat1 (double lat1) {
    this.lat1 = lat1;
  }

  /**
   * Returns the first longitude.
   *
   * @return double longitude
   */
  public double getLong1 () {
    return long1;
  }
  
  /**
   * Sets the first longitude.
   *
   * @param long1 double longitude
   */
  public void setLong1 (double long1) {
    this.long1 = long1;
  }
  
  /**
   * Returns the second latitude.
   *
   * return double latitude
   */
  public double getLat2 () {
    return lat2;
  }
  
  /**
   * Sets the second latitude.
   *
   * @param lat2 double latitude
   */
  public void setLat2 (double lat2) {
    this.lat2 = lat2;
  }
  
  /**
   * Returns the second longitude.
   *
   * @return double longitude
   */
  public double getLong2 () {
    return long2;
  }
  
  /**
   * Sets the second longitude.
   *
   * @param long2 double longitude
   */
  public void setLong2 (double long2) {
    this.long2 = long2;
  }

  /**
   * Returns the smaller of the latitude values.
   *
   * @return smaller latitude
   */
  public double getMinLat () {
    return Math.min(lat1, lat2);
  }
  
  /**
   * Returns the larger of the latitude values.
   *
   * @return larger latitude
   */
  public double getMaxLat () {
    return Math.max(lat1, lat2);
  }
  
  /**
   * Returns the smaller of the longitude values.
   *
   * @return smaller longitude
   */
  public double getMinLong () {
    return Math.min(long1, long2);
  }
  
  /**
   * Returns the larger of the longitude values.
   *
   * @return larger longitude
   */
  public double getMaxLong () {
    return Math.max(long1, long2);
  }
   
  /**
   * Attempts to compare two CoordinateRect objects.
   *
   * @param o Object with which to compare
   * @return true if objects contain equal values
   */
  public boolean equals (Object o) {
    // hmm...should this compare c.getMaxLat() == getMaxLat(), etc.?
    if (o instanceof CoordinateRect) {
      CoordinateRect c = (CoordinateRect) o;
      return c.getLat1()  == getLat1()   &&
	     c.getLat2()  == getLat2()   &&
	     c.getLong1() == getLong1()  &&
	     c.getLong2() == getLong2();
    }
    return false;
  }
    
  /**
   * Returns the horizontal distance between longitude 1 and longitude 2.
   *
   * @return double distance
   */
  public double getHorizontalDistance () {
    return Math.abs(long2 - long1);
  }

  /**
   * Returns the vertical distance between latitude 1 and latitude 2.
   *
   * @return double distance
   */
  public double getVerticalDistance () {
    return Math.abs(lat2 - lat1);
  }

  /**
   * Returns whether or not the given coordinate is in the rectangular
   * range.
   *
   * @param lat double latitude
   * @param lon double longitude
   * @return int IN_RANGE, LAT_OUT_OF_RANGE, LONG_OUT_OF_RANGE,
   *    BOTH_OUT_OF_RANGE
   */
  public int inRange (double lat, double lon) {
    int ret = IN_RANGE;
    if (lat < getMinLat() || lat > getMaxLat()) {
      ret = LAT_OUT_OF_RANGE;
    }
    if (lon < getMinLong() || lon > getMaxLong()) {
      ret = ret == LAT_OUT_OF_RANGE ? BOTH_OUT_OF_RANGE : LONG_OUT_OF_RANGE;
    }
    return ret;
  }
}
