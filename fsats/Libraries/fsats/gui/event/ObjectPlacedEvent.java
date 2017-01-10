package fsats.gui.event;

import fsats.gui.*;

/**
 * Event used when an object has been placed on the GridPane.
 *
 * @author Daniel Rogers (drogers@arlut.utexas.edu)
 * @version 1.0, 05/25/99
 */
public class ObjectPlacedEvent extends java.util.EventObject {

  /**
   * Indicates that an object was placed on the grid.
   */
  public static final int OBJECT_PLACED = 1;

  private int id;
  private Object obj;
  private Location loc;

  /**
   * Constructs an ObjectPlacedEvent with the specified source object,
   * ID, a pass through object, and a Location of the click object.
   *
   * @param s source object where event originated
   * @param r ID for the event type
   * @param o Object that has been placed
   * @param l Location where the object was placed
   */
  public ObjectPlacedEvent (Object s, int r, Object o, Location l) {
    super(s);
    id  = r;
    obj = o;
    loc = l;
  }

  /** 
   * Returns the ID for the event type.
   *
   * @return int event ID
   */
  public int getID () {
    return id;
  }

  /**
   * Returns the object that was placed.
   *
   * @return Object reference
   */
  public Object getPlacedObject () {
    return obj;
  }

  /**
   * Returns the coordinates of where the object was placed.
   *
   * @return Location object
   */
  public Location getLocation () {
    return loc;
  }
}

