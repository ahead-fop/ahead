package fsats.gui;

/** TableModel for a list of missions.
 */
abstract class MissionTableModel
    extends CommonTableModel
{
    public MissionTableModel(Client client) { super(client); }

    public String getColumnName(int column) { return columnNames[column]; }
    public Class getColumnClass(int column) { return columnClasses[column]; }
    public int getColumnCount() { return columnNames.length; }

    public abstract Mission getMissionAt(int row);

    public Object getValueAt(int row, int column)
    {
        Mission mission = getMissionAt(row);
        Object value=null;
        switch (column)
        {
        case 0: 
            value=mission.getMissionID(); 
            break;
        case 1: 
            value= mission.getTargetLocation().getLatitudeDMS(); 
            break;
        case 2: 
            value= mission.getTargetLocation().getLongitudeDMS(); 
            break;
        case 3: 
            value=mission.getMissionState();
            break;
        }
        return value;
    }

    protected void cellClicked(int row, int column)
    {
        Mission mission = getMissionAt(row);
        switch (column)
        {
        case 0: clickedMission(mission.getMissionID()); break;
        }
    }

    private static final String[] columnNames =
    {
        "Mission", 
        "Latitude", 
        "Longitude",
        "State"
    };
    private static final Class[] columnClasses =
    {
        String.class, 
        String.class, 
        String.class,
        String.class
    };
}
