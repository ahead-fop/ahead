package composer.args ;

import java.io.File ;

class FileArgumentException extends ArgumentException {

    public FileArgumentException (String message) {
	super (message) ;
    }

    public FileArgumentException (Exception except) {
	super (except) ;
    }

}
