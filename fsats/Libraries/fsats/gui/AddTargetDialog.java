package fsats.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Dialog used to request information about the target to place on the
 * grid.
 *
 * @author Daniel Rogers (drogers@arlut.utexas.edu)
 * @version 1.0, 06/14/99
 */
public class AddTargetDialog extends JDialog {

  // er...this should probably not be hard coded, and come from
  // somewhere else.
  private static final String[] TARGET_TYPES =
    {"Armor_Unknown", "INFANTRY", "ARTILLERY"};

  private Location targetLocation;
  private Client client;
  
  /**
   * Constructs the dialog.
   *
   * @param owner Frame owner for the dialog
   * @param client Client reference
   * @param grid GridPane reference
   * @param targetLocation Location of target
   */
  public AddTargetDialog (Frame owner, Client client, GridPane grid,
			  Location targetLocation) {
    super(owner, "Add Target", true);

    this.client = client;
    
    owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    
    this.targetLocation = targetLocation;
    
    // Create the layout
    makeLayout();
    
    // Make sure to cancel out if close window button pressed
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    // Auto-display ourselves
    pack();
    show();
    
    owner.setCursor(Cursor.getDefaultCursor());
  }
  
  /**
   * Closes the dialog.
   */
  protected void close () {
    setVisible(false);
    dispose();
  }
  
  /**
   * Creates the layout for the dialog.
   */
  protected void makeLayout () {
    
    Container pane = this.getContentPane();
    GridBagLayout gbl = new GridBagLayout();
    pane.setLayout(gbl);
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth  = GridBagConstraints.REMAINDER;
    gbc.gridheight = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new Insets(5, 0, 20, 0);
    
    JLabel title = new JLabel("Add Target", SwingConstants.CENTER);
    title.setFont(new Font(getFont().getName(), Font.BOLD,
			   getFont().getSize() + 8));
    pane.add(title, gbc);
    
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.insets = new Insets(0, 15, 20, 0);
    gbc.anchor = GridBagConstraints.WEST;
    
    JLabel location = new JLabel("Target Location: ");
    
    pane.add(location, gbc);
    
    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.insets = new Insets(0, 0, 20, 15);

    JTextField locField = new JTextField(20);
    locField.setEditable(false);
    locField.setText(targetLocation.toString());
    pane.add(locField, gbc);
    
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.insets = new Insets(0, 15, 20, 0);
    
    JLabel type = new JLabel("Target Type: ");
    pane.add(type, gbc);
    
    gbc.gridx = 1;
    gbc.gridwidth = 1;
    gbc.insets = new Insets(0, 0, 20, 0);
    
    final JComboBox jcType = new JComboBox();
    // :NOTE: This comes from the hardcoded list above...may need to
    // change this.
    for (int i = 0; i < TARGET_TYPES.length; i++) {
      jcType.addItem(TARGET_TYPES[i]);
    }
    
    pane.add(jcType, gbc);

    gbc.gridx = 2;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.insets = new Insets(0, 0, 20, 15);
    
    pane.add(Box.createGlue(), gbc); // spacer
    
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.insets = new Insets(0, 15, 15, 15);
    gbc.anchor = GridBagConstraints.CENTER;
    
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 2, 10, 5));
    
    JButton ok = new JButton("OK");
    JButton cancel = new JButton("Cancel");

    buttonPanel.add(ok);
    buttonPanel.add(cancel);
    
    ok.addActionListener(new ActionListener () {
      public void actionPerformed (ActionEvent e) {
	client.putTarget("", new fsats.guiIf.Target () {
	  public String getType () {
	    return (String) jcType.getSelectedItem();
	  }
	  public fsats.guiIf.Location getLocation () {
	    return targetLocation;
	  }
			     });
	close();
      }
    });
    
    cancel.addActionListener(new ActionListener () {
      public void actionPerformed (ActionEvent e) {
	close();
      }
    });

    pane.add(buttonPanel, gbc);
  }
}
   
