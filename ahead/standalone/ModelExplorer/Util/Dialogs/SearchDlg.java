/* ******************************************************************
   class    : SearchDlg
*********************************************************************/

package ModelExplorer.Util.Dialogs;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;

import ModelExplorer.Util.BaseDialog;
import ModelExplorer.Util.SeparatorCanvas;
import ModelExplorer.Util.OkTextField;
import ModelExplorer.Editor.Utils.ResourceUtil;

/**
 * A dialog that allows to specify search and replace options.
 */
public class SearchDlg extends BaseDialog
  implements ActionListener,TextListener {

  // class variable declarations
  /**
   * This constant value indicates that the purpose of the
   * search dialog window is to search a string.
   */
  public final static int SEARCH     = 0;
  /**
   * This constant value indicates that the purpose of the
   * search dialog window is to replace a string.
   */
  public final static int REPLACE    = 1;
  /**
   * This constant value indicates that the purpose of the
   * search dialog window is to search a string by spezifing
   * an extended command set.
   */
  public final static int SEARCH_EX  = 2;
  /**
   * This constant value indicates that the purpose of the
   * search dialog window is to replace a string by spezifing
   * an extended command set.
   */
  public final static int REPLACE_EX = 3;

  private final static int TYPE_MASK  = 1;

  // Keys to the dialog labels.
  private final static String TITLE_SEARCH  = "searchTitle";
  private final static String TITLE_REPLACE = "replaceTitle";
  private final static String BUTTONS_SEAR  = "searchButtons";
  private final static String BUTTONS_REP   = "replaceButtons";
  private final static String LABEL_SEARCH  = "labelSearchFor";
  private final static String LABEL_REPLACE = "labelReplaceWith";
  private final static String CHECKBOX_WORD = "labelWholeWords";
  private final static String CHECKBOX_CASE = "labelCaseSensitive";
  private final static String LABEL_CONTENT = "labelSearchIn";
  private final static String CHECKBOX_CODE = "labelCode";
  private final static String CHECKBOX_LIT  = "labelLiterals";
  private final static String CHECKBOX_COMM = "labelComments";

  // instance variable declarations
  private int mode;
  private ResourceBundle resources;
  private String resourcePrefix;
  private boolean confirm;
  private OkTextField searchTextField;
  private OkTextField replaceTextField;
  private Checkbox wordCheckbox;
  private Checkbox caseCheckbox;
  private Checkbox codeCheckbox;
  private Checkbox literalCheckbox;
  private Checkbox commentCheckbox;

  // constructors
  /**
   * Create a initially modal search dialog box.
   *
   * @param parent The parent frame.
   * @param mode One of the search dialog mode constants.
   * @param resources A resource bundle to look for widget labels
   * @param resourcePrefix A string that acts as prefix for resource keys.
   */
  public SearchDlg(Frame parent, int mode, ResourceBundle resources,
                   String resourcePrefix) {
    super(parent,true);
    boolean replace  = ((mode & TYPE_MASK) == REPLACE);
    boolean extended = ((mode |   REPLACE) == REPLACE_EX);

    this.mode           = mode;
    this.resources      = resources;
    this.resourcePrefix = resourcePrefix;
    confirm             = true;

    Panel panel = getContentPanel();
    Label label;

    if (replace) {
      setTitle(getSetting(TITLE_REPLACE));
      createButtons(getSetting(BUTTONS_REP));
      addActionListener(this);
    }
    else {
      setTitle(getSetting(TITLE_SEARCH));
      createButtons(getSetting(BUTTONS_SEAR));
    } // end if
    // this dialog box uses the gridbaglayout to provide a pretty good layout
    GridBagLayout gridbag          = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    panel.setLayout(gridbag);
    // create the widgets and do the layout
    constraints.insets     = new Insets(10,6,10,6);
    constraints.fill       = GridBagConstraints.NONE;
    constraints.anchor     = GridBagConstraints.WEST;
    constraints.weightx    = 1.0;
    constraints.weighty    = 1.0;
    constraints.gridwidth  = GridBagConstraints.RELATIVE;
    constraints.gridheight = 1;
    label = new Label(getSetting(LABEL_SEARCH));
    gridbag.setConstraints(label,constraints);
    panel.add(label);

    constraints.insets    = new Insets(10,0,10,6);
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    searchTextField = new OkTextField(16);
    searchTextField.addActionListener(this);
    searchTextField.addTextListener(this);
    gridbag.setConstraints(searchTextField,constraints);
    panel.add(searchTextField);

    if (replace) {
      constraints.insets    = new Insets(0,6,10,6);
      constraints.gridwidth = GridBagConstraints.RELATIVE;
      label = new Label(getSetting(LABEL_REPLACE));
      gridbag.setConstraints(label,constraints);
      panel.add(label);

      constraints.insets    = new Insets(0,0,10,6);
      constraints.gridwidth = GridBagConstraints.REMAINDER;
      replaceTextField = new OkTextField(16);
      replaceTextField.addActionListener(this);
      gridbag.setConstraints(replaceTextField,constraints);
      panel.add(replaceTextField);
    } // end if

    constraints.insets     = new Insets(0,6,10,6);
    constraints.gridwidth  = GridBagConstraints.REMAINDER;
    if (!extended) constraints.gridheight = GridBagConstraints.RELATIVE;
    wordCheckbox = new Checkbox(getSetting(CHECKBOX_WORD));
    gridbag.setConstraints(wordCheckbox,constraints);
    panel.add(wordCheckbox);

    if (!extended) constraints.gridheight = GridBagConstraints.REMAINDER;
    caseCheckbox = new Checkbox(getSetting(CHECKBOX_CASE));
    gridbag.setConstraints(caseCheckbox,constraints);
    panel.add(caseCheckbox);

    if (extended) {
      SeparatorCanvas separator = new SeparatorCanvas();

      constraints.insets = new Insets(0,0,10,0);
      constraints.fill   = GridBagConstraints.HORIZONTAL;
      separator = new SeparatorCanvas();
      gridbag.setConstraints(separator,constraints);
      panel.add(separator);

      constraints.insets = new Insets(0,6,10,6);
      constraints.fill   = GridBagConstraints.NONE;
      label = new Label(getSetting(LABEL_CONTENT));
      gridbag.setConstraints(label,constraints);
      panel.add(label);

      codeCheckbox = new Checkbox(getSetting(CHECKBOX_CODE));
      codeCheckbox.setState(true);
      gridbag.setConstraints(codeCheckbox,constraints);
      panel.add(codeCheckbox);

      constraints.gridheight = GridBagConstraints.RELATIVE;
      literalCheckbox = new Checkbox(getSetting(CHECKBOX_LIT));
      literalCheckbox.setState(true);
      gridbag.setConstraints(literalCheckbox,constraints);
      panel.add(literalCheckbox);

      constraints.gridheight = GridBagConstraints.REMAINDER;
      commentCheckbox = new Checkbox(getSetting(CHECKBOX_COMM));
      commentCheckbox.setState(true);
      gridbag.setConstraints(commentCheckbox,constraints);
      panel.add(commentCheckbox);
    } // end if

    pack();
    center(parent);
    setResizable(false);
  } // end constructor SearchDlg

  // methods
  /** Returns true if each replace operation should be confirmed. */
  public boolean isToConfirm() {
    return confirm;
  } // end isToConfirm

  /** Returns the search string. */
  public String getSearchString() {
    return searchTextField.getText();
  } // end getSearchString

  /** Returns the replace string. */
  public String getReplaceString() {
    if ((mode & TYPE_MASK) != REPLACE) return "";
    return replaceTextField.getText();
  } // end getReplaceString

  /** Returns true if only full words should be searched. */
  public boolean isWordsOnlyMode() {
    return wordCheckbox.getState();
  } // end isWordsOnlyMode

  /** Returns true if search operation should be case sensitive. */
  public boolean isCaseSensitive() {
    return caseCheckbox.getState();
  } // end isCaseSensitive

  /** Returns true if code should be included into the search operation. */
  public boolean isCodeEnabled() {
    if ((mode | REPLACE) != REPLACE_EX) return true;
    return codeCheckbox.getState();
  } // end isCodeEnabled

  /** Returns true if literals should be included into the search operation. */
  public boolean isLiteralEnabled() {
    if ((mode | REPLACE) != REPLACE_EX) return true;
    return literalCheckbox.getState();
  } // end isLiteralEnabled

  /** Returns true if comments should be included into the search operation. */
  public boolean isCommentEnabled() {
    if ((mode | REPLACE) != REPLACE_EX) return true;
    return commentCheckbox.getState();
  } // end isCommentEnabled

  /** Specify the search options. */
  public void setSearchOptions(String text, String replace,
                               boolean caseSens, boolean wordOnly) {

    searchTextField.setText(text);
    if ((mode & TYPE_MASK) == REPLACE)
      replaceTextField.setText(replace);
    // end if
    wordCheckbox.setState(wordOnly);
    caseCheckbox.setState(caseSens);
    searchTextField.selectAll();
  } // end setSearchOptions

  /** Returns a resource string from the resource bundle. */
  protected String getSetting(String key) {
    return ResourceUtil.getResStringOrKey(resources,resourcePrefix + key);
  } // end getSetting

  /** Called to close this dialog. */
  protected void closeDialog(AWTEvent event) {
    if ((event instanceof WindowEvent) || !isCanceled() ||
        ((mode & TYPE_MASK) != REPLACE)) {
      super.closeDialog(event);
    } // end if
  } // end closeDialog

  public void actionPerformed(ActionEvent e) {
    Object obj = e.getSource();

    if (obj == replaceTextField) {
      super.closeDialog(e);
    } else if (obj == searchTextField) {
      if ((mode & TYPE_MASK) == REPLACE) {
        replaceTextField.selectAll();
        replaceTextField.requestFocus();
      }
      else {
        super.closeDialog(e);
      } // end if
    } else { // if it's a button
      String cmd = e.getActionCommand();
      String label;

      if (buttonVector.size() < 2) return;
      label = ((Button)buttonVector.elementAt(1)).getLabel();
      if (cmd.equals(label)) { // if it's the replace all button
        confirm = false;
        super.closeDialog(e);
      } // end if
      if (buttonVector.size() < 3) return;
      label = ((Button)buttonVector.elementAt(2)).getLabel();
      if (cmd.equals(label)) { // if it's the cancel button
        canceled = true;
        super.closeDialog(e);
      } // end if
    } // end if
  } // end actionPerformed

  public void textValueChanged(TextEvent e) {
    int length = searchTextField.getText().length();

    setButtonEnabled(0,length > 0);
    if ((mode & TYPE_MASK) == REPLACE) setButtonEnabled(1,length > 0);
  } // end textValueChanged

} // end SearchDlg

/* ******************************************************************
   end of file
*********************************************************************/
