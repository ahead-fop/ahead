/**
 * class: MyCellRenderer2
 */

package ModelExplorer.MMatrixBrowser;

import java.util.*;
import java.awt.*;
import javax.swing.*;
									
class MyCellRenderer2 extends JLabel implements ListCellRenderer {
     //final static ImageIcon longIcon = null;
     //final static ImageIcon shortIcon = null;
	
     // This is the only method defined by ListCellRenderer.
     // We just reconfigure the JLabel each time we're called.

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
			list.setBackground(Color.lightGray);
			setBackground(list.getBackground());
			setForeground(list.getForeground());
			setEnabled(list.isEnabled());
			setFont(list.getFont());
         return this;
     }
 }
