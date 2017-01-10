
package fsats.db;

/**
 * A database connection to the plan database with the given id.
 */
public class PlanDB
    extends DB
{
    public PlanDB(int planId)
    {
        super("fsats_plan_"+planId, DBinfo.getPassword());
    }
}
