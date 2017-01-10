package fsats.gui.event;

/**
 * Event used when the GridPane has a scale method called upon it.
 *
 * @author Daniel Rogers (drogers@arlut.utexas.edu)
 * @version 1.0, 04/07/99
 */
public class GridPaneScaledEvent extends java.util.EventObject {

  /**
   * Indicates that the grid was scaled.
   */
  public static final int GRID_SCALED = 1;

  /**
   * Indicates that the grid lines were scaled.
   */
  public static final int GRID_LINES_SCALED = 2;

  private int id;

  /**
   * Constructs a GridPaneScaledEvent with the specified source object and
   * action ID.
   *
   * @param s source object where event originated
   * @param r ID for the event type
   */
  public GridPaneScaledEvent (Object s, int r) {
    super(s);
    id = r;
  }

  /** 
   * Returns the ID for the event type.
   *
   * @return int event ID
   */
  public int getID () {
    return id;
  }
}

