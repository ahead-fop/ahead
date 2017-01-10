package Jakarta.util;

import Jakarta.io.DirectoryFileFilter ;

import java.io.* ;
import java.util.* ;

/** Utility that finds all occurs of a named file within a subtree.
    @author R. Cardone (1/12/98)
  */
public class FindFile
{
 // Static variables.
 protected static FilenameFilter dfilter = new DirectoryFileFilter () ;

 // Instance variables
 public boolean               debug = false;// Set object tracing on.

 // Instance variables that don't change during recursion.
 protected boolean            entries;
 protected boolean            fullpath;
 protected StringPatternMatch pattern;

 /* ******************************************************************* */
 /*                              Methods                                */
 /* ******************************************************************* */
 /* ------------------------------------------------------------------- */
 /* findEntries:                                                        */
 /* ------------------------------------------------------------------- */
 /** Returns a vector of directory names in which the specified file
     was found in the subtree.
     @param dir The top-level directory in the tree.
     @param fn The filename to be searched for.
     @return A vector of zero or more elements. */
 public Vector findDirectories(String dir, String fn)
 {return find(dir, fn, true, true);}

 /* ------------------------------------------------------------------- */
 /* findEntries:                                                        */
 /* ------------------------------------------------------------------- */
 /** Returns a vector of directory names in which the specified file
     was found in the subtree.
     @param dir The top-level directory in the tree.
     @param fn The filename to be searched for.
     @param fullpath true means return fullpath, false means return
            directory only.
     @return A vector of zero or more elements. */
 public Vector findEntries(String dir, String fn, boolean fullpath)
 {return find(dir, fn, true, fullpath);}

 /* ------------------------------------------------------------------- */
 /* findFiles:                                                          */
 /* ------------------------------------------------------------------- */
 /** Returns a vector of full pathnames for the file.
     @param dir The top-level directory in the tree.
     @param fn The filename to be searched for.
     @return A vector of zero or more elements. */
 public Vector findFiles(String dir, String fn)
 {return find(dir, fn, false, true);}

 /* ------------------------------------------------------------------- */
 /* findFiles:                                                          */
 /* ------------------------------------------------------------------- */
 /** Returns a vector of full pathnames for the file.
     @param dir The top-level directory in the tree.
     @param fn The filename to be searched for.
     @param fullpath true means return fullpath, false means return
            directory only.
     @return A vector of zero or more elements. */
 public Vector findFiles(String dir, String fn, boolean fullpath)
 {return find(dir, fn, false, fullpath);}

 /* ------------------------------------------------------------------- */
 /* find:                                                               */
 /* ------------------------------------------------------------------- */
 /** Returns a vector containing an element for each directory in which
     the specified file was found.
     @param dir The top-level directory in the tree.
     @param fn The filename to be searched for.
     @param entries True means all entries are considered (directories as
            well as regular files), false means only regular files are
            considered.
     @param fullpath Whether the file's full pathname or directory name
            only should be returned (true means full pathname is returned).
     @return A vector of zero or more elements. */
 protected Vector find(String dir, String fn, boolean entries,
                       boolean fullpath)
 {
  // Always return a vector.
  Vector v = new Vector();

  // Is there anything to do?
  if ((fn == null) || (fn.length() == 0)) return v;

  // Process directory selection.
  if ((dir == null) || (dir.length() == 0))
     dir = System.getProperty("user.dir");
   else
    {
     // Homogenize input.
     dir = dir.trim();

     // Massage directory string to allow slash or
     // backslash to be used as input if necessary.
     if (File.separator.equals("\\"))
        dir = dir.replace('/', File.separatorChar);
     else if (File.separator.equals("/"))
        dir = dir.replace('\\', File.separatorChar);
    }

  // Work around for File class' strange behavior when
  // given just a drive designation under Windows.
  dir = driveFixUp(dir);

  // Perform a recursive search into the subtree.
  this.entries  = entries;
  this.fullpath = fullpath;
  pattern       = new StringPatternMatch(fn);
  dfs(dir, v);
  return v;
 }

