/* ******************************************************************
   class      : SeparatorCanvas
*********************************************************************/

package ModelExplorer.Util;

import java.awt.*;

/**
 * A canvas that draws a 3D separator line.
 */
public class SeparatorCanvas extends Canvas {

  // instance variable declarations
  private boolean vertical;

  // constructors
  public SeparatorCanvas() {
    vertical = false;
  } // end constructor SeparatorCanvas

  public SeparatorCanvas(boolean vertical) {
    this.vertical = vertical;
  } // end constructor SeparatorCanvas

  // methods
  public void paint(Graphics g) {
    g.setColor(SystemColor.control);
    g.draw3DRect(0,0,getSize().width-1,getSize().height-1,false);
  } // end paint

  public Dimension getPreferredSize() {
    if (vertical)
      return new Dimension(2,100);
    else
      return new Dimension(100,2);
    // end if
  } // end getPreferredSize

  public Dimension getMinimumSize() {
    return new Dimension(2,2);
  } // end getMinimumSize

  public Dimension getMaximumSize() {
    if (vertical)
      return new Dimension(2,10000);
    else
      return new Dimension(10000,2);
    // end if
  } // end getMaximumSize

} // end SeparatorCanvas

/* ******************************************************************
   end of file
*********************************************************************/
