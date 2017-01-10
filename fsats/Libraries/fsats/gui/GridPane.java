package fsats.gui;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import fsats.gui.event.*;

/**
 * Grid component.
 *
 * @author Daniel Rogers (drogers@arlut.utexas.edu)
 * @version 1.23, 06/14/99
 */
public class GridPane extends JPanel {

  /**
   * Minimum pixel size for either axis of the grid - it won't get
   * smaller than this.
   */
  public static final int ABSOLUTE_MINIMUM = 100;
  
  private boolean showGridLines=true; 
  private CoordinateRect coordinateBounds; // Bounding coordinate rectangle
  private double gridDistance; // degrees between grid lines.
  private Dimension currSize; // Current perfect size - sanity check
  private Dimension origSize; // Original grid size
  private Color gridLineColor = Color.black; // Color of the grid lines
  private double currScale = 1.0; // Current scale for the grid
  private double currGridLineScale = 1.0; // Current grid line scale
  private Image gridBuf=null; // Double buffer for background grid
  private boolean buffer = false; // Whetner to buffer grid or not

  // Scale event listener queue
  private final Vector scaleListeners = new Vector(); 

  // ObjectPlaced listeners
  private final ObjectPlacedHandler placedHandler= new ObjectPlacedHandler(); 

  // Grid headers.
  private final LongHeader longHeader = new LongHeader(); 
  private final LatHeader latHeader = new LatHeader(); 

  // Message arrow layer.
  private final GridOverlay overlay = new GridOverlay(this);

  private Image terrain=null; // Original terrain object
  private Image scaledTerrain; // Scaled terrain image object
  private boolean showTerrain=false; // Show terrain Image or not
  private Object objectToPlace=null; // Object that is being placed
  private JPanel placePanel=null; // Overlay panel for placing objects
  
  /**
   * Constructs a GridPane with the given latitude/longitude bounding
   * rectangle.
   *
   * @param rect latitude/longitude rectangle pair
   */
  public GridPane (CoordinateRect rect) {
    this(rect, 1.0, 1.0);
  }
  
  /**
   * Constructs a GridPane with the given latitude/longitude bounding
   * rectangle and initial distance between latitude and longitude grids.
   *
   * @param rect latitude/longitude rectangle pair
   * @param gridLatDistance distance between latitude lines
   * @param gridLongDistance distance between longitude lines
   */
  public GridPane (CoordinateRect rect,
                   double gridLatDistance,
                   double gridLongDistance) {
    this(rect, gridLatDistance, gridLongDistance, new Dimension(500,500));
  }

  /**
   * Constructs a GridPane with the given latitude/longitude bounding
   * rectangle, initial distance between latitude and longitude grid lines,
   * and an initial size.
   *
   * @param rect latitude/longitude rectangle pair
   * @param gridLatDistance distance between latitude lines
   * @param gridLongDistance distance between longitude lines
   * @param size Dimension with initial size
   */
  public GridPane (CoordinateRect rect,
                   double gridLatDistance,
                   double gridLongDistance,
                   Dimension size) {
    this(rect, gridLatDistance, gridLongDistance, size, null);
  }

  /**
   * Constructs a GridPane with the given latitude/longitude bounding
   * rectangle, initial distance between latitude and longitude grid lines,
   * initial size, and terrain image.
   *
   * @param rect latitude/longitude rectangle pair
   * @param gridLatDistance distance between latitude lines
   * @param gridLongDistance distance between longitude lines
   * @param size Dimension with initial size
   * @param terrain Image for terrain
   */
  public GridPane (CoordinateRect rect,
                   double gridLatDistance,
                   double gridLongDistance,
                   Dimension size,
                   Image terrain)
  {
    super(true); // double buffer?
    this.coordinateBounds = rect;
    this.gridDistance = gridLatDistance;
    this.gridDistance = gridLongDistance;
    this.terrain = terrain;
    setGridDimension(size);

    // Set some initial values
    showTerrain = terrain != null;
    scaleTerrain();
    
    setOpaque(true);

    // Set the layout manager to be a CoordinateLayout
    setLayout(new CoordinateLayout());

    add(targetPlacer, new Point(0, 0), 0);
    add(overlay, new Point(0,0), 1);
  }

  /**
   * Sets the bounding coordinates to a new value.
   *
   * @param rect CoordinateRect rectangle
   */
  public void setCoordinateBounds (CoordinateRect rect) {
    this.coordinateBounds = rect;
    setGridDimension(currSize);
    scale(1.0);
  }

