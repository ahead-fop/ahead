/* ******************************************************************
   class      : ErrorDlg
   description: a dialog that displays an error message
*********************************************************************/

package ModelExplorer.Util.Dialogs;

import java.awt.*;
import java.awt.event.*;

import ModelExplorer.Util.InsetsPanel;

public class ErrorDlg extends Dialog implements ActionListener {

  // instance variable declarations
  private boolean autoCancel = false;
  private Button okButton;
  private Button detailsButton;
  private Label label;
  private Component iconImage;
  private Panel buttonPanel;
  private String errorString;
  private String detailsString;

  // inner classes
  class ErrorDlgListener extends WindowAdapter {

    // methods
    public void windowClosing(WindowEvent e) {
      setVisible(false);
      dispose();
    } // end windowClosing

    public void windowDeactivated(WindowEvent e) {
      if (autoCancel) {
        setVisible(false);
        dispose();
      } // end if
    } // end WindowDeactivated

  } // end ErrorDlgListener

  // costructors
  public ErrorDlg(Frame parent, String errorMsg) {
    super(parent);
    init(errorMsg);
  } // end constructor ErrorDlg

  public ErrorDlg(Frame parent, boolean modal, String errorMsg) {
    super(parent,modal);
    init(errorMsg);
  } // end constructor ErrorDlg

  public ErrorDlg(Frame parent, String title, String errorMsg) {
    super(parent,title);
    init(errorMsg);
  } // end constructor ErrorDlg

  public ErrorDlg(Frame parent, String title,
                  boolean modal, String errorMsg) {
    super(parent,title,modal);
    init(errorMsg);
  } // end constructor ErrorDlg

  // methods
  private void init(String errorMsg) {
    int newLineIndex = errorMsg.indexOf('\n');

    if (newLineIndex < 0) {
      errorString   = errorMsg;
      detailsString = null;
    }
    else {
      errorString = errorMsg.substring(0,newLineIndex);
      if (errorMsg.length() == (newLineIndex+1))
        detailsString = null;
      else
        detailsString = errorMsg.substring(newLineIndex+1,errorMsg.length());
      // end if
    } // end if
    addWindowListener(new ErrorDlgListener());

    // create the widgets
    okButton = new Button("OK");
    okButton.addActionListener(this);
    detailsButton = new Button("Details >>");
    detailsButton.addActionListener(this);
    if (detailsString == null) detailsButton.setEnabled(false);
    buttonPanel = new Panel();
    buttonPanel.setLayout(new GridLayout(2,1,0,7));
    buttonPanel.add(okButton);
    buttonPanel.add(detailsButton);

    label = new Label(errorString);

    iconImage = new MemoryImageCanvas(HandImage.pixels,32,32);

    makeLayout(false);

    setResizable(false);
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation((screen.width - getSize().width) / 2,
                (screen.height - getSize().height) / 2);
    setVisible(true);
  } // end init

  private void makeLayout(boolean showDetails) {
    InsetsPanel pan = new InsetsPanel(new Insets(15,20,15,20));

    // this dialog box uses the gridbaglayout to provide a pretty good layout
    GridBagLayout gridbag          = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    pan.setLayout(gridbag);

    constraints.fill       = GridBagConstraints.NONE;
    constraints.anchor     = GridBagConstraints.CENTER;
    constraints.weightx    = 1.0;
    constraints.weighty    = 1.0;
    constraints.insets     = new Insets(0,0,0,20);
    constraints.gridwidth  = 1;
    if (showDetails)
      constraints.gridheight = GridBagConstraints.RELATIVE;
    else
      constraints.gridheight = GridBagConstraints.REMAINDER;
    // end if

    gridbag.setConstraints(iconImage,constraints);
    pan.add(iconImage);

    constraints.insets     = new Insets(0,0,0,0);
    constraints.gridwidth  = GridBagConstraints.RELATIVE;

    gridbag.setConstraints(label,constraints);
    pan.add(label);

    constraints.gridwidth = GridBagConstraints.REMAINDER;

    gridbag.setConstraints(buttonPanel, constraints);
    pan.add(buttonPanel);

    if (showDetails) {
      TextArea textArea      = new TextArea(detailsString,7,40);
      textArea.setEditable(false);
      constraints.gridheight = GridBagConstraints.REMAINDER;
      constraints.insets     = new Insets(15,0,0,0);
      gridbag.setConstraints(textArea,constraints);
      pan.add(textArea);
    } // end if

    add(pan);
    pack();
  } // end makeLayout

  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();

    if (cmd.equals("OK")) {
      setVisible(false);
      dispose();
    }
    else { // if cmd equals details
      removeAll();
      detailsButton.setEnabled(false);
      makeLayout(true);
      okButton.requestFocus();
    } // end if
  } // end actionPerformed

  public boolean getAutoCancel() {
    return autoCancel;
  } // end getAutoCancel

  public void setAutoCancel(boolean autoCancel) {
    this.autoCancel = autoCancel;
  } // end getAutoCancel

} // end ErrorDlg

/* ******************************************************************
   end of file
*********************************************************************/
