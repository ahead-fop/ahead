package composer ;

import java.io.IOException ;

import java.util.logging.Logger ;

final class Command {

    Command (String[] command) throws IOException {

	Checks.nonNull (command, "command") ;
	Checks.require (command.length > 0, "no command specified") ;

	process = Runtime.getRuntime().exec (command) ;
	process.getOutputStream().close () ;

	error = new CopyRunnable (process.getErrorStream (), System.err) ;
	new Thread(error).start () ;

	input = new CopyRunnable (process.getInputStream (), System.out) ;
	new Thread(input).start () ;
    }

    final int waitFor () throws InterruptedException {
	return process.waitFor () ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private Process process ;

    final private Runnable error ;
    final private Runnable input ;

}
