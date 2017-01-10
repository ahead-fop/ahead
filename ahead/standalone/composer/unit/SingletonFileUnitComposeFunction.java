package composer.unit ;

import composer.algebra.AlgebraException ;
import composer.algebra.Unit ;

import java.io.File ;

import java.util.List ;

final class SingletonFileUnitComposeFunction
extends LastFileUnitComposeFunction {

    public SingletonFileUnitComposeFunction (Factory f, List s, Class c) {
	super (f, s, c) ;
    }

    public void invoke (Unit target) throws AlgebraException {

	if (getSources().size () > 1)
	    throw new AlgebraException (
		"unit \"" + target + "\" not a singleton"
	    ) ;

	super.invoke (target) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    SingletonFileUnitComposeFunction (Factory factory, List sources) {
	this (factory, sources, SingletonFileUnit.class) ;
    }

}
