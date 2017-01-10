package fsats.guiIf;

/** 
 * Information about a mission thread.
 */
public interface Mission
{
    String getId();
    Location getTargetLocation();
    String getState();
    String toString();
}

