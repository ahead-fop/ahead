package Jakarta.util;

import java.io.*;
import java.util.*;

/** Directory clean facility.  Use this application by entering:
    <p>
                   java Clean <dir>
    <p>
    where dir is an optional directory specification; if not present,
    the current directory is assumed.
    <p>
    Clean looks for the file "cleanfile" in the specified directory and
    interprets the directives in that file to determine which files or
    directories should be deleted.  See the cleanlist file in
    Jakarta/util for an explanation of the directive language.
    @author R. Cardone (1/8/98)
  */
public class Clean
{
 // Trace control
 public final boolean debug = false;

 // Constants
 public static final String cleanFile         = "cleanlist";
 public static final String cmdRmFileTok      = "rmfile(";
 public static final String cmdRmRecursiveTok = "rmrecursive(";
 public static final String idRmFile          = "rmfile";
 public static final String idRmFileSuffix    = "rmsuffix";
 public static final String idRmRecursive     = "rmrecursive";
 public static final String metaChars         = "*?";
 public static final String wsFilter          = " \n\r\t\b\f";
 public static final char   commentChar       = '#';

 /* ******************************************************************* */
 /*                              Methods                                */
 /* ******************************************************************* */
 /* ------------------------------------------------------------------- */
 /* remove:                                                             */
 /* ------------------------------------------------------------------- */
 /** Perform the file/directory cleaning.  This method also catches
     and handles all exceptions thrown by lower level routines.
     @param dir The target directory or the current directory if a zero
                length string or a null.
     @return The number of files and directories deleted. */
 public int remove(String dir)
 {
  // Initialize count variable.
  int result = 0;

  // Process directory selection.
  if ((dir == null) || (dir.length() == 0))
     dir = "";
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

  // Get specification string vector.
  Vector specs = null;
  try {specs = getSpecVector(dir);}
  catch (Exception e)
   {
    // Abort if we can't get the input.
    if (!(e instanceof FileNotFoundException))
      {
       // Report exception.
       e.printStackTrace();
       System.out.println("CLEAN ABORTING - NO FILES REMOVED.");
      }
    return result;
   }

  // Tracing.
  if (debug) System.out.println(specs);

  // Process each specification object.
  CleanFileSpec spec;
  Enumeration en = specs.elements();
  while(en.hasMoreElements())
   {
    // Process each object in turn.
    // Note use of equal operator with static strings.
    spec = (CleanFileSpec) en.nextElement();
    if (spec.subcommand == idRmFile)
       result += processRmFile(spec);
    else if (spec.subcommand == idRmFileSuffix)
       result += processRmFileSuffix(spec);
    else if (spec.subcommand == idRmRecursive)
       result += processRmRecursive(spec);
   }

  return result;
 }

 /* ------------------------------------------------------------------- */
 /* processRmFile:                                                      */
 /* ------------------------------------------------------------------- */
 /** Issue system call for rmfile subcommand.
     @param spec The file specification object.
     @return The number of files and directories deleted. */
 protected int processRmFile(CleanFileSpec spec)
 {
  // Call the remove file method with the filter specification.
  return new Rm().removeFile(spec.filter);
 }

 /* ------------------------------------------------------------------- */
 /* processRmFileSuffix:                                                */
 /* ------------------------------------------------------------------- */
 /** Issue system call for rmfile subcommand with suffix replacement.
     @param spec The file specification object.
     @return The number of files and directories deleted. */
 protected int processRmFileSuffix(CleanFileSpec spec)
 {
  // Initialize count variable.
  int result = 0;

  // Create file object for specification.
  File specFile;
  try {specFile = new File(spec.filter);}
  catch (Exception e)
   {
    System.out.println(
     "CLEAN ERROR: Unable to access " + spec.filter + '.');
    return result;
   }

  // Segregate directory and filename in a portable way.
  String fn  = specFile.getName();
  String dir = specFile.getAbsolutePath();
  dir        = dir.substring(0, dir.length() - fn.length());

  // Create file object for the directory.
  File dirFile;
  try {
    // Verify specified directory.
    dirFile = new File(dir);
    if (!dirFile.isDirectory())
       throw new Exception("Not a directory.");
   }
  catch (Exception e)
   {
    System.out.println(
     "CLEAN ERROR: Unable to access directory " + dir + '.');
    return result;
   }

  // Filter the list of files in the target directory.
  String[] files = dirFile.list(new StringPatternMatch(fn));

  // Process each file.
  if (files != null)
    {
     String s;
     Rm     r = new Rm();
     for (int i = 0; i < files.length; i++)
      {
       // Append file to directory then replace suffix.
       s = dir + files[i];
       s = s.substring(0, s.lastIndexOf('.')) +
           (String) spec.subcommandParms;

       // Call the remove file method with the new filter specification.
       result += r.removeFile(s);
      }
    }

  return result;
 }

