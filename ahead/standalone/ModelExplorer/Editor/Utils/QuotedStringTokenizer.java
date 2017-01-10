/* ******************************************************************
   class    : QuotedStringTokenizer
*********************************************************************/

package ModelExplorer.Editor.Utils;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 *
 * A StringTokenizer that can handle quoted strings.
 */
public class QuotedStringTokenizer implements Enumeration {

  // class variable declarations
  private final static char ESCAPE_CHAR = '\\';

  // instance variable declarations
  private char[] cbuf;
  private int offs = 0;
  private String delim;
  private boolean returnTokens;
  private char quoteCh;
  private String allDelim;
  private String curToken;

  // constructors
  /**
   * Constructs a string tokenizer for the specified string. The characters
   * in the delim argument are the delimiters for separating tokens.
   * If the returnTokens flag is true, then the delimiter characters are
   * also returned as tokens. Each delimiter is returned as a string of
   * length one. If the flag is false, the delimiter characters are skipped
   * and only serve as separators between tokens.
   *
   * @param str A string to be parsed.
   * @param delim The delimiters.
   * @param returnTokens Flag indicating whether to return the delimiters
   *                     as tokens.
   */
  public QuotedStringTokenizer(String str, String delim,
                               boolean returnTokens) {
    this(str,delim,returnTokens,(int)'"');
  } // end constructor QuotedStringTokenizer

  /**
   * Constructs a string tokenizer for the specified string. The characters
   * in the delim argument are the delimiters for separating tokens.
   *
   * @param str A string to be parsed.
   * @param delim The delimiters.
   */
  public QuotedStringTokenizer(String str, String delim) {
    this(str,delim,false);
  } // end constructor QuotedStringTokenizer

  /**
   * Constructs a string tokenizer for the specified string. The tokenizer
   * uses the default delimiter set, which is "\t\n\r ": the space character,
   * the tab character, the newline character, and the carriage-return
   * character.
   *
   * @param str A string to be parsed.
   */
  public QuotedStringTokenizer(String str) {
    this(str,"\t\n\r ");
  } // end constructor QuotedStringTokenizer

  /**
   * Constructs a string tokenizer for the specified string. The characters
   * in the delim argument are the delimiters for separating tokens.
   * If the returnTokens flag is true, then the delimiter characters are
   * also returned as tokens. Each delimiter is returned as a string of
   * length one. If the flag is false, the delimiter characters are skipped
   * and only serve as separators between tokens.
   *
   * @param str A string to be parsed.
   * @param delim The delimiters.
   * @param returnTokens Flag indicating whether to return the delimiters
   *                     as tokens.
   * @param quoteCh The matching pairs of characters delimit string constants.
   */
  public QuotedStringTokenizer(String str, String delim,
                               boolean returnTokens, int quoteCh) {
    cbuf              = str.toCharArray();
    this.delim        = delim;
    this.returnTokens = returnTokens;
    this.quoteCh      = (char)quoteCh;
    if (this.delim.indexOf(quoteCh) < 0) allDelim = this.delim + this.quoteCh;
      else allDelim = this.delim;
    readToken();
  } // end constructor QuotedStringTokenizer

  // class methods
/* DEBUG ON */
  /** Only for debuging purposes */
  public static void main(String[] args) throws java.io.IOException {
    QuotedStringTokenizer tokenizer;
    String str = "";
    int c      = System.in.read();

    while (c != -1) {
      str += (char)c;
      c = System.in.read();
    } // end while
    tokenizer = new QuotedStringTokenizer(str,"\t\n\r " + "+*-/%=()[]{}\"'\\.;:,#<>",(args.length == 0));
    System.out.println("Number of tokens: " + tokenizer.countTokens());
    while (tokenizer.hasMoreTokens())
      System.out.print("|" + tokenizer.nextToken());
  } // end main
/* DEBUG OFF */

  // methods
  /**
   * Tests if there are more tokens available from this tokenizer's string.
   *
   * @return True if there are more tokens available from this tokenizer's
   *         string; false otherwise.
   */
  public boolean hasMoreTokens() {
    return (curToken != null);
  } // end hasMoreTokens


