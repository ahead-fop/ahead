//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Created On      : Tue Jun 30 12:56:23 1998
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Wed Mar 10 13:41:52 1999

package fsats.util;


import fsats.util.Command;

/**
 * This class set synonyms to an fsats test plan.
 */
public class SetPlan extends Command
{

    /**
     * Construct an object that will exec the fsats cmd set_plan,
     * passing planID as a parameter.
     *
     * @param planID - the ID of a fsats test plan.
     */
    public SetPlan( int planID )
    {        
        // when set_plan is called by java without the -s option,
        // it fails with a i/o error from writing to /dev/tty
        String cmd = fsatsHome + separator + "tools" + separator
            + "set_plan";
        cmdArray = new String[] { cmd, "-s", "" + planID };
    }
    
    public static void main( String[] args )
    {
        SetPlan cmd = new SetPlan( Integer.parseInt( args[0] ) );
        int status = -1;
        try {
             status = cmd.execute();
        } catch( Exception e ) {
            e.printStackTrace();
        }
        System.out.println( "setPlan=" + status );
    }
        
}
