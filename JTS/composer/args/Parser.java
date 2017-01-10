package composer.args ;

import composer.Checks ;

import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Map ;

/**
 * An argument <code>Parser</code> parses "command-line" arguments represented
 * as {@link java.lang.String} arrays, acting as a
 * <a href="http://www.meurrens.org/ip-Links/java/designPatterns/builder/">
 * Builder</a> for objects satisfying the {@link Arguments} interface.
 *
 * <p>
 * Arguments are classified as one of two types: <em>operands</em> (positional
 * arguments) and <em>options</em> (named arguments).
 **/
public final class Parser {

    public Parser (Handler operandHandler) {

	Checks.nonNullArgument (operandHandler, "operandHandler") ;

	this.optionHandlers = new HashMap () ;
	this.operandHandler = operandHandler ;
    }

    public Parser () {
	this (Handlers.stringHandler ()) ;
    }

    public final void addHandler (String name, Handler handler) {

	Checks.nonNullArgument (handler, "handler") ;
	Checks.nonNullArgument (name, "name") ;

	optionHandlers.put (name, handler) ;
    }

    /**
     * Parses an array of argument strings according to type information stored
     * in this {@link Parser} instance, separating option specifiers from
     * operands.  The result is returned in an {@link Arguments} instance.
     **/
    public final Arguments parse (String[] argArray) throws ArgumentException {

	List args = Arrays.asList (argArray) ;
	List operands = new ArrayList () ;
	Map options = new HashMap () ;

	for (Iterator p = args.iterator () ; p.hasNext () ; ) {
	    String arg = (String) p.next () ;

	    // "--" as an argument means all remaining arguments are operands:
	    //
	    if (arg.equals ("--")) {
		while (p.hasNext ())
		    operands.add (operandHandler.handle ((String) p.next ())) ;
		break ;
	    }

	    // Separate options from operands:
	    //
	    if (arg.startsWith ("--"))
		longOption (arg.substring (2), p, options) ;
	    else if (arg.startsWith ("-"))
		dashOption (arg.substring (1), p, options) ;
	    else
		operands.add (operandHandler.handle (arg)) ;
	}

	Iterator p = optionHandlers.entrySet().iterator () ;
	while (p.hasNext ()) {
	    Map.Entry entry = (Map.Entry) p.next () ;
	    String option = (String) entry.getKey () ;

	    if (! options.containsKey (option)) {
		Object value = ((Handler) entry.getValue ()) . getValue () ;
		if (value == null)
		    throw new OptionException (option, "must be specified") ;
		options.put (option, value) ;
	    }
	}

	return new ParserArguments (operands, options) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Private material appears below this point.                            */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private final void dashOption (String name, Iterator p, Map options)
    throws ArgumentException {

	Handler handler = (Handler) optionHandlers.get (name) ;
	if (handler == null)
	    throw new OptionException (name, "is unrecognized") ;

	if (! handler.requiresValue ())
	    setOption (name, null, options) ;
	else if (p.hasNext ())
	    setOption (name, (String) p.next (), options) ;
	else
	    throw new OptionException (name, "requires a value") ;
    }

    private final void longOption (String name, Iterator p, Map options)
    throws ArgumentException {

	int equals = name.indexOf ('=') ;

	if (equals < 0)
	    dashOption (name, p, options) ;

	else {
	    String key = name.substring (0, equals) ;
	    String value = name.substring (1+equals) ;

	    setOption (key, value, options) ;
	}
    }

    private final void setOption (String key, String value, Map options)
    throws ArgumentException {

	Handler handler = (Handler) optionHandlers.get (key) ;
	if (null == handler)
	    throw new OptionException (key, "is not a valid option") ;

	options.put (key, handler.handle (value)) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private final Map optionHandlers ;
    private final Handler operandHandler ;

}
