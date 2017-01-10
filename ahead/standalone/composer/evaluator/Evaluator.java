package composer.evaluator ;

import composer.Checks ;
import composer.args.PatternFilter ;
import composer.unit.EquationsFileUnit ;

import java.io.File ;
import java.io.IOException ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.HashMap ;
import java.util.HashSet ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Map ;
import java.util.Properties ;
import java.util.Set ;

import java.util.regex.Pattern ;

final public class Evaluator {

    public Evaluator (final File equationsFile, final PatternFilter filter)
    throws IOException {
	this (EquationsFileUnit.loadProperties (equationsFile), filter) ;
    }

    Evaluator (final Map inp, final PatternFilter filter)
    throws IOException {

	Checks.nonNullArgument (filter, "filter") ;
	Checks.nonNullArgument (inp, "inp") ;
    
	Map out = new HashMap () ;
	results = new ArrayList () ;

	Set set = new HashSet (inp.keySet ()) ;
	for (Iterator p = set.iterator() ; p.hasNext () ; ) {
	    String key = (String) p.next () ;
	    if (filter.accept (key)) {
		Function fun = null ;
		if (inp.containsKey (key))
		    fun = recursiveMove (key, inp, out, new HashSet()) ;
		else {
		    fun = (Function) out.get (key) ;
		    fun.incrementReferences () ;
		}
		results.add (fun) ;
	    }
	}

	((ArrayList) results) . trimToSize () ;
    }

    final List getResults () {
	return Collections.unmodifiableList (results) ;
    }

    final private static Function recursiveMove
    (final String key, final Map source, final Map target, final Set visited)
    throws IOException {

	if (visited.contains (key))
	    throw new IllegalStateException ("cycle found: \"" + key + '"') ;

	String[] arguments = SPACES.split ((String) source.remove (key)) ;
	Function[] operands = new Function [arguments.length] ;

	CompositionFunction result = new CompositionFunction (key, arguments) ;
	target.put (key, result) ;

	visited.add (key) ;
	for (int n = arguments.length ; --n >= 0 ; ) {
	    String arg = arguments [n] ;
	    if (source.containsKey (arg)) {
		operands [n] = recursiveMove (arg, source, target, visited) ;
	    } else if (target.containsKey (arg)) {
		operands [n] = (Function) target.get (arg) ;
		operands [n] . incrementReferences () ;
	    } else {
		operands [n] = new SourceFunction (arg) ;
		target.put (arg, operands [n]) ;
	    }
	}
	visited.remove (key) ;

	for (int n = operands.length ; --n >= 0 ; )
	    if (operands [n] instanceof CompositionFunction) {
		CompositionFunction fun = (CompositionFunction) operands [n] ;
		if (fun.size () == 1)
		    operands [n] = (Function) fun.getOperands().get (0) ;
	    }

	result.setOperands (operands) ;
	return result ;
    }

    final private List results ;

    final private static Pattern SPACES = Pattern.compile ("\\s+") ;

}

