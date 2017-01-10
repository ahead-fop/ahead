package Call;

import java.io.*;

extends class Parse {

   public boolean parseLine( String token ) {
      if (token.equals(Constants.CALL)) {
         String args[] = firstNTokens(3);

         // Step 1: do minimalist error checking and argument harvesting

         String methodName = args[1];
         if (methodName == null || !args[2].equals(Constants.WIDGET))
            Error.stop("call statement incorrectly formatted");

         // Step 2: add a call object to the html object

         html.addLast( new call( methodName ) );

         // Step 3: return true because we consumed this input

         return true;
      }
      else
      return Base( String ).parseLine( token );
   }
}
