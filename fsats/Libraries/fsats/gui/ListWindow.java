package fsats.gui;

import java.awt.*;
import javax.swing.*;

/** Window which displays some fixed information followed by a 
 *  scrolling table.
 */
class ListWindow
    extends JFrame
{
    ListWindow(String name, CommonTableModel model)
    {
        super(name);
        this.model=model;

        // Construct data pane
        getContentPane().add(top, BorderLayout.NORTH);

        // Construct scroll pane 
        JTable table = new JTable(model);
        model.listenTo(table);
        table.getSelectionModel().setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);
        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

        setSize(new Dimension(500, 200));
        setVisible(true);
    }

    protected final Box top = Box.createVerticalBox();
    protected CommonTableModel model;
}
