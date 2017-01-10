package excis.demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import fsats.gui.Message;


public class SequenceDiagram extends JScrollPane {


    private Vector                     playerNames;
    private DefaultTableModel          tableModel;
    private JTable                     sequenceTable;
    private ColorfulTableCellRenderer  cellRenderer;
    private DisplayColors              displayColors;


    private String    previousMessage = "";
    private int       previousRow = -1;
    private Object[]  blankRow = {"","","","",""};


    /* ---------------- constructor ---------------- */

    /** @param playerNames  Vector of player name Strings.
     */

    public SequenceDiagram(Vector playerNames) {    

        this.playerNames = playerNames;
	playerNames.insertElementAt("", 0);

	// The scrollbar isn't behaving well so best to allocate
	// at least as many rows as you may need.

	tableModel = new DefaultTableModel(playerNames, 100);
	sequenceTable = new JTable(tableModel);

	setHeaderRenderers(sequenceTable, playerNames);
	setColumnWidths(sequenceTable, playerNames);
	cellRenderer = colorfulRendererFor(sequenceTable);
	displayColors = new DisplayColors();

	sequenceTable.setShowHorizontalLines(false);
	sequenceTable.setPreferredScrollableViewportSize(new Dimension(560, 305));

        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
	setViewportView (sequenceTable);

    }


    /* setColumnWidths */

    /** The first column needs to be wider to accomodate the message name.
        The last column looks better if it is smaller.  If a flexible number
	of players is to be used the number of columns must dictate the width.
	Don't forget to make the 'bar' variable flexible, too.
    */

    private void setColumnWidths(JTable sequenceTable,
			        Vector playerNames) {
	TableColumn  column;

	column = sequenceTable.getColumnModel().getColumn(0);
	column.setPreferredWidth(100);
	//	column.setPreferredWidth(120);

//  	for (int i=1; i<playerNames.size()-1; i++) {
//  	    column = sequenceTable.getColumnModel().getColumn(i);
//  	    //	    column.setPreferredWidth(73);
//  	    column.setPreferredWidth(51);
//  	}

//  	column = sequenceTable.getColumnModel().getColumn(playerNames.size()-1);
//  	column.setPreferredWidth(60);
    }


    /* colorfulRendererFor */

    /** Set a cell renderer for each column.
     */

    private ColorfulTableCellRenderer colorfulRendererFor(JTable sequenceTable) {
	TableColumn                column;
	ColorfulTableCellRenderer  cellRenderer;

	cellRenderer = new ColorfulTableCellRenderer();
	
	for (int i=0; i<sequenceTable.getColumnCount(); i++) {
	    column = sequenceTable.getColumnModel().getColumn(i);
	    column.setCellRenderer(cellRenderer);
	}	

	return cellRenderer;
    }


    /* setHeaderRenderers */

    /** Set a renderer for each column header.
     */

    private void setHeaderRenderers(JTable sequenceTable,
				    Vector playerNames) {
	TableColumn       column;
	TableCellRenderer renderer;
	String            player;

	for (int i=0; i<playerNames.size(); i++) {
	    column = sequenceTable.getColumn(playerNames.elementAt(i));
 	    renderer = column.getHeaderRenderer();
	    player = playerNames.elementAt(i).toString();
	    column.setHeaderRenderer(new HeaderDecorator(renderer, player));
	}
    }


    /* displayMessage */

    /** @param   message the message to be displayed.

	Display message from source player to destination 
	player on the sequence diagram.
     */

    public void displayMessage(Message  message) {
	int row;
	int source;
	int dest;

	row = previousRow + 1;

	// New message type.

	if (! previousMessage.equals(message.getName())) {
	    displayColors.setNext();
	    cellRenderer.setTextColor(row, displayColors.current());
	    addBlankRow(row); 
	    row = row + 1;
	    setMessageName(row, message.getName());
	    previousMessage = message.getName();
	}

	previousRow = row;

	source = playerNames.indexOf(message.getSourceRole());
	dest   = playerNames.indexOf(message.getDestinationRole());

	displayArrow(source, dest, row);

    }


    /* displayArrow */

    /** Display a left arrow or a right arrow from source
	to dest in row.
     */

    void displayArrow(int source,
		      int dest,
		      int row) {

	if (source < dest) {
	    for (int i=source; i <dest-1; i++) {
		showMessageMove(row, i, "");
	    }
	    showMessageMove(row, dest-1, "RIGHT");

	} else {
	    for (int i=source-1; i>dest; i--) {
		showMessageMove(row, i, "");
	    }
	    showMessageMove(row, dest, "LEFT");

	}
    }


    /* makeRowVisible */

    /** Scroll to make newest row visible.
     */

