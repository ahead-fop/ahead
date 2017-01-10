//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Created On      : Tue Jun 30 12:56:23 1998
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Wed Mar 10 13:38:57 1999

package fsats.util;


import fsats.util.Command;

/**
 * This class deletes an fsats test plan.
 */
public class DeletePlan extends Command
{

    /**
     * @param planID - the id of the plan
     */
    public DeletePlan( int planID )
    {        
        // when clean_plan is called by java without the -s option,
        // it fails with a i/o error from writing to /dev/tty
        String cmd = fsatsHome + separator + "tools" + separator
            + "clean_plan";
        
        cmdArray = new String[] { cmd, "-s", "-d", "" + planID };
    }



    public String toString()
    {
        return "[Delete: " + formatCmdArray() + "]";
    }

    
    
    public static void main( String[] args )
    {
        int id = 0;
        try {
            id = Integer.parseInt( args[0] );
        } catch( Throwable t )
            {}

        DeletePlan cmd = new DeletePlan( id );
        int status = -1;
        try {
             status = cmd.execute();
        } catch( Exception e ) {
            e.printStackTrace();
        }
        System.out.println( "Delete=" + status );
    }
        
}
