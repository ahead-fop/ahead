
package fsats.db;

/**
 * A database connection to the plan master database.
 */
public class MasterDB
    extends DB
{
    public MasterDB()
    {
        super(DBinfo.getMaster(), DBinfo.getPassword());
    }
}
