package composer.args ;

final class IntegerHandler implements Handler {

    IntegerHandler (Integer defaultValue) {
	this.defaultValue = defaultValue ;
    }

    IntegerHandler (int defaultValue) {
	this (new Integer (defaultValue)) ;
    }

    public final Object getValue () {
	return defaultValue ;
    }

    public final Object handle (String argument) {
	return Integer.valueOf (argument) ;
    }

    public final boolean requiresValue () {
	return true ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private final Integer defaultValue ;

}
