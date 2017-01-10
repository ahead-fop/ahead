package fsats.gui;

import java.awt.*;
import java.util.*;

/**
 * Layout manager for use with GridPane.
 *
 * @author Daniel Rogers (drogers@arlut.utexas.edu)
 * @version 1.1, 04/09/99
 */
public class CoordinateLayout implements LayoutManager2 {
  
  // Component/constraints table
  private final Hashtable compConstraints = new Hashtable();
  
  /**
   * Constructs a new CoordinateLayout layout manager.
   */
  public CoordinateLayout () {}
  
  /**
   * Adds the specified component to the layout - we use the other
   * addLayoutComponent method.
   *
   * @param name String name
   * @param comp Component
   */
  public void addLayoutComponent (String name, Component comp) { }
  
  /**
   * Returns the minimum size for the layout.
   *
   * @param parent Container
   * @return Dimension object 
   */
  public Dimension minimumLayoutSize (Container parent) {
    return preferredLayoutSize(parent);
  }
   
  /**
   * Returns the maximum size for the layout.
   *
   * @param target Container
   * @return Dimension object
   */
  public Dimension maximumLayoutSize (Container target) {
    return preferredLayoutSize(target);
  }
  
  /**
   * Returns the preferred size for the layout.
   *
   * @param parent Container
   * @return Dimension object
   */
  public Dimension preferredLayoutSize (Container parent) {
    return parent.getSize();
  }
  
  /**
   * Adds a new component to the layout.
   *
   * @param comp Component to add
   * @param constraints CoordinateConstraints object
   */
  public void addLayoutComponent (Component comp, Object constraints) {
    if (compConstraints.containsKey(comp)) {
      compConstraints.remove(comp);
    }
    compConstraints.put(comp, constraints);
  }
  
  /**
   * Removes a component from the layout.
   *
   * @param comp Component to remove
   */
  public void removeLayoutComponent (Component comp) {
    if (compConstraints.containsKey(comp)) {
      compConstraints.remove(comp);
    }
  }
  

  /**
   * Returns the alignment along the x axis.
   *
   * @param target Container on which to operate
   * @return float alignment
   */
  public float getLayoutAlignmentX (Container target) {
    return Component.CENTER_ALIGNMENT;
  }
  
  /**
   * Returns alignment along the y axis.
   *
   * @param target Container on which to operate
   * @return float alignment
   */
  public float getLayoutAlignmentY (Container target) {
    return Component.CENTER_ALIGNMENT;
  }
  
  /**
   * Invalidates the layout - flush any cached stuff if applicable.
   *
   * @param target Container on which to operate
   */
  public void invalidateLayout (Container target) { }
  
  /**
   * Lays out the components for the given container.
   *
   * @param parent Container on which to operate
   */
  public void layoutContainer (Container parent) {
    Enumeration keys = compConstraints.keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      if (key instanceof Component) {
	Component comp = (Component) key;
	Object elem = compConstraints.get(comp);
	if (elem instanceof CoordinateConstraints) {
	  CoordinateConstraints cc = (CoordinateConstraints) elem;
	  double lat = cc.getLatitude();
	  double lon = cc.getLongitude();
	  
	  // Calculate where on the screen things should go.
	  Point loc = ((GridPane)parent).coordinateToPoint(lat, lon);

	  // Use the size of the component to figure out exactly where
	  // it should go - center it on the coordinate, in other words.
	  Dimension cs = comp.getPreferredSize();
	  int x = loc.x - (cs.width/2);
	  int y = loc.y - (cs.height/2);
	  comp.setBounds(x, y, cs.width, cs.height);
	} else if (elem instanceof Point) {

	  // If it is a point, just position it absolutely
	  Point     loc = (Point) elem;
	  Dimension cs  = comp.getPreferredSize();

	  comp.setBounds(loc.x, loc.y, cs.width, cs.height);
	}
      }
    }
  }
}
