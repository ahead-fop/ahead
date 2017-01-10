package fsats.gui;

/**
 * Used when an arrow already exists between components and the user
 * wishes to know this fact.
 *
 * @author Daniel Rogers (drogers@arlut.utexas.edu)
 * @version 1.0, 04/23/99
 */
public class ArrowExistsException extends Exception {
  public ArrowExistsException (String s) {
    super(s);
  }
}
