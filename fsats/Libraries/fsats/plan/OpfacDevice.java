package fsats.plan;

import java.util.*;


/**
 * Enumeration of opfac-net devices.
 */
public class OpfacDevice
{

    private static HashMap map = new HashMap();


    /**
     * Returns a OpfacDevice such that: opfacDevice.toString().equals( s ).
     * @throws OpfacDeviceStringException if no such device exists
     */
    public static OpfacDevice fromString( String s )
           throws OpfacDeviceStringException
    {
        OpfacDevice dev = (s == null? null: (OpfacDevice) map.get( s ) );
        if ( dev == null )
           throw new OpfacDeviceStringException( s );
        return dev;
    }

    /**
     * Returns a iteration of the string equivalents of the enumeration.
     * The class returned is String.  Order is guaranteed to be ascending.
     */
    public static Iterator iterator()
    {
        return map.keySet().iterator();
    }

    public final static OpfacDevice UNKNOWN = new OpfacDevice( "UNKNOWN" );
    public final static OpfacDevice AFATDS = new OpfacDevice( "AFATDS" );
    public final static OpfacDevice TACFIRE_BCD =  
        new OpfacDevice( "TACFIRE_BCD" );
    public final static OpfacDevice TACFIRE_BN = 
        new OpfacDevice( "TACFIRE_BN" );
    public final static OpfacDevice LTACFIRE = new OpfacDevice( "LTACFIRE" );
    public final static OpfacDevice BCS = new OpfacDevice( "BCS" );
    public final static OpfacDevice FDS = new OpfacDevice( "FDS" );
    public final static OpfacDevice MDS = new OpfacDevice( "MDS" );
    public final static OpfacDevice ARMY_GSM = new OpfacDevice( "ARMY_GSM" );
    public final static OpfacDevice USMC_DCT = new OpfacDevice( "USMC_DCT" );
    public final static OpfacDevice FED_MSR = new OpfacDevice( "FED_MSR" );
    public final static OpfacDevice DMD = new OpfacDevice( "DMD" );
    public final static OpfacDevice FED_FOFIST = 
        new OpfacDevice( "FED_FOFIST" );
    public final static OpfacDevice FIST_DMD = new OpfacDevice( "FIST_DMD" );
    public final static OpfacDevice FED_FOCC = new OpfacDevice( "FED_FOCC" );
    public final static OpfacDevice FIREFINDER = 
        new OpfacDevice( "FIREFINDER" );
    public final static OpfacDevice ARMY_DCT = new OpfacDevice( "ARMY_DCT" );
    public final static OpfacDevice FED = new OpfacDevice( "FED" );
    public final static OpfacDevice ATHS = new OpfacDevice( "ATHS" );
    public final static OpfacDevice MBC = new OpfacDevice( "MBC" );
    public final static OpfacDevice FCS_V4 = new OpfacDevice( "FCS_V4" );
    public final static OpfacDevice FCS_V6 = new OpfacDevice( "FCS_V6" );
    public final static OpfacDevice AFCS = new OpfacDevice( "AFCS" );
    public final static OpfacDevice IFSAS = new OpfacDevice( "IFSAS" );
    public final static OpfacDevice FATDS_11_IFSAS = 
        new OpfacDevice( "FATDS_11_IFSAS" );
    public final static OpfacDevice FATDS_11_LTACFIRE = 
        new OpfacDevice( "FATDS_11_LTACFIRE" );
    public final static OpfacDevice FATDS_11_BCS = 
        new OpfacDevice( "FATDS_11_BCS" );
    public final static OpfacDevice FATDS_11_FDS = 
        new OpfacDevice( "FATDS_11_FDS" );
    public final static OpfacDevice FATDS_11_FOS = 
        new OpfacDevice( "FATDS_11_FOS" );
    public final static OpfacDevice FATDS_11_IMBC = 
        new OpfacDevice( "FATDS_11_IMBC" );
    public final static OpfacDevice FATDS_11_MMS = 
        new OpfacDevice( "FATDS_11_MMS" );
    public final static OpfacDevice MCS = new OpfacDevice("MCS");
    public final static OpfacDevice ASAS = new OpfacDevice("ASAS");
    public final static OpfacDevice FBCB2 = new OpfacDevice("FBCB2");
    public final static OpfacDevice CSSCS = new OpfacDevice("CSSCS");
    public final static OpfacDevice AMDWS = new OpfacDevice("AMDWS");
    public final static OpfacDevice FAAD_EO = new OpfacDevice("FAAD EO");
    public final static OpfacDevice FAAD_FO = new OpfacDevice("FAAD FO");


    private String name;

    private OpfacDevice( String name )
    {
        this.name = name;
        map.put( name, this );
    }

    public String toString()
    {
        return name;
    }

}
