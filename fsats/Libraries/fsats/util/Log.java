//                              -*- Mode: Java -*- 
// File            : $Workfile:    $
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Ted Beckett

package fsats.util;

import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.Date;
import java.text.SimpleDateFormat;

import fsats.util.DataContainer;
import fsats.util.FsatsProperties;

/**
 * This class implements a thread safe log for handling
 * debug-type text messages.  Messages to be logged must
 * have an associated message tag. The tag normally identifies
 * a subsytem or functional area.  The tag serves to identify
 * the source of messages by functional area and supports the use of grep
 * when analyzing the log file.
 *
 * The log may be configured to:
 *   (1) log only certain classes or levels of messages
 *   (2) log only messages from certain sources or message with certain tags.
 *   (3) log to stdout, a file or both.
 *
 * Messages to be logged are categorized as being at one of four levels:
 * <debug>, <status>, <warning> and <error>.
 * The levels are ordered with <debug> being the least.  The specification of a
 * level means that that level and all levels greater than it are logged.
 * For example,  if the level is warning, then warning and error messages
 * are logged.  If the level is error, only error messages are logged.
 *
 * Configuration of this log is done via the Fsats env var ERROR_LOG.
 * This env var is also used by the equivalent Ada package ERROR_LOG.  
 * The format of the value of ERROR_LOG is:
 * error_log=(tag1=(console=level file=level)
 * tag2=(console=level file=level)... tagn=(console=level file=level))
 *
 * <Tagn> corresponds directly to msg tags, which correspond to
 * subsytem/functional area identifiers.  This allows logging to be
 * configured on a subystem basis.
 * The value for <level> is one of debug, status, warning, error.
 * <Console> controls what is written to stdout and <file> specifies
 * what is written to the log file.  For example:
 * error_log=(doe=(console=warning file=debug) would write warning and
 * error msgs to stdout and all msgs, debug, status, warning and error,
 * the file.
 *
 * The special tag "default" allows setting a default for any
 * tags not explicitly specified.  An example is:
 * error_log=(default=(console=level file=level)
 *
 * Unless overriden with the "default" tag, the implicit default
 * setting is console=WARNING and file=STATUS.
 *
 */
public class Log
{
    /**
     * Predefined message tags.  Each tag corresponds to a subsytem or
     * functionally related group of classes.
     */
    public static String DOE = "DOE";
    public static String UI = "UI";
    public static String PLAN = "PLAN";

    
    private static final Level ERROR = new Level( Level.ERROR );
    private static final Level WARNING = new Level( Level.WARNING );
    private static final Level STATUS = new Level( Level.STATUS );
    private static final Level DEBUG = new Level( Level.DEBUG );
    private static final Level EXCEPTION = new Level( Level.EXCEPTION );

    // cmd line args of main program
    private static String[] args = new String[0];
    
    // optional prefix prepended to file name
    private static String fileNamePrefix = "";

    // Data Container labels for parsing ERROR_LOG env var
    private static final String FILE_LABEL = "file";
    private static final String CONSOLE_LABEL = "console";
    private static final String DEFAULT_TAG_LABEL = "default";
    
    // tags are padded to this length to improve output readability
    private static final int NORMALIZED_TAG_LENGTH = 12;

    private static LogLevel defaultLevel = new LogLevel();
    
    private static PrintWriter logFile;
    
    private static boolean logFileClosed = false;

    private static Hashtable logLevelTable = getLogLevelTable();

    
    
    private Log()
    {}

    
    /**
     * If error-message file-level logging is enabled, write msgTag/msg
     * to the log file.
     * If error-message console-level logging is enabled, write msgTag/msg
     * to stdout.
     *
     * @param msgTag written to the log on the same line as msg.
     * @param msg the error message
     */
    public synchronized static void error( String msgTag, Object msg )
    {
        write( ERROR, msgTag, msg.toString() );
    }

    /**
     * Same as the other error() method,  on also print the stack trace
     * of throwable.
     */
    public synchronized static void error( String msgTag, Object msg,
                                           Throwable t )
    {
        write( ERROR, msgTag, msg.toString() + "; stack trace follows:" );
        t.printStackTrace();
        if ( logFile == null )
            createLogFile();
        t.printStackTrace( logFile );
    }

    public synchronized static void error( String msgTag, Throwable t )
    {
        Log.error( msgTag, "", t );
    }

    /**
     * If warning-message file-level logging is enabled, write msgTag/msg
     * to the log file.
     * If warning-message console-level logging is enabled, write msgTag/msg
     * to stdout.
     *
     * @param msgTag written to the log on the same line as msg.
     * @param msg the warning message
     */
    public synchronized static void warning( String msgTag, Object msg )
    {
        write( WARNING, msgTag, msg.toString() );
    }

