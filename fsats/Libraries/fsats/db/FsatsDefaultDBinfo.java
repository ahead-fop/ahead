//                              -*- Mode: Java -*- 
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett


package fsats.db;

import fsats.util.FsatsProperties;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.DriverManager;

/**
 * This class stores information needed to connect to the fsats database.
 *
 * require: Properties TWO_TASK and ORACLE_HOST_URL are defined.
 * 
 */
public class FsatsDefaultDBinfo
{
    static private String password = "fsats_db_daemon";
    static private String dbURL = null;
    static private String masterAccount = null;
    static private String planAccount = null;
    static private String driverName = "oracle.jdbc.driver.OracleDriver";
    static private String osUserName = System.getProperty( "user.name" );
	
    /**
     * This Class is not intended to be instantiated.
     */
    private FsatsDefaultDBinfo()
    {}  
    
    /**
     * The Database password.  Fsats uses the same password for both
     * the Master and Plan accounts.
     */
    public static synchronized String getPassword()
    {
        return password;
    }
    
    /**
     * The Database URL
     */
    public static synchronized String getDBurl()
    {
        if ( dbURL == null )
            {
                String hostURL =
                    FsatsProperties.get( FsatsProperties.ORACLE_HOST_URL );

                String twoTask =
                    FsatsProperties.get( FsatsProperties.TWO_TASK );
                
                // jdbc doesn't work with the two_task env var as
                // used by fsats.  the trailing ".WORLD" has to be stripped
                String jdbcTwoTask = twoTask;
                int i = twoTask.indexOf( '.' );
                if ( i > -1 )
                    jdbcTwoTask = twoTask.substring( 0, i );

                dbURL = "jdbc:oracle:thin:@" + hostURL + ":1521:" +jdbcTwoTask;
            }
        return dbURL;
    }
    
    /**
     * The name of the master database account to which synonyms are currently
     * set in the ops$user account.
     */
    public static synchronized String getMasterAccount()
    {
        if ( masterAccount == null )
            masterAccount = getAccount( "fsats_master" );
        if ( masterAccount == null )
            masterAccount = "fsats_plan_master";
        return masterAccount;
    }
    
    /**
     * The name of the plan database account to which synonyms are currently
     * set in the ops$user account.
     */
    public static synchronized String getPlanAccount()
    {
        if ( planAccount == null )
            planAccount = getAccount( "fsats_plan" );
        return planAccount;
    }
    
	/**
	 * The name of the JDBC Driver
	 */
    public static synchronized String getDriverName()
    {
        return driverName;
    }

    /*
     * returns the account name to which synonyms are set
     * in the ops$user account and which has a
     * prefix equal to param accountPrefix.  normally accountPrefix is
     * equal to either "fsats_master" or "fsats_plan".
     */
	private static String getAccount( String accountPrefix )
    {
        String account = null;
        
        // do the <count():group by:order by desc> to make the query more
        // robust.  occasionally in the development environment (as opposed
        // to the target environment) a few stray synonyms can get created
        String query =
            "SELECT table_owner, count(table_name) "
            + " FROM dba_synonyms "
            + " WHERE UPPER(owner) = 'OPS$" + osUserName.toUpperCase() + "'"
            + "   AND UPPER(table_owner) like '"
            + accountPrefix.toUpperCase() + "%'"
            + " GROUP BY table_owner "
            + " ORDER BY count(table_name) DESC";

        try {
            Class.forName( driverName );
            
            // to execute the query in this method, any user account will do.
            // fsats_plan_master is always present in the installed fsats
            // environment and it also always exists in the developer environ.
            Connection connection =
                DriverManager.getConnection( getDBurl(),
                                             "fsats_plan_master",
                                             password);
            
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if ( resultSet.next() )
                {
                    account = resultSet.getString( 1 );
                }
            resultSet.close();
            connection.close();
        }
        catch (ClassNotFoundException e) {
            System.err.println(e);
        }       
        catch (SQLException e) {
            System.err.println(e + " : " + query);
        }
        return account;
    }



    
    public static void main( String[] args )
    {
        System.out.println("pwd="+FsatsDefaultDBinfo.getPassword()
                           +"  dburl="+FsatsDefaultDBinfo.getDBurl()
                           +"  master="+FsatsDefaultDBinfo.getMasterAccount()
                           +"  plan="+FsatsDefaultDBinfo.getPlanAccount() );
    }

    
}
