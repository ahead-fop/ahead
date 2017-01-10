/* ******************************************************************
   class      : TreeBrowser
   description: Swing application that visualizes the model as a
					 tree view.
*********************************************************************/

package ModelExplorer.Browser;

import ModelExplorer.SwingUtils.*;
import ModelExplorer.Editor.*;
import ModelExplorer.Composer.*;
import ModelExplorer.Util.*;
import ModelExplorer.Main;

import javax.swing.*;
import javax.swing.tree.*;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class TreeBrowser extends SwingApp {

    private Main me;

	// Atomic Component declarations
	JTree								tree;
	DefaultMutableTreeNode		root;
   DefaultTreeModel				treeModel;
   Toolkit							toolkit = Toolkit.getDefaultToolkit();
	PopupMenu						popup;
	MenuItem							mitem;

    public void initAtoms() {
    	super.initAtoms();

    	//Create the nodes.
		String fileName;
		if (me.modelDir.equals("."+File.separator)){
			fileName=(new File(me.modelDir)).getAbsolutePath();
			fileName=fileName.substring(0,fileName.lastIndexOf(File.separatorChar));
			fileName=fileName.substring(fileName.lastIndexOf(File.separatorChar)+1);
		}
		else
			fileName=(new File(me.modelDir)).getName();

	    root = new MyMutableTreeNode(new FileInfo(me.modelDir, fileName));

        createNodes(root);

        //Create a tree that allows one selection at a time.

        treeModel = new DefaultTreeModel(root);

        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        //Enable tool tips.
        ToolTipManager.sharedInstance().registerComponent(tree);

        /*
         * Set the icon for leaf nodes.
         * Note: In the Swing 1.0.x release, we used
         * swing.plaf.basic.BasicTreeCellRenderer.
         */
        tree.setCellRenderer(new DefaultTreeCellRenderer());
		  tree.putClientProperty("JTree.lineStyle", "Angled");

		  //set font
		  setFont(null);
	}

	 // layout component declarations
	 JScrollPane treeView;

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
		  treeView = new JScrollPane(tree);
		  treeView.setPreferredSize(new Dimension(300, 500));
	}

	public void initContentPane() {
	    // ContentPane uses a BoxLayout of two columns
	    ContentPane = initPanel(true);
	   	ContentPane.add(treeView);
		ContentPane.setMaximumSize(new Dimension(300,500));
	}

    public void initListeners(){
		  //Listen for the change.
		  treeModel.addTreeModelListener(new MyTreeModelListener());

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
               DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                   tree.getLastSelectedPathComponent();

               if (node == null) return;

               Object nodeInfo = node.getUserObject();
					FileInfo fileInfo = (FileInfo)nodeInfo;

					DisplayFile.file=new File(fileInfo.getPath());
					if (node.isLeaf()) {
						if (me.isReadOnly){
						    //displayFile(fileInfo);
							DisplayFile.edit(me);
							DisplayFile.display(me);
						}
						else{
						    //displayFile(fileInfo);
							DisplayFile.display(me);
							DisplayFile.edit(me);
						}
						if (me.editorTabbedPane.getSelectedIndex()==2)
							me.editorTabbedPane.setSelectedIndex(0);
					}
					if (me.editorTabbedPane.getSelectedIndex()==1){
						me.equationPanel.addStringToList(fileInfo.getName());
					}
            }
        });

		  tree.addMouseListener (new MouseAdapter(){
			   public void mousePressed (MouseEvent evt)
			   {
		 			popAction(evt);
				}

				public void mouseReleased (MouseEvent evt)
				{
					popAction(evt);
				}
				public void mouseClicked (MouseEvent evt)
				{
					popAction(evt);
				}
		  });
     }

    public TreeBrowser() { super(); }

	 public TreeBrowser(Main me){
		 super();
		 this.me = me;
	 }
	 public TreeBrowser(String AppTitle) { super(AppTitle); }

	 public static void main(String[] args) {
	 		new TreeBrowser("Model Tree Browser");
	 }

    //some utility class and functions
    private class FileInfo {
        public String fileName;
        public String filePath;

        public FileInfo(String filePath, String fileName) {
            this.fileName=fileName;
            this.filePath =filePath;
        }

		public String getPath(){
			return filePath;
		}

		public String getName(){
			return fileName;
		}

        public String toString() {
            return fileName;
        }
    }

    private void createNodes(DefaultMutableTreeNode root) {

        DefaultMutableTreeNode node = null;
        FileInfo		   fileInfo = null;
		  ModelFilter		filter = new ModelFilter();
		  Main.setFilter(filter);

		String filePath=((FileInfo)root.getUserObject()).getPath();
		ArrayList pathList=new ArrayList();
		String[] list;
		DefaultMutableTreeNode currentNode;
		try{
			File path=new File(filePath);
			if (path.isDirectory()){
				list=path.list();
				Arrays.sort(list);
				for (int i=0; i<list.length; i++){
					if (!filter.dirs.contains(list[i])&&!filter.files.contains(list[i])&&!list[i].endsWith("~")){
						if (list[i].lastIndexOf(".")==-1 ||!filter.files.contains(list[i].substring(list[i].lastIndexOf(".")))){
							fileInfo= new FileInfo(filePath+File.separator+list[i]+File.separator, list[i]);
							node = new MyMutableTreeNode(fileInfo);
							root.add(node);
							pathList.add(node);
						}

					}
				}
			}
			int index=0;
			while(pathList.size()>index){
				currentNode=(DefaultMutableTreeNode)pathList.get(index);
				filePath=((FileInfo)currentNode.getUserObject()).getPath();
				path=new File(filePath);
				if (path.isDirectory()){
					currentNode.setAllowsChildren(true);
					list=path.list();
					Arrays.sort(list);
					for (int i=0; i<list.length; i++){
						if (!filter.dirs.contains(list[i])&&!filter.files.contains(list[i])&&!list[i].endsWith("~")){
							if (list[i].lastIndexOf(".")==-1 ||!filter.files.contains(list[i].substring(list[i].lastIndexOf(".")))){
								fileInfo=new FileInfo(filePath+File.separator+list[i]+File.separator, list[i]);
								node = new MyMutableTreeNode(fileInfo);
								currentNode.add(node);
								pathList.add(node);
							}
						}
					}
				}
				else{
					currentNode.setAllowsChildren(false);
				}
				index++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

    }

	 //to set font
	 public void setFont(Font font){
		 if (font==null)
				font=me.font;
		 if (font!=null)
			tree.setFont(font);
	 }

	 //utility function to delete a file or directory
	 public boolean deleteFile(File file) {
		 boolean result=true;
		 if (file.isDirectory()){
			 String[] filelist = file.list();
			 File tmpFile = null;
			 for (int i = 0; i < filelist.length; i++) {
				 tmpFile = new File(file.getAbsolutePath(),filelist[i]);
				 if (tmpFile.isDirectory()) {
				    if (!deleteFile(tmpFile))
						 result=false;
				 }
				 else if (tmpFile.isFile()) {
					 if (!tmpFile.delete())
						 result=false;
				 }
			}
	     }
		 if (!file.delete())
			 result=false;
		 return result;
	 }

	//utility function for actionlistener
	private void popAction(MouseEvent evt){
		final int selectedRow = tree.getRowForLocation(evt.getX(), evt.getY());

		if (selectedRow != -1&&evt.isPopupTrigger()){
			 tree.setSelectionRow(selectedRow);
			 final DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                          tree.getLastSelectedPathComponent();

			 if (node == null) return;
			 tree.setSelectionPath(new TreePath(node.getPath()));

			 popup = new PopupMenu();
			 if (!node.isLeaf()){
				 mitem = new MenuItem("Add Dir");
				 mitem.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
					   Object nodeInfo = node.getUserObject();
						FileInfo fileInfo = (FileInfo)nodeInfo;
						String inputValue = JOptionPane.showInputDialog("Directory Name:");
						if (inputValue!=null){
							File file=new File(fileInfo.getPath(),inputValue);
							if (!file.mkdir()){
								me.status.setForeground(Color.red);
								me.status.setText("Status: Failure to add a new directory!");
								me.status.setForeground(me.editorTitle.getForeground());
							}
							else{
								me.status.setText("Status: Successful to add a new directory!");
								DefaultMutableTreeNode newNode=addObject(node,new FileInfo(file.getAbsolutePath(),file.getName()), true, true);
								tree.setSelectionPath(new TreePath(newNode.getPath()));
								//tree.setAnchorSelectionPath(new TreePath(newNode.getPath()));
							}
						}
					}
				 });
				 popup.add(mitem);
				 mitem = new MenuItem("Add File");
				 mitem.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
					   Object nodeInfo = node.getUserObject();
						FileInfo fileInfo = (FileInfo)nodeInfo;
						String inputValue = JOptionPane.showInputDialog("File Name:");
						if (inputValue!=null){
							File file=new File(fileInfo.getPath(),inputValue);
							try{
								FileWriter out=new FileWriter(file);
								out.write("//created on: "+new Date());
								out.close();
								DefaultMutableTreeNode newNode=addObject(node,new FileInfo(file.getAbsolutePath(),file.getName()),true, false);
								tree.setSelectionPath(new TreePath(newNode.getPath()));
								//tree.setAnchorSelectionPath(new TreePath(newNode.getPath()));
								me.status.setForeground(me.editorTitle.getForeground());
								me.status.setText("Status: Successful to add a new file!");
								me.toolsPanel.resetEq();
								me.equationPanel.resetEq();
							}
							catch(IOException ioe){
								me.status.setForeground(Color.red);
								me.status.setText("Status: Failure to add a new file!");
							}
						}
					}
				 });
				 popup.add(mitem);
				 popup.addSeparator();
			 }
			 else{
				 Object nodeInfo = node.getUserObject();
			  	 final FileInfo fileInfo = (FileInfo)nodeInfo;
				 String ext=fileInfo.getName().substring(fileInfo.getName().lastIndexOf('.')+1);
				 if (ext.equals("jak")||ext.equals("layer")||ext.equals("java")){
					me.toolsPanel.selectedFile=fileInfo.getPath();
					mitem = new MenuItem("Reform");
					mitem.addActionListener(new ActionListener(){
					  public void actionPerformed(ActionEvent e){
						   me.editorTabbedPane.setSelectedIndex(2);
						   ReformThread thread = new ReformThread(me,me.toolsPanel,true,node);
					  }
					});
					popup.add(mitem);
					if (ext.equals("jak")){
						mitem = new MenuItem("Unmixin");
						mitem.addActionListener(new ActionListener(){
						  public void actionPerformed(ActionEvent e){
							   me.editorTabbedPane.setSelectedIndex(2);
							   UnmixinThread thread = new UnmixinThread(me,me.toolsPanel,true, node);
						  }
						});
						popup.add(mitem);
						mitem = new MenuItem("Jak2Java");
						mitem.addActionListener(new ActionListener(){
						  public void actionPerformed(ActionEvent e){
							   me.editorTabbedPane.setSelectedIndex(2);
							   J2JThread thread = new J2JThread(me,me.toolsPanel,true, node);
						  }
						});
						popup.add(mitem);
					}
					if (ext.equals("java")){
						mitem = new MenuItem("Compile");
						mitem.addActionListener(new ActionListener(){
						  public void actionPerformed(ActionEvent e){
							   me.editorTabbedPane.setSelectedIndex(2);
							   J2CThread thread = new J2CThread(me,me.toolsPanel,true, node);
						  }
						});
						popup.add(mitem);
					}
					popup.addSeparator();
				 }
			 }

			 mitem = new MenuItem("Delete");
			 mitem.addActionListener(new ActionListener(){
			   public void actionPerformed(ActionEvent e){
					Object nodeInfo = node.getUserObject();
			  		FileInfo fileInfo = (FileInfo)nodeInfo;
			  		int option=JOptionPane.showConfirmDialog(null,
			  							"Do you really want to delete the selected item?");
			  		if (option==0){
						File file=new File(fileInfo.getPath());
						if (deleteFile(file)){
							removeCurrentNode();
							me.status.setForeground(me.editorTitle.getForeground());	
							me.status.setText("Status: Delete successfully!");
						}
						else{
							updateNode(getCurrentNode());
							me.status.setForeground(Color.red);
							me.status.setText("Status: Failure to delete due to file used violation! try it later!");
						}
						me.codeEditor.close();
						me.toolsPanel.resetEq();
						me.equationPanel.resetEq();
					}
			   }
			 });
			 popup.add(mitem);
			 /*mitem = new MenuItem("Rename");
			 mitem.addActionListener(new ActionListener(){
			   public void actionPerformed(ActionEvent e){
			     tree.setSelectionRow(selectedRow);
			   }
			 });
			 popup.add(mitem);*/
			 tree.add(popup);
			 popup.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}

	//function to return the tree

	public JTree getTree(){
		return tree;
	}

	 //classes and functions for modify the tree

	 /** update the given node. */
	 public void updateNode(DefaultMutableTreeNode node){
		 if (node!=null){
			String fileName=((FileInfo)node.getUserObject()).getName();
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
			parent.removeAllChildren();
			createNodes(parent);
			//find out the file to be set selected.
			treeModel.nodeStructureChanged(parent);
			Enumeration eu=parent.children();
			while(eu.hasMoreElements()){
				 DefaultMutableTreeNode current=(DefaultMutableTreeNode)eu.nextElement();
				 if (fileName.equals(current.toString())){
				 	 tree.scrollPathToVisible(new TreePath(current.getPath()));
					 tree.setSelectionPath(new TreePath(current.getPath()));
				 }
			}
		 }
	 }

	  /** update the given node which is specified by the give equation name */
	 	 public void updateNode(String eqName){
	 		 String name=eqName.substring(0,eqName.lastIndexOf('.'));
	 		 //find out if the directory exists or not.
			 Enumeration eu=root.children();
			 while(eu.hasMoreElements()){
				 DefaultMutableTreeNode current=(DefaultMutableTreeNode)eu.nextElement();
				 if (name.equals(current.toString()))
					 treeModel.removeNodeFromParent(current);
	 		 }
	 		 DefaultMutableTreeNode newNode=new MyMutableTreeNode(new FileInfo(me.modelDir+File.separator+name, name));
	 		 createNodes(newNode);
	 		 treeModel.insertNodeInto(newNode, root,root.getChildCount());
	 		 //set the new node to be selected.
	 		 tree.scrollPathToVisible(new TreePath(newNode.getPath()));
	 		 tree.setSelectionPath(new TreePath(newNode.getPath()));
	 }

	  /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode)(currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        }

        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }
	 
	 public DefaultMutableTreeNode getCurrentNode(){
		 TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) 
            return (DefaultMutableTreeNode)(currentSelection.getLastPathComponent());
		  else
			  return null;
	 }

    /** Add child to the currently selected node. */
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child,
                                            boolean shouldBeVisible,														  boolean allowChildren) {
        DefaultMutableTreeNode childNode =
                new MyMutableTreeNode(child, allowChildren);

        if (parent == null) {
            parent = root;
        }

        treeModel.insertNodeInto(childNode, parent,
                                 parent.getChildCount());

        // Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

     /** Add child to the root node. */
	    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
	                                            File file,
	                                            boolean shouldBeVisible,
															  boolean allowChildren) {
			 DefaultMutableTreeNode childNode=null;
			//find out if the file exists or not.
			 Enumeration eu=root.children();
			 while(eu.hasMoreElements()){
				 DefaultMutableTreeNode current=(DefaultMutableTreeNode)eu.nextElement();
				 if (file.getName().equals(current.toString()))
					 childNode=current;
	 		 }
	 		 if (childNode==null){
	         	childNode = new MyMutableTreeNode(new FileInfo(file.getPath(), file.getName()), allowChildren);
				if (parent == null) {
					parent = root;
				}
				treeModel.insertNodeInto(childNode, parent,parent.getChildCount());
			}

	        // Make sure the user can see the lovely new node.
	        if (shouldBeVisible) {
	            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
	        }
	        return childNode;
    }

    class MyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode)
                     (e.getTreePath().getLastPathComponent());

				//find out which file are changed
				Enumeration eu=parent.children();
				HashSet hs=new HashSet();
				while(eu.hasMoreElements()){
					hs.add(eu.nextElement().toString());
				}

				FileInfo fi = (FileInfo)parent.getUserObject();				
				String[] list=(new File(fi.getPath())).list();
				String oldName=null;				
				for (int i=0; i<list.length; i++){					
					if (!hs.contains(list[i])){
						oldName=list[i];						
						break;
					}				
				}

            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */				DefaultMutableTreeNode node=null;
            try {
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)(parent.getChildAt(index));
            } catch (NullPointerException exc) {}				
				if (node!=null){					
					String newName=(String)node.getUserObject();					
					removeCurrentNode();					
					File file=new File(fi.getPath(),oldName);					
					File dest=new File(fi.getPath(),newName);
					try{
						file.renameTo(dest);
						if (dest.isDirectory()){							
							node=addObject(parent,new FileInfo(dest.getPath(),dest.getName()),true,true);
							tree.setSelectionPath(new TreePath(node.getPath()));
						}						
						else{							
							node=addObject(parent,new FileInfo(dest.getPath(),dest.getName()),true,false);							
							tree.setSelectionPath(new TreePath(node.getPath()));
						}						
						me.status.setForeground(me.editorTitle.getForeground());		
						me.status.setText("Status: Successful to rename!");
						me.toolsPanel.resetEq();
						me.equationPanel.resetEq();
					}
					catch(Exception ioe){						
						me.status.setForeground(Color.red);
						me.status.setText("Status: Failure to rename!");				
						if (file.isDirectory()){							
							node=addObject(parent,new FileInfo(file.getPath(),file.getName()),true,true);
							tree.setSelectionPath(new TreePath(node.getPath()));
						}						
						else{							
							node=addObject(parent,new FileInfo(file.getPath(),file.getName()),true,false);
							tree.setSelectionPath(new TreePath(node.getPath()));
						}
					}				
				}
        }
        public void treeNodesInserted(TreeModelEvent e) {
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }


}
