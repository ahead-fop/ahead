package composer.unit ;

import composer.algebra.AlgebraException ;
import composer.algebra.Unit ;

import java.io.File ;
import java.io.IOException ;

import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.List ;

import drc.Main ;

class DRCFileUnitComposeFunction extends FileUnitComposeFunction {

    public DRCFileUnitComposeFunction (Factory fact, List src, Class klass) {
	super (fact, src, klass) ;
    }

    public void invoke (Unit targetUnit) throws AlgebraException {

	File target = ((FileUnit) targetUnit) . getFile () ;
	writeEquation (target) ;

	List args = new ArrayList (6 + getSources().size ()) ;

	String aspect = aspectName (target) ;
	if (aspect != null) {
	    args.add ("-a") ;
	    args.add (aspect) ;
	} else {
	    String msg = fileMessage (target, "doesn't provide aspect name") ;
	    getLogger().warning (msg) ;
	}

	String model = ((Factory) getFactory ()) . getModelPath () ;
	if (model != null) {
	    args.add ("-model") ;
	    args.add (model) ;
	} else {
	    String msg = fileMessage (target, "invoked without model path") ;
	    getLogger().warning (msg) ;
	}

	args.add ("-f") ;
	args.add (target.getPath ()) ;

	for (Iterator p = getSources().iterator () ; p.hasNext() ; )
	    args.add (((FileUnit) p.next ()) . getFile() . getPath ()) ;

	StringBuffer buffer = new StringBuffer ("drc") ;
	for (Iterator p = args.iterator () ; p.hasNext () ; )
	    buffer.append(' ').append (p.next ()) ;
	getLogger().info (buffer.toString ()) ;

	try {
	    backupFile (target) ;
	    String[] argArray = new String [args.size()] ;
	    drc.Main.main ( (String[]) args.toArray (argArray) ) ;

	} catch (Exception except) {
	    throw new AlgebraException ("failed drc " + args,	except) ;
	}
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    DRCFileUnitComposeFunction (Factory factory, List sources) {
	this (factory, sources, DRCFileUnit.class) ;
    }

}
