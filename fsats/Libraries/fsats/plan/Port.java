package fsats.plan;

import java.sql.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import fsats.util.DataContainer;
import fsats.util.DataContainerFormatException;
import fsats.util.Log;


/**
 * A Port is an abstraction for a modem attached to a net.
 * The PortName specifies the modem type.
 */
public class Port extends PlanObject implements PlanContainerLabels
{
    private PortName portName = PortName.UNKNOWN;
    private Processor processor;
    private Net net;
    private boolean isLogged = true;

    public Port( PortName name )
    {
        this.portName = (name==null? PortName.UNKNOWN: name);
    }
    
    /*
     * Constructs a port using the contents of container for initialization 
     * and adds the new instance to the Plan.
     */
    Port ( DataContainer cont, Plan plan )
        throws DataContainerFormatException
    { 
        this.plan = plan;
        initFromContainer( cont );
        plan.addPort( this );
    }

    /**
     * Sets the portName iff there is no other port associated with
     * this port's processor which has the same portName.
     */
    public void setPortName( PortName name )
    {
        if ( name != null && name.equals( portName ) )
            return;
        if ( plan != null && processor != null 
             && plan.containsPort( processor, portName ) )
            throw new IllegalArgumentException( name.toString() );
        this.portName = (name==null? PortName.UNKNOWN: name);
        fireChangeEvent();
    }

    /**
     * Sets the processor iff there is no other port with the same portName
     * associated with the processor.
     */
    public void setProcessor( Processor processor )
    {
        if ( plan != null && plan.containsPort( processor, portName ) )
            return;
        this.processor = processor;
        fireChangeEvent();
    }

    public void setNet( Net net )
    {
        this.net = net;
        fireChangeEvent();
    }

    /**
     * Sets whether or not the port is to be logged.
     */
    public void setIsLogged( boolean isLogged )
    {
        this.isLogged = isLogged;
        fireChangeEvent();
    }

    public String getName()
    {
        return portName.toString();
    }

    public PortName getPortName()
    {
        return portName;
    }
    
    public Processor getProcessor()
    {
        return processor;
    }

    public Net getNet()
    {
        return net;
    }

    public boolean isLogged()
    {
        return isLogged;
    }

    /**
     * Tests if all required attributes have been set.
     */
    public boolean isComplete()
    {
        return ( portName != null && processor != null );
    }

    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;

        if ( obj == null || !(obj instanceof Port) )
            return false;

        Port port2 = (Port) obj;

        boolean result =
            ( (portName == null ? 
               port2.portName == null : portName.equals(port2.portName) )

              && (processor == null ? port2.processor == null :
                  processor.equals( port2.processor ) )

              && (net == null ? port2.net == null : net.equals(port2.net) )

              && (isLogged == port2.isLogged) );

        return result;
    }

    public DataContainer toContainer()
    {
        return toContainer( PORT );
    }

    public DataContainer toContainer( String label )
    {
        DataContainer cont = new DataContainer( label );
        String name = ( portName == null ? "" : portName.toString() );
        cont.addField( new DataContainer( PORT_NAME, name ) );
        cont.addField( new DataContainer( PORT_ID, "" + getID() ) );

        String netName = (net== null ? null: net.getName() );
        cont.addField( new DataContainer( NET_NAME, netName ) );
        int netID = (net==null? -1: net.getID() );
        cont.addField( new DataContainer( NET_ID, "" + netID ) );

        String processorName = (processor== null ? null: processor.getName() );
        cont.addField( new DataContainer( PROCESSOR_NAME, processorName ) );
        int processorID = (processor==null? -1: processor.getID() );
        cont.addField( new DataContainer( PROCESSOR_ID, "" + processorID ) );

        cont.addField( new DataContainer( IS_LOGGED, "" + isLogged ) );
        return cont;
    }

    private void initFromContainer( DataContainer portCont )
        throws DataContainerFormatException
    {
        String str = null;
        
        try {
            str = portCont.getField( PORT_NAME ).getValue();
            setPortName( PortName.fromString( str ) );

            str = portCont.getField( PORT_ID ).getValue();
            setID( Integer.parseInt( str ) );

            int processorID = 
                Integer.parseInt(portCont.getField(PROCESSOR_ID).getValue());
            setProcessor( plan.getProcessor( processorID ) );

            int netID = Integer.parseInt(portCont.getField(NET_ID).getValue());
            setNet( plan.getNet( netID ) );

            Boolean b = new Boolean( portCont.getField( IS_LOGGED).getValue());
            isLogged = b.booleanValue();
        } catch ( NumberFormatException e ) {
            Log.error( Log.PLAN, e );
            throw new DataContainerFormatException();
        } catch ( PortNameStringException e ) {
            Log.error( Log.PLAN, e );
            throw new DataContainerFormatException();
        }
    }

}
