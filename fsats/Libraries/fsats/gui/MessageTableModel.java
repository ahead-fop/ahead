package fsats.gui;

/** TableModel for a list of messages.
 */
abstract class MessageTableModel
    extends CommonTableModel
{
    public MessageTableModel(Client client) { super(client); }

    public String getColumnName(int column) { return columnNames[column]; }
    public Class getColumnClass(int column) { return columnClasses[column]; }
    public int getColumnCount() { return columnNames.length; }

    public abstract Message getMessageAt(int row);

    public Object getValueAt(int row, int column)
    {
        Message message = getMessageAt(row);
        Object value=null;
        switch (column)
        {
        case 0: value=message.getMissionID(); break;
        case 1: value=message.getSource(); break;
        case 2: value=message.getDestination(); break;
        case 3: value=message.getType(); break;
        case 4: value=message.getSpecific(); break;
        }
        return value;
    }

    protected void cellClicked(int row, int column)
    {
        Message message = getMessageAt(row);
        switch (column)
        {
        case 0: clickedMission(message.getMissionID()); break;
        case 1: clickedOpfac(message.getSource()); break;
        case 2: clickedOpfac(message.getDestination()); break;
        case 3: clickedMessage(message);
        }
    }

    /**
     * Currently takes a message and displays an arrow of that message.
     */
    private void clickedMessage (Message message) 
    {
        client.drawArrow(message);
    }
		       
    private static final String[] columnNames =
    {
        "Mission", 
        "Source", 
        "Destination", 
        "Type", 
        "Other"
    };
    private static final Class[] columnClasses =
    {
        String.class, 
        String.class, 
        String.class, 
        String.class, 
        String.class
    };
}
