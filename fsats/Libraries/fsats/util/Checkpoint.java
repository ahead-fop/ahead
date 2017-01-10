//                              -*- Mode: Java -*-
// Checkpoint.java ---
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Created On      : Tue Jun 30 12:56:23 1998
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Wed Mar 10 13:47:30 1999

package fsats.util;

import java.io.IOException;
import fsats.misc.Plan;


/**
 * This class provides the "Checkpoint" operations for a test plan.
 * It does so by executing the fsats cmd <checkpoint> in /usr/fsats/tools.
 * The checkpoint cmd operates on the ops$user account and so synonyms
 * must be set to the plan.  So all methods take a plan ID as a paramter
 * and have the side effect of setting synonyms to the specified plan.
 */
public class Checkpoint extends Command
{

    public Checkpoint()
    {
        String cmd = fsatsHome + separator + "tools" + separator
            + "checkpoint";
        
        // when checkpoint is called by java without the -s option,
        // it fails with a i/o error from writing to /dev/tty.
        // checkpoint doc says -s implies -f but it doesn't seem to work
        // that way
        cmdArray = new String[] { cmd, "-s", "-f", "dummy", "dummy" };
    }
    
    /**
     * Copy the specified checkpoint to checkpoint 0.
     * This method has the side effect of setting database
     * synonyms to the specified plan.
     *
     * @return zero on success, -1 on failure
     */
    public int restore( String checkpointName, int planID )
         throws InterruptedException, IOException
    {
        setPlan( planID );
        cmdArray[3] = "-r";
        cmdArray[4] = checkpointName;
        int status = execute();
        return status;
    }

    /**
     * In the specified plan, copy Checkpoint 0 to a new
     * Checkpoint with the specified name.  Add a row to table
     * CHECKPOINT containing the name of the new Checkpoint.
     * This method has the side effect of setting database
     * synonyms to the specified plan.
     *
     * @return zero on success, -1 on failure
     */
    public int create( String checkpointName, int planID )
         throws InterruptedException, IOException
    {
        setPlan( planID );
        cmdArray[3] = "-c";
        cmdArray[4] = checkpointName;
        int status = execute();
        return status;
    }


    /**
     * Delete the specified checkpoint.
     * This method has the side effect of setting database
     * synonyms to the specified plan.
     *
     * @return zero on success, -1 on failure
     */
    public int delete( String checkpointName, int planID)
         throws InterruptedException, IOException
    {
        setPlan( planID );
        cmdArray[3] = "-d";
        cmdArray[4] = checkpointName;
        int status = execute();
        return status;
    }


    private int setPlan( int planID )
         throws InterruptedException, IOException
    {
        SetPlan setPlan = new SetPlan( planID );
        int status = setPlan.execute();
        return status;
    }

    public static void main( String[] args )
    {
        Checkpoint cp = new Checkpoint();
        int status = -1;
        try {
            switch( args[0].charAt( 0 ) ) {
            case 'c':
                status = cp.create( args[1], Integer.parseInt( args[2] ) );
                break;
            case 'd':
                status = cp.delete( args[1], Integer.parseInt( args[2] ) );
                break;
            case 'r':
                status = cp.restore( args[1], Integer.parseInt( args[2] ) );
                break;
            }
        } catch( Exception e ) {
            e.printStackTrace();
        }
        System.out.println( "create=" + status );
    }

}    
