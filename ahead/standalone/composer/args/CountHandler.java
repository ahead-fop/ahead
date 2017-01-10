package composer.args ;

final class CountHandler implements Handler {

    CountHandler (Integer initialValue) {
	this.currentValue = initialValue ;
    }

    CountHandler (int initialValue) {
	this (new Integer (initialValue)) ;
    }

    public final Object getValue () {
	return currentValue ;
    }

    public final Object handle (String argument) {
	currentValue =
	    argument != null
	    ? Integer.valueOf (argument)
	    : new Integer (1 + currentValue.intValue ()) ;

	return currentValue ;
    }

    public final boolean requiresValue () {
	return false ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private Integer currentValue ;

}
