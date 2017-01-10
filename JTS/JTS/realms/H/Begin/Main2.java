package Begin;

import java.io.*;
import java.util.*;
import java.lang.*;
import Jakarta.util.*;

// another command-line interface to XC

public class Main2 {

   static void usage() {
      System.err.println( "Usage: XC.Main2 basefile extfile1 extfile2 ...");
      System.err.println( "       all files in same directory");
      System.exit(1);
   }

   public static void main( String args[] ) {
      // for debugging only 
      int i;

      if (args.length <= 1) {
         usage();
         return;
      }

      MLObject base = new MLObject( ".", args[0] );
      for ( i = 1; i < args.length; i++ ) {
         MLObject m = new MLObject( ".", args[i] );
         if (m.isExtension())
            base.compose( m );
         else
            Util.stop(args[i] + " is not an extension");
      }
      base.write( ".", "result.html" );
      System.out.println( "result.html written" );
   }
}
