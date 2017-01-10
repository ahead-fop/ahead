package composer.evaluator ;

import composer.Checks ;

import java.util.Arrays ;
import java.util.Collections ;
import java.util.List ;

final class CompositionFunction extends Function {

    CompositionFunction (final String lhs, final String[] arguments) {
	super (lhs, arguments) ;
	this.lhs = lhs ;
    }

    List getOperands () {
	return Collections.unmodifiableList (Arrays.asList (operands)) ;
    }

    final void setOperands (Function[] operands) {

	Checks.nonNullArray (operands, "operands") ;

	if (operands.length != size ())
	    throw new IllegalArgumentException ("operands.length != size()") ;

	this.operands = operands ;
    }

    final public String toString () {
	StringBuffer buffer = new StringBuffer () ;
	buffer.append (lhs) ;
	buffer.append (" <- CompositionFunction(") ;

	if (operands.length > 0) {
	    buffer.append (operands [0]) ;
	    for (int n = 1 ; n < operands.length ; ++n) {
		buffer.append (", ") ;
		buffer.append (operands [n]) ;
	    }
	}

	buffer.append (')') ;
	return buffer.toString () ;
    }

    final private String lhs ;
    private Function[] operands ;

}
