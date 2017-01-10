/**
 * class: MMatrix
 */

package ModelExplorer.MMatrixBrowser;

import java.io.*;
import java.util.*;
import mmatrix.*;
import ModelExplorer.Util.*;
import ModelExplorer.Main;

public class MMatrix extends Matrix{
	
	//Construct a matrix of the specified model directory
	public MMatrix(){
		super();
		nested = new HashMap();
		createMatrix();
	}

	//The method to create the matrix which is called by the constructor.
	protected void createMatrix(){
		//get the path list.
		ModelFilter	filter = new ModelFilter();
		filter.dirs.add("bin");
		Main.setFilter(filter);
		ArrayList pathList=new ArrayList();
		String[] list;
		String currentDir;
		String tempStr;
		Object[] tuple;

		try{
			File path, file;
			if (Main.activeEquation == null){
				path=new File(Main.modelDir);
				if (path.isDirectory()){
					pathList.add(Main.modelDir+"/");
					list=path.list();
					for (int i=0; i<list.length; i++){
						if (!filter.dirs.contains(list[i])&&!filter.files.contains(list[i])){
							pathList.add(Main.modelDir+"/"+list[i]+"/");
						}
					}
				}
				int index=1;
				columnHeaders.add("Artifact Type");
				columnHeaders.add("Artifact");
				columnHeaders.addElement(".");
				while(pathList.size()>index){
					currentDir=(String)pathList.get(index);
					path=new File(currentDir);
					if (path.isDirectory()){
						tempStr=currentDir.substring(0,currentDir.length()-1);
						tempStr=tempStr.substring(tempStr.lastIndexOf('/')+1);
						columnHeaders.add(tempStr);
					}
					index++;
				}
			}
			else{	
				BufferedReader br=new BufferedReader(new FileReader(Main.modelDir+"/"+Main.activeEquation));
				String theStr;
				columnHeaders.add("Artifact Type");
				columnHeaders.add("Artifact");
					
				theStr=br.readLine();
				while(theStr!=null){
					if (!theStr.equals("")){
						file = new File(Main.modelDir+"/"+theStr+"/");
						if (file.isDirectory()){
							pathList.add(Main.modelDir+"/"+theStr+"/");
							columnHeaders.add(theStr);
						}
					}
					theStr=br.readLine();
				}
			}
						
			int len=columnHeaders.size();
			int index=0;
			for (int j=0; j<pathList.size();j++){
				currentDir=(String)pathList.get(j);
				path=new File(currentDir);
				if (path.isDirectory()){
					list=path.list();
					for (int i=0; i<list.length; i++){
						file = new File(currentDir+list[i]+"/");
						if (file.isFile() && Main.appliedFileExtens.contains(list[i].substring(list[i].indexOf('.')+1)))
						{
							//tempStr = list[i].substring(0,list[i].indexOf('.'));
							tempStr = list[i];
							//System.out.println(file.getName());
							if (navigator.containsKey(tempStr)){
								tuple = (Object[])navigator.get(tempStr);
							}
							else{
								tuple = new Object[len];
								tuple[1]=tempStr;
								navigator.put(tempStr, tuple);
							}
							if (Main.appliedFileExtens.contains(list[i].substring(list[i].indexOf('.')+1))){
								try {
										  //System.out.println( currentDir+list[i] );
									MMOutput output = mmatrix.Main.eval( currentDir+list[i] );
									tuple[0]=output.getType();
									tuple[index+2]=output;
								}
								catch (Exception e) {
									//tuple[1]="u";
									tuple[index+2]="1";
								}
							}
							else{
								tuple[0]="u";
								tuple[index+2]="1";
							}
						}
						else if (j!=0&&file.isDirectory()&&!filter.dirs.contains(list[i])){
							if (navigator.containsKey(list[i])){
								tuple = (Object[])navigator.get(list[i]);
							}
							else{
								tuple = new Object[len];
								tuple[1]=list[i];
								navigator.put(list[i], tuple);
							}
							tuple[0]="+";
							tuple[index+2]="1";
						}
					}
					index++;
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
			if (temObj[0]!=null && ((String)temObj[0]).equals("+")){
				nested.put(temObj[1], new SubMatrix(null, temObj, columnHeaders));
			} // end if
		} // end while
	} // end creatMatrix
} // end MMatrix


