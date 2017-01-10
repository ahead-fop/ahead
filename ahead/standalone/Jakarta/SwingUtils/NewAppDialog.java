// NewAppDialog.java 

package Jakarta.SwingUtils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class NewAppDialog extends SwingDialog {

   String action = null;

   // initialize constants used in the application
   // REMEMBER -- make constants static!
   
   public void initConstants() { }

   // declare and initialize atomic components here

   JTextField filename;
   JRadioButton application;
   JRadioButton applet;
   JRadioButton dialog;
   JButton generate;
 
   public void initAtoms() {
      filename = new JTextField(25);
      filename.setBorder( BorderFactory.createTitledBorder(
                            "Name of File to Generate") );

      application = new JRadioButton("Swing Application");
      application.setSelected(true);
      application.setActionCommand("-f");

      applet = new JRadioButton("SwingApplet");
      applet.setSelected(false);
      applet.setActionCommand("-a");

      dialog = new JRadioButton("Swing Dialog");
      dialog.setSelected(false);
      dialog.setActionCommand("-d");

      ButtonGroup group = new ButtonGroup();
      group.add(application);
      group.add(applet);
      group.add(dialog);

      generate = new JButton("Generate");
   }

   // declare and initialize layout components here

   JPanel rbuttons;
   public void initLayout() {
      rbuttons = new JPanel();
      rbuttons.setLayout( new GridLayout(0,1) );
      rbuttons.setBorder( BorderFactory.createTitledBorder(
                            "select type of file to generate") );
      rbuttons.add(application);
      rbuttons.add(applet);
      rbuttons.add(dialog);
   }

   // initialize ContentPane here

   public void initContentPane() {
      ContentPane = new JPanel();
      ContentPane.setLayout( new GridLayout(0,1) );
      ContentPane.setBorder(BorderFactory.createEtchedBorder());
      ContentPane.add( filename );
      ContentPane.add( rbuttons );
      ContentPane.add( generate );
   }

   class RadioListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         action = e.getActionCommand();
      }
   }

   // initialize listeners here

   public void initListeners() {
      RadioListener rl = new RadioListener();
      application.addActionListener( rl );
      applet.addActionListener( rl );
      dialog.addActionListener( rl );

      generate.addActionListener( new ActionListener() {
         public void actionPerformed( ActionEvent e ) {
            String fn = filename.getText();
            if (action == null)
               action = "-f";
            else
            if (action.equals("-a"))
               fn = "SwingApplet";
            String args[] = { action, fn };
            NewApp.main( args );
         }
      });
   }

   // place in this method any action for exiting application

   public void applicationExit() {

   }

   public NewAppDialog() { super(null, true); } 

   public NewAppDialog(String AppTitle) { super(null, AppTitle, true); } 

   public static void main(String[] args) {
      new NewAppDialog("NewAppDialog");
   }
}
