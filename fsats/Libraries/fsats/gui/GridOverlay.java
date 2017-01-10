package fsats.gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import fsats.gui.event.*;

/**
 * This component draws the overlays onto the grid - they should appear
 * above other components and can be arrows, etc.
 *
 * @author Daniel Rogers (drogers@arlut.utexas.edu)
 * @version 1.9, 06/10/99
 */
public class GridOverlay extends JComponent implements GridPaneScaledListener {

  private GridPane grid;
  private Vector drawOrders = new Vector();
  
  /**
   * Creates a new GridOverlay object with the given grid.
   *
   * @param grid GridPane to use
   */
  public GridOverlay (GridPane grid) {
    this.grid = grid;
    this.setPreferredSize(grid.getGridSize());
    grid.addGridPaneScaledListener(this);
    
    this.setOpaque(false);
  }
  
  /**
   * Called when a grid is scaled.
   *
   * @param e GridPaneScaledEvent to process
   */
  public void gridPaneScaled (GridPaneScaledEvent e) {
    this.setPreferredSize(grid.getGridSize());
    // :NOTE: probably need a redrawing of some sort here

    revalidate();
    repaint();
  }
  
  /**
   * Specifies the click range for this component. In this case, it
   * is always false, which will create a sort of "transparent click-through"
   * component.
   *
   * @param x int x value of the point in question
   * @param y int y value of the point in question
   * @return whether this component should respond to mouse events at the
   * given coordinate
   */
  public boolean contains (int x, int y) {
    return false;
  }
  
  /**
   * Paints the contents of the component.
   *
   * @param g Graphics context to use
   */
  public void paintComponent (Graphics g) 
  {
    super.paintComponent(g);
    for (int i = 0; i < drawOrders.size(); i++)
      renderArrow((ArrowInfo) drawOrders.elementAt(i), grid, g);
  }
  
  /**
   * Accepts 2 components that are in the gridPane and draws an arrow
   * between them.
   *
   * @param from Component
   * @param to Component
   * @param color Color object for the arrow color
   * @param text String to display with the arrow
   * @return object referencing the arrow
   */
  public Object drawArrow (Component from, Component to,
			   Color color, String text) {
    Object o = new ArrowInfo(from, to, color, text);
    drawOrders.addElement(o);
    repaint();
    return o;
  }

  /**
   * Removes any arrows found between the source and destination component.
   * Only removes them in both directions if desired.
   *
   * @param source Component source
   * @param destination Component destination
   * @param bothDirections boolean true if remove in both directions
   */
  public void removeArrows (Component from, Component to)
  {
    Enumeration arrows = drawOrders.elements();
    while (arrows.hasMoreElements()) {
      ArrowInfo ai = (ArrowInfo) arrows.nextElement();
      if ((ai.from==from && ai.to==to) || (ai.from==to && ai.to==from))
          removeArrow(ai);
    }
  }

  /**
   * Clears out all arrows.
   */
  public void clearArrows () {
    drawOrders.removeAllElements();
    repaint();
  }
  
  /**
   * Removes an arrow specified by the given reference object.
   *
   * @param ref object referencing the arrow
   */
  public void removeArrow (Object ref) {
    drawOrders.removeElement(ref);
    repaint();
  }
  
