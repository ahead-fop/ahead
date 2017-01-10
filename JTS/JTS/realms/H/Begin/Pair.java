package Begin;

import java.io.*;

// Pair is used for entering elements on contents lists --
// a contents list is a set of declarations present in an HTML file.
// in general, a Pair is (name-of-method, method-call).
// this might be generalized in the future when we add properties

class Pair {
   String      name;
   Decl        d;

   public Pair( String name, Decl d) {
      this.name = name;
      this.d = d;
   }

   // set and get methods for Pair values

   public Decl getDecl() { 
      return d;
   }

   public void setDecl( Decl d ) {
      this.d = d;
   }

   public String getName() {
      return name;
   }

   public void setName( String n ) {
      name = n;
   }
    
   public void write( PrintWriter pw ) {
      if (name.equals(Constants.MAIN_NAME)) 
          d.write( pw );
      else
         Error.stop("unrecognizable Pair!");
   }

   public void inline( HTMLfile ext ) {
      d = d.inline(ext);
   }
}