    /**
     * If status-message file-level logging is enabled, write msgTag/msg
     * to the log file.
     * If status-message console-level logging is enabled, write msgTag/msg
     * to stdout.
     *
     * @param msgTag written to the log on the same line as msg.
     * @param msg the error message
     */
    public synchronized static void status( String msgTag, Object msg )
    {
        write( STATUS, msgTag, msg.toString() );
    }

    /**
     * If debug-message file-level logging is enabled, write msgTag/msg
     * to the log file.
     * If debug-message console-level logging is enabled, write msgTag/msg
     * to stdout.
     *
     * @param msgTag written to the log on the same line as msg.
     * @param msg the debug message
     */
    public synchronized static void debug( String msgTag, Object msg )
    {
        write( DEBUG, msgTag, msg.toString() );       
    }


    /**
     * Write throwable and it's stack trace to stdout and the log file.
     */
    public synchronized static void exception( Throwable throwable )
    {
        exception( "", throwable );
    }

    
    /**
     * Write msgTag/throwable and it's stack trace to stdout and the log file.
     */
    public synchronized static void exception( String msgTag,
                                               Throwable throwable )
    {
        write( EXCEPTION, msgTag, " stack trace follows" );
        throwable.printStackTrace();
        throwable.printStackTrace( logFile );
    }


    
    /**
     * This is an optional method.  It may be used to pass in the
     * cmd line args of the main program.
     */
    public synchronized static void setArgs( String[] args )
    {
        Log.args = args;
        writeCmdLineArgs();
    }



    /**
     * The default file name is log.host.processID.  If this method
     * is called before the first write to the log file occurs, the file name
     * will be prefix + "log.host.processID".
     */
    public synchronized static void setFileNamePrefix( String prefix)
    {
        fileNamePrefix = prefix;
    }

    
    /**
     * Permanently close the log file.  Once closed, the log will not
     * be automatically reopened by a write. 
     */
    public synchronized static void close()
    {
        if ( logFile != null )
            {
                logFile.close();
                logFile = null;
                logFileClosed = true;
            }
    }


    /**
     * Returns the name of the log file.  The format of the file name is
     * log.host.processID.
     */
    private static String getFileName()
    {
        String host = "host";
        try {
            host = InetAddress.getLocalHost().getHostName();
            int i = host.indexOf( '.' );
            if ( i > 0 && i < host.length() )
                host = host.substring( 0, i );
        } catch (Throwable e) {
            System.out.println("Class Log: error getting host name");
            e.printStackTrace();
        }

        String pid = String.valueOf( ProcessID.get() );
        String separator = System.getProperty( "file.separator" );
        String fsatsHome = FsatsProperties.get( FsatsProperties.FSATS_HOME );
        String fileName = fsatsHome + separator + "cfg0" + separator
            + fileNamePrefix + "log." + host + "." + pid;
        
        return fileName;
    }



    private static void write( Level level, String tag, String msg )
    {
        // build formatted output msg
        SimpleDateFormat formatter = new SimpleDateFormat( "MM-dd:hh:mm:ss");
        String currTime = formatter.format( new Date() );
        String localTag = new String( tag );
        while ( localTag.length() < NORMALIZED_TAG_LENGTH )
            localTag = localTag + " ";
        localTag = localTag + ";";
        String localLevel = new String( level.toString() );
        while ( localLevel.length() < Level.MAX_LENGTH )
            localLevel = localLevel + " ";
        String logMsg = localLevel + ": " + localTag + currTime + ";   " + msg;

        // get log level for msg tag
        LogLevel tagLevel = (LogLevel)logLevelTable.get( tag );
        if (tagLevel == null)
            tagLevel = defaultLevel;

        if ( level.greaterThanOrEqual( tagLevel.file ) )
            {
                // logFile will only get closed when the application exits, so
                // once logFile is closed ensure it is never reopened,
                // otherwise the log could get truncated
                if ( logFile == null && !logFileClosed )
                    createLogFile();

                if ( logFile != null )
                    {
                        logFile.println( logMsg );
                        logFile.flush();
                    }               
            }
        
        if ( level.greaterThanOrEqual( tagLevel.console ) )
            System.out.println( logMsg );
    }

    
    
    private static void createLogFile()
    {
        String filename = getFileName();
        try {
            logFile = new PrintWriter( new FileWriter( filename ) );
        } catch( IOException e ) {
            System.out.println( "Log: " + e );
        }
        writeCmdLineArgs();
    }


    private static void writeCmdLineArgs()
    {
        if ( logFile != null && args != null )
            {
                for (int i = 0; i < args.length; i++ )
                    logFile.println( "Command Line args[" + i + "] = " 
                                     + args[i] );
            }
    }


