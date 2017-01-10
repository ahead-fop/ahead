/* ******************************************************************
   class    : ImageCanvas
*********************************************************************/

package ModelExplorer.Util;

import java.awt.*;

/**
 * A canvas that draws an image.
 */
public class ImageCanvas extends Canvas {

  // class variable declarations

  // instance variable declarations
  /** The image to display. */
  protected Image image;

  // constructors
  /** Create a new ImageCanvas object. */
  public ImageCanvas(Image image) {
    super();
    this.image = image;

    MediaTracker tracker = new MediaTracker(this);
    tracker.addImage(this.image,0);
    try {
      tracker.waitForID(0);
    } catch (InterruptedException e) {}
  } // end constructor ImageCanvas

  // methods
  /** returns the image to paint. */
  public Image getImage() {
    return image;
  } // end getImage

  public void paint(Graphics g) {
    g.drawImage(image,0,0,image.getWidth(this),
                image.getHeight(this),this);
  } // end paint

  public Dimension getPreferredSize() {
    return getMinimumSize();
  } // end getPreferredSize

  public Dimension getMinimumSize() {
    return new Dimension(image.getWidth(this),
                         image.getHeight(this));
  } // end getMinimumSize

} // end ImageCanvas

/* ******************************************************************
   end of file
*********************************************************************/
