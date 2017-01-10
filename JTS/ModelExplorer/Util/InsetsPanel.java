/* ******************************************************************
   class      : InsetsPanel
   description: a panel which allows you to define a Inset in its
                constructors
*********************************************************************/

package ModelExplorer.Util;

import java.awt.*;

public class InsetsPanel extends Panel {

  // instance variable declarations
  private Insets insets = null;

  // constructors
  public InsetsPanel() {
    super();
    insets = new Insets(0,0,0,0);
  } // end constructor InsetsPanel

  public InsetsPanel(LayoutManager layout) {
    super(layout);
    insets = new Insets(0,0,0,0);
  } // end constructor InsetsPanel

  public InsetsPanel(Insets insets) {
    super();
    this.insets = insets;
  } // end constructor InsetsPanel

  public InsetsPanel(LayoutManager layout, Insets insets) {
    super(layout);
    this.insets = insets;
  } // end constructor InsetsPanel

  // methods
  public Insets getInsets() {
    return insets;
  } // end getInsets

} // end InsetsPanel

/* ******************************************************************
   end of file
*********************************************************************/
