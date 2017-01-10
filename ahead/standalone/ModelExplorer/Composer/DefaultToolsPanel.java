/* ******************************************************************
   class      : DefaultToolsPanel
   description: Swing application that provides a Gui to run the
					 composer tools.
*********************************************************************/
package ModelExplorer.Composer;

import ModelExplorer.SwingUtils.*;
import ModelExplorer.Main;
import ModelExplorer.Util.ModelFilter;

import javax.swing.*;
import javax.swing.tree.*;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import javax.swing.border.*;

public class DefaultToolsPanel extends ToolsPanel {

	// Atomic Component declarations
	JButton						composerButton;
	JButton						reformButton;
	JButton						j2jButton;
	JButton						unmixinButton;
	JButton						j2cButton;
	ToolOptions					optionDialog;
	
	public ToolOptions getOptions(){
		return optionDialog;
	}
	
    public void initAtoms() {
    	super.initAtoms();
		
		composerButton = new JButton("COMPOSER");
		composerButton.setForeground(Color.blue);
		composerButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		composerButton.setToolTipText("Composes the layers specified by equation to build the tool");

		reformButton = new JButton("REFORM");
		reformButton.setForeground(Color.blue);
		reformButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		reformButton.setToolTipText("Reformat the Jak files to make it more readable"); 

		unmixinButton = new JButton("UNMIXIN");
		unmixinButton.setForeground(Color.blue);
		unmixinButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		unmixinButton.setToolTipText("Unmixin the files in the specified directory.");

		j2jButton = new JButton("J2J");
		j2jButton.setForeground(Color.blue);
		j2jButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		j2jButton.setToolTipText("Translates all Jak files to Java sources");

		j2cButton = new JButton("BUILD");
		j2cButton.setForeground(Color.blue);
		j2cButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		j2cButton.setToolTipText("Compile Java sources to class files");

		optionDialog = null;
	}

	// layout component declarations

	public void initLayout() {
      super.initLayout();
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );
	   buttonPanel.setBorder( BorderFactory.createEtchedBorder());
		
		buttonPanel.add(composerButton);
		buttonPanel.add(reformButton);
		buttonPanel.add(unmixinButton);
		buttonPanel.add(j2jButton);
		buttonPanel.add(j2cButton);
		buttonPanel.add(checkerButton);
		topPanel.add(buttonPanel);
	}

   public void initListeners(){
		super.initListeners();
		optionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (optionDialog==null){
					optionDialog=new ToolOptions(me);
				}
				optionDialog.setVisible(true);
			}
	   });

	   composerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String equationName=(String)eqComboBox.getSelectedItem();
				if (!equationName.equals("Choose one")){
					ComposerThread thread = new ComposerThread(me,DefaultToolsPanel.this);
				}
				else{
					JOptionPane.showMessageDialog(null,
													"Please choose an equation first!",
													"Alert",
													JOptionPane.ERROR_MESSAGE);
				}
			}
	   });
		
		reformButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String equationName=(String)eqComboBox.getSelectedItem();
				if (!equationName.equals("Choose one")){
					int option=JOptionPane.showConfirmDialog(null,
							"Are you sure to reform all the files in the dir "+
							equationName.substring(0,equationName.lastIndexOf('.'))+"?");
					if (option==0){
						ReformThread thread = new ReformThread(me,DefaultToolsPanel.this,false);
					}
				}
				else{
					JOptionPane.showMessageDialog(null,
													"Please choose an equation first!",
													"Alert",
													JOptionPane.ERROR_MESSAGE);
				}
			}
	   });

		unmixinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String equationName=(String)eqComboBox.getSelectedItem();
				if (!equationName.equals("Choose one")){
					int option=JOptionPane.showConfirmDialog(null,
							"Are you sure to unmixin all the jak files in the dir "+
							equationName.substring(0,equationName.lastIndexOf('.'))+"?");
					if (option==0){
						UnmixinThread thread = new UnmixinThread(me,DefaultToolsPanel.this,false);
					}
				}
				else{
					JOptionPane.showMessageDialog(null,
													"Please choose an equation first!",
													"Alert",
													JOptionPane.ERROR_MESSAGE);
				}
			}
	   });

		j2jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String equationName=(String)eqComboBox.getSelectedItem();
				if (!equationName.equals("Choose one")){
					int option=JOptionPane.showConfirmDialog(null,
							"Are you sure to transform all the jak files in the dir "+
							equationName.substring(0,equationName.lastIndexOf('.'))+" to java?");
					if (option==0){
						J2JThread thread = new J2JThread(me,DefaultToolsPanel.this,false);
					}
				}
				else{
				JOptionPane.showMessageDialog(null,
													"Please choose an equation first!",
													"Alert",
													JOptionPane.ERROR_MESSAGE);
				}
			}
	   });

		j2cButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String equationName=(String)eqComboBox.getSelectedItem();
				if (!equationName.equals("Choose one")){
					String filePath=me.modelDir+File.separator+equationName.substring(0,equationName.lastIndexOf('.'));
					File tmp=new File(filePath+File.separator+"build.xml");
					if (!tmp.isFile()){
						JOptionPane.showMessageDialog(null,
													"No build file available! Build abort!",
													"Alert",
													JOptionPane.ERROR_MESSAGE);
					}
					else{
						J2CThread thread = new J2CThread(me,DefaultToolsPanel.this,false);
					}
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

	 //constructor
	 public DefaultToolsPanel(Main me){
		 super(me);
	 }
    
	 //set font. used by the Main
	 public void setFont(Font font){
		 super.setFont(font);
		 composerButton.setFont(font);
		 reformButton.setFont(font);
		 unmixinButton.setFont(font);
		 j2jButton.setFont(font);
		 j2cButton.setFont(font);
	 }

	 //set enable. used by the tool thread
	 public void setEnabled(boolean b){
		 super.setEnabled(b);
		 composerButton.setEnabled(b);
		 reformButton.setEnabled(b);
		 unmixinButton.setEnabled(b);
		 j2jButton.setEnabled(b);
		 j2cButton.setEnabled(b);
	}

}
