package fsats.gui;

import java.awt.*;
import java.util.*;
import javax.swing.event.*;

/**
 * Contains the mission for an opfac.
 *
 * @author Bryan Tower (btower@arlut.utexas.edu)
 * @version 1.0, 04/15/99
 */
public class Mission 
    extends MessageTableModel
    implements TableModelListener
{

  private String    missionId;
  private String    missionState;
  private String    missionString;
  private Location  targetLocation;

  private MessageTableModel allMessages;
  private Vector messages = new Vector();

  private Color messageColor=Color.red;
  //Color of the message lines for this mission.

  Mission(fsats.guiIf.Mission simMission, Client client)
  {
      super(client);
      update(simMission);
      allMessages = client.getMessageTableModel();
      allMessages.addTableModelListener(this);
      tableChanged(null);
  }

  public boolean equals(fsats.guiIf.Mission simMission)
  {
      return 
          missionId.equals(simMission.getId())
              && missionState.equals(simMission.getState())
              && targetLocation.equals(simMission.getTargetLocation());
  }

  public void update(fsats.guiIf.Mission simMission)
  {
      missionId = simMission.getId();
      missionState = simMission.getState();

      targetLocation = new Location(simMission.getTargetLocation());

      missionString = simMission.toString();
  }

  /** The message table has changed - see if I have more messages.  
   */
  public void tableChanged(TableModelEvent e)
  {
      messages.removeAllElements();
      for (int i=0; i<allMessages.getRowCount(); ++i)
      {
          Message m = allMessages.getMessageAt(i);
          if (m.getMissionID().equals(getMissionID()))
              messages.addElement(m);
      }
      fireTableDataChanged();
  }

  public int getRowCount() { return messages.size(); }

  public Message getMessageAt(int row)
  {
      return (Message)messages.elementAt(row);
  }

  protected Frame newWindow() { return new MissionWindow(this); }

  /**
   * Get the missionID for this mission.
   *
   * @return  String     missionID
   */
  public String getMissionID() { return missionId; }

  public String getMissionState() { return missionState; }

  /**
   * Get the target location for this mission.
   *
   * @return  Location targetLocation
   */
  public Location getTargetLocation() { return targetLocation; }

  /**
   * Get the color for the message lines for this mission.
   * @return Color messageColor 
   */
  public Color getColor() { return messageColor; }

  /**
   * Set the color for the message lines for this mission.
   * @param Color messageColor 
   */
  public void setColor(Color messageColor) { messageColor = messageColor; }

  /**
   * Return the string representation for this mission.
   *
   * @return  String missionString
   */
  public String toString() { return missionString; }

  /**
   * Verifys that the Missions are equal by comparing the MissionIDs.
   *
   * @return  boolean 
   */
  public boolean equals(Object mission) {
    if(mission instanceof Mission){
      Mission newMission = (Mission) mission;



      if(newMission.getMissionID().equals(getMissionID())){
        return true;
      }
    }
    return false;     
  }
}
