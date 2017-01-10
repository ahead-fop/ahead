// Package and name scoping
package Jakarta.util;
import java.io.*;
import java.util.*;

/** Pattern-matching class handling asterisk and question-mark wildcards.
    This class can be used in two ways.  The simplist way is to call the static
    version of the accept method that takes a pattern and a text string.
    <p>
    If the same pattern is going to be used to filter numerous
    text strings, creating an object using the constructor that takes the
    pattern string allows pre-processing to occur.  The non-static
    accept method that takes only a text string parameter can then be called.
    This accept method uses heuristics to match the string without recursion
    if possible.  This approach is recommended for filtering all the files
    in a directory according to a single pattern.  Note that this usage was
    not made thread-safe for performance reasons; the caller will have to
    synchronize if accept is called on the same object by multiple threads
    concurrently.
    <p>
    Many thanks to Yannis Smaragdakis for suggesting the recursive approach
    to this pattern matching problem.
    @author R. Cardone  (1/8/98)
  */
public final class StringPatternMatch
 implements FilenameFilter
{
 // Control tracing.
 private boolean debug              = false;// trace on or off
 private static boolean staticDebug = false;// trace on/off in static methods

 // Handle comparisons like the native OS.
 protected static final boolean osCaseSensitive = getOSCaseSensitivity();

 // Instance variables used in pattern preprocessing execution.
 protected String               p;            // pattern w/wildcards
 protected int                  plen;         // pattern string length
 protected int                  minlen;       // min length for match
 protected boolean              fixedLength;  // no "*" in pattern
 protected boolean              fixedPrefix;  // non-* first char
 protected boolean              fixedSuffix;  // non-* last char
 protected int                  endPrefix;    // last prefix index
 protected int                  startSuffix;  // first suffix index
 protected int                  pNoSuffixLen; // pattern len w/o suffix
 protected boolean              caseSensitive;// invocation sensitivity

 // Variables set by accept method and seen by all recursions (preproc exe).
 protected int                  sNoSuffixLen; // string length w/o suffix

 /* ******************************************************************* */
 /*                     Pattern Matching Constructors                   */
 /* ******************************************************************* */
 /* ------------------------------------------------------------------- */
 /* constructor:                                                        */
 /* ------------------------------------------------------------------- */
 /** Construct an object that does not use a preprocessed pattern.  Only
     the static methods will perform any meaningful processing.
 public StringPatternMatch(){p="";}

 /* ------------------------------------------------------------------- */
 /* constructor:                                                        */
 /* ------------------------------------------------------------------- */
 /** Construct object and assign filename template string and default
     OS case sensitivity policy for matching file names for use in all
     string comparisons.
     @param pattern The filename template string cannot be null. */
 public StringPatternMatch(String pattern)
 {this(pattern, osCaseSensitive);}

 /* ------------------------------------------------------------------- */
 /* constructor:                                                        */
 /* ------------------------------------------------------------------- */
 /** Construct object and assign filename template string.  Perform all
     preprocessing possible without knowing target string.  Use the
     specified case sensitivity policy.
     @param pattern The filename template string cannot be null.
     @param sensitive true means perform case sensitive comparisons. */
 public StringPatternMatch(String pattern, boolean sensitive)
 {
  // Assign case sensitivity.
  caseSensitive = sensitive;

  // Assign pattern string.
  if (caseSensitive) p = pattern;
   else p = pattern.toUpperCase();
  plen = p.length();
  if (plen < 1) return;

  // Discover fixed position characteristics.
  int firstStar = p.indexOf('*');
  if (firstStar == -1)
    {
     // Easy case
     fixedLength = fixedPrefix = fixedSuffix = true;
     minlen = plen;
    }
   else
    {
     // Assign prefix flag and end index.
     if (firstStar != 0)
       {
        fixedPrefix = true;
        endPrefix   = firstStar - 1;
       }

     // Assign suffix flag, start index
     // and pattern w/o fixed suffix.
     int lastStar = p.lastIndexOf('*');
     if (lastStar != (plen - 1))
       {
        fixedSuffix = true;
        startSuffix = lastStar + 1;
        pNoSuffixLen= startSuffix;
       }
      else pNoSuffixLen = plen;

     // Assign minimum length of any matching string
     // by counting up all non "*" characters.
     minlen = firstStar;
     for (int j = firstStar+1; j < plen; j++)
       if (p.charAt(j) != '*') minlen++;
    }

  // Tracing.
  if (debug) System.out.println(toString());

  return;
 }

 /* ******************************************************************* */
 /*                 Preprocessed Pattern Matching Methods               */
 /* ******************************************************************* */
 /* ------------------------------------------------------------------- */
 /* accept:                                                             */
 /* ------------------------------------------------------------------- */
 /** Determine if filename string matches pattern.
     @param s The string to be pattern matched.
     @return true means file is accepted by filter. */
 public boolean accept(String s)
 {
  // An empty or null pattern matches nothing.
  if (plen < 1) return false;

  // Locals.
  int slen = s.length();

  // ------------------- Preliminary Checks ----------------------
  // Length based checks.
  if (slen == 0) return false;              // empty string matches nothing
  if (!caseSensitive) s = s.toUpperCase();  // preserve OS semantics
  if (fixedLength) return fixedStringMatch(p, s);
  if (slen < minlen) return false;

  // Fixed length prefix check.
  if (fixedPrefix)
     if (!fixedPrefixMatch(s)) return false;

  // Fixed length suffix check.
  if (fixedSuffix)
     if (!fixedSuffixMatch(s)) return false;

  // ------------------- Recursive Pattern Match -----------------
  // Position the pattern start index to the character following
  // the first asteriks.  Position the text string start index to
  // the character following the prefix.
  int pstart, sstart;
  if (fixedPrefix) pstart = endPrefix + 2;  // skip over prefix and *
   else pstart = 1;                         // skip over *
  sstart = pstart - 1;                      // 1st char after prefix

  // Pattern matching can end at the begining of the fixed length
  // suffix.  Each recursion can used the value calculated here
  // and stored in the member field.
  sNoSuffixLen = slen - (plen - pNoSuffixLen);

  // Recursively match the characters in pattern to those in the string.
  return variableLengthMatch(pstart, sstart, s);
 }

 /* ------------------------------------------------------------------- */
 /* fixedStringMatch:                                                   */
 /* ------------------------------------------------------------------- */
 /** Determine if the text string match a fixed length pattern that may
     contain "?" wildcard characters.
     @param fp The fixed length pattern.
     @param fs The candidate text string.
     @return true if the text matches the pattern. */
 private boolean fixedStringMatch(String fp, String fs)
 {
  // Length check.
  int fplen = fp.length();
  if (fplen != fs.length()) return false;

  // Perform character by character match.
  char fpchar;
  int i;
  for (i = 0; i < fplen; i++)
   {
    fpchar = fp.charAt(i);
    if ((fpchar != fs.charAt(i)) && (fpchar != '?')) break;
   }

  // Determine results.
  if (i != fplen) return false;
   else return true;
 }

 /* ------------------------------------------------------------------- */
 /* fixedPrefixMatch:                                                   */
 /* ------------------------------------------------------------------- */
 /** Determine if the text string matches a fixed length prefix pattern
     that may contain "?" wildcard characters.
     @param s The candidate text string.
     @return true if the string text matches the pattern's prefix. */
 private boolean fixedPrefixMatch(String s)
 {
  // Perform character by character match.
  char pchar;
  int i;
  for (i = 0; i <= endPrefix; i++)
   {
    pchar = p.charAt(i);
    if ((pchar != s.charAt(i)) && (pchar != '?')) break;
   }

  // Determine results.
  if (i != endPrefix + 1) return false;
   else return true;
 }

 /* ------------------------------------------------------------------- */
 /* fixedSuffixMatch:                                                   */
 /* ------------------------------------------------------------------- */
 /** Determine if the text string matches a fixed length suffix pattern
     that may contain "?" wildcard characters.
     @param s The candidate text string.
     @return true if the string text matches the pattern's suffix. */
 private boolean fixedSuffixMatch(String s)
 {
  // Calculate suffix start offset from end of candidate string.
  int sOffset = s.length() - (plen - startSuffix);

  // Perform character by character match.
  char pchar;
  int i;
  for (i = startSuffix; i < plen; i++, sOffset++)
   {
    pchar = p.charAt(i);
    if ((pchar != s.charAt(sOffset)) && (pchar != '?')) break;
   }

  // Determine results.
  if (i != plen) return false;
   else return true;
 }

 /* ------------------------------------------------------------------- */
 /* variableLengthMatch:                                                */
 /* ------------------------------------------------------------------- */
 /** Determine if the text string match a fixed length pattern that may
     contain "?" wildcard characters.
     @param pstart The start index into the pattern string p.
     @param sstart The start index into the text string s.
     @param s The text string.
     @return true if the text string matches the pattern. */
 private boolean variableLengthMatch(int pstart, int sstart, String s)
 {
  // Tracing.
  if (debug)
     System.out.println("-varmatch: sNoSuffixLen=" + sNoSuffixLen +
      ", pstart=" + pstart + ", p=" + p.substring(pstart, pNoSuffixLen) +
      ", sstart=" + sstart + ", s=" + s.substring(sstart, sNoSuffixLen));

  // No more characters in the pattern counts as
  // a match because of the preceding askerik.
  if (pstart >= pNoSuffixLen - 1) return true;

  // Variables.
  int  pcur = pstart, scur = sstart, snext = sstart;
  char pchar;
  while ((pcur < pNoSuffixLen) && (scur < sNoSuffixLen))
   {
    // See if the current pattern character matches
    // the current string character.
    pchar = p.charAt(pcur);
    if ((pchar == s.charAt(scur)) || (pchar == '?'))
      {
       // We have a matching character.  Move to
       // next position in both pattern and string.
       pcur++;
       scur++;
      }
    else if (pchar == '*')
      {
       // Recurse because we hit an asterik.
       return variableLengthMatch(pcur+1, scur, s);
      }
    else
      {
       // No match.  Reset pattern index back to
       // begining, increment the string start offset
       // and reset the current string index to start
       // a whole new matching sequence.
       pcur = pstart;
       scur = ++snext;
      }
   }

  // If the text string is consumed, consume all
  // adjacent asteriks that may be left in the pattern.
  if (scur == sNoSuffixLen)
     while ((pcur < pNoSuffixLen) && ('*' == p.charAt(pcur))) pcur++;

  // An exact match occured if both the pattern
  // and text string are consumed.
  if ((pcur == pNoSuffixLen) && (scur == sNoSuffixLen)) return true;
   else return false;
 }

 /* ******************************************************************* */
 /*                 Unprocessed Pattern Matching Methods                */
 /* ******************************************************************* */
 /* ------------------------------------------------------------------- */
 /* accept:                                                             */
 /* ------------------------------------------------------------------- */
 /** Determine if filename string matches the given pattern using the
     default case sensitivity policy of OS.
     @param p The pattern string.
     @param s The string to be pattern matched.
     @return true means file is accepted by filter. */
 public static boolean accept(String p, String s)
 {return accept(p, s, osCaseSensitive);}

 /* ------------------------------------------------------------------- */
 /* accept:                                                             */
 /* ------------------------------------------------------------------- */
 /** Determine if filename string matches the given pattern.
     @param p The pattern string.
     @param s The string to be pattern matched.
     @param sensitive true means perform comparisons w/case sensitivity.
     @return true means file is accepted by filter. */
 public static boolean accept(String p, String s, boolean sensitive)
 {
  // Bogus input.
  if ((p.length() < 1) || (s.length() < 1)) return false;

  // Preserve OS semantics with regard to case comparisions.
  if (!sensitive)
    {
     p = p.toUpperCase();
     s = s.toUpperCase();
    }

  // Recursively match the characters in pattern to those in the string.
  return patternMatch(p, 0, s, 0, false);
 }

 /* ------------------------------------------------------------------- */
 /* patternMatch:                                                       */
 /* ------------------------------------------------------------------- */
 /** Determine if the text string matches an unprocess pattern that may
     contain "*" and "?" wildcard characters.
     @param p The pattern string.
     @param pstart The start index into the pattern.
     @param s The text string.
     @param sstart The start index into the text string.
     @param shift Asteriks processing enable flag.
     @return true if string matches pattern. */
 private static boolean patternMatch(String p, int pstart,
         String s, int sstart, boolean shift)
 {
  // Tracing.
  if (staticDebug)
     System.out.println("- p=" + p.substring(pstart) +
      ", pstart=" + pstart + ", s=" + s.substring(sstart) +
      ", sstart=" + sstart);

  // Get total string lengths.
  int plen = p.length();
  int slen = s.length();

  // No more characters in the pattern counts as
  // a match because if shift is true we're
  // processing an askerik.
  if (shift && (pstart >= plen - 1)) return true;

  // Variables.
  int  pcur = pstart, scur = sstart, snext = sstart;
  char pchar;
  while ((pcur < plen) && (scur < slen))
   {
    // See if the current pattern character matches
    // the current string character.
    pchar = p.charAt(pcur);
    if ((pchar == s.charAt(scur)) || (pchar == '?'))
      {
       // We have a matching character.  Move to
       // next position in both pattern and string.
       pcur++;
       scur++;
      }
    else if (pchar == '*')
      {
       // Recurse because we hit an asterik.
       return patternMatch(p, pcur+1, s, scur, true);
      }
    else
      {
       // We can only shift if we are concurrently processing an asterik.
       if (shift)
         {
          // No match.  Reset pattern index back to
          // begining, increment the string start offset
          // and reset the current string index to start
          // a whole new matching sequence.
          pcur = pstart;
          scur = ++snext;
         }
        else break;
      }
   }

  // If the text string is consumed, consume all
  // adjacent asteriks that may be left in the pattern.
  if (scur == slen)
     while ((pcur < plen) && ('*' == p.charAt(pcur))) pcur++;

  // An exact match occured if both the pattern
  // and text string are consumed.
  if ((pcur == plen) && (scur == slen)) return true;
   else return false;
 }

 /* ******************************************************************* */
 /*                        Utility Methods                              */
 /* ******************************************************************* */
 /* ------------------------------------------------------------------- */
 /* accept:                                                             */
 /* ------------------------------------------------------------------- */
 /** Provide filtering method required by FilenameFilter interface.
     The File.list() method uses this method to filter directory entries.
     @param dir The directory in which the file was found.
     @param name The name of the file.
     @return true means file is accepted by filter. */
 public boolean accept(File dir, String name)
 {
  // We only need to pattern match the file name.
  // The pattern object has been initialized with
  // the file name template.
  return accept(name);
 }

 /* ------------------------------------------------------------------- */
 /* toString:                                                           */
 /* ------------------------------------------------------------------- */
 /** Override default printing method.
     @return This object in string form. */
 public String toString()
 {
  String s = '{' + p + " : len=" + plen + ", minlen=" + minlen +
     ", fixedLength=" + fixedLength + ", fixedPrefix=" + fixedPrefix +
     ", fixedSuffix=" + fixedSuffix + ", endPrefix=" + endPrefix +
     ", startSuffix=" + startSuffix + ", pNoSuffixLen=" + pNoSuffixLen +
     '}';
  return s;
 }

 /* ------------------------------------------------------------------- */
 /* getPattern:                                                         */
 /* ------------------------------------------------------------------- */
 /** Accessor method.
     @return The pattern used to initialize this object. */
 public String getPattern(){return p;}

 /* ------------------------------------------------------------------- */
 /* getOSCaseSensitivity:                                               */
 /* ------------------------------------------------------------------- */
 /** Return true if OS is sensitive to case on file name comparisions
     or false if not.
     @return true if OS is case sensitive on filename comparisions.
   */
 protected static boolean getOSCaseSensitivity()
 {
  // List the case insensitive OS's by name.
  String os = System.getProperty("os.name").toLowerCase();
  if (os.startsWith("window")) return false;
  if (os.startsWith("dos")) return false;
  if (os.startsWith("os/2")) return false;
  return true;
 }

 /* ******************************************************************* */
 /*                            Unit Test                                */
 /* ******************************************************************* */
 /* ------------------------------------------------------------------- */
 /* main:                                                               */
 /* ------------------------------------------------------------------- */
 /** Driver stub.
     @param arg First parameter should be pattern string, next
            parameters should be filenames to be matched. */
 public static void main(String[] args)
 {
  // Locals.
  String s;
  
  // Get pattern string.
  if (args.length > 0) s = args[0];
   else
    {
     System.out.println(
       "Enter a pattern string followed by one or more filenames.");
     return;
    }

  // Create pattern object and display.
  StringPatternMatch pattern = new StringPatternMatch(s);

  // Test each file name.
  boolean result;
  for (int i = 1; i < args.length; i++)
   {
    result = pattern.accept(args[i]);
    //result = StringPatternMatch.accept(s, args[i]);
    System.out.println("Result = " + result + " for " + args[i] + '.');
   }
 }
}
