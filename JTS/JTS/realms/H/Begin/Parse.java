package Begin;

import java.io.*;
import java.util.*;
import java.lang.*;

// this is the "parser" of HTML files.  Actually its really crude --
// each line is read in, and the first token is examined.  If it is
// a CH command, then we act on it, otherwise we buffer the line.

public class Parse {

   static String           directory; // directory of file to parse
   static String           filename;  // name of file to parse
   static HTMLfile         html;      // internal representation of file
   static Stack            stack;     // stack used in parsing nested methods
                                      // i.e., methods inside "main"

   static String    line   = null;    // current line
   static int       lineNo = 0;       // line number of current line
   static LineNumberReader f = null; // file reader

   // note: all the above members are static, because we only parse
   // one file at a time. This gives other classes the ability to
   // get localized parsing information without knowing the parse object
   // that is currently being used.

   public Parse( String Directory, String fileName ) {
      directory = Directory;
      filename = fileName;
      html     = new HTMLfile( directory, fileName );
      stack    = new Stack();
   }

   public String fileName() {
      return directory + "/" + filename;
   }

   public HTMLfile parse() {

      // Step 1: open file

      try {
         f = new LineNumberReader( new FileReader( fileName() ));
      }
      catch (Exception e) {
         Error.stop("cannot open " + fileName() + " : " + e.getMessage());
      }
       
      // Step 2: read the file one line at a time.
      //         after a line is read, examine its contents
 
      while (true) {
         nextLine();
         if (line == null)
            break;
         action(firstToken());
      }

      // Step 3: close file.  If the stack is not empty, then
      //         we are parsing a nested construct that hasn't
      //         terminated

      html.put( "main" );
      if (!empty()) 
         Error.stop( "last method in " + fileName() + " not terminated" );

      try {
         f.close();
      }
      catch (Exception e) { 
         Error.stop("cannot close " + fileName() + " : " +e.getMessage());
      }
      return html;  
   }

   // take action on line 'line' whose first token is 'token'

   public void action( String token ) {
      if (!parseLine(token))
         html.addLast(line);  // add to main method
   }

   // returns true if the line contained a CH feature
   // false means add the line to main

   public boolean parseLine( String token ) { 
      return false;
   }

   // return first token of 'line'
   // if there is no such token, return ""

   public String firstToken() {
      StringTokenizer t = new StringTokenizer(line);
      if (t.hasMoreTokens())
         return t.nextToken();
      return "";
   }

   // return first n tokens of 'line'
   // if such tokens are missing, "" is returned in their place

   public String[] firstNTokens( int count ) {
      String args[] = new String[count];
      StringTokenizer t = new StringTokenizer(line);
      int i = 0;
      while (count-- > 0) {
        if (t.hasMoreTokens()) 
           args[i++] = t.nextToken();
        else
           args[i++] = null;
      }
      return args;
   }

   // get the next line, lineNo in the input file

   public String nextLine() {
      try {
         line = f.readLine();
         lineNo = f.getLineNumber();
      } 
      catch (Exception e) { 
         Error.stop("cannot read line from " + fileName() + " : " +
                    e.getMessage()); 
      }
      return line;
   }

   // push the current ProgramText object onto a stack
   // and create a new ProgramText object

   public void push( String name ) {
      stack.push( html.pt );
      html.pt = new ProgramText( name, html );
   }

   // pop the ProgramText object off the stack and 
   // assign it to the html object

   public void pop() {
      html.pt = (ProgramText) stack.pop();
   }

   // is the parsing stack empty?

   public boolean empty() {
      return stack.empty();
   }
}
