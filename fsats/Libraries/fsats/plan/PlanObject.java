package fsats.plan;

import java.io.PrintWriter;
import fsats.util.DataContainer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public abstract class PlanObject
{
    private ChangeEventMulticaster multicaster = new ChangeEventMulticaster();
    private int id;
    protected Plan plan;

    /**
     * Sets the int id.  The id should be >0 and unique among all instances
     * of this class in a given plan.
     */
    void setID( int id )
    {
        if ( id <= 0 )
            throw new IllegalArgumentException( "id must be > 0" );
        this.id = id;
    }

    void setPlan( Plan plan )
    {
        this.plan = plan;
    }

    /**
     * Register for ChangeEvents.
     */
    public void addChangeListener( ChangeListener listener )
    {
        multicaster.addListener( listener );
    }

    /**
     * De-register for ChangeEvents.
     */
    public void removeChangeListener( ChangeListener listener )
    {
        multicaster.removeListener( listener );
    }

    /**
     * Directs this opfac to notify it's listeners that it's state has changed.
     * Used for plan data like the opfac-to-net connections where mutator
     * methods exist outside of this class.
     */
    protected void fireChangeEvent()
    {
        multicaster.send( new ChangeEvent( this ) );
    }

    /**
     * Returns the unique id for this object.  Valid values are always greater
     * than zero.  
     */
    public int getID()
    {
        return id;
    }

    /**
     * Returns a meaningful name for the object.
     */
    abstract String getName();

    /**
     * Tests if all required attributes have been set.
     */
    public abstract boolean isComplete();
    
    /**
     * Returns a container representation of this object.
     */
    public abstract DataContainer toContainer();

    /**
     * Returns a container representation of this object.
     */
    public abstract DataContainer toContainer( String label );

}
