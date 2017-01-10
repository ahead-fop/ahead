//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Created On      : Tue Jun 30 12:56:23 1998
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Wed Mar 10 13:38:57 1999

package fsats.util;


import fsats.util.Command;

/**
 * This class creates an fsats test plan.
 */
public class CreatePlan extends Command
{

    public CreatePlan( int planID, String planName )
    {        
        // when Create_plan is called by java without the -s option,
        // it fails with a i/o error from writing to /dev/tty
        String cmd = fsatsHome + separator + "tools" + separator
            + "create_plan";
        
        cmdArray = 
            new String[] { cmd, "-s", "" + planID, planName };
    }


    public CreatePlan( String master, String template, 
                       int planID, String planName )
    {        
        // when Create_plan is called by java without the -s option,
        // it fails with a i/o error from writing to /dev/tty
        String cmd = fsatsHome + separator + "tools" + separator
            + "create_plan";
        
        cmdArray = 
            new String[] { cmd, "-s", "-m", master, "-t", template, 
                           "" + planID, planName };
    }



    public String toString()
    {
        return "[CreatePlan: " + formatCmdArray() + "]";
    }

    
    
    public static void main( String[] args )
    {
        int status = -1;
        CreatePlan cmd;
        try {
            if ( args.length == 2 )
                {
                    int planID = Integer.parseInt( args[0] );
                    cmd = new CreatePlan( planID, args[1] );
                }
            else if ( args.length == 4 )
                {
                    int planID = Integer.parseInt( args[2] );
                    cmd = new CreatePlan( args[0], args[1], planID, args[3] );
                }
            else throw new Exception( "wrong cmd line args" );
            status = cmd.execute();
            System.out.println( "CreatePlan=" + status );
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }
        
}
