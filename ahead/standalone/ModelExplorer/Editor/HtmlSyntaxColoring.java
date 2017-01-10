/* ******************************************************************
   class      : HtmlSyntaxColoring
   description: A syntax highlighter for html syntax.
*********************************************************************/

package ModelExplorer.Editor;

import java.awt.*;
import java.util.*;

public class HtmlSyntaxColoring extends AbstractSyntaxColoring {

  // class variable declarations
  private final static String KEYWORD_PREFIX    = "keywords";
  private final static String TOKEN_KEY         = "tokens";
  private final static String DEF_TOKENS        = " \t\n";
  private final static String STRING_QUOTE_KEY  = "stringQuoteChar";
  private final static char   DEF_STRING_QUOTE  = '"';
  private final static String CHAR_QUOTE_KEY    = "charQuoteChar";
  private final static char   DEF_CHAR_QUOTE    = '\'';
  private final static String ESCAPE_CHAR_KEY   = "escapeChar";
  private final static char   DEF_ESCAPE_CHAR   = '\\';
  private final static String EOL_COMMENT_KEY   = "endOfLineComment";
  private final static String DEF_EOL_COMMENT   = "//";
  private final static String BLOCK_COMMENT_KEY = "blockComment";
  private final static String DEF_BLOCK_COMMENT = "/**/";

  private static int instCnt = 0;

  // instance variable declarations
  protected char[][] keywords;
  protected char[] tokens;
  protected char stringQuote;
  protected char charQuote;
  protected char escapeChar;
  protected char[] eolComment;
  protected char[] blockComment;
  protected char blockEndChar;
  protected int offset;
  protected boolean openComment;
  private int tokenType;
  private int numScanned;
  private int numPrefetched;
  private int prefetchedType;
  private boolean nextLineInv;
  private char[] array;
  private int count;
  private int lineNr;
  private BitSet commentBuffer;

  // constructors
  public HtmlSyntaxColoring() {
    super();
    keywords      = loadKeywords();
    tokens        = loadTokens();
    stringQuote   = loadStringQuote();
    charQuote     = loadCharQuote();
    escapeChar    = loadEscapeChar();
    eolComment    = loadEOLComment();
    blockComment  = loadBlockComment();
    blockEndChar  = blockComment.length == 2 ? blockComment[1] : blockComment[2];
    commentBuffer = new BitSet();
    commentBuffer.set(0);
    instCnt++;
  } // end constructor HtmlSyntaxColoring

  // class methods
  private static int match(char[] cbuf, int offs, int len,
                               char[] key) {
    int minLen = key.length < len ? key.length : len;

    for (int i=0; i<minLen; i++) {
      if (cbuf[offs] < key[i]) {
        return -1;
      } else if (cbuf[offs++] > key[i]) {
        return 1;
      }
    } // end for
    return len - key.length;
  } // end match

  // methods
  /**
   * Returns the previous scanned token(s) as string.
   *
   * @return The previous scanned token(s) as string.
   */
  public String getTokenString() {
    return new String(array,offset-numScanned,numScanned);
  } // end getTokenString

  /**
   * Returns the token type of the previous scanned token(s).
   *
   * @return the token type of the previous scanned token(s).
   */
  public int getTokenType() {
    return tokenType;
  } // end getTokenType

  /**
   * Returns true if the next line after the actually scanned must be
   * repainted; false otherwise.
   *
   * @return True if the next line must be repainted.
   */
  public boolean nextLineInvalid() {
    return nextLineInv;
  } // end netxLineInvalid

  /**
   * Set one line of code to tokenize.
   *
   * @param text This is the array containing the text of interest.
   * @param offset The offset into the array that the desired text begins.
   * @param count The number of chars that make up the text of interest.
   * @param lineNr The number of this line in the document.
   */
  public void setLine(char[] text, int offset, int count,
                               int lineNr) {
    this.array    = text;
    this.offset   = offset;
    this.count    = count;
    this.lineNr   = lineNr;
    tokenType     = NORMAL_TEXT;
    numScanned    = 0;
    numPrefetched = 0;
    openComment   = commentBuffer.get(lineNr);
    nextLineInv   = false;
  } // end setLine

  /**
   * Returns the length of the scanned text block, which consists
   * only of one type of tokens.
   *
   * @return The length of the scanned text block, zero if end of block.
   */
  public int scan() {
    numScanned = 0;
    if (numPrefetched == 0) numPrefetched = scanToken();
    tokenType = prefetchedType;
    while ((tokenType == prefetchedType) && (numPrefetched > 0)) {
      numScanned   += numPrefetched;
      numPrefetched = scanToken();
    } // end while
    if (eol()) {
      nextLineInv = openComment ^ commentBuffer.get(lineNr+1);
      if (openComment) commentBuffer.set(lineNr+1);
        else commentBuffer.clear(lineNr+1);
    }
    else {
      nextLineInv = false;
    } // end if
    return numScanned;
  } // end scan

