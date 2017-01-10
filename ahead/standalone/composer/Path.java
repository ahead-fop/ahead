package composer ;

import java.io.File ;

import java.net.URI ;
import java.net.URISyntaxException ;

import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.Iterator ;
import java.util.List ;

import java.util.logging.Logger ;

import java.util.regex.Pattern ;

/**
 * Provides methods to search for files within a search path of directories.
 **/
final public class Path {

    public Path (String path) {
	this (PATTERN.split (path)) ;
    }

    public Path (String[] fileNames) {

	List d = new ArrayList (fileNames.length) ;
	for (int n = 0 ; n < fileNames.length ; ++n) {
	    File file = new File (fileNames [n]) ;
	    if (file.isDirectory ())
		d.add (IO.getCanonicalFile (file)) ;
	}

	this.directories = Arrays.asList (d.toArray (new File [d.size ()])) ;
    }

    /**
     * Returns a singleton instance of <code>Path</code> constructed from the
     * property, <code>composer.model.path</code>.
     **/
    final public static Path getInstance () {

	if (INSTANCE == null) {
	    String path = System.getProperty ("composer.model.path") ;
	    LOGGER.fine ("composer.model.path is \"" + path + '"') ;
	    INSTANCE = new Path ( (path != null) ? path.trim () : "." ) ;
	}

	return INSTANCE ;
    }

    /**
     * Returns the directory where the last file was matched, <code>null</code>
     * if no match has yet occurred.  The non-matching case can occur either
     * because no file has yet been matched <em>or</em> because all files
     * have been absolute.
     **/
    final public File getMatchedDirectory () {
	return matchedDirectory ;
    }

    /**
     * Resolves a {@link File} against this search path.
     *
     * @return
     * a {@link File} if <code>file</code> is absolute or found via the search
     * path, <code>null</code> otherwise.
     **/
    final public File resolve (File file) {

	if (file.isAbsolute ())
	    return file ;

	String path = file.getPath () ;
	for (Iterator p = directories.iterator () ; p.hasNext () ; ) {
	    File directory = (File) p.next () ;
	    File local = new File (directory, path) ;
	    if (local.exists ()) {
		matchedDirectory = directory ;
		return local ;
	    }
	}

	return null ;
    }

    /**
     * Resolves a {@link String} against this search path by, first, trying it
     * as a {@link URI}, then as a {@link File}.
     * 
     * @param name {@link String} to resolve against search path
     *
     * @see #resolve(File)
     * @see #resolve(URI)
     **/
    final public File resolve (String name) {

	Checks.nonNull (name, "name") ;

	try {
	    return resolve (new URI (name)) ;
	} catch (URISyntaxException exception) {
	    // Fall through to default behavior below.
	}

	return resolve (new File (name)) ;
    }

    /**
     * Resolves a {@link URI} against this search path.  If the scheme is
     * specified, it must be "file" else a "file" scheme is assumed.
     *
     * @return
     * a {@link File} if <code>uri</code> is absolute or found via the search
     * path, <code>null</code> otherwise.
     **/
    final public File resolve (URI uri) {

	if (uri.isAbsolute () && ! uri.isOpaque ())
	    return resolve (new File (uri)) ;

	String scheme = uri.getScheme () ;
	if (scheme != null && ! scheme.equals ("file"))
	    throw new IllegalArgumentException (
		"URI scheme not supported; URI=\"" + uri + '"'
	    ) ;

	String path = uri.getPath () ;
	if (File.separatorChar != '/')
	    path = path.replace ('/', File.separatorChar) ;

	return resolve (new File (path)) ;
    }

    /**
     * The list of directories to search for a file match:
     **/
    final private List directories ;

    /**
     * The last directory where a file was matched:
     **/
    private File matchedDirectory = null ;

    private static Path INSTANCE ;

    final private static Logger LOGGER = Logger.getLogger ("composer") ;

    final private static Pattern PATTERN =
        Pattern.compile ("\\" + File.pathSeparatorChar) ;

}
