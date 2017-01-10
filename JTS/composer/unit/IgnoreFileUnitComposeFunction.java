package composer.unit ;

import composer.algebra.AlgebraException ;
import composer.algebra.Unit ;

import java.io.File ;
import java.io.IOException ;

import java.util.List ;

class IgnoreFileUnitComposeFunction extends FileUnitComposeFunction {

    public IgnoreFileUnitComposeFunction
    (Factory fact, List src, Class klass) {
	super (fact, src, klass) ;
    }

    public void invoke (Unit targetUnit) throws AlgebraException {
	File target = ((FileUnit) targetUnit) . getFile () ;
	getLogger().info (fileMessage (target, "ignored")) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    IgnoreFileUnitComposeFunction (Factory factory, List sources) {
	this (factory, sources, IgnoreFileUnit.class) ;
    }

}
