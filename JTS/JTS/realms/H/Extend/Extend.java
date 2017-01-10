package Extend;

import java.util.*;
import java.io.*;

// Extend objects represent <extend> invocations
// Extend objects must implement the Action interface

public class Extend implements Action {

   public Extend() { }

   public String toString() {
      return Constants.EXTEND;
   }

   public void write( PrintWriter pw ) {
      pw.println( this );  // Extend toString method
   }

   public void inline( ProgramText t, HTMLfile ext ) {
      // do nothing
   }
}
