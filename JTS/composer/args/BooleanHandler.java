package composer.args ;

final class BooleanHandler implements Handler {

    BooleanHandler (boolean initialValue) {
	currentValue = Boolean.valueOf (initialValue) ;
    }

    BooleanHandler (Boolean initialValue) {
	this (initialValue.booleanValue ()) ;
    }

    public final Object getValue () {
	return currentValue ;
    }

    public final Object handle (String argument) {
	currentValue =
	    argument != null
	    ? Boolean.valueOf (argument)
	    : Boolean.valueOf (currentValue.equals (Boolean.FALSE)) ;

	return currentValue ;
    }

    public final boolean requiresValue () {
	return false ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Private material below this point.                                    */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private Boolean currentValue ;

}
