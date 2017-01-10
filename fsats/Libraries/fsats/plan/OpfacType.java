package fsats.plan;

import fsats.db.Database;
import fsats.db.*;
import java.sql.*;
import java.util.*;

/**
 * Enumeration of the types of opfacs.
 */
public class OpfacType
{
    private static TreeMap map = new TreeMap();
    private static Set firingUnits = new HashSet();
    private static Set observers = new HashSet();
    private static Set cmdPosts = new HashSet();


    public final static OpfacType ARM_FIST = new OpfacType( "ARM_FIST" );
    public final static OpfacType ARM_FIST_TFVAR = new OpfacType( "ARM_FIST_TFVAR" );
    public final static OpfacType ATHS = new OpfacType( "ATHS" );
    public final static OpfacType ATHS_TFVAR = new OpfacType( "ATHS_TFVAR" );
    public final static OpfacType BDE_FSE = new OpfacType( "BDE_FSE" );
    public final static OpfacType BN_FSE = new OpfacType( "BN_FSE" );
    public final static OpfacType COLT = new OpfacType( "COLT" );
    public final static OpfacType COLT_TFVAR = new OpfacType( "COLT_TFVAR" );
    public final static OpfacType CORPS_ARTY_CF = new OpfacType( "CORPS_ARTY_CF" );
    public final static OpfacType CORPS_ARTY_CP = new OpfacType( "CORPS_ARTY_CP" );
    public final static OpfacType CORPS_FSE_MAIN = new OpfacType( "CORPS_FSE_MAIN" );
    public final static OpfacType CORPS_FSE_REAR = new OpfacType( "CORPS_FSE_REAR" );
    public final static OpfacType CORPS_FSE_TAC = new OpfacType( "CORPS_FSE_TAC" );
    public final static OpfacType DIVARTY_CF = new OpfacType( "DIVARTY_CF" );
    public final static OpfacType DIVARTY_CP = new OpfacType( "DIVARTY_CP" );
    public final static OpfacType DIV_FSE_MAIN = new OpfacType( "DIV_FSE_MAIN" );
    public final static OpfacType DIV_FSE_REAR = new OpfacType( "DIV_FSE_REAR" );
    public final static OpfacType DIV_FSE_TAC = new OpfacType( "DIV_FSE_TAC" );
    public final static OpfacType DS_BN_CP = new OpfacType( "DS_BN_CP" );
    public final static OpfacType DS_FA_PLT = new OpfacType( "DS_FA_PLT" );
    public final static OpfacType FA_BDE_CP = new OpfacType( "FA_BDE_CP" );
    public final static OpfacType FA_BTRY = new OpfacType( "FA_BTRY" );
    public final static OpfacType FA_PLT = new OpfacType( "FA_PLT" );
    public final static OpfacType FA_SEC = new OpfacType( "FA_SEC" );
    public final static OpfacType FIST = new OpfacType( "FIST" );
    public final static OpfacType FIST_TFVAR = new OpfacType( "FIST_TFVAR" );
    public final static OpfacType FO = new OpfacType( "FO" );
    public final static OpfacType FO_TFVAR = new OpfacType( "FO_TFVAR" );
    public final static OpfacType GSR_BN_CP = new OpfacType( "GSR_BN_CP" );
    public final static OpfacType GSR_FA_PLT = new OpfacType( "GSR_FA_PLT" );
    public final static OpfacType GS_BN_CP = new OpfacType( "GS_BN_CP" );
    public final static OpfacType GS_FA_PLT = new OpfacType( "GS_FA_PLT" );
    public final static OpfacType JSTARS = new OpfacType( "JSTARS" );
    public final static OpfacType LANCE_BN = new OpfacType( "LANCE_BN" );
    public final static OpfacType LANCE_BTRY = new OpfacType( "LANCE_BTRY" );
    public final static OpfacType MECH_FIST = new OpfacType( "MECH_FIST" );
    public final static OpfacType MECH_FIST_TFVAR = new OpfacType( "MECH_FIST_TFVAR" );
    public final static OpfacType MET_SEC = new OpfacType( "MET_SEC" );
    public final static OpfacType MLRS_BN = new OpfacType( "MLRS_BN" );
    public final static OpfacType MLRS_BTRY = new OpfacType( "MLRS_BTRY" );
    public final static OpfacType MLRS_PLT = new OpfacType( "MLRS_PLT" );
    public final static OpfacType MLRS_SEC = new OpfacType( "MLRS_SEC" );
    public final static OpfacType MTR_PLT = new OpfacType( "MTR_PLT" );
    public final static OpfacType Q_36 = new OpfacType( "Q_36" );
    public final static OpfacType Q_36_TFVAR = new OpfacType( "Q_36_TFVAR" );
    public final static OpfacType Q_37 = new OpfacType( "Q_37" );
    public final static OpfacType Q_37_TFVAR = new OpfacType( "Q_37_TFVAR" );
    public final static OpfacType R_BN_CP = new OpfacType( "R_BN_CP" );
    public final static OpfacType R_FA_PLT = new OpfacType( "R_FA_PLT" );
    public final static OpfacType STRIKER = new OpfacType( "STRIKER" );
    public final static OpfacType STRIKER_TFVAR = new OpfacType( "STRIKER_TFVAR" );
    public final static OpfacType UNKNOWN = new OpfacType( "UNKNOWN" );



