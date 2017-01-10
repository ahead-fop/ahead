// Package and name scoping
package Jakarta.util;
import java.io.*;
import java.util.*;

/** Utility that removes files and directories.  The following entry
    points provide the main functionality:
    <ul>
    <li>removeFile - remove all matching files.
    <li>removeDir  - remove all matching directories and their contents.
    <li>removeRecursive - removeFile and removeDir.
    </ul>
    @author R. Cardone (1/12/98)
  */
public class Rm
{
 // Constants
 protected static final char RMFILE      = 'f';
 protected static final char RMDIR       = 'd';
 protected static final char RMRECURSIVE = 'r';

 // Instance variables
 public boolean               debug    = false; // Set object tracing on.
 public boolean               echo     = false; // Echo selected entries.
 public boolean               del      = true;  // Delete entries or not.

 /* ******************************************************************* */
 /*                              Methods                                */
 /* ******************************************************************* */
 /* ------------------------------------------------------------------- */
 /* removeFile:                                                         */
 /* ------------------------------------------------------------------- */
 /** Remove files that match the file specification.
     @param fn The file specification.
     @return The number of files/directories deleted. */
 public int removeFile(String fn)
 {return remove(RMFILE, fn);}

 /* ------------------------------------------------------------------- */
 /* removeDir:                                                          */
 /* ------------------------------------------------------------------- */
 /** Remove directories that match the file specification.
     @param fn The file specification.
     @return The number of files/directories deleted. */
 public int removeDir(String fn)
 {return remove(RMDIR, fn);}

 /* ------------------------------------------------------------------- */
 /* removeRecursive:                                                    */
 /* ------------------------------------------------------------------- */
 /** Remove files and directories that match the file specification.
     @param fn The file specification.
     @return The number of files/directories deleted. */
 public int removeRecursive(String fn)
 {return remove(RMRECURSIVE, fn);}

 /* ------------------------------------------------------------------- */
 /* remove:                                                             */
 /* ------------------------------------------------------------------- */
 /** Preprocess file specification then call the actual deletion methods.
     @param cmd The command directive.
     @param spec The file/directory specification
     @return The number of files/directories deleted. */
 protected int remove(char cmd, String spec)
 {
  // Initialize result.
  int result = 0;

  // Is there anything to do?
  if ((spec == null) || (spec.length() == 0)) return result;

  // Set processing flags.
  boolean processFile = (cmd == RMFILE) || (cmd == RMRECURSIVE);
  boolean processDir  = (cmd == RMDIR)  || (cmd == RMRECURSIVE);

  // Homogenize input.
  spec = spec.trim();

  // Massage directory string to allow slash or
  // backslash to be used as input if necessary.
  if (File.separator.equals("\\"))
     spec = spec.replace('/', File.separatorChar);
  else if (File.separator.equals("/"))
     spec = spec.replace('\\', File.separatorChar);

  // Work around for File class' strange behavior when
  // given just a drive designation under Windows.
  spec = driveFixUp(spec);

  // Get a file representation of the specification.
  if (debug) System.out.println(" Rm: spec=" + spec);
  File fspec = new File(spec);

  // Tracing.
  if (debug)
    {
     System.out.println(" Absolute path=" + fspec.getAbsolutePath());
     System.out.println(" Path=" + fspec.getPath());
     System.out.println(" Name=" + fspec.getName());
     System.out.println(" Parent=" + fspec.getParent());
     System.out.println(" isDirectory=" + fspec.isDirectory());
     System.out.println(" isFile=" + fspec.isFile());
     System.out.println(" exists=" + fspec.exists());
    }

  // Fixed specs that exist can be handled without delay.
  if (fspec.exists())
    {
     // One or both processing options may be in effect.
     if (processFile && fspec.isFile())
        result += rmEntry(fspec);
     else if (processDir && fspec.isDirectory())
        result += rmDir(fspec);
     return result;
    }

  // Split absolute path up.  Use our own code
  // to avoid the flakiness of the File class.
  String pathPrefix = getPathPrefix(fspec.getAbsolutePath());
  String pathSuffix = getPathSuffix(fspec.getAbsolutePath());

  // More tracing.
  if (debug)
    {
     System.out.println(" pathPrefix=" + pathPrefix);
     System.out.println(" pathSuffix=" + pathSuffix);
    }

  // Wildcards are not allowed in path prefix.
  if ((pathPrefix.indexOf('*') != -1) || (pathPrefix.indexOf('?') != -1))
    {
     System.out.println(
        "Rm Error: Wildcards cannot appear in directory prefix.");
     return result;
    }

  // Fixed specs that don't exist require no work.
  if ((pathSuffix.indexOf('*') == -1) && (pathSuffix.indexOf('?') == -1))
     return result;

  // Find all directory entries which match the suffix.
  File fprefix = new File(pathPrefix);
  StringPatternMatch pattern = new StringPatternMatch(pathSuffix);
  String[] list = fprefix.list(pattern);
  if (list == null) list = new String[0];

  // Process each file and/or directory entry in list.
  File fe = null;
  for (int i = 0; i < list.length; i++, fe = null)
   {
    // Get a file representation of this entry.
    fe = new File(getPathname(pathPrefix, list[i]));

    // Perform the deletion based on directive and entry type.
    if (processFile && fe.isFile()) result += rmEntry(fe);
    else if (processDir && fe.isDirectory()) result += rmDir(fe);
   }

  // Return cumulative result.
  return result;
 }

