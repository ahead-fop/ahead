//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Created On      : Tue Jun 30 12:56:23 1998
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Wed Mar 10 13:40:22 1999

package fsats.util;


import fsats.util.Command;

/**
 * This class exports an fsats test plan.
 */
public class ExportPlan extends Command
{

    /**
     * Construct an object that will exec the fsats cmd export_plan,
     * passing planID as a parameter.
     *
     * @param planID - the ID of an existing test plan.  
     */
    public ExportPlan( int planID )
    {        
        // when export_plan is called by java without the -s option,
        // it fails with a i/o error from writing to /dev/tty
        String cmd = fsatsHome + separator + "tools" + separator
            + "export_plan";
        
        cmdArray = new String[] { cmd, "-s", "-f", "" + planID };
    }
    
    public static void main( String[] args )
    {
        ExportPlan cmd = new ExportPlan( Integer.parseInt( args[0] ) );
        int status = -1;
        try {
             status = cmd.execute();
        } catch( Exception e ) {
            e.printStackTrace();
        }
        System.out.println( "ExportPlan=" + status );
    }
        
}
