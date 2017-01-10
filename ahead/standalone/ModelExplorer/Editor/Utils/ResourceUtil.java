/* ******************************************************************
   class      : ResourceUtil
   description: a class that helps you access resource bundles
*********************************************************************/

package ModelExplorer.Editor.Utils;

import java.util.*;
import java.io.File;
import java.net.URL;

public class ResourceUtil {

  // constructors
  private ResourceUtil() {} // avoids instanciation of this class

  // class methods
  /**
   * Read a string from a resource file.
   *
   * @return The string or null, if nm does not exist.
   */
  public final static String getResourceString(ResourceBundle res,
                                               String nm) {
    String str;

    try {
      str = res.getString(nm);
    } catch (MissingResourceException mre) {
      str = null;
    }
    return str;
  } // end getResourceString

  /**
   * Read a int from a resource file.
   *
   * @param res The resource bundle.
   * @param nm The resource key name.
   * @param def A default value, if the resource couldn'T be found
   *        or has wrong number format.
   * @return The integer value or the default value.
   */
  public final static int getResourceInt(ResourceBundle res,
                                         String nm, int def) {
    String str;
    int i;

    try {
      str = res.getString(nm);
      i   = Integer.parseInt(str);
    } catch (NumberFormatException excep) {
      i = def;
    } catch (MissingResourceException mre) {
      i = def;
    }
    return i;
  } // end getResourceInt

  /**
   * Read a boolean from a resource file.
   *
   * @param res The resource bundle.
   * @param nm The resource key name.
   * @return The integer value or false if the key doesn't exist.
   */
  public final static boolean getResourceBool(ResourceBundle res,
                                              String nm) {
    String str;
    boolean b;

    try {
      str = res.getString(nm);
      b   = (new Boolean(str)).booleanValue();
    } catch (MissingResourceException mre) {
      b = false;
    }
    return b;
  } // end getResourceBool

  /**
   * Read a string from a resource file.  Return the key, if this resource
   * does not exist.
   *
   * @return The string or the nm argument, if it is no key.
   */
  public final static String getResStringOrKey(ResourceBundle res,
                                               String nm) {
    String str;

    try {
      str = res.getString(nm);
    } catch (MissingResourceException mre) {
      str = nm;
    }
    return str;
  } // end getResStringOrKey

  /**
   * Returns the url of a resource spezified by key, which is at the
   * same location as inst.
   */
  public final static URL getResource(Object inst, ResourceBundle res,
                                      String key) {
    String name = getResourceString(res,key);

    if(name != null)
      return inst.getClass().getResource(name);
    // end if
    return null;
  } // end getResource

  /**
   * Take the given string and chop it up into a series
   * of strings on whitespace boundries. This is useful
   * for trying to get an array of strings out of the
   * resource file.
   */
  public final static String[] splitToWords(String input) {
    if (input == null) return null;

    Vector v          = new Vector();
    StringTokenizer t = new StringTokenizer(input);
    String cmd[];

    while (t.hasMoreTokens()) v.addElement(t.nextToken());
    cmd = new String[v.size()];
    for (int i=0; i<cmd.length; i++) cmd[i] = (String)v.elementAt(i);
    return cmd;
  } // end splitToWords

  /**
   * Converts a file to an url.
   */
  public final static String fileToURL(String file) {
    return "file:/" + file.replace(File.separatorChar,'/');
  } // end fileToURL

  /**
   * Converts an url to a file.
   */
  public final static String urlToFile(String url) {
    return url.substring(6).replace('/',File.separatorChar);
  } // end urlToFile

} // end ResourceUtil

/* ******************************************************************
   end of file
*********************************************************************/
