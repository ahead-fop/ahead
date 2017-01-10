/* ******************************************************************
   class      : JCodePane
   description: a complete code editor with syntax coloring and
                an extended command set
*********************************************************************/

package ModelExplorer.Editor;
import ModelExplorer.Editor.Utils.DocumentIterator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.InputMethodEvent;
import java.io.*;
import java.util.Hashtable;
import java.text.BreakIterator;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.plaf.*;

public class JCodePane extends JEditorPane
  implements UndoableEditListener, KeyListener {

  // class variable declarations
  private static final String WORD_SEPARATORS = " \t\n\r.()[]";

  // instance variable declarations
  private Hashtable commands;
  private Keymap keymap;
  private Segment tmpSegment = new Segment();

  // constructors
  /**
   * Constructs a new JCodePane.
   */
  public JCodePane() {
    super();

    Action action;
    Action actions[];
    KeyStroke ks;
    KeyStroke keystrokes[];
    EditorKit ek = new CodeEditorKit();

    setEditorKit(ek);
    keymap = getKeymap();
    // load editor specific actions and assign keystrokes
    actions  = ek.getActions();
    commands = new Hashtable();
    for (int i = 0; i < actions.length; i++) {
      Action a = actions[i];
      commands.put(a.getValue(Action.NAME),a);
    } // end for
    // document home and end actions
    ks     = KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_HOME,0);
    action = keymap.getAction(ks);
    if (action != null) keymap.removeKeyStrokeBinding(ks);
    assignKeyStroke(DefaultEditorKit.beginAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_HOME,
                             java.awt.event.InputEvent.CTRL_MASK));
    ks     = KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_END,0);
    action = keymap.getAction(ks);
    if (action != null) keymap.removeKeyStrokeBinding(ks);
    assignKeyStroke(DefaultEditorKit.endAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_END,
                             java.awt.event.InputEvent.CTRL_MASK));
    assignKeyStroke(DefaultEditorKit.selectionBeginAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_HOME,
                             java.awt.event.InputEvent.CTRL_MASK |
                             java.awt.event.InputEvent.SHIFT_MASK));
    assignKeyStroke(DefaultEditorKit.selectionEndAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_END,
                             java.awt.event.InputEvent.CTRL_MASK |
                             java.awt.event.InputEvent.SHIFT_MASK));
    // home and end actions
    assignKeyStroke(CodeEditorKit.beginLineAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_HOME,0));
    assignKeyStroke(DefaultEditorKit.selectionBeginLineAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_HOME,
                             java.awt.event.InputEvent.SHIFT_MASK));
    assignKeyStroke(DefaultEditorKit.endLineAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_END,0));
    assignKeyStroke(DefaultEditorKit.selectionEndLineAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_END,
                             java.awt.event.InputEvent.SHIFT_MASK));
    // line up and down actions
    assignKeyStroke(DefaultEditorKit.selectionUpAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP,
                             java.awt.event.InputEvent.SHIFT_MASK));
    assignKeyStroke(DefaultEditorKit.selectionDownAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN,
                             java.awt.event.InputEvent.SHIFT_MASK));
    // word backward and forward actions
    assignKeyStroke(DefaultEditorKit.nextWordAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT,
                             java.awt.event.InputEvent.CTRL_MASK));
    assignKeyStroke(DefaultEditorKit.selectionNextWordAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT,
                             java.awt.event.InputEvent.CTRL_MASK |
                             java.awt.event.InputEvent.SHIFT_MASK));
    assignKeyStroke(DefaultEditorKit.previousWordAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT,
                             java.awt.event.InputEvent.CTRL_MASK));
    assignKeyStroke(DefaultEditorKit.selectionPreviousWordAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT,
                             java.awt.event.InputEvent.CTRL_MASK |
                             java.awt.event.InputEvent.SHIFT_MASK));
    // page up and down action
    assignKeyStroke(CodeEditorKit.selectionPageUpAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PAGE_UP,
                             java.awt.event.InputEvent.SHIFT_MASK));
    assignKeyStroke(CodeEditorKit.selectionPageDownAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PAGE_DOWN,
                             java.awt.event.InputEvent.SHIFT_MASK));
    // delete line action
    assignKeyStroke(CodeEditorKit.deleteLineAction,
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y,
                             java.awt.event.InputEvent.CTRL_MASK));
    // tabulator actions
    action = (Action)commands.get(DefaultEditorKit.insertTabAction);
    if (action != null) keystrokes = keymap.getKeyStrokesForAction(action);
      else keystrokes = null;
    if ((keystrokes != null) && (keystrokes.length > 0)) {
      ks = keystrokes[0];
      ks = KeyStroke.getKeyStroke(ks.getKeyCode(),ks.getModifiers() ^
                                  java.awt.event.InputEvent.SHIFT_MASK);
    }
    else {
      ks = KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_TAB,
                                  java.awt.event.InputEvent.SHIFT_MASK);
    } // end if
    assignKeyStroke(CodeEditorKit.deletePrevTabAction,ks);
    // cut, copy and paste actions
    String osName = System.getProperty("os.name");

    if ((osName != null) && (osName.toLowerCase().indexOf("win") >= 0)) {
      // Unfortunatly if this is a windows machine add the old keystrokes
      assignKeyStroke(DefaultEditorKit.cutAction,
        KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE,
                               java.awt.event.InputEvent.SHIFT_MASK));
      assignKeyStroke(DefaultEditorKit.copyAction,
        KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_INSERT,
                               java.awt.event.InputEvent.CTRL_MASK));
      assignKeyStroke(DefaultEditorKit.pasteAction,
        KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_INSERT,
                               java.awt.event.InputEvent.SHIFT_MASK));
    } // end if
    // garbage collect
    keymap   = null;
    commands = null;
    addKeyListener(this);
  } // end constructor JCodePane

  /**
   * Constructs a new JCodePane, with a specified document model.
   *
   * @param doc the document model
   */
  public JCodePane(Document doc) {

    this();
    setDocument(doc);
  } // end constructor JCodePane

  // methods
  /**
   * Associates the editor with a text document. The currently registered
   * factory is used to build a view for the document, which gets
   * displayed by the editor.
   */
  public void setDocument(Document doc) {
    Object obj;

    obj = doc.getProperty(CodeEditorKit.CHANGED_PROPERTY);
    if (obj != null)
      doc.putProperty(CodeEditorKit.CHANGED_PROPERTY,Boolean.FALSE);
    doc.addUndoableEditListener(this);
    super.setDocument(doc);
  } // end setDocument

  /**
   * Assign a KeyStroke to an Action.
   */
  private void assignKeyStroke(String cmd, KeyStroke ks) {
    Object object = commands.get(cmd);

    if (object != null) keymap.addActionForKeyStroke(ks,(Action)object);
  } // end assignKeyStroke

  /**
   * Returns true, if the document assigned to the editor has been changed.
   */
  public boolean hasChanged() {
    Document doc = getDocument();
    Object obj   = doc.getProperty(CodeEditorKit.CHANGED_PROPERTY);
    Boolean b    = obj == null ? Boolean.FALSE : (Boolean)obj;

    return b.booleanValue();
  } // end hasChanged

  /**
   * Replaces the currently selected content with new content
   * represented by the given string.  If there is no selection
   * this amounts to an insert of the given text.  If there
   * is no replacement text this amounts to a removal of the
   * current selection.  The replacement text will have the
   * attributes currently defined for input.
   *
   * @param content The content to replace the selection with.
   */
  public void replaceSelection(String content) {
    if (!isEditable()) {
      getToolkit().beep();
      return;
    } // end if

    Document doc = getDocument();

    if (doc != null) {
      try {
        Caret caret = getCaret();
        int p0      = Math.min(caret.getDot(),caret.getMark());
        int p1      = Math.max(caret.getDot(),caret.getMark());

        if (p0 != p1) doc.remove(p0,p1 - p0);
        // p1 is no longer valid!
        if ((content != null) && (content.length() > 0)) {
          doc.insertString(p0,content,null);
        } // end if
      } catch (BadLocationException excep) {
        getToolkit().beep();
      }
    } // end if
  } // end replaceSelection

  /**
   * Returns the caret position in human readable format.
   */
  public Point getCaretCharPos() {
    int x,y;
    Insets insets;
    Rectangle rect;
    Document doc;
    Element root,line;

    try {
      insets = getMargin();
      if (insets == null) insets = new Insets(0,0,0,0);
      rect = modelToView(getCaret().getDot());
      y    = rect == null ? 0 : (rect.y-insets.top) / rect.height;
      doc  = getDocument();
      root = doc.getRootElements()[0];
      line = root.getElement(y);
      x    = getCaretPosition() - line.getStartOffset();
    } catch (BadLocationException excep) {
      System.err.println(excep);
      x = -1; y = -1;
    }
    return new Point(x,y);
  } // end getCaretCharPos

  /**
   * Search and select a portion of text, if found.
   *
   * @param str The string to search for.
   * @param words Search only full words.
   * @param caseSense Search case sensitive.
   *
   * @return True if the specified text was found.
   */
  public boolean search(String str, boolean words, boolean caseSense) {
    if ((str == null) || (str.equals(""))) return false;

    Document doc  = getDocument();
    int pos       = getCaretPosition();
    int end       = doc.getEndPosition().getOffset();
    DocumentIterator iter = new DocumentIterator(doc);
    char[] aChar;

    if (caseSense) aChar = str.toCharArray();
      else aChar = str.toUpperCase().toCharArray();
    if (words) {
      char ch;

      try {
        ch = doc.getText(pos - 1,1).charAt(0);
      } catch (BadLocationException excep) {
        ch = ' ';
      }
      if (WORD_SEPARATORS.indexOf((int)ch) < 0)
        pos += offsetToNextWord(iter,pos);
      // end if
    } // end if
    while (pos < end) {
      if (match(iter,pos,aChar,caseSense)) {
        break;
      }
      else {
        if (words) pos += offsetToNextWord(iter,pos);
          else pos++;
      } // end if
    } // end while
    if (pos < end) { // if found
		//System.out.println("Start: "+pos+" length: "+aChar.length);
      setCaretPosition(pos);
      moveCaretPosition(pos + aChar.length);
      return true;
    } // end if
    return false;
  } // end search

  /** Matches a part of the document to a given char pattern. */
  private final boolean match(DocumentIterator iter, int pos, char[] chars,
                              boolean caseSense) {
    int len = chars.length;
    char ch;

    if ((iter.getEndIndex() - pos) < len) return false;
    for (int i=0; i<len; i++) {
      ch = caseSense ? iter.setIndex(pos) : Character.toUpperCase(iter.setIndex(pos));
      if (ch != chars[i]) return false;
      pos++;
    } // end for
    return true;
  } // end match

  /** Returns the offset to the beginning of the next word. */
  private final int offsetToNextWord(DocumentIterator iter, int pos) {
    int start = pos;
    int end   = iter.getEndIndex();

    // scip characters until word separators appear
    while ((pos < end) &&
           (WORD_SEPARATORS.indexOf(iter.setIndex(pos)) < 0)) pos++;
    // scip word separator characters
    do {
      pos++;
    } while ((pos < end) &&
             (WORD_SEPARATORS.indexOf(iter.setIndex(pos)) >= 0));
    return pos - start;
  } // end offsetToNextWord

  /**
   * Return the word around the caret.
   */
  public String getWordAtCaretPos() {
    String str;

    try {
      int offs    = getCaretPosition();
      int begOffs = getWordStart(this,offs);
      int endOffs = getWordEnd(this,offs);

      str = getText(begOffs,endOffs-begOffs);
    } catch (BadLocationException excep) {
      getToolkit().beep();
      str = null;
    }
    return str;
  } // end getWordAtCaretPos


  /**
   * Set the caret at the specified line.
   */
  public void goToLine(int lineNr) {
    Document doc = getDocument();
    Element elem = doc.getDefaultRootElement();

    if ((lineNr < 0) || (lineNr >= elem.getElementCount())) {
      Toolkit.getDefaultToolkit().beep();
      return;
    } // end if
    setCaretPosition(elem.getElement(lineNr).getStartOffset());
  } // end goToLine

  /**
   * Determine the start of a word for the given location.
   *
   * @returns the location in the model of the word start.
   */
  private static final int getWordStart(JTextComponent c, int offs)
    throws BadLocationException {
    Document doc  = c.getDocument();
    Element line  = getParagraphElement(c,offs);
    int lineStart = line.getStartOffset();
    int lineEnd   = Math.min(line.getEndOffset(),doc.getLength());
    String s      = doc.getText(lineStart,lineEnd - lineStart);
    if((s != null) && (s.length() > 0)) {
      BreakIterator words = BreakIterator.getWordInstance();
      words.setText(s.replace('.',' '));
      int wordPosition = offs - lineStart;

      if(wordPosition >= words.last()) {
        wordPosition = words.last() - 1;
      } // end if
      words.following(wordPosition);
      offs = lineStart + words.previous();
    } // end if
    return offs;
  } // end getWordStart

  /**
   * Determine the end of a word for the given location.
   *
   * @returns the location in the model of the word end.
   */
  private static final int getWordEnd(JTextComponent c, int offs)
    throws BadLocationException {
    Document doc  = c.getDocument();
    Element line  = getParagraphElement(c,offs);
    int lineStart = line.getStartOffset();
    int lineEnd   = Math.min(line.getEndOffset(),doc.getLength());
    String s      = doc.getText(lineStart,lineEnd - lineStart);

    if((s != null) && (s.length() > 0)) {
      BreakIterator words = BreakIterator.getWordInstance();

      words.setText(s.replace('.',' '));
      int wordPosition = offs - lineStart;

      if(wordPosition >= words.last()) {
        wordPosition = words.last() - 1;
      } // end if
      offs = lineStart + words.following(wordPosition);
    } // end if
    return offs;
  } // end getWordEnd

  /**
   * Determine the element to use for a paragraph/line.
   */
  private static final Element getParagraphElement(JTextComponent c, int offs) {
    Document doc      = c.getDocument();
    Element map       = doc.getDefaultRootElement();
    int index         = map.getElementIndex(offs);
    Element paragraph = map.getElement(index);

    return paragraph;
  } // end getParagraphElement

  // --- JEditorPane ------------------------------------

  /**
   * Creates the EditorKit to use by default.  This
   * is implemented to return CodeEditorKit.
   */
  protected EditorKit createDefaultEditorKit() {
    return new CodeEditorKit();
  } // end createDefaultEditorKit

  /**
   * Initialize from a stream. By default this will load the
   * model as plain text.
   */
  public void read(Reader in, Object desc) throws IOException {
    Document doc = getDocument();
    char[] buff  = new char[4096];
    int nch;

    try {
      while ((nch = in.read(buff,0,buff.length)) != -1) {
        doc.insertString(doc.getLength(),new String(buff,0,nch),null);
      } // end while
    } catch (BadLocationException excep) {
      excep.printStackTrace();
    }
    doc.putProperty(CodeEditorKit.CHANGED_PROPERTY,Boolean.FALSE);
    doc.addUndoableEditListener(this);
  } // end read

  /**
   * Stores the contents of the model into the given stream. By
   * default this will store the model as plain text.
   */
  public void write(Writer out) throws IOException {
    Document doc = getDocument();
    Object obj   = doc.getProperty(CodeEditorKit.CHANGED_PROPERTY);
    Boolean b    = obj == null ? Boolean.FALSE : (Boolean)obj;
    int pos      = 0;
    int cnt      = 8192;
    String str;

    // Using a string isn't very efficient, but the version of getText
    // with the Segment did "eat" some characters, which made me very
    // angry.
    while (pos < doc.getLength()) {
      if ((pos + cnt) > doc.getLength()) cnt = doc.getLength() - pos;
      try {
        str = doc.getText(pos,cnt);
        out.write(str.toCharArray(),0,str.length());
      } catch (BadLocationException excep) {
        excep.printStackTrace();
      }
      pos += cnt;
    } // end while

    if (b.booleanValue()) { // if document has been changed
      doc.putProperty(CodeEditorKit.CHANGED_PROPERTY,Boolean.FALSE);
      doc.addUndoableEditListener(this);
    } // end if
  } // end write

  // --- Bugfix non working AltGr key -------------------

  private final static int CTRL_ALT_MASK   = InputEvent.CTRL_MASK |
                                             InputEvent.ALT_MASK;
  private final static int SHIFT_META_MASK = InputEvent.SHIFT_MASK |
                                             InputEvent.META_MASK;

  private static boolean altGrEnabled = false;

  /**
   * Turn the AltGr key bugfix on and off.
   */
  public static void fixAltGrBug(boolean b) {
    altGrEnabled = b;
  } // end fixAltGrBug

  protected void processKeyEvent(KeyEvent e) {
    /*int modifiers = e.getModifiers();

    if (altGrEnabled &&
        ((modifiers & CTRL_ALT_MASK  ) == CTRL_ALT_MASK) &&
        ((modifiers & SHIFT_META_MASK) == 0)) {
      if (isAltGrKey(e.getKeyChar())) {
        e.setModifiers(modifiers & ~CTRL_ALT_MASK);
      } // end if
    } // end if*/
    super.processKeyEvent(e);
  } // end processKeyEvent

  // --- Bugfix accidently inserted mnemonic keys -------

  private static boolean mnemonicEnabled = false;

  private boolean ignoreKey = false;

  /**
   * Turn the mnemonic key bugfix on and off.
   */
  public static void fixMnemonicBug(boolean b) {
    mnemonicEnabled = b;
  } // end fixMnemonicBug

  protected void processInputMethodEvent(InputMethodEvent e) {
    // If the alt+key combo is pressed then don't display the
    // keystroke.  This is a workaround for a JDK 1.2 bug.
    if(mnemonicEnabled) {
      if (!ignoreKey) super.processInputMethodEvent(e);
    }
    else {
      super.processInputMethodEvent(e);
    } // end if
    ignoreKey = false;
  } // end processInputMethodEvent

  //---- KeyListener ------------------------------------------------
  /**
   * Invoked when a key has been typed.  This event occurs when a
   * key press is followed by a key release.
   */
  public void keyTyped(KeyEvent e) {
    // do nothing
  } // end keyTyped

  /** Invoked when a key has been pressed. */
  public void keyPressed(KeyEvent e) {
    // If a alt plus key combo was pressed,
    // then set a flag for processInputMethodEvent().
    if(mnemonicEnabled) {
      if (e.isAltDown() &&
          !e.isControlDown() &&
          e.getKeyCode() != KeyEvent.VK_ALT) {
        ignoreKey = true;
      } // end if
    } // end if
  } // end keyPressed

  /** Invoked when a key has been released. */
  public void keyReleased(KeyEvent e) {
    // do nothing
  } // end keyReleased

  // --- Scrollable -------------------------------------

  /**
   * Returns true if a viewport should always force the width of this
   * Scrollable to match the width of the viewport, false otherwise.
   */
  public boolean getScrollableTracksViewportWidth() {
    return false;
  } // end getScrollableTracksViewportWidth

  // --- UndoableEditListener ---------------------------

  /**
   * Messaged when the Document has created an edit. Set the changed
   * property of the document to true.
   */
  public void undoableEditHappened(UndoableEditEvent e) {
    Document doc = getDocument();

    doc.putProperty(CodeEditorKit.CHANGED_PROPERTY,Boolean.TRUE);
    doc.removeUndoableEditListener(this);
  } // end undoableEditHappened

  public Hashtable getCommands(){
	  return commands;
  }

} // end JCodePane

/* ******************************************************************
   end of file
*********************************************************************/
