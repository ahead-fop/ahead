package fsats.guiIf;

/** Information about a target.  */
public interface Target
{
    /** Type is ARMOR, INFANTRY, ARTILLERY... */
    String getType();

    /** Location of target. */
    Location getLocation();
}

