package composer.unit ;

import balicomposer.Main ;

import composer.algebra.AlgebraException ;
import composer.algebra.Unit ;

import java.io.File ;
import java.io.IOException ;

import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.List ;

class BaliComposerFileUnitComposeFunction extends FileUnitComposeFunction {

    public BaliComposerFileUnitComposeFunction
    (Factory fact, List src, Class klass) {
	super (fact, src, klass) ;
    }

    public void invoke (Unit targetUnit) throws AlgebraException {

	File target = ((FileUnit) targetUnit) . getFile () ;
	writeEquation (target) ;

	List args = new ArrayList (2 + getSources().size ()) ;

	args.add ("-output") ;
	args.add (target.getPath ()) ;

	for (Iterator p = getSources().iterator () ; p.hasNext() ; )
	    args.add (((FileUnit) p.next ()) . getFile() . getPath ()) ;

	StringBuffer buffer = new StringBuffer ("balicomposer") ;
	for (Iterator p = args.iterator () ; p.hasNext () ; )
	    buffer.append(' ').append (p.next ()) ;
	getLogger().info (buffer.toString ()) ;

	try {
	    backupFile (target) ;
	    String[] argArray = new String [args.size()] ;
	    balicomposer.Main.main ( (String[]) args.toArray (argArray) ) ;

	} catch (Exception except) {
	    throw new AlgebraException ("failed balicomposer " + args, except);
	}
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    BaliComposerFileUnitComposeFunction (Factory factory, List sources) {
	this (factory, sources, BaliComposerFileUnit.class) ;
    }

}
