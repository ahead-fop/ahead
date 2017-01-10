/**********************************************************
	class: JakMatrix
**********************************************************/

package ModelExplorer.MMatrixBrowser;

import java.io.*;
import java.util.*;
import mmatrix.*;
import ModelExplorer.Util.*;
import ModelExplorer.Main;

public class JakMatrix extends Matrix{
	
	/** When creating jak matrix, it will recursive to get the methods inside its super
	 *  classes. superMap to record the class path and name of the methods(name as key) 
	 *  in the supers. When we want to show the methods in the supers, we can get the 
	 *  coresponding file path by this superMap. 
	 */
	public static HashMap superMap=new HashMap();
	public static String fileName;
	public static Main me;
	/** To indicate whether the jakMatrix is super or not. if it is, then the
	 *  super file path should be put into superMap.
	 */
	private boolean isSuper;
	
	/** Construct a matrix of the specified model directory. */
	public JakMatrix(Object[] theRow, Vector theHeaders, Main me){
		super();
		isSuper=false;
		this.me = me;
		createNavigator(theRow, theHeaders);
		createMatrix();
	} // end JakMatrix
	
   private JakMatrix(Object[] theRow, Vector theHeaders, boolean isSuper){
		super();
		this.isSuper=isSuper;
		createNavigator(theRow, theHeaders);
	} //end JakMatrix

	//The method to create the matrix which is called by the constructor.
	protected void createNavigator(Object[] theRow, Vector theHeaders){
		//get the path list.
		String tempStr;
		Object[] tuple;
		
		columnHeaders.add("Type");
		if (((String)theHeaders.get(1)).equals("Artifact"))
			columnHeaders.add((String)theRow[1]);
		else
			columnHeaders.add((String)theHeaders.get(1)+"."+(String)theRow[1]);
		if (me.matrixIndex==1)
			columnHeaders.addElement("Super");
		int len=theRow.length;
		for (int i=2; i<len; i++){
			if (theRow[i]!=null && !(theRow[i] instanceof String)){
				columnHeaders.add(theHeaders.get(i));	
			}
		}
		len=columnHeaders.size();
		int index;
		if (me.matrixIndex==1)
			index=3;
		else
			index=2;
		for (int i=2; i<theRow.length; i++){
			if (theRow[i]!=null && !(theRow[i] instanceof String)){
				//Get the set of methods inside this object
				MMHashMap theNested=((MMOutput)theRow[i]).getNested();
				if (theNested != null) {
					Iterator it = theNested.values().iterator();
					while (it.hasNext()) {
					    MMOutput o = (MMOutput) it.next();
					    tempStr=o.getName();
					    if (navigator.containsKey(tempStr)){
							tuple = (Object[])navigator.get(tempStr);
					    }
						else{
							tuple = new Object[len];
							tuple[1]=tempStr;
							navigator.put(tempStr, tuple);
						}						tuple[0]=o.getType(); 
						if (isSuper){
							superMap.put(tempStr, (String)theHeaders.get(i)+File.separator+(String)theRow[1]);
							tuple[2]=o;
						}
						else							tuple[index]=o;
					}
				}
				index++;
			}
		}
		addSuperNavigator(theRow, theHeaders);
	}
	
	protected void addSuperNavigator(Object[] theRow, Vector theHeaders){
		for (int i=2; i<theRow.length; i++){
			if (theRow[i]!=null && !(theRow[i] instanceof String)){
				//*Get the super infor
				Iterator ite = ((MMOutput)theRow[i]).values().iterator(); 				if (ite.hasNext()){
					while (ite.hasNext()) {
						 NamedVector nv = (NamedVector) ite.next();						 if (nv.getName().equals("super")){
								Iterator it = nv.iterator();
								while (it.hasNext()) {									 final String str = (String) it.next();
									 Object[] objArr=(Object[])me.mmatrixGui.navigator.get(str);
									 if (objArr!=null){
										  JakMatrix jm=new JakMatrix(objArr,theHeaders,true);
										  Iterator iter=jm.getNavigator().values().iterator();										  Object[] temObj;
										  while(iter.hasNext()){
												temObj=(Object[])iter.next();
												if (navigator.containsKey((String)temObj[1]))
													((Object[])navigator.get((String)temObj[1]))[2]=temObj[2];												else													navigator.put((String)temObj[1], temObj);										  }
									 }		  
							   }
								break;
						 }
					}
				}
			}
		}
	}
	
	/*Creat the matrix vector from the TreeMap. */
	public void createMatrix(){	
		Iterator it=navigator.values().iterator();
		Vector temVec1;
		Object[] temObj;
		while(it.hasNext()){
			temObj=(Object[])it.next();
			temVec1 = new Vector();
			for (int i=0; i<temObj.length; i++){
				if (i==0 || i==1){
					temVec1.add((String)temObj[i]);
				}
				else {
					if (temObj[i]!=null){
						temVec1.add(((MMOutput)temObj[i]).getDefn());
					}
					else{
						temVec1.add(temObj[i]);
					}
				}
			}
			myMatrix.add(temVec1);
		}
	}
	
}//end


