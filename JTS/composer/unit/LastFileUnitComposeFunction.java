package composer.unit ;

import composer.algebra.AlgebraException ;
import composer.algebra.Unit ;

import java.io.File ;
import java.io.IOException ;

import java.util.List ;

class LastFileUnitComposeFunction extends FileUnitComposeFunction {

    public LastFileUnitComposeFunction (Factory fact, List src, Class klass) {
	super (fact, src, klass) ;
    }

    public void invoke (Unit targetUnit) throws AlgebraException {

	Object sourceUnit = getSources().get (getSources().size () - 1) ;

	File source = ((FileUnit) sourceUnit) . getFile () ;
	File target = ((FileUnit) targetUnit) . getFile () ;
	writeEquation (target) ;

	try {
	    copyFile (source, target) ;
	} catch (IOException exception) {
	    throw new AlgebraException (
		fileMotion ("failed to copy", source, target),
		exception
	    );
	}
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    LastFileUnitComposeFunction (Factory factory, List sources) {
	this (factory, sources, LastFileUnit.class) ;
    }

}
