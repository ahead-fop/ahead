package Method;

import java.io.*;

// this class adds the ability to parse method declarations.
// they are of the form:  <METHOD name > ... </METHOD>

public extends class Parse {

   String methodName = null;

   public boolean parseLine( String token ) {
      if (token.equals(Constants.METHOD)) {
         if (methodName == null) {
            String args[] = firstNTokens( 3 );
            methodName = args[1];
            if (args[2] == null || !args[2].equals(Constants.WIDGET) ||
                methodName == null || methodName == "" )
               Error.stop("incorrectly formatted method declaration: " + line);
            push( methodName );
            return true;
         }
         else {
            Error.stop("method declarations can not be nested");
            return true;
         }
      }
      else
      if (token.equals(Constants.METHOD_END)) {
         if (methodName == null) {
            Error.stop("method end declaration not paired with method begin");
            return true;
         }
         else {
            // end of method
            html.put( methodName );
            methodName = null;
            pop();
            return true;
         }
      }
      else
      if (methodName != null) {
         html.addLast(line);
         return true;
      }
      else
      return Base( String ) .parseLine( token );
   }

   public boolean inMethod() {
      return methodName != null;
   }
}
