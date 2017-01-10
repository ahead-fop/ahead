package Begin;

import java.io.*;
import java.util.*;
import java.lang.*;
import Jakarta.util.*;

// command-line interface to CH

public class Main {

   static void usage() {
      System.err.println( "Usage: XC.Main baseDir extDir1 extDir2 ...");
      System.err.println( "       all HTML files must end in .htm");
      System.err.println( "       so base file is in baseDir/baseDir.htm, etc.");
      System.exit(1);
   }

   static String filename( String n ) {
      return n + ".htm";
   }

   public static void main( String args[] ) {
      // for debugging only 
      int i;

      if (args.length <= 1) {
         usage();
         return;
      }

      MLObject base = new MLObject( args[0], filename(args[0]) );
      for ( i = 1; i < args.length; i++ ) {
         MLObject m = new MLObject( args[i], filename(args[i]) );
         if (m.isExtension())
            base.compose( m );
         else
            Util.stop(filename(args[i]) + " is not an extension" );
      }
      base.write( ".", "result.html" );
      System.out.println( "result.html written" );
   }
}
