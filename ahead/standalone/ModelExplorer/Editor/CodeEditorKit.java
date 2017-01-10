/* ******************************************************************
   class      : CodeEditorKit
*********************************************************************/

package ModelExplorer.Editor;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;

import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * This is the set of things needed by a text component
 * to be a reasonably functioning editor for some type
 * of text document.  This implementation provides a default
 * implementation which treats text as programming source code and
 * provides a extended set of actions for editing source code.
 *
 */
public class CodeEditorKit extends DefaultEditorKit {

  // class variable declarations
  /**
   * Name of the action to delete a tab character from
   * the document.  If there is a selection, a block unindent
   * action is executed.
   * @see DeletePrevTabAction
   * @see #getActions
   */
  public static final String deletePrevTabAction = "delete-tab";

  /**
   * Name of the Action for deleteing a line around the caret.
   * @see DeleteLineAction
   * @see #getActions
   */
  public static final String deleteLineAction = "delete-line";

  /**
   * Name of the action to page up vertically.
   * @see PageUpAction
   * @see #getActions
   */
  public static final String selectionPageUpAction = "selection-page-up";

  /**
   * Name of the action to page down vertically.
   * @see PageDownAction
   * @see #getActions
   */
  public static final String selectionPageDownAction = "selection-page-down";

  /**
   * The document property that spezifies the tabulator size.
   */
  public static final String TABSIZE_PROPERTY = "tabSize";

  /**
   * The document property that spezifies if the document had been changed.
   */
  public static final String CHANGED_PROPERTY = "changed";

  /**
   * The document property that spezifies the syntax coloring class.
   */
  public static final String COLORING_PROPERTY = "syntaxColoringClass";

  private static Action selectLine;
  private static Action deleteNextChar;
  private static final ViewFactory defaultFactory = new CodeViewFactory();
  private static final Action[] defaultActions    = {
    new PageUpAction(pageUpAction,false),
    new PageDownAction(pageDownAction,false),
    new PageUpAction(selectionPageUpAction,true),
    new PageDownAction(selectionPageDownAction,true),
    new InsertBreakAction(),
    new InsertTabAction(),
    new DeletePrevTabAction(),
    new DeleteLineAction()
  };


  // constructors
  public CodeEditorKit() {
    super();
    Action[] actions = super.getActions();
    String nm;

    for (int i=0; i<actions.length; i++) {
      nm = (String)actions[i].getValue(Action.NAME);
      if (nm.equals(DefaultEditorKit.selectLineAction))
        selectLine = actions[i];
      if (nm.equals(DefaultEditorKit.deleteNextCharAction))
        deleteNextChar = actions[i];
    } // end for
  } // end constructor CodeEditorKit

  // methods
  // --- EditorKit methods ---------------------------

  /**
   * Create a copy of the editor kit.  This
   * allows an implementation to serve as a prototype
   * for others, so that they can be quickly created.
   */
  public Object clone() {
    return new CodeEditorKit();
  } // end clone

  /**
   * Fetches the command list for the editor.  This is
   * the list of commands supported by the superclass
   * augmented by the collection of commands defined
   * locally.
   *
   * @return The command list.
   */
  public Action[] getActions() {
    return TextAction.augmentList(super.getActions(),this.defaultActions);
  } // end getActions

  /**
   * Creates an uninitialized text storage model
   * that is appropriate for this type of editor.
   *
   * @return The model.
   */
  public Document createDefaultDocument() {
    Document doc = new PlainDocument();

    doc.putProperty(CHANGED_PROPERTY,Boolean.FALSE);
    return doc;
  } // end createDefaultDocument

  /**
   * Called when the kit is being installed into
   * a JEditorPane.
   *
   * @param c The JEditorPane.
   */
  public void install(JEditorPane c) {
  } // end install

  /**
   * Called when the kit is being removed from the
   * JEditorPane.  This is used to unregister any
   * listeners that were attached.
   *
   * @param c the JEditorPane
   */
  public void deinstall(JEditorPane c) {
  } // end deinstall

  /**
   * Fetches a factory that is suitable for producing
   * views of any models that are produced by this
   * kit.  This is implemented to return View implementations
   * for the following kinds of elements:
   * <ul>
   * <li>AbstractDocument.ContentElementName
   * <li>AbstractDocument.ParagraphElementName
   * <li>AbstractDocument.SectionElementName
   * </ul>
   *
   * @return the factory
   */
  public ViewFactory getViewFactory() {
    return defaultFactory;
  } // end getViewFactory

