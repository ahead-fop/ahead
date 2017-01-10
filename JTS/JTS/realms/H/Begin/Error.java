package Begin;

import java.lang.*;
import java.util.*;
import java.io.*;

// handles all CH error reporting.  Similar to, but different than
// Util.java.  Not altogether clear that this is necessary.

class Error {

   private static PrintWriter reportStream = new PrintWriter(System.err);

   static public void setReportStream( PrintWriter w ) {
      reportStream = w;
   }

   static void stop( String msg ) throws RuntimeException {
      reportStream.println( "File " + Parse.filename + 
                          " line " + Parse.lineNo + " " + msg );
      reportStream.flush();
      throw new RuntimeException();
   }

   static void RuntimeStop( String msg ) throws RuntimeException {
      reportStream.println( msg );
      throw new RuntimeException();
   }
}
