package fsats.gui;

import javax.swing.*;

/** Window for displaying a mission and its associated list of messages.
 */
class MissionWindow 
    extends ListWindow
{
    MissionWindow(Mission mission)
    {
        super("Mission "+mission.getMissionID(), mission);
        update(mission);
        setVisible(true);
    }

    void update(Mission mission)
    {
        top.removeAll();
        top.add(new JLabel("Mission ID: "+mission.getMissionID()));
        Location l = mission.getTargetLocation();
        top.add(new JLabel("Latitude: "+l.getLatitudeDMS()));
        top.add(new JLabel("Longitude: "+l.getLongitudeDMS()));

        repaint();
    }
}

