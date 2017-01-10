/* ******************************************************************
   class      : ToolsPanel
   description: Swing application that provides a Gui to run the
					 composer tools.
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

public abstract class ToolsPanel extends SuperPanel {

	public static String selectedFile=null;
	// Atomic Component declarations
	JLabel						titleLabel;
	JButton						optionButton;
	JButton						checkerButton;
	JTextArea               outputArea;
	
    public void initAtoms() {
    	super.initAtoms();

		titleLabel = new JLabel("Tool Invocation Panel");
		titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		optionButton	= new JButton("Options...");
		optionButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		optionButton.setToolTipText("Change the options for the tools using the option panel");

		checkerButton = new JButton("CHECKER");
		checkerButton.setForeground(Color.blue);
		checkerButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		checkerButton.setToolTipText("Check the matrix navigator to see whether it satisfy the composition rules");

		outputArea		= new JTextArea();
		outputArea.setEditable(false);
		
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
	JPanel  EqPanel;
	JPanel  buttonPanel;
	JPanel  topPanel;
	TitledBorder titledBorder;
	TitledBorder titledBorder2;
	JScrollPane outputScroll;
	JPanel  CenterPanel;

	public void initLayout() {

		EqPanel = new JPanel();
		EqPanel.setLayout( new BoxLayout( EqPanel, BoxLayout.X_AXIS ) );
	   EqPanel.setBorder( BorderFactory.createEtchedBorder());
		EqPanel.add(eqButton);
		EqPanel.add(eqComboBox);
		EqPanel.add(optionButton);
		
	   topPanel = new JPanel();
		topPanel.setLayout( new BoxLayout( topPanel, BoxLayout.Y_AXIS ) );
		titledBorder2 = new TitledBorder("Tool Options:");
		topPanel.setBorder(titledBorder2);
		topPanel.setForeground(Color.magenta);
		topPanel.add(EqPanel);

		outputScroll = new JScrollPane(outputArea);
		titledBorder = new TitledBorder("Running Output:");
		outputScroll.setBorder(titledBorder);

		CenterPanel = new JPanel();
		CenterPanel.setLayout( new BorderLayout() );
      CenterPanel.setBorder( BorderFactory.createEtchedBorder());
      CenterPanel.add(topPanel, BorderLayout.NORTH);
      CenterPanel.add(outputScroll, BorderLayout.CENTER);

	}

	public void initContentPane() {
	    // ContentPane uses a BoxLayout of two columns
	   ContentPane = initPanel(false);
	   ContentPane.add(titleLabel);
		ContentPane.add(CenterPanel);
	}

   public void initListeners(){

		checkerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String equationName=(String)eqComboBox.getSelectedItem();
				if (!equationName.equals("Choose one")){
					CheckerThread thread = new CheckerThread(me,ToolsPanel.this);
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
	
	 public ToolsPanel(){super();}	
	
	 public ToolsPanel(Main me){
		 super(me);
	 }

	 //set outputArea. used by the tools button except the checker button
	 public void setOutputView(Component view){
		 //int n=CenterPanel.getComponentCount();
		 //CenterPanel.remove(1);
		 //CenterPanel.add(outputScroll, BorderLayout.CENTER);
		 outputScroll.setViewportView(view);
	 }

	 public void setOutputView(){
		 outputScroll.setViewportView(outputArea);
	 }

	 //set font. used by the Main
	 public void setFont(Font font){
		titleLabel.setFont(font);

		super.setFont(font);
		optionButton.setFont(font);

		checkerButton.setFont(font);

		outputArea.setFont(font);
		titledBorder.setTitleFont(font);
		titledBorder2.setTitleFont(font);
	 }

	 //set enable. used by the tool thread
	 public void setEnabled(boolean b){
		checkerButton.setEnabled(b);
		optionButton.setEnabled(b);
		super.setEnabled(b);
	}

	//get all the files from the given dir. used by the tool thread.
	public Object[] getFiles(String filePath){
		ArrayList files= new ArrayList();
		ArrayList pathList = new ArrayList();
		String[] list;
		ModelFilter	filter=null;
		try{
			File path=new File(filePath);
			if (path.isDirectory()){
				filter = new ModelFilter();
				Main.setFilter(filter);
				list=path.list();
				for (int i=0; i<list.length; i++){
					if (!filter.dirs.contains(list[i])&&!filter.files.contains(list[i])){
						if (list[i].lastIndexOf(".")==-1 ||!filter.files.contains(list[i].substring(list[i].lastIndexOf(".")))){
							pathList.add(filePath+File.separator+list[i]+File.separator);
						}
					}
				}
			}
			int index=0;
			String curPath;
			while(pathList.size()>index){
				curPath=(String)pathList.get(index);
				path=new File(curPath);
				if (path.isDirectory()){
					list=path.list();
					for (int i=0; i<list.length; i++){
						if (!filter.dirs.contains(list[i])&&!filter.files.contains(list[i])){
							if (list[i].lastIndexOf(".")==-1 ||!filter.files.contains(list[i].substring(list[i].lastIndexOf(".")))){
								pathList.add(curPath+File.separator+list[i]+File.separator);
							}
						}
					}
				}
				else{
					files.add(curPath);
				}
				index++;
			}
			return files.toArray();
		}catch(Exception e){
			outputArea.append(e.getMessage());
			outputArea.append("\n");
			try{
				outputArea.setCaretPosition(outputArea.getDocument().getLength());
			}catch(Exception be){}
			return null;
		}
	}

	//get the activeEquation for tools
	public String getEquation(){
		return (String)eqComboBox.getSelectedItem();
	}

}
