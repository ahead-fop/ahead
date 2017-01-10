package Begin;

import java.util.*;
import java.io.*;

// each HTML file is represented internally a single HTMLfile object
// an HTMLfile object maintains a symboltable (of all the methods
// declared in the HTML file), a linked list of its contents (of type
// Decl) -- this is used for outputing a computed HTML file, and
// a current programText object (while parsing).

class HTMLfile {
   String      directory;         // directory of HTML file
   String      name;              // name of HTML file (e.g, "x.html")
   Hashtable   symbolTable;       // hash table of (method-name, programtext)
   ProgramText pt;                // current method (programtext) being built
   LinkedList  contents;          // list of (method-name, programtext) Pairs
   boolean     isExtension;       // is this file an extension or not

   public HTMLfile( String directory, String name ) {
      this.directory = directory;
      this.name = name;
      symbolTable = new Hashtable();
      pt = new ProgramText( "main", this );
      contents = new LinkedList();
   }

   public String getFileName() {
      return directory + "/" + name ;
   }

   public String toString() {
      String result = "";

      ListIterator li = contents.listIterator();
      while (li.hasNext()) {
         Decl d = ((Pair) li.next()).getDecl();
         result += d.toString();
      }
      return result;
   }
              
   // append line (string or whatever) to the end of the programtext object

   public void addLast( Object o ) {
      pt.addLast(o);
   }

   // search the symboltable for a given programtext object

   public ProgramText get( String name ) {
      return (ProgramText) symbolTable.get( name );
   }

   // insert a programtext object into the symbol table

   public void put( Object key, Object value ) {
      symbolTable.put( key, value );
      contents.add( new Pair( (String) key, (Decl) value ) );
   }

   // put (key,pt) into symbol table and contents

   public void put( String key ) {
      symbolTable.put( key, pt );
      contents.add( new Pair( (String) key, pt ));
   }

   // put Pair == (key,pt) into symbol table and contents

   public void put( Pair p ) {
      symbolTable.put( p.name, p.d );
      contents.add( p );
   }

   // write a text version of the HTML object in given file

   public String fileName( String directory, String filename ) {
      return directory + "/" + filename;
   }
 
   public void write( String directory, String filename ) {
      PrintWriter pw  = null;

      // Step 1: open file

      try {
         pw = new PrintWriter( new FileOutputStream( 
                   fileName( directory, filename ) ) );
      }
      catch (Exception e) { 
         Error.stop( "can not open/create " + fileName(directory, filename)
                     + " : " + e.getMessage() ); 
      }

      // Step 2: write contents of file -- this is a list of all elements
      //         of the contents list

      ListIterator li = contents.listIterator();
      while (li.hasNext()) {
         Decl d = ((Pair) li.next()).getDecl();
         d.write( pw );
      }

      // Step 3: flush and close the file

      pw.flush();
      pw.close();
   }

   // inline means examine each method in the base,
   // and perform an inline on each method

   public void compose( HTMLfile ext ) {
      ListIterator li = contents.listIterator();
      while (li.hasNext()) {
         Pair p = (Pair) li.next();
         p.inline(ext);
      }      
   }

   public boolean getExtension() {
      return isExtension;
   }

   public void setExtension( boolean value ) {
      isExtension = value;
   }

/* // NOT CURRENTLY USED -- KEEP FOR NOW, DELETE LATER
   // merge means copy each method in the extension
   // that doesn't exist in the base.  That is we don't
   // copy "overriding" methods in the extension

   public void merge( HTMLfile f ) {
      ListIterator li = f.contents.listIterator();
      while (li.hasNext()) {
         Pair p = (Pair) li.next();
 
         // add the Pair from the extension if it hasn't been
         // registered in the base

         if (get(p.name) == null)
            put(p);
      }
   }
*/
}
