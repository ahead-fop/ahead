package bctostub;

import java.util.*;
import java.io.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Repository;
/**
 * Read class file(s) and union all of its methods.
 *
 * @version $Id: bctostub.Main.java,v 1.0 2003/04/30
 * @author  <A HREF="http://www.cs.utexas.edu/users/yczhou">Y. Zhou</A>
 */
public final class Main {
  final public static String COPYRIGHT = "(C) 2003 Yancong Zhou" ;
  final public static String NAME = "bctostub.Main" ;
  final public static String VERSION = "v2003.04.30alpha" ;

  public static void main(String[] argv) {
	  // Parse command-line arguments:

	 if (argv.length==0){
		 usage();
	 }
	 String file_name=null;
	 List args = Arrays.asList (argv) ;
    for (Iterator p = args.iterator () ; p.hasNext () ; ) {
	    String arg = (String) p.next () ;
	    if (arg.equals ("-help"))
			 usage () ;
	    else
			file_name = arg ;
	}

	//change class name process
	 renameProcess(file_name);

  }
  public static void renameProcess(String file_name){
	  ClassGen cg;     try {
		 File file = new File(file_name);
		 if (file.isFile()&&file_name.endsWith(".class")){
			 //System.out.println(file_name);
			 JavaClass jc = new ClassParser (file_name) . parse () ;
			 cg = new ClassGen(jc);
			 cg.setClassName("stub."+cg.getClassName());			 try{
				cg.getJavaClass().dump(file_name);
			 }catch(java.io.IOException e) { System.err.println(e); }		 }
		 else if (file.isDirectory()){
			//System.out.println("I'm a directory.");
			String[] list=file.list();
			for (int i=0; i<list.length; i++){
				renameProcess(file.getPath()+File.separator+list[i]);
			}
		 }
	  }catch (Exception e){System.err.println(e);}
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
		    "Usage: java bctostub.Main [<options>] <file_name>"
		) ;

		System.out.println () ;
		System.out.println ("where <options> include any of the following:") ;
		System.out.println () ;
		System.out.println (" -help") ;
		System.out.println ("  Prints this helpful message, then exits.") ;
		System.out.println () ;
		System.out.println ("Where file_name means the class directory.");
		System.out.println ();
   }

	//When things go really wrong, this method is invoked.
	final private static void abort (final String message) {
		System.out.println(message) ;
		System.exit (1) ;
   }
}
