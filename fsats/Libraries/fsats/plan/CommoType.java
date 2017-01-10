//
// CommoType
//
// $Revision: 1.2 $
//
// $Date: 2002-03-15 22:34:16 $
//
package fsats.plan;


import java.util.*;
import java.util.StringTokenizer;
import java.io.*;

import fsats.util.Log;
import fsats.util.FsatsProperties;
import fsats.util.DataContainer;

/**
 * Enumeration of the CommoType.
 */
public class CommoType implements PlanContainerLabels
{
    private int commoTypeID;
	private String deviceDescription;
    private String netMedium;  // column ntp_device decoded to readable values
	private String protocol;
	private String modulation;
	private String bitRate;
	private String errorControl;
	private String portConfiguration;
	
	private static Vector typesList = new Vector();
	private static int initalIndex = 0;
	
	private CommoType (int commoTypeID, String deviceDescription, 
                       String netMedium, String protocol, 
                       String modulation, String bitRate, 
                       String errorControl, String portConfiguration )
	{
		this.commoTypeID = commoTypeID;
	 	this.deviceDescription = deviceDescription;
        this.netMedium = netMedium;
	 	this.protocol = protocol;
	 	this.modulation = modulation;
	 	this.bitRate = bitRate;
	 	this.errorControl = errorControl;
	 	this.portConfiguration = portConfiguration;
	}
	
	private static String nextToken(String s)
	{
		int lastIndex = s.indexOf(";", initalIndex);
		
		if(lastIndex < 0)
		{
			lastIndex = s.length();
		}
		
		String sub = s.substring(initalIndex, lastIndex);
		initalIndex = lastIndex + 1;
		
		return sub;
	}
	
	private static int nextIntToken(String s)
	{
		String num = nextToken(s);
		
		try
		{
			return Integer.parseInt(num);
		}
		catch(NumberFormatException nfe)
		{
		}
		
		return 0;
	}
	
	static
	{
		int commoTypeID;
		String deviceDescription = null;
		int device = 0;
		String protocol = null;
		String modulation = null;
		String bitRate = null;
		String errorControl = null;
		String portConfiguration = null;
		
		String fsats_home_string = "";

		String fsats_home = FsatsProperties.get (
		    FsatsProperties.FSATS_HOME
		);

		fsats_home_string = fsats_home + File.separator + "sqlfiles" + File.separator
							+ "master" + File.separator + "net_types.dat";
		
		try
		{
			FileReader fr = new FileReader(fsats_home_string);
			BufferedReader br = new BufferedReader(fr);
			String s;
			
			try
			{
				// Skip column headings.
				br.readLine();
				int line = 1;
				
				// now add the items to the vector from the net_types.dat file.
				while((s  = br.readLine()) != null)
				{
					initalIndex = 0;
					
					commoTypeID = nextIntToken(s);
					nextIntToken(s);
					deviceDescription = nextToken(s);
					device = nextIntToken(s);
					nextIntToken(s);
					nextToken(s);
					nextIntToken(s);
					nextIntToken(s);
					nextIntToken(s);
					nextIntToken(s);
					nextIntToken(s);
					nextIntToken(s);
					nextToken(s);
					nextIntToken(s);
					nextIntToken(s);
					nextIntToken(s);
					protocol = nextToken(s);
					modulation = nextToken(s);
					bitRate = nextToken(s);
					errorControl = nextToken(s);
					portConfiguration = nextToken(s);
					
					typesList.add
                        (new CommoType(commoTypeID, deviceDescription, 
                                       decodeDevice(device), protocol,
                                       modulation, bitRate, errorControl, 
                                       portConfiguration ));
				}
				
				fr.close();
			}
			catch(IOException ioe)
			{
                Log.error( Log.PLAN, ioe );
			}
            finally {
                fr.close();
            }                             
		}
		catch(FileNotFoundException e)
		{
            Log.error( Log.PLAN, "File [net_types.dat] not found.", e );
		}
		catch(IOException e)
		{
            Log.error( Log.PLAN, e );
		}
	}

	/**
     * Duplicates the decode of column ntp_device as done by net.sqlv.
     * This was added to satisfy tmt.  TB 05/02/01
     */
    private static String decodeDevice( int device )
    {
        switch( device ) {
            case 0:
                return "UNDEFINED";
            case 1:
                return "RADIO";
            case 7:
                return "WIRELINE";
            case 9:
                return "WIRELINE";
            case 11:
                return "WIRELINE";
            case 15:
                return "EPUU";
            case 16:
                return "ETHERNET";
            case 17:
                return "ETHERNET";
            case 1000:
                return "UNDEFINED";
        default:
            return "" + device;
        }
    }

	public static Iterator iterator()
    {
        return typesList.iterator();
    }

	/**
     * Returns the CommoType which has the specified commoTypeID, 
     * null when not found.
     */
    public static CommoType get( int commoTypeID )
    {
        Iterator iter = iterator();
        while( iter.hasNext() ) {
            CommoType commo = (CommoType) iter.next();
            if ( commo.getCommoTypeID() == commoTypeID )
                return commo;
        }
        return null;
    }

    public DataContainer toContainer()
    {
        DataContainer cont = new DataContainer( COMMO_TYPE );
        cont.addField( new DataContainer( COMMO_TYPE_ID, "" + commoTypeID));
        cont.addField( new DataContainer( DEVICE_DESCRIPTION, deviceDescription));
        cont.addField( new DataContainer( NET_MEDIUM, netMedium ) );
        cont.addField( new DataContainer( PROTOCOL, protocol));
        cont.addField( new DataContainer( MODULATION, modulation));
        cont.addField( new DataContainer( BIT_RATE, bitRate));
        cont.addField( new DataContainer( ERROR_CONTROL, errorControl));
        cont.addField( new DataContainer( PORT_CONFIGURATION, portConfiguration));
        return cont;
    }

	public String toString()
	{
		String newString = protocol + ";" + deviceDescription + ";" 
						   + modulation + ";" + bitRate + ";" + errorControl;
		
		return newString;
	}

	/**
     * Returns a id guaranteed to be unique among all instances of CommoType.
     */
	public int getCommoTypeID()
	{
		return commoTypeID;
	}
	
	public String getDeviceDescription()
	{
		return deviceDescription;
	}

    public String getNetMedium()
    {
        return netMedium;
    }
	
	public String getProtocol()
	{
		return protocol;
	}
	
	public String getModulation()
	{
		return modulation;
	}
	
	public String getBitRate()
	{
		return bitRate;
	}
	
	public String getErrorControl()
	{
		return errorControl;
	}
	
	public String getPortConfiguration()
	{
		return portConfiguration;
	}

    /**
     * Test jig. assumes this program run with fsats_home set and 
     * with net_types.dat in the right subdir under $fsats_home.
     */
	public static void main( String args[] )
    {
        Iterator iter = CommoType.iterator();
        CommoType c = (CommoType) iter.next();
        System.out.println( c.toString() );
        System.out.println( c.getPortConfiguration() );
        System.out.println();
        CommoType c1 = CommoType.get( 1 );
        System.out.println( c1.toString() );
        System.out.println( c1.getPortConfiguration() );
        System.out.println();
        CommoType c90 = CommoType.get( 90 );
        System.out.println( c90.toString() );
        System.out.println( c90.getPortConfiguration() );
        System.out.println();
        DataContainer cont = c90.toContainer();
        cont.print();
    }

}





