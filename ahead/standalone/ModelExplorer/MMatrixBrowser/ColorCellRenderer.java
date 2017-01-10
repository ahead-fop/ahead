/**
 * class: VerticalHeaderRenderer
 */

package ModelExplorer.MMatrixBrowser;

import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class ColorCellRenderer extends JList implements TableCellRenderer {
  public ColorCellRenderer() {
		setOpaque(true);
		//setForeground(UIManager.getColor("TableHeader.foreground"));
		setBackground(Color.lightGray);
		//setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		ListCellRenderer renderer = new MyCellRenderer2();
		((JLabel)renderer).setHorizontalAlignment(JLabel.LEFT);
		((JLabel)renderer).setVerticalAlignment(JLabel.TOP);
		setCellRenderer(renderer);
  }
  
  public Component getTableCellRendererComponent(JTable table, Object value,
		       boolean isSelected, boolean hasFocus, int row, int column) 
  {
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
