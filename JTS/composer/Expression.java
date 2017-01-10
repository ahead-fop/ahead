package composer ;

import java.io.File ;
import java.io.PrintStream ;

import java.net.URI ;

import java.util.Arrays ;
import java.util.ArrayList ;
import java.util.Collections ;
import java.util.Comparator ;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Map ;
import java.util.Set ;

final class Expression implements Comparable {

    final public static Operation COMPOSE = new Operation ("COMPOSE") ;
    final public static Operation FILE = new Operation ("FILE") ;
    final public static Operation READ = new Operation ("READ") ;

    final public static Comparator IDENTITY_COMPARATOR = new Comparator () {

	final public int compare (Object l, Object r) {
	    return System.identityHashCode (l) - System.identityHashCode (r) ;
	}

	final public boolean equals (Object object) {
	    return (this == object) ;
	}

    } ;

    final public int compareTo (Expression that) {

	int order = this.references - that.references ;
	if (order != 0)
	    return -order ;

	if (this.target == null && that.target == null)
	    return this.hashCode () - that.hashCode () ;

	if (that.target == null)
	    return -1 ;

	if (this.target == null)
	    return +1 ;

	order = this.target.compareTo (that.target) ;
	if (order != 0)
	    return order ;

	return this.hashCode () - that.hashCode () ;
    }

    final public int compareTo (Object object) {
	return compareTo ((Expression) object) ;
    }

    final public String composition () {

	if (operation != COMPOSE)
	    return operands[0].toString () ;

	Expression lhs = (Expression) operands [0] ;
	Expression rhs = (Expression) operands [1] ;
	return
	    ((lhs.target != null) ? lhs.target : lhs.composition ())
	    + ' '
	    + rhs.composition () ;
    }

    final public static void dump (PrintStream out) {

	List list = new ArrayList (expressions.keySet ()) ;
	Collections.sort (list, IDENTITY_COMPARATOR) ;

	for (Iterator p = list.iterator () ; p.hasNext () ; ) {
	    Expression expr = (Expression) p.next () ;
	    out.print (
		dumpLabel (expr)
		+ ','
		+ expr.references
		+ ":  "
		+ expr.operation
	    ) ;
	    for (int n = 0 ; n < expr.operands.length ; ++n)
		out.print (" " + dumpOperand (expr.operands [n])) ;
	    if (expr.target != null)
		out.print (" -> " + expr.target) ;
	    out.println () ;
	}
    }

    final private static String dumpLabel (Object object) {
	int code = System.identityHashCode (object) ;
	String label = Integer.toHexString(code).toUpperCase () ;
	return
	    (label.length () < 8)
	    ? "00000000".substring (label.length ()) + label
	    : label ;
    }

    final private static String dumpOperand (Object object) {
	return
	    (object instanceof Expression)
	    ? dumpLabel (object)
	    : object.toString () ;
    }

    final public boolean equals (Expression that) {
	return
	    this == that
	    || (this.operation == that.operation
		&& Arrays.equals (this.operands, that.operands)) ;
    }

    final public boolean equals (Object that) {
	return equals ((Expression) that) ;
    }

    final public int hashCode () {
	return
	    31 * operation.hashCode ()
	    + Arrays.asList(operands).hashCode () ;
    }

    final public static Set getExpressions () {
	return expressions.keySet () ;
    }

    final public static Expression getInstance (File file) {
	Checks.nonNull (file, "file") ;
	return getInstance (FILE, new File[] {file}) ;
    }

    final public static Expression getInstance
    (Expression prefix, File refinement) {
	Checks.nonNull (prefix, "prefix") ;
	Checks.nonNull (refinement, "refinement") ;
	return getInstance (
	    COMPOSE,
	    new Expression[] {prefix, getInstance (refinement)}
	) ;
    }

    final public static Expression getInstance
    (Operation operation, Object[] operands) {

	Checks.nonNull (operation, "operation") ;
	Checks.nonNull (operands, "operands") ;

	Expression key = new Expression (operation, operands) ;
	Expression instance = (Expression) expressions.get (key) ;
	if (instance != null)
	    return instance ;

	expressions.put (key, key) ;
	return key ;
    }

    final public static void clearReferences () {
	for (Iterator p = expressions.keySet().iterator () ; p.hasNext () ; )
	    ((Expression) p.next ()) . references = 0 ;
    }

    final public int getReferences () {
	return references ;
    }

    final public static void updateReferences (Expression source) {

	Checks.nonNull (source, "source") ;

	source.references += 1 ;
	for (int n = source.operands.length ; --n >= 0 ; )
	    if (source.operands [n] instanceof Expression)
		updateReferences ((Expression) source.operands [n]) ;
    }

    final public String getTarget () {
	return target ;
    }

    final public void setTarget (String target) {
	this.target = target ;
    }

    final public int intermediaries (String path) {

	Checks.nonNull (path, "path") ;

	if (operation != COMPOSE)
	    return 0 ;

	Expression prefix = (Expression) operands [0] ;
	int level = prefix.intermediaries (path) ;

	if (prefix.references <= references)
	    return level ;

	if (prefix.target != null)
	    return level ;

	prefix.setTarget ("tmp" + level + ':' + path) ;

	return 1 + level ;
    }

    final public void relativize (URI uri) {

	Checks.nonNull (uri, "uri") ;

	if (operation != FILE)
	    return ;

	File file = (File) operands [0] ;
	String base = uri.toString () ;
	String path = file.toURI().toString () ;
	int minSize = Math.min (base.length (), path.length ()) ;

	int len = 0 ;
	while (len < minSize && base.charAt (len) == path.charAt (len))
	    ++ len ;

	String name = path.substring (1 + path.lastIndexOf ('/', len)) ;

	operands = new String[] {name} ;
	operation = READ ;
    }

    final public String toString () {
	return
	    (target != null ? target + " = " : "")
	    + references
	    + '#'
	    + operation
	    + ' '
	    + Arrays.asList (operands) ;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    private Expression (Operation operation, Object[] operands) {
	this.operation = operation ;
	this.operands = operands ;
	this.references = 0 ;
	this.target = null ;
    }

    private Operation operation ;
    private Object[] operands ;

    private int references ;
    private String target ;

    final private static Map expressions = new HashMap () ;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    private static class Operation {

	private Operation (String name) {
	    this.name = name ;
	}

	final public String toString () {
	    return name ;
	}

	final private String name ;

    }

}
