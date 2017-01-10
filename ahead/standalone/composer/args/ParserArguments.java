package composer.args ;

import java.util.Collections ;
import java.util.List ;
import java.util.Map ;

/**
 * A <em>package-private</em> class encapsulating the implementation of
 * {@link Arguments} objects returned from {@link Parser}.
 **/
final class ParserArguments implements Arguments {

    ParserArguments (List operands, Map options) {
	operandsList = Collections.unmodifiableList (operands) ;
	optionsMap = Collections.unmodifiableMap (options) ;
    }

    final public Object getOption (String optionName) {
	return optionsMap.get (optionName) ;
    }

    final public List operands () {
	return operandsList ;
    }

    final public String toString () {
	return "options are " + optionsMap + "; operands are " + operandsList ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private final List operandsList ;
    private final Map optionsMap ;

}
