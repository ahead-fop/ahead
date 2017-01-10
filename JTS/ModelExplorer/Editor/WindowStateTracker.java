/* ******************************************************************
   class      : WindowStateTracker
   description: a class that tracks window events and stores
                the state
*********************************************************************/
package ModelExplorer.Editor;

import java.awt.*;
import java.awt.event.*;

public class WindowStateTracker implements WindowListener {

  // instance variable declarations

  // constructors
  public WindowStateTracker() {}

  // methods
  private boolean open             = false;
  private boolean iconic           = false;
  private boolean active           = false;
  private Component focusRequester = null;

  /**
   * Invoked when a window has been opened.
   */
  public void windowOpened(WindowEvent e) {
    open = true;
  } // end windowOpened

  /**
   * Invoked when a window is in the process of being closed. The close
   * operation can be overridden at this point.
   */
  public void windowClosing(WindowEvent e) {
  } // end windowClosing

  /**
   * Invoked when a window has been closed.
   */
  public void windowClosed(WindowEvent e) {
    open = false;
  } // end windowClosed

  /**
   * Invoked when a window is iconified.
   */
  public void windowIconified(WindowEvent e) {
    iconic = true;
  } // end windowIconified

  /**
   * Invoked when a window is deiconified.
   */
  public void windowDeiconified(WindowEvent e) {
    iconic = false;
  } // end windowDeiconified

  /**
   * Invoked when a window is activated. 
   */
  public void windowActivated(WindowEvent e) {
    active = true;
    if (focusRequester != null) focusRequester.requestFocus();
  } // end windowActivated

  /**
   * Invoked when a window is de-activated.
   */
  public void windowDeactivated(WindowEvent e) {
    active = false;
  } // end windowDeactivated

  /**
   * Returns true if a window is open.
   */
  public boolean isOpen(WindowEvent e) {
    return open;
  } // end isOpen

  /**
   * Returns true if a window is iconic.
   */
  public boolean isIconic() {
    return iconic;
  } // end isIconic

  /**
   * Returns true if a window is active. 
   */
  public boolean isActive() {
    return active;
  } // end isActive

  /**
   * Sets a component that will automatically request the focus
   * on window activation.
   */
  public void setFocusRequester(Component c) {
    focusRequester = c;
  } // end setFocusRequester

} // end WindowStateTracker

/* ******************************************************************
   end of file
*********************************************************************/
