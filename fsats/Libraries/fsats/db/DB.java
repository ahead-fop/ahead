
package fsats.db;

import fsats.db.FsatsDefaultDBinfo;

import java.sql.ResultSetMetaData;

/**
 * A database connection to a given account.
 */
public class DB
{

    public DB( String user )
    {
        this ( user, FsatsDefaultDBinfo.getPassword() );
    }

    
    /**
     * Connect to user/password.
     */
    public DB(String user, String password)
    {
        try
        {
            if (connection==null)
            {
				String driverName = FsatsDefaultDBinfo.getDriverName();
                Class.forName( driverName );

                connection =
                    java.sql.DriverManager.getConnection( 
                        FsatsDefaultDBinfo.getDBurl(),
                        user,
                        password);
            }

            connection.setAutoCommit(false);

            statement = connection.createStatement();
        }
        catch (java.sql.SQLException x)
        {
            System.out.println(x);
            statement = null;
        }
        catch (java.lang.ClassNotFoundException x)
        {
            System.out.println(x);
            statement = null;
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
        java.sql.ResultSet result;
        java.util.Vector rowVector = new java.util.Vector();
        try
        {
            result = statement.executeQuery(query);
            ResultSetMetaData meta = result.getMetaData();
            int colCnt = meta.getColumnCount();
            java.util.Vector colVector;
            
            while (result.next())
            {
               colVector = new java.util.Vector();
               for (int i=1; i<=colCnt; i++)
               {
                  colVector.addElement(result.getString(i));
               }
               rowVector.addElement(colVector);
            }
        }
        catch (java.sql.SQLException x)
        {
            System.out.println(x.getMessage()+": "+query);
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
            return statement.executeUpdate(query);
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
    public void commit()
       throws java.sql.SQLException
    {
       connection.commit();
    }

    /**
     * Drop this connection and release resources.
     */
    public void close()
        throws java.sql.SQLException
    {
        connection.close();
        statement.close();
    }

    private java.sql.Statement statement;
    private java.sql.Connection connection = null;
}

