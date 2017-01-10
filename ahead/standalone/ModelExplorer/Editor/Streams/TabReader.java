/* ******************************************************************
   class    : TabReader
*********************************************************************/
package ModelExplorer.Editor.Streams;

import java.io.*;

/**
 *
 * A class that replaces tab characters by the specified number of
 * space characters and modifies the line separators.
 */
public class TabReader extends FilterReader {

  // class variable declarations
  public final static int NO_CHANGE = 0;
  public final static int UNIX      = 1;
  public final static int MAC       = 2;
  public final static int DOS       = 3;
  public final static int PLATFORM  = 4;

  protected static int platformMode;

  // instance variable declarations
  /** The line separator mode. */
  protected int mode;
  /** The tabulator size. */
  protected int tabSize;
  /** The column number. */
  protected int col = 0;
  /** The character readed at the last read operation. */
  protected int oldCh = 0;
  /** True if a previous readed cr character implies a lf character. */
  protected boolean pendingLineFeed = false;
  /** The number of space characters produced by a tabulator. */
  protected int pendingSpaces = 0;

  // static initializers
  static {
    String lineSepar = System.getProperty("line.separator");

    if (lineSepar.equals("\n"))
      platformMode = UNIX;
    else if (lineSepar.equals("\r"))
      platformMode = MAC;
    else if (lineSepar.equals("\r\n"))
      platformMode = DOS;
    else
      platformMode = NO_CHANGE;
    // end if
  } // end static initializer

  // constructors
  /**
   * Constructs a new TabReader initialized with the specified reader.
   *
   * @param in The reader.
   */
  public TabReader(Reader in) {
    this(in,8);
  } // end constructor TabReader

  /**
   * Constructs a new TabReader initialized with the specified
   * reader and tabulator size.
   *
   * @param in The reader.
   * @param tabSize The tabulator size.
   */
  public TabReader(Reader in, int tabSize) {
    this(in,tabSize,NO_CHANGE);
  } // end constructor TabReader

  /**
   * Constructs a new TabReader initialized with the specified
   * reader, tabulator size and line separator mode.
   *
   * @param in The reader.
   * @param tabSize The tabulator size.
   * @param mode One of the line separator mode constants.
   */
  public TabReader(Reader in, int tabSize, int mode) {
    super(in);
    setTabSize(tabSize);
    setLineSeparator(mode);
  } // end constructor TabReader

  // class methods
  /** Only for debuging purposes */
  public static void main(String[] args) throws IOException {
    TabReader filter;
    int nch,m;
    char[] cb = new char[64];

    if (args.length >= 2) {
      nch = Integer.parseInt(args[0]);
      switch (args[1].toLowerCase().charAt(0)) {
        case 'p': m = PLATFORM; break;
        case 'u': m = UNIX;     break;
        case 'm': m = MAC;      break;
        case 'd': m = DOS;      break;
        default:  m = NO_CHANGE;
      }
      filter = new TabReader(new InputStreamReader(System.in),nch,m);
    } else if (args.length == 1) {
      nch = Integer.parseInt(args[0]);
      filter = new TabReader(new InputStreamReader(System.in),nch);
    } else {
      filter = new TabReader(new InputStreamReader(System.in));
    } // end if
    while ((nch = filter.read(cb,0,cb.length)) != -1) {
      System.out.print(new String(cb,0,nch));
    } // end while
    filter.close();
  } // end main

  // methods
  /**
   * Reads a character.  The method will block if no input is available.
   *
   * @return  the character read, or -1 if the end of the stream is reached.
   */
  public int read() throws IOException {
    // This is the routine that really implements the special
    // functionality of this class; the others just call this
    // one to get the data that they need.
    int c;

    if (pendingSpaces > 0) {
      pendingSpaces--;
      col++;
      c = (int)' ';
    } else if (pendingLineFeed) {
      pendingLineFeed = false;
      c = (int)'\n';
    } else {
      c = in.read();
      switch (c) {
        case (int)'\r':                // carrige return
          col   = 0;
          oldCh = c;
          switch (mode) {
            case UNIX: c = (int)'\n'; break;
            case DOS:  pendingLineFeed = true;
            default:
          } // end switch
        break;
        case (int)'\n':                // line feed
          if (oldCh == (int)'\r') {
            oldCh = c;
            if (mode != NO_CHANGE) c = read();
          }
          else {
            col   = 0;
            oldCh = c;
            switch (mode) {
              case DOS: pendingLineFeed = true;
              case MAC: c = (int)'\r';
              default:
            } // end switch
          } // end if
        break;
        case (int)'\t':                // tabulator
          pendingSpaces = (col / tabSize + 1) * tabSize - col - 1;
          col++;
          oldCh = c;
          c     = (int)' ';
        break;
        default:                       // all other characters
          col++;
          oldCh = c;
      } // end switch
    } // end if
    return c;
  } // end read

  /**
   * Reads into an array of characters.
   * Blocks until some input is available.
   *
   * @param cbuf The buffer into which the data is read.
   * @param off The start offset of the data.
   * @param len The maximum number of bytes read.
   * @return The actual number of characters read, -1 is
   *         returned when the end of the stream is reached.
   */
  public int read(char cbuf[], int off, int len) throws IOException {
    for (int i=off; i<(off + len); i++) {
      int c = read();

      if (c == -1) return (i - off) == 0 ? -1 : i - off;
      cbuf[i] = (char)c;
    } // end for
    return len;
  } // end read

  /**
   * Skips bytes of input.
   *
   * @param n Characters to be skipped.
   * @return Actual number of bytes skipped.
   */
  public long skip(long n) throws IOException {
    // Can't just read n bytes from 'in' and throw them
    // away, because n bytes from 'in' doesn't necessarily
    // correspond to n bytes from 'this'.
    for (int i=1; i <= n; i++) {
      int c = read();

      if (c == -1) return i - 1;
    } // end for
    return n;
  } // end skip

  /**
   * Set the mode for handling line separators.
   *
   * @param mode One of the line separator mode constants.
   */
  public void setLineSeparator(int mode) {
    if ((mode < 0) || (mode > PLATFORM))
      throw new IllegalArgumentException();
    if (mode == PLATFORM) this.mode = platformMode;
      else this.mode = mode;
  } // end setLineSeparator

  /**
   * Set the tabulator size.
   *
   * @param tabSize The tabulator size.
   */
  public void setTabSize(int tabSize) {
    if (tabSize < 1) throw new IllegalArgumentException();
    this.tabSize = tabSize;
  } // end setTabSize

} // end TabReader

/* ******************************************************************
   end of file
*********************************************************************/
