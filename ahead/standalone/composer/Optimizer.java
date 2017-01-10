package composer ;

import composer.algebra.AlgebraException ;
import composer.algebra.AlgebraFactory ;
import composer.algebra.Collective ;
import composer.algebra.Function ;
import composer.algebra.Signature ;
import composer.algebra.Type ;
import composer.algebra.Unit ;

import composer.args.Arguments ;
import composer.args.ArgumentException ;
import composer.args.Handler ;
import composer.args.Handlers ;
import composer.args.Parser ;

import composer.unit.EquationException ;
import composer.unit.EquationFileUnit ;
import composer.unit.EquationsFileUnit;
import composer.unit.FileUnit ;
import composer.unit.FileUnitComposeFunction ;

import java.io.File ;
import java.io.IOException ;

import java.net.URI ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.Comparator ;
import java.util.HashSet ;
import java.util.Iterator ;
import java.util.List ;
import java.util.ListIterator ;
import java.util.Map ;
import java.util.Properties ;
import java.util.Set ;
import java.util.TreeMap ;
import java.util.TreeSet ;

import java.util.logging.Level ;
import java.util.logging.Logger ;

final public class Optimizer {

    public static final String COPYRIGHT =
        "(C) 2002 The University of Texas at Austin" ;

    final public static String NAME = "optimizer" ;
    final public static Logger LOGGER = Main.LOGGER ;
    final public static LogHandler LOG_HANDLER = new LogHandler (NAME) ;
    final public static String VERSION = "v2002.09.26alpha" ;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    final private static void abort (String message, Throwable thrown) {
	thrown.printStackTrace (System.err) ;
	LOGGER.log (Level.SEVERE, message, thrown) ;
	exit (1) ;
    }

    final private static void abort (String message) {
	LOGGER.log (Level.SEVERE, message) ;
	exit (1) ;
    }

    final private static void exit (int status) {
	Main.exit (status, LOG_HANDLER) ;
    }

