package fsats.gui.event;

/**
 * Listener interface for the GridPaneScaledEvent.
 *
 * @author Daniel Rogers (drogers@arlut.utexas.edu)
 * @version 1.0, 04/07/99
 */
public interface GridPaneScaledListener extends java.util.EventListener {

  /**
   * Called when the scale of the GridPane has been changed.
   *
   * @param e GridPaneScaledEvent to process
   */
  public void gridPaneScaled (GridPaneScaledEvent e);
}

