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
import java.util.Arrays ;
import java.util.Iterator ;
import java.util.List ;
import java.util.ListIterator ;
import java.util.Map ;
import java.util.Properties ;

class EquationsFileUnitComposeFunction
extends PropertiesFileUnitComposeFunction {

    public EquationsFileUnitComposeFunction
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
	    getLogger().info (filesComposed ("equations", sources, target)) ;
	    composeEquations (sources, target) ;

	} catch (IOException except) {
	    throw new AlgebraException (
		fileMessage (
		    target,
		    "can't be created -- " + except.getMessage ()
		),
		except
	    ) ;
	}
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    EquationsFileUnitComposeFunction (Factory factory, List sources) {
	this (factory, sources, EquationsFileUnit.class) ;
    }

    final private void composeEquations (List sources, File target)
    throws AlgebraException, IOException {

	String keyIfNoThis = target.getName () ;

	// Compose multiple equations files by loading them in order:
	//
	Properties composition = new Properties () ;
	for (Iterator p = sources.iterator() ; p.hasNext() ; ) {

	    File file = (File) p.next () ;

	    InputStream input = null ;
	    try {
		input = new BufferedInputStream (new FileInputStream (file)) ;

		Properties properties = new Properties () ;
		properties.load (input) ;

		try {
		    superSubstitute (properties, composition, keyIfNoThis) ;

		} catch (EquationException except) {
		    throw new AlgebraException (
			fileMessage (target, "-- " + except.getMessage ()),
			except
		    ) ;
		}

		composition.putAll (properties) ;

	    } finally {
		if (input != null)
		    input.close () ;
	    }
	}

	backupFile (target) ;

	// Write the composed equations map to the target file:
	//
	OutputStream output = null ;
	try {
	    output = new BufferedOutputStream (new FileOutputStream (target)) ;
	    composition.store (output, "Generated from " + sources) ;

	} finally {
	    if (output != null)
		output.close () ;
	}
    }

    final private static String joinStrings (List strings, String separator) {

	Iterator p = strings.iterator () ;
	if (! p.hasNext ())
	    return "" ;

	StringBuffer buffer = new StringBuffer (p.next().toString ()) ;
	while (p.hasNext ())
	    buffer.append(separator).append (p.next().toString ()) ;

	return buffer.toString () ;
    }

    final private static void superSubstitute
    (final Map target, final Map source, final String keyThis)
    throws EquationException {

	for (Iterator p = target.entrySet().iterator () ; p.hasNext () ; ) {
	    Map.Entry entry = (Map.Entry) p.next () ;
	    String key = (String) entry.getKey () ;
	    String value = ((String) entry.getValue ()) . trim () ;
	    value = EquationsFileUnit.superSubstitute (value, source, keyThis);
	    target.put (key, value) ;
	}
    }

}
