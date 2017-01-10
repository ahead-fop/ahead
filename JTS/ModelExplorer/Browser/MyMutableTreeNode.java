/* ******************************************************************
   class      : MyMutableTreeNode
   description: to change the behavior of isLeaf() in DefaultMutableTreeNode
*********************************************************************/

package ModelExplorer.Browser;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class MyMutableTreeNode extends DefaultMutableTreeNode {
 
	MyMutableTreeNode(Object obj){
		super(obj);
	}
	
	MyMutableTreeNode(Object obj, boolean allowChildren){
		super(obj);
		setAllowsChildren(allowChildren);
	}
	
	public boolean isLeaf(){
		  return (super.isLeaf()&&!getAllowsChildren());
	}
	
}
