/* ******************************************************************
   class    : TabWriter
*********************************************************************/

package ModelExplorer.Editor.Streams;

import java.io.*;

/**
 *
 * A class that replaces a number of space characters by a
 * tab character and modifies the line separators.
 */
public class TabWriter extends FilterWriter {

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
  /** True if tab characters are used in the output stream. */
  protected boolean useTabs;
  /** One element array to speed up the single character write operation. */
  private char[] aOneChar = new char[1];
  /** The column number. */
  protected int col = 0;
  /** The character readed at the last read operation. */
  protected char oldCh = (char)0;
  /** Number spaces that must be written before the next non white char. */
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
   * Constructs a new TabWriter initialized with the specified writer.
   *
   * @param out The writer.
   */
  public TabWriter(Writer out) {
    this(out,8);
  } // end constructor TabWriter

  /**
   * Constructs a new TabWriter initialized with the specified
   * writer and tabulator size.
   *
   * @param out The writer.
   * @param tabSize The tabulator size.
   */
  public TabWriter(Writer out, int tabSize) {
    this(out,tabSize,true);
  } // end constructor TabWriter

  /**
   * Constructs a new TabWriter initialized with the specified
   * writer and tabulator size.
   *
   * @param out The writer.
   * @param tabSize The tabulator size.
   * @param useTabs True if tabulator chars should be used.
   */
  public TabWriter(Writer out, int tabSize, boolean useTabs) {
    this(out,tabSize,useTabs,NO_CHANGE);
  } // end constructor TabWriter

  /**
   * Constructs a new TabWriter initialized with the specified
   * writer, tabulator size and line separator mode.
   *
   * @param out The writer.
   * @param tabSize The tabulator size.
   * @param useTabs True if tabulator chars should be used.
   * @param mode One of the line separator mode constants.
   */
  public TabWriter(Writer out, int tabSize, boolean useTabs, int mode) {
    super(out);
    setTabSize(tabSize);
    setTabSupport(useTabs);
    setLineSeparator(mode);
  } // end constructor TabWriter

  // class methods
  /** Only for debuging purposes */
  public static void main(String[] args) throws IOException {
    InputStreamReader reader = new InputStreamReader(System.in);
    TabWriter filter;
    int nch,m;
    char[] cb    = new char[64];
    boolean tabs = (args.length < 3) || args[2].toLowerCase().equals("t");

    if (args.length >= 2) {
      nch = Integer.parseInt(args[0]);
      switch (args[1].toLowerCase().charAt(0)) {
        case 'p': m = PLATFORM; break;
        case 'u': m = UNIX;     break;
        case 'm': m = MAC;      break;
        case 'd': m = DOS;      break;
        default:  m = NO_CHANGE;
      }
      filter = new TabWriter(new OutputStreamWriter(System.out),nch,tabs,m);
    } else if (args.length == 1) {
      nch = Integer.parseInt(args[0]);
      filter = new TabWriter(new OutputStreamWriter(System.out),nch);
    } else {
      filter = new TabWriter(new OutputStreamWriter(System.out));
    } // end if
    while ((nch = reader.read(cb,0,cb.length)) != -1) {
      filter.write(cb,0,nch);
    } // end while
    filter.close();
  } // end main

  // methods
  /**
   * Write a single character.
   */
  public void write(int c) throws IOException {
    aOneChar[0] = (char)c;
    write(aOneChar,0,1);
  } // end write

  /**
   * Write a portion of an array of characters.
   *
   * @param cbuf Buffer of characters.
   * @param off Offset from which to start writing characters.
   * @param len Number of characters to write.
   */
  public void write(char cbuf[], int off, int len) throws IOException {
    // This is the routine that really implements the special
    // functionality of this class; the others just call this
    // one to get the data that they need.
    int wp = off;
    int spaces;

    for (int i=off; i<(off + len); i++) {
      switch (cbuf[i]) {
        case '\r':                     // carrige return
        case '\n':                     // line feed
          col           = 0;
          pendingSpaces = 0;           // skip trailing whitechars
          out.write(cbuf,wp,i - wp);
          writeLineSepar(cbuf[i],oldCh);
          wp = i + 1;
        break;
        case '\t':                     // tabulator
          if (wp < i) {
            out.write(cbuf,wp,i - wp);
            wp = i + 1;
          }
          else {
            wp++;
          } // end if
          spaces = (col / tabSize + 1) * tabSize - col;
          pendingSpaces += spaces;
          col           += spaces;
        break;
        case ' ':                      // blankspace
        case (char)160:
          if (wp < i) {
            out.write(cbuf,wp,i - wp);
            wp = i + 1;
          }
          else {
            wp++;
          } // end if
          pendingSpaces++;
          col++;
        break;
        default:                       // all other characters
          if (pendingSpaces > 0) {
            writeBlankSpace(pendingSpaces,col);
            pendingSpaces = 0;
          } // end if
          col++;
      } // end switch
      oldCh = cbuf[i];
    } // end for
    out.write(cbuf,wp,len - wp);
  } // end write

  /** Write a line separator to the output stream. */
  private final void writeLineSepar(char curChar, char oldChar)
    throws IOException {
    if (curChar == '\r') {             // carrige return
      switch (mode) {
        case UNIX: out.write((int)'\n'); break;
        case DOS:
          out.write((int)curChar);
          out.write((int)'\n');
        break;
        default:
          out.write((int)curChar);
      } // end switch
    }
    else {                             // line feed
      //Utils.Assert.assert(Utils.Assert.ENABLED &&(curChar == '\n'));
      if (oldChar == '\r') {
         if (mode == NO_CHANGE) out.write((int)curChar);
      }
      else {
        switch (mode) {
          case MAC: out.write((int)'\r'); break;
          case DOS: out.write((int)'\r');
          default: out.write((int)curChar);
        } // end switch
      } // end if
    } // end if
  } // end writeLineSepar

  /** Write blank spaces or tab characters to the output stream. */
  private final void writeBlankSpace(int spaces, int column)
    throws IOException {
    if (useTabs && (spaces != 1)) {
      int pos = column - spaces;
      int tmp = (pos / tabSize + 1) * tabSize - pos;

      while ((pos + tmp) <= column) {
        // if the tab would only jump one position, write a space instead.
        out.write((int)(tmp == 1 ? ' ' : '\t'));
        pos += tmp;
        tmp = tabSize;
      } // end while
      while (pos < column) {
        out.write((int)' ');
        pos++;
      } // end while
    }
    else {
      char[] tmp = new char[spaces];

      for (int i=0; i<spaces; i++) tmp[i] = ' ';
      out.write(tmp,0,spaces);
    } // end if
  } // end writeBlankSpaces

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

  /**
   * Spezifies if tabulator chars should be used in the output
   * stream.
   *
   * @param enbl True if tab chars should be used.
   */
  public void setTabSupport(boolean enbl) {
    useTabs = enbl;
  } // end setTabSupport

} // end TabWriter

/* ******************************************************************
   end of file
*********************************************************************/
