//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Mon Aug 30 13:41:23 1999

package fsats.util;


import fsats.util.Command;

/**
 * This class creates the Test Plan Data Container File by running
 * the Fsats program make_plan_dump_file.
 */
public class DumpPlan extends Command
{

    public DumpPlan( int planID )
    {
        String cmd = fsatsHome + separator + "tools" + separator
            + "dump_plan";
        
        String fileName = fsatsHome + separator + "plans" + separator
            + "plan_" + planID + ".dc";

        cmdArray = new String[] { cmd, fileName };
    }


}
