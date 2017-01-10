package fsats.plan;

import fsats.util.DataContainer;
import fsats.util.DataContainerFormatException;
import fsats.util.ReadOnlyIterator;
import fsats.util.ManyToOneMap;
import fsats.util.Log;
import java.util.*;
import java.sql.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class is an abstraction of a computer with or without a monitor.
 * @see Port
 * @see Plan#addProcessorNodeTuple
 */
public class Processor extends PlanObject implements PlanContainerLabels
{
    private String name;

    public Processor( String name )
    {
        this.name = name;
    }

    /*
     * Constructs a processor using the contents of container for 
     * initialization and adds the new instance to the Plan.
     */
    Processor( DataContainer cont, Plan plan )
        throws DataContainerFormatException
    {
        if ( cont == null || plan == null )
            throw new IllegalArgumentException();
        setPlan( plan );
        initFromContainer( cont );
        plan.addProcessor( this );
    }

    /**
     * Sets the name of the processor iff no other processor exists 
     * with the same name.
     */
    public void setName( String name )
    {
        if ( name != null && name.equals( this.name ) )
            return;
        if ( plan != null && plan.containsProcessor(name) )
            throw new IllegalArgumentException( name );
        this.name = name;
        fireChangeEvent();
    }

    /**
     * Returns the name of the processor.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Implements Verifiable.  Tests if all required attributes have been set.
     */
    public boolean isComplete()
    {
        return ( name != null );
    }

    /**
     * Two processors are equal iff they have the same name.
     */
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null || !(obj instanceof Processor) )
            return false;
        Processor p2 = (Processor) obj;
        return ( (name == null ? p2.name == null : name.equals(p2.name)) );
    }

    public String toString()
    {
        return "" + name;
    }

    public DataContainer toContainer()
    {
        return toContainer( PROCESSOR );
    }

    public DataContainer toContainer( String label )
    {
        DataContainer cont = new DataContainer( label );
        cont.addField( new DataContainer( PROCESSOR_NAME, name ));
        cont.addField( new DataContainer( PROCESSOR_ID, "" + getID() ) );
        return cont;
    }

    private void initFromContainer( DataContainer cont )
        throws DataContainerFormatException
    {
        setName( cont.getField( PROCESSOR_NAME ).getValue() );
        String idStr= cont.getField( PROCESSOR_ID ).getValue();
        try {
            setID( Integer.parseInt( idStr ) );
        }  catch( NumberFormatException e ) {
            Log.error( Log.PLAN, e );
            throw new DataContainerFormatException();
        }
    }
    
}