  /**
   * Returns the next token from this string tokenizer.
   *
   * @return The next token from this string tokenizer.
   * @exception NoSuchElementException If there are no more tokens
   *                                   in this tokenizer's string.
   */
  public String nextToken() {
    if (curToken == null) throw new NoSuchElementException();

    String str = curToken;

    readToken();
    return str;
  } // end nextToken

  /**
   * Returns the next token in this string tokenizer's string. The
   * new delimiter set remains the default after this call.
   *
   * @param delim The new delimiters.
   * @return The next token, after switching to the new delimiter set.
   * @exception NoSuchElementException if there are no more tokens
   *                                   in this tokenizer's string.
   */
  public String nextToken(String delim) {
    delimiterChars(delim);
    return nextToken();
  } // end nextToken

  /**
   * Returns the next token without removing it from the tokenizer.
   *
   * @return The next token.
   * @exception NoSuchElementException if there are no more tokens
   *                                   in this tokenizer's string.
   */
  public String previewToken() {
    if (curToken == null) throw new NoSuchElementException();

    return curToken;
  } // end previewToken

  /**
   * Returns the same value as the hasMoreTokens method. It exists so that
   * this class can implement the Enumeration interface.
   *
   * @return True if there are more tokens; false otherwise.
   * @see hasMoreTokens
   */
  public boolean hasMoreElements() {
    return hasMoreTokens();
  } // end hasMoreElements

  /**
   * Returns the same value as the nextToken method, except that its
   * declared return value is Object rather than String. It exists so
   * that this class can implement the Enumeration interface.
   *
   * @return The next token in the string.
   * @exception NoSuchElementException If there are no more tokens in this
   *                                   tokenizer's string.
   * @see nextToken
   */
  public Object nextElement() {
    return nextToken();
  } // end nextElement

  /**
   * Calculates the number of times that this tokenizer's nextToken method
   * can be called before it generates an exception.
   *
   * @return The number of tokens remaining in the string using the current
   *         delimiter set.
   * @see nextToken
   */
  public int countTokens() {
    // brut force implementation
    int tmp  = offs;
    int i    = 0;
    String s = curToken;

    while (hasMoreTokens()) {
      nextToken();
      i++;
    } // end while
    curToken = s;
    offs     = tmp;
    return i;
  } // end countTokens

  /**
   * Specifies that matching pairs of this character delimit string
   * constants in this tokenizer.
   *
   * @param ch The character.
   */
  public void quoteChar(int ch) {
    quoteCh  = (char)ch;
    if (this.delim.indexOf(ch) < 0) allDelim = delim + quoteCh;
      else allDelim = delim;
    if (curToken != null) {
      offs -= curToken.length();
      readToken();
    } // end if
  } // end quoteChar

  /**
   * Set the delimiter characters of this tokenizer.
   *
   * @param delim The delimiter characters.
   */
  public void delimiterChars(String delim) {
    this.delim = delim;
    if (this.delim.indexOf(quoteCh) < 0) allDelim = this.delim + quoteCh;
      else allDelim = this.delim;
    if (curToken != null) {
      offs -= curToken.length();
      readToken();
    } // end if
  } // end delimiterChars

  private void readToken() {
    int pos = offs;

    if (offs >= cbuf.length) {
      curToken = null;
      return;
    } // end if
    if (allDelim.indexOf(cbuf[offs++]) < 0) { // if cbuf[offs] is not a token
      while ((offs < cbuf.length) &&
             (allDelim.indexOf(cbuf[offs]) < 0)) offs++;
      curToken = new String(cbuf,pos,offs - pos);
      return;
    }
    else { // if cbuf[offs] is a token
      if (cbuf[pos] == quoteCh) { // if token is a string literal
        boolean escape = false;
        char ch;

        if (!returnTokens) pos++; // skip quote character
        while (offs < cbuf.length) {
          ch = cbuf[offs];
          if ((ch == quoteCh) && !escape) { // Yes, we've found the end
            if (returnTokens) offs++;
            break;
          } // end if
          if (ch == ESCAPE_CHAR) escape = !escape;
            else escape = false;
          offs++;
        } // end while
        curToken = new String(cbuf,pos,offs - pos);
      }
      else {
        if (returnTokens) curToken = new String(cbuf,pos,offs - pos);
          else readToken();
      } // end if
    } // end if
  } // end readToken

} // end QuotedStringTokenizer

/* ******************************************************************
   end of file
*********************************************************************/
