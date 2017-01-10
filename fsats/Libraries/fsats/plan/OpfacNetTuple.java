package fsats.plan;

import java.sql.*;
import fsats.util.DataContainer;
import fsats.util.DataContainerFormatException;
import fsats.util.Log;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Stores the data needed to link an opfac to a net.
 */
public class OpfacNetTuple extends PlanObject implements PlanContainerLabels
{
    private Opfac opfac;
    private Net net;
    private String address;
    private int nad;
    private int useSerialization;
    private int highInitial;
    private int highSubsequent;
    private int lowInitial;
    private int lowSubsequent;
    private int stationRank;


    /**
     * throw IllegalArgumentException if opfac, net or address is null.
     */
    public OpfacNetTuple( Opfac opfac, Net net, String address )
    {
        if ( opfac==null || net==null || address==null )
            throw new IllegalArgumentException();
        this.opfac = opfac;
        this.net = net;        
        this.address = address;
    }

    /*
     * Constructs a OpfacNetTuple using the contents of container for 
     * initialization and adds the new instance to the Plan.
     */
    OpfacNetTuple( DataContainer container, Plan plan )
        throws DataContainerFormatException
    {
        setPlan( plan );
        initFromContainer( container, plan );
        plan.addOpfacNetTuple( this );
    }

    public void setNad( int nad )
    {
        this.nad = nad;
       fireChangeEvent();
    }

    public void setUseSerialization( int useSerialization )
    {
        this.useSerialization = useSerialization;
       fireChangeEvent();
    }

    public void setHighInitial( int highInitial )
    {
        this.highInitial = highInitial;
       fireChangeEvent();
    }

    public void setHighSubsequent( int highSubsequent )
    {
        this.highSubsequent = highSubsequent;
       fireChangeEvent();
    }

    public void setLowInitial( int lowInitial )
    {
        this.lowInitial = lowInitial;
       fireChangeEvent();
    }

    public void setLowSubsequent( int lowSubsequent )
    {
        this.lowSubsequent = lowSubsequent;
       fireChangeEvent();
    }

    public void setStationRank( int stationRank )
    {
        this.stationRank = stationRank;
       fireChangeEvent();
    }

    public String getName()
    {
        StringBuffer sb = new StringBuffer(50);
        if ( net.getName() != null )
            sb.append( net.getName() );
        sb.append( "_" );
        if ( opfac.getUnitName() != null )
            sb.append( opfac.getUnitName() );
        return sb.substring( 0, sb.length() );
    }

    public Opfac getOpfac()
    {
        return opfac;
    }

    public Net getNet()
    {
        return net;
    }

    public String getAddress()
    {
        return address;
    }

    public int getNad()
    {
        return nad;
    }

    public int getUseSerialization()
    {
        return useSerialization;
    }

    public int getHighInitial()
    {
        return highInitial;
    }

    public int getHighSubsequent()
    {
        return highSubsequent;
    }

    public int getLowInitial()
    {
        return lowInitial;
    }

    public int getLowSubsequent()
    {
        return lowSubsequent;
    }

    public int getStationRank()
    {
        return stationRank;
    }

    /**
     * Implements Verifiable.  Tests if all required attributes have been set.
     */
    public boolean isComplete()
    {
        return true;
    }

    /**
     * A convience function. Tests if this tuple contains the opfac and net.
     */
    public boolean contains( Opfac opfac, Net net )
    {
        return ( this.opfac.equals( opfac ) && this.net.equals( net ) );
    }

    /**
     * OpfacNetTuples are equal iff the nets are the same and the opfacs are
     * the same.  This is consistent with the constraint that an opfac
     * can only be on a given net once.
     */
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null || !(obj instanceof OpfacNetTuple) )
            return false;
        OpfacNetTuple t2 = (OpfacNetTuple) obj;
        return ( (opfac == null ? t2.opfac == null : opfac.equals(t2.opfac) )

                 && (net == null ? t2.net == null : net.equals( t2.net ) )

                 && (address == null ? 
                     t2.address == null : address.equals( t2.address ) )

                 && nad == t2.nad 
                 && useSerialization == t2.useSerialization
                 && highInitial == t2.highInitial 
                 && highSubsequent == t2.highSubsequent
                 && lowInitial == t2.lowInitial
                 && lowSubsequent == t2.lowSubsequent
                 && stationRank == t2.stationRank );
    }

    public DataContainer toContainer()
    {
        return toContainer( OPFAC_NET_TUPLE );
    }

    public DataContainer toContainer( String label )
    {
        DataContainer container = new DataContainer( label );

        container.addField( new DataContainer
            ( NET_NAME, net==null? "": net.getName() ) );

        container.addField( new DataContainer
            ( NET_ID, "" + (net==null? 0: net.getID()) ) );

        container.addField ( new DataContainer
            ( OPFAC_UNIT_NAME, (opfac==null? "" : opfac.getUnitName() ) ) );

        container.addField( new DataContainer
            ( OPFAC_ID, "" + (opfac==null? 0: opfac.getID()) ) );

        container.addField( new DataContainer( ADDRESS, address ) );

        container.addField( new DataContainer( NAD, "" + nad )  );

        container.addField( new DataContainer
            ( USE_SERIALIZATION, "" + useSerialization ) );

        container.addField( new DataContainer
            ( HIGH_INITIAL, "" + highInitial ) );

        container.addField( new DataContainer
            ( LOW_INITIAL, "" + lowInitial ) );

        container.addField( new DataContainer
            ( HIGH_SUBSEQUENT, "" + highSubsequent ) );

        container.addField( new DataContainer
            ( LOW_SUBSEQUENT, "" + lowSubsequent ) );

        container.addField( new DataContainer
            ( STATION_RANK, "" + stationRank ) );

        return container;
    }

    private void initFromContainer( DataContainer cont, Plan plan )
        throws DataContainerFormatException
    {
        address = cont.getField( ADDRESS ).getValue();

        try {
            setLowInitial( Integer.parseInt
                           ( cont.getField
                             ( LOW_INITIAL).getValue() ) );
                               
            setHighInitial( Integer.parseInt
                            ( cont.getField
                              ( HIGH_INITIAL ).getValue() ) );

            setLowSubsequent( Integer.parseInt
                              (cont.getField
                               ( LOW_SUBSEQUENT ).getValue()));
                
            setHighSubsequent( Integer.parseInt
                               (cont.getField
                                (HIGH_SUBSEQUENT).getValue()));

            setNad( Integer.parseInt
                    ( cont.getField
                      ( NAD ).getValue() ) );
                
            setUseSerialization( Integer.parseInt
                                 ( cont.getField
                                   ( USE_SERIALIZATION ).getValue() ) );

            setStationRank( Integer.parseInt
                            ( cont.getField
                              ( STATION_RANK ).getValue() ) );

            net = plan.getNet( Integer.parseInt
                               ( cont.getField
                                 ( NET_ID ).getValue("-1") ) );
                                   
            opfac = plan.getOpfac( Integer.parseInt
                                   ( cont.getField
                                     ( OPFAC_ID ).getValue("-1") ) );

        } catch( NumberFormatException e ) {
            Log.error( Log.PLAN, e );
            throw new DataContainerFormatException();
        }
    }

}
