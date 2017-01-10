/* ******************************************************************
   class      : AntToolsPanel
   description: Swing application that provides a Gui to run the
					 composer tools.
*********************************************************************/
package ModelExplorer.Composer;

import ModelExplorer.SwingUtils.*;
import ModelExplorer.Main;
import ModelExplorer.Util.ModelFilter;

import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.input.DefaultInputHandler;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.util.JavaEnvUtils;

import javax.swing.*;
import javax.swing.tree.*;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import javax.swing.border.*;

public class AntToolsPanel extends ToolsPanel {

	public static File buildFile;
	private ToolsFromAnt tools;
	// Atomic Component declarations
	JButton[] buttons;
	AntToolOptions optionDialog;
	
	public AntToolOptions getOptions(){
		return optionDialog;
	}

    public void initAtoms() {
    	super.initAtoms();
		tools = new ToolsFromAnt(this, buildFile);
		Vector names = tools.getNames();
		Vector descriptions = tools.getDescriptions();
		buttons = new JButton[names.size()];
		for (int i=0; i<buttons.length; i++){
			buttons[i]=new JButton((String)names.get(i));
			buttons[i].setForeground(Color.blue);
		   buttons[i].setAlignmentX(JLabel.CENTER_ALIGNMENT);
			buttons[i].setToolTipText((String)descriptions.get(i));
		}
		
		optionDialog = null;
	}

	 int row;
	 //JScrollPane sp;
	// layout component declarations
	public void initLayout() {
		super.initLayout();
			
		buttonPanel = new JPanel();
		buttonPanel.setLayout( new FlowLayout(FlowLayout.CENTER,1,0));
	   //buttonPanel.setBorder( BorderFactory.createEtchedBorder());
		
		for (int i=0; i<buttons.length; i++){
			buttonPanel.add(buttons[i]);
		}
		checkerButton.setText("checker");
		buttonPanel.add(checkerButton);
		
		row=(new Double(Math.ceil((buttons.length+1)/6))).intValue();
		buttonPanel.setPreferredSize(new Dimension(500,30*row));
		//sp = new JScrollPane(buttonPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
									//JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		topPanel.add(buttonPanel);
	}

   public void initListeners(){
		super.initListeners();
		
		optionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (optionDialog==null){
					optionDialog=new AntToolOptions(me, tools);
				}
				optionDialog.setVisible(true);
			}
	   });
		
		for (int i=0; i<buttons.length; i++){
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					JButton button = (JButton)e.getSource();
					String targetName = button.getText();
					String equationName=(String)eqComboBox.getSelectedItem();
					if (!equationName.equals("Choose one")){
						   ToolsThread thread = new ToolsThread(me,AntToolsPanel.this,tools,targetName);
					}
					else{
						JOptionPane.showMessageDialog(null,
														"Please choose an equation first!",
														"Alert",
														JOptionPane.ERROR_MESSAGE);
					}
				}
			});	
		}
   }

    //constructor	
	 public AntToolsPanel(Main me){
		 super(me);
	 }
    
	 //set font. used by the Main
	 public void setFont(Font font){
		 super.setFont(font);
		 
		 buttonPanel.setPreferredSize(new Dimension(580,25*(row+1)*font.getSize()/12));
		
		 for (int i=0; i<buttons.length; i++){
			 buttons[i].setFont(font);
		 }
	 }

	 //set enable. used by the tool thread
	 public void setEnabled(boolean b){
		 super.setEnabled(b);
		 for (int i=0; i<buttons.length; i++){
			 buttons[i].setEnabled(b);
		 }
	}

}
