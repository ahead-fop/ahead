package composer ;

import java.io.BufferedReader ;
import java.io.InputStream ;
import java.io.InputStreamReader ;
import java.io.IOException ;
import java.io.PrintStream ;

import java.util.logging.Level ;
import java.util.logging.Logger ;

final class CopyRunnable implements Runnable {

    CopyRunnable (InputStream input, PrintStream print) {

	Checks.nonNull (input, "input") ;
	Checks.nonNull (print, "print") ;

	this.input = new BufferedReader (new InputStreamReader (input)) ;
	this.print = print ;
    }

    public void run () {

	try {
	    for (String line ; (line = input.readLine ()) != null ; )
		print.println (line) ;
	} catch (IOException e) {
	    Logger logger = Logger.getLogger ("composer") ;
	    logger.log (Level.WARNING, "exception while copying stream", e) ;
	}

	try {
	    input.close () ;
	} catch (IOException exception) {
	    /* Close exception deliberately ignored. */
	}
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private BufferedReader input ;
    final private PrintStream print ;

}
