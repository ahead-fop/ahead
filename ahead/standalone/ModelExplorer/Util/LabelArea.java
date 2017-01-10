/* ******************************************************************
   class      : LabelArea
   description: a label class which displays a label with multiple
                lines separated by \n
*********************************************************************/

package ModelExplorer.Util;

import java.awt.*;
import java.util.*;

public class LabelArea extends Panel {

  // class variable declarations

  public static final int LEFT = Label.LEFT;
  // indicates that the label should be left justified

  public static final int CENTER = Label.CENTER;
  // indicates that the label should be centered

  public static final int RIGHT = Label.RIGHT;
  // indicates that the label should be right justified

  // instance variable declarations
  private Vector labelVector;
  private int alignment;

  // constructors
  public LabelArea() {
    // constructs an empty labelarea
    super();
    labelVector = new Vector();
    alignment   = LEFT;
    setText("");
  } // end constructor LabelArea

  public LabelArea(String text) {
    // constructs a new labelarea with the specified string of text,
    // left justified
    super();
    labelVector = new Vector();
    alignment   = LEFT;
    setText(text);
  } // end constructor LabelArea

  public LabelArea(String text, int alignment) {
    // constructs a new labelarea that presents the specified string of text
    // with the specified alignment
    super();
    labelVector    = new Vector();
    this.alignment = alignment;
    setText(text);
  } // end constructor LabelArea

  // methods
  public int getAlignment() {
    return alignment;
  } // end getAlignment

  public synchronized void setAlignment(int alignment) {
    int n = labelVector.size();
    int i;
    Label label;

    for (i=0; i<n; i++) {
      label = (Label)labelVector.elementAt(i);
      label.setAlignment(alignment);
    } // end for
    this.alignment = alignment;
  } // end setAlignment

  public String getText() {
    String str;
    int n      = labelVector.size();
    int i;
    Label label;

    label = (Label)labelVector.elementAt(0);
    str   = label.getText();
    for (i=1; i<n; i++) {
      label = (Label)labelVector.elementAt(i);
      str = str+'\n'+label.getText();
    } // end for
    return str;
  } // end getText

  public synchronized void setText(String text) {
    StringTokenizer strTok = new StringTokenizer(text,"\n");
    int n                  = strTok.countTokens();
    int i;
    Label label;

    removeAll();
    labelVector.removeAllElements();
    setLayout(new GridLayout(n,1));
    for (i=0; i<n; i++) {
      label = new Label(strTok.nextToken());
      labelVector.addElement(label);
      add(label);
    } // end for
    validate();
  } // end setText

} // end LabelArea

/* ******************************************************************
   end of file
*********************************************************************/
