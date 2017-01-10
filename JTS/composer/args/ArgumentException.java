package composer.args ;

public class ArgumentException extends Exception {

    public ArgumentException (String message) {
	super (message) ;
    }

    public ArgumentException (String message, Throwable thrown) {
	super (message, thrown) ;
    }

    public ArgumentException (Throwable thrown) {
	super (thrown) ;
    }

}
