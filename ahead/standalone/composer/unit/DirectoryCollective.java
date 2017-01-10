package composer.unit ;

import composer.algebra.AlgebraException ;
import composer.algebra.Collective ;
import composer.algebra.Signature ;
import composer.algebra.Function ;
import composer.algebra.Type ;
import composer.algebra.Unit ;

import composer.Checks ;
import composer.FileException ;

import java.io.File ;
import java.io.FilenameFilter ;

import java.util.AbstractList ;
import java.util.List ;
import java.util.Set ;

import java.util.logging.Logger ;

import java.util.regex.Pattern ;
import java.util.regex.PatternSyntaxException ;

final class DirectoryCollective extends FileUnit implements Collective {

    final public Set getSignatures () {
	return new Signatures (getFile().list (FILE_FILTER)) ;
    }

    final public Unit getUnit (Signature signature) throws AlgebraException {

	String name = ((StringSignature) signature) . toString () ;
	File file = new File (getFile (), name) ;

	return file.exists () ? getFactory().getUnit (file) : null ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    public static Function composeFunction (Factory factory, List sources) {
	return new DirectoryCollectiveComposeFunction (factory, sources) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    public DirectoryCollective (Factory factory, File file, Type type) {
	super (factory, file, type) ;

	try {
	    Checks.directoryFile (file) ;
	} catch (FileException exception) {
	    throw new IllegalArgumentException (exception.getMessage ()) ;
	}
    }

    private class Signatures extends AbstractList implements Set {

	Signatures (String[] fileNames) {
	    this.fileNames = fileNames ;
	}

	public Object get (int n) {
	    return new StringSignature (fileNames [n]) ;
	}

	public int size () {
	    return fileNames.length ;
	}

	final String[] fileNames ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private static Pattern pattern (String property) {

	try {
	    String value = System.getProperty(property).trim () ;
	    LOGGER.fine ("property \"" + property + "\" is \"" + value + '"') ;
	    return (value != null) ? Pattern.compile (value) : NONE ;

	} catch (PatternSyntaxException exception) {
	    LOGGER.warning (
		"property \""
		+ property
		+ "\" is invalid; it will be ignored"
	    ) ;
	}

	return NONE ;
    }

    final private static Logger LOGGER = Logger.getLogger ("composer") ;

    final private static Pattern NONE = Pattern.compile ("") ;
    final private static Pattern DPAT = pattern ("composer.directory.ignore") ;
    final private static Pattern FPAT = pattern ("composer.file.ignore") ;

    final private static FilenameFilter FILE_FILTER  = new FilenameFilter () {

	public boolean accept (File directory, String fileName) {
	    File file = new File (directory, fileName) ;
	    Pattern pattern = file.isDirectory () ? DPAT : FPAT ;
	    return ! pattern.matcher(fileName).matches () ;
	}

    } ;

}
