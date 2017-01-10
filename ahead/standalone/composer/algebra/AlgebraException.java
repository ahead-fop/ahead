package composer.algebra ;

public class AlgebraException extends Exception {

    public AlgebraException (String message) {
	super (message) ;
    }

    public AlgebraException (String message, Throwable thrown) {
	super (message, thrown) ;
    }

    public AlgebraException (Throwable thrown) {
	super (thrown) ;
    }

}