  static {
        firingUnits.add( DS_FA_PLT);
        firingUnits.add( R_FA_PLT);
        firingUnits.add( GS_FA_PLT);
        firingUnits.add( GSR_FA_PLT);
        firingUnits.add( FA_BTRY);
        firingUnits.add( FA_SEC);
        firingUnits.add( MLRS_BTRY);
        firingUnits.add( MLRS_PLT);
        firingUnits.add( MLRS_SEC);
        firingUnits.add( FA_PLT);
        firingUnits.add( MTR_PLT);
       
        observers.add( FO );
        observers.add( FIST );
        observers.add( MECH_FIST );
        observers.add( ARM_FIST );
        observers.add( ATHS );
        observers.add( COLT ); 
        observers.add( Q_36 );
        observers.add( Q_36_TFVAR );
        observers.add( Q_37 );
        observers.add( Q_37_TFVAR );
        observers.add( JSTARS );
        observers.add( ARM_FIST_TFVAR );
        observers.add( ATHS_TFVAR );
        observers.add( COLT_TFVAR );
        observers.add( FIST_TFVAR );			 
        observers.add( FO_TFVAR );
        observers.add( MECH_FIST_TFVAR );
        observers.add( STRIKER );
        observers.add( STRIKER_TFVAR);

        cmdPosts.add( DIVARTY_CP );
        cmdPosts.add( DIVARTY_CF );
        cmdPosts.add( DS_BN_CP );
        cmdPosts.add( R_BN_CP );
        cmdPosts.add( GS_BN_CP );
        cmdPosts.add( GSR_BN_CP );
        cmdPosts.add( MLRS_BN );
        cmdPosts.add( FA_BDE_CP );
    }


    /**
     * Returns a OpfacType such that: opfacType.toString().equals( s ).
     * @throws OpfacTypeStringException if string doesn't map to an OpfacType.
     */
    public static OpfacType fromString( String s )
           throws OpfacTypeStringException
    {        
        OpfacType type = ( s == null? null: (OpfacType) map.get( s ) );
        if ( type == null )
           throw new OpfacTypeStringException( s );
        return type;
    }

    /**
     * Returns a iteration of the string equivalents of the enumeration.
     * The class returned is String.  Order is guaranteed to be ascending.
     */
    public static Iterator iterator()
    {
        return map.keySet().iterator();
    }






    private String name;

    // column opt_type_id of database table opfac_types
    private int id = Integer.MIN_VALUE;  



    private OpfacType( String name )
    {
        this.name = name;
        OpfacType.map.put( name, this );
    }

    public String toString()
    {
        return name;
    }

    public boolean isObserver()
    {
        return OpfacType.observers.contains( this );
    }

    public boolean isFiringUnit()
    {
        return OpfacType.firingUnits.contains( this );
    }

    public boolean isCommandPost()
    {
        return OpfacType.cmdPosts.contains( this );
    }

    public int getDatabaseID() throws SQLException
    {
        if ( id == Integer.MIN_VALUE )
            initID();
        return id;
    }

    private void initID() throws SQLException
    {
        Database db = new Database( FsatsDefaultDBinfo.getMasterAccount() );

        String query = "select opt_type_id from opfac_types where "
            + " opt_type_name = '" + name + "'";

        Statement statement = db.getConnection().createStatement();

        ResultSet resultSet = statement.executeQuery( query );

        if (resultSet.next())
            id = resultSet.getInt( 1 );

        statement.close();
        db.close();
    }


}