    /**
     * Read the system property ERROR_LOG and store the specified log levels
     * into a hashtable, keyed by tag.
     */
    private static Hashtable getLogLevelTable()
    {
        Hashtable table = new Hashtable();
        String str = FsatsProperties.get( FsatsProperties.ERROR_LOG );
        if (str == null || str.length() == 0)
            return table;
        DataContainer container;
        try {
            container = DataContainer.read( str.toLowerCase() );
        } catch( IOException e ) {
            System.out.println("Log: Error parsing ERROR_LOG env var: " + e);
            return table;
        }
        int numFields = container.getFieldCount();
        for (int i = 1; i <= numFields; i++)
            {
                DataContainer tagCont = container.getField( i );
                String tag = tagCont.getLabel();
                DataContainer fileCont = tagCont.getField( FILE_LABEL );
                DataContainer consoleCont = tagCont.getField( CONSOLE_LABEL );
                LogLevel tagLevel =
                    new LogLevel( fileCont.getValue(),
                                  consoleCont.getValue() );
                if ( tag.equals( DEFAULT_TAG_LABEL ) )
                    defaultLevel = tagLevel;
                else
                    table.put( tag, tagLevel );
            }
        dumpLogLevelTable();
        return table;
    }


    
    // This method is for debugging
    private static void dumpLogLevelTable()
    {
        System.out.println("Log: default=" + defaultLevel);
        if ( logLevelTable != null )
            {
                java.util.Enumeration e = logLevelTable.keys();
                while ( e.hasMoreElements() )
                    {
                        String tag = (String)e.nextElement();
                        LogLevel l = (LogLevel)logLevelTable.get( tag );
                        System.out.println("Log: " + tag + " " + l);
                    }        
            }
    }
    

    /**
     * test driver
     */
    public static void main( String[] args )
    {
        Log.setArgs( args );
        // settings for error_log to copy and paste to cmd line
        // "error_log=(doe=(console=error file=debug)
        //  default=(console=status file=error))"
        Log.debug( "doe", "debug msg" );
        Log.status( "doe", "status msg" );
        Log.warning( "doe", "warning msg" );
        Log.error( "doe", "error msg" );        
        Log.debug( "tmt", "debug msg" );
        Log.status( "tmt", "status msg" );
        Log.warning( "tmt", "warning msg" );
        Log.error( "tmt", "error msg" );
        Log.close();
    }

}




/**
 * This class implements an enumeration of the log levels.
 */
class Level
{
    /**
     * The log levels
     */
    public static final String DEBUG =   "debug";
    public static final String STATUS =  "status";
    public static final String WARNING = "warning";
    public static final String ERROR =   "error";
    public static final String EXCEPTION =   "exception";

    /**
     * Max length of the log level strings
     */
    public static final int MAX_LENGTH = 9;

    private static final int DEBUG_INT = 0;
    private static final int STATUS_INT = 1;
    private static final int WARNING_INT = 2;
    private static final int ERROR_INT = 3;
    private static final int EXCEPTION_INT = 4;

    private String strValue;
    private int intValue;
    
    public Level( String s )
    {
        if (s == null)
            { strValue = STATUS; intValue = STATUS_INT; }
        else if (s.equals(EXCEPTION))
            { strValue = EXCEPTION; intValue = EXCEPTION_INT; } 
        else if (s.equals(ERROR))
            { strValue = ERROR; intValue = ERROR_INT; } 
        else if (s.equals(WARNING))
            { strValue = WARNING; intValue = WARNING_INT; }
        else if (s.equals(STATUS))
            { strValue = STATUS; intValue = STATUS_INT; }
        else if (s.equals(DEBUG))
            { strValue = DEBUG; intValue = DEBUG_INT; }
        else
            { strValue = STATUS; intValue = STATUS_INT; }
    }      

    public boolean greaterThanOrEqual( Level level )
    {
        return this.intValue >= level.intValue;
    }
              
    public String toString()
    {
        return strValue;
    }
    
    public int toInt()
    {
        return intValue;
    }    
}



/**
 * This class is a container for two Levels, one specifying the level
 * of logging to stdout and the other to the log file.
 */
class LogLevel
{
    public Level console;  // stdout
    public Level file;


    /**
     * Construct a LogLevel with default values for file and stdout.
     */
    public LogLevel()
    {
        this( Level.STATUS, Level.WARNING );
    }

    public LogLevel( String fileLevel, String consoleLevel )
    {
        this.file = new Level( fileLevel );
        this.console = new Level( consoleLevel );
    }

    public String toString()
    {
        return "LogLevel[file=" + file +  " console=" + console + "]";
    }
}

    

    