  /**
   * Sets the dimension in pixels to be used as a starting point for
   * the grid.
   *
   * @param d Dimension for grid
   */
  public void setGridDimension (Dimension d) {
    CoordinateRect r = coordinateBounds;
    double ratio = Math.cos((r.getMinLat()+r.getMaxLat())*Math.PI/360.0);
    ratio *= r.getHorizontalDistance()/r.getVerticalDistance();
    currSize = new Dimension((int)((double)d.height*ratio), d.height);
    origSize = currSize;
    scale(1.0);
    setPreferredSize(currSize);
    setMaximumSize(currSize);
  }

  /**
   * Returns the original grid dimension.
   *
   * @return Dimension object
   */
  public Dimension getGridDimension () {
    return origSize;
  }
    
  /**
   * Returns the bounding coordinates currently used in the grid.
   *
   * @return rect CoordinateRect rectangle
   */
  public CoordinateRect getCoordinateBounds () {
    return coordinateBounds;
  }
  
  /**
   * Sets whether or not to show the grid lines.
   *
   * @param b boolean true if grid lines are to be shown
   */
  public void setVisibleGridLines (boolean b) {
    showGridLines = b;
    resetBuffer();
    repaint();
  }

  /**
   * Returns whether or not to show the grid lines.
   *
   * @return boolean true if grid lines are showing
   */
  public boolean isVisibleGridLines () {
    return showGridLines;
  }

  /**
   * Returns the color currently being used for the grid lines.
   *
   * @return Color object
   */
  public Color getGridLineColor () {
    return gridLineColor;
  }
  
  /**
   * Sets the color used for the grid lines. The default is black.
   *
   * @param color Color object
   */
  public void setGridLineColor (Color color) {
    gridLineColor = color;
    if (gridLineColor == null) {
      gridLineColor = Color.black;
    }
    resetBuffer();
    repaint();
  }

  /**
   * Returns the current grid size.
   *
   * @return Dimension grid size
   */
  public Dimension getGridSize () {
    return currSize;
  }
  
  /**
   * Returns the current scale factor.
   *
   * @return double current scale factor
   */
  public double getCurrentScale () {
    return currScale;
  }

  /**
   * Sets the terrain image to be used.
   *
   * @param Image object for terrain
   */
  public void setTerrainImage (Image terrain) {
    this.terrain = terrain;
    scaleTerrain();
    repaint();
  }
  
  /**
   * Resets the scaling to 1.0 for the component.
   */
  public void resetScale () {
    currSize = origSize;
    currScale = 1.0;
    scale(1.0);
  }

  /**
   * Returns the current grid line distance scale factor.
   *
   * @return double current scale factor
   */
  public double getCurrentGridLineScale () {
    return currGridLineScale;
  }
  
  /**
   * Resets the grid line distance scaling to 1.0.
   */
  public void resetGridLineScale () {
    scaleGridLines(1.0/currGridLineScale);
  }
   
  /**
   * Scales the GridPane based on the current size, not the original size.
   *
   * @param factor double scale factor
   */
  public void scale (double factor) {
    Dimension d = new Dimension(currSize);
    d.width = (int)((double)d.width * factor);
    d.height = (int)((double)d.height * factor);
    if (factor < 1.0 && 
        (d.width < ABSOLUTE_MINIMUM || d.height < ABSOLUTE_MINIMUM)) {
      // er... just do nothing for the moment.
      return;
    }
    setPreferredSize(d);
    currSize = d;
    currScale *= factor;
    scaleTerrain();
    resetBuffer();
    
    redrawAll();
    
    // Finally, send out a notice that we have changed size
    processGridPaneScaledEvent(new GridPaneScaledEvent
                               (this, GridPaneScaledEvent.GRID_SCALED));
  }

  /**
   * Scales the distance between grid lines.
   *
   * @param factor double scale factor
   */
  public void scaleGridLines (double factor) {
    gridDistance   *= factor;
    currGridLineScale *= factor;
    resetBuffer();
    redrawAll();
    processGridPaneScaledEvent(new GridPaneScaledEvent
                               (this, GridPaneScaledEvent.GRID_LINES_SCALED));
  }
  

  /**
   * Returns whether or not the grid component is using buffering for
   * the grid background.
   *
   * @return boolean true if using buffering
   */
  public boolean isBuffered () {
    return buffer;
  }
  
