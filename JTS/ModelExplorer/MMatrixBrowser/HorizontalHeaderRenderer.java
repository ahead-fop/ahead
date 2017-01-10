/**
 * class: HorizontalHeaderRenderer
 */

package ModelExplorer.MMatrixBrowser;

import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import ModelExplorer.Main;

public class HorizontalHeaderRenderer extends JList implements TableCellRenderer {
  public HorizontalHeaderRenderer() {
		setOpaque(true);
		setForeground(UIManager.getColor("TableHeader.foreground"));
		setBackground(UIManager.getColor("TableHeader.background"));
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		ListCellRenderer renderer = new HCellRenderer();
		setCellRenderer(renderer);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
		       boolean isSelected, boolean hasFocus, int row, int column) {
	setFont(table.getFont());
	String str = (value == null) ? "" : value.toString();
	BufferedReader br = new BufferedReader(new StringReader(str));
	String line;
	Vector v = new Vector();
	try {
	  while ((line = br.readLine()) != null) {
	    v.addElement(line);
	  }
	} catch (IOException ex) {
	  ex.printStackTrace();
	}
	setListData(v);
	setVisibleRowCount(1);
	return this;
  }
}
class HCellRenderer extends JLabel implements ListCellRenderer {

     public Component getListCellRendererComponent(
       JList list,
       Object value,            // value to display
       int index,               // cell index
       boolean isSelected,      // is the cell selected
       boolean cellHasFocus)    // the list and the cell have the focus
     {
         String s = value.toString();
         setText(s);
			setBackground(list.getBackground());
			setForeground(list.getForeground());
			setEnabled(list.isEnabled());
			if (Main.font==null)
				setFont(list.getFont());
			else
				setFont(Main.font);
			setHorizontalAlignment(JLabel.CENTER);
			setHorizontalTextPosition(JLabel.CENTER);
			setVerticalTextPosition(JLabel.CENTER);
			setVerticalAlignment(JLabel.CENTER);
			list.setVisibleRowCount(1);
			return this;
	 }
 }
