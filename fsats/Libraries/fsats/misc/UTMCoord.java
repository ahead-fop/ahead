//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Mon Mar 22 16:09:02 1999
//

package fsats.misc;


import fsats.util.DataContainer;
import fsats.util.Log;



/**
 * This class implements a UTM Long coordinate.
 */
public class UTMCoord
{
    private static final String EASTING = "EASTING";
    private static final String NORTHING = "NORTHING";    
    private static final String GRIDZONE = "GRID_ZONE";
    private static final String ALTITUDE = "ALTITUDE";
    
    private int easting;
    private int northing;    
    private int gridZone;
    private int altitude;

    public UTMCoord( int easting, int northing, int altitude, int gridZone )
    {
        setEasting( easting );
        setNorthing( northing );
        setGridZone( gridZone );
        setAltitude( altitude );
    }


    public UTMCoord( DataContainer container )
    {
        String strEasting = container.getField( EASTING ).getValue( "0" );
        String strNorthing = container.getField( NORTHING ).getValue( "0" );
        String strGridZone = container.getField( GRIDZONE ).getValue( "0" );
        String strAltitude = container.getField( ALTITUDE ).getValue( "0" );
        try {
            easting = Integer.parseInt( strEasting );
            northing = Integer.parseInt( strNorthing );
            gridZone = Integer.parseInt( strGridZone );
            altitude = Integer.parseInt( strAltitude );
        } catch ( NumberFormatException e ) {
            Log.error( Log.UI, "new UTMCoord(" + container + ")", e );
        }
    }

    
    public void setEasting( int easting )
    {
        this.easting = easting;
    }
    
    public void setNorthing( int northing )
    {
        this.northing = northing;
    }
             
    public void setGridZone(int gridZone )
    {
       this.gridZone = gridZone; 
    }

    public void setAltitude(int altitude )
    {
       this.altitude = altitude; 
    }


    public DataContainer toDataContainer()
    {
        DataContainer c = new DataContainer( "UTMCOORD" );
        c.addField( new DataContainer( EASTING, String.valueOf( easting ) ) );
        c.addField( new DataContainer( NORTHING, String.valueOf( northing ) ));
        c.addField( new DataContainer( GRIDZONE, String.valueOf( gridZone ) ));
        c.addField( new DataContainer( ALTITUDE, String.valueOf( altitude ) ));
        return c;
    }

    
    public int getEasting()
    {
        return easting;
    }
    
    public int getNorthing()
    {
        return northing;
    }
             
    public int getGridZone()
    {
        return gridZone;
    }

    public int getAltitude()
    {
        return altitude;
    }

    
}
