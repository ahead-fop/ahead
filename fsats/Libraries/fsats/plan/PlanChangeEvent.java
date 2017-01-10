package fsats.plan;

import javax.swing.event.ChangeEvent;


/**
 * Indicates a change in a Plan instance.
 * @see PlanChangeAdapter
 */
public class PlanChangeEvent extends ChangeEvent
{
    private String changeType;

    /**
     * Indicates an object was added.
     */
    public static final String ADDED = "ADDED";

    /**
     * Indicates an object was removed.
     */
    public static final String REMOVED = "REMOVED";

    /**
     * Indicates an object was modified.
     */
    public static final String MODIFIED = "MODIFIED";


    public PlanChangeEvent( String changeType, Object source )
    {
        super( source );
        this.changeType = changeType;
    }

    public String getType()
    {
        return changeType;
    }

    public String toString()
    {
        return "PlanChangeEvent: source=" + source + " type=" + changeType;
    }

}
