/* ******************************************************************
   class      : MRUList
   description: A class to hold the most recently used files list.

*********************************************************************/

package ModelExplorer.Util;

import java.io.*;
import java.util.*;

/* ******************************************************************
   top level class declaration
*********************************************************************/

public class MRUList{

  public static int MRU_MAX=4;
  private String fileName;
  public String[] myList;

  // constructors
  public MRUList(String file){ 
    fileName = file;
	 load();
  } 
 
  protected void load() {
	 myList = new String[8];
    int i         = 1;
    
	 try{
		 BufferedReader br=new BufferedReader(new FileReader(fileName));
		 String str=br.readLine();
		 while(str!=null){
			 myList[i-1]=str;
			 i++;
			 str=br.readLine();
		 }
		 br.close();
	 }catch(IOException e){
		 //System.out.println(e);
	 }
  } 
  
  protected int checkDuplicate(String file){
	  int indexDup=-1;
		for (int i=0; i<MRU_MAX; i++){
			if (myList[i]!=null){
			  if (myList[i].equals(file)){
				  indexDup=i;
				  return indexDup;
			  }
			}
			else
				break;
		}
	  return indexDup;
  }
  
  public void add(String file){
	  int index=checkDuplicate(file);
	  if (index==-1){
		  for (int i=MRU_MAX-2; i>=0; i--){
			  myList[i+1]=myList[i];
		  }
		  myList[0]=file;
	  }
	  else{
		  if (index!=0){
			  for (int i=index-1; i>=0; i--){
				  myList[i+1]=myList[i];
			  }
			  myList[0]=file;
		  }
	  }
  }
  
  public void save(){
	  try{
			BufferedWriter bw=new BufferedWriter(new FileWriter(fileName));
			for (int i=0; i<MRU_MAX; i++){
				  if(myList[i]!=null){
					  bw.write(myList[i]);
					  bw.newLine();
				  }
				  else{
					  bw.flush();
					  bw.close();
					  return;
				  }
			}
			bw.flush();
		   bw.close();
	  }catch(IOException e){
		  System.out.println(e);
	  }
  }
}