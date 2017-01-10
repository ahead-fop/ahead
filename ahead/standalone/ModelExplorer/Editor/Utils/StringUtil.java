/* ******************************************************************
   class    : StringUtil
*********************************************************************/

package ModelExplorer.Editor.Utils;

import java.io.File;

/**
 * A class the contains some useful string manipulation methods.
 */
public class StringUtil {

  // class variable declarations
  private static StringBuffer buf;

  // constructors
  /** Avoid instantiation of this Class. */
  private StringUtil() {
  } // end constructor StringUtil

  // class methods
/* DEBUG ON */
  public static void main(String[] argv) {
    if (argv.length < 1) System.err.println("Missing parameter");
      else {
        System.out.println(argv[0]);
        System.out.println(convertToLiteral(argv[0]));
      } // end if
  } // end main
/* DEBUG OFF */

  /** Replace the escape sequences from the quoted string representation. */
  public final static String replaceEscapeSeq(String str) {
    int i = 0;
    int l = str.length();
    char c;
    String num;
    StringBuffer buf;
    int n;

    if ((i < l) && (str.charAt(0) == '"')) {
      i++; l--;
    } // end if
    buf = new StringBuffer(l + 2);
    while (i < l) {
      c = str.charAt(i++);
      if (c == '\\') {
        try {
          c = str.charAt(i++);
          switch (c) {
            case 'n':  buf.append('\n'); break;
            case 't':  buf.append('\t'); break;
            case 'b':  buf.append('\b'); break;
            case 'r':  buf.append('\r'); break;
            case 'f':  buf.append('\f'); break;
            case '\\': buf.append('\\'); break;
            case '\'': buf.append('\''); break;
            case '"':  buf.append('"');  break;
            case 'u':
              num = "" + str.charAt(i++) + str.charAt(i++) +
                    str.charAt(i++) + str.charAt(i++);
              n = Integer.parseInt(num,16);
              buf.append((char)n);
            break;
            case '0': case '1': case '2': case '3':
            case '4': case '5': case '6': case '7':
              num = "" + str.charAt(i++) + str.charAt(i++) +
                    str.charAt(i++);
              n = Integer.parseInt(num,8);
              buf.append((char)n);
            break;
            default:
          } // end switch
        } catch (StringIndexOutOfBoundsException e) {
        } catch (NumberFormatException e) {
        }
      }
      else {
        buf.append(c);
      } // end if
    } // end while
    return buf.toString();
  } // end replaceEscapeSeq

  /** Converts a string to the string literal representation. */
  public final static String convertToLiteral(String str) {
    int i = 0;
    int l = str.length();
    char c;
    String num;
    StringBuffer buf;
    int n;

    buf = new StringBuffer(l + 2);
    buf.append('"');
    while (i < l) {
      c = str.charAt(i++);
      switch (c) {
        case '\n':  buf.append("\\n");  break;
        case '\t':  buf.append("\\t");  break;
        case '\b':  buf.append("\\b");  break;
        case '\r':  buf.append("\\r");  break;
        case '\f':  buf.append("\\f");  break;
        case '\\':  buf.append("\\\\"); break;
        case '"':   buf.append("\\\""); break;
        default:
          if ((c < (char)32) || (c > (char)127)) {
            buf.append("\\u");
            num = Integer.toOctalString((int)c);
            while (num.length() < 4) num = "0" + num;
            buf.append(num);
          }
          else {
            buf.append(c);
          } // end if
        } // end switch
    } // end while
    buf.append('"');
    return buf.toString();
  } // end convertToLiteral

  /**
   * Converts a string containing an absolute path to a string
   * containing a path relative to absPart.
   */
  public final static String relativePath(String path, String absPart) {
    if (path.startsWith(absPart)) {
      path = path.substring(absPart.length());
      if (path.startsWith(File.separator)) return path.substring(1);
    } // end if
    return path;
  } // end relativePath

} // end StringUtil

/* ******************************************************************
   end of file
*********************************************************************/
