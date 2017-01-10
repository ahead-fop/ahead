package log;

import java.util.logging.Formatter ;
import java.util.logging.Handler ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.util.logging.StreamHandler ;

/**
 * An application-common logging facility for messages, errors and warnings.
 **/
final public class Log {

    public static void error (final String message) {
	LOGGER.severe (message) ;
    }
    
    public static void log (final String message) {
	LOGGER.info (message) ;
    }
    
    public static void warn (final String message) {
	LOGGER.warning (message) ;
    }
    
    final public static String NAME = "fsats" ;
    final public static Logger LOGGER = Logger.getLogger (NAME) ;
    final public static Handler HANDLER = new LogHandler (NAME) ;

    static {
	LOGGER.setLevel (Level.INFO) ;
	LOGGER.setUseParentHandlers (false) ;
	LOGGER.addHandler (HANDLER) ;
    }
    
}
