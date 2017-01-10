/* ******************************************************************
   class      : QueryBroeserGui
   description: Swing application that visualizes the model as a
					 relation.
*********************************************************************/

package ModelExplorer.Browser;

import ModelExplorer.Main;
import ModelExplorer.Util.*;
import ModelExplorer.Editor.*;
import ModelExplorer.Composer.*;
import ModelExplorer.SwingUtils.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.io.*;
import java.net.URL;

public class QueryBrowserGui extends SwingApp {

    // initialize constants used in the application

	private Main	me;
    private QueryBrowser	qbrowser;
	private Vector			columnHeaders;

   public void initConstants() {
      qbrowser = new QueryBrowser();
	   columnHeaders=new Vector();
   }

	// Atomic Component declarations
	JButton[]			levelButtons;
	JList[]				selectLists;
	JTable				resultTable;
	JButton				deselectButton;
	JLabel				resultTableLabel;
	MyTableModel		tableModel;
	TableSorter			sorter;

   public void initAtoms() {
      super.initAtoms();

		int keyLength=qbrowser.getNumKey();

      // initialize buttons
		levelButtons=new JButton[keyLength];
		for(int i=0; i<keyLength; i++){
			if (i==0){
				levelButtons[i]=new JButton("Layer");
				levelButtons[i].setAlignmentX(JButton.CENTER_ALIGNMENT);
				levelButtons[i].setFont(new Font("Courier", Font.BOLD, 12));
				levelButtons[i].setBorderPainted(false);
				levelButtons[i].setMargin(new Insets(-2,2,-2,2));
			}
			else if (i==keyLength-1){
				levelButtons[i]=new JButton("Artifact");
				levelButtons[i].setAlignmentX(JButton.CENTER_ALIGNMENT);
				levelButtons[i].setFont(new Font("Courier", Font.BOLD, 12));
				levelButtons[i].setBorderPainted(false);
				levelButtons[i].setMargin(new Insets(-2,2,-2,2));
			}
			else{
				levelButtons[i]=new JButton("Level "+(i+1));
				levelButtons[i].setAlignmentX(JButton.CENTER_ALIGNMENT);
				levelButtons[i].setFont(new Font("Courier", Font.BOLD, 12));
				levelButtons[i].setBorderPainted(false);
				levelButtons[i].setMargin(new Insets(-2,2,-2,2));
			}
		}

		deselectButton = new JButton("Deselect All");
		deselectButton.setFont(new Font("Courier", Font.BOLD, 12));
		deselectButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		deselectButton.setBorderPainted(false);
		deselectButton.setMargin(new Insets(-2,-2,-2,-2));

		// initialize lists
		selectLists=new JList[keyLength];
		for(int i=0; i<keyLength; i++){
			selectLists[i]=new JList(qbrowser.getLevelValues(i));
			selectLists[i].setSelectionMode(0);
		}

	  //initialize JTable
	  for(int i=0; i<keyLength; i++){
		  if (i==0){
				columnHeaders.add("Layer");
		  }
		  else {
			  if (i==keyLength-1){
					columnHeaders.add("Artifact");
			  }
			  else{
				  columnHeaders.add("Level "+(i+1));
			  }
		  }
	  }
	  tableModel=new MyTableModel();
	  tableModel.setDataVector(qbrowser.getRelation(), columnHeaders);
	  sorter = new TableSorter(tableModel);
	  resultTable = new JTable(sorter);
	  sorter.addMouseListenerToHeaderInTable(resultTable);
	  resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	  resultTable.setRowSelectionAllowed(true);
	  //resultTable.setFont(new Font("Serif", Font.PLAIN, 10));

	  //initialize JLabel
	  resultTableLabel = new JLabel("Query Result");
	  resultTableLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

	  //set font
	  setFont(null);

  }

   // layout component declarations
   JSplitPane		QueryBrowserPane;
   JPanel			SelectlistPanel;
   JPanel			ResultTablePanel;
   JScrollPane		TableScroll;
   JScrollPane[]	ListScrolls;

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