  /**
   * Sets whether or not the grid component should use buffering. Default
   * is no - if set to yes, make sure the machine has a lot of memory.
   * A whole lot.
   *
   * @param b boolean true if buffering should be enabled
   */
  public void setBuffered (boolean b) {
    buffer = b;
    resetBuffer();
    redrawAll();
  }

  /**
   * Returns whether or not terrain is visible.
   *
   * @return boolean terrain visible
   */
  public boolean isTerrainVisible () {
    return showTerrain;
  }
  
  /**
   * Sets whether terrain is visible or not.
   *
   * @param show boolean true to show terrain
   */
  public void setTerrainVisible (boolean show) {
    showTerrain = show;
    scaleTerrain();
    repaint();
  }

  /**
   * Adds a new ObjectPlacedListener to the queue.
   *
   * @param l ObjectPlacedListener to add
   */
  public void addObjectPlacedListener (ObjectPlacedListener l) {
    placedHandler.addObjectPlacedListener(l);
  }
  
  /**
   * Removes a new ObjectPlacedListener from the queue.
   *
   * @param l ObjectPlacedListener to remove
   */
  public void removeObjectPlacedListener (ObjectPlacedListener l) {
    placedHandler.removeObjectPlacedListener(l);
  }

  /** A layer atop all other layers which is behaves as though it is
   *  not there except when placing a target.
   */
  private class TargetPlacer
      extends JComponent
      implements MouseListener
  {
      private Object objectToPlace = null;

      public TargetPlacer()
      {
          addMouseListener(this);
      }

      public void place(Object o)
      {
          if (o!=null) 
          {
              objectToPlace=o;
              setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
          }
      }

      /** Contain everything if placing, otherwise nothing. */
      public boolean contains(int x, int y)
      {
          return objectToPlace!=null;
      }

      public void mouseClicked(MouseEvent e) {}
      public void mouseEntered(MouseEvent e) {}
      public void mouseExited(MouseEvent e) {}
      public void mousePressed(MouseEvent e) {}
      public void mouseReleased(MouseEvent e) 
      {
          setCursor(Cursor.getDefaultCursor());

          // If button one pressed...
          if ((e.getModifiers() & MouseEvent.BUTTON1_MASK)!=0)
          {
              Location loc = pointToCoordinate(e.getPoint());
              placedHandler.fireObjectPlacedEvent(
                  new ObjectPlacedEvent(
                      this, 
                      ObjectPlacedEvent.OBJECT_PLACED, 
                      objectToPlace, 
                      loc));
          }
          objectToPlace = null;
          revalidate();
          repaint();
      }

  }

  private final TargetPlacer targetPlacer = new TargetPlacer();
  
  /**
   * Turns on the place object mode for the grid - when the object is
   * placed, an event will be fired to any listeners.
   *
   * @param o Object to place - just passed through to the event firing
   */
  public void placeObject (Object o) { targetPlacer.place(o); }
  
  /**
   * Translates a specific lat/long pair to its corresponding Point on the
   * GridPane.
   *
   * @param lat double latitude
   * @param long double longitude
   * @return Point on the GridMap (or off of it, possibly)
   */
  public Point coordinateToPoint (double lat, double lon) 
  {
    double dy = 
        (lat - coordinateBounds.getMinLat())/
            coordinateBounds.getVerticalDistance();
    double dx = 
        (lon - coordinateBounds.getMinLong())/
            coordinateBounds.getHorizontalDistance();
    int x = (int)((double)currSize.width*dx);
    int y = (int)((double)currSize.height*(1.0-dy));
    return new Point(x, y);
  }

  public double pointToLatitude(Point p)
  {
      double dy = 1.0 - (double)p.y/(double)currSize.height;
      return 
          coordinateBounds.getMinLat() + 
              coordinateBounds.getVerticalDistance()*dy;
  }

  public double pointToLongitude(Point p)
  {
      double dx = (double)p.x/(double)currSize.width;
      return
          coordinateBounds.getMinLong() + 
              coordinateBounds.getHorizontalDistance()*dx;
  }

  /**
   * Translates a specific point to a lat/long pair, returned in the first
   * two values of the double array that is returned.
   *
   * @param p point on the GridMap
   * @return double array with latitude in value 0 and longitude in value 1
   */
  public Location pointToCoordinate (Point p) {
      return new Location( pointToLatitude(p), pointToLongitude(p) );
  }
   
