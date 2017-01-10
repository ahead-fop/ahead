/**
 *   class      : CustomDialog
 */

package ModelExplorer.Util;

import ModelExplorer.*;
import ModelExplorer.MMatrixBrowser.*;

import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*; //Property change stuff
import java.awt.*;
import java.awt.event.*;

public class CustomDialog extends JDialog {
    private String regex1 = null;
	 private String regex2 = null;

    private JOptionPane optionPane;

    public String getValidatedR1() {
        return regex1;
    }
	 public String getValidatedR2() {
        return regex2;
    }
	 public JOptionPane getOptionPane(){
		 return optionPane;
	 }
	 
	 public CustomDialog(String title, Object[] array, Frame aFrame, Main parent) {
		 super(aFrame, true);
		 final Main me = parent;
		 final Object[] myArray=array;
		 setTitle(title);
		  final String btnString1 = "Ok";
        final String btnString2 = "Cancel";
        Object[] options = {btnString1, btnString2};

        optionPane = new JOptionPane(myArray, 
												JOptionPane.PLAIN_MESSAGE, 
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]);
        setContentPane(optionPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                     optionPane.setValue(new Integer(
                                        JOptionPane.CLOSED_OPTION));
            }
        });
		  optionPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
			    String prop = e.getPropertyName();

			    if (isVisible() 
			     && (e.getSource() == optionPane)
			     && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
			         prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
						
						Object value = optionPane.getValue();
			        if (value == JOptionPane.UNINITIALIZED_VALUE) {
			            return;
			        }
			        optionPane.setValue(
			                JOptionPane.UNINITIALIZED_VALUE);
			        if (value.equals(btnString1)) {
			            me.compositionDir=((JTextField)myArray[2]).getText();
							setVisible(false);
			        } else { // user closed dialog or clicked cancel
			           setVisible(false);
			        }
			    }
			}
		});
	 
	 }	 

    public CustomDialog(Frame aFrame, Main parent) {
        super(aFrame, true);
        final Main me = parent;

        setTitle("Regular Expressions");

        final String msgString1 = "Regular Expression For Rows:";
        final String msgString2 = "Regular Expression For Cols:";
        final JTextField textField1 = new JTextField(10);
		  textField1.setText(".*");
		  textField1.selectAll();
		  final JTextField textField2 = new JTextField(10);
		  textField2.setText(".*");
        Object[] array = {msgString1, textField1, msgString2, textField2};

        final String btnString1 = "Ok";
        final String btnString2 = "Cancel";
        Object[] options = {btnString1, btnString2};

        optionPane = new JOptionPane(array, 
									JOptionPane.PLAIN_MESSAGE, 
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]);
        setContentPane(optionPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    optionPane.setValue(new Integer(
                                        JOptionPane.CLOSED_OPTION));
            }
        });

        textField1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionPane.setValue(btnString1);
            }
        });

        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();

                if (isVisible() 
                 && (e.getSource() == optionPane)
                 && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
                     prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
                   
						  Object value = optionPane.getValue();
 
                    if (value == JOptionPane.UNINITIALIZED_VALUE) {
                        //ignore reset
                        return;
                    }

                    // Reset the JOptionPane's value.
                    optionPane.setValue(
                            JOptionPane.UNINITIALIZED_VALUE);

                    if (value.equals(btnString1)) {
                        regex1 = textField1.getText();
								regex2 = textField2.getText();
								//me.status.setText("Status: "+regex1+"  "+regex2);
								me.isReduced=true;
								me.mmatrixGui.reducer(regex1,regex2);
								me.rmatrixGui=new MMatrixGui(me);
								me.browserTabbedPane.remove(2);
								me.browserTabbedPane.add(me.rmatrixGui.ContentPane,2);
								me.browserTabbedPane.setTitleAt(2,"Navigator");
								me.browserTabbedPane.setSelectedIndex(2);
								me.matrixIndex=-1;
								me.forwardButton.setEnabled(true);
								me.backButton.setEnabled(false);
								setVisible(false);
								me.isReduced=false;
                        /*} else { 
                            // text was invalid
                            textField.selectAll();
                            JOptionPane.showMessageDialog(
                                            CustomDialog.this,
                                            "Sorry, \"" + typedText + "\" "
                                            + "isn't a valid response.\n"
                                            + "Please enter " 
                                            + magicWord + ".",
                                            "Try again",
                                            JOptionPane.ERROR_MESSAGE);
                            typedText = null;
                        }*/
                    } else { // user closed dialog or clicked cancel
								me.status.setText("Status: Regalur expresson input was cancelled.");
								      textField1.setText(".*");
								textField2.setText(".*");
								textField1.selectAll();
                        setVisible(false);
                    }
                }
            }
        });
    }
}
