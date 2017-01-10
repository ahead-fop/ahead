/* ******************************************************************
   class      : PrintDeviceWriter
   description: a class for printing text on a printing device
*********************************************************************/

package ModelExplorer.Editor.Streams;

import java.awt.*;
import java.io.*;
import java.util.*;

public class PrintDeviceWriter extends Writer {

  // class variable declarations
  protected Frame frame    = null;
  protected String lineSep = System.getProperty("line.separator");

  // instance variable declarations
  protected PrintJob pj;
  protected Graphics g;
  protected int fontHeight;
  protected int y,h;

  // constructors
  /**
   * Create a new print device writer.
   */
  public PrintDeviceWriter() {
    this(new Font("Courier",Font.PLAIN,12));
  } // end constructor PrintDeviceWriter

  /**
   * Create a new print device writer.
   *
   * @param font The font to use for printing.
   */
  public PrintDeviceWriter(Font font) {
    super();
    if (frame == null) (frame = new Frame()).pack();
    pj = frame.getToolkit().getPrintJob(frame,"print job",null);
    if (pj == null) return;
    g = pj.getGraphics();
    if (g == null) return;
    g.setFont(font);
    fontHeight = g.getFontMetrics().getHeight();
    y          = g.getFontMetrics().getAscent();
    h          = pj.getPageDimension().height -
                 g.getFontMetrics().getMaxDescent();
  } // end constructor PrintDeviceWriter

  // methods
  /**
   * Write a portion of an array of characters.
   */
  public void write(char cbuf[],int off,int len) throws IOException {
    char ch = lineSep.charAt(0);
    int l;
    
    while (len > 0) {
     l = 0;
     while ((l < len) && (cbuf[off+l] != ch)) l++;
     writeLine(cbuf,off,l);
     if (l < len) l++;
     if ((lineSep.length() > 1) && (l < len) &&
         (lineSep.charAt(1) == cbuf[off+l])) l++;
     off += l;
     len -= l;
    } // end while
  } // end write

  /**
   * Flush the stream.
   */
  public void flush() throws IOException {
    // Hm, nothing to do here.
  } // end flush

  /**
   * Close the stream, flushing it first.  Closing a previously-closed
   * stream, however, has no effect.
   */
  public void close() throws IOException {
    flush();
    if (g != null) g.dispose();
    if (pj != null) pj.end();
  } // end close

  /**
   * Write a portion of an array of characters, which represents a line.
   */
  protected void writeLine(char cbuf[],int off,int len) throws IOException {
    if ((pj == null) || (g == null)) return;
    if (y > h) {
      Font font = g.getFont();
      g.dispose();
      g = pj.getGraphics();
      g.setFont(font);
      y = g.getFontMetrics().getAscent();
    } // end if
    g.drawChars(cbuf,off,len,0,y);
    y += fontHeight;
  } // end writeLine

} // end PrintDeviceWriter

/* ******************************************************************
   end of file
*********************************************************************/
