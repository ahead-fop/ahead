//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Created On      : Tue Jun 30 12:56:23 1998
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Wed Mar 10 13:38:57 1999

package fsats.util;


import fsats.util.Command;

/**
 * This class imports an fsats test plan.
 */
public class ImportPlan extends Command
{

    /**
     * Construct an object that will exec the fsats cmd import_plan,
     * passing fileName as a parameter.
     *
     * @param fileName - the full path of an oracle export file (a .dmp file)
     */
    public ImportPlan( String fileName )
    {        
        // when import_plan is called by java without the -s option,
        // it fails with a i/o error from writing to /dev/tty
        String cmd = fsatsHome + separator + "tools" + separator
            + "import_plan";
        
        cmdArray = new String[] { cmd, "-s", fileName };
    }


   
    /**
     * Construct an object that will exec the fsats cmd import_plan,
     * passing fileName and PlanName as parameters.
     *
     * @param fileName - the full path of an oracle export file (a .dmp file)
     * @param planName - the name given to the imported plan.  it must be
     * unique
     */
    public ImportPlan( String fileName, String planName )
    {        
        // when import_plan is called by java without the -s option,
        // it fails with a i/o error from writing to /dev/tty
        String cmd = fsatsHome + separator + "tools" + separator
            + "import_plan";
        
        cmdArray = new String[] { cmd, "-s", "-n", planName, fileName };
    }



    public String toString()
    {
        return "[ImportPlan: " + formatCmdArray() + "]";
    }

    
    
    public static void main( String[] args )
    {
        ImportPlan cmd = new ImportPlan( args[0] );
        int status = -1;
        try {
             status = cmd.execute();
        } catch( Exception e ) {
            e.printStackTrace();
        }
        System.out.println( "importPlan=" + status );
    }
        
}
