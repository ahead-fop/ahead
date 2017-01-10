package composer.unit ;

import composer.algebra.AlgebraException ;
import composer.algebra.Unit ;

import java.io.BufferedInputStream ;
import java.io.BufferedOutputStream ;
import java.io.File ;
import java.io.FileInputStream ;
import java.io.FileOutputStream ;
import java.io.InputStream ;
import java.io.IOException ;
import java.io.OutputStream ;

import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Properties ;

class PropertiesFileUnitComposeFunction extends FileUnitComposeFunction {

    public PropertiesFileUnitComposeFunction
    (Factory factory, List source, Class klass) {
	super (factory, source, klass) ;
    }

    public void invoke (Unit targetUnit) throws AlgebraException {

	File target = ((FileUnit) targetUnit) . getFile () ;
	writeEquation (target) ;

	List sources = new ArrayList (getSources().size ()) ;
	for (Iterator p = getSources().iterator () ; p.hasNext () ; )
	    sources.add (((FileUnit) p.next ()) . getFile ()) ;

	try {
	    getLogger().info (filesComposed ("properties", sources, target)) ;
	    composeProperties (sources, target) ;

	} catch (IOException except) {
	    throw new AlgebraException (
		fileMessage (target, "can't create properties file"),
		except
	    ) ;
	}
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    PropertiesFileUnitComposeFunction (Factory factory, List sources) {
	this (factory, sources, PropertiesFileUnit.class) ;
    }

    final private static void composeProperties (List sources, File target)
    throws IOException {

	// Just copy the properties file when there's just one:
	// (a convenience with the advantage that comments are preserved)
	//
	if (sources.size() == 1) {
	    copyFile ((File) sources.get (0), target) ;
	    return ;
	}

	// Otherwise, compose multiple property files by loading them in order:
	//
	Properties composition = new Properties () ;
	for (Iterator p = sources.iterator() ; p.hasNext() ; ) {

	    File file = (File) p.next () ;

	    InputStream input = null ;
	    try {
		input = new FileInputStream (file) ;
		input = new BufferedInputStream (input) ;
		composition.load (input) ;

	    } finally {
		if (input != null)
		    input.close () ;
	    }
	}

	backupFile (target) ;

	// Write the composed properties map to the target file:
	//
	OutputStream output = null ;
	try {
	    output = new FileOutputStream (target) ;
	    output = new BufferedOutputStream (output) ;
	    composition.store (output, "Generated from " + sources) ;

	} finally {
	    if (output != null)
		output.close () ;
	}
    }
}
