package composer.evaluator ;

import composer.args.PatternFilter ;

import java.io.IOException ;

import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Properties ;

import junit.framework.TestCase ;

final public class EvaluatorTest extends TestCase {

    public EvaluatorTest (String testName) {
	super (testName) ;
    }

    final public void testSmokeTest () throws IOException {

	Properties source = new Properties () ;
	source.put ("a", "b") ;
	source.put ("b", "c") ;
	source.put ("c", "d") ;
	source.put ("x", "u a v") ;
	source.put ("y", "x a w") ;
	source.put ("z", "a x w y") ;

	PatternFilter filter = new PatternFilter ("[axy]") ;

	Evaluator eval = new Evaluator (source, filter) ;
	List list = eval.getResults () ;
	// System.out.println (list) ;
	assertEquals (3, list.size ()) ;
	CompositionFunction a = null, x = null, y = null ;
	for (Iterator p = list.iterator () ; p.hasNext () ; ) {
	    CompositionFunction fn = (CompositionFunction) p.next () ;
	    if (fn.getName().equals ("a"))
		a = fn ;
	    else if (fn.getName().equals ("x"))
		x = fn ;
	    else if (fn.getName().equals ("y"))
		y = fn ;
	}
	assertNotNull (a) ;
	assertNotNull (x) ;
	assertNotNull (y) ;

	checkOperands (a, new String[] {"d"}) ;
	checkOperands (x, new String[] {"u", "d", "v"}) ;
	checkOperands (y, new String[] {"x", "d", "w"}) ;

    }

    final private void checkOperands
    (final CompositionFunction fun, final String[] args) {

	List names = new ArrayList (args.length) ;
	for (Iterator p = fun.getOperands().iterator () ; p.hasNext () ; )
	    names.add ( ((Function) p.next ()) . getName () ) ;

	List list = Arrays.asList (args) ;
	assertEquals (list, names) ;

    }
}