  public void initLayout() {
	  TableScroll = new JScrollPane(resultTable);
	  ResultTablePanel = initPanel(false);
	  ResultTablePanel.add(resultTableLabel);
	  ResultTablePanel.add(TableScroll);

	  int keyLength=qbrowser.getNumKey();
	  ListScrolls=new JScrollPane[keyLength];
	  for (int i=0; i<keyLength; i++){
		  ListScrolls[i]=new JScrollPane(selectLists[i]);
	  }
	  SelectlistPanel = initPanel(false);
	  for (int i=0; i<keyLength; i++){
		  SelectlistPanel.add(levelButtons[i]);
		  SelectlistPanel.add(ListScrolls[i]);
	  }
	  SelectlistPanel.add(deselectButton);
	  SelectlistPanel.setPreferredSize(new Dimension(120,400));

	  QueryBrowserPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                           SelectlistPanel, ResultTablePanel);
	  QueryBrowserPane.setOneTouchExpandable(true);
	  QueryBrowserPane.setDividerLocation(120);
	  //QueryBrowserPane.setPreferredSize(new Dimension(400,400));
   }

   public void initContentPane() {
     // ContentPane uses a BoxLayout of two columns
        ContentPane = initPanel(true);
    	ContentPane.add(QueryBrowserPane);
		ContentPane.setMaximumSize(new Dimension(500,500));
   }

   public void initListeners(){
		int keyLength=qbrowser.getNumKey();
		for (int i=0; i<keyLength; i++){
			levelButtons[i].addActionListener(new ActionListener() {
			   public void actionPerformed( ActionEvent e )
				{
			      JButton b=(JButton)e.getSource();
					int index=0;
					while(!b.equals(levelButtons[index])){
						index++;
					}
					selectLists[index].clearSelection();
					String tempStr;
					int[] tempInt=new int[selectLists.length];
					Vector   tempVec;
					ArrayList myPair=new ArrayList();
					for (int j=0; j<selectLists.length; j++){
						tempStr=(String)selectLists[j].getSelectedValue();
						if (tempStr!=null){
							myPair.add(new Pair(j,tempStr));
							tempInt[j]=1;
						}
					}
					tempVec=qbrowser.select(myPair.toArray());
					for(int j=0; j<selectLists.length; j++){
						if (tempInt[j]!=1){
							selectLists[j].setListData(qbrowser.getLevelValues(tempVec,j));
						}
					}
					tableModel=new MyTableModel();
					tableModel.setDataVector(tempVec, columnHeaders);
					sorter = new TableSorter(tableModel);
					resultTable.setModel(sorter);
					sorter.addMouseListenerToHeaderInTable(resultTable);
					me.status.setText("Status: query table......");
			   }
			});
		}
		deselectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				for (int j=0; j<selectLists.length; j++){
					selectLists[j].clearSelection();
					selectLists[j].setListData(qbrowser.getLevelValues(j));
				}
				tableModel=new MyTableModel();
				tableModel.setDataVector(qbrowser.getRelation(), columnHeaders);
				sorter = new TableSorter(tableModel);
				resultTable.setModel(sorter);
				sorter.addMouseListenerToHeaderInTable(resultTable);
				me.status.setText("Status: deselect all......");
			}
		});
		for (int i=0; i<keyLength; i++){
			selectLists[i].addListSelectionListener(new ListSelectionListener() {
			   public void valueChanged( ListSelectionEvent e )
				{
					JList l=(JList)e.getSource();
					int index=0;
					while(!l.equals(selectLists[index])){
						index++;
					}
					String tempStr;
					int[] tempInt=new int[selectLists.length];
					Vector   tempVec;
					ArrayList myPair=new ArrayList();
					for (int j=0; j<selectLists.length; j++){
						tempStr=(String)selectLists[j].getSelectedValue();
						if (tempStr!=null){
							myPair.add(new Pair(j,tempStr));
							tempInt[j]=1;
						}
					}
					if (myPair.size()!=0){
						tempVec=qbrowser.select(myPair.toArray());
						for(int j=0; j<selectLists.length; j++){
							if (tempInt[j]!=1){
								selectLists[j].setListData(qbrowser.getLevelValues(tempVec,j));
							}
						}
						tableModel=new MyTableModel();
						tableModel.setDataVector(tempVec, columnHeaders);
						sorter = new TableSorter(tableModel);
						resultTable.setModel(sorter);
						sorter.addMouseListenerToHeaderInTable(resultTable);
					}
					me.status.setText("Status: query table......");
			   }
			});
		}

		ListSelectionModel rowSM = resultTable.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;

				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (!lsm.isSelectionEmpty()) {
					int selectedRow = lsm.getMinSelectionIndex();
					StringBuffer sb=new StringBuffer();
					sb.append(me.modelDir);
					if (me.modelDir.charAt(me.modelDir.length()-1)!='/'){
						sb.append("/");
					}
					String fileName=null;
					for(int i=0; i<resultTable.getColumnCount(); i++){
						if (!resultTable.getValueAt(selectedRow, i).equals("<->")){
							sb.append(resultTable.getValueAt(selectedRow, i));
							sb.append("/");
							if (i==resultTable.getColumnCount()-1){
								fileName=(String)resultTable.getValueAt(selectedRow, i);
							}
						}
					}

					DisplayFile.file=new File(sb.toString());
					if (me.isReadOnly){
						DisplayFile.edit(me);
						DisplayFile.display(me);
					}
					else{
						DisplayFile.display(me);
						DisplayFile.edit(me);
					}
					if (me.editorTabbedPane.getSelectedIndex()==2)
						me.editorTabbedPane.setSelectedIndex(0);
					else if (me.editorTabbedPane.getSelectedIndex()==1){
						me.equationPanel.addStringToList(sb.toString());
					}
				}
			}
		});
   }

   public QueryBrowserGui() { super(); }

   public QueryBrowserGui(Main me){
	   super();
	   this.me = me;
   }

   public QueryBrowserGui(String AppTitle) { super(AppTitle); }

	//utility function to set font
	public void setFont(Font font){
		if (font==null)
			font=me.font;
		if (font!=null){
			for (int i=0; i<levelButtons.length; i++){
				levelButtons[i].setFont(font);
			}
			for (int i=0; i<selectLists.length; i++){
				selectLists[i].setFont(font);
			}
			resultTable.setFont(font);
			deselectButton.setFont(font);
			resultTableLabel.setFont(font);
		}
	}

   public static void main(String[] args) {
		new QueryBrowserGui("Model Tree Browser");
   }
}

// end
