package fsats.gui;

import java.awt.*;
import java.util.*;

/**
 * Updates any changes to the list of messages.
 *
 * @author Bryan Tower (btower@arlut.utexas.edu)
 * @version 1.0, 04/13/99
 */
class MessageModel 
    extends MessageTableModel
{
  private Vector messageList = new Vector();

  public MessageModel(Client client) { super(client); }
    
  /**
   * Adds a message to this model.
   *
   * @param Message message
   */
  public void addMessage (Message  message) 
  {
      int i = messageList.size();
      messageList.addElement(message);
      fireTableRowsInserted(i, i);
  }
  
  /**
   * Remove the specified Message.
   * @param MessageReference messageRef
   */
  public void removeMessage(MessageReference   messageRef)
    {
      Message message = messageRef.getMessage();
      int index = messageList.indexOf(message);
      messageList.removeElementAt(index);
      fireTableRowsDeleted(index, index);                    
    }    
  
  /**
   * Return all Messages in the model.
   */
  public Enumeration getMessages()
    {
      return messageList.elements();
    }
  
    

  /**
   * Return all Messages in the model that are for a given mission.
   */
  public Vector getMessages(Mission mission)
    {
      //System.out.println("MessageModel - getMessages()");
      Vector missionMessages = new Vector(10);
      //The missionID that we are looking for on the vector of messages.
      String inputMissionID = mission.getMissionID().toString();

      //Loop through the message vector and look for any
      //messages for the given missionID.
      for (int i = 0; i < messageList.size(); i++){


        Message message = (Message) messageList.elementAt(i);
        //Get the missionID for this message
        String missionID = message.getMissionID().toString();



        if(missionID.equals(inputMissionID)){
          //System.out.println("MessageModel - I am adding a message to the list");
          missionMessages.addElement(message);
        }
      }

      return missionMessages;
    }
  
    // MessageTableModel stuff.

    /**
     * Return the Message in the specified row.
     */
    public Message getMessage(int rowNum) { return getMessageAt(rowNum); }
    public Message getMessageAt(int row) 
    { 
        return (Message)messageList.elementAt(row);
    }

    public int getRowCount() { return messageList.size(); }

    protected Frame newWindow()
    {
        return new ListWindow("All Messages", this);
    }
}
