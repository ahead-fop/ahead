// Package and name scoping
package Jakarta.util;
import java.io.*;
import java.util.*;

/** Utility that deletes the specified directory and its subtree.
    @author R. Cardone (1/12/98)
  */
public class Rmdir
{
 /* ******************************************************************* */
 /*                         Application Driver                          */
 /* ******************************************************************* */
 /* ------------------------------------------------------------------- */
 /* main:                                                               */
 /* ------------------------------------------------------------------- */
 /** Driver stub.
     @param fn File name.
     @param dir Top-level directory.
     @param fullpath designator (any character string). */
 public static void main(String[] args)
 {
  // Locals
  String fn = "";

  // Get directory specification from command line.
  if (args.length > 0) fn  = args[0];

  // Make sure we have something to do.
  if (fn.length() == 0)
    {
     System.out.println("A directory name string is required.  " +
        "It may contain wildcard and directory separation characters.");
     return;
    }

  // Create remover object and execute.
  Rm r = new Rm();
  System.out.println(r.removeDir(fn) + " file(s) deleted.");
  return;
 }
}
