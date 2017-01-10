package composer.args ;

class OptionException extends ArgumentException {

    public OptionException (String optionName, String message) {
	super ("option \"" + optionName + "\" " + message) ;
	option = optionName ;
    }

    public String getOption () {
	return option ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Private material below this point.                                    */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private final String option ;

}