 /* ------------------------------------------------------------------- */
 /* processRmRecursive:                                                 */
 /* ------------------------------------------------------------------- */
 /** Issue system call for rmrecursive subcommand.
     @param spec The file specification object.
     @return The number of files and directories deleted. */
 protected int processRmRecursive(CleanFileSpec spec)
 {
  // Call the remove recursive method with the filter specification.
  return new Rm().removeRecursive(spec.filter);
 }

 /* ------------------------------------------------------------------- */
 /* getSpecVector:                                                      */
 /* ------------------------------------------------------------------- */
 /** Perform the file/directory cleaning.
     @param dir The target directory or the current directory if a zero
                length string.
     @return Vector of specification objects. */
 protected Vector getSpecVector(String dir) throws Exception
 {
  // Locals
  String specString = null;
  Vector specs      = new Vector();

  // Parse together file's pathname.
  String fname = getPathname(dir, cleanFile);

  // Read in the specifications.
  specString = readSpecFile(fname);
  //System.out.println(specString);

  // Each iteration of the while loop will discover another
  // complete file specification object.
  StringTokenizer specTok = new StringTokenizer(specString, wsFilter);
  String tok, residue;
  while (specTok.hasMoreTokens())
   {
    // Get next token.
    tok = specTok.nextToken(wsFilter);

    // Process valid subcommands only.
    if (tok.startsWith(cmdRmFileTok))
      {
       // Get a vector of file specs.
       Vector v = parseRmFile(specTok, dir, getResidue(cmdRmFileTok,tok));

       // Add one or more file specs to the result vector.
       Enumeration en = v.elements();
       while(en.hasMoreElements()) specs.addElement(en.nextElement());
      }
    else if (tok.startsWith(cmdRmRecursiveTok))
      {
       // Get a vector of file specs.
       Vector v = parseRmRecursive(specTok, dir,
                    getResidue(cmdRmRecursiveTok,tok));

       // Add one or more file specs to the result vector.
       Enumeration en = v.elements();
       while(en.hasMoreElements()) specs.addElement(en.nextElement());
      }
    else
     {
      // Bogus input means no processing.
	 String msg = "CLEAN ERROR: token\"" + tok + "\" in " + fname ;
      throw new Exception(msg);
     }
   }

  return specs;
 }

 /* ------------------------------------------------------------------- */
 /* parseRmFile:                                                        */
 /* ------------------------------------------------------------------- */
 /** Subcommand processor for remove file.
     @param specTok The current tokenized input string.
     @param dir Target directory.
     @param residue The residue from the current token.
     @return A vector of file specification objects. */
 protected Vector parseRmFile(StringTokenizer specTok, String dir,
  String residue)
   throws Exception
 {
  // Specification string.
  String s;

  // The residue string may or may not contain the closing
  // parenthesis.  If it does, that's that all we need.  If
  // not, get all tokens up to the next closing parenthesis.
  if ((residue.length() > 0) && residue.endsWith(")"))
     s = residue.substring(0, residue.length()-1);
   else s = residue + ' ' + specTok.nextToken(")");

  // Make sure we have something to work with.
  if (s.length() == 0)
     throw new Exception("CLEAN ERROR: Invalid rmfile subcommand.");

  // Validate the spec string by making
  // sure that commands are not nested.
  if (s.indexOf('(') != -1)
     throw new Exception(
       "CLEAN ERROR: Nested subcommands (unpaired parenthesis).");

  // Create a vector to hold all specification strings.
  // First remove all suffix specifications from current
  // string and put them in the vector.
  Vector v = new Vector();
  s = getSuffixFileVector(s, dir, v);

  // Create a spec object for each token in the remainder of the string.
  if (s.length() > 0)
   {
    StringTokenizer stok = new StringTokenizer(s, wsFilter);
    String cur;
    while (stok.hasMoreTokens())
     {
      // Get next token.
      cur = stok.nextToken(wsFilter);

      // Create, initialize and add file spec to vector.
      CleanFileSpec spec = new CleanFileSpec();
      spec.filter = getPathname(dir, cur);
      spec.subcommand = idRmFile;
      v.addElement(spec);
     }
   }

  return v;
 }

