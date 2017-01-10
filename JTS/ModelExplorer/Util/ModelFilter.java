/* ******************************************************************
   class      : ModelFilter
   description: A ModelFilter class for hidden the specified files or 
					 dirs in the model directory.
*********************************************************************/

package ModelExplorer.Util;

import java.io.*;
import java.util.*;
import ModelExplorer.Editor.Utils.ResourceUtil;
import ModelExplorer.Main;

/* ******************************************************************
   top level class declaration
*********************************************************************/

public class ModelFilter{

  public static String FILTERFILE_PREFIX = "filterfiles";
  public static String FILTERDIR_PREFIX = "filterdirs";
  protected Properties prop;
  protected Properties prop0;
  public HashSet files;
  public HashSet dirs;

  // constructors
  public ModelFilter(){
	  prop = new Properties();
	  try{
	      prop.load(getClass().getResourceAsStream("_Filter.properties"));
	  }catch(IOException ie1){}
	 
	  String str=Main.modelDir+File.separator+"_Filter.properties";
	  if ((new File(str)).isFile()){
		  prop0 = new Properties();
		  try{
			  prop0.load(new FileInputStream(str));
		  }catch(IOException ie1){}
	  }	 
	  loadFilterFiles();
	  loadFilterDirs();
  } 
 
  protected void loadFilterFiles() {
	 files = new HashSet();
    int i         = 1;
    String words  = prop.getProperty(FILTERFILE_PREFIX + 1);
	 StringTokenizer t;
    while (words != null) {
      t = new StringTokenizer(words, " ");
      while (t.hasMoreTokens()) {
           files.add(t.nextToken());
      } 
      i++;
      words = prop.getProperty(FILTERFILE_PREFIX + i);
    } 
	 if (prop0!=null){
		 i=1;
		 words =prop0.getProperty(FILTERFILE_PREFIX + 1);
		 while(words!=null){
			  t = new StringTokenizer(words, " ");
			  while (t.hasMoreTokens()) {
				    files.add(t.nextToken());
			  } 
			  i++;
			  words = prop0.getProperty(FILTERFILE_PREFIX + i);
		 }  
    } 
  } 

  protected void loadFilterDirs() {
	 dirs = new HashSet();
    int i         = 1;
    String words  = prop.getProperty(FILTERDIR_PREFIX + i);
    StringTokenizer t;

    while (words != null) {
      t = new StringTokenizer(words);
      while (t.hasMoreTokens()) {
           dirs.add(t.nextToken());
      } 
      i++;
      words = prop.getProperty(FILTERDIR_PREFIX + i);
    } 

    if (prop0!=null){
	i=1;
	words =prop0.getProperty(FILTERDIR_PREFIX + i);
	while(words!=null){
	    t = new StringTokenizer(words, " ");
	    while (t.hasMoreTokens()) {
		dirs.add(t.nextToken());
	    } 
	    i++;
	    words = prop0.getProperty(FILTERDIR_PREFIX + i);
	}  
    } 
	 
  }  

	public static void main(String[] args)
	{
		new ModelFilter();
	}
}
