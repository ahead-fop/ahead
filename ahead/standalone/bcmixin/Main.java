package bcmixin;

import java.util.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import java.io.*;
import java.util.regex.*;

/**
 * Read class file(s) and union all of its methods.
 *
 * @version $Id: Main.java,v 2.0 2004/03/31
 * @author  <A HREF="http://www.cs.utexas.edu/users/yczhou">Y. Zhou</A>
 */
public final class Main {
  final public static String COPYRIGHT = "(C) 2003 Yancong Zhou" ;
  final public static String NAME = "Main" ;
  final public static String VERSION = "v2003.05.12alpha" ;


  final public static HashSet ctable = new HashSet();
  private static ClassFileComposer composer;

  public static void main(String[] argv) {

	  if (argv.length<2){
		  usage();
	  }
	  if (argv[0].equals("-help")){
		  usage();
	  }
	  String modelDir = argv[0];
	  String equation = argv[1];
	  String pname;

	  if (argv.length==3)
	  	 pname = argv[2];
	  else
	  	 pname = equation.substring(0, equation.lastIndexOf("."));

	  composer = new ClassFileComposer();
	  ClassFileComposer.pname = pname;

	  File dir = new File(modelDir);
	  if (!dir.isDirectory()){
	  	  System.out.println("invalid model directory!");
	  	  System.exit(1);
	  }
	  File equationFile = new File(dir, equation);
	  if (!equationFile.isFile()){
	  	 System.out.println("invalid equation file!");
	  	 System.exit(1);
	  }

	  //get all the layers by the given equation inside the model
	  ArrayList layers = new ArrayList();
	  try{
		 BufferedReader br = new BufferedReader(new FileReader(equationFile));
		 String line = br.readLine();
		 while (line!=null){
			if (!line.startsWith("\\") && !line.startsWith("##") && !line.equals("")){
				 File file = new File(dir, line.trim());
				 if (file.isDirectory() && !file.getName().equals(dir.getName())){
                                        //System.out.println("layer: "+file.getName());
					layers.add(file);
                                 }
				 else{
					System.out.println("Layer "+line+" inside equation file is invalid!");
					System.exit(1);
				 }
			 }
			 line = br.readLine();
		 }
	  }catch(IOException e){
		  System.out.println("in getlayers:");
		  System.out.println(e);
		  System.exit(1);
	  }

	  //get all the files in the layers
	  Hashtable units = new Hashtable();
	  ArrayList alist;
	  String[] list;
	  for (int j=0; j<layers.size(); j++){
		//System.out.println("files in layer"+j);
		  list = ((File)layers.get(j)).list();
		  for (int k=0; k<list.length; k++){
			  if (list[k].endsWith(".class")){
				//System.out.println(list[k]);
				  if (units.containsKey(list[k])){
					  alist=(ArrayList)units.get(list[k]);
					  alist.add(layers.get(j));
				  }
				  else{
					  alist=new ArrayList();
					  alist.add(layers.get(j));
					  units.put(list[k],alist);
				  }
			  }
		  }
	  }

	  //Get a table for final class names
	  Iterator it = units.keySet().iterator();
	  while (it.hasNext()){
		  String key = (String)it.next();
		  if (key.endsWith("$$.class")){
			  alist = (ArrayList)units.get(key);
			  for (int y=0; y<alist.size()-1; y++){
				  ctable.add(key.substring(0,key.lastIndexOf("."))+((File)alist.get(y)).getName());
			  }
			  ctable.add(key.substring(0,key.indexOf("$")));
		  }
		  if (key.indexOf("$$$")!=-1){
			  ctable.add(key.substring(0,key.indexOf("$$$"))+key.substring(key.indexOf("$$$")+2,key.lastIndexOf(".")));
		  }
	  }

	  //call the coposition process on each entry in the hashtable
	  //File composedDir = new File(dir, equation.substring(0, equation.lastIndexOf(".")));
      File composedDir = new File(dir, pname);
	  if (!composedDir.isDirectory())
			  composedDir.mkdir();
	  it = units.keySet().iterator();
	  while (it.hasNext()){
		  String key = (String)it.next();
		  //System.out.println(key + " in layers:");
		  if (key.endsWith("$$.class")){
			  alist = (ArrayList)units.get(key);
			  Vector operands = new Vector();
			  for (int x=0; x<alist.size(); x++){
				 //System.out.println("  "+((File)alist.get(x)).getPath());
				   operands.add(new File((File)alist.get(x), key));
			  }
			  //generating stub process
              //System.out.println("The file is: "+key);
			  composer.Main(composedDir, key.substring(0,key.lastIndexOf(".")), operands);
		  }
	  }

          /** write out all the composed class names, only for special use

          it = ctable.iterator();
          try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File("composedclass.txt")));
                while(it.hasNext()){
                        String s = (String)it.next();
                        s = s.substring(s.lastIndexOf(File.separator)+1);
                        bw.write(s);
                        bw.newLine();
                }
                bw.flush();
                bw.close();
          }catch(IOException ioe){}*/


  }


   /**
     * Print a usage message in response to a <code>-help</code> command-line
     * option, then exit successfully.
     **/
    final private static void usage () {
		 usage_print () ;
		 System.exit (0) ;
    }

    /**
     * Print a usage message in response to an argument-parsing error,
     * then abort with a message about the argument error.
     *
     * @param message a {@link String} describing the argument-parsing error.
     **/
    final private static void usage (String message) {
		 usage_print () ;
		 System.err.println () ;
		 abort (message) ;
    }

    /**
     * Utility method to print a usage message onto standard output.
     **/
   final private static void usage_print () {
		System.out.println (NAME + ' ' + VERSION + ' ' + COPYRIGHT) ;
		System.out.println () ;

		System.out.println (
		    "Usage: java bcmixin.Main [<options>] <modelname> <equationname>"
		) ;

		System.out.println () ;
		System.out.println ("where [<options>] include any of the following:") ;
		System.out.println () ;
		System.out.println (" -help") ;
		System.out.println ("  Prints this helpful message, then exits.") ;
		System.out.println () ;
		System.out.println (" where <modelname> means the model name that you want to work on.") ;
		System.out.println () ;
		System.out.println ("where <equationname> provides the equation name, which must end with .equation.") ;
		System.out.println () ;
   }

	//When things go really wrong, this method is invoked.
	final private static void abort (final String message) {
		System.out.println(message) ;
		System.exit (1) ;
   }
}
