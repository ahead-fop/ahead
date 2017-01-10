/* ******************************************************************
   class      : SuperPanel
   description: Swing application that provides a super common Gui to 
					 the equationEditor and toolsPanel
*********************************************************************/
package ModelExplorer.Composer;

import ModelExplorer.SwingUtils.*;
import ModelExplorer.Main;
import ModelExplorer.Util.ModelFilter;

import org.apache.tools.ant.*;

import javax.swing.*;
import javax.swing.tree.*;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import javax.swing.border.*;

public abstract class SuperPanel extends SwingApp {

	protected Main me;
	
	// Atomic Component declarations
	JButton						eqButton;
	JComboBox					eqComboBox;
	
    public void initAtoms() {
    	super.initAtoms();

		eqButton         = new JButton("Existing Equations:");
		eqButton.setBorderPainted(false);
		eqButton.setMargin(new Insets(-2,2,-2,2));
		eqButton.setToolTipText("Click this button to get the existing equations in the current model");

		eqComboBox      = new JComboBox();
		eqComboBox.addItem("Choose one");
		eqComboBox.setBackground(Color.white);
		resetEq();
	}

   public void initListeners(){
		
	   eqButton.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent e)
			{
				File file=new File(me.modelDir);
				if (file.isDirectory()){
					eqComboBox.removeAllItems();
					String[] list = equationFiles (file) ;
					for (int i=0; i<list.length; i++){
					    eqComboBox.addItem(list[i]);
					}
				}
			}
		});
		
     }
	
	 public SuperPanel(){super();}	
	
	 public SuperPanel(Main me){
		 super();
		 this.me = me;
	 }
	 
	 public SuperPanel(String AppTitle) { super(AppTitle); }

	 //set font. used by the Main
	 public void setFont(Font font){
		eqButton.setFont(font);
		eqComboBox.setFont(font);
	 }

	 //set enable. used by the tool thread
	 public void setEnabled(boolean b){
		eqComboBox.setEnabled(b);
		eqButton.setEnabled(b);
	}

	//refresh the combobox for equations
	public void resetEq(){
		File file=new File(me.modelDir);
		if (file.isDirectory()){
			String str=(String)eqComboBox.getSelectedItem();
			eqComboBox.removeAllItems();
			String[] list = equationFiles (file) ;
			for (int i=0; i<list.length; i++){
			    eqComboBox.addItem(list[i]);
			    if (list[i].equals(str)){
					 eqComboBox.setSelectedItem(list[i]);
			    }
			}
		}
	}
	
	//find all the euqtion files in the given directory
	public String[] equationFiles (File directory) {
		String[] list = directory.list () ;
		int put = 0 ;
		for (int n = 0 ; n < list.length ; ++n) {
		    if (! list[n].endsWith(".equation"))
				 continue ;
		    File file = new File (me.modelDir+File.separator+list [n]) ;
		    if (! (file.isFile() && file.canRead()))
				 continue ;
		    list [put++] = list [n] ;
		}

		String[] found = new String [put] ;
		System.arraycopy (list, 0, found, 0, put) ;
		return found ; 
	}

}
