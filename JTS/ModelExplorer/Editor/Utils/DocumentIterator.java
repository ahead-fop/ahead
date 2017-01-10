/* ******************************************************************
   class    : DocumentIterator
******************************************************************* */

package ModelExplorer.Editor.Utils;

import java.text.CharacterIterator;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;

/**
 * CharacterIterator iterating over a javax.swing.text.Document.
 */
public class DocumentIterator implements CharacterIterator {

  // class variable declarations
  /** The number of right shifts to get the segment number. */
  protected static int SEG_SHIFT = 12;
  /** The size of the text buffer. */
  protected static int SEG_SIZE = 1 << SEG_SHIFT; // 4096

  // instance variable declarations
  /** The document to iterate over. */
  protected Document doc;
  /** The position of the iterator. */
  protected int pos;
  /** The offset of the current loaded part of the document. */
  protected int offset;
  /** The segment number of the current loaded part of the document. */
  protected int seg;
  /** The text buffer. */
  protected char[] buf;

  // constructors
  /** Create a new DocumentIterator object. */
  public DocumentIterator(Document doc) {
    this(doc,0);
  } // end constructor DocumentIterator

  /**
   * Create a new DocumentIterator object.
   *
   * @param doc The document.
   * @param pos The initial position of the iterator.
   */
  public DocumentIterator(Document doc, int pos) {
    this.doc = doc;
    try {
      loadSegmentForPosition(pos);
    } catch (BadLocationException excep) {
      throw new IllegalArgumentException();
    }
  } // end constructor DocumentIterator

  // methods
  /**
   * Sets the position to getBeginIndex() and returns the character at
   * that position.
   *
   * @return The first character in the text, or DONE if the text is empty.
   */
  public char first() {
    return setIndex(0);
  } // end first

  /**
   * Sets the position to getEndIndex()-1 (getEndIndex() if the text is empty)
   * and returns the character at that position.
   *
   * @return The last character in the text, or DONE if the text is empty.
   */
  public char last() {
    if (doc.getLength() < 1) return DONE;
    return setIndex(doc.getLength() - 1);
  } // end last

  /**
   * Gets the character at the current position (as returned by getIndex()).
   *
   * @return the character at the current position or DONE if the current
   *         position is off the end of the text.
   */
  public char current() {
    int i = pos - offset;

    if (i < buf.length) return buf[i];
    return DONE;
  } // end current

  /**
   * Increments the iterator's index by one and returns the character at the
   * new index. If the resulting index is greater or equal to getEndIndex(),
   * the current index is reset to getEndIndex() and a value of DONE is
   * returned.
   *
   * @return The character at the new position or DONE if the new position is
   *         off the end of the text range.
   */
  public char next() {
    int i = pos - offset + 1;

    if (i < buf.length) {
      pos++;
      return buf[i];
    } // end if
    if (pos >= buf.length - 1) return DONE;
    // assert that new position is in next segment
    try {
      loadSegmentForPosition(pos + 1);
    } catch (BadLocationException excep) {
      excep.printStackTrace();
    }
    return buf[0];
  } // end next

  /**
   * Decrements the iterator's index by one and returns the character at the
   * new index. If the current index is getBeginIndex(), the index remains at
   * getBeginIndex() and a value of DONE is returned.
   *
   * @return The character at the new position or DONE if the current position
   *         is equal to getBeginIndex().
   */
  public char previous() {
    int i = pos - offset - 1;

    if (i >= 0) {
      pos--;
      return buf[i];
    } // end if
    if (pos <= 0) return DONE;
    // assert that new position is in previous segment
    try {
      loadSegmentForPosition(pos - 1);
    } catch (BadLocationException excep) {
      excep.printStackTrace();
    }
    return buf[pos - offset];
  } // end previous

  /**
   * Sets the position to the specified position in the text and returns that
   * character.
   *
   * @param position The position within the text. Valid values range from
   *                 getBeginIndex() to getEndIndex(). An
   *                 IllegalArgumentException is thrown if an invalid value is
   *                 supplied.
   * @return The character at the specified position or DONE if the specified
   *         position is equal to getEndIndex().
   */
  public char setIndex(int position) {
    int s = position >> SEG_SHIFT;
    int i;

    if (seg != s) try {
      loadSegmentForPosition(position);
    } catch (BadLocationException excep) {
      throw new IllegalArgumentException();
    }
    i = position - offset;
    if (i >= buf.length) throw new IllegalArgumentException();
    return buf[i];
  } // end setIndex

  /**
   * Returns the start index of the text.
   *
   * @return The index at which the text begins.
   */
  public int getBeginIndex() {
    return 0;
  } // end getBeginIndex

  /**
   * Returns the end index of the text. This index is the index of the first
   * character following the end of the text.
   *
   * @return The index after the last character in the text.
   */
  public int getEndIndex() {
    return doc.getLength();
  } // end getEndIndex

  /**
   * Returns the current index.
   *
   * @return The current index.
   */
  public int getIndex() {
    return pos;
  } // end getIndex

  /**
   * Create a copy of this iterator.
   *
   * @return A copy of this.
   */
  public Object clone() {
    return new DocumentIterator(doc,pos);
  } // end clone

  private final void loadSegmentForPosition(int _pos) throws BadLocationException {
    int _seg    = _pos >> SEG_SHIFT;
    int _offset = _seg << SEG_SHIFT;
    int size    = doc.getLength() - _offset;

    if (size > SEG_SIZE) size = SEG_SIZE;
    buf    = doc.getText(_offset,size).toCharArray();
    pos    = _pos;
    seg    = _seg;
    offset = _offset;
  } // end loadSegmentForPosition

} // end DocumentIterator

/* ******************************************************************
   end of file
******************************************************************* */