  /**
   * Renders the arrow between a couple of components on the given
   * graphics object. Now new and improved with lightening bolts!
   *
   * @param ai ArrowInfo object
   * @param grid GridPane object
   * @param g Graphics object
   */
  protected static void renderArrow (ArrowInfo ai, GridPane grid, Graphics g) {
    if (ai.color == null) {
      ai.color = Color.red;
    }
    Color origColor = g.getColor();
    g.setColor(ai.color);
    try {
      Point fromComp = ai.from.getLocation();
      Dimension fromSize = ai.from.getSize();
      Point toComp = ai.to.getLocation();
      Dimension toSize = ai.to.getSize();
      Point fromCenter = new Point(fromComp.x + fromSize.width / 2,
				   fromComp.y + fromSize.height /2);
      Point toCenter = new Point(toComp.x + toSize.width / 2,
				 toComp.y + toSize.height / 2);
      int a = toCenter.y - fromCenter.y;
      int b = toCenter.x - fromCenter.x;
      double theta = Math.atan((double)a / (double)b) + 2 * Math.PI;
      if (b < 0) { theta += Math.PI; }
      double bigr = Math.sqrt(a * a + b * b);
      double crickLength = Math.min(bigr * .045, 100);
      double nox  = (bigr - 20) * Math.cos(theta) + (double)fromCenter.x;
      double noy  = (bigr - 20) * Math.sin(theta) + (double)fromCenter.y;
      Point center = new Point((int)((bigr / 2) * Math.cos(theta)) + fromCenter.x,
			       (int)((bigr / 2) * Math.sin(theta)) + fromCenter.y);
      double boltAngle = 35 * Math.PI / 180;
      Point boltPoint =
	new Point((int)(crickLength * Math.cos(theta + boltAngle)) + center.x,
		  (int)(crickLength * Math.sin(theta + boltAngle)) + center.y);
      double boltTheta = // angle from fromCenter to bolt point
	Math.atan((crickLength * Math.sin(boltAngle)) /
		  (bigr / 2 + crickLength * Math.cos(boltAngle)));
      Point ap1 = new Point((int)(15 * Math.cos(theta + Math.PI / 2) + nox),
			    (int)(15 * Math.sin(theta + Math.PI / 2) + noy));
      Point ap2 = new Point((int)(15 * Math.cos(theta - Math.PI / 2) + nox),
			    (int)(15 * Math.sin(theta - Math.PI / 2) + noy));
      g.drawLine(fromCenter.x, fromCenter.y, boltPoint.x, boltPoint.y);
      g.drawLine(boltPoint.x, boltPoint.y, center.x, center.y);
      g.drawLine(center.x, center.y, toCenter.x, toCenter.y);
      g.drawLine(ap1.x, ap1.y, toCenter.x, toCenter.y);
      g.drawLine(ap2.x, ap2.y, toCenter.x, toCenter.y);
      if (ai.text != null && ai.text.length() > 0) {
	Point textLoc = new 
	  Point((int)((bigr * 0.4) * Math.cos(theta + boltTheta)) + fromCenter.x,
		(int)((bigr * 0.4) * Math.sin(theta + boltTheta)) + fromCenter.y);
	FontMetrics fm = g.getFontMetrics();
	int stringWidth = fm.stringWidth(ai.text);
	int stringHeight = fm.getAscent() + fm.getDescent();
	int textX = textLoc.x - stringWidth / 2;
	// Draw the box around the text
	Color drawColor = g.getColor();
	g.setColor(grid.getBackground());
	g.fillRect(textX - 2, textLoc.y - fm.getAscent() - 2,
		   stringWidth + 4, fm.getAscent() + fm.getDescent() + 4);
	g.setColor(drawColor);
	g.drawRect(textX - 2, textLoc.y - fm.getAscent() - 2,
		   stringWidth + 4, fm.getAscent() + fm.getDescent() + 4);
	// Draw the text
	g.drawString(ai.text, textX, textLoc.y);
      }
	  
    } catch (Exception e) {
      // draw nothing
    } finally {
      g.setColor(origColor);
    }
  }
}

/**
 * Class used to represent arrows for drawing. This is really just an
 * example at the moment and should probably be generalized at some point.
 */
class ArrowInfo {
  public Component from;
  public Component to;
  public Color color;
  public String text;
  
  public ArrowInfo (Component from, Component to, Color color,
		    String text) {
    this.from  = from;
    this.to    = to;
    this.color = color;
    this.text  = text != null ? text : "";
  }
}