  /**
   * Returns the size of the next element of this token enumeration.
   *
   * @return The size of the next element of this enumeration.
   * @throws NoSuchElementException if no more elements exist.
   */
  public int getTokenLength() {
    if (scan() == 0) throw new NoSuchElementException();
    return numScanned;
  } // end getTokenLength

  /** Scan a single token. */
  private int scanToken() {
    char ch;
    int scanned = 0;

    if (eol()) return 0;
    if (openComment) {
      prefetchedType = BLOCK_COMMENT;
      if (blockComment.length > 2) return readDoubleBlockComment();
        else return readSingleBlockComment();
    } // end if
    ch = read();
    scanned++;
    if (isToken(ch)) {
      if (ch == stringQuote) {
        scanned += readStringLiteral();
        prefetchedType = STRING_LITERAL;
        return scanned;
      } else if (ch == charQuote) {
        scanned += readCharLiteral();
        prefetchedType = CHAR_LITERAL;
        return scanned;
      } else if ((ch == eolComment[0]) && (
                  (eolComment.length == 1) ||
                  (!eol() && (readUnconsumed() == eolComment[1]))
                 )) {
        scanned += readEOLComment();
        prefetchedType = EOL_COMMENT;
        return scanned;
      } else if ((ch == blockComment[0]) && (
                  (blockComment.length == 2) ||
                  (!eol() && (readUnconsumed() == blockComment[1]))
                 )) {
        if (blockComment.length > 2) {
          read();
          scanned++;
          scanned += readDoubleBlockComment();
        }
        else {
          scanned += readSingleBlockComment();
        } // end if
        prefetchedType = BLOCK_COMMENT;
        return scanned;
      } else {
        prefetchedType = NORMAL_TEXT;
        return scanned;
      } // end if
    }
    else {
      int start = offset;
      do {
        ch = read();
      } while (!isToken(ch));
      unRead();
      scanned += offset-start;
      if (isKeyword(array,start-1,scanned))
        prefetchedType = KEYWORD;
      else
        prefetchedType = NORMAL_TEXT;
      // end if
      return scanned;
    } // end if
  } // end scanToken

  /**
   * Tests if this enumeration contains more elements.
   *
   * @return True if this enumeration has more elements, false otherwise.
   */
  public boolean hasMoreElements() {
    return (numPrefetched > 0) || !eol();
  } // end hasMoreElements

  /**
   * Load the keywords from the properties. If the keywords in the
   * properties file are sorted, then it is faster!
   */
  protected char[][] loadKeywords() {
    Vector vector = new Vector();
    int i         = 1;
    String words  = getProperty(KEYWORD_PREFIX + 1);
    String str;
    StringTokenizer t;
    char[][] result;

    while (words != null) {
      t = new StringTokenizer(words);
      while (t.hasMoreTokens()) {
        str = t.nextToken();
        if (vector.isEmpty() ||
            (str.compareTo((String)vector.lastElement()) >= 0)) {
          vector.addElement(str);
        }
        else {
          int j = vector.size()-1;
          while ((j >= 0) &&
                 (str.compareTo((String)vector.elementAt(j)) < 0))
            j--;
          // end while
          vector.insertElementAt(str,j+1);
        } // end if
      } // end while
      i++;
      words = getProperty(KEYWORD_PREFIX+i);
    } // end while
    result = new char[vector.size()][];
    for (i=0; i<result.length; i++)
      result[i] = ((String)vector.elementAt(i)).toCharArray();
    // end for
    return result;
  } // end loadKeywords

  /**
   * Returns true is str is a keyword
   *
   * @return true is str is a keyword
   */
  protected boolean isKeyword(char[] cbuf, int offs, int len) {
    int cur,l,r,result;

    if (len < 2) return false;
    // binary search
    l = 0; r = keywords.length-1;
    do {
      cur    = (l + r) / 2;
      result = match(cbuf,offs,len,keywords[cur]);
      if ((result < 0) || (l > r))
        r = cur-1;
      else
        l = cur+1;
      // end if
    } while ((result != 0) && (l <= r));
    return (result == 0);
  } // end isKeyword

  /** Load the tokens from the properties. */
  protected char[] loadTokens() {
    String str = getProperty(TOKEN_KEY);
    char[] chars;

    if (str == null) str = DEF_TOKENS;
      else str = DEF_TOKENS+str;
    return str.toCharArray();
  } // end loadTokens

