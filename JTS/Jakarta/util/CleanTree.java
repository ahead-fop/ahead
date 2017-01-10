// Package and name scoping
package Jakarta.util;
import java.io.*;
import java.util.*;

/** Subtree cleaner part of the clean facility.  Use this application by entering:
    <p>
                   java CleanTree <dir>
    <p>
    where dir is an optional directory specification; if not present, the current
    directory is assumed.
    <p>
    CleanTree looks for the file "cleanfile" in the specified directory and all its
    subdirectories.  It then calls Clean.remove() for each directory in which a
    cleanfile was found.
    @author R. Cardone (1/12/98)
  */
public class CleanTree
{
 /* ******************************************************************* */
 /*                              Methods                                */
 /* ******************************************************************* */
 /* ------------------------------------------------------------------- */
 /* remove:                                                             */
 /* ------------------------------------------------------------------- */
 /** Perform the file/directory cleaning in all directories with a
     cleanlist file.  This method also catches and handles all exceptions
     thrown by lower level routines.
     @param dir The target directory or the current directory if a zero
                length string or a null. 
     @return the number of files and directories deleted. */
 public int remove(String dir)
 {
  // Initialize count.
  int results = 0;

  // Get vector of directories with cleanfiles.
  Vector dirs = new FindFile().findFiles(dir, Clean.cleanFile, false);

  // Call Clean for each directory.
  int dirNum = dirs.size();
  if (dirNum > 0)
    {
     String curdir;
     Clean  clean = new Clean();
     for (int i = 0; i < dirNum; i++)
      {
       // Clean the next directory.
       try {
         curdir = (String) dirs.elementAt(i);
         System.out.println("Cleaning " + curdir);
         results += clean.remove(curdir);
        }
       catch (Exception e){e.printStackTrace();}
      }
    }
   else System.out.println(
     "No cleanlist files found in " + dir + " subtree.");

  return results;
 }

 /* ******************************************************************* */
 /*                         Application Driver                          */
 /* ******************************************************************* */
 /* ------------------------------------------------------------------- */
 /* main:                                                               */
 /* ------------------------------------------------------------------- */
 /** Driver stub.
     @param arg Optional first parameter can be the designated target
                directory. */
 public static void main(String[] args)
 {
  // Locals
  String dir = "";

  // Get directory specification from command line.
  if (args.length > 0) dir = args[0];

  // Create clean object and execute.
  CleanTree c = new CleanTree();
  System.out.println(c.remove(dir) + " file(s) removed.");
 }
}
