package excis.demo;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.JFrame;

import fsats.gui.Message;

/** Provides a message sequence diagram within a JFrame.
    Accepts requests to display a message on the diagram.
*/


public class ShowDiagram extends JFrame {

    private Container          container;
    private GridBagLayout      layout;
    private GridBagConstraints constraints;
    private SequenceDiagram    sequenceDiagram;
    private Vector             playerNames;

    static boolean standAlone = false;


    /*---------------- constructor ---------------- */

    public ShowDiagram() {

	super("ExCIS Message Sequence Chart");

	container = getContentPane();
	layout = new GridBagLayout();
	container.setLayout (layout);
        constraints = new GridBagConstraints();

	playerNames = new Vector();
	playerNames.addElement("FO");
	playerNames.addElement("MECH_FIST");
	playerNames.addElement("BN_FSE");
	playerNames.addElement("DS_FA_PLT");
	playerNames.addElement("DS_BN_CP");
	playerNames.addElement("BDE_FSE");

	sequenceDiagram = new SequenceDiagram(playerNames);
	constraints.insets = new Insets(0,0,0,0);
        addConstraints (constraints, 1, 0, 5, 1, 0, 0);
	layout.setConstraints(sequenceDiagram, constraints);
	container.add(sequenceDiagram);

	addWindowListener(new Terminate());

	pack();
	//	setSize(600, 360);
	setLocation(500,0);
	show();
    }



    /* addConstraints */

    private void addConstraints (GridBagConstraints constraints,
			         int       row,     int column, 
			         int       width,   int height,
			         int       wty,     int wtx) {
	constraints.gridy = row;
	constraints.gridx = column;
	constraints.gridwidth = width;
	constraints.gridheight = height;
	constraints.weighty = wty;
	constraints.weightx = wtx;

    }



    //-- class Terminate --

    private class Terminate extends WindowAdapter {

	public void windowClosing(WindowEvent event) {
	    if (standAlone) {
		dispose();
		System.exit(0);
	    }
	}
    }


    /* displayMessage */

    /** @param   message the message to be displayed.

	Display message from source player to destination 
	player on the sequence diagram.
     */

    public void displayMessage (Message message) {
	sequenceDiagram.displayMessage(message);
    }


    //-- main --


    public static void main (String[] args) {
	ShowDiagram     showIt;

	standAlone = true;
	showIt = new ShowDiagram();
    }

}


