/* ******************************************************************
   class      : OptionDialog
*********************************************************************/

package ModelExplorer.Util;

import ModelExplorer.*;
import ModelExplorer.MMatrixBrowser.*;
import ModelExplorer.Util.*;
import ModelExplorer.Util.Dialogs.*;

import javax.swing.*;
import java.beans.*; //Property change stuff
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class OptionDialog extends JDialog {
	 private Main me;
	 private static boolean[] isChanged={false, false, false};

	 JTabbedPane myPanel;
	 //First Panel: File extension panel
	 JPanel		 fileExtPanel;  //First tab panel
	 JList       fileExtsList;
	 JList       extsAppliedList;
	 DefaultListModel  leftModel;
	 DefaultListModel  rightModel;
	 JButton     addButton;
	 JButton     addAllButton;
	 JButton     remButton;
	 JButton     remAllButton;
	 JLabel      leftLabel;
	 JLabel      rightLabel;
	 //Second Panel: MRU options panel
	 JPanel      MRUPanel; //Second tab panel
	 JLabel      MRULabel;
	 JComboBox   minMaxBox;
	 JLabel      tabLabel;
	 JTextField  tabSize;
	 JLabel		 fontLabel;
	 JLabel		 sizeLabel;
	 JComboBox	 fontBox;
	 JComboBox   sizeBox;
	 //Third Panel: Global filter
	 JPanel      GFilterPanel;
	 JLabel		 fileLabel;
	 JLabel		 dirLabel;
	 JTextField	 fileField;
	 JTextField	 dirField;
	 //bottom Panel
	 JPanel      bottomPanel;
	 JButton     okButton;
	 JButton     cancelButton;
	 JButton     appButton;
	 JTextArea   text;

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

	private void initFirstPanel(){
		 //init atomics
		 leftModel = new DefaultListModel();
		 fileExtsList = new JList(leftModel);
		 Object[] objs=Main.fileExtens.toArray();
		 for (int i=0; i<objs.length; i++){
			 leftModel.addElement(objs[i]);
		 }
		 fileExtsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 fileExtsList.setSelectedIndex(0);
		 JScrollPane sp1=new JScrollPane(fileExtsList);

		 rightModel = new DefaultListModel();
		 extsAppliedList	=new JList(rightModel);
		 objs=Main.appliedFileExtens.toArray();
		 for (int i=0; i<objs.length; i++){
			 leftModel.removeElement(objs[i]);
			 rightModel.addElement(objs[i]);
		 }
		 extsAppliedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		 JScrollPane sp2=new JScrollPane(extsAppliedList);
		 addButton = new JButton(" > ");
		 addAllButton = new JButton(">>");
		 remButton = new JButton(" < ");
		 remAllButton = new JButton("<<");
		 leftLabel = new JLabel("File types:");
		 rightLabel = new JLabel("Applied to matrix:");
		 //init layout
		 JPanel p1=initPanel(false);
		 p1.add(new JLabel(""));
		 p1.add(addButton);
		 p1.add(addAllButton);
		 p1.add(remButton);
		 p1.add(remAllButton);
		 JPanel p2=initPanel(false);
		 p2.add(leftLabel);
		 p2.add(sp1);
		 JPanel p3=initPanel(false);
		 p3.add(rightLabel);
		 p3.add(sp2);
		 fileExtPanel=initPanel(true);
		 fileExtPanel.add(p2);
		 fileExtPanel.add(p1);
		 fileExtPanel.add(p3);

	 }

	 JPanel otherPanel;
	 private void initSecondPanel(){
		 MRULabel = new JLabel("Max. of MRU model: ");
		 String[] value={"1", "2", "3","4", "5", "6", "7", "8"};
		 minMaxBox = new JComboBox(value);
		 minMaxBox.setSelectedIndex(MRUList.MRU_MAX-1);
		 minMaxBox.setBackground(Color.white);
		 JPanel mruPanel=initPanel(true);
		 mruPanel.add(MRULabel);
		 mruPanel.add(minMaxBox);

		 tabLabel = new JLabel("Tabulator size:          ");
		 tabSize = new JTextField(me.codeEditor.TAB_SIZE);
		 JPanel tabPanel=initPanel(true);
		 tabPanel.add(tabLabel);
		 tabPanel.add(tabSize);

		 fontLabel = new JLabel("Font:");
		 String[] fonts={"Courier", "Arial", "Serif", "Lucida Console"};
		 fontBox=new JComboBox(fonts);
		 fontBox.setSelectedIndex(0);
		 fontBox.setBackground(Color.white);
		 JPanel fontPanel=initPanel(false);
		 fontPanel.add(fontLabel);
		 fontPanel.add(fontBox);

		 sizeLabel = new JLabel("size:");
		 String[] sizes={"8", "10", "12", "14", "16", "18", "20", "22", "24", "28", "32", "36"};
		 sizeBox=new JComboBox(sizes);
		 sizeBox.setSelectedIndex(2);
		 sizeBox.setBackground(Color.white);
		 JPanel sizePanel=initPanel(false);
		 sizePanel.add(sizeLabel);
		 sizePanel.add(sizeBox);

		 JPanel textPanel=initPanel(true);
		 textPanel.add(fontPanel);
		 textPanel.add(sizePanel);

		 //tabChar = new JCheckBox("Store tabulator characters");
		 //tabChar.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		 MRUPanel=initPanel(false);
		 MRUPanel.add(mruPanel);
		 MRUPanel.add(tabPanel);
		 MRUPanel.add(textPanel);
		 //MRUPanel.add(tabChar);

		 otherPanel = new JPanel();
		 otherPanel.setLayout( new BorderLayout() );
		 otherPanel.setBorder( BorderFactory.createEtchedBorder());
		 otherPanel.add(MRUPanel, BorderLayout.NORTH);
		 //otherPanel.add(outputScroll, BorderLayout.CENTER);
	 }

	 JPanel FilterPanel;
	 private void initThirdPanel(){
		 fileLabel = new JLabel("Files to hide:");
		 fileField = new JTextField();
		 dirLabel = new JLabel("Directories to hide:");
		 dirField = new JTextField();
		 JPanel filePanel=initPanel(false);
		 filePanel.add(fileLabel);
		 filePanel.add(fileField);
		 JPanel dirPanel=initPanel(false);
		 dirPanel.add(dirLabel);
		 dirPanel.add(dirField);
		 JPanel textPanel=initPanel(false);
		 textPanel.add(new JLabel("Note: Using \";\" as separator between each item."));
		 textPanel.add(new JLabel("      For the files, the file extension can be given."));
		 textPanel.add(new JLabel("      For example, to hide \"*.zip\" files, \".zip\" should be given"));
		 textPanel.add(new JLabel("   "));

		 GFilterPanel=initPanel(false);
		 GFilterPanel.add(textPanel);
		 GFilterPanel.add(filePanel);
		 GFilterPanel.add(dirPanel);

		 FilterPanel = new JPanel();
		 FilterPanel.setLayout( new BorderLayout() );
		 FilterPanel.setBorder( BorderFactory.createEtchedBorder());
		 FilterPanel.add(GFilterPanel, BorderLayout.NORTH);
	 }

	 public void initTabbedPane(){
		 initFirstPanel();
		 initSecondPanel();
		 initThirdPanel();
		 myPanel=new JTabbedPane();
		 myPanel.setBorder( BorderFactory.createEtchedBorder());
		 myPanel.addTab("Matrix Filter", null, fileExtPanel);
		 myPanel.addTab("Global Filter", null, FilterPanel);
		 myPanel.addTab("Fonts,tab...", null, otherPanel);

	 }

	public JPanel ContentPane;
   public void initContentPane() {
		okButton = new JButton("   Ok   ");
		okButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		cancelButton = new JButton("Cancel");
		cancelButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		appButton = new JButton(" Apply ");
		appButton.setEnabled(false);
		appButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		bottomPanel = initPanel(true);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(6,0,0,0));
		bottomPanel.add(okButton);
		bottomPanel.add(cancelButton);
		bottomPanel.add(appButton);
		bottomPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		/*ContentPane = new JPanel();
      ContentPane.setLayout( new BorderLayout() );
      ContentPane.setBorder( BorderFactory.createEmptyBorder(2,//top
                                                             2,//left
                                                             2,//bottom
                                                             2 // right
                                                             ) );
      ContentPane.add(myPanel, BorderLayout.CENTER);
		ContentPane.add(bottomPanel, BorderLayout.SOUTH);*/
		ContentPane = initPanel(false);
		ContentPane.add(myPanel);
		ContentPane.add(bottomPanel);

	};

	public void initListeners() {
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String value= (String)fileExtsList.getSelectedValue();
				if(value!=null){
					leftModel.remove(fileExtsList.getSelectedIndex());
					rightModel.addElement(value);
					appButton.setEnabled(true);
					isChanged[0]=true;
				}
			}
	   });

		addAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				for (int i=0; i<leftModel.getSize(); i++){
					rightModel.addElement(leftModel.getElementAt(i));
				}
				leftModel.clear();
				appButton.setEnabled(true);
				isChanged[0]=true;
			}
	   });

		remButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String value= (String)extsAppliedList.getSelectedValue();
				if(value!=null){
					rightModel.remove(extsAppliedList.getSelectedIndex());
					leftModel.addElement(value);
					appButton.setEnabled(true);
					isChanged[0]=true;
				}
			}
	   });

		//second panel
		tabSize.addActionListener(new OtherAction());
		tabSize.getDocument().addDocumentListener(new MyDocumentListener());
		minMaxBox.addActionListener(new OtherAction());
		fontBox.addActionListener(new OtherAction());
		sizeBox.addActionListener(new OtherAction());

		//third panel
		fileField.addActionListener(new FilterAction());
		fileField.getDocument().addDocumentListener(new FilterDocumentListener ());
		dirField.addActionListener(new FilterAction());
		dirField.getDocument().addDocumentListener(new FilterDocumentListener ());

		remAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				for (int i=0; i<rightModel.getSize(); i++){
					leftModel.addElement(rightModel.getElementAt(i));
				}
				rightModel.clear();
				appButton.setEnabled(true);
			}
	   });

		appButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (isChanged[0]){
					me.appliedFileExtens.clear();
					for (int i=0; i<rightModel.getSize(); i++)
						me.appliedFileExtens.add(rightModel.getElementAt(i));
					me.reloadMatrix();
					isChanged[0]=false;
				}
				if (isChanged[1]){
					if (fileField.getText()==null){
						me.tmpFileFilter=null;
					}
					else{
						me.tmpFileFilter=new Vector();
						StringTokenizer st=new StringTokenizer(fileField.getText(),";");
						while(st.hasMoreTokens()){
							me.tmpFileFilter.add(st.nextToken());
						}
					}
					if (dirField.getText()==null){
						me.tmpDirFilter=null;
					}
					else{
						me.tmpDirFilter=new Vector();
						StringTokenizer st=new StringTokenizer(dirField.getText(),";");
						while(st.hasMoreTokens()){
							me.tmpDirFilter.add(st.nextToken());
						}
					}
					me.reset();
					isChanged[1]=false;
				}
				if (isChanged[2]){
					 Integer max = new Integer((String)minMaxBox.getSelectedItem());
					 me.setMRUMax(max.intValue());
					 Integer size=new Integer(tabSize.getText());
					 if (size == null){
						 tabSize.setText(me.codeEditor.TAB_SIZE);
					 }
					 else{
						 me.codeEditor.setTab(size.intValue());
					 }
					 String fontName=(String)fontBox.getSelectedItem();
					 int fontsize=Integer.parseInt((String)sizeBox.getSelectedItem());
					 me.setFont(fontName,fontsize);
					 isChanged[2]=false;
				}
				appButton.setEnabled(false);
			}
		});

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (appButton.isEnabled()){
					if (isChanged[0]){
						me.appliedFileExtens.clear();
						for (int i=0; i<rightModel.getSize(); i++)
							me.appliedFileExtens.add(rightModel.getElementAt(i));
						me.reloadMatrix();
						isChanged[0]=false;
					}
					if (isChanged[1]){
						if (fileField.getText()==null){
							me.tmpFileFilter=null;
						}
						else{
							me.tmpFileFilter=new Vector();
							StringTokenizer st=new StringTokenizer(fileField.getText(),";");
							while(st.hasMoreTokens()){
								me.tmpFileFilter.add(st.nextToken());
							}
						}
						if (dirField.getText()==null){
							me.tmpDirFilter=null;
						}
						else{
							me.tmpDirFilter=new Vector();
							StringTokenizer st=new StringTokenizer(dirField.getText(),";");
							while(st.hasMoreTokens()){
								me.tmpDirFilter.add(st.nextToken());
							}
						}
						me.reset();
						isChanged[1]=false;
					}
					if (isChanged[2]){
						 Integer max = new Integer((String)minMaxBox.getSelectedItem());
						 me.setMRUMax(max.intValue());
						 Integer size=new Integer(tabSize.getText());
						 if (size == null){
							 tabSize.setText("4");
							 me.codeEditor.setTab(4);
						 }
						 else{
							 me.codeEditor.setTab(size.intValue());
						 }
						 String fontName=(String)fontBox.getSelectedItem();
						 int fontsize=Integer.parseInt((String)sizeBox.getSelectedItem());
						 me.setFont(fontName,fontsize);
						 isChanged[2]=false;
					}
					appButton.setEnabled(false);
				}
				setVisible(false);
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});



	}

	 public void init() {
      //initConstants();                   // initialize constants
      //initAtoms();                       // initialize atoms
      //initLayout();                      // initialize layout
		initTabbedPane();
      initContentPane();                 // initialize content pane
      getContentPane().add(ContentPane); // set ContentPane of window
      initListeners();                   // initialize listeners
   }

   public OptionDialog(Main me) {
      super( me, "Model Explorer Options" );	         // set title
		this.me=me;
      init();                            // initialize hierarchy
      addWindowListener(	         // standard code to kill window
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    setVisible(false);
                }
            });
      //setLocationRelativeTo(me);
		setLocation(200,200);
		setSize(400,250);
		setVisible(true);
   }

	// Inner class
	class OtherAction extends AbstractAction{
		// constructors
		public OtherAction() {
			super("Other");
			setEnabled(true);
		} // end constructor CheckAction

	   // instance methods
		public void actionPerformed(ActionEvent e) {
			appButton.setEnabled(true);
			isChanged[2]=true;
		}
	}

	// Inner class
	class FilterAction extends AbstractAction{
		// constructors
		public FilterAction() {
			super("Filter");
			setEnabled(true);
		} // end constructor CheckAction

	   // instance methods
		public void actionPerformed(ActionEvent e) {
			appButton.setEnabled(true);
			isChanged[1]=true;
		}
	}

	//Inner class
	class FilterDocumentListener implements DocumentListener{
		public void insertUpdate(DocumentEvent e){
			appButton.setEnabled(true);
			isChanged[1]=true;
		}
		public void removeUpdate(DocumentEvent e){
			appButton.setEnabled(true);
			isChanged[1]=true;
		}
		public void changedUpdate(DocumentEvent e){
			appButton.setEnabled(true);
			isChanged[1]=true;
		}
	}

	//Inner class
	class MyDocumentListener implements DocumentListener{
		public void insertUpdate(DocumentEvent e){
			appButton.setEnabled(true);
			isChanged[2]=true;
		}
		public void removeUpdate(DocumentEvent e){
			appButton.setEnabled(true);
			isChanged[2]=true;
		}
		public void changedUpdate(DocumentEvent e){
			appButton.setEnabled(true);
			isChanged[2]=true;
		}
	}

}
