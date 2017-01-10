/**
 * class: MyCellRenderer
 */

package ModelExplorer.MMatrixBrowser;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import ModelExplorer.Main;
									
class MyCellRenderer extends JVLabel implements ListCellRenderer {
     //final static ImageIcon longIcon = null;
     //final static ImageIcon shortIcon = null;

     // This is the only method defined by ListCellRenderer.
     // We just reconfigure the JLabel each time we're called.
	private Color myColor;
	public MyCellRenderer(){
		myColor=null;
	}
	public MyCellRenderer(Color myColor){
		this.myColor = myColor;
	}

     public Component getListCellRendererComponent(
       JList list,
       Object value,            // value to display
       int index,               // cell index
       boolean isSelected,      // is the cell selected
       boolean cellHasFocus)    // the list and the cell have the focus
     {
         String s = value.toString();
         setText(s);
         //setIcon((s.length() > 10) ? longIcon : shortIcon);
   	   /*if (isSelected) {
            setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
         else {
	       setBackground(list.getBackground());
	       setForeground(list.getForeground());
			}
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			   return this;
     }*/
			if (myColor!=null){
				list.setBackground(myColor);
			}
			setBackground(list.getBackground());
			setForeground(list.getForeground());
			setEnabled(list.isEnabled());
			if (Main.font==null)
				setFont(list.getFont());
			else
				setFont(Main.font);
			return this;
	 }
 }
