package fsats.gui.event;

import java.util.*;

/**
 * Helper class for keeping track of and sending events to
 * ObjectPlacedListeners.
 *
 * @author Daniel Rogers (drogers@arlut.utexas.edu)
 * @version 1.0, 05/25/99
 */
public class ObjectPlacedHandler {
  
  private Vector listeners = new Vector();
  
  /**
   * Creates a new ObjectPlacedHandler.
   */
  public ObjectPlacedHandler () {
    super();
  }
  
  /**
   * Adds a new ObjectPlacedListener to the queue of listeners to receive
   * events.
   *
   * @param l ObjectPlacedListener
   */
  public synchronized void addObjectPlacedListener (ObjectPlacedListener l) {
    listeners.addElement(l);
  }
  
  /**
   * Removes an ObjectPlacedListener to the queue of listeners to receive
   * events.
   *
   * @param l ObjectPlacedListener
   */
  public synchronized void removeObjectPlacedListener (ObjectPlacedListener l) {
    listeners.removeElement(l);
  }
  
  /**
   * Fires an ObjectPlacedEvent to any listeners.
   *
   * @param e ObjectPlacedEvent to send
   */
  public void fireObjectPlacedEvent (ObjectPlacedEvent e) {
    Vector targets = (Vector) listeners.clone();
    for (int i = 0; i < targets.size(); i++) {
      ObjectPlacedListener opl = (ObjectPlacedListener) targets.elementAt(i);
      opl.objectPlaced(e);
    }
  }
}
 