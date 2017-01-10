package fsats.gui;

import java.awt.*;
import java.util.*;

/**
 * Updates any changes to the list of missions.
 * The list of missions does not contain any duplicats.
 *
 * @author Bryan Tower (btower@arlut.utexas.edu)
 * @version 1.0, 04/16/99
 */
class MissionModel 
    extends MissionTableModel
{
  public MissionModel(Client client) { super(client); }

  private Vector missionList = new Vector();
    
  //TESTING!!!
  Color[] colors = {Color.blue,
                    Color.yellow, 
                    Color.cyan,
                    Color.green,
                    Color.magenta,
                    Color.orange,
                    Color.pink};
  //Color.black, RESERVED FOR GRID LINES!!
  //Color.gray, RESERVED FOR BACKGROUND
  //Color.red RESERVED FOR TARGETS!!
  //Color.white RESERVED FOR BACKGROUND
  //Color.darkGray, RESERVED FOR BACKGROUND
  //Color.lightGray, RESERVED FOR BACKGROUND


  int colorCount = 0;
   
   
   
   
    private int indexOfName(String name)
    {
        for (int i=0; i<missionList.size(); ++i)
        {
            Mission mission=(Mission)missionList.elementAt(i);
            if (mission.getMissionID().equals(name)) 
                return i;
        }
        return -1;
    }

    public Mission getMissionNamed(String id)
    {
        int i=indexOfName(id);
        return i<0 ? null : getMissionAt(i);
    }

  public Mission addMission(fsats.guiIf.Mission simMission)
  {
      int i=indexOfName(simMission.getId());
      Mission mission = null;
      if (i<0)
      {
          i=missionList.size();
          mission = new Mission(simMission, client);
          missionList.addElement(mission);
          fireTableRowsInserted(i, i);
      }
      else
      {
          mission = getMissionAt(i);
          mission.update(simMission);
          fireTableRowsUpdated(i, i);
      }
      return mission;
  }
  
    /**
     * Return all Missions in the model.
     */
    public Enumeration getMissions()
    {
      return missionList.elements();
    }
  

    /**
     * Return the Mission in the specified row.
     */
    public Mission getMission(int rowNum) { return getMissionAt(rowNum); }
    public Mission getMissionAt(int row) 
    { 
        return (Mission)missionList.elementAt(row);
    }

    public int getRowCount()
    {
      return missionList.size();
    }

    protected Frame newWindow() 
    { 
        return new ListWindow("All Missions", this);
    }

    public Color getMissionColor(String missionID)
    {
      //Find the given mission on the vector.
      Color returnColor = Color.blue;
      Mission mission = null;
      int i=indexOfName(missionID);
      if (i>=0)
      {
          mission=(Mission)missionList.elementAt(i);
          returnColor=mission.getColor();
      }
      return returnColor;
    }

    
}
