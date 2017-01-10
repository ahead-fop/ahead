//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett

package fsats.db;

import fsats.util.FsatsProperties;
import fsats.db.FsatsDefaultDBinfo;
import fsats.db.PlanDatabaseCreationException;
import fsats.util.Log;
import fsats.util.CreatePlan;
import java.sql.*;
import java.io.*;
import java.util.*;


/**
 * Utilities dealing with management of Test Plan Database accounts.
 */
public class PlanUtils
{
    

    public PlanUtils()
    {}


    /**
     * Returns the planID of the plan created.
     */
    public static int createPlan( String planName) 
        throws PlanDatabaseCreationException
    {
        return PlanUtils.createPlan( "fsats_plan_master/fsats_db_daemon", 
                                     "fsats_plan_template/fsats_db_daemon",
                                     planName );
    }
    

    /**
     * Returns the planID of the plan created.
     */
    public static int createPlan( String master, String template,
                                  String planName )
        throws PlanDatabaseCreationException
    {
        int id = -1;
        try {
            id = maxPlanID() + 1;
            CreatePlan cmd = new CreatePlan( master, template, id, planName );
            int status = cmd.execute();
            if ( status != 0 ) {
                throw new PlanDatabaseCreationException
                    ( "Cmd createPlan return code=" + status );
            }
        } catch( SQLException e ) {
            throw new PlanDatabaseCreationException( e );
        } catch( IOException e ) {
            throw new PlanDatabaseCreationException( e );
        } catch( InterruptedException e ) {
            throw new PlanDatabaseCreationException( e );
        }
        return id;
    }
    

    /**
     * Returns the largest plan ID.  Returns 0 if none exist.
     */
    public static int maxPlanID() throws SQLException
    {
        int id = 0;

        Database db = new Database( FsatsDefaultDBinfo.getMasterAccount() );
        
        String query = 
            "select NVL(max(to_number(substr(username,12,length(" +
            " username)-11))), 0) " +
            " from all_users where username like 'FSATS_PLAN_%' " +
            " and substr(username,12,1) >= '0' and " +
            " substr(username,12,1) <= '9'";

        Statement statement = db.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery( query );
        if (resultSet.next())
            id = resultSet.getInt( 1 );
        resultSet.close();
        statement.close();
        db.close();
        
        return id;
    }



    public static void cleanPlan( int planID )
    {
        throw new UnsupportedOperationException();
    }
    
 
    /**
     * Get the database user account for specified Plan ID.
     */
    public static String userAccountOf( int planID )
    {
        return "fsats_plan_" + planID;
    }



    /**
     * Return the plan ID of the given Test Plan.
     * Find the row in fsats_plan_master.plan_data_attributes with 
     * column pd_name equal to the specified planName.  Return the
     * value in column pd_id if found, otherwise return -1.
     */
    public static int idOf( String planName )
    {
        int id = -1;

        Database db = new Database( FsatsDefaultDBinfo.getMasterAccount() );
        
        String query = "select pd_id from plan_data_attributes where "
            + " pd_name = '" + planName + "'";
        try {
            Statement statement = db.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery( query );
            if (resultSet.next())
                {
                    String str = resultSet.getString( 1 );
                    if ( str != null && str.length() > 0 )
                        id = Integer.parseInt( str );
                }
            statement.close();
            db.close();
        } catch( SQLException e ) {
            Log.error( Log.UI, "error reading pd_id for " + planName 
                       + ": " + query, e );
        }
        return id;
    }



    /**
     * Return the plan Name of the given Test Plan.
     * Find the row in fsats_plan_master.plan_data_attributes with 
     * column pd_id equal to the specified planID.  Return the
     * value in column pd_name if found, otherwise return a zero
     * length string.
     */
    public static String nameOf( int planID )
    {
        String name = "";
        
        Database db = new Database( FsatsDefaultDBinfo.getMasterAccount() );

        String query = "select pd_name from plan_data_attributes where "
            + " pd_id = " + planID;
        try {
            Statement statement = db.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery( query );
            if (resultSet.next())
                {
                    String str = resultSet.getString( 1 );
                    if ( str != null )
                        name = str;
                }
            statement.close();
            db.close();
        } catch( SQLException e ) {
            Log.error( Log.UI, "error reading pd_name for " + planID
                       + ": " + query, e );
        }
        return name;
    }


    /**
     * Returns the names of all Test Plans registered in the master account.
     * Each item in the enumeration is a string and is the name of a plan.
     */
    public static Enumeration getPlanNames()
    {
        Vector v = new Vector();
        
        Database db = new Database( FsatsDefaultDBinfo.getMasterAccount() );

        String query = "select pd_name from plan_data_attributes";
        try {
            Statement statement = db.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery( query );
            while (resultSet.next())
                {
                    String str = resultSet.getString( 1 );
                    if ( str != null )
                        v.addElement( str );
                }
            statement.close();
            db.close();
        } catch( SQLException e ) {
            Log.error( Log.UI, "error reading pd_name: " + query, e );
        }
        return v.elements();
    }

    public static int toelIDOf( String toelName )
    {
        int id = -1;
        String plan = FsatsProperties.get( "fsats.sql.user", "" );
        Database db = new Database( plan );

        String query = "select toel_id from toel where toel_name = '" 
            + toelName + "'";

        try {
            Statement statement = db.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery( query );
            if (resultSet.next() ) {
                id = resultSet.getInt( 1 );
            }
            statement.close();
            db.close();
        } catch( Throwable t ) {
            Log.error( Log.UI, "error reading toel_id for " + toelName
                       + " : " + query, t );
        }
        return id;
    }


    public static void main( String[] args )
    {
        System.out.println( "id=" + idOf( args[0] ) );
        int i = 0;
        try {
            i = Integer.parseInt( args[1] );
        } catch ( Throwable t ) {
            t.printStackTrace();
            System.exit(0);
        }
        System.out.println( "name=" + nameOf( i ) );
        System.out.println();
        Enumeration e = getPlanNames();
        while ( e.hasMoreElements() )
            System.out.println( (String)e.nextElement() );
        System.out.println( "toel_id=" + toelIDOf( args[2] ) );
    }
    
}
