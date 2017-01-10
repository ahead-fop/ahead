//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.2 $
// Author          : Theodore Cox Beckett

package fsats.util;

import fsats.util.FsatsProperties;
import fsats.util.Log;
import java.io.*;

/**
 * This execute system commands.  It provides a blocking execute and
 * implements the Runnable interface so the command can be executed in
 * the background.  This class pipes the std err and std out of the
 * command to be executed to the java runtime's std err/std out.
 *
 */
public class Command implements Runnable
{

    protected static final String separator =
    System.getProperty( "file.separator" );

    protected static final String fsatsHome =
    FsatsProperties.get( FsatsProperties.FSATS_HOME );

    protected String cmdArray[] = new String[0];

    protected String env[] = new String[0];




    public Command( String[] cmdArray, String[] env )
    {
        this.cmdArray = cmdArray;
        this.env = env;
    }


    
    
    /**
     * This constructor allows subclass constructors to build and set
     * the instance variables cmdArray and env.
     */
    protected Command()
    {}


    

    /**
     * Execute the command passed in to this object's constructor.
     * The command is executed using the environment passed to the
     * constructor.  The caller blocks until the command completes.
     * The command's std out and std err are read and piped to
     * System.out and System.err.
     *
     * @return the exit value of the command
     */     
    public int execute() throws IOException, InterruptedException
    {
        //debug();
        
        Process p;
        if ( env == null || env.length == 0 )
            p = Runtime.getRuntime().exec( cmdArray );
        else
            p = Runtime.getRuntime().exec( cmdArray, env );
        
        Pipe outPipe = new Pipe( p.getInputStream(), System.out );
        outPipe.start();
        Pipe errPipe = new Pipe( p.getErrorStream(), System.err );
        errPipe.start();
        
        int status = p.waitFor();
        
        try {
            outPipe.setStopped();
        } catch( SecurityException e ) {}
        try {
            errPipe.setStopped();
        } catch( SecurityException e ) {}
        
        return status;
    }
    


    

    /**
     * This method implements Runnable.run().  It invokes the execute()
     * method of this class in a separate thread.
     */
    public void run()
    {
        try {
            int status = execute();
            if (status != 0)
                Log.warning( "UTILITY", "Command " + cmdArray[0] +
                             " returned unexpected value: " + status);
        } catch( Exception e ) {
            Log.error( "UTILITY", "Error executing " + cmdArray[0], e );
        }
    }
    



    /**
     * Returns the cmdArray formatted to be readable.
     */
    public String toString()
    {
        return "Command: " + formatCmdArray();
    }

    
    
    /**
     * This thread pipes the InputStream to the PrintStream,
     * exiting upon end of stream.
     */
    private class Pipe extends Thread
    {
        private BufferedReader reader;
        private PrintStream out;
        
        public Pipe( InputStream in, PrintStream out )
        {
           reader = new BufferedReader( new InputStreamReader(in) );
           this.out = out;
	   this.stopped = false ;
        }

	public synchronized boolean isStopped ()
	{
	    return stopped ;
	}

        public void run()
        {
            try {
                String line = reader.readLine();
                while ( line != null && ! isStopped ())
		{
		    out.println( line );
		    out.flush();
		    yield();
		    line = reader.readLine();
		}
                reader.close();
            } catch( IOException e ) {
                System.out.println("Command: " + e );
            }
        }

	public synchronized void setStopped ()
	{
	    stopped = true ;
	}

	private boolean stopped ;
    }




    protected String formatCmdArray()
    {
        String s = cmdArray[0];
        for (int i=1; i<cmdArray.length; i++)
            s = s + " " + cmdArray[i];
        return s;
    }


    
    private void debug()
    {
        System.out.println( "executing " + formatCmdArray() );
    }
    

}    

