/* ******************************************************************
   class      : JToolBarLoader
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

class JToolBarLoader {
 // instance variable declarations
  //private ResourceBundle resources;
  private Hashtable commands;
  private ActionListener defaultAction;

  // constructors
  /**
   * Create a new JToolBarLoader object.
   */
  JToolBarLoader(Hashtable commands) {
     this.commands  = commands;
     defaultAction  = null;
  } // end constructor JToolBarLoader
  
  
	public void loader(){
		  Enumeration en = Main.buttonsForEditor.keys();
		  while(en.hasMoreElements()){
				String key=(String)en.nextElement();
				JButton b=(JButton)Main.buttonsForEditor.get(key);
				b.setActionCommand(key);

				ActionListener a = getAction(key);
				if(a != null) {
				  b.addActionListener(a);
				  if(a instanceof Action) {
				    Action action = (Action)a;
				    action.addPropertyChangeListener(new ActionChangedListener(b));
				    b.setEnabled(action.isEnabled());
				  } // end if
				}
				else {
				  b.setEnabled(false);
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
	
	// inner classes
	 private class ActionChangedListener implements PropertyChangeListener {
    // class variable declarations
    JButton button;
	 
    // constructors
    ActionChangedListener(JButton b) {
      super();
      button = b;
    } // end constructor ActionChangedListener

    // methods
    public void propertyChange(PropertyChangeEvent e) {
      String propertyName = e.getPropertyName();

      if(e.getPropertyName().equals(Action.NAME)) {
        String text = (String)e.getNewValue();
        //button.setText(text);
      } else if(propertyName.equals("enabled")) {
        Boolean enabledState = (Boolean)e.getNewValue();

        button.setEnabled(enabledState.booleanValue());
      } // end if
    } // end propertyCange

  } // end ActionChangedListener
  
}