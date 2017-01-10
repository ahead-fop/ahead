/* ******************************************************************
   class      : MMatrixGui
   description: Swing application that visualizes the model as a
					 multi dimentional matrix.
*********************************************************************/

package ModelExplorer.MMatrixBrowser;

import ModelExplorer.Main;
import ModelExplorer.Editor.*;
import ModelExplorer.Browser.*;
import ModelExplorer.SwingUtils.*;
import ModelExplorer.Util.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.io.*;
import java.net.URL;
import java.beans.*;

import mmatrix.*;

public class MMatrixGui extends SwingApp {

    // initialize constants used in the application
	 Main			me;
    Matrix			matrix;
	 Vector			columnHeaders;
	 TreeMap		navigator;
	 VerticalHeaderRenderer renderer;
	 VerticalHeaderRenderer greenRenderer;
	 int selectedColIndex;

	public static JakMatrix jakMatrix=null;
	public static SubMatrix subMatrix=null;
	public static boolean isSub=false;
	public static int       theRow;

   public void initConstants() {
		if (me.isReduced){
			matrix = me.rmatrix;
		}
		else{
			if (me.matrixIndex==0){
				if(isSub)
					matrix = subMatrix;
				else
					matrix = new MMatrix();
			}
			else
				matrix = jakMatrix;
		}
		columnHeaders=matrix.getColumnHeaders();
		navigator =matrix.getNavigator();
		selectedColIndex = -1;
		renderer = new VerticalHeaderRenderer();
		greenRenderer = new VerticalHeaderRenderer(Color.orange);
   }


	MyTableModel		tableModel;
	JTable				matrixTable;
	JLabel				tableLabel;
	PopupMenu			popup;
	MenuItem				mitem;
	TableSorter			sorter;

	//set font
	public void setFont(Font font){
		if (font==null)
			font=me.font;
		if (font!=null){
			tableLabel.setFont(font);
			matrixTable.setFont(font);
		}
	}

   public void initAtoms() {
      super.initAtoms();

	  tableModel=new MyTableModel();
	  tableModel.setDataVector(matrix.getMatrix(), columnHeaders);
	  //matrixTable = new JTable(tableModel);
	  sorter = new TableSorter(tableModel);
	  matrixTable = new JTable(sorter);
	  sorter.addMouseListenerToHeaderInTable(matrixTable);

	  Enumeration en = matrixTable.getColumnModel().getColumns();
	  int init=0;
	  TableColumn tempTC;
	  while (en.hasMoreElements()) {
		  if (init==0){
			  tempTC = (TableColumn)en.nextElement();
			  tempTC.setHeaderRenderer(renderer);
			  tempTC.setCellRenderer(new ColorCellRenderer());
			  tempTC.setPreferredWidth(20);
			  init++;
		  }
		  else if (init==1){
			  tempTC = (TableColumn)en.nextElement();
			  tempTC.setHeaderRenderer(new HorizontalHeaderRenderer());
			  if (me.matrixIndex<1)
					tempTC.setPreferredWidth(150);
			  else
				  if (me.matrixIndex==1)
					  tempTC.setPreferredWidth(200);
			     else
					  tempTC.setPreferredWidth(250);
			  //tempTC.sizeWidthToFit();
			  init++;
		  }
		  else{
			  tempTC = (TableColumn)en.nextElement();
			  tempTC.setHeaderRenderer(renderer);
			  tempTC.setPreferredWidth(20);
		  }
	  }
	  matrixTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	  //matrixTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	  matrixTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	  matrixTable.setRowSelectionAllowed(true);
	  //matrixTable.setColumnSelectionAllowed(true);
	 //matrixTable.setCellSelectionEnabled(true);
	  //matrixTable.setSelectionBackground(Color.green);
	  matrixTable.getTableHeader().setReorderingAllowed(false);

      enableEvents(AWTEvent.MOUSE_EVENT_MASK);

	  //initialize JLabel
		if (me.matrixIndex<1){
		  if (isSub)
			  tableLabel = new JLabel(subMatrix.getSubPath()+" SubModel Navigator");
		  else
			  tableLabel = new JLabel("Model Navigator");
		}
	  else
		   tableLabel = new JLabel("Jak Navigator");
	  tableLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	  setFont(null);
  }

   // layout component declarations
   JScrollPane TableScroll;
   JPanel      TablePanel;

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
	  TableScroll = new JScrollPane(matrixTable);
	  TablePanel = initPanel(false);
	  TablePanel.add(tableLabel);
	  TablePanel.add(TableScroll);
   }

   public void initContentPane() {
     // ContentPane uses a BoxLayout of two columns
      ContentPane = initPanel(true);
   	  ContentPane.add(TablePanel);
	  ContentPane.setMaximumSize(new Dimension(600,600));
   }

   //utility function to create the table dialog
   public void updateDialog(int theRow){
		if (((String)matrixTable.getValueAt(theRow,0)).equals("+")){
			subMatrix=(SubMatrix)(matrix.getNested().get((String)matrixTable.getValueAt(theRow,1)));
			me.isSub=true;
			isSub=true;
			me.subMatrixGui=new MMatrixGui(me);
			isSub=false;
			me.browserTabbedPane.setComponentAt(2, me.subMatrixGui.ContentPane);
			me.browserTabbedPane.setSelectedIndex(2);
			me.upButton.setEnabled(true);
		}
		else{
			if (me.matrixIndex<1){
				me.matrixIndex=1;
			   JakMatrix.superMap=new HashMap();
				if (matrix instanceof SubMatrix)
					JakMatrix.fileName=subMatrix.getSubPath()+matrixTable.getValueAt(theRow,1);
				else
				   JakMatrix.fileName=File.separator+matrixTable.getValueAt(theRow,1);
			}
			else
				me.matrixIndex++;
			jakMatrix=new JakMatrix((Object[])navigator.get((String)matrixTable.getValueAt(theRow,1)),columnHeaders,me);
			switch (me.matrixIndex){
			case 1:
				me.jakMatrixGui1 = new MMatrixGui(me);
				//me.browserTabbedPane.remove(2);
				//me.browserTabbedPane.add(me.jakMatrixGui1.ContentPane,2);
				/*DSB*/ // I deleted the above 2 lines and replaced it with 
				        // one -- this avoids an event for remove and add
	                        me.browserTabbedPane.setComponentAt(2,me.jakMatrixGui1.ContentPane);

				me.browserTabbedPane.setTitleAt(2,"Navigator");
				me.browserTabbedPane.setSelectedIndex(2);
				me.browserTabbedPane.fire(); /* DSB */

				if (me.highestIndex<2){
					me.highestIndex=1;
					me.forwardButton.setEnabled(false);
				}
				me.backButton.setEnabled(true);
				break;
			case 2:
				me.jakMatrixGui2 = new MMatrixGui(me);
				//me.browserTabbedPane.remove(2);
				//me.browserTabbedPane.add(me.jakMatrixGui2.ContentPane,2);
				/*DSB*/ // I deleted the above 2 lines and replaced it with
				        // one -- to avoid an event for remove
	                        me.browserTabbedPane.setComponentAt(2,me.jakMatrixGui2.ContentPane);

				me.browserTabbedPane.setTitleAt(2,"Navigator");
				me.browserTabbedPane.setSelectedIndex(2);
				me.browserTabbedPane.fire(); /* DSB */

				if (me.highestIndex<2)
					me.highestIndex=2;
				me.backButton.setEnabled(true);
				me.forwardButton.setEnabled(false);
			}
		}
   }

	//utility functions for actionlistener
	private void popAction(MouseEvent evt, boolean pop){

		int nRow = matrixTable.rowAtPoint(new Point(evt.getX(),evt.getY()));
		int nCol = matrixTable.columnAtPoint( new Point(evt.getX(),evt.getY()));
		if (me.matrixIndex<1)
			theRow = nRow;

		if (evt.isPopupTrigger()&&!(nCol==0||nCol==1)){
			popup = new PopupMenu();
			final String theArt=(String)matrixTable.getValueAt(nRow,1);
			Object obj=((Object[])navigator.get(theArt))[nCol];
			if (obj==null ||(obj instanceof String)){
				mitem = new MenuItem("- Empty -");
				popup.add(mitem);
			}
			else{
			    Iterator i = ((MMOutput)obj).values().iterator();
			    if (i.hasNext()){
				while (i.hasNext()) {
						 NamedVector nv = (NamedVector) i.next();
						 if (!nv.getName().equals("super")){
								mitem = new MenuItem(nv.getName());
								popup.add(mitem);
								Iterator it = nv.iterator();
								while (it.hasNext()) {
									  final String str = (String) it.next();
									  mitem = new MenuItem(" " + str);
									  mitem.addActionListener(new ActionListener(){
											public void actionPerformed(ActionEvent e){
										  		 //look up the super class in the matrix
												boolean isExist=false;
												for (int i=0; i<navigator.size(); i++){
													if (((String)matrixTable.getValueAt(i,1)).equals(str+".jak")){
														me.mmatrixGui.matrixTable.changeSelection(i,1, false,false );
														me.status.setText("Status: Showing the artifact "+theArt+"'s super artifact.");
														isExist=true;
														break;
													}
												}
												if (!isExist)
													me.status.setText("Status: The super artifact "+str+" isn't inside the model!");
											}
									  });
									  popup.add(mitem);
								}
								popup.addSeparator();
						 }
					}
				}
				else{
					mitem = new MenuItem("- Empty -");
					popup.add(mitem);
				}
			}
			matrixTable.add(popup);
			popup.show(evt.getComponent(), evt.getX(), evt.getY());
			pop=true;
		}
	}

	public void getCompositionDir(){
		final JFileChooser chooser = new JFileChooser(me.modelDir);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);;
		final JButton chooserButton=new JButton("Browser...");
		final String str1="The composition is not in the model directory,";
		final String str2="Please choose the composition directory.";
		final JTextField tf=new JTextField(me.modelDir);
		final Object[] array = {str1,str2,tf,chooserButton};
		final CustomDialog cd=new CustomDialog("Composition Directory",array,MMatrixGui.this,me);
		chooserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				int openVal = chooser.showOpenDialog(MMatrixGui.this);
		      if (openVal == JFileChooser.APPROVE_OPTION) {
		         File file = chooser.getSelectedFile();
					tf.setText(file.getAbsolutePath());
				}
			}
		});
		cd.setLocationRelativeTo(MMatrixGui.this);
		cd.setSize(300,170);
		cd.setVisible(true); //show();
	}

	public void clickAction(MouseEvent evt, boolean pop){
		int nRow = matrixTable.rowAtPoint(new Point(evt.getX(),evt.getY()));
		int nCol = matrixTable.columnAtPoint( new Point(evt.getX(),evt.getY()));

		if (nCol==0){
			if (me.matrixIndex<1){
				if (!((String)matrixTable.getValueAt(nRow,0)).equals("u")){
					updateDialog(nRow);
				}
			}
			else{
				if (((String)matrixTable.getValueAt(nRow,0)).equals("T")
										 ||((String)matrixTable.getValueAt(nRow,0)).equals("S")){
					updateDialog(nRow);
				}
			}
			//matrixTable.getColumnModel().getColumn(nCol).setHeaderRenderer(greenRenderer);
			if (selectedColIndex!=-1 && selectedColIndex!=nCol){
				matrixTable.getColumnModel().getColumn(selectedColIndex).setHeaderRenderer(renderer);
			}
			selectedColIndex=-1;
			matrixTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		else {
			Object obj=((Object[])navigator.get((String)matrixTable.getValueAt(nRow,1)))[nCol];
			StringBuffer sb=new StringBuffer();
			if (nCol==1){
		  		if (me.compositionDir!=null){
		  			File file=new File(me.compositionDir);
		  			if (file.isDirectory()){
		  				sb.append(me.compositionDir);
		  			}
		  			else{
		  				getCompositionDir();
		  				sb.append(me.compositionDir);
		  			}
				}
			}
			else{
				sb.append(me.modelDir);
				if (me.modelDir.charAt(me.modelDir.length()-1)!=File.separatorChar){
					sb.append(File.separatorChar);
				}
				if (((String)columnHeaders.get(nCol)).equals("Super")){
					//System.out.println((String)matrixTable.getValueAt(nRow,1));
					if (me.matrixIndex==1){
						sb.append((String)JakMatrix.superMap.get((String)matrixTable.getValueAt(nRow,1)));
					}
					else{
						int sr=me.jakMatrixGui1.matrixTable.getSelectedRow();
						sb.append((String)JakMatrix.superMap.get((String)me.jakMatrixGui1.matrixTable.getValueAt(sr,1)));
					}
				}
				else{
					sb.append((String)columnHeaders.get(nCol));
				}
			}

			if (nCol==1 && me.compositionDir==null){
				JOptionPane.showMessageDialog(null, "There's no active equation!");
			}
			else{
				if (!((String)columnHeaders.get(nCol)).equals("Super")){
					if (matrix instanceof SubMatrix)
						sb.append(subMatrix.getSubPath());
					else
						sb.append(File.separator);
					if (me.matrixIndex<1)
			  			sb.append((String)matrixTable.getValueAt(nRow,1));
					else{
						sb.append(JakMatrix.fileName);
						//sb.append((String)me.mmatrixGui.matrixTable.getValueAt(MMatrixGui.theRow,1));
					}
				}
			  	//sb.append(".jak");

				//System.out.println(sb.toString());
				//System.out.println(JakMatrix.superMap);
				DisplayFile.file=new File(sb.toString());
				if ((me.matrixIndex>0)&&(nCol==1)){
					try{
						MMOutput output = mmatrix.Main.eval( sb.toString() );
						MMHashMap theNested=output.getNested();
						if (theNested != null) {
							Iterator it = theNested.values().iterator();
							while (it.hasNext()) {
							    MMOutput o = (MMOutput) it.next();
							    if (o.getName().equals((String)matrixTable.getValueAt(nRow,1))){
									obj = o;
							    }
							}
						}
						//obj = theNested.get(matrixTable.getValueAt(nRow,1));
					}catch (Exception e) {
						obj=null;
					}
				}
				if(me.matrixIndex>0 &&(obj!=null))
					DisplayFile.obj=(MMOutput)obj;
				if (me.isReadOnly){
					DisplayFile.edit(me);
					DisplayFile.display(me);
					me.editorTabbedPane.setSelectedIndex(0);
				}
				else{
					DisplayFile.display(me);
					DisplayFile.edit(me);
					me.editorTabbedPane.setSelectedIndex(0);
				}
				DisplayFile.obj=null;
				if (nCol!=1)
					matrixTable.getColumnModel().getColumn(nCol).setHeaderRenderer(greenRenderer);
				if (selectedColIndex!=-1 && selectedColIndex!=nCol){
					matrixTable.getColumnModel().getColumn(selectedColIndex).setHeaderRenderer(renderer);
				}
				if (nCol!=1)
					selectedColIndex=nCol;
				else
					selectedColIndex=-1;
				matrixTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			}
		}
	}

   public void initListeners(){
		matrixTable.addMouseListener (new MouseListener(){
		boolean pop = false;
      public void mousePressed (MouseEvent evt)
      {
		 	popAction(evt, pop);
		}

		public void mouseReleased (MouseEvent evt)
		{
			popAction(evt, pop);
		}

		public void mouseClicked (MouseEvent evt)
		{
			popAction(evt, pop);
			if (!pop){
				clickAction(evt, pop);
			}
			pop=false;
		}
		 public void mouseEntered(MouseEvent evt){}
		 public void mouseExited(MouseEvent evt){}
	  });

   }

	public Matrix getMatrix(){
		return matrix;
	}

   public void checker(){
	   Vector v;
	   Vector mma=matrix.getMatrix();
	   boolean hasR, hasError;
	   String str;
	   Vector sourceVec = new Vector();
	   //me.toolsPanel.CenterPanel.remove(1);
	   //me.toolsPanel.CenterPanel.add(me.outputScrollPane, BorderLayout.CENTER);
	   me.toolsPanel.setOutputView(me.output);
	   if (me.matrixIndex<1)
		  sourceVec.add("Errors in following artifacts:");
	   else
	   	  sourceVec.add("Errors in following methods:");
		  //me.output.setBorder(new TitledBorder("Errors in following methods:"));
	   sourceVec.add("");
	   hasError=false;
	   for (int i=0; i<mma.size(); i++){
		   v=(Vector)mma.get(i);
		   hasR=false;
		   for (int j=2; j<v.size(); j++){
			   str=(String)v.get(j);
				if (str!=null){
					if (str.equals("D")||str.equals("E")){
					   if (hasR){
						   sourceVec.add(new MyData(i,j, "   "+(String)v.get(1)));
						   hasError=true;
						   break;
					   }
					}
					else if (str.equals("R")){
					   hasR=true;
					}
				}
		  }
	   }
	   if (hasError){
		   //me.outputTitle.setText("Navigator Error Checking Results");
		   me.output.setListData(sourceVec);
		   me.output.addListSelectionListener(new ListSelectionListener(){
			   public void valueChanged(ListSelectionEvent e) {
				   JList theList = (JList)e.getSource();
				   int index = theList.getSelectedIndex();
				   if (index>1){
					   int row=((MyData)theList.getSelectedValue()).row;
					   int col=((MyData)theList.getSelectedValue()).col;
					   matrixTable.changeSelection(row,col, false,false );
				   }
			   }
		  });
	   }
	   else{
		   //me.output.setBorder(new TitledBorder("Error Checking Results:"));
		   sourceVec.add("   No error in current navigator.");
		   me.output.setListData(sourceVec);
		   //JOptionPane.showMessageDialog(null, "Error Checking Result:\n No error in current navigator.");
	   }
	   me.editorTabbedPane.setSelectedIndex(2);
   }

	public void reducer(String rowStr, String colStr){
		me.rmatrix=new RMatrix(rowStr,colStr, matrix.getNavigator(), columnHeaders);
	}


   public MMatrixGui() { super(); }

   public MMatrixGui(Main me){
	   super();
	   this.me = me;
   }

   public MMatrixGui(String AppTitle) { super(AppTitle); }

   public static void main(String[] args) {
		new MMatrixGui("Model Navigator");
   }

	class MyData{
		public int row;
		public int col;
		public String str;

		public MyData(int theRow,int theCol, String theStr){
			row=theRow;
			col=theCol;
			str=theStr;
		}

		public String toString(){
			return str;
		}

	}

}

// end
