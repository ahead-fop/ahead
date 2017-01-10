package fsats.plan;

import fsats.util.DataContainer;
import fsats.util.DataContainerFormatException;
import fsats.util.ManyToOneMap;
import fsats.util.ReadOnlyIterator;
import fsats.util.Log;
import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * An abstraction of one or more computers on a LAN.  One of the computers
 * hosts the UI.  One of the computers contains the archive device.
 * @see Processor
 * @see ProcessorNodeTuple
 * @see Plan#addProcessorNodeTuple
 */
public class Node extends PlanObject implements PlanContainerLabels
{
    private String name;
    private Processor uiProcessor;
    private Processor archiveProcessor;
    private ArchiveDevice archiveDevice;

    public Node( String name )
    {
        this.name = name;
    }

    /*
     * Constructs a node using the contents of container for initialization 
     * and adds the new instance to the Plan.
     */
    Node( DataContainer cont, Plan plan ) throws DataContainerFormatException

    {
        setPlan( plan );
        initFromContainer( cont );
        plan.addNode( this );
    }

    /**
     * Sets the node name iff no other node exists with the same name.
     */
    public void setName( String name )
    {
        if ( name != null && name.equals( this.name ) )
            return;
        if ( plan != null && plan.containsNode( name ) == true ) 
            throw new IllegalArgumentException( name );
        this.name = name;
        fireChangeEvent();
    }

    /**
     * Sets the processor where the UI will run iff this node has been
     * added to a plan and the processor is in that plan.
     */
    public void setUIProcessor( Processor processor )
    {
        if ( plan == null || !plan.contains( processor ) )
            return;
        uiProcessor = processor;
        fireChangeEvent();
    }

    /**
     * Sets the processor where the archiver will run.
     */
    public void setArchiveProcessor( Processor processor )
    {
        if ( plan == null || !plan.contains( processor ) )
            return;
        archiveProcessor = processor;
        fireChangeEvent();
    }

    public void setArchiveDevice( ArchiveDevice device )
    {
        this.archiveDevice = device;
        fireChangeEvent();
    }

    /**
     * Returns the node name.
     */
    public String getName()
    {
        return name;
    }

    public Processor getUIProcessor()
    {
        return uiProcessor;
    }

    public Processor getArchiveProcessor()
    {
        return archiveProcessor;
    }

    public ArchiveDevice getArchiveDevice()
    {
        return archiveDevice;
    }

    /**
     * Implements Verifiable.  Tests if all required attributes have been set.
     */
    public boolean isComplete()
    {
        return (name != null && uiProcessor != null);
    }

    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null || !(obj instanceof Node ) )
            return false;
        Node n2 = (Node) obj;
        boolean result =
            ( (name == null ? n2.name == null : name.equals(n2.name) )

              && (uiProcessor == null ? n2.uiProcessor == null 
                  : uiProcessor.equals(n2.uiProcessor) )

              && (archiveProcessor == null ? n2.archiveProcessor == null 
                  : archiveProcessor.equals(n2.archiveProcessor) )

              && (archiveDevice == null ? n2.archiveDevice == null :
                  archiveDevice.equals(n2.archiveDevice) ) );
        return result;
    }

    public String toString()
    {
        return name;
    }

    private void initFromContainer( DataContainer cont )
        throws DataContainerFormatException
    {
        setName( cont.getField( NODE_NAME ).getValue() );
        String nodeID = cont.getField( NODE_ID ).getValue();
        String uiProcID = cont.getField( UI_PROCESSOR_ID ).getValue();
        String archProcID = cont.getField( ARCHIVE_PROCESSOR_ID ).getValue();
        try {
            setID( Integer.parseInt( nodeID ) );
            setUIProcessor( plan.getProcessor( Integer.parseInt(uiProcID) ) );
            setArchiveProcessor( plan.getProcessor
                                 ( Integer.parseInt( archProcID ) ) );

        } catch ( NumberFormatException e ) {
            Log.error( Log.PLAN, e );
            throw new DataContainerFormatException();
        }
        DataContainer archiveDeviceCont = cont.getField( ARCHIVE_DEVICE );
        if ( !archiveDeviceCont.isNull() ) 
            setArchiveDevice( new ArchiveDevice( archiveDeviceCont ) );
    }
    
    public DataContainer toContainer()
    {
        return toContainer( NODE );
    }

    public DataContainer toContainer( String label )
    {
        DataContainer cont = new DataContainer( label );

        String uiProcName = (uiProcessor==null? "": uiProcessor.getName() );
        int uiProcID = (uiProcessor==null? -1: uiProcessor.getID() );
        String archProcName = (archiveProcessor==null? 
                               "": archiveProcessor.getName() );
        int archProcID= (archiveProcessor==null? -1: archiveProcessor.getID());

        cont.addField( new DataContainer( NODE_NAME, name ) );
        cont.addField( new DataContainer( NODE_ID, "" + getID() ) );
        cont.addField( new DataContainer( UI_PROCESSOR_NAME, uiProcName) );
        cont.addField( new DataContainer( UI_PROCESSOR_ID, "" + uiProcID) );
        cont.addField( new DataContainer( ARCHIVE_PROCESSOR_NAME, 
                                          archProcName) );
        cont.addField( new DataContainer( ARCHIVE_PROCESSOR_ID, 
                                          "" + archProcID) );

        if ( archiveDevice != null )
            cont.addField( archiveDevice.toContainer() );

        return cont;
    }
    
}
