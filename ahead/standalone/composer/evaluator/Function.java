package composer.evaluator ;

import composer.Checks ;

import java.util.Arrays ;
import java.util.Collections ;
import java.util.List ;

abstract class Function {

    protected Function (final String name, final String[] arguments) {

	Checks.nonNullArray (arguments, "arguments") ;
	Checks.nonNullArgument (name, "name") ;

	this.arguments = new String [arguments.length] ;
	System.arraycopy (arguments, 0, this.arguments, 0, arguments.length) ;

	this.name = name ;
	this.references = 1 ;

    }

    protected Function (final String name) {
	this (name, new String [0]) ;
    }

    int decrementReferences () {
	return (-- references) ;
    }

    List getArguments () {
	return Collections.unmodifiableList (Arrays.asList (arguments)) ;
    }

    String getName () {
	return name ;
    }

    int getReferences () {
	return references ;
    }

    int incrementReferences () {
	return (++ references) ;
    }

    int size () {
	return arguments.length ;
    }

    final private String[] arguments ;
    final private String name ;
    private int references ;

}
