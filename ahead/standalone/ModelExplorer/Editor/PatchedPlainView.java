/* ******************************************************************
   class    : PatchedPlainView
******************************************************************* */
package ModelExplorer.Editor;

import javax.swing.*;
import javax.swing.text.*;

/**
 * Does the same as PlainView, but returns the preferred span size
 * not to be less than the minimum span size.
 */
public class PatchedPlainView extends PlainView {

  // class variable declarations

  // instance variable declarations
  /** The minimum horizontal span size. */
  private float minSpan = -1;

  // constructors
  /** Constructs a new PatchedPlainView wrapped on an element. */
  public PatchedPlainView(Element elem) {
    super(elem);
  } // end constructor PatchedPlainView

  // methods
  /** Determines the minimum span for this view along an axis. */
  public float getMinimumSpan(int axis) {
    float f = super.getMinimumSpan(axis);
    if (axis == View.X_AXIS) minSpan = f;
    return f;
  } // end getMinimumSpan

  /** Determines the preferred span for this view along an axis. */
  public float getPreferredSpan(int axis) {
    float f = super.getPreferredSpan(axis);

//    if (axis == View.X_AXIS) {
//      System.out.println("Min Pref Span: " + minSpan + " " + f);
//    } // end if
    if (minSpan < 0) return f;
    return (f < minSpan) ? minSpan : f;
  } // end getPreferredSpan

} // end PatchedPlainView

/* ******************************************************************
   end of file
******************************************************************* */
