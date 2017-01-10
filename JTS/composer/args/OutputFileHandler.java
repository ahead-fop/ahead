package composer.args ;

import composer.Checks ;

import java.io.File ;

final class OutputFileHandler implements Handler {

    public final Object getValue () throws FileArgumentException {
	try {
	    return elseFile != null ? Checks.outputFile (elseFile) : null ;
	} catch (Exception except) {
	    throw new FileArgumentException (except) ;
	}
    }

    public final Object handle (String argument) throws FileArgumentException {
	try {
	    return Checks.outputFile (new File (directory, argument)) ;
	} catch (Exception except) {
	    throw new FileArgumentException (except) ;
	}
    }

    public final boolean requiresValue () {
	return true ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Private and protected material below this line.                       */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    OutputFileHandler (File elseDirectory, File elseFile) {
	this.elseFile = elseFile ;
	this.directory = elseDirectory ;
    }

    OutputFileHandler (File elseFile) {
	this (null, elseFile) ;
    }

    OutputFileHandler () {
	this (null) ;
    }

    static final OutputFileHandler NULL_HANDLER = new OutputFileHandler () ;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private final File elseFile ;
    private final File directory ;

}
