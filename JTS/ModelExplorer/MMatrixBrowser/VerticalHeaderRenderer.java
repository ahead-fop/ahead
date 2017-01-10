/**
 * class: VerticalHeaderRenderer
 */

package ModelExplorer.MMatrixBrowser;

import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class VerticalHeaderRenderer extends JList implements TableCellRenderer {
  public VerticalHeaderRenderer() {
		setOpaque(true);
		setForeground(UIManager.getColor("TableHeader.foreground"));
		setBackground(UIManager.getColor("TableHeader.background"));
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		ListCellRenderer renderer = new MyCellRenderer();
		//((JVLabel)renderer).setHorizontalAlignment(javax.swing.SwingConstants.TOP);
		//((JVLabel)renderer).setVerticalAlignment(javax.swing.SwingConstants.RIGHT);
		setCellRenderer(renderer);
  }
  
   public VerticalHeaderRenderer(Color myColor) {
		setOpaque(true);
		setForeground(UIManager.getColor("TableHeader.foreground"));
		setBackground(UIManager.getColor("TableHeader.background"));
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		ListCellRenderer renderer = new MyCellRenderer(myColor);
		//((JVLabel)renderer).setHorizontalAlignment(javax.swing.SwingConstants.TOP);
		//((JVLabel)renderer).setVerticalAlignment(javax.swing.SwingConstants.RIGHT);
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
	return this;
  }
}
