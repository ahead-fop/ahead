package Copy;

import java.util.*;
import java.io.*;

// copy objects represent <copy file > invocations
// copy objects must implement the Action interface

public class Copy implements Action {
   public String fileName;

   public Copy( String name ) { 
      fileName = name;
   }

   // used for generating HTML files

   public String toString() {
      return Constants.COPY + " " + fileName + " " + Constants.WIDGET;
   }

   public void write( PrintWriter pw ) {
      pw.println( this );  // call toString method
   }

   // we don't inline "copied" files.  Instead, we copy them
   // when we write out the generated HTML file.  All we do now
   // is register the file name with the program text file.

   public void inline( ProgramText t, HTMLfile ext ) {
      t.parent.addFile2Copy( t.parent.directory, fileName );
   }
}