 /* ------------------------------------------------------------------- */
 /* parseRmRecursive:                                                   */
 /* ------------------------------------------------------------------- */
 /** Subcommand processor for remove subdirectory.
     @param specTok The current tokenized input string.
     @param dir Target directory.
     @param residue The residue from the current token.
     @return A vector of file specification objects. */
 protected Vector parseRmRecursive(StringTokenizer specTok,
  String dir, String residue)
   throws Exception
 {
  // Specification string.
  String s;

  // The residue string may or may not contain the closing
  // parenthesis.  If it does, that's that all we need.  If
  // not, get all tokens up to the next closing parenthesis.
  if ((residue.length() > 0) && residue.endsWith(")"))
     s = residue.substring(0, residue.length()-1);
   else s = residue + ' ' + specTok.nextToken(")");

  // Make sure we have something to work with.
  if (s.length() == 0)
     throw new Exception("CLEAN ERROR: Invalid rmrecursive subcommand.");

  // Validate the spec string by making
  // sure that commands are not nested.
  if (s.indexOf('(') != -1)
     throw new Exception(
       "CLEAN ERROR: Nested subcommands (unpaired parenthesis).");

  // Create output vector.
  Vector v = new Vector();

  // Create a spec object for each token in the remainder of the string.
  StringTokenizer stok = new StringTokenizer(s, wsFilter);
  String cur;
  while (stok.hasMoreTokens())
   {
    // Get next token.
    cur = stok.nextToken(wsFilter);

    // Create, initialize and add file spec to vector.
    CleanFileSpec spec = new CleanFileSpec();
    spec.filter = getPathname(dir, cur);
    spec.subcommand = idRmRecursive;
    v.addElement(spec);
   }

  return v;
 }

 /* ------------------------------------------------------------------- */
 /* getSuffixFileVector:                                                */
 /* ------------------------------------------------------------------- */
 /** Subcommand processor for remove file.
     @param s Source string.
     @param dir Target directory.
     @param vsuffix Output vector of suffix file spec objects.
     @return The reduced source string. */
 protected String getSuffixFileVector(String s, String dir, Vector v)
   throws Exception
 {
  // Index variables.
  int eindex = s.indexOf('=');
  int pindex, findex, i;

  // Remove all suffix specifications from current source string.
  while (eindex != -1)
   {
    // We need the word before and the word after
    // the equals sign.  There may be intervening
    // spaces; if the words are not found there's
    // an error.

    // --------------- Preceding Word -----------------
    // Get preceding word's last character index.
    for (pindex = eindex - 1; pindex >= 0; pindex--)
     if (!Character.isWhitespace(s.charAt(pindex))) break;

    // We're we successful?
    if (pindex < 0)
       throw new Exception("CLEAN ERROR: Invalid suffix lvalue.");

    // Get preceding word's first character index.
    for (; pindex >= 0; pindex--)
     if (Character.isWhitespace(s.charAt(pindex))) break;
    pindex++;

    // Isolate the preceding word in a string.
    String pstring = s.substring(pindex, eindex).trim();

    // --------------- Following Word -----------------
    // Get following word's first character index.
    for (findex = eindex + 1; findex < s.length(); findex++)
     if (!Character.isWhitespace(s.charAt(findex))) break;

    // We're we successful?
    if (findex >= s.length())
       throw new Exception("CLEAN ERROR: Invalid suffix rvalue.");

    // Get following word's last character index.
    for (; findex < s.length(); findex++)
     if (Character.isWhitespace(s.charAt(findex))) break;
    findex--;

    // Isolate the following word in a string.
    String fstring = s.substring(eindex+1, findex+1).trim();
    //System.out.println("--> " + pstring + '=' + fstring);

    // --------------- Verification -------------------
    // Preceding word must end in a dot followed by alphanumerics.
    i = pstring.lastIndexOf('.');
    if (i == -1)
       throw new Exception("CLEAN ERROR: No dot in suffix lvalue.");
    for (int j = i; j < pstring.length(); j++)
      if (metaChars.indexOf(pstring.charAt(j)) != -1)
         throw new Exception(
           "CLEAN ERROR: Metacharacters in lvalue suffix.");

    // Following word must begin with a dot followed by alphanumerics.
    if ('.' != fstring.charAt(0))
       throw new Exception("CLEAN ERROR: No leading dot in suffix rvalue.");
    for (i = 1; i < fstring.length(); i++)
      if (metaChars.indexOf(fstring.charAt(i)) != -1)
         throw new Exception(
           "CLEAN ERROR: Metacharacters in rvalue suffix.");

    // --------------- Remove Expression --------------
    // Create spec object and add to vector.
    CleanFileSpec spec = new CleanFileSpec();
    spec.filter = getPathname(dir, pstring);
    spec.subcommand = idRmFileSuffix;
    spec.subcommandParms = fstring;
    v.addElement(spec);

    // Remove suffix expression from string.
    s = s.substring(0, pindex) + s.substring(findex+1);

    // Find the next suffix expression.
    eindex = s.indexOf('=');
   }

  // Return the reduced and trimmed source string.
  return s.trim();
 }

