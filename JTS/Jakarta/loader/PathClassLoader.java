package Jakarta.loader ;

import Jakarta.string.Strings ;

import Jakarta.util.Debug ;
import Jakarta.util.LineWriter ;
import Jakarta.util.Util ;

import java.io.File ;
import java.io.IOException ;

import java.net.MalformedURLException ;
import java.net.URL ;
import java.net.URLClassLoader ;

import java.util.Enumeration ;
import java.util.Iterator ;
import java.util.List ;

/**
 * This class loader extends {@link URLClassLoader} by allowing a resource
 * to be named with a name <em>local</em> to this class loader.
 *
 * Additional convenience methods are provided to locate writable directories
 * in the host system via a search path.
 **/
public class PathClassLoader extends AbstractClassLoader {

    public PathClassLoader (URL[] searchPath, ClassLoader parent) {
	super (searchPath, parent) ;
	debug.println ("constructed " + toString()) ;
    }

    public PathClassLoader (String searchPath, ClassLoader parent) {
	this (new URL[0], parent) ;
	addSearchPath (searchPath) ;
    }

    public PathClassLoader (String searchPath) {
	this (searchPath, ClassLoader.getSystemClassLoader()) ;
    }

    /**
     * Adds a {@link File} to the search path for classes and resources.
     * 
     * This method is shorthand for <code>addSearch(file.toURL())</code>.  The
     * <code>File</code> argument is converted to a {@link URL}, then appended
     * to the search path of URLs as per {@link URLClassLoader#addURL(URL)}.
     * In particular, if the argument is a directory, a slash character
     * (<code>/</code>) will be appended.
     **/
    protected void addSearch (File file) throws MalformedURLException {
	debug.println ("add search file " + file) ;
	addSearch (file.getAbsoluteFile().toURI().toURL ()) ;
    }

    /**
     * Adds an element to the search path for classes and resources.
     *
     * This method accepts a {@link String} as an argument.  The argument is
     * checked to determine if it specifies a URL or a local file.  A file
     * argument is first converted to a URL, then appended to the search path,
     * while a URL is immediately appended.
     **/
    protected void addSearch (String element) throws MalformedURLException {
	String slashForm = element.replace (File.separatorChar, '/') ;

	if (! slashForm.equals (element))

	    addSearch (new File (element)) ;

	else {

	    URL url = new URL (defaultContext, element) ;

	    if (! url.getProtocol().equals (fileContext.getProtocol()))
		addSearch (url) ;
	    else if (! url.getHost().equals (fileContext.getHost()))
		addSearch (url) ;
	    else {
		String file = element.replace ('/', File.separatorChar) ;
		addSearch (new File (file)) ;
	    }
	}
    }

    /**
     * Adds a URL to the search path for classes and resources.
     *
     * This method is equivalent to {@link URLClassLoader#addURL(URL)}.
     **/
    protected void addSearch (URL url) {
	debug.println ("add search URL " + url) ;
	addURL (url) ;
    }

    /**
     * Parses an external search path, adding elements to the search list.
     **/
    public void addSearchPath (String searchPath) {
	debug.println ("add search path " + searchPath) ;

	List elements = Strings.split (searchPath, ";") ;
	for (Iterator p = elements.iterator() ; p.hasNext() ; )
	    try {
		addSearch ((String) p.next()) ;
	    } catch (MalformedURLException except) {
		// Ignore these.
		debug.println ("MalformedURLException " + except) ;
	    }

	return ;
    }

    public List listResources (String name, List result, int limit)
    throws IOException {
	debug.println ("list resources for " + name) ;
	super.listResources (name, result, limit) ;
	return result ;
    }

    public String toString () {
	if (null == toStringValue) {
	    toStringValue = getClass() . getName() + " (" ;
	    toStringValue = toStringValue + getURLs() ;

	    String parent = ClassLoaders.toString (getParent()) ;
	    toStringValue = toStringValue + ", " + parent + ")" ;
	}
	return toStringValue ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    protected String toStringValue = null ;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    static protected URL defaultContext = null ;

    static {
        try {
	    defaultContext = new URL ("file", "", "") ;
	} catch (MalformedURLException except) {
	    throw new IllegalStateException ("search context -- " + except) ;
	}
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    static private LineWriter
	debug = Debug.global.getWriter ("debug.pathclassloader") ;

}
