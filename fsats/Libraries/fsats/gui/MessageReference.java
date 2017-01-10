package fsats.gui;

/**
 * Contains the state attributes of a messages.
 *
 * @author Bryan Tower (btower@arlut.utexas.edu)
 * @version 1.0, 04/014/99
 */
public class MessageReference {

  private Message message;     //The Message object.
  private Object  arrow;       //Reference to the gui arrow.
  private long    startTime;     //The time a message started.
  private int     timeOut;     //The time to remove the message.

  /**
   * Creates a new message with the given string.
   *
   * @param String  message
   * @param Object  arrow
   * @param long    startTime
   * @param int     timeOut
   */
  public MessageReference (Message message,
                           Object  arrow,
                           long    startTime,
                           int     timeOut)


 { 
    this.message     = message;
    this.arrow       = arrow;
    this.startTime   = startTime;
    this.timeOut     = timeOut;
  }
  
  /**
   * Get the Message object. 
   *
   * @return Message message 
   */
  public Message getMessage() {
    return this.message;
  }

  /**
   * Get the gui reference to the arrow for this message.
   *
   * @return Object arrow
   */
  public Object getarrow() {
    return this.arrow;
  }

  /**
   * Get the start time for this message.
   *
   * @return long startTime
   */
  public long getStartTime() {
    return this.startTime;
  }

  /**
   * Get the time out period for this message.
   *
   * @return int timeOut
   */
  public int getTimeOut() {
    return this.timeOut;
  }
}
