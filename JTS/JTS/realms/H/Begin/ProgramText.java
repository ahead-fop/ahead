package Begin;

import java.util.*;
import java.io.*;

// a ProgramText object is CH's internal representation of a method

public class ProgramText extends LinkedList implements Decl {

   HTMLfile parent;
   String methodName;

   public void setName( String n ) { 
      methodName = n;
   }

   public String getName() {
      return methodName;
   }

   public ProgramText( String name, HTMLfile p ) {
      methodName = name;
      parent     = p;
   }

   // convert contents of program text object into a string.

   public String toString() {
      String result = "";
      boolean surround = !methodName.equals(Constants.MAIN_NAME);

      // Step 1: if this programtext object does not corresponds to MAIN
      //         then make a proper declaration

      if (surround)
         result = Constants.METHOD + " " + methodName + " " +
                  Constants.WIDGET + "\n";

      // Step 2: translate each line of the object

      ListIterator li = listIterator(0);
      while (li.hasNext()) {
         Action a = (Action) li.next();
         result += a.toString() + "\n";
      }

      // Step 3: finish off the definition, if necessary, and return

      if (surround)
         result += Constants.METHOD_END + "\n";
      return result;
   }

   // add o to the end of the programtext list
   // if o is a string, place a StringWrapper around it 
   // if o isn't a string, make sure that it is an Action object

   public void addLast( Object o ) {
      if (o instanceof String)
          super.addLast( new StringWrapper( (String) o ));
      else
          super.addLast( (Action) o);
   }

   // write the contents of the programtext object (e.g., method)
   // to the given printwriter

   public void write( PrintWriter pw ) {
      boolean surround = !methodName.equals(Constants.MAIN_NAME);

      // Step 1: if this programtext object does not corresponds to MAIN
      //         then make a proper declaration

      if (surround)
         pw.println(Constants.METHOD + " " + methodName + " " +
                  Constants.WIDGET);

      // Step 2: translate each line of the object

      ListIterator li = listIterator();
      while (li.hasNext()) {
         Action a = (Action) li.next();
         pw.println( a.toString() );
      }

      // Step 3: finish off the definition, if necessary, and return

      if (surround)
         pw.println( Constants.METHOD_END );
   }

   public ProgramText inline( HTMLfile ext ) {

      // Step 1: we are going to create a replacement for the
      //         current PT object.  It will have the same name,
      //         but its calls will be expanded

      ProgramText t = new ProgramText( methodName, this.parent );

      ListIterator li = listIterator();
      while (li.hasNext()) {
         Action a = (Action) li.next();
         a.inline( t, ext );
      }
      return t;
   }
}
