package Begin;

import java.io.*;

// StringWrapper holds a line and wraps it as an Action object

public class StringWrapper implements Action {
   String value;

   public StringWrapper( String v ) {
      value = v;
   }

   public String toString() {
      return value;
   }

   public void write( PrintWriter pw ) {
      pw.println( value );
   }

   public void inline( ProgramText t, HTMLfile f ) {
      // just copy this line into t
      t.add( this );
   }
}

