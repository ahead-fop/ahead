/**
 * class: SubMatrix
 */

package ModelExplorer.MMatrixBrowser;

import java.io.*;
import java.util.*;
import mmatrix.*;
import ModelExplorer.Util.*;
import ModelExplorer.Main;

public class SubMatrix extends Matrix{
	
	private String subPath;
	private SubMatrix parent;
	
	//Construct a matrix of the specified model directory
	public SubMatrix(SubMatrix parent, Object[] objs, Vector theHeaders){
		super();
		if (parent!=null)
			this.parent=parent;
		columnHeaders = theHeaders;
		if (parent!=null)
			subPath=parent.getSubPath()+objs[1]+File.separator;
		else
			subPath=File.separator+objs[1]+File.separator;
		nested = new HashMap();
		createMatrix(objs);
	}

	//The method to create the matrix which is called by the constructor.
	protected void createMatrix(Object[] objs){
	
		ModelFilter	filter = new ModelFilter();
		filter.dirs.add("bin");
		Main.setFilter(filter);
		String currentDir;
		String[] list;
		Object[] tuple;

		try{
			File path, file;
			int len=columnHeaders.size();
			for (int j=2; j<objs.length;j++){
				if (objs[j]!=null){
					currentDir=Main.modelDir+File.separator+columnHeaders.get(j)+subPath;
					path=new File(currentDir);
					list=path.list();
					for (int i=0; i<list.length; i++){
						file = new File(currentDir+list[i]+"/");
						if (file.isFile() && Main.appliedFileExtens.contains(list[i].substring(list[i].indexOf('.')+1)))
						{
							//tempStr = list[i].substring(0,list[i].indexOf('.'));
							//tempStr = list[i];
							//System.out.println(file.getName());
							if (navigator.containsKey(list[i])){
								tuple = (Object[])navigator.get(list[i]);
							}
							else{
								tuple = new Object[len];
								tuple[1]=list[i];
								navigator.put(list[i], tuple);
							}
							if (list[i].substring(list[i].indexOf('.')+1).equals("jak")){
								try {
									MMOutput output = mmatrix.Main.eval( currentDir+list[i] );
									tuple[0]=output.getType();
									tuple[j]=output;
								}
								catch (Exception e) {
								   //tuple[1]="u";
								   tuple[j]="1";
								}
							}
							else{
								tuple[0]="u";
								tuple[j]="1";
							}
						}
						else if (file.isDirectory()&&!filter.dirs.contains(list[i])){
							if (navigator.containsKey(list[i])){
								tuple = (Object[])navigator.get(list[i]);
							}
							else{
								tuple = new Object[len];
								tuple[1]=list[i];
								navigator.put(list[i], tuple);
							}
							tuple[0]="+";
							tuple[j]="1";
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		/*Creat the matrix vector from the TreeMap. */
		Iterator it=navigator.values().iterator();
		Vector temVec1;
		Object[] temObj;
		while(it.hasNext()){
			temObj=(Object[])it.next();
			temVec1 = new Vector();
			/** Creat one row and add it to myMatrix. */
			for (int i=0; i<temObj.length; i++){
				if (i==0 || i==1){
					temVec1.add((String)temObj[i]);
				} //end if
				else {
					if (temObj[i]!=null){
						if (temObj[i] instanceof String){
							temVec1.add("1");
						} // end if
						else{
							temVec1.add(((MMOutput)temObj[i]).getDefn());
						} // end else
					} // end if
					else{
						temVec1.add(temObj[i]);
					} //end else
				} // end else
			} // end for
			myMatrix.add(temVec1);
			/** Creat the nested matrix if this is a directory. 
			 * Add the nested matrix to nested hashmap
			 */
			if (temObj!=null && ((String)temObj[0]).equals("+")){
				nested.put(temObj[1], new SubMatrix(this,temObj,columnHeaders));
			} // end if
		} // end while
	} // end creatMatrix
	
	public String getSubPath(){
		return subPath;
	}
	
	public SubMatrix getParent(){
		return parent;
	}
	
} // end SubMatrix