    public static final void main (String[] argsMain) {

	LOGGER.setLevel (Level.WARNING) ;
	LOGGER.setUseParentHandlers (false) ;
	LOGGER.addHandler (LOG_HANDLER) ;

	Properties appProperties = Main.initProperties (
	    System.getProperty(Main.RESOURCE,Main.RESOURCE).trim ()
	) ;

	Arguments args = argsParse (argsMain) ;

	if (args.operands().size () < 1)
	    usage ("no equation(s) file specified") ;

	// Convert input equation files to optimized expressions:
	//
	Set expressions = null ;
	try {
	    expressions = equations2expressions (args.operands ()) ;
	} catch (Exception exception) {
	    exception.printStackTrace (System.err) ;
	    abort ("can't process equation(s) files: ", exception) ;
	}

	// println ("BEGIN: original expressions from files") ;
	// Expression.dump (System.out) ;
	// println ("END: original expressions from files") ;

	// Select those {@link Expression} objects that have been assigned to
	// targets.  While doing that, relativize all {@link File} operands:
	//
	List targets = new ArrayList () ;
	for (Iterator p = expressions.iterator () ; p.hasNext () ; ) {
	    Expression expression = (Expression) p.next () ;
	    if (expression.getTarget () != null)
		targets.add (expression) ;
	    expression.relativize (new File (".") . toURI ()) ;
	}

	// Update reference counts:
	//
	Expression.clearReferences () ;
	for (Iterator p = targets.iterator () ; p.hasNext () ; )
	    Expression.updateReferences ((Expression) p.next ()) ;

	// println ("BEGIN: shortened expressions from files") ;
	// Expression.dump (System.out) ;
	// println ("END: shortened expressions from files") ;

	// Assign intermediate targets to shared sub-expressions:
	//
	for (Iterator p = targets.iterator () ; p.hasNext () ; ) {
	    Expression target = (Expression) p.next () ;
	    target.intermediaries (target.getTarget ()) ;
	}

	// println ("BEGIN: expressions with intermediaries") ;
	// Expression.dump (System.out) ;
	// println ("END: expressions with intermediaries") ;

	// Print out expressions in decreasing order by reference count:
	//
	expressions = new TreeSet (expressions) ;
	for (Iterator p = expressions.iterator () ; p.hasNext () ; ) {
	    Expression expression = (Expression) p.next () ;
	    String target = expression.getTarget () ;
	    if (target != null) {
		int references = expression.getReferences () ;
		println (
		    String.valueOf (references)
		    + ' '
		    + target
		    + " = "
		    + expression.composition ()
		) ;
	    }
	}

	exit (0) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Private static material below this line:                              */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private static Arguments argsParse (String[] argsArray) {

	Parser parser = new Parser (Handlers.stringHandler ()) ;
	parser.addHandler ("help", USAGE_HANDLER) ;
	parser.addHandler ("logging", Handlers.stringHandler ("WARNING")) ;

	Arguments args ;
	try {
	    args = parser.parse (argsArray) ;

	} catch (ArgumentException exception) {
	    usage (exception.toString ()) ;
	    return null ;
	}

	// Set the logging level from optional --logging argument:
	//
	String level = (String) args.getOption ("logging") ;
	try {
	    LOGGER.setLevel (Level.parse (level.toUpperCase ())) ;
	} catch (IllegalArgumentException exception) {
	    usage ("\"" + level + "\" is an invalid logging level") ;
	}

	return args ;
    }

    /**
     * Converts a {@link List} of equation(s) file names to a {@link List} of
     * {@link Collective} objects.
     **/
    final private static Set equations2expressions (List files)
    throws AlgebraException, EquationException, IOException {

	// Create a factory to handle input equation files:
	//
	AlgebraFactory factory = null ;
	try {
	    factory = Main.createFactory (Main.UNIT_FACTORY) ;
	} catch (Exception exception) {
	    abort ("can't create unit factory: ", exception) ;
	}

	// Each input file is an equation(s) file.  Convert each to an
	// assignment (an {@link Expression} with a target):
	//
	List targets = new ArrayList (files.size ()) ;
	for (Iterator p = files.iterator () ; p.hasNext () ; ) {

	    // An equation(s) file is itself an algebraic {@link Unit}, so
	    // convert it to one:
	    //
	    String fileName = (String) p.next () ;
	    Unit unit = factory.getUnit (new File (fileName)) ;

	    // Read the operands from the equation(s) file and resolve them to
	    // {@link File} objects relative to the model path:
	    //
	    List operands = null ;
	    if (unit instanceof EquationFileUnit)
		operands = ((EquationFileUnit) unit) . readEquation () ;
	    else if (unit instanceof EquationsFileUnit)
		operands = ((EquationsFileUnit) unit) . readEquation () ;
	    else
		abort ("invalid equation(s) file: " + fileName) ;

	    if (operands.size () < 1) {
		LOGGER.warning (
		    "ignoring empty equation(s) file \""
		    + fileName
		    + '"'
		) ;
		continue ;
	    }

	    operands = Main.operandsResolve (operands) ;

	    File lastOperand = (File) operands.get (operands.size () - 1) ;

	    // Convert each operand {@link File} objects to a {@link Unit}.
	    // Then, compose them via a composition {@link Function}:
	    //
	    for (ListIterator q = operands.listIterator () ; q.hasNext () ; )
		q.set (factory.getUnit ((File) q.next ())) ;
	    Function composition = factory.getFunction ("compose", operands) ;

	    Class parent = composition.getClass().getSuperclass () ;
	    if (! FileUnitComposeFunction.class.isAssignableFrom (parent)) {
		LOGGER.info ("composition type unknown: \"" + fileName + '"') ;
		continue ;
	    }

	    // Build the target name from the base name of the equation(s)
	    // {@link File} and, when appropriate, an extension from the
	    // operands:
	    //
	    int dot = fileName.lastIndexOf ('.') ;
	    String target =
		(dot > 0)
		? fileName.substring (0, dot)
		: fileName ;

	    if (! (composition instanceof Collective)) {
		String extension = lastOperand.getName () ;
		dot = extension.lastIndexOf ('.') ;
		target += '.' + extension.substring (1 + dot) ;
	    } else if (! target.endsWith ("/"))
		target += "/" ;

	    // Reduce the composition to an optimized {@link Expression} and
	    // assign it to the target name:
	    //
	    Expression expression = toExpression (composition, target) ;

	    if (expression != null) {
		expression.setTarget (target) ;
		targets.add (expression) ;
	    }
	}

	// Return a {@link Set} containing all the sub-expressions:
	//
	return Expression.getExpressions () ;
    }

    final private static void print (String message) {
	System.out.print (message) ;
    }

    final private static void println (String message) {
	System.out.println (message) ;
    }

    final private static void println () {
	System.out.println () ;
    }

    final private static Expression toExpression
    (Function function, String target)
    throws AlgebraException {

	if (! (function instanceof FileUnitComposeFunction))
	    throw new IllegalStateException ("not a FileUnitComposeFunction") ;

	if (function instanceof Collective) {
	    Collective collective = (Collective) function ;
	    Set signatures = collective.getSignatures () ;
	    for (Iterator p = signatures.iterator () ; p.hasNext () ; ) {
		Signature signature = (Signature) p.next () ;
		Unit unit = collective.getUnit (signature) ;
		String subTarget =
		    target
		    + signature
		    + ((unit instanceof Collective) ? "/" : "") ;
		Expression expr = toExpression ((Function) unit, subTarget) ;
		if (expr != null)
		    expr.setTarget (subTarget) ;
	    }
	}

	FileUnitComposeFunction fn = (FileUnitComposeFunction) function ;

	Class parent = fn.getClass().getSuperclass () ;
	if (! FileUnitComposeFunction.class.isAssignableFrom (parent)) {
	    LOGGER.info (
		"composition type unknown: \""
		+ fn.getSources ()
		+ '"'
	    ) ;
	    return null ;
	}

	Iterator p = fn.getSources().iterator () ;
	File operand = ((FileUnit) p.next ()) . getFile () ;
	Expression expr = Expression.getInstance (operand) ;
	while (p.hasNext ()) {
	    operand = ((FileUnit) p.next ()) . getFile () ;
	    expr = Expression.getInstance (expr, operand) ;
	}

	return expr ;
    }

    final private static void usage (String message) {
	usage_print () ;
	println () ;
	LOGGER.log (Level.SEVERE, message) ;
	exit (0) ;
    }

    final private static void usage () {
	usage_print () ;
	exit (0) ;
    }

    final private static void usage_print () {

	println ("JTS " + NAME + ' ' + VERSION + ' ' + COPYRIGHT) ;
	println () ;
	println ("Usage: " + NAME + " [<option> ...] <equation-file> ...") ;

	Main.usage_paragraph (
	    "where an <equation-file> is any existing equation(s) file and"
	    + " <option> may be any of the following:"
	) ;
	
	Main.usage_option (
	    "--help",
	    "Prints this helpful message, then exits."
	) ;

	Main.usage_option (
	    "--logging=<level> (default level is \"warning\")",
	    "Selects how much detail to report during execution.  As per"
	    + " java.util.logging (see method \"Level.parse\"), <level> is"
	    + " one of \"off\", \"severe\", \"warning\", \"info\", \"fine\""
	    + " or \"all\".  Other values are specified in java.util.logging,"
	    + " but are not meaningful in this program."
	) ;
    }

    final private static Handler USAGE_HANDLER = new Handler () {

	final public Object getValue () {
	    return this ;
	}

	final public Object handle (String argument) {
	    usage () ;
	    return argument ;
	}

	final public boolean requiresValue () {
	    return false ;
	}

    } ;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private static class MapCollective implements Collective {

	final public AlgebraFactory getFactory () {
	    return null ;
	}

	final public Type getType () {
	    return null ;
	}

	final public Set getSignatures () {
	    return map.keySet () ;
	}

	final public Unit getUnit (Signature signature) {
	    return (Unit) map.get (signature) ;
	}

	final private String normalize (String fileName) {
	    return
		(File.separatorChar != '/')
		? fileName.replace (File.separatorChar, '/')
		: fileName ;
	}

	void put (String signature, Unit unit) {
	    Checks.nonNull (signature, "signature") ;
	    Checks.nonNull (unit, "unit") ;
	    map.put (normalize (signature), unit) ;
	}

	final public int size () {
	    return map.size () ;
	}

	final private Map map = new TreeMap () ;

    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private static Comparator ALPHABETIC_COMPARATOR = new Comparator () {

	final public int compare (Object l, Object r) {

	    String ls = l.toString () ;
	    String rs = r.toString () ;
	    int order = ls.compareToIgnoreCase (rs) ;
	    if (order != 0)
		return order ;

	    order = ls.compareTo (rs) ;
	    if (order != 0)
		return order ;

	    return System.identityHashCode (l) - System.identityHashCode (r) ;
	}

	final public boolean equals (Object that) {
	    return (this == that) ;
	}

    } ;

}
