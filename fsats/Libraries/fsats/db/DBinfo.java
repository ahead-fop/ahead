
package fsats.db;

/**
 * information specific to connecting a particular database.
 */

public class DBinfo
{
   static private String password = null;
   static private String dbURL = null;
   static private String master = null;
   static private String driverName = "oracle.jdbc.driver.OracleDriver";
	
   /**
    */
   public DBinfo (
      String password,
      String twoTask,
      String hostURL,
      String master)
   {
      this.password = password;
      this.master = master;
      dbURL = new String("jdbc:oracle:thin:@"+hostURL+":1521:"+twoTask);
   }
  
   /**
    * The Database password
    */
   static public String getPassword()
   {
      return password;
   }

   /**
    * The Database URL
    */
   static public String getdbURL()
   {
      return dbURL;
   }

   /**
    * The Database Master User
    */
   static public String getMaster()
   {
      return master;
   }

	/**
	 * The name of the JDBC Driver
	 */
   static public String getDriverName()
   {
	  return driverName;
   }
	
}