  // ---- default ViewFactory implementation ---------------------

  static class CodeViewFactory implements ViewFactory {

    // instance variable declarations
    private String className;
    private Class clazz;

    // methods
    public View create(Element elem) {
      Document doc = elem.getDocument();
      Object obj   = doc.getProperty(COLORING_PROPERTY);
      String str;
      View view;

      if (obj == null) return new PatchedPlainView(elem);
      str = obj.toString();
      try {
        if (!str.equals(className)) {
				clazz     = Class.forName("ModelExplorer.Editor."+str);
				className = "ModelExplorer.Editor."+str;
        } // end if
        view = new CodeView(elem,clazz);
      } catch (Exception excep) {
        System.err.println("CodeEditorKit: Exception on creating a " +
                           "document view with syntax coloring \"" + str +
                           "\": " + excep);
        view = new PatchedPlainView(elem);
      }
      return view;
   } // end create

  } // end CodeViewFactory

  // --- Action implementations ---------------------------------

  public static class PageUpAction extends TextAction {

    // instance variable declarations
    private boolean select;

    // constructors
    PageUpAction(String nm, boolean select) {
      super(nm);
      this.select = select;
    } // end constructor PageUpAction

    // methods
    /** The operation to perform when this action is triggered. */
    public void actionPerformed(ActionEvent e) {
      JTextComponent target = getTextComponent(e);
      JScrollPane scroller  = getScroller(target);

      if ((target != null) && (scroller != null)) {
        Dimension d  = scroller.getViewport().getSize();
        int pageIncr = target.getScrollableBlockIncrement(new Rectangle(0,0,
                         d.width,d.height),SwingConstants.VERTICAL,
                         getDirection());

        try {
          Rectangle rect = target.modelToView(target.getCaretPosition());
          Point point    = new Point(rect.x,rect.y);

          point.y += pageIncr*getDirection();
          if (select)
            target.moveCaretPosition(target.viewToModel(point));
          else
            target.setCaretPosition(target.viewToModel(point));
          // end if
        } catch (BadLocationException excep) {
          excep.printStackTrace();
        }
      }
      else {
        Toolkit.getDefaultToolkit().beep();
      } // end if
    } // end actionPerformed

    /**
     * Find the hosting JScrollPane.
     */
    protected final JScrollPane getScroller(Component comp) {
      if (comp == null) return null;
      for (Container p = comp.getParent(); p != null; p = p.getParent()) {
        if (p instanceof JScrollPane) {
          return (JScrollPane)p;
        } // end if
      } // end for
      return null;
    } // end getScroller

    /**
     * Returns -1 for up direction and 1 for down direction
     */
    protected int getDirection() { return -1; }

  } // end PageUpAction

  public static class PageDownAction extends PageUpAction {

    // constructors
    PageDownAction(String nm, boolean select) {
      super(nm,select);
    } // end constructor PageDownAction

    // methods
    /**
     * Returns -1 for up direction and 1 for down direction
     */
    protected int getDirection() { return 1; }

  } // end PageDownAction

  /**
   * Place a line/paragraph break into the document.
   * If there is a selection, it is removed before
   * the break is added.
   *
   * @see #getActions
   */
  public static class InsertBreakAction extends TextAction {

    // constructors
    /** Create this object with the appropriate identifier. */
    InsertBreakAction() {
      super(insertBreakAction);
    } // end constructor InsertBreakAction

    // methods
    /** The operation to perform when this action is triggered. */
    public void actionPerformed(ActionEvent e) {
      JTextComponent target = getTextComponent(e);
      boolean beep          = true;
      String insertStr      = "";

      if (target != null) {
        try {
          Caret caret      = target.getCaret();
          int dot          = caret.getDot();
          int mark         = caret.getMark();
          int pos          = mark < dot ? mark : dot;
          int p0           = InsertTabAction.getHomePos(target,pos);
          int p1           = pos;
          String str       = target.getText(p0,p1 - p0);

          // search first occurence of a non whitechar character
          pos -= p0;
          p0   = 0;
          p1   = 0;
          while (p1 < str.length()) {
            if ((str.charAt(p1) != '\t') &&
                (str.charAt(p1) != ' ')) break;
            p1++;
          } // end while
          if (p1 < str.length()) { // if this line has not only whitechars
            insertStr = str.substring(p0,p1);
          }
          else {
            if ((pos >= p0) && (pos <= p1)) insertStr = str.substring(p0,pos);
          } // end if
          beep = false;
        } catch (BadLocationException excep) {
          insertStr = "";
        }
      } // end if
      insertStr = "\n" + insertStr;
      target.replaceSelection(insertStr);
      if (beep) Toolkit.getDefaultToolkit().beep();
    } // end actionPerformed

  } // end InsertBreakAction

