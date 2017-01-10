package Copy;

import java.util.*;

extends class HTMLfile {
   LinkedList files2copy = new LinkedList();

   public void addFile2Copy( String directory, String filename ) {
      files2copy.add( new FileName(directory,filename) );
   }

   public void write( String dir, String filename ) {

      // Step 1: write out everything as before

      Base( String, String ).write(dir, filename);

      // Step 2: foreach file to copy, copy it

      ListIterator li = files2copy.listIterator();
      while (li.hasNext()) {
         FileName fn = (FileName) li.next();
         CopyFile.move( fn.directory + "/" + fn.filename, 
                        dir + "/" + fn.filename);
      }
   }
}

