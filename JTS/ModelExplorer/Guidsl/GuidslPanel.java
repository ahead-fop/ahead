/********************************************************************
class      : GuidslPanel
description: Swing application that provides a GUI to invoke GuiDsl
*********************************************************************/

package ModelExplorer.Guidsl;

import ModelExplorer.SwingUtils.*;
import ModelExplorer.Main;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.border.*;

public class GuidslPanel extends SwingApp {

	protected Main me;

	// Atomic Component declarations
	//JCheckBox					debugbox;
	//JCheckBox					printinputfilebox;
	JCheckBox					usingmodelmbox;
	JCheckBox					equationfileformatbox;
	JButton						modelfileButton;
	JComboBox					modelfileComboBox;
	JButton						browserButton;
	JButton						runButton;

    public void initAtoms() {
    	super.initAtoms();

    	//debugbox = new JCheckBox("debug");
    	//printinputfilebox = new JCheckBox("print input file");
    	usingmodelmbox = new JCheckBox("model mode uses model.m");
    	equationfileformatbox = new JCheckBox("equation file format");
    	equationfileformatbox.setSelected(true);

		modelfileButton         = new JButton("Existing model files:");
		modelfileButton.setBorderPainted(false);
		modelfileButton.setMargin(new Insets(-2,2,-2,2));
		modelfileButton.setToolTipText("Click this button to get the existing model files in the current model");

		modelfileComboBox      = new JComboBox();
		modelfileComboBox.addItem("Choose one");
		modelfileComboBox.setBackground(Color.white);
		resetModel();

		browserButton = new JButton("browser");
		runButton = new JButton("run");

	}

	//Utility function to initilize the JPanel
	JPanel initPanel(boolean horizontal) {
	   JPanel p = new JPanel();
	   if (horizontal)
		  p.setLayout( new BoxLayout( p, BoxLayout.X_AXIS ) );
	   else
		  p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
	   p.setBorder( BorderFactory.createEmptyBorder(1,2,1,2));
	   return p;
	}

	// layout component declarations
	JPanel  modelfilePanel;
	JPanel  optionPanel;
	TitledBorder titledBorder;
	TitledBorder titledBorder2;
	JPanel  CenterPanel;

	public void initLayout() {

		modelfilePanel = new JPanel();
		modelfilePanel.setLayout( new BoxLayout( modelfilePanel, BoxLayout.X_AXIS ) );
	    modelfilePanel.setBorder( BorderFactory.createEtchedBorder());
		modelfilePanel.add(modelfileButton);
		modelfilePanel.add(modelfileComboBox);
		modelfilePanel.add(browserButton);
		titledBorder = new TitledBorder("Model File:");
		modelfilePanel.setBorder(titledBorder);
		modelfilePanel.setForeground(Color.magenta);

		optionPanel = new JPanel();
		optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
		titledBorder2 = new TitledBorder("GuiDsl Options:");
		optionPanel.setBorder(titledBorder2);
		//optionPanel.add(debugbox);
		//optionPanel.add(printinputfilebox);
		optionPanel.add(usingmodelmbox);
		optionPanel.add(equationfileformatbox);

		CenterPanel = new JPanel();
		CenterPanel.setLayout( new BorderLayout() );
	  	CenterPanel.setBorder( BorderFactory.createEtchedBorder());
	  	CenterPanel.add(modelfilePanel, BorderLayout.NORTH);
	  	CenterPanel.add(optionPanel, BorderLayout.CENTER);
	  	CenterPanel.add(runButton, BorderLayout.SOUTH);

	}

	public void initContentPane() {
		// ContentPane uses a BoxLayout of two columns
	   ContentPane = initPanel(false);
	   ContentPane.add(CenterPanel);
	}

   public void initListeners(){

	   modelfileButton.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent e)
			{
				File file=new File(me.modelDir);
				if (file.isDirectory()){
					modelfileComboBox.removeAllItems();
					String[] list = modelFiles (file) ;
					for (int i=0; i<list.length; i++){
					    modelfileComboBox.addItem(list[i]);
					}
				}
			}
		});

		browserButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				 JFileChooser  chooser;
				 chooser = new JFileChooser(me.modelDir);
				 ModelFileFilter mff = new ModelFileFilter();
				 chooser.setFileFilter(mff);
				 int openVal = chooser.showOpenDialog(me);
			  	 if (openVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
			 	 	String mfile = file.getAbsolutePath();
			 	 	modelfileComboBox.addItem(mfile);
			 	 	modelfileComboBox.setSelectedItem(mfile);
				}
			}
		});

		usingmodelmbox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (usingmodelmbox.isSelected()){
					File file=new File(me.modelDir);
					if (isDefaultModelFile(file)){
						modelfileComboBox.setSelectedItem("model.m");
					}
					else{
						JOptionPane.showMessageDialog(null,
														"there is no model.m file in the current model!",
														"Alert",
														JOptionPane.ERROR_MESSAGE);
						usingmodelmbox.setSelected(false);
					}
				}
			}
		});

		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String modelName=(String)modelfileComboBox.getSelectedItem();
				if (!modelName.equals("Choose one")){
					GuidslThread thread = new GuidslThread(me,GuidslPanel.this);
				}
				else{
					JOptionPane.showMessageDialog(null,
													"Please choose an model file first!",
													"Alert",
													JOptionPane.ERROR_MESSAGE);
				}
			}
	    });



     }

	 public GuidslPanel(){super();}

	 public GuidslPanel(Main me){
		 super();
		 this.me = me;
	 }

	 public GuidslPanel(String AppTitle) { super(AppTitle); }

	 //set font. used by the Main
	 public void setFont(Font font){
		//debugbox.setFont(font);
		//printinputfilebox.setFont(font);
		usingmodelmbox.setFont(font);
		equationfileformatbox.setFont(font);
		modelfileButton.setFont(font);
		modelfileComboBox.setFont(font);
		browserButton.setFont(font);
		runButton.setFont(font);
	 }

	 //set enable. used by the tool thread
	 public void setEnabled(boolean b){
		//debugbox.setEnabled(b);
		//printinputfilebox.setEnabled(b);
		usingmodelmbox.setEnabled(b);
		equationfileformatbox.setEnabled(b);
		modelfileButton.setEnabled(b);
		modelfileComboBox.setEnabled(b);
		browserButton.setEnabled(b);
		runButton.setEnabled(b);
	}

	//refresh the combobox for equations
	public void resetModel(){
		File file=new File(me.modelDir);
		if (file.isDirectory()){
			String str=(String)modelfileComboBox.getSelectedItem();
			modelfileComboBox.removeAllItems();
			String[] list = modelFiles (file) ;
			for (int i=0; i<list.length; i++){
			    modelfileComboBox.addItem(list[i]);
			    if (list[i].equals(str)){
					 modelfileComboBox.setSelectedItem(list[i]);
			    }
			}
		}
	}

	//find all the model files in the given directory
	public String[] modelFiles (File directory) {
		String[] list = directory.list () ;
		int put = 0 ;
		for (int n = 0 ; n < list.length ; ++n) {
		    if (! list[n].endsWith(".m"))
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

	//find all the model files in the given directory
	public boolean isDefaultModelFile (File directory) {
		String[] list = directory.list () ;
		for (int n = 0 ; n < list.length ; ++n) {
			if (! list[n].equals("model.m"))
				 continue ;
			File file = new File (me.modelDir+File.separator+list [n]) ;
			if (! (file.isFile() && file.canRead()))
				 continue ;
			return true;
		}
		return false;
	}


}