 /* ------------------------------------------------------------------- */
 /* readSpecFile:                                                       */
 /* ------------------------------------------------------------------- */
 /** Read the specification file into a character arrray.
     @param fname The target file.
     @return File contents as a string. */
 protected String readSpecFile(String fname) throws Exception
 {
  // Locals
  int        flen = 0;
  FileReader fr = null;

  // Open the specification file.
  try {
    fr = new FileReader(fname);
   }
  catch (FileNotFoundException e) {
    System.out.println("CLEAN ERROR: " + fname + " not found.");
    throw e;
   }

  // Get the file length.
  File f = new File(fname);
  flen   = (int) f.length();

  // Read the whole file.
  char[] carray = new char[flen];
  int bytesRead, totalBytes;
  for (bytesRead = totalBytes = 0;
       totalBytes < flen;
       totalBytes += bytesRead)
   {
    bytesRead = fr.read(carray, totalBytes, flen-totalBytes);
    if (bytesRead < 1) throw new IOException("Overran file!");
   }

  // Close the file and ignore any errors.
  try {fr.close();} catch (IOException e){}

  // Make all comments into whitespace while still in an array.
  int linebegin, curindex;
  for (curindex = 0; curindex < carray.length; curindex++)
   {
    // Advance to first non-whitespace character in line.
    for (; curindex < carray.length; curindex++)
      if ((carray[curindex] == '\n') ||
          !Character.isWhitespace(carray[curindex]))
         break;

    // Four things could have happened.  We could be at EOL, EOF,
    // the comment character or some other non-whitespace character.
    // We iterate immediately in the first two cases and perform
    // more processing on this line in the latter two cases.
    if ((curindex < carray.length) &&
        (!Character.isWhitespace(carray[curindex])))
       linebegin = curindex;
     else continue;

    // We found the line's beginning, we
    // now have to advance to end of line.
    for (; curindex < carray.length; curindex++)
      if (carray[curindex] == '\n') break;

    // EOF means we must back up one char.
    if (curindex == carray.length) curindex--;

    // Only comment lines require more processing.
    if (carray[linebegin] != commentChar) continue;

    // Overwrite all non-whitespace on the line,
    // include any newline character, with spaces.
    for (; linebegin <= curindex; linebegin++)
     carray[linebegin] = ' ';
   }

  // Homogenize all whitespace into blanks.
  for (curindex = 0; curindex < carray.length; curindex++)
    if (Character.isISOControl(carray[curindex]))
       carray[curindex] = ' ';

  // Convert file separator characters.
  String s = (new String(carray)).trim();
  if (File.separator.equals("\\"))
     s = s.replace('/', File.separatorChar);
  else if (File.separator.equals("/"))
     s = s.replace('\\', File.separatorChar);

  // Return trimmed string.
  return s;
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
 /* getResidue                                                          */
 /* ------------------------------------------------------------------- */
 /** Return a substring of a command identifier toker.
     @param cmdid Command id string.
     @param tok Current token.
     @return Residual string. */
 protected String getResidue(String cmdid, String tok)
 {
  // Get residue stuck on end of token.
  if (tok.length() > cmdid.length())
     return tok.substring(cmdid.length());
   else return "";
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
  Clean c = new Clean();
  System.out.println(c.remove(dir) + " file(s) deleted.");
 }
}
