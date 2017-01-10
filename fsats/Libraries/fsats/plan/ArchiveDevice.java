package fsats.plan;


import fsats.util.DataContainer;
import java.util.*;


public class ArchiveDevice implements PlanContainerLabels
{
    private String deviceType;
    private String path;
    private int id;

    public ArchiveDevice( String deviceType, String path, int id )
    {
        this.deviceType = deviceType;
        this.path = path;
        this.id = id;
    }

    public ArchiveDevice( DataContainer cont )
    {
        deviceType = cont.getField( DEVICE_TYPE ).getValue();
        path = cont.getField( PATH ).getValue();
        String idField = cont.getField( ARCHIVE_DEVICE_ID ).getValue("-1");
        try {
            id = Integer.parseInt( idField );
        } catch( NumberFormatException e ) {
            e.printStackTrace();
        }
    }

    public DataContainer toContainer()
    {
        DataContainer cont = new DataContainer( ARCHIVE_DEVICE );
        cont.addField( new DataContainer( DEVICE_TYPE, deviceType ) );
        cont.addField( new DataContainer( PATH, path ) );
        cont.addField( new DataContainer( ARCHIVE_DEVICE_ID, "" + id ) );
        return cont;
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public String getPath()
    {
        return path;
    }

    int getID()
    {
        return id;
    }

    public boolean equals(Object obj)
    {
        if ( this==obj )
            return true;
        if ( obj==null || !(obj instanceof ArchiveDevice) )
            return false;
        ArchiveDevice ad2 = (ArchiveDevice) obj;
        return ( (deviceType==null? 
                  ad2.deviceType==null: deviceType.equals(ad2.deviceType) )
                 && (path==null? 
                  ad2.path==null: path.equals(ad2.path) )
                 && id == id );        
    }

}
