package fsats.gui;

/**
 * Contains the constraints used for a specific component when entering
 * it into use with a CoordinateLayout.
 *
 * @author Daniel Rogers (drogers@arlut.utexas.edu)
 * @version 1.0, 04/05/99
 */
public class CoordinateConstraints {

  private double latitude;
  private double longitude;
  
  /**
   * Creates a new CoordinateConstraints object with the given
   * latitude and longitude.
   *
   * @param lat double latitude
   * @param lon double longitude
   */
  public CoordinateConstraints (double lat, double lon) {
    latitude = lat;
    longitude = lon;
  }

  public CoordinateConstraints(fsats.guiIf.Location l)
  {
      this(l.getLatitude(), l.getLongitude());
  }
  
  public Location getLocation()
  {
      return new Location(latitude, longitude);
  }

  /**
   * Returns the latitude specified.
   *
   * @return double latitude
   */
  public double getLatitude () {
    return latitude;
  }

  /**
   * Sets the latitude to be used.
   *
   * @param lat double latitude
   */
  public void setLatitude (double lat) {
    latitude = lat;
  }
  
  /**
   * Returns the longitude specified.
   *
   * @return double longitude
   */
  public double getLongitude () {
    return longitude;
  }
  
  /**
   * Sets the longitude to be used.
   *
   * @param lon double longitude
   */
  public void setLongitude (double lon) {
    longitude = lon;
  }
}