  /**
   * Place a tab character into the document. If there
   * is a selection, it is removed before the tab is added.
   *
   * @see #getActions
   */
  public static class InsertTabAction extends TextAction {

    // instance variable declarations
    private Position endPos;
    private int startOffs;
    private boolean inverseSel;

    // constructors
    /** Create this object with the appropriate identifier. */
    InsertTabAction() {
      super(insertTabAction);
    } // end constructor InsertTabAction

    /** Create this object with the appropriate identifier. */
    InsertTabAction(String nm) {
      super(nm);
    } // end constructor InsertTabAction

    // methods
    /** The operation to perform when this action is triggered. */
    public void actionPerformed(ActionEvent e) {
      JTextComponent target = getTextComponent(e);

      if (target != null) {
        String str   = target.getSelectedText();
        Document doc = target.getDocument();
        Caret caret  = target.getCaret();
        int dot      = caret.getDot();
        int mark     = caret.getMark();
        int p0       = dot < mark ? dot : mark;
        int p0Home;
        int tabSize,leadingTabSize;
        StringBuffer indentStr = new StringBuffer();

        rememberSelPos(doc,dot,mark);
        // calculate the home position
        p0Home = getHomePos(target,p0);
        // read the tab size from the document properties
        tabSize = getTabSize(doc);
        leadingTabSize = tabSize - ((p0 - p0Home) % tabSize);
        for (int i=0; i<leadingTabSize; i++) indentStr.append(' ');
        if ((str == null) || (str.length() < 1)) { // if nothing is selected
          target.replaceSelection(indentStr.toString());
        }
        else {
          StringBuffer theTabChars = new StringBuffer(tabSize);
          char ch;
          char oldChar = ' ';

          for (int i=0; i<tabSize; i++) theTabChars.append(' ');
          for (int i=0; i<str.length(); i++) {
            ch = str.charAt(i);
            if ((oldChar == '\n') && (ch != '\r')) {
              indentStr.append(theTabChars);
              indentStr.append(ch);
            } else {
              indentStr.append(ch);
            } // end if
            oldChar = ch;
          } // end for
          target.replaceSelection(indentStr.toString());
          reSelect(target);
        } // end if
      } // end if
    } // end actionPerformed

    /** Read the tab size from the document properties. */
    protected int getTabSize(Document d) {
      int s;

      try {
        s = Integer.parseInt(d.getProperty(TABSIZE_PROPERTY).toString());
      } catch (NumberFormatException excep) {
        s = 2;
      }
      return s;
    } // end getTabSize

    /**
     * Determine the position in the model of the row start in the view
     * that the given model position resides.
     */
    private static final int getRowStart(JTextComponent c, int offs)
      throws BadLocationException {
      Rectangle r  = c.modelToView(offs);
      int lastOffs = offs;
      int y        = r.y;

      while ((r != null) && (y == r.y)) {
        offs = lastOffs;
        lastOffs -= 1;
        r = (lastOffs >= 0) ? c.modelToView(lastOffs) : null;
      } // end while
      return offs;
    } // end getRowStart

    /** Calculate the home position. */
    static final int getHomePos(JTextComponent t, int p) {
      int hp;

      try {
        hp = getRowStart(t,p);
      } catch (BadLocationException excep) {
        hp = p;
        Toolkit.getDefaultToolkit().beep();
      }
      return hp;
    } // end getHomePos

    /** Remembers the current selection for later use */
    protected void rememberSelPos(Document doc, int d, int m) {
      inverseSel = (d < m);

      if (inverseSel) {
        int tmp = d;

        d = m;
        m = tmp;
      } // end if
      try {
        endPos = doc.createPosition(d);
      } catch (BadLocationException excep) {
        excep.printStackTrace();
      }
      startOffs = m;
    } // end rememberSelPos

