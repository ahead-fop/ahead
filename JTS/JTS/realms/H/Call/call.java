package Call;

import java.util.*;
import java.io.*;

// call objects represent <call Amethod > invocations
// call objects must implement the Action interface

public class call implements Action {
   public String methodName;

   public call( String name ) { 
      methodName = name;
   }

   // used for generating HTML files

   public String toString() {
      return Constants.CALL + " " + methodName + " " + Constants.WIDGET;
   }

   public void write( PrintWriter pw ) {
      pw.println( this );  // call toString method
   }

   // base file action is "call N".  We must find method "N" in the
   // extension file (ext).  Then we must copy each action in N
   // into t.  If there is no such method, notify as an error.

   public void inline( ProgramText t, HTMLfile ext ) {
      ProgramText p = (ProgramText) ext.get( methodName );
      if (p == null) {
         Error.RuntimeStop("call to non-existent method " + methodName);
      }
      ListIterator li = p.listIterator();
      while( li.hasNext() ) {
         t.addLast( (Action) li.next() );
      }
   }
}