 /* ------------------------------------------------------------------- */
 /* rmEntry:                                                            */
 /* ------------------------------------------------------------------- */
 /** The only place a entry (file or directory) actually gets removed.
     Processing flags echo and del are used here.
     @param entry A file or directory represented as a file object.
     @return Deleted file/directory count.  */
 protected int rmEntry(File entry)
 {
  // Print and/or delete entries.
  int result = 0;
  if (echo) System.out.println(entry.getAbsolutePath());
  if (del) {if (entry.delete()) result = 1;}
  return result;
 }

 /* ------------------------------------------------------------------- */
 /* rmDir:                                                              */
 /* ------------------------------------------------------------------- */
 /** Remove a directory and all its contents.
     @param spec A directory represented as a file object.
     @return Deleted file/directory count.  */
 protected int rmDir(File dir)
 {
  // Tracing
  if (debug)
     System.out.println(" rmDir called for " + dir.getAbsolutePath());

  // Deletion count.
  int result = 0;

  // Get the path prefix.
  String pathPrefix = dir.getAbsolutePath();

  // Get the list of entries in this directory.
  String[] list = dir.list();
  if (list == null) list = new String[0];

  // Process each file and/or directory entry in list.
  File fe = null;
  for (int i = 0; i < list.length; i++, fe = null)
   {
    // Get a file representation of this entry.
    fe = new File(getPathname(pathPrefix, list[i]));

    // Perform the deletion based on directive and entry type.
    if (fe.isFile()) result += rmEntry(fe);
    else if (fe.isDirectory()) result += rmDir(fe);
   }

  // Once empty, delete the directory.
  result += rmEntry(dir);

  return result;
 }

 /* ------------------------------------------------------------------- */
 /* getPathPrefix:                                                      */
 /* ------------------------------------------------------------------- */
 /** Return all characters up to, and sometimes including, the last file
     separator.
     @param path The source path.
     @return The prefix string. */
 protected String getPathPrefix(String path)
 {
  // Locate last file separator and return preceding string.
  int last = path.lastIndexOf(File.separatorChar);
  if ((last == -1) || (last == path.length() - 1)) return path;
   else return path.substring(0, last);
 }

 /* ------------------------------------------------------------------- */
 /* getPathSuffix:                                                      */
 /* ------------------------------------------------------------------- */
 /** Return all, if any, characters after the last file separator.
     @param path The source path.
     @return The suffix string. */
 protected String getPathSuffix(String path)
 {
  // Locate last file separator and return string following it.
  int last = path.lastIndexOf(File.separatorChar);
  if ((last == -1) || (last == path.length() - 1)) return "";
   else return path.substring(last + 1);
 }

 /* ------------------------------------------------------------------- */
 /* getPathname:                                                        */
 /* ------------------------------------------------------------------- */
 /** Parse together a directory and filename string.
     @param dir Directory string.
     @param fn  Filename string.
     @return Pathname string. */
 protected String getPathname(String dir, String fn)
 {
  // See if there's a path to append.
  if (dir.length() == 0) return fn;

  // Parse together file's pathname depending
  // on file separator character.
  if (dir.endsWith(File.separator)) return dir + fn;
   else return dir + File.separator + fn;
 }

 /* ------------------------------------------------------------------- */
 /* driveFixUp:                                                         */
 /* ------------------------------------------------------------------- */
 /** Work around a bug in File.list that causes prevents any entries
     from being returned when just a drive designation or root directory
     of a drive is searched.  Bogus, bogus, bogus.
     @param dir The specified directory.
     @return The fixed up directory. */
 protected String driveFixUp(String dir)
 {
  // Use heuristics to identify problem case.
  int len = dir.length();
  if (len == 2)
    {
     // Drive only.
     if ((':' == dir.charAt(1)) && Character.isLetter(dir.charAt(0)) &&
         File.separator.equals("\\"))
        dir = dir + "\\..";
    }
  else if (len == 3)
    {
     // Root directory (this assumes file separator homogenation).
     if ((':' == dir.charAt(1)) && Character.isLetter(dir.charAt(0)) &&
         ('\\' == dir.charAt(2)))
        dir = dir + "..";
    }

  return dir;
 }

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
     System.out.println("A filename string is required.  " +
        "It may contain wildcard and directory separation characters.");
     return;
    }

  // Create remover object and execute.
  Rm r = new Rm();
  System.out.println(r.removeFile(fn) + " file(s) deleted.");
  return;
 }
}
