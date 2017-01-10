//                              -*- Mode: Java -*- 
// FsatsEnvironment.java --- 
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Created On      : Tue Jun 30 12:56:23 1998
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Tue Sep 29 16:35:18 1998
// 

package fsats.misc;

/**
 * This class defines and provides access to FSATS env vars.
 *
 * Implementation Note:
 * The current implementation assumes all env vars defined here are
 * passed in to this java program as system properties via the cmd line
 * definition.
 *
 * If the number of fsats env vars accessed grows, the implementation
 * could be changed so that the system property ENV_FILE is read to get the
 * name of a file assumed to contain fsats env variables in the format expected
 * by the Properties class (the env shell command can be run with it's output
 * piped to a file.  the name of this file would be the value of the property
 * ENV_FILE).
 */
public class FsatsEnvironment
{
    public static final String TWO_TASK = "TWO_TASK";

    public static final String ORACLE_HOST_URL = "ORACLE_HOST_URL";
    
    public static final String LOG_DIR = "LOG_DIR";

    public static final String ERROR_LOG = "ERROR_LOG";


    /**
     * Return the value of the specified env var.
     * Null is returned if the value is not found.
     */
    public static String get( String name )
    {
        return System.getProperty( name );
    }
        
    /**
     * Return the value of the specified env var,
     * default is returned if the value is not found.
     */
    public static String get( String name, String defaultValue )
    {
        return System.getProperty( name, defaultValue );
    }

    private FsatsEnvironment()
    {}
    
}
