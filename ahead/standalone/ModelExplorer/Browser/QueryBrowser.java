/* ******************************************************************
   class      : QueryBrowser
   description: A class which provides all the logic operation on a
					 model relation.
*********************************************************************/

package ModelExplorer.Browser;

import ModelExplorer.Main;
import ModelExplorer.Util.*;
import java.io.*;
import java.util.*;

public class QueryBrowser{
	private Vector myRelation;
	private int num_key=0;
	private String modelDir;

	//Construct a relation of the specified model directory
	public QueryBrowser(){
		modelDir = Main.modelDir;
		myRelation=new Vector();
		createRelation();
	}

	public QueryBrowser(String dir){
		modelDir = dir;
		myRelation=new Vector();
		createRelation();
	}

	//The method to create the relation which is called by the constuctor.
	protected void createRelation(){
		Main.fileExtens = new HashSet();
		//get the path list.
		ModelFilter	filter = new ModelFilter();
		filter.dirs.add("bin");
		Main.setFilter(filter);
		ArrayList pathList=new ArrayList();
		String[] list;
		String currentDir;
		StringTokenizer st;

		try{
			File path;
			if (Main.activeEquation == null){
				path=new File(modelDir);
				if (path.isDirectory()){
					list=path.list();
					for (int i=0; i<list.length; i++){
						if (!filter.dirs.contains(list[i])&&!filter.files.contains(list[i])&&!list[i].endsWith("~")){
							if (list[i].lastIndexOf(".")==-1 ||!filter.files.contains(list[i].substring(list[i].lastIndexOf(".")))){
								pathList.add(modelDir+"/"+list[i]+"/");
							}
						}
					}
				}
				else if (path.isFile()){
					String str=path.getName();
					if (str.lastIndexOf(".")!=-1)
						Main.fileExtens.add(str.substring(str.lastIndexOf(".")+1));
				}
			}
			else{
				BufferedReader br=new BufferedReader(new FileReader(modelDir+"/"+Main.activeEquation));
				String theStr;
				theStr=br.readLine();
				while(theStr!=null){
					if (!theStr.equals("")){
						path = new File(modelDir+"/"+theStr+"/");
						if (path.isDirectory()||path.isFile()){
							pathList.add(modelDir+"/"+theStr+"/");
						}
					}
					theStr=br.readLine();
				}
			}

			int index=0;
			while(pathList.size()>index){
				currentDir=(String)pathList.get(index);
				path=new File(currentDir);
				if (path.isDirectory()){
					pathList.set(index, "D "+currentDir);
					list=path.list();
					for (int i=0; i<list.length; i++){
						if (!filter.dirs.contains(list[i])&&!filter.files.contains(list[i])&&!list[i].endsWith("~")){
							if (list[i].lastIndexOf(".")==-1 ||!filter.files.contains(list[i].substring(list[i].lastIndexOf(".")))){
								pathList.add(currentDir+list[i]+"/");
							}
						}
					}
				}
				else{
					String str=path.getName();
					if (str.lastIndexOf(".")!=-1)
						Main.fileExtens.add(str.substring(str.lastIndexOf(".")+1));
					pathList.set(index, "F "+currentDir);
				}
				index++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		/*Creat the relation from the pathList. the relation is an ArrayList.
		  Each tuple is hold by a String Array.*/
		String tempStr;
		Vector tuple;
		int listSize=pathList.size();
		if (listSize>0){
			tempStr=(String)pathList.get(listSize-1);
			st = new StringTokenizer(tempStr.substring(modelDir.length()+2),"/");
			while(st.hasMoreTokens()){
				 num_key++;
				 st.nextToken();
			}
			int index;
			for(int i=0; i<listSize; i++){
				tuple=new Vector(num_key);
				tempStr=(String)pathList.get(i);
				st = new StringTokenizer(tempStr.substring(modelDir.length()+2),"/");
				index=0;
				while(st.hasMoreTokens()){
					tuple.add(st.nextToken());
					index++;
				}
				if (tempStr.charAt(0)=='D'){
					for(int j=index; j<num_key; j++){
						tuple.add(j,"<->");
					}
				}
				else{
					if (index<num_key){
						tempStr=(String)tuple.get(index-1);
						for(int j=index-1; j<num_key-1; j++){
							tuple.add(j,"<->");
						}
						tuple.add(num_key-1,tempStr);
					}
				}
				myRelation.add(tuple);
			}
		}
	}

	/*The select method which take an object(Pair) array to specify the projection.
	  and return the projection as an object array */
	public Vector select(Object[] keyValueList){
	   Pair onePair;
		ArrayList rlist=new ArrayList();
		int key;
		String value;
		int index, firstPair=1;

		if (keyValueList.length==0){
			return myRelation;
		}

		for (int i=0; i<keyValueList.length; i++){
			onePair=(Pair)keyValueList[i];
			key=onePair.getColumn();
			value=onePair.getValue();
			if (firstPair==1){
				for (int j=0; j<myRelation.size(); j++){
					if (((Vector)myRelation.get(j)).get(key).equals(value)){
						rlist.add(new Integer(j));
					}
				}
				firstPair=0;
			}
			else{
				index=0;
				for (int j=0; j<rlist.size(); j++){
					if (((Vector)myRelation.get(((Integer)rlist.get(j)).intValue())).get(key).equals(value)){
						rlist.set(index, rlist.get(j));
						index++;
					}
				}
				for (int j=rlist.size()-1; j>index-1; j--){
					rlist.remove(j);
				}
			}
		}

		Vector result=new Vector(rlist.size());
		for(int i=0; i<rlist.size(); i++){
			result.add((Vector)myRelation.get(((Integer)rlist.get(i)).intValue()));
		}
		return result;
	}

	//Return an Object array as the relation
	public Vector getRelation(){
		return myRelation;
	}

	//Return a set of values by a specified level
	public Object[] getLevelValues(int level){
		HashSet values=new HashSet();
		for (int i=0; i<myRelation.size(); i++){
			values.add(((Vector)myRelation.get(i)).get(level));
		}
		Object[] tempArray=values.toArray();
		Arrays.sort(tempArray);
		return tempArray;
	}

	public Object[] getLevelValues(Vector subRelation,int level){
		HashSet values=new HashSet();
		for (int i=0; i<subRelation.size(); i++){
			values.add(((Vector)subRelation.get(i)).get(level));
		}
		Object[] tempArray=values.toArray();
		Arrays.sort(tempArray);
		return tempArray;
	}

	//Get the number of the keys, which is the length of each tuple in the relation
	public int getNumKey(){
		return num_key;
	}

	//To test the relation and select method.
	public static void main(String[] args){
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		String s;
		System.out.println("Please give the model directory:");
		try{
			s=in.readLine();
			s.replace('\\', '/');
			File f=new File(s);
			while (!f.isDirectory()){
				System.out.println("Invalid model directory! try again:");
				s=in.readLine();
				s.replace('\\', '/');
			    f=new File(s);
			}
			StringBuffer strBuf;
			QueryBrowser bs=new QueryBrowser(s);

			//print out the relation
			System.out.println("\nThe original relation:\n");
			Vector r=bs.getRelation();
			for (int i=0; i<bs.getNumKey()-1; i++){
				System.out.print("L"+i+"\t");
			}
			System.out.println("Artifact\n");
			for (int i=0; i<r.size(); i++){
				strBuf=new StringBuffer();
				for (int j=0; j<bs.getNumKey(); j++){
					strBuf.append(((Vector)r.get(i)).get(j));
					strBuf.append("\t");
				}
				System.out.println(strBuf.toString());
			}

			//Test the getLevelValues() method
			int column;
			System.out.println("\nTest the getLevelValues() method:");
			System.out.println("Please give the column#:");
			s=in.readLine();
			column=Integer.parseInt(s);
			Object[] tempArray=bs.getLevelValues(column);
			System.out.println("\nThe given column values:\n");
			strBuf=new StringBuffer();
			for (int i=0; i<tempArray.length; i++){
				strBuf.append(tempArray[i]);
				strBuf.append("\n");
			}
			System.out.println(strBuf.toString());

			//using select method to creat the projection
			String value;
			ArrayList myPair=new ArrayList();
			System.out.println("\nUsing the select method to creat the projection:");
			System.out.println("Please give the column# and the value:");
			System.out.println("The column#:");
			while((s=in.readLine()).length()!=0){
				column=Integer.parseInt(s);
				while(column>bs.getNumKey()){
					System.out.println("The column#:");
					s=in.readLine();
					column=Integer.parseInt(s);
				}
				System.out.println("The value:");
				value=in.readLine();
				myPair.add(new Pair(column, value));
				System.out.println("The column#:");
			}
			r=bs.select(myPair.toArray());
			System.out.println("\nThe selection results:\n");
			for (int i=0; i<bs.getNumKey()-1; i++){
				System.out.print("L"+i+"\t");
			}
			System.out.println("Artifact\n");
			for (int i=0; i<r.size(); i++){
				strBuf=new StringBuffer();
				for (int j=0; j<bs.getNumKey(); j++){
					strBuf.append(((Vector)r.get(i)).get(j));
					strBuf.append("\t");
				}
				System.out.println(strBuf.toString());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}


