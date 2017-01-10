/**
 * class: RMatrix
 */

package ModelExplorer.MMatrixBrowser;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import mmatrix.*;
import ModelExplorer.Util.*;
import ModelExplorer.Main;

public class RMatrix extends Matrix{

	//Construct a matrix of the specified model directory
	public RMatrix(String rowStr, String colStr, TreeMap theNavigator, Vector theHeader){
		super();
		createMatrix(rowStr, colStr, theNavigator, theHeader);
	}

	//The method to create the matrix which is called by the constructor.
	protected void createMatrix(String rowStr, String colStr, TreeMap theNavigator, Vector theHeader){
		Vector indexVec=new Vector();
		indexVec.add(new Integer(0));
		indexVec.add(new Integer(1));
		Pattern p = Pattern.compile(colStr);
		columnHeaders.add("Artifact Type");
		columnHeaders.add("Artifact");
		for (int i=2; i<theHeader.size(); i++){
			if (p.matcher((String)theHeader.get(i)).matches()){
				columnHeaders.add((String)theHeader.get(i));
				indexVec.add(new Integer(i));
			}
		}
		
		int len=columnHeaders.size();
		p=Pattern.compile(rowStr);
		Iterator it=theNavigator.keySet().iterator();
		String key;
		Object[] temObj;
		while(it.hasNext()){
			key =(String)it.next();
			if (p.matcher(key).matches()){
				temObj=(Object[])theNavigator.get(key);
				Object[] temArray=new Object[len];
				for (int i=0; i<len; i++){
					temArray[i]=temObj[((Integer)indexVec.get(i)).intValue()];
				}
				navigator.put(key, temArray);
			}
		}
		
		/*Creat the matrix vector from the TreeMap. */
		it=navigator.values().iterator();
		Vector temVec1;
		while(it.hasNext()){
			temObj=(Object[])it.next();
			temVec1 = new Vector();
			for (int i=0; i<temObj.length; i++){
				if (i==0 || i==1){
					temVec1.add((String)temObj[i]);
				}
				else {
					if (temObj[i]!=null){
						if (temObj[i] instanceof String){
							temVec1.add("1");
						}
						else{
							temVec1.add(((MMOutput)temObj[i]).getDefn());
						}
					}
					else{
						temVec1.add(temObj[i]);
					}
				}
			}
			myMatrix.add(temVec1);
		}
	}
}


