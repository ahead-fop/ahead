package fsats.gui;

/** TableModel for opfacs.
 */
abstract class OpfacTableModel
    extends CommonTableModel
{
    public OpfacTableModel(Client client) { super(client); }

    public String getColumnName(int column) { return columnNames[column]; }
    public Class getColumnClass(int column) { return columnClasses[column]; }
    public int getColumnCount() { return columnNames.length; }

    public abstract Opfac getOpfacAt(int row);

    public Object getValueAt(int row, int column)
    {
        Opfac opfac = getOpfacAt(row);
        Object value=null;
        switch (column)
        {
        case 0: value=opfac.getSubscriber(); break;
        case 1: value=opfac.getType(); break;
        case 2: value=opfac.getLocation().getLatitudeDMS(); break;
        case 3: value=opfac.getLocation().getLongitudeDMS(); break;
        }
        return value;
    }
    protected void cellClicked(int row, int column)
    {
        Opfac opfac = getOpfacAt(row);
        switch (column)
        {
        case 0: clickedOpfac(opfac.getSubscriber()); break;
        }
    }

    private static final String[] columnNames =
    {
        "Id", 
        "Type", 
        "Latitude", 
        "Longitude"
    };
    private static final Class[] columnClasses =
    {
        String.class, 
        String.class, 
        String.class, 
        String.class
    };
}
