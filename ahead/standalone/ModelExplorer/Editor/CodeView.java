/* ******************************************************************  
   class      : CodeView
   description: The view class for the CodeEditorKit.
*********************************************************************/
package ModelExplorer.Editor;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.*;

public class CodeView extends PatchedPlainView {

  // class variable declarations
  private static Class classBackup;

  // instance variable declarations
  protected AbstractSyntaxColoring coloring;
  private Segment tmpSeg = new Segment();
  private int lineIndex;
  private int remain;

  // constructors
  /** Create a new CodeView object. */
  public CodeView(Element elem, Class clazz)
    throws IllegalAccessException, InstantiationException {
    super(elem);
    coloring    = (AbstractSyntaxColoring)clazz.newInstance();
    classBackup = clazz;
  } // end constructor CodeView

  /**
   * Create a new CodeView object.
   *
   * @deprecated
   */
  public CodeView(Element elem) {
    super(elem);
    try {
      coloring = (AbstractSyntaxColoring)classBackup.newInstance();
    } catch (IllegalAccessException e1) {
      coloring = null;
    } catch (InstantiationException e2) {
      coloring = null;
    }
    // if one of the previous caught exception occured, then the
    // next line will cause a null pointer exception, to indicate,
    // that something is wrong.
    coloring.getColor(0);
  } // end constructor CodeView

  // methods
    /**
     * Renders a line of text, suppressing whitespace at the end
     * and exanding any tabs.  This is implemented to make calls
     * to the methods <code>drawUnselectedText</code> and
     * <code>drawSelectedText</code> so that the way selected and
     * unselected text are rendered can be customized.
     *
     * @param lineIndex the line to draw
     * @param g the graphics context
     * @param x the starting X position
     * @param y the starting Y position
     * @see #drawUnselectedText
     * @see #drawSelectedText
     */
    protected void drawLine(int lineIndex, Graphics g, int x, int y) {
      Document doc = getDocument();
      Element line = getElement().getElement(lineIndex);
      int p        = line.getStartOffset();
      int l        = Math.min(doc.getLength(),line.getEndOffset()) - p;

      this.lineIndex = lineIndex;
      this.remain    = 0;
      try {
        doc.getText(p,l,tmpSeg);
        coloring.setLine(tmpSeg.array,tmpSeg.offset,tmpSeg.count,lineIndex);
        super.drawLine(lineIndex,g,x,y);
        if (coloring.nextLineInvalid()) getContainer().repaint();
      } catch (BadLocationException e) {
        e.printStackTrace();
      }
    } // end drawLine

    /**
     * Renders the given range in the model as normal unselected text.
     *
     * @param g the graphics context
     * @param x the starting X coordinate
     * @param y the starting Y coordinate
     * @param p0 the beginning position in the model
     * @param p1 the ending position in the model
     * @returns the location of the end of the range
     * @exception BadLocationException if the range is invalid
     */
  protected int drawUnselectedText(Graphics g, int x, int y,
                                   int p0, int p1) throws BadLocationException {
    Document doc = getDocument();
    int l;

    if (remain > 0) {
      l      = remain;
      remain = 0;
    }
    else {
      l = coloring.hasMoreElements() ? coloring.getTokenLength() : 0;
    } // end if
    while (l > 0) {
      if (l > p1 - p0) {
        remain = l - (p1 - p0);
        l      = p1 - p0;
      } // end if
      doc.getText(p0,l,tmpSeg);
      g.setColor(coloring.getColor());
      x = Utilities.drawTabbedText(tmpSeg,x,y,g,this,p0);
      p0 += l;
      l = coloring.hasMoreElements() && remain == 0 ?
          coloring.getTokenLength() :
          0;
    } // end while
    return x;
  } // end drawUnselectedText

  protected int drawSelectedText(Graphics g, int x, int y,
                                 int p0, int p1) throws BadLocationException {
    Document doc = getDocument();
    int prel     = p0;
    int l;

    if (remain > 0) {
      l      = remain;
      remain = 0;
    }
    else {
      l = coloring.hasMoreElements() ? coloring.getTokenLength() : 0;
    } // end if
    while (l > 0) {
      if (l > p1 - prel) {
        remain = l - (p1 - prel);
        l      = p1 - prel;
      } // end if
      prel += l;
      l = coloring.hasMoreElements() && remain == 0 ?
          coloring.getTokenLength() :
          0;
    } // end while
    return super.drawSelectedText(g,x,y,p0,p1);
  } // end drawSelectedText

} // end CodeView

/* ******************************************************************
   end of file
*********************************************************************/