  /**
   * Renders the component.
   *
   * @param g Graphics object on which to paint
   */
  public void paintComponent (Graphics g) {
    super.paintComponent(g);
    
    if (buffer && gridBuf == null) {
      resetBuffer();
    }
    
    Dimension size = getSize();

    if (buffer) {
      g.drawImage(gridBuf, 0, 0, this);
    } else {
      renderGrid(g);
    }
    
  }

  /**
   * Adds a new GridPaneScaledListener to the queue.
   *
   * @param l GridPaneScaledListener
   */
  public void addGridPaneScaledListener (GridPaneScaledListener l) {
    scaleListeners.addElement(l);
  }
  
  /**
   * Removes a GridPaneScaledListener from the queue.
   *
   * @param l GridPaneScaledListener
   */
  public void removeGridPaneScaledListener (GridPaneScaledListener l) {
    scaleListeners.removeElement(l);
  }

  /**
   * Returns the longitude header to be used with this grid - good for
   * scroll pane use.
   *
   * @return JComponent longitude header
   */
  public JComponent getLongitudeHeader () { return longHeader; }
  
  /**
   * Returns the latitude header to be used with this grid - good for
   * scroll pane use.
   *
   * @return JComponent latitude header
   */
  public JComponent getLatitudeHeader () { return latHeader; }

  /**
   * Draws an arrow between 2 components.
   * :NOTE: this is probably a temporary
   * test method that will be generalized later.
   *
   * @param from from Component
   * @param to to Component
   * @param color Color object
   * @param text String text associated with arrow
   * @return arrow reference object
   */
  public void drawArrow (
      Component from, Component to, Color color, String text) 
  {
    overlay.drawArrow(from, to, color, text);
    repaint();
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
    overlay.removeArrows(from, to);
    repaint();
  }

  /**
   * Removes all of the arrows from the grid.
   */
  public void clearArrows () 
  {
    overlay.clearArrows();
    repaint();
  }
  
  /**
   * Attempts to redraw the entire component.
   */
  public void redrawAll () {
    revalidate();
    getLatitudeHeader().revalidate();
    getLongitudeHeader().revalidate();
    repaint();
    getLatitudeHeader().repaint();
    getLongitudeHeader().repaint();
  }
  
  /**
   * Fires a new GridPaneScaledEvent.
   *
   * @param e GridPaneScaledEvent to fire
   */
  protected void processGridPaneScaledEvent (GridPaneScaledEvent e) {
    Vector targets = (Vector) scaleListeners.clone();
    for (int i = 0; i < targets.size(); i++) {
      GridPaneScaledListener l = (GridPaneScaledListener)targets.elementAt(i);
      l.gridPaneScaled(e);
    }
  }
          
  /**
   * Sets up the internal buffer for the background grid.
   */
  protected void resetBuffer () {
    if (!buffer) {
      return;
    }
    // If there is a current buffer, flush it down the drain.
    if (gridBuf != null) {
      gridBuf.flush();
    }
    // Blah...hacks a plenty - just return if there is no peer created -
    // this should work better in 1.2 I think.
    Graphics g;
    try {
      gridBuf = createImage(currSize.width, currSize.height);
      g = gridBuf.getGraphics();
    } catch (Exception e) {
      return;
    }
    renderGrid(g);
  }

  /**
   * Class that is used for the latitude header.
   */
  class LatHeader extends JPanel {
    public LatHeader () { setOpaque(true); }

    public void paintComponent (Graphics g) {
      super.paintComponent(g);
      Rectangle r = g.getClipBounds();
      g.clearRect(r.x, r.y, r.width, r.height);
      double lat = coordinateBounds.getMinLat();
      Point p = coordinateToPoint(lat+=gridDistance, 0.0);
      while (p.y>0)
      {
          drawLatNum(g, p, lat);
          p = coordinateToPoint(lat+=gridDistance, 0.0);
      }

      Color oldColor = g.getColor();
      g.setColor(Color.blue);
      int x = getSize().width-1;
      g.drawLine(x, 0, x, currSize.height-1);
      g.setColor(oldColor);
    }        

    public Dimension getPreferredSize () {
      FontMetrics fm = getFontMetrics(getFont());
      double maxLat = coordinateBounds.getMaxLat();
      String l = Location.formatLatitude(90.0-1.0/3600.0);
      int w = fm.stringWidth(l);
      return new Dimension(w + 3, currSize.height);
    }

