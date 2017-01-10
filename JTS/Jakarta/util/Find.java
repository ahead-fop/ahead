package Jakarta.util;

import Jakarta.io.ExtensionFilenameFilter ;

import java.io.File ;

import java.util.Iterator ;
import java.util.ListIterator ;
import java.util.Vector ;

public class Find {

    public static Vector find (String dir, String ext) {
	File directory = new File (dir) ;

	if (! directory.isDirectory ()) {
	    System.err.println (directory.toString() + " is not a directory") ;
	    System.exit (1) ;
	}

	ExtensionFilenameFilter filter = new ExtensionFilenameFilter (ext) ;

	return (Vector) filter.collection (directory, new Vector ()) ;
    }

    // does the same thing as Find, except that what is returned is 
    // a list of file names of the form <name>.<ext> -- the leading
    // path (e.g. "c:/foo/bar/") is eliminated

    public static Vector truncatedFind (String dir, String ext) {
	Vector list = find (dir, ext) ;
	for (ListIterator p = list.listIterator () ; p.hasNext () ; )
	    p.set ( ((File) p.next ()) . getName () ) ;
	return list ;
    }

    // call: java Jakarta.util.Find

    public static void main( String[] args ) {
	String directory = args.length > 0 ? args[0] : "." ;
	String extension = args.length > 1 ? args[1] : ".jak" ;

	Iterator p = find (directory, extension) . iterator () ;
	while (p.hasNext ())
	    System.out.println (p.next ()) ;

	p = truncatedFind (directory, extension) . iterator () ;
	while (p.hasNext ())
	    System.out.println (p.next ()) ;
    }

    // returns string name of current directory -- but not
    // the absolute path

   public static String currentDirectory() {
      // Step 1: path that is returned looks like "blah/blah/directory/."

      String fs = new File(".").getAbsolutePath();

      // Step 2: now trim off the "/." from the end

      int i = fs.length();
      String fs1 = fs.substring(0, i-2);

      // Step 3: trim off the leading blahs

      i = fs1.lastIndexOf( File.separatorChar );
      return fs1.substring( i+1, fs1.length());
   }

   // returns the string name of the directory containing
   // the current directory

   public static String currentSuperDirectory()  {
      // Step 1: path that is returned looks like "blah/super/directory/."

      String fs = new File(".").getAbsolutePath();

      // Step 2: now trim off the "/." from the end

      int i = fs.length();
      String fs1 = fs.substring(0, i-2);

      // Step 3: trim off the trailing "directory/"

      i = fs1.lastIndexOf( File.separatorChar );
      String fs2 = fs1.substring(0, i);
 
      // Step 34: trim off the leading blahs

      i = fs2.lastIndexOf( File.separatorChar );
      return fs2.substring( i+1, fs2.length());
   }

/*
   // for debugging only 

   public static void main(String args[]) {
      System.out.println( currentDirectory() );
      System.out.println( currentSuperDirectory() );
   }
*/
}
