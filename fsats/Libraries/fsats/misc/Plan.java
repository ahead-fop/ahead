//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Created On      : Tue Jun 30 12:56:23 1998
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Wed Oct 20 16:15:10 1999

package fsats.misc;

import fsats.util.FsatsProperties;
import fsats.util.Log;
import fsats.util.Command;
import fsats.db.MasterDB;
import fsats.db.DBinfo;
import fsats.db.FsatsDefaultDBinfo;

import java.sql.*;
import java.io.*;


/**
 * This class stores id information about an Fsats test plan.
 * A test plan is database account and has an associated name and numeric id.
 * The name and id are stored in columns pd_name, pd_id in table
 * plan_data_attributes in account fsats_plan_master.  By convention,
 * the database account name is fsats_plan_<id>, where <id> is the id
 * number.
 */
public class Plan
{
    
    private String name = "";  // corresponds to plan_data_attributes.pd_name
    
    private int id = -1;  // corresponds to plan_data_attributes.pd_id


    

    public Plan()
    {}

   

 
    public Plan( String planName )
    {
        setName( planName );
    }


    
    public Plan( int planID )
    {
        setID( planID );
    }



    
    /**
     * Set the name of the test plan.  Corresponds to column pd_name in
     * fsats_plan_master.plan_data_attributes.
     */
    public synchronized void setName( String name ) 
    {
        this.name = name;
        id = idOf( name );
    }




    /**
     * Set the id of the test plan.  Corresponds to column pd_id in
     * fsats_plan_master.plan_data_attributes.
     */
    public synchronized void setID( int id ) 
    {
        this.id = id;
        name = nameOf( id );
    }




    public synchronized void setNull()
    {
        id = -1;
        name = "";
    }
    



    public synchronized boolean isNull()
    {
        if ( id == -1 || name == null || name.length() == 0  ) 
            return true;
        else
            return false;
    }
   


 
    /**
     * Returns the name of the test plan.  Return "" if isNull().
     */
    public synchronized String getName() 
    {
        return name;
    }




    /**
     * Returns the plan id of the test plan.  Return -1 if isNull().
     */
    public synchronized int getID()
    {
        return id;
    }




    /**
     * Get the name of database user account for the current plan.
     */
    public synchronized String getUserAccount()
    {
        return "fsats_plan_" + getID();
    }



    /**
     * Find the row in fsats_plan_master.plan_data_attributes with 
     * column pd_name equal to the specified planName.  Return the
     * value in column pd_id if found, otherwise return -1.
     */
    private int idOf( String planName )
    {
        int id = -1;
        
        // dbInfo must be initialized before creating MasterDB. see DBinfo
        DBinfo dbInfo = new DBinfo( FsatsDefaultDBinfo.getPassword(),
                                    FsatsProperties.get(FsatsProperties.
                                                        TWO_TASK),
                                    FsatsProperties.get(FsatsProperties.
                                                        ORACLE_HOST_URL),
                                    FsatsDefaultDBinfo.getMasterAccount() );
        MasterDB db = new MasterDB();
        String query = "select pd_id from plan_data_attributes where "
            + " pd_name = '" + planName + "'";
        try {
            ResultSet resultSet = db.doQuery( query );
            if (resultSet.next())
                {
                    String str = resultSet.getString( 1 );
                    if ( str != null && str.length() > 0 )
                        id = Integer.parseInt( str );
                }
	    resultSet.close();
            db.close();
        } catch( SQLException e ) {
            Log.error( Log.UI, "error reading pd_id for " + planName 
                       + ": " + query, e );
        }
        return id;
    }



    /**
     * Find the row in fsats_plan_master.plan_data_attributes with 
     * column pd_id equal to the specified planID.  Return the
     * value in column pd_name if found, otherwise return a zero
     * length string.
     */
    private String nameOf( int planID )
    {
        String name = "";
        
        // dbInfo must be initialized before creating MasterDB. see DBinfo
        DBinfo dbInfo = new DBinfo( FsatsDefaultDBinfo.getPassword(),
                                    FsatsProperties.get(FsatsProperties.
                                                        TWO_TASK),
                                    FsatsProperties.get(FsatsProperties.
                                                        ORACLE_HOST_URL),
                                    FsatsDefaultDBinfo.getMasterAccount() );
        MasterDB db = new MasterDB();
        String query = "select pd_name from plan_data_attributes where "
            + " pd_id = " + planID;
        try {
            ResultSet resultSet = db.doQuery( query );
            if (resultSet.next())
                {
                    String str = resultSet.getString( 1 );
                    if ( str != null )
                        name = str;
                }
	    resultSet.close();
            db.close();
        } catch( SQLException e ) {
            Log.error( Log.UI, "error reading pd_name for " + planID
                       + ": " + query, e );
        }
        return name;
    }




    public String toString()
    {
        String s = "Plan[ name=" + name + ", id=" + id + " ]";
        return s;
    }

    


    public static void main( String[] args )
    {
        Plan plan = new Plan();
        plan.setName( args[0] );
        System.out.println( "id=" + plan.getID() );
        plan.setID( Integer.parseInt( args[1] ) );
        System.out.println( "name=" + plan.getName() );
    }
    
}
