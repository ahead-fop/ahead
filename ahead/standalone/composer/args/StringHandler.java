package composer.args ;

final class StringHandler implements Handler {

    public final Object getValue () {
	return defaultValue ;
    }

    public final Object handle (String argument) {
	return argument ;
    }

    public final boolean requiresValue () {
	return true ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Private and protected material below this line.                       */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    StringHandler (String defaultValue) {
	this.defaultValue = defaultValue ;
    }

    StringHandler () {
	this.defaultValue = null ;
    }

    static final StringHandler NULL_HANDLER = new StringHandler () ;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private final String defaultValue ;

}
