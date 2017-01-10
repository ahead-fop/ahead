/**
 *   class      : MyTableModel
 *   description: Change cell editable to uneditable 
 */
package ModelExplorer.Util;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class MyTableModel extends DefaultTableModel{
	public boolean isCellEditable(int row, int column){
		return false;
	}
}