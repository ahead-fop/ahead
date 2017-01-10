/* ******************************************************************
   class    : BaseDialog
*********************************************************************/

package ModelExplorer.Util;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Enumeration;

/**
 * A class that implements frequently used methods of dialogs.
 * Before showing this kind of dialogs you should call the pack()
 * method.
 */
public class BaseDialog extends Dialog {

  // instance variable declarations
  /** The panel to which extended classes should add their components. */
  protected Panel contentPanel;
  /** The panel which contains the buttons. */
  protected Panel buttonPanel;
  /** The vector containing the dialog buttons */
  protected Vector buttonVector;
  /** The label of the Ok button. */
  protected String okLabel;
  /** True if this dialog has been canceled. */
  protected boolean canceled = false;
  /** The action and window event listener. */
  private Listener listener;

  // constructors
  /**
   * Constructs an initially invisible BaseDialog with an empty title.
   *
   * @param parent The owner of the dialog.
   */
  public BaseDialog(Frame parent) {
    super(parent);
    init();
  } // end BaseDialog

  /**
   * Constructs an initially invisible BaseDialog with an empty title.
   * A modal dialog grabs all the input to the parent frame
   * from the user.
   *
   * @param parent The owner of the dialog.
   * @param modal If true, dialog blocks input to the parent window
   *              when shown.
   */
  public BaseDialog(Frame parent, boolean modal) {
    super(parent,modal);
    init();
  } // end BaseDialog

  /**
   * Constructs an initially invisible BaseDialog with a title.
   *
   * @param parent The owner of the dialog.
   * @param title  The title of the dialog.
   */
  public BaseDialog(Frame parent, String title) {
    super(parent,title);
    init();
  } // end BaseDialog

  /**
   * Constructs an initially invisible BaseDialog with a title. A modal
   * dialog grabs all the input to the parent frame from the user.
   *
   * @param parent The owner of the dialog.
   * @param title The title of the dialog.
   * @param modal If true, dialog blocks input to the parent window
   *              when shown.
   */
  public BaseDialog(Frame parent, String title, boolean modal) {
    super(parent,title,modal);
    init();
  } // end BaseDialog

  // class methods
  /** Only for debuging purposes. */
  public static void main(String[] args) {
    Frame frame    = new Frame();
    BaseDialog dlg = new BaseDialog(frame,true);
    Panel panel    = dlg.getContentPanel();

    dlg.createButtons("OK Cancel Reset");
    panel.add(new Label("BaseDialog Content Panel"));
    dlg.pack();
    dlg.center(null);
    dlg.setResizable(false);
    dlg.setVisible(true); //show();
    System.out.println("isCanceled returned " + dlg.isCanceled());
    System.exit(0);
  } // end main

  // methods
  /** Avoids multiple constructor implementation. */
  private void init() {
    listener = new Listener();
    addWindowListener(listener);

    GridBagLayout gridbag          = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    setLayout(gridbag);

    contentPanel = new BufferedPanel();
    constraints.weightx    = 1.0;
    constraints.weighty    = 1.0;
    constraints.gridwidth  = GridBagConstraints.REMAINDER;
    constraints.gridheight = GridBagConstraints.RELATIVE;
    gridbag.setConstraints(contentPanel,constraints);
    add(contentPanel);

    buttonPanel = new Panel();
    constraints.insets     = new Insets(0,6,10,6);
    constraints.gridheight = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(buttonPanel,constraints);
    add(buttonPanel);
  } // end init

  /**
   * Creates the buttons for this BaseDialog.  The button labels
   * are specified as a string, separated by blanks. The first
   * label is the Ok button, the second is the Cancel button
   * and all other buttons are application specific.
   *
   * @param btns The button labels, separated by blanks.
   */
  public void createButtons(String btns) {
    StringTokenizer tokenizer = new StringTokenizer(btns," \t\n\r",false);
    int numBtns               = tokenizer.countTokens();
    int curBtn                = 0;

    if (!tokenizer.hasMoreTokens()) return;
    buttonPanel.setLayout(new GridLayout(1,numBtns,10,0));
    buttonVector = new Vector(numBtns);
    do {
      String str = tokenizer.nextToken();
      Button button = new Button(str);

      switch (curBtn) {
        case 0: // the Ok button
          okLabel = str;
          button.addActionListener(listener);
        break;
        case 1: // the Cancel button
          button.addActionListener(listener);
        break;
        default:
      } // end switch
      buttonPanel.add(button);
      buttonVector.addElement(button);
      curBtn++;
    } while (tokenizer.hasMoreTokens());
  } // end createButtons

  /**
   * Returns the panel to which extended classes should add
   * their components.
   */
  public Panel getContentPanel() {
    return contentPanel;
  } // end getContentPanel

  /** Returns true if this dialog has been canceled. */
  public boolean isCanceled() {
    return canceled;
  } // end isCanceled

  /**
   * Adds the specified action listener to receive action events
   * from all buttons in this dialog. Action events occur when a user
   * presses or releases the mouse over one of these buttons.
   *
   * @param l The action listener.
   * @see removeActionListener
   */
  public void addActionListener(ActionListener l) {
    Enumeration en;

    if (buttonVector == null) return;
    en = buttonVector.elements();
    while (en.hasMoreElements()) {
      ((Button)en.nextElement()).addActionListener(l);
    } // end while
  } // end addActionListener

  /**
   * Removes the specified action listener so that it no longer
   * receives action events from all buttons in this dialog.
   * Action events occur when a user presses or releases the
   * mouse over one of these buttons.
   *
   * @param l The action listener.
   * @see addActionListener
   */
  public void removeActionListener(ActionListener l) {
    Enumeration en;

    if (buttonVector == null) return;
    en = buttonVector.elements();
    while (en.hasMoreElements()) {
      ((Button)en.nextElement()).removeActionListener(l);
    } // end while
  } // end removeActionListener

  /**
   * Set a enabled or disabled.
   *
   * @param btnNr The number of the button.
   */
  public void setButtonEnabled(int btnNr, boolean enbl) {
    if ((buttonVector == null) ||
        (buttonVector.size() <= btnNr)) return;
    ((Button)buttonVector.elementAt(btnNr)).setEnabled(enbl);
  } // end setButtonEnabled

  /**
   * Center the dialog in the given frame.  If it is null, then center
   * the dialog within the screen. Invoke it after pack() and before
   * you set resizeable to false.
   *
   * @param frame The parent frame.
   */
  public void center(Frame frame) {
    int x,y;
    Point p;
    Dimension f,d;

    if (frame == null) {
      p = new Point(0,0);
      f = getToolkit().getScreenSize();
    }
    else {
      p = frame.getLocation();
      f = frame.getSize();
    } // end if
    d = getSize();
    x = p.x + (f.width  - d.width)  / 2;
    y = p.y + (f.height - d.height) / 2;
    if (x < 0) x = 0;
    if (y < 0) y = 0;
    setLocation(x,y);
  } // end center

  /**
   * Called to close this dialog.
   */
  protected void closeDialog(AWTEvent event) {
    setVisible(false);
    dispose();
  } // end closeDialog

  // inner classes
  private class Listener extends WindowAdapter
    implements ActionListener {

    //methods
    /** Invoked when a window is in the process of being closed. */
    public void windowClosing(WindowEvent e) {
      canceled = true;
      closeDialog(e);
    } // end windowClosing

    // --- ActionListener ------------------------------------
    /** Invoked when an action occurs. */
    public void actionPerformed(ActionEvent e) {
      canceled = !e.getActionCommand().equals(okLabel);
      closeDialog(e);
    } // end actionPerformed

  } // end Listener

} // end BaseDialog

/* ******************************************************************
   end of file
*********************************************************************/
