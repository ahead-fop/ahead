//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Created On      : Tue Jun 30 12:56:23 1998
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Thu Jul 29 11:49:35 1999

package fsats.util;


import fsats.util.Command;

/**
 * This class starts test execution by invoking the FSATS.run command.
 */
public class FsatsRun extends Command
{

    public FsatsRun( String nodeName )
    {
        // the output of the processes started by fsats.run gets lost
        // unless everthing is redirected to FSATS_CONSOLE.
        // in /usr/fsats/bin see start_java_ui and
        // redirect_output_fsats_console.

        String fsatsConsoleCmd = fsatsHome + separator + "bin" + separator
            + "redirect_output_fsats_console";

        // param "3" specifies a test execution node.
        String fsatsRunCmd = separator + "bin" + separator + "nice -10 " + separator 
            + fsatsHome + separator + "bin" + separator + "FSATS.run" + " "  + nodeName + " " + "3";
        
        cmdArray = new String[] { fsatsConsoleCmd, fsatsRunCmd };
    }


    public static void main( String[] args )
    {
        FsatsRun cmd = new FsatsRun( args[0] );
        Thread t = new Thread( cmd );
        t.start();
    }
        
}
