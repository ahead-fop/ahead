package Extend;

import java.io.*;

extends class Parse {

   public boolean extension = false;

   public boolean parseLine( String token ) {
      if (token.equals(Constants.EXTEND)) {

         // Step 1: do minimalist error checking and argument harvesting

         if (extension)
            Error.stop("too many <extend> statements in document");
         if (inMethod())
            Error.stop("<extend> statement embedded inside method");

         // Step 2: add an extension object to the html object

         html.addLast( new Extend() );
         extension = true;

         // Step 3: return true because we consumed this input

         return true;
      }
      else
      return Base( String ).parseLine( token );
   }
}
