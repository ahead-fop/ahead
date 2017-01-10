package composer.unit ;

import composer.algebra.AlgebraException ;
import composer.algebra.Unit ;

import java.io.BufferedInputStream ;
import java.io.BufferedOutputStream ;
import java.io.File ;
import java.io.FileInputStream ;
import java.io.FileNotFoundException ;
import java.io.FileOutputStream ;
import java.io.InputStream ;
import java.io.IOException ;
import java.io.OutputStream ;
import java.io.SequenceInputStream ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.Iterator ;
import java.util.List ;

class ReverseAdjoinFileUnitComposeFunction
extends AdjoinFileUnitComposeFunction {

    public ReverseAdjoinFileUnitComposeFunction
    (Factory factory, List sources, Class klass) {
	super (factory, sources, klass) ;
    }

    public void invoke (Unit targetUnit) throws AlgebraException {
	writeEquation (((FileUnit) targetUnit) . getFile ()) ;
	List reverseSources = new ArrayList (getSources ()) ;
	Collections.reverse (reverseSources) ;
	adjoinUnits (reverseSources, targetUnit) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    ReverseAdjoinFileUnitComposeFunction (Factory factory, List sources) {
	this (factory, sources, ReverseAdjoinFileUnit.class) ;
    }

}
