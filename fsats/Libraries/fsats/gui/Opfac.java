//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Mon Mar 22 14:41:20 1999

package fsats.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Opfac
    extends MissionTableModel
{
    private String subscriber;
    private int unit;
    private String type;
    private int name;
    private final SortedMap missions = new TreeMap();
    private Mission[] missionArray = new Mission[0];
    private fsats.guiIf.Opfac simOpfac;
    private CoordinateConstraints      coordinates;

    Opfac(fsats.guiIf.Opfac simOpfac, Client client)
    {
        super(client);
        update(simOpfac);
	this.simOpfac = simOpfac;
    }

    protected void addMission(fsats.guiIf.Mission simMission)
    {
        String id = simMission.getId();
        Mission mission = (Mission)missions.get(id);
        if (mission==null)
        {
            mission = new Mission(simMission, client);
            missions.put(id, mission);
            client.addMission(simMission);
        }
        else 
        {
            if (!mission.equals(simMission))
                client.addMission(simMission);
            mission.update(simMission);
        }
    }

    public void update(fsats.guiIf.Opfac simOpfac)
    {
        subscriber = simOpfac.getId();
        type = simOpfac.getType();
        coordinates = new CoordinateConstraints(simOpfac.getLocation());

        fsats.guiIf.Mission[] active = simOpfac.getActiveMissions();
        for (int i=0; i<active.length; ++i)
            addMission(active[i]);

        // This array is used for indexing by table listeners.
        missionArray = (Mission[])missions.values().toArray(missionArray);

        fireTableDataChanged();
    }

    public OpfacIcon getIcon()
    {
        OpfacIcon icon = new OpfacIcon(getType());
        icon.setFillColor(Color.white);
        return icon;
    }
    

  /**
   * Returns the location on the grid for this Opfac.
   *
   * @return CoordinateConstraints  coordinates
   */
  public Location getLocation () { return coordinates.getLocation(); }

  /**
   * Returns the coordinates for this LocalOpfac.
   *
   * @return CoordinateConstraints  coordinates
   */
  public CoordinateConstraints getCoordinates () { return coordinates; }

  /**
   * Adds a new target.
   *
   * @param id String mission ID
   * @param target fsats.guiIf.Target object to add
   */
  public void putTarget (String id, fsats.guiIf.Target target) {
    simOpfac.putTarget(id, target);
  }
    
  /**
   * Sets the coordinates for this LocalOpfac.
   *
   * @param CoordinateConstraints  coordinates
   */
  public void  setCoordinates (CoordinateConstraints coordinates) {
    this.coordinates = coordinates;
  }


    public String getSubscriber()
    {
        return subscriber;
    }
    
    public int getUnit()
    {
        return unit;
    }

    public String getType()
    {
        return type;
    }

    public int getName()
    {
      return name;
    }
    
    // MissionTableModel stuff.

    public int getRowCount() { return missionArray.length; }

    public Mission getMissionAt(int row) 
    { 
        return missionArray[row];
    }

    protected Frame newWindow()
    {
        return new OpfacWindow(this);
    }
}

