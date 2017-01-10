//                              -*- Mode: Java -*- 
// File            : $Workfile:    $
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Ted Beckett
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Mon Dec  7 15:19:07 1998
// History


package fsats.util;

import java.io.*;
import java.util.Date;
import java.lang.Runtime;
import fsats.util.Log;

/**
 * This class implements a singleton object which returns the process ID
 * of the java virtual machine.
 */
public class ProcessID
{
    private static int pid = -1;
    
    private ProcessID()
    {}

    /**
     * Get the process id of this program.  On error, return an int based
     * on the current time.
     */
    public static int get()
    {
        if (pid == -1)
            {
                String separator = System.getProperty( "file.separator" );
                
                // name of c program which write this VM's process id to a file
                String programName = "pid_to_file";
                
                // name of the file written to by the c program
                String fileName = separator + "tmp" + separator + "javapid";
                
                String fsatsHome =
                    FsatsProperties.get( FsatsProperties.FSATS_HOME );

                // full path of c program
                String cmd = fsatsHome + separator + "bin" +
                    separator + programName;
                
                try {
                    Process p = Runtime.getRuntime().exec( cmd );
                    p.waitFor();
                    BufferedReader reader =
                        new BufferedReader( new FileReader( fileName ) );
                    String str = reader.readLine();
                    pid = Integer.parseInt( str );
                } catch( Exception e ) {
                    Log.error( "UTIL", "ProcessID.get: unable to get"
                               + " process id; using generated value" );
                    long t = new Date().getTime();
                    pid = (int)( t % 100000000 );                    
                }
            }
        return pid;
    }

    public static void main( String[] args )
    {
        System.out.println( "pid=" + get() );
        for (;;)
            ;
    }

}
