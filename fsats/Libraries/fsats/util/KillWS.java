//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Created On      : Tue Jun 30 12:56:23 1998
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Wed Mar 10 13:40:22 1999

package fsats.util;


import fsats.util.Command;

/**
 * This class kills all of the fsats processes.
 */
public class KillWS extends Command
{


    public KillWS()
    {
        // use the legacy kill.ws script
        String cmd = fsatsHome + separator + "tools" + separator + "kill.ws";
        
        cmdArray = new String[] { cmd };
    }
    
    public static void main( String[] args )
    {
        KillWS cmd = new KillWS();
        int status = -1;
        try {
             status = cmd.execute();
        } catch( Exception e ) {
            e.printStackTrace();
        }
        System.out.println( "KillWS=" + status );
    }
        
}