 /* ------------------------------------------------------------------- */
 /* dfs:                                                                */
 /* ------------------------------------------------------------------- */
 /** Perform a depth-first search for directories containing the
     specified file.  The instance variables pattern, entries, and
     fullpath are used and remain unchanged throughout all recursions.
     @param dir The top-level directory in the tree.
     @param v The input/output vector. */
 protected void dfs(String dir, Vector v)
 {
  // Tracing
  if (debug) System.out.println("dfs called.");

  // Create a file object for the specfied directory.
  File d;
  try {d = new File(dir);}
  catch (Exception e){e.printStackTrace(); return;}

  // Make sure we have a directory.
  try {
    if (!d.exists())
      {
       System.out.println(dir + " not found.");
       return;
      }
    if (!d.isDirectory())
      {
       System.out.println(dir + " is not a directory.");
       return;
      }
   }
  catch (Exception e)
   {e.printStackTrace();
    return;}

  // ------------------ Search for File ---------------------
  // Get all pattern matches.
  String[] list = d.list(pattern);
  if (list == null) list = new String[0];

  // Make sure each matching string is a normal file
  // before adding it to the output vector.
  for (int i = 0; i < list.length; i++)
   {
    // Tracing.
    if (debug)
       System.out.println(" -> Pattern match" + i + ": " + list[i]);

    // Add to vector according to caller's preferences.
    if (entries)
      {
       // Add files or directories entries truncated if necessary.
       if (fullpath) v.addElement(getPathname(dir, list[i]));
        else
         {
          v.addElement(dir);
          break;    // Only need to report directory once.
         }
      }
     else
      {
       // Add only files truncated if necessary.
       File f = new File(dir, list[i]);
       if (f.isFile())
         {
          if (fullpath) v.addElement(getPathname(dir, list[i]));
            else
            {
             v.addElement(dir);
             break;    // Only need to report directory once.
            }
         }
      }
   }

  // -------------- Search for Subdirectories ---------------
  // Get the list of all subdirectories.
  list = null;
  list = d.list(dfilter);
  if (list == null) list = new String[0];

  // Search each subdirectory.
  for (int i = 0; i < list.length; i++)
   {
    // Tracing
    if (debug) System.out.println(
               " -> Dir match" + i +": " + getPathname(dir,list[i]));
    dfs(getPathname(dir, list[i]), v);
   }

  return;
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
  String fn  = "";
  String dir = "";
  boolean ientries  = true;
  boolean ifullpath = true;

  // Get directory specification from command line.
  if (args.length > 0) fn  = args[0];
  if (args.length > 1) dir = args[1];
  if (args.length > 2)
     if (args[2].equals("false")) ientries  = false;
  if (args.length > 3)
     if (args[3].equals("false")) ifullpath = false;

  // Make sure we have something to do.
  if (fn.length() == 0)
    {
     System.out.println("The filename search string parameter is required.");
     System.out.println("Parameters are ordered as follows:\n");
     System.out.println("  filename  - file name search string.");
     System.out.println(
      "  directory - directory to start search from (default=current).");
     System.out.println(
      "  entries   - true means include all entries in search (default),\n" +
      "              false means include only regular files in search.");
     System.out.println(
  "  fullpath  - true means return the path name of each match (default),\n"+
      "              false means return only the entry's directory.");
     return;
    }

  // Create finder object and execute.
  FindFile f = new FindFile();
  Vector v;
  if (ientries) v = f.findEntries(dir, fn, ifullpath);
   else v = f.findFiles(dir, fn, ifullpath);

  // Print results;
  String s;
  int len = v.size();
  for (int i = 0; i < len; i++)
   {
    try {System.out.println(v.elementAt(i));}
    catch (Exception e){e.printStackTrace();}
   }

  return;
 }
}
