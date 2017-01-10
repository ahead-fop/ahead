package composer.unit ;

import composer.algebra.AlgebraException ;
import composer.algebra.Collective ;
import composer.algebra.Function ;
import composer.algebra.Signature ;
import composer.algebra.Unit ;

import java.io.BufferedOutputStream ;
import java.io.File ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.PrintStream ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Set ;
import java.util.TreeSet ;

final class DirectoryCollectiveComposeFunction
extends FileUnitComposeFunction implements Collective {

    final public void invoke (Unit targetUnit) throws AlgebraException {

	File dir = ((FileUnit) targetUnit) . getFile () ;

	if (! makeDirectory (dir))
	    throw new AlgebraException (
		"directory \"" + dir + "\" can't be created"
	    ) ;

	this.dirBaseName = dir.getName () ;

	getLogger().fine (fileMessage (dir, "<- ") + getSources ()) ;

	int errors = 0 ;
	for (Iterator p = getSignatures().iterator () ; p.hasNext () ; ) {

	    Signature signature = (StringSignature) p.next () ;
	    Function compose = (Function) getUnit (signature) ;

	    if (! subInvoke (compose, new File (dir, signature.toString ())))
		++ errors ;
	}

	if (errors == 0 && ! timestamp (new File (dir, ".timestamp")))
	    ++ errors ;

	if (errors > 0)
	    throw new AlgebraException (
		fileMessage (dir, "build had " + errors + " errors")
	    ) ;
    }

    final public Set getSignatures () {
	return signatures ;
    }

    final public Unit getUnit (Signature signature) throws AlgebraException {

	List operands = new ArrayList () ;
	for (Iterator p = getSources().iterator () ; p.hasNext () ; ) {

	    Collective source = (Collective) p.next () ;
	    Unit operand = source.getUnit (signature) ;

	    if (operand != null)
		operands.add (operand) ;
	}

	return
	    operands.size () > 0
	    ? getFactory().getFunction ("compose", operands)
	    : null ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    DirectoryCollectiveComposeFunction (Factory factory, List sources) {

	super (factory, sources, DirectoryCollective.class) ;

	Set signatures = new TreeSet () ;
	for (Iterator p = getSources().iterator () ; p.hasNext () ; ) {
	    Collective collective = (Collective) p.next () ;
	    signatures.addAll (collective.getSignatures ()) ;
	}

	this.signatures = Collections.unmodifiableSet (signatures) ;
    }

    final private static boolean makeDirectory (File directory) {

	if (directory.isDirectory ()) {
	    getLogger().info (fileMessage (directory, "already exists")) ;
	    return true ;
	}

	backupFile (directory) ;

	if (! directory.exists ())
	    directory.mkdirs () ;

	if (directory.isDirectory ()) {
	    getLogger().info (fileMessage (directory, "created")) ;
	    return true ;
	}

	return false ;
    }

    final private boolean subInvoke (Function function, File target) {

	try {
	    function.invoke (getFactory().getUnit (target)) ;
	    return true ;

	} catch (AlgebraException exception) {
	    getLogger().severe (exception.getMessage ()) ;
	}

	return false ;
    }

    final private boolean timestamp (File target) {

	PrintStream output = null ;

	try {
	    backupFile (target) ;

	    FileOutputStream stream = new FileOutputStream (target) ;
	    BufferedOutputStream buffered = new BufferedOutputStream (stream) ;
	    output = new PrintStream (buffered) ;

	    for (Iterator p = getSignatures().iterator () ; p.hasNext () ; )
		output.println (p.next ()) ;

	    getLogger().fine (fileMessage (target, "created")) ;
	    return true ;

	} catch (IOException except) {
	    getLogger().warning ("failed timestamp; " + except.getMessage ()) ;

	} finally {
	    if (output != null)
		output.close () ;
	}

	return false ;
    }

    private String dirBaseName ;
    final private Set signatures ;

}
