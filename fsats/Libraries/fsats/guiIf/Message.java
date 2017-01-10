package fsats.guiIf;


/** 
 * Interface for a one way communication between two opfacs 
 */
public interface Message 
{
    /** MessageImpl type. fr;grid, fo;cmd, etc. */
    String getType();
    
    /** ID of sending OpfacImpl */
    String getSource();
    
    /** ID of destination OpfacImpl */
    String getDestination();
    
    /** ID of mission this message is for */
    String getMissionId();
    
    /** Any interesting information not otherwise available. */
    String getSpecific();
}
    
