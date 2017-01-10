package composer.args ;

final class PatternHandler implements Handler {

    public final Object getValue () {
	return new PatternFilter (defaultValue) ;
    }

    public final Object handle (String argument) {
	return new PatternFilter (argument) ;
    }

    public final boolean requiresValue () {
	return true ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Private and protected material below this line.                       */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    PatternHandler (String defaultValue) {
	this.defaultValue = defaultValue ;
    }

    PatternHandler () {
	this.defaultValue = null ;
    }

    static final PatternHandler NULL_HANDLER = new PatternHandler () ;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private final String defaultValue ;

}
