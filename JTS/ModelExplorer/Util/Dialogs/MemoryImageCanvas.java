/* ******************************************************************
   class      : MemoryImageCanvas
*********************************************************************/

package ModelExplorer.Util.Dialogs;

import java.awt.*;
import java.awt.image.*;

public class MemoryImageCanvas extends Canvas {

  // instance variable declarations
  private Dimension size;
  private int[] pixels;
  private Image image;

  // constructors
  public MemoryImageCanvas(int[] pixels, Dimension size) {
    this.pixels = pixels;
    this.size   = new Dimension(size);
    image = createImage(new MemoryImageSource(this.size.width,
                        this.size.height,this.pixels,0,this.size.width));

  } // end constructor MemoryImageCanvas

  public MemoryImageCanvas(int[] pixels, int width, int height) {
    this.pixels = pixels;
    this.size   = new Dimension(width,height);
    image = createImage(new MemoryImageSource(this.size.width,
                        this.size.height,pixels,0,this.size.width));
  } // end constructor MemoryImageCanvas

  // methods
  public void paint(Graphics g) {
    g.drawImage(image,0,0,size.width,size.height,this);
  } // end paint

  public Dimension getPreferredSize() {
    return size;
  } // end getPreferredSize

  public Dimension getMinimumSize() {
    return size;
  } // end getMinimumSize

  public Dimension getMaximumSize() {
    return size;
  } // end getMaximumSize

} // end MemoryImageCanvas

/* ******************************************************************
   end of file
*********************************************************************/
