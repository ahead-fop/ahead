/* ******************************************************************
   class      : JMenuLoader
******************************************************************* */
package ModelExplorer.Editor;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javax.swing.event.*;
import javax.swing.*;
import ModelExplorer.Main;

/* ******************************************************************
   top level class declaration
 *********************************************************************/

class JMenuLoader {
	
  /**
   * Suffix applied to the key used in resource file
   * lookups for a accelerator.
   */
  public static final String ACCEL_SUFFIX = "Accel";
  /**
   * Suffix applied to the key used in resource file
   * lookups for an action.
   */
  public static final String ACTION_SUFFIX = "Action";
   /** A reference to the java.awt.event.InputEvent class. */
  private static Class eventClass;
  /** A reference to the java.awt.event.KeyEvent class. */
  private static Class keyEventClass;
  	
  // instance variable declarations
  private ResourceBundle resources;
  private Hashtable commands;
  private ActionListener defaultAction;
  
   // static initializers
  static {
    try {
      keyEventClass = Class.forName("java.awt.event.KeyEvent");
    } catch (ClassNotFoundException e) { e.printStackTrace(); }
    try {
      eventClass = Class.forName("java.awt.Event");
    } catch (ClassNotFoundException e) { e.printStackTrace(); }
  } // end static initializer

  // constructors
  /**
   * Create a new JMenuLoader object.
   */
  JMenuLoader(ResourceBundle resources,Hashtable commands) {
	  this.resources = resources;
     this.commands  = commands;
     defaultAction  = null;
  } // end constructor JMenuLoader
  
	public void loader(){
		Enumeration en = Main.menuItemsForEditor.keys();
		while(en.hasMoreElements()){
			String key=(String)en.nextElement();
			JMenuItem mi=(JMenuItem)Main.menuItemsForEditor.get(key);
			//System.out.println("key= "+key);
			String astr  = getResourceString(key + ACTION_SUFFIX);
			if (astr == null) astr = key;
			//System.out.println("actionname= "+astr);
			String acc   = getResourceString(key + ACCEL_SUFFIX);
			mi.setActionCommand(astr);
			ActionListener a = getAction(astr);
			 // connect an action to the menu command
			if (a != null) {
			  mi.addActionListener(a);
			  if (a instanceof Action) {
			    Action action = (Action)a;
			    action.addPropertyChangeListener(new ActionChangedListener(mi));
			    mi.setEnabled(action.isEnabled());
			  } // end if
			} else {
			  mi.setEnabled(false);
			} // end if
			// set the accelerator
			if (acc != null) {
			  try {
			    String[] arr = tokenize(acc);
			    int value    = keyEventClass.getField(arr[0]).getInt(null);
			    int mod      = Event.CTRL_MASK;
			    for (int i=1; i<arr.length; i++) {
			      mod ^= eventClass.getField(arr[i]).getInt(null);
			    } // end for
			    mi.setAccelerator(KeyStroke.getKeyStroke(value,mod));
			  } catch (NoSuchFieldException e1) {
			    e1.printStackTrace();
			  } catch (SecurityException e2) {
			    e2.printStackTrace();
			  } catch (IllegalAccessException e3) {
			    e3.printStackTrace();
			  }
			} // end if
		} // end while
	} // end loader
	
	 /**
   * Return the Action with name cmd stored in the command hashtable.
   * If it doesn't exist, return the default action listener.
   *
   * @param cmd The action command of the associated toolbar button.
   */
	protected ActionListener getAction(String cmd) {
	  ActionListener a;

	  if(commands == null) a = null;
	    else a = (ActionListener)commands.get(cmd);
	  if(a == null) return defaultAction;
	    else return a;
	} // end getAction
	
	/**
   * Read a string from the resource bundle containing the menu
   * definition.
   */
	protected String getResourceString(String key) {
	  String str;

	  try {
	    str = resources.getString(key);
	  } catch (MissingResourceException mre) {
	    str = null;
	  }
	  return str;
	} // end getResourceString
	
	 /**
   * Take the given string and chop it up into a series
   * of strings on whitespace boundries.  This is useful
   * for trying to get an array of strings out of the
   * resource file.
   */
	protected String[] tokenize(String input) {
	  Vector v          = new Vector();
	  StringTokenizer t = new StringTokenizer(input);
	  String cmd[];

	  while (t.hasMoreTokens()) v.addElement(t.nextToken());
	  cmd = new String[v.size()];
	  for (int i=0; i<cmd.length; i++) cmd[i] = (String)v.elementAt(i);
	  return cmd;
	} // end tokenize
	
	 // inner classes
	private class ActionChangedListener implements PropertyChangeListener {

	  // class variable declarations
	  JMenuItem menuItem;

	  // constructors
	  ActionChangedListener(JMenuItem mi) {
	    super();
	    menuItem = mi;
	  } // end constructor ActionChangedListener

	  // methods
	  public void propertyChange(PropertyChangeEvent e) {
	    String propertyName = e.getPropertyName();

	    if (e.getPropertyName().equals(Action.NAME)) {
	      String text = (String)e.getNewValue();

	      menuItem.setText(text);
	    } else if (propertyName.equals("enabled")) {
	      Boolean enabledState = (Boolean)e.getNewValue();

	      menuItem.setEnabled(enabledState.booleanValue());
	    } else if (propertyName.equals("checked") &&
	               (menuItem instanceof JCheckBoxMenuItem)) {
	      boolean checkedState   = ((Boolean)e.getNewValue()).booleanValue();
	      JCheckBoxMenuItem cbmi = (JCheckBoxMenuItem)menuItem;

	      if (cbmi.getState() != checkedState)
	        cbmi.setState(checkedState);
	      // end if
	    } // end if
	  } // end propertyCange

	} // end ActionChangedListener
  
}