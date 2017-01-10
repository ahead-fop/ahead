/* ******************************************************************
   class      : AbstractSyntaxColoring
   description: A abstract baseclass for syntax coloring purposes.

*********************************************************************/
package ModelExplorer.Editor;
import ModelExplorer.Editor.Utils.ResourceUtil;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Graphics;
import java.util.*;

public abstract class AbstractSyntaxColoring implements Enumeration {

  // class variable declarations
  /**
   * This constant value indicates that the last returned token(s)
   * is/are normal text
   */
  public static final int NORMAL_TEXT     = 0;

  /**
   * This constant value indicates that the last returned token(s)
   * is/are a character literal
   */
  public static final int CHAR_LITERAL    = 1;

  /**
   * This constant value indicates that the last returned token(s)
   * is/are a string literal
   */
  public static final int STRING_LITERAL  = 2;

  /**
   * This constant value indicates that the last returned token(s)
   * is/are a numeric literal
   */
  public static final int NUMERIC_LITERAL = 3;

  /**
   * This constant value indicates that the last returned token(s)
   * is/are a keyword
   */
  public static final int KEYWORD         = 4;

  /**
   * This constant value indicates that the last returned token
   * is a preprocessor statement
   */
  public static final int PREPROC_STAT    = 5;

  /**
   * This constant value indicates that the last returned token
   * is a comment that reaches till the end of line
   */
  public static final int EOL_COMMENT     = 6;

  /**
   * This constant value indicates that the last returned token
   * is a block comment
   */
  public static final int BLOCK_COMMENT  = 7;

  /**
   * This constant value indicates that the last returned token
   * is another type of block comment
   */
  public static final int ALT_COMMENT  = 8;

  /**
   * This constant value indicates that the last returned token
   * is a documentation block comment
   */
  public static final int DOC_COMMENT  = 9;
  
  public static final int SPECIALWORD =10;

  /** Keys to token type colors. */
  private final static String[] DEF_COLOR_NAMES = {
    "color.normalText",
    "color.charLiteral",
    "color.stringLiteral",
    "color.numLiteral",
    "color.keyword",
    "color.preprocStat",
    "color.eolComment",
    "color.blockComment",
    "color.altBlockComment",
    "color.docBlockComment",
	"color.specialword"
  };

  /** Hashtable containing all loaded property resource bundles. */
  private static Hashtable loadedProperties = new Hashtable();

  // instance variable declarations
  /** Settings for the syntax coloring. */
  protected ResourceBundle prop;

  /** The token type colors. */
  private Color[] colorMapping = {
    SystemColor.textText, // normal text
    Color.red,            // char literal
    Color.red,            // string literal
    SystemColor.textText, // numeric literal
    Color.blue,           // keyword
    Color.yellow,         // preprocessor statement
    Color.green.darker(), // end of line comment
    Color.green.darker(), // block comment
    Color.green.darker(), // alternative block comment
    Color.green.darker(),  // documentation block comment
	Color.magenta.darker() // special words
  };

  // constructors
  public AbstractSyntaxColoring() throws MissingResourceException {
    String bundleName = getClass().getName();
    int i             = bundleName.lastIndexOf((int)'.');
    int[] rgb         = new int[3];

    if (i >= 0) bundleName = "ModelExplorer/Editor/_" + bundleName.substring(i + 1);
      else bundleName = "ModelExplorer/Editor/_" + bundleName;
    prop = (ResourceBundle)loadedProperties.get(bundleName);
    if (prop == null) {
      prop = ResourceBundle.getBundle(bundleName,Locale.getDefault());
      loadedProperties.put(bundleName,prop);
    } // end if
    for (i=0; i<colorMapping.length; i++) {
      String str = ResourceUtil.getResourceString(prop,DEF_COLOR_NAMES[i]);
      String[] aStr;

      if (str == null) continue;
      aStr = ResourceUtil.splitToWords(str);
      try {
        for (int j=0; j<3; j++) rgb[j] = Integer.parseInt(aStr[j]);
      } catch (ArrayIndexOutOfBoundsException e1) {
        System.err.println(bundleName + ".properties: " + str + ": " + e1);
        continue;
      } catch (NumberFormatException e2) {
        System.err.println(bundleName + ".properties: " + str + ": " + e2);
        continue;
      }
      colorMapping[i] = new Color(rgb[0],rgb[1],rgb[2]);
    } // end for
  } // end constructor AbstractSyntaxColoring

  // methods
  /**
   * Returns the previous scanned token(s) as string.
   *
   * @return The previous scanned token(s) as string.
   */
  public abstract String getTokenString();

  /**
   * Returns the token type of the previous scanned token(s).
   *
   * @return the token type of the previous scanned token(s).
   */
  public abstract int getTokenType();

  /**
   * Returns true if the next line after the actually scanned must be
   * repainted; false otherwise.
   *
   * @return True if the next line must be repainted.
   */
  public abstract boolean nextLineInvalid();

  /** Gets the color of the previous scanned token(s). */
  public final Color getColor() {
    return colorMapping[getTokenType()];
  } // ;

  /** Gets the color for a token type. */
  public final Color getColor(int tokenType) {
    return colorMapping[tokenType];
  } // end getColor

  /** Sets the color for a token type. */
  public final void setColor(Color color, int tokenType) {
    colorMapping[tokenType] = color;
  } // end setColor

  /**
   * Set one line of code to tokenize.
   *
   * @param text This is the array containing the text of interest.
   * @param offset The offset into the array that the desired text begins.
   * @param count The number of chars that make up the text of interest.
   * @param lineNr The number of this line in the document.
   */
  public abstract void setLine(char[] text, int offset, int count,
                               int lineNr);

  /**
   * Set one line of code to tokenize.
   *
   * @param line This is the string containing the text of interest.
   * @param lineNr The number of this line in the document.
   */
  public void setLine(String line, int lineNr) {
    int l        = line.length();
    char[] chars = new char[l];

    line.getChars(0,l,chars,0);
    setLine(chars,0,l,lineNr);
  } // end setLine

  /**
   * Returns the length of the scanned text block, which consists
   * only of one type of tokens.
   *
   * @return The length of the scanned text block, zero if end of block.
   */
  public abstract int scan();

  /**
   * Returns the next element of this token enumeration.
   *
   * @return The next element of this enumeration.
   * @throws NoSuchElementException if no more elements exist.
   */
  public Object nextElement() {
    if (scan() == 0) throw new NoSuchElementException();
    return getTokenString();
  } // end nextElement

  /**
   * Returns the size of the next element of this token enumeration.
   *
   * @return The size of the next element of this enumeration.
   * @throws NoSuchElementException if no more elements exist.
   */
  public int getTokenLength() {
    if (scan() == 0) throw new NoSuchElementException();
    return getTokenString().length();
  } // end getTokenLength

  /** Returns a property from the syntax coloring resource bundle. */
  protected final String getProperty(String key) {
    return ResourceUtil.getResourceString(prop,key);
  } // end getProperty

} // end AbstractSyntaxColoring

/* ******************************************************************
   end of file
*********************************************************************/
