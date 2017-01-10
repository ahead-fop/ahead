package fsats.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/** A TableModel which can listen for mouse clicks on cells in the table.
 */
abstract class CommonTableModel
    extends AbstractTableModel
{
    public CommonTableModel(Client client) { this.client=client; }

    /** Listen for mouse clicks on the specified table. */
    public void listenTo(final JTable table)
    {
        table.addMouseListener(
            new MouseAdapter()
            {
                private JTable myTable = table;
                public void mouseClicked(MouseEvent e)
                {
                    cellClicked(
                        myTable.getSelectedRow(), 
                        myTable.getSelectedColumn());
                }
            });
    }

    /** Called when a cell is clicked. 
     */
    protected abstract void cellClicked(int row, int column);

    /** Method to use when an opfac cell is clicked.  
     */
    protected void clickedOpfac(String id)
    {
        client.getOpfac(id).openWindow();
    }

    /** Method to use when a mission cell is clicked.
     */
    protected void clickedMission(String id)
    {
        client.getMission(id).openWindow();
    }

    protected Client client;

    // Window management stuff.

    private Frame window = null;

    private synchronized void closedWindow()
    {
        window=null;
    }

    public synchronized void openWindow()
    {
        if (window!=null)
            window.toFront();
        else
        {
            window = newWindow();
            window.addWindowListener(
                new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e) 
                    { 
                        closedWindow(); 
                    }
                });
        }
    }

    protected abstract Frame newWindow();
       
}

