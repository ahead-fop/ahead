package composer.args ;

import composer.Checks ;

import java.io.File ;

final class InputFileHandler implements Handler {

    public final Object getValue () throws FileArgumentException {
	try {
	    return elseFile != null ? Checks.inputFile (elseFile) : null ;
	} catch (Exception except) {
	    throw new FileArgumentException (except) ;
	}
    }

    public final Object handle (String argument) throws FileArgumentException {
	try {
	    return Checks.inputFile (new File (directory, argument)) ;
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

    InputFileHandler (File elseDirectory, File elseFile) {
	this.elseFile = elseFile ;
	this.directory = elseDirectory ;
    }

    InputFileHandler (File elseFile) {
	this (null, elseFile) ;
    }

    InputFileHandler () {
	this (null) ;
    }

    static final InputFileHandler NULL_HANDLER = new InputFileHandler () ;

    private final File elseFile ;
    private final File directory ;

}
