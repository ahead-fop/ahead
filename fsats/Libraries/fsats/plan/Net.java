package fsats.plan;

import fsats.util.DataContainer;
import fsats.util.DataContainerFormatException;
import java.sql.*;
import java.util.Iterator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The data that defines a network.
 */
public class Net extends PlanObject implements PlanContainerLabels
{
    private String name ; 
    private CommoType commo; 
    private String ipDomain;  // column nta_ip_domain


    public Net( String name )
    {
        this.name = name;
    }

    /*
     * Constructs a net using the contents of container for initialization 
     * and adds the new instance to the Plan.
     */
    Net( DataContainer cont, Plan plan) throws DataContainerFormatException
    {
        setPlan( plan );
        initFromContainer( cont );
        plan.addNet( this );
    }

    /**
     * Sets the net name iff no other net exists with the same name.
     */
    public void setName( String name )
    {
        if ( name != null && name.equals( this.name ) )
            return;
        if ( plan != null && plan.containsNet( name ) == true )
            throw new IllegalArgumentException( name );
        this.name = name;
        fireChangeEvent();
    }

    /**
     * Sets the commo configuration of the net.
     */
    public void setCommo( CommoType commo )
    {
        this.commo = commo ;
        fireChangeEvent();
    }

    public void setIPDomain( String ipDomain )
    {
        this.ipDomain = ipDomain;
        fireChangeEvent();
    }

    /**
     * Returns the net name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the commo configuration.
     */
    public CommoType getCommo()
    {
        return commo;
    }

    public String getIPDomain()
    {
        return ipDomain;
    }

    /**
     * Implements Verifiable.  Tests if all required attributes have been set.
     */
    public boolean isComplete()
    {
        return (name != null && commo != null);
    }

    /**
     * Two nets are equal iff they have the same net name.
     */
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null || !(obj instanceof Net) )
            return false;
        Net net2 = (Net) obj;

        return( ( name==null? net2.name==null: name.equals(net2.name) )
                && ( commo==null? net2.commo==null: commo.equals(net2.commo) )
                && ( ipDomain==null? 
                     net2.ipDomain==null: ipDomain.equals(net2.ipDomain) ) );
    }

    public String toString()
    {
        return name;
    }

    private void initFromContainer( DataContainer cont )
        throws DataContainerFormatException
    {
        int commoID = -1;
        try {
            setName( cont.getField( NET_NAME ).getValue() );
            setID( Integer.parseInt( cont.getField( NET_ID ).getValue() ) );
            setIPDomain( cont.getField( IP_DOMAIN ).getValue() );
            DataContainer commoIDCont = cont.getMultilevelField
                ( COMMO_TYPE + "." + COMMO_TYPE_ID );
            if ( !commoIDCont.isNull() )
                commoID = Integer.parseInt( commoIDCont.getValue( "-1" ) );
            setCommo( CommoType.get( commoID ) );
        } catch( NumberFormatException e ) {
            e.printStackTrace();
            throw new DataContainerFormatException();
        }
    }
        
    public DataContainer toContainer()
    {
        return toContainer( NET );
    }

    public DataContainer toContainer( String label )
    {
        DataContainer cont = new DataContainer( label );
        cont.addField( new DataContainer( NET_ID, "" + getID() ) );
        cont.addField( new DataContainer( NET_NAME, name ) );
        cont.addField( new DataContainer( IP_DOMAIN, ipDomain ) );
        if ( commo == null )
            cont.addField( new DataContainer( COMMO_TYPE ) );
        else
            cont.addField( commo.toContainer() );
        return cont;
    }

}
