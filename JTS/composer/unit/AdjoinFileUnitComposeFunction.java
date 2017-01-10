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

class AdjoinFileUnitComposeFunction extends FileUnitComposeFunction {

    public AdjoinFileUnitComposeFunction (Factory f, List s, Class c) {
	super (f, s, c) ;
    }

    public void invoke (Unit targetUnit) throws AlgebraException {
	writeEquation (((FileUnit) targetUnit) . getFile ()) ;
	adjoinUnits (getSources (), targetUnit) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    AdjoinFileUnitComposeFunction (Factory factory, List sources) {
	this (factory, sources, AdjoinFileUnit.class) ;
    }

    final protected void adjoinFiles (List sources, File target)
    throws IOException {

	getLogger().info (filesComposed ("adjoin", sources, target)) ;

        BufferedInputStream inp =
            new BufferedInputStream (filesInputStream (sources)) ;

	backupFile (target) ;

        BufferedOutputStream out =
            new BufferedOutputStream (new FileOutputStream (target)) ;

        copyStream (inp, out) ;

        out.close () ;
        inp.close () ;
    }

    final protected void adjoinUnits (List sourceUnits, Unit targetUnit)
    throws AlgebraException {

	File target = ((FileUnit) targetUnit) . getFile () ;

	List sources = new ArrayList (sourceUnits.size ()) ;
	for (Iterator p = sourceUnits.iterator () ; p.hasNext () ; )
	    sources.add (((FileUnit) p.next ()) . getFile ()) ;

	try {
	    adjoinFiles (sources, target) ;
	} catch (IOException exception) {
	    throw new AlgebraException (
		fileMessage (target, "not adjoined from sources"),
		exception
	    ) ;
	}
    }

    final protected SequenceInputStream filesInputStream (List sources)
    throws FileNotFoundException {

        List streams = new ArrayList (sources.size ()) ;
        for (Iterator p = sources.iterator() ; p.hasNext() ; )
            streams.add ( new FileInputStream ((File) p.next ()) ) ;

        return new SequenceInputStream (Collections.enumeration (streams)) ;
    }

}
