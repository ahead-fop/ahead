/**
 * class: Matrix
 */

package ModelExplorer.MMatrixBrowser;

import java.io.*;
import java.util.*;
import mmatrix.*;
import ModelExplorer.Util.*;
import ModelExplorer.Main;

abstract public class Matrix{
	Vector	myMatrix;
	Vector	columnHeaders;
	TreeMap	navigator;
	HashMap	nested;

	/** Construct a matrix of the specified model directory
	 */
	public Matrix(){
		myMatrix=new Vector();
		columnHeaders=new Vector();
		navigator=new TreeMap();
		nested = null;
	}

	/** The method to create the matrix which is called by the constructor.
	 */
	protected void createMatrix(){
	
	}

	/** Return an Object array as the matrix
	 */ 
	public Vector getMatrix(){
		return myMatrix;
	}
	
	public TreeMap getNavigator(){
		return navigator;
	}

	public Vector getColumnHeaders(){
		return columnHeaders;
	}
	
	public HashMap getNested(){
		return nested;
	}
	
}


