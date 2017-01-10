package composer.unit ;

public class EquationException extends Exception {

    public EquationException (String message) {
	super (message) ;
    }

    public EquationException (String operand, String message) {
	this ("equation name \"" + operand + "\" " + message) ;
    }

    public EquationException (String message, Throwable thrown) {
	super (message, thrown) ;
    }

}
