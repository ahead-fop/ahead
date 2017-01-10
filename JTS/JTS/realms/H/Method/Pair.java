package Method;

import java.io.*;

// pair is used for entering elements on contents lists --
// a contents list is a set of declarations present in an HTML file.
// in general, a pair is (name-of-method, method-call).
// this might be generalized in the future when we add properties

extends class Pair {
    
   public void write( PrintWriter pw ) {
      if (!name.equals(Constants.MAIN_NAME)) {
          pw.println(Constants.METHOD + " " + name);
          d.write( pw );
          pw.println(Constants.METHOD_END + " " + name);
      }
      Base( PrintWriter ).write(pw);
   }
}
