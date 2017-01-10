/* ******************************************************************
   class    : BufferedPanel
******************************************************************* */

package ModelExplorer.Util;

import java.awt.*;

/**
 * A Panel that supports double buffering.
 */
public class BufferedPanel extends Panel {

  // instance variable declarations
  /** The offscreen buffer. */
  private Image offImage;
  /** The graphis context to the offscreen buffer. */
  private Graphics offGrfx;

  // constructors
  /**
   * Creates a new panel using the default layout manager.  The
   * default layout manager for all panels is the FlowLayout class.
   */
  public BufferedPanel() {
    super();
  } // end constructor BufferedPanel

  /**
   * Creates a new panel with the specified layout manager.
   *
   * @param layout The layout manager for this panel.
   */
  public BufferedPanel(LayoutManager layout) {
    super(layout);
  } // end constructor BufferedPanel

  // methods
  /** Update with double buffering. */
  public void update(Graphics g) {
    // Create the offscreen graphics context
    Dimension dim = getSize();

    if (offGrfx == null) {
      offImage = createImage(dim.width,dim.height);
      offGrfx = offImage.getGraphics();
    } // end if
    // Erase the previous image
    offGrfx.setColor(getBackground());
    offGrfx.fillRect(0,0,dim.width,dim.height);
    offGrfx.setColor(Color.black);
    // Passes our offscreen buffer to our paint method
    paint(offGrfx);
    // Draw the image onto the screen
    g.drawImage(offImage,0,0,null);
  } // end update

} // end BufferedPanel

/* ******************************************************************
   end of file
******************************************************************* */
