package fsats.plan;

import java.util.*;

/**
 * Enumeration of the ports which may be assigned to an processor.
 * A port name specifies both the modem type and the physical
 * address/location of the modem.
 */
public class PortName implements Comparable
{
    private static TreeMap map = new TreeMap();


    /**
     * Returns a PortName such that: portName.toString().equals( s ).
     * @throws PortNameStringException if there is no such string mapping.
     */
    public static PortName fromString( String s )
           throws PortNameStringException
    {
        PortName port = ( s == null? null: (PortName) map.get( s ) );
        if ( port == null )
           throw new PortNameStringException( s );
        return port;
    }

    /**
     * Returns a iteration of the string equivalents of the enumeration.
     * The class returned is String.  Order is guaranteed to be ascending.
     */
    public static Iterator iterator()
    {
        return map.keySet().iterator();
    }

    public final static PortName MODEM_PORT_1 = new PortName( "MODEM_PORT_1" );
    public final static PortName MODEM_PORT_2 = new PortName( "MODEM_PORT_2" );
    public final static PortName MODEM_PORT_3 = new PortName( "MODEM_PORT_3" );
    public final static PortName MODEM_PORT_4 = new PortName( "MODEM_PORT_4" );
    public final static PortName MODEM_PORT_5 = new PortName( "MODEM_PORT_5" );
    public final static PortName MODEM_PORT_6 = new PortName( "MODEM_PORT_6" );
    public final static PortName MODEM_PORT_7 = new PortName( "MODEM_PORT_7" );
    public final static PortName MODEM_PORT_8 = new PortName( "MODEM_PORT_8" );

    public final static PortName DME_0_CH_1 = new PortName( "DME_0_CH_1" );
    public final static PortName DME_0_CH_2 = new PortName( "DME_0_CH_2" );
    public final static PortName DME_1_CH_1 = new PortName( "DME_1_CH_1" );
    public final static PortName DME_1_CH_2 = new PortName( "DME_1_CH_2" );
    public final static PortName DME_2_CH_1 = new PortName( "DME_2_CH_1" );
    public final static PortName DME_2_CH_2 = new PortName( "DME_2_CH_2" );
    public final static PortName DME_3_CH_1 = new PortName( "DME_3_CH_1" );
    public final static PortName DME_3_CH_2 = new PortName( "DME_3_CH_2" );
    public final static PortName DME_4_CH_1 = new PortName( "DME_4_CH_1" );
    public final static PortName DME_4_CH_2 = new PortName( "DME_4_CH_2" );
    public final static PortName DME_5_CH_1 = new PortName( "DME_5_CH_1" );
    public final static PortName DME_5_CH_2 = new PortName( "DME_5_CH_2" );
    public final static PortName DME_6_CH_1 = new PortName( "DME_6_CH_1" );
    public final static PortName DME_6_CH_2 = new PortName( "DME_6_CH_2" );

    public final static PortName SIMULATED_01 = new PortName( "SIMULATED_01" );
    public final static PortName SIMULATED_02 = new PortName( "SIMULATED_02" );
    public final static PortName SIMULATED_03 = new PortName( "SIMULATED_03" );
    public final static PortName SIMULATED_04 = new PortName( "SIMULATED_04" );
    public final static PortName SIMULATED_05 = new PortName( "SIMULATED_05" );
    public final static PortName SIMULATED_06 = new PortName( "SIMULATED_06" );
    public final static PortName SIMULATED_07 = new PortName( "SIMULATED_07" );
    public final static PortName SIMULATED_08 = new PortName( "SIMULATED_08" );
    public final static PortName SIMULATED_09 = new PortName( "SIMULATED_09" );
    public final static PortName SIMULATED_10 = new PortName( "SIMULATED_10" );
    public final static PortName SIMULATED_11 = new PortName( "SIMULATED_11" );
    public final static PortName SIMULATED_12 = new PortName( "SIMULATED_12" );
    public final static PortName SIMULATED_13 = new PortName( "SIMULATED_13" );
    public final static PortName SIMULATED_14 = new PortName( "SIMULATED_14" );
    public final static PortName SIMULATED_15 = new PortName( "SIMULATED_15" );
    public final static PortName SIMULATED_16 = new PortName( "SIMULATED_16" );
    public final static PortName SIMULATED_17 = new PortName( "SIMULATED_17" );
    public final static PortName SIMULATED_18 = new PortName( "SIMULATED_18" );
    public final static PortName SIMULATED_19 = new PortName( "SIMULATED_19" );
    public final static PortName SIMULATED_20 = new PortName( "SIMULATED_20" );
    public final static PortName SIMULATED_21 = new PortName( "SIMULATED_21" );
    public final static PortName SIMULATED_22 = new PortName( "SIMULATED_22" );
    public final static PortName SIMULATED_23 = new PortName( "SIMULATED_23" );
    public final static PortName SIMULATED_24 = new PortName( "SIMULATED_24" );
    public final static PortName SIMULATED_25 = new PortName( "SIMULATED_25" );
    public final static PortName SIMULATED_26 = new PortName( "SIMULATED_26" );
    public final static PortName SIMULATED_27 = new PortName( "SIMULATED_27" );
    public final static PortName SIMULATED_28 = new PortName( "SIMULATED_28" );
    public final static PortName SIMULATED_29 = new PortName( "SIMULATED_29" );
    public final static PortName SIMULATED_30 = new PortName( "SIMULATED_30" );

    public final static PortName TCIM_0_CH_1 = new PortName( "TCIM_0_CH_1" );
    public final static PortName TCIM_0_CH_2 = new PortName( "TCIM_0_CH_2" );
    public final static PortName TCIM_1_CH_1 = new PortName( "TCIM_1_CH_1" );
    public final static PortName TCIM_1_CH_2 = new PortName( "TCIM_1_CH_2" );
    public final static PortName TCIM_2_CH_1 = new PortName( "TCIM_2_CH_1" );
    public final static PortName TCIM_2_CH_2 = new PortName( "TCIM_2_CH_2" );
    public final static PortName TCIM_3_CH_1 = new PortName( "TCIM_3_CH_1" );
    public final static PortName TCIM_3_CH_2 = new PortName( "TCIM_3_CH_2" );
    public final static PortName TCIM_4_CH_1 = new PortName( "TCIM_4_CH_1" );
    public final static PortName TCIM_4_CH_2 = new PortName( "TCIM_4_CH_2" );
    public final static PortName TCIM_5_CH_1 = new PortName( "TCIM_5_CH_1" );
    public final static PortName TCIM_5_CH_2 = new PortName( "TCIM_5_CH_2" );
    public final static PortName TCIM_6_CH_1 = new PortName( "TCIM_6_CH_1" );
    public final static PortName TCIM_6_CH_2 = new PortName( "TCIM_6_CH_2" );

	public final static PortName eth0 = new PortName( "eth0" );
	public final static PortName eth1 = new PortName( "eth1" );
    
	public final static PortName UNKNOWN = new PortName( "UNKNOWN" );

    private String name;

    private PortName( String name )
    {
        this.name = name;
        map.put( name, this );
    }

    public String toString()
    {
        return name;
    }

    public int compareTo( Object obj )
    {
        if ( obj == null || !(obj instanceof PortName ) )
            return -1;
        if ( this == obj )
            return 0;
        PortName pn2 = (PortName) obj;
        return ( name.compareTo(pn2.name) );
    }

}
