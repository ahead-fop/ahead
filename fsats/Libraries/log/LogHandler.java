package log ;

import java.util.logging.ConsoleHandler ;
import java.util.logging.Level ;
import java.util.logging.LogRecord ;

final class LogHandler extends ConsoleHandler {

    /**
     * Overrides {@link ConsoleHandler#publish(LogRecord)} to print a labeled,
     * single-line log message on {@link System#err}.
     **/
    final public void publish (LogRecord record) {

	System.err.println (prefix + record.getMessage ()) ;

	if (record.getLevel () == Level.SEVERE)
	    ++ severe ;
	else if (record.getLevel () == Level.WARNING)
	    ++ warning ;
    }

    final public int getSevere () {
	return severe ;
    }

    final public int getWarning () {
	return warning ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    /**
     * Package-private constructor to prevent external instantiation.
     **/
    LogHandler (String label) {
	super () ;
	prefix = label + ": " ;
    }

    private final String prefix ;

    private int severe = 0 ;
    private int warning = 0 ;

}