    /**
     * Adds the numeric latitude degree value for the given pixel position.
     *
     * @param g Graphics object on which to work
     * @param p integer pixel position
     * @return integer width of string that was drawn
     */
    private void drawLatNum (Graphics g, Point p, double lat) {
      String num = Location.formatLatitude(lat);
      FontMetrics fm = g.getFontMetrics();
      Color gColor = g.getColor();
      g.setColor(getForeground());
      int y = p.y + fm.getMaxAscent()/2;
      g.drawString(num, 3, y);
      g.setColor(gColor);
    }

  }

  /**
   * Class that is used for the longitude header.
   */
  class LongHeader extends JPanel {
    public LongHeader () { setOpaque(true); }
    
    public void paintComponent (Graphics g) {
      super.paintComponent(g);
      double lon = coordinateBounds.getMinLong();
      Point p = coordinateToPoint(0.0, lon+=gridDistance);
      while (p.x<currSize.width)
      {
          drawLongNum(g, p, lon);
          p = coordinateToPoint(0.0, lon+=gridDistance);
      }

      Color oldColor = g.getColor();
      g.setColor(Color.blue);
      int y = getSize().height-1;
      g.drawLine(0, y, currSize.width-1, y);
      g.setColor(oldColor);
    }

    public Dimension getPreferredSize () {
      FontMetrics fm = getFontMetrics(getFont());
      return new Dimension(currSize.width, fm.getMaxAscent() + 6);
    }

    /**
     * Adds the numeric longitude degree value for the given pixel position.
     *
     * @param g Graphics object on which to work
     * @param p integer pixel position
     * @return integer height of the string drawn
     */
    private void drawLongNum(Graphics g, Point p, double lon)
    {
      String num = Location.formatLongitude(lon);
      FontMetrics fm = g.getFontMetrics();
      Color gColor = g.getColor();
      g.setColor(getForeground());
      int x = p.x - fm.stringWidth(num)/2;
      g.drawString(num, x, fm.getMaxAscent() + 3);
      g.setColor(gColor);
    }

  }
  
  /**
   * Renders the grid on a graphics object.
   *
   * @param g Graphics object to use
   */
  protected void renderGrid (Graphics g) {
    // First thing would be to scale/draw the terrain image, if there is
    // such a thing. Since there is not at the moment, don't worry about it
    if (showTerrain && scaledTerrain != null) {
      g.drawImage(scaledTerrain, 0, 0, this);
    }
    
    // Draw a bounding box, for reference right now.
    g.setColor(Color.blue);
    g.drawRect(0, 0, currSize.width-1, currSize.height-1);
    g.setColor(gridLineColor);

    // Draw grid lines if necessary
    if (showGridLines) {
          double lat = coordinateBounds.getMinLat();
          double lon = coordinateBounds.getMinLong();
          double dlat = coordinateBounds.getVerticalDistance();
          double dlon = coordinateBounds.getHorizontalDistance();

          for (double delta=0.0; delta<dlat; delta+=gridDistance)
          {
              Point p = coordinateToPoint(lat+delta, lon);
              g.drawLine(0, p.y, currSize.width, p.y);
          }
          for (double delta=0.0; delta<dlon; delta+=gridDistance)
          {
              Point p = coordinateToPoint(lat, lon+delta);
              g.drawLine(p.x, 0, p.x, currSize.height);
          }
    }
  }
                        
  /**
   * Scales terrain to the current size, if it needs to.
   */
  protected void scaleTerrain () {
    if (showTerrain && terrain != null) {
      if (scaledTerrain == null ||
          (terrain.getWidth(this) == currSize.width &&
           terrain.getHeight(this) == currSize.height)) {
        scaledTerrain = terrain;
      }
      if (!(scaledTerrain.getWidth(this) == currSize.width &&
            scaledTerrain.getHeight(this) == currSize.height)) {
        scaledTerrain = terrain.getScaledInstance(currSize.width,
                                                    currSize.height,
                                                    Image.SCALE_FAST);
      }
    }
  }

  public void addTop(Component comp, CoordinateConstraints constraint)
  { 
      add(comp, constraint, 2); 
  }

  public void addBottom(Component comp, CoordinateConstraints constraint)
  { 
      add(comp, constraint); 
  }
  
} // GridPane