    /** Reselects the altered area */
    protected void reSelect(JTextComponent t) {
      int start,end;

      if (inverseSel) {
        start = endPos.getOffset();
        end   = startOffs;
      }
      else {
        start = startOffs;
        end   = endPos.getOffset();
      } // end if
      t.setCaretPosition(start);
      t.moveCaretPosition(end);
    } // end reSelect

  } // end InsertTabAction

  /**
   * Remove a tab character from the document. If there
   * is a selection, make a block unindent action.
   *
   * @see #getActions
   */
  public static class DeletePrevTabAction extends InsertTabAction {

    // instance variable declarations
    private int tabSize;

    // constructors
    /** Create this object with the appropriate identifier. */
    DeletePrevTabAction() {
      super(deletePrevTabAction);
    } // end constructor DeletePrevTabAction

    // methods
    /** The operation to perform when this action is triggered. */
    public void actionPerformed(ActionEvent e) {
      JTextComponent target = getTextComponent(e);

      if (target != null) {
        String str   = target.getSelectedText();
        Document doc = target.getDocument();
        Caret caret  = target.getCaret();
        int dot      = caret.getDot();
        int mark     = caret.getMark();
        int p0       = dot < mark ? dot : mark;
        int p0Home;
        int leadingTabSize;
        StringBuffer indentStr = new StringBuffer();

        rememberSelPos(doc,dot,mark);
        // calculate the home position
        p0Home = getHomePos(target,p0);
        // read the tab size from the document properties
        tabSize = getTabSize(doc);
        leadingTabSize = firstNonWhiteChar(str);
        if (leadingTabSize < 0) leadingTabSize = 0;
        leadingTabSize = (p0 + leadingTabSize - p0Home) % tabSize;
        if (leadingTabSize == 0) leadingTabSize = tabSize;
        if ((str == null) || (str.length() < 1)) { // if nothing is selected
          Toolkit.getDefaultToolkit().beep();
        }
        else {
          StringTokenizer strTok = new StringTokenizer(str,"\n\r",true);
          int ts                 = leadingTabSize;

          while (strTok.hasMoreElements()) {
            str = strTok.nextToken();
            if (!str.equals("\n") && !str.equals("\r")) {
              str = removeTab(str,ts);
              if (str == null) {
                Toolkit.getDefaultToolkit().beep();
                return;
              } // end if
              ts = tabSize;
            } // end if
            indentStr.append(str);
          } // end while
          target.replaceSelection(indentStr.toString());
          reSelect(target);
        } // end if
      } // end if
    } // end actionPerformed

    /** Returns the index of the first non white char character in s. */
    protected final int firstNonWhiteChar(String s) {
      int i = 0;
      int l = s.length();

      while ((i < l) && ((s.charAt(i) == ' ') || (s.charAt(i) == '\t'))) i++;
      return i == l ? -1 : i;
    } // end firstNonWhiteChar

    /** Removes the leading tab with size t from string s. */
    protected final String removeTab(String s, int t) {
      int textStart = firstNonWhiteChar(s);
      int i         = textStart;
      int removed   = 0;
      String str;

      while ((removed < t) && (i > 0)) {
        i--;
        if (s.charAt(i) == ' ') removed++;
          else removed += tabSize;
      } // end while
      if (removed < t) return null;
      str = s.substring(0,i);
      while (removed > t) { str += " "; removed--; }
      str += s.substring(textStart);
      return str;
    } // end removeTab

  } // end DeletePrevTabAction

  /**
   * Delete the line around the caret.
   *
   * @see #getActions
   */
  static class DeleteLineAction extends TextAction {

    // constructors
    /** Create this object with the appropriate identifier. */
    DeleteLineAction() {
      super(deleteLineAction);
    } // end constructor

    /** The operation to perform when this action is triggered. */
    public void actionPerformed(ActionEvent e) {
      selectLine.actionPerformed(e);
      deleteNextChar.actionPerformed(e);
      deleteNextChar.actionPerformed(e);
    } // end actionPerformed

  } // end DeleteLineAction

} // end CodeEditorKit

/* ******************************************************************
   end of file
*********************************************************************/