    void makeRowVisible(JTable table, int row) {
	Rectangle cellRect;

	if (row > 18) {
	    cellRect = table.getCellRect(row + 2, 0, true);
	} else {
	    cellRect = table.getCellRect(row, 0, true);
	}
	if(cellRect != null) {
	    table.scrollRectToVisible(cellRect);
	}

    }


    /* addBlankRow */

    /** If all of the preallocated rows have been used, add another.
	This isn't always pretty.
    */

    void addBlankRow(int row) {
	tableModel.addRow(blankRow);
	makeRowVisible(sequenceTable, row);
    }


    /* setMessageName */

    /** Advance to the next display color.  Display the message name
	in the first column.
    */

    void setMessageName(int    row,
			String name) {
	cellRenderer.setTextColor(row, displayColors.current());
	while (tableModel.getRowCount() <= row) {
	    addBlankRow(row);
	}
	sequenceTable.setValueAt(name, row, 0);
    }


    /* symbolOf */

    /** Provide a right arrow, a left arrow, or a plain bar.
     */

    String symbolOf(String move) {
       	String bar = "----------------";

	if (move.equals("RIGHT")) {
	    return (bar + ">");
	} else if (move.equals("LEFT")) {
	    return ("<" + bar);
	} else {
	    return (bar + "--");
	}

    }


    /* showMessageMove */

    /** Display the move symbol on the sequence diagram.
     */

    void showMessageMove(int    row,
			 int    col,
			 String move) {

	cellRenderer.setTextColor(row, displayColors.current());

	while (tableModel.getRowCount() <= row) {
	    addBlankRow(row);
	    makeRowVisible(sequenceTable, row);
	}

	sequenceTable.setValueAt(symbolOf(move), row, col);
    }


    /*--- class HeaderDecorator ---*/

    /** Provides a cell renderer which aligns the text to the left, 
        makes the font size 10, and darkens the text color.
    */

    class HeaderDecorator implements TableCellRenderer {
	TableCellRenderer realRenderer;
	JLabel            leftLabel;

	/*------- constructor -------*/

	public HeaderDecorator(TableCellRenderer r,
			       String            label) {
	    realRenderer = r;
	    leftLabel = new JLabel(label);
       	    leftLabel.setHorizontalAlignment(SwingConstants.LEFT);
	    leftLabel.setFont(getFont().deriveFont(Float.parseFloat("10")));
	    leftLabel.setForeground(leftLabel.getForeground().darker());
	}


	/* getTableCellRendererComponent */

	public Component getTableCellRendererComponent(JTable  table,
						       Object  value,
						       boolean isSelected,
						       boolean hasFocus,
						       int     row,
						       int     col) {
	    return leftLabel;
	}
    }


    /*--- class ColorfulTableCellRenderer ---*/

    /** Provides a cell renderer which aligns the text to the right, 
        makes it bold, and alternates row colors as requested.
    */

    class ColorfulTableCellRenderer extends DefaultTableCellRenderer {
	Vector  rowColors = new Vector();

	/*------constructor -------*/

	public ColorfulTableCellRenderer() {
	    setHorizontalAlignment(SwingConstants.RIGHT);
	    setFont(getFont().deriveFont(Font.BOLD));
	}


	/* getTableCellRendererComponent */

	/** Extends this functionality by setting text color to the
	    currently requested color in the DisplayColors list.
	*/

	public Component getTableCellRendererComponent(JTable table,
						       Object value,
						       boolean isSelected,
						       boolean hasFocus,
						       int row, int col) {
	    Integer colorIndex;

	    if (rowColors.size() > row) {
		colorIndex = (Integer)rowColors.elementAt(row);
		setForeground(displayColors.getColor(colorIndex.intValue()).darker());
	    }
	    return super.getTableCellRendererComponent(table, value,
						       isSelected,hasFocus,
						       row, col);

	}


	/* setTextColor */

	/** As each row in the table is populated this must be called to
	    add it to the Vector which keeps track of the color to render
	    for each row.
	*/

  	public void setTextColor(int  row,
				 int  colorIndex) {
	    if (row  >= rowColors.size()) {
		rowColors.addElement(new Integer(colorIndex));
	    }
  	}

    }


    /*--- class DisplayColors ---*/

    /** Provides a sequence of Colors.
     */

    class DisplayColors {

	Color[] colors = {Color.blue, Color.red, Color.green.darker().darker(), 
			  Color.magenta.darker(), Color.darkGray};
	int     index; 

	/*----------- constructor -----------*/

	public DisplayColors() {
	    index = -1;
	}


	/* current */

	/** Return the index of the currently active color.
	 */

	public int current() {
	    return index;
	}


	/* setNext */

	/** Advance to the next color in the sequence.
	 */

	public void setNext() {
	    index++;
	    if (index == colors.length) {
		index = 0;
	    } 
	}


	/* getColor */

	/** Return the Color represented by index.
	 */

	public Color getColor(int index) {
	    return colors[index];
	}

    }


}





