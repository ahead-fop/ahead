//                 :            -*- Mode: Java -*-
// Version         : 1.3
// Author          :
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Feb 15 15:06:00 2000

package fsats.db;


/**
 * A database connection to a given account.
 */
public class Database
{
    private java.sql.Connection connection = null;

    public Database()
    {
	this ( FsatsDefaultDBinfo.getPlanAccount(),
	       FsatsDefaultDBinfo.getPassword());
    }

    public Database( String user )
    {
        this ( user, FsatsDefaultDBinfo.getPassword() );
    }

    
    /**
     * Connect to user/password.
     */
    public Database(String user, String password)
    {
        try
        {
            Class.forName(FsatsDefaultDBinfo.getDriverName());

            connection =
                java.sql.DriverManager.getConnection( 
                    FsatsDefaultDBinfo.getDBurl(),
                    user,
                    password);

            connection.setAutoCommit(false);
        }
        catch (Exception x)
        {
            System.out.println(
                "Connection failed to "+user+"/"+password+", "
                    +x+" "+x.getMessage());
            connection = null;
        }
    }

    /**
     * Try a query over this connection.
     */
    public synchronized java.sql.ResultSet doQuery(String query)
        throws java.sql.SQLException
    {
        try
        {
            java.sql.Statement statement=connection.createStatement();
            return statement.executeQuery(query);
        }
        catch (java.sql.SQLException x)
        {
            System.out.println(x.getMessage()+": "+query);
            throw x;
        }
    }

    /**
     * Try a query over this connection - return vector of vectors.
     */
    public synchronized java.util.Vector doQueryIntoVector(String query)
        throws java.sql.SQLException
    {
        java.util.Vector rowVector = new java.util.Vector();
        try
        {
            java.sql.Statement statement=connection.createStatement();
            java.sql.ResultSet result=statement.executeQuery(query);

            java.sql.ResultSetMetaData meta = result.getMetaData();
            int colCnt = meta.getColumnCount();
            
            while (result.next())
            {
               java.util.Vector colVector = new java.util.Vector();
               for (int i=1; i<=colCnt; i++)
               {
                  colVector.addElement(result.getString(i));
               }
               rowVector.addElement(colVector);
            }

            statement.close();
        }
        catch (java.sql.SQLException x)
        {
            throw x;
        }
        return rowVector;
    }


    /**
     * Try to update over this connection.
     */
    public synchronized int doUpdate(String query)
        throws java.sql.SQLException
    {
        try
        {
            return connection.createStatement().executeUpdate(query);
        }
        catch (java.sql.SQLException x)
        {
            System.out.println(x.getMessage()+": "+query);
            throw x;
        }
    }


    /**
     * commit changes made on this connection
     */
    public synchronized void commit()
       throws java.sql.SQLException
    {
       connection.commit();
    }


    /**
     * Drop this connection and release resources.
     */
    public synchronized void close()
        throws java.sql.SQLException
    {
        connection.close();
    }

    /**
     * Returns a reference to the Connection.
     */
    public java.sql.Connection getConnection()
    {
        return connection;
    }
}

