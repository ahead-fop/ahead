/* ******************************************************************
   class    : OkTextField
******************************************************************* */

package ModelExplorer.Util;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * A TextField which generates action event on cr or newline.
 */
public class OkTextField extends TextField implements KeyListener {

  // class variable declarations

  // instance variable declarations
  Hashtable listeners = new Hashtable(1);

  // constructors
  /** Create a new OkTextField object. */
  public OkTextField() {
    super();
    addKeyListener(this);
  } // end constructor OkTextField

  /** Create a new OkTextField object. */
  public OkTextField(String text) {
    super(text);
    addKeyListener(this);
  } // end constructor OkTextField

  /** Create a new OkTextField object. */
  public OkTextField(int columns) {
    super(columns);
    addKeyListener(this);
  } // end constructor OkTextField

  /** Create a new OkTextField object. */
  public OkTextField(String text, int columns) {
    super(text,columns);
    addKeyListener(this);
  } // end constructor OkTextField

  // methods
  /**
   * Adds the specified action listener to receive action events.
   *
   * @param l The action listener.
   * @see removeActionListener
   */
  public synchronized void addActionListener(ActionListener l) {
    listeners.put(l,new Object());
  } // end addActionListener

  /**
   * Removes the specified action listener so that it no longer
   * receives action events.
   *
   * @param l The action listener.
   * @see addActionListener
   */
  public synchronized void removeActionListener(ActionListener l) {
    listeners.remove(l);
  } // end removeActionListener

  // --- KeyListener ------------------------------------------------
  /** Invoked when a key has been typed. */
  public void keyTyped(KeyEvent e) {
    // do nothing
  } // end keyTyped

  /** Invoked when a key has been pressed. */
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    int mod = e.getModifiers() | InputEvent.SHIFT_MASK;

    if ((key == KeyEvent.VK_ENTER) &&
        (mod == InputEvent.SHIFT_MASK)) {
      Enumeration en = listeners.keys();
      String cmd       = "\n";

      while (en.hasMoreElements()) {
        ActionListener al = (ActionListener)en.nextElement();
        ActionEvent ae;

        ae = new ActionEvent(this,ActionEvent.ACTION_PERFORMED,cmd);
        al.actionPerformed(ae);
      } // end while
      e.consume();
    } // end if
  } // end keyPressed

  /** Invoked when a key has been released. */
  public void keyReleased(KeyEvent e) {
    // do nothing
  } // end keyReleased

} // end OkTextField

/* ******************************************************************
   end of file
******************************************************************* */
