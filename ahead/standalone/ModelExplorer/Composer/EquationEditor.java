/**
 *   class      : EquationEditor
 *   description: A GUI class for equation.
 */

package ModelExplorer.Composer;

import ModelExplorer.SwingUtils.*;
import ModelExplorer.Main;

import javax.swing.*;
import javax.swing.tree.*;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class EquationEditor extends SuperPanel {

	private String currentEqu="*.equation";

	// Atomic Component declarations
	JLabel				titleLabel;
	JList				   sourceList;
	JButton				newButton;
	JButton				removeButton;
	JButton				removeAllButton;
	JButton				saveButton;
	JButton				saveAsButton;
	JFileChooser		saveChooser;

	DefaultListModel     sourceModel; // Container to store strings to shown in the list.
	boolean              isEventEnable;

    public void initAtoms() {
    	super.initAtoms();

		titleLabel = new JLabel("                       Equation Editor (*.equation)                   ");
		//titleLabel.setForeground(Color.blue);
		titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		titleLabel.setBorder(BorderFactory.createEtchedBorder());

		sourceModel = new DefaultListModel();

		sourceList	=new JList(sourceModel);
		sourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sourceList.setSelectedIndex(0);

		newButton		= new JButton("New");
		newButton.setForeground(Color.blue);
		newButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		removeButton 	= new JButton("Del");
		removeButton.setForeground(Color.blue);
		removeButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		removeAllButton = new JButton("Del All");
		removeAllButton.setForeground(Color.blue);
		removeAllButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		saveButton		= new JButton("Save");
		saveButton.setForeground(Color.blue);
		saveButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		saveAsButton	= new JButton("SaveAs");
		saveAsButton.setForeground(Color.blue);
		saveAsButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

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
	JPanel  CommandPanel1;
	JPanel  CommandPanel2;
	JPanel  CommandPanel;
	JPanel  CenterPanel;
	JScrollPane TextScroll;

	public void initLayout() {
		TextScroll = new JScrollPane(sourceList);
		TextScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		CommandPanel1 = new JPanel();
		CommandPanel1.setLayout( new BoxLayout( CommandPanel1, BoxLayout.X_AXIS ) );
	   CommandPanel1.setBorder( BorderFactory.createEtchedBorder());
		CommandPanel1.add(eqButton);
		CommandPanel1.add(eqComboBox);
		CommandPanel2 = initPanel(true);
		CommandPanel2.add(newButton);
		CommandPanel2.add(removeButton);
		CommandPanel2.add(removeAllButton);
		CommandPanel2.add(saveButton);
		CommandPanel2.add(saveAsButton);
		CommandPanel = new JPanel();
		CommandPanel.setLayout( new BoxLayout( CommandPanel, BoxLayout.Y_AXIS ) );
	   CommandPanel.setBorder( BorderFactory.createEtchedBorder());
		CommandPanel.add(titleLabel);
		CommandPanel.add(CommandPanel2);

		CenterPanel = new JPanel();
		CenterPanel.setLayout( new BorderLayout() );
      CenterPanel.setBorder( BorderFactory.createEtchedBorder());
      CenterPanel.add(CommandPanel, BorderLayout.NORTH);
      CenterPanel.add(TextScroll, BorderLayout.CENTER);
	}

	public void initContentPane() {
	    // ContentPane uses a BoxLayout of two columns
	   ContentPane = initPanel(false);
	   ContentPane.add(CommandPanel1);
		ContentPane.add(CenterPanel);
	}

	//An utility function
	public void saveas(){
		try {
			saveChooser = new JFileChooser(me.modelDir);
			if (me.font!=null)
				setFont(me.font);
			else
				setFont(me.editorTabbedPane.getFont());
			CustomFileFilter filter = new CustomFileFilter();
			filter.addExtension("equation");
			filter.setDescription("Model equation files");
			saveChooser.setFileFilter(filter);
			int returnVal = saveChooser.showSaveDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
			     File file=saveChooser.getSelectedFile();
				  String temp=file.getAbsolutePath();
				  if (! temp.endsWith (".equation"))
					  file=new File(temp+".equation");
				 try{
					 BufferedWriter bw=new BufferedWriter(new FileWriter(file));
					 titleLabel.setText("                   Equation Editor("+file.getName()+")                   ");
					 String theStr;
					 for(int i=0; i<sourceModel.size(); i++){
						 theStr=(String)sourceModel.get(i);
						 bw.write(theStr+"\n");
					 }
					 bw.flush();
					 bw.close();
					 me.status.setText("Status: "+file.getName()+" was saved.");
					 currentEqu = file.getName();
					 me.treeBrowser.addObject(null, file, true, false);
					 me.toolsPanel.resetEq();
					 me.equationPanel.resetEq();
				 }catch(IOException ee){
				    ee.printStackTrace();
				 }

			}
		}catch(Exception ex){}
	}

    public void initListeners(){

		eqComboBox.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
				if (isEventEnable){
					JComboBox cb = (JComboBox)e.getSource();
					String equationName = (String)cb.getSelectedItem();
					if (equationName.endsWith (".equation")){
						try{
							sourceModel.clear();
							//sourceList.setListData(sourceVec);
							BufferedReader br=new BufferedReader(new FileReader(me.modelDir+"/"+equationName));
							titleLabel.setText("Equation Editor("+equationName+")");
							currentEqu=equationName;
							String theStr;
							theStr=br.readLine();
							while(theStr!=null){
								addStringToList(theStr);
								theStr=br.readLine();
							}
						}catch(IOException ioe){}
					}
				}
		    }
		});

		saveAsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				saveas();
			}
		});

		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				sourceModel.clear();
				//sourceList.setListData(sourceVec);
				currentEqu="*.equation";
				titleLabel.setText("Equation Editor("+currentEqu+")");
			}
		});

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if (currentEqu.equals("*.equation")){
					saveas();
				}
				else{
					try{
						 BufferedWriter bw=new BufferedWriter(new FileWriter(me.modelDir+"/"+currentEqu,false));
						 String theStr;
						 for(int i=0; i<sourceModel.size(); i++){
							 theStr=(String)sourceModel.get(i);
							 bw.write(theStr+"\n");
						 }
						 bw.flush();
						 bw.close();
						 me.status.setText("Status: "+currentEqu+" was saved.");
					}catch(IOException ee){
					   ee.printStackTrace();
					}
				}
			}
		});


	   removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				int index = sourceList.getSelectedIndex();
				if(index != -1){
					sourceModel.removeElementAt(index);
					//sourceList.setListData(sourceVec);
					//if(index < sourceVec.size())
					//	sourceList.setSelectedIndex(index);
				}
			}
	   });

	   removeAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				sourceModel.clear();
				//sourceList.setListData(sourceVec);
			}
	   });
     }

	 public EquationEditor(){super();}

     public EquationEditor(Main me) {
		 super(me);
	 }

	 public EquationEditor(String AppTitle) { super(AppTitle); }

	 public void addStringToList(String inString) {
		 int index = sourceList.getSelectedIndex();
		 if(index !=-1 && sourceModel.size()>0){
			index++;
			sourceModel.insertElementAt(inString, index);
			sourceList.setSelectedIndex(index);
			//sourceList.setListData(sourceVec);
		 }
		 else{
			sourceModel.addElement(inString);
			sourceList.setSelectedIndex(sourceModel.size()-1);
		 }

	}

	public static void main(String[] args) {
	 	new EquationEditor("Equation Editor");
	}

	//set font
	public void setFont(Font font){
		titleLabel.setFont(font);
		sourceList.setFont(font);
		super.setFont(font);
		newButton.setFont(font);
		removeButton.setFont(font);
		removeAllButton.setFont(font);
		saveButton.setFont(font);
		saveAsButton.setFont(font);
		//saveChooser.setFont(font);
	}

	//refresh the combobox for equations
	public void resetEq(){
		isEventEnable=false;
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
		isEventEnable=true;
	}

}
