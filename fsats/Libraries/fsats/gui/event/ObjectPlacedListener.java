package fsats.gui.event;

/**
 * Listener interface for the ObjectPlacedEvent.
 *
 * @author Daniel Rogers (drogers@arlut.utexas.edu)
 * @version 1.0, 05/25/99
 */
public interface ObjectPlacedListener extends java.util.EventListener {

  /**
   * Called when an object has been placed on the GridPane.
   *
   * @param e ObjectPlacedEvent to process
   */
  public void objectPlaced (ObjectPlacedEvent e);
}

