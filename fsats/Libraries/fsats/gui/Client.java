//                              -*- Mode: Java -*-
// Version         : 1.0
// Author          :
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Thu Jun  1 11:40:00 2000

package fsats.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import fsats.gui.event.*;
import excis.demo.ShowDiagram;                //ExCIS
import excis.demo.SequenceDiagram;            //ExCIS

/**
 * Client handles all of the management of the gridpane
 * and the creation of the OpfacModel and MessageModel.
 *
 * @author Bryan Tower (btower@arlut.utexas.edu)
 * @version 1.0, 04/07/99
 */
class Client 
    implements TableModelListener
{

  private  final OpfacModel opfacModel = new OpfacModel(this);
  private  final MessageModel messageModel = new MessageModel(this);
  private  final MissionModel missionModel = new MissionModel(this);

  private        ShowDiagram messageSequenceChart;            //ExCIS
  private        boolean     sequenceChartOpen = false;       //ExCIS

  private GridPane grid;  //Grid panel reference from main panel.
  private boolean  displayMessageArrows=true;
  private boolean  accumulateMessageArrows=false;
  
  public Client (GridPane grid) 
  {
    this.grid = grid;

    //Now register to listen on the Models for any table updates.
    opfacModel.addTableModelListener(this);
    messageModel.addTableModelListener(this);
    missionModel.addTableModelListener(this);
  }

  
  private void messageTableChanged(TableModelEvent e){
    if(e.getType() == TableModelEvent.INSERT){
        int row = e.getLastRow();

        //Now get the message from the Model.
        Message message = messageModel.getMessage(row);

	if (displayMessageArrows) 
	  drawArrow(message);

	if (sequenceChartOpen) {                           //ExCIS
	    messageSequenceChart.displayMessage(message);  //ExCIS
	}                                                  //ExCIS
    }
  }

  private final Map missionButtons = new HashMap();

  private void missionTableChanged(TableModelEvent e)
  {
    int row = e.getLastRow();

    if(e.getType() != TableModelEvent.DELETE){
        Mission mission = missionModel.getMission(row);
        MissionButton iconB = 
            (MissionButton)missionButtons.get(mission.getMissionID());
        if (iconB==null) 
        {
            iconB = new MissionButton(mission);
            missionButtons.put(mission.getMissionID(), iconB);
        }
        else
            grid.remove(iconB);
        
        //Now place that new target on the grid on top of any others.
        grid.addTop(
            iconB, new CoordinateConstraints(mission.getTargetLocation()));
        grid.revalidate();
        grid.repaint();        
    }
  }

    private final Map opfacButtons = new HashMap();

    private OpfacButton getOpfacButton(String name)
    {
        return (OpfacButton)opfacButtons.get(name);
    }

  private void opfacTableChanged(TableModelEvent e){
    if(e.getType() == TableModelEvent.INSERT){
        int row = e.getLastRow();

        //Now get the opfac from the OpfacModel.
        Opfac opfac = opfacModel.getOpfac(row);
        JButton iconB = new OpfacButton(opfac);
        opfacButtons.put(opfac.getSubscriber(), iconB);
        
        //Now place that new opfac on the grid.
        grid.addBottom(iconB, opfac.getCoordinates());
        grid.revalidate();
        grid.repaint();
    }
  }

  public void tableChanged(TableModelEvent e){
      if (e.getSource()==opfacModel)
          opfacTableChanged(e);
      else if (e.getSource()==missionModel)
          missionTableChanged(e);
      else if (e.getSource()==messageModel)
          messageTableChanged(e);
  }

  /**
   * Renders an arrow between a source and destination - currently
   * removes any arrows that exist between the two components.
   *
   * @param src String message source
   * @param dest String message destination
   * @param text String arrow text
   * @return arrow reference Object, null if there was a problem - 
   *         can be used later to refer to the arrow
   */
  public void drawArrow (Message message)
  {
      OpfacButton from = getOpfacButton(message.getSource());
      OpfacButton to = getOpfacButton(message.getDestination());
      String text = message.getType();
      if (accumulateMessageArrows)
          grid.removeArrows(from, to);
      else
          grid.clearArrows();

      grid.drawArrow(from, to, Color.red, message.getType());
  }
  
  public void setAccumulateMessageArrows(boolean value)
  {
      accumulateMessageArrows=value;
  }

  public boolean getAccumulateMessageArrows()
  {
      return accumulateMessageArrows;
  }

  /**
   * Returns whether or not new Messages are displayed as arrows.
   *
   * @return boolean true if new messages are displayed as arrows
   */
  public boolean isDisplayMessageArrows () {
    return displayMessageArrows;
  }
  
  /**
   * Sets whether or not new Messages are displayed as arrows on the
   * screen.
   *
   * @param display boolean true if should display new messages
   */
  public void setDisplayMessageArrows (boolean display) {
    displayMessageArrows = display;
  }

  /**
   * Adds a Target to each Opfac.
   *
   * @param id String mission ID
   * @param target Target object to add
   */
  public void putTarget (String id, fsats.guiIf.Target target) {

    Enumeration opfacs = opfacModel.getOpfacs();
    while (opfacs.hasMoreElements()) {
      Opfac opfac = (Opfac)opfacs.nextElement();
      opfac.putTarget(id, target);
    }
  }
  
    /**
     * Launches and maintains the Message List Window.
     */
    public void manageMessageListWindow() 
    {
       messageModel.openWindow();
    }
  
    public Message addMessage(fsats.guiIf.Message simMessage)
    {
        Message message = new Message(simMessage);
        messageModel.addMessage(message);
        return message;
    }

    public MessageTableModel getMessageTableModel() { return messageModel; }

  
  
    /**                                                              //ExCIS
     * Launches and maintains the Message Sequence Diagram Window.   //ExCIS
     */                                                              //ExCIS
    public void manageMessageSequenceChartWindow()                   //ExCIS
    {                                                                //ExCIS
	messageSequenceChart = new ShowDiagram();                    //ExCIS
	sequenceChartOpen = true;                                    //ExCIS
    }                                                                //ExCIS
  
  
  
    /**
     * Launches and maintains the Opfac List Window.
     */
    public void manageOpfacListWindow() 
    {
        opfacModel.openWindow();
    }
  
    public Opfac addOpfac(fsats.guiIf.Opfac simOpfac)
    {
        return opfacModel.addOpfac(simOpfac);
    }

    public Opfac getOpfac(String id) 
    { 
        return opfacModel.getOpfac(id); 
    }
      

    /**
     * Launches and maintains the Mission List Window.
     */
    public void manageMissionListWindow() 
    {
        missionModel.openWindow();
    }

    public Mission getMission(String id)
    {
        return missionModel.getMissionNamed(id);
    }

    public void addMission(fsats.guiIf.Mission simMission)
    {
        missionModel.addMission(simMission);
    }
}


class OpfacButton
    extends JButton
    implements ActionListener
{
    private Opfac myOpfac;

    public OpfacButton(Opfac opfac)
    {
        super(opfac.getIcon());
        myOpfac=opfac;
        addActionListener(this);
        setBorderPainted(false);
        setMargin(new Insets(0,0,0,0));
    }

    public void actionPerformed(ActionEvent e)
    {
        myOpfac.openWindow();
    }
}

class MissionButton
    extends JButton
    implements ActionListener
{
    private Mission myMission;

    public MissionButton(Mission mission)
    {
        super(new MissionIcon(mission.getMissionID(), 3, true));
        myMission=mission;
        addActionListener(this);
        setBorderPainted(false);
        setMargin(new Insets(0,0,0,0));
    }

    public void actionPerformed(ActionEvent e)
    {
        myMission.openWindow();
    }
}