  /**
   * Returns true is ch is a token char
   *
   * @return true is ch is a token char
   */
  protected boolean isToken(char ch) {
    int i = 0;
    while (i < tokens.length) {
      if (tokens[i] == ch) return true;
      i++;
    } // end while
    return false;
  } // end isToken

  /** Load the string quoting character from the properties. */
  protected char loadStringQuote() {
    String str = getProperty(STRING_QUOTE_KEY);

    if ((str == null) || (str.length() < 1))
      return DEF_STRING_QUOTE;
    else
      return str.charAt(0);
    // end if
  } // end loadStringQuote

  /** Load the char quoting character from the properties. */
  protected char loadCharQuote() {
    String str = getProperty(CHAR_QUOTE_KEY);

    if ((str == null) || (str.length() < 1))
      return DEF_CHAR_QUOTE;
    else
      return str.charAt(0);
    // end if
  } // end loadCharQuote

  /** Load the escape character from the properties. */
  protected char loadEscapeChar() {
    String str = getProperty(ESCAPE_CHAR_KEY);

    if ((str == null) || (str.length() < 1))
      return DEF_ESCAPE_CHAR;
    else
      return str.charAt(0);
    // end if
  } // end loadEscapeChar

  /** Load the end of line comment characters from the properties. */
  protected char[] loadEOLComment() {
    String str = getProperty(EOL_COMMENT_KEY);
    int l      = str == null ? 0 : str.length();

    switch (l) {
      case 0:
        str = DEF_EOL_COMMENT;
      case 1:
      case 2:
        return str.toCharArray();
      default:
        char[] c = new char[2];
        c[0]     = str.charAt(0);
        c[1]     = str.charAt(1);
        return c;
    } // end switch
  } // end loadEOLComment

  /** Load the block comment characters from the properties. */
  protected char[] loadBlockComment() {
    String str = getProperty(BLOCK_COMMENT_KEY);
    int l      = str == null ? 0 : str.length();
    char[] c;

    switch (l) {
      case 0:
      case 1:
        str = DEF_BLOCK_COMMENT;
        return str.toCharArray();
      case 2:
      case 3:
        c    = new char[2];
        c[0] = str.charAt(0);
        c[1] = str.charAt(1);
        return c;
      default:
        c    = new char[4];
        for (int i=0; i<4; i++) c[i] = str.charAt(i);
        return c;
    } // end switch
  } // end loadBlockComment

  /** Reads over a string literal. */
  private int readStringLiteral() {
    int i = offset;
    char ch;

    do {
      ch = read();
      if ((ch == escapeChar) && !eol()) read();
    } while(!eol() && (ch != stringQuote));
    return offset-i;
  } // end readStringLiteral

  /** Reads over a character literal. */
  private int readCharLiteral() {
    int i = offset;
    char ch;

    do {
      ch = read();
      if ((ch == escapeChar) && !eol()) read();
    } while(!eol() && (ch != charQuote));
    return offset-i;
  } // end readCharLiteral

  /** Reads over a end of line comment. */
  private int readEOLComment() {
    int i = count;

    offset += count;
    count = 0;
    return i;
  } // end readEOLComment

  /** Reads over a block comment with single brackets. */
  private int readSingleBlockComment() {
    int i = offset;
    char ch;

    do {
      if (eol()) {
        openComment = true;
        return offset-i;
      } // end if
      ch = read();
    } while (ch != blockEndChar);
    openComment = false;
    return offset-i;
  } // end readSingleBlockComment

  /** Reads over a block comment with double brackets. */
  protected int readDoubleBlockComment() {
    int i = offset;
    char ch;
    boolean partialMatch = false;

    do {
      if (eol()) {
        openComment = true;
        return offset-i;
      } // end if
      ch = read();
      if ((partialMatch) && (ch == blockComment[3])) {
        openComment = false;
        return offset-i;
      } // end if
      partialMatch = (ch == blockEndChar);
    } while(true);
  } // end readDoubleBlockComment

  /** Reads a character from the array. */
  protected final char read() {
    count--;
    return array[offset++];
  } // end read

  /**
   * Reads a character from the array without moving
   * the read position.
   */
  private final char readUnconsumed() {
    return array[offset];
  } // end readUnconsumed

  /** Make a undo of a previous read operation from the array. */
  private final void unRead() {
    offset--;
    count++;
  } // end unRead

  /** Returns the if end of array is reached. */
  protected final boolean eol() {
    return (count < 1);
  } // end eol

} // end HtmlSyntaxColoring

/* ******************************************************************
   end of file
*********************************************************************/
