package composer.unit ;

import composer.algebra.AlgebraException ;
import composer.algebra.Unit ;

import java.util.List ;

final class TraceOnlyComposeFunction extends FileUnitComposeFunction {

    TraceOnlyComposeFunction (Factory f, List s, Class c) {
	super (f, s, c) ;
    }

    public void invoke (Unit target) throws AlgebraException {
	writeEquation (((FileUnit) target) . getFile ()) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    TraceOnlyComposeFunction (Factory factory, List sources) {
	this (factory, sources, FileUnit.class) ;
    }

}
