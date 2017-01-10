package Jakarta.loader ;

import Jakarta.util.Debug ;
import Jakarta.util.LineWriter ;

import java.io.File ;
import java.io.InputStream ;
import java.io.IOException ;

import java.net.MalformedURLException ;
import java.net.URL ;
import java.net.URLClassLoader ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.Enumeration ;
import java.util.Iterator ;
import java.util.List ;

import java.util.jar.Manifest ;

abstract public class AbstractClassLoader
extends URLClassLoader implements Loader {

    /**
     * Appends resources matching a given name, up to a limit, to a list.
     *
     * This is the only method that must be implemented in subclasses.  The
     * implementer should implement the resource search method in this method.
     * The order of the elements in the resulting list will be the order of
     * preference for {@link #findResource(String)}.
     *
     * @param name   Resource name for which to search.
     * @param result {@link List} to contain resources found.
     * @param limit  Maximum size of list to return.
     *
     * @return Resulting list (usually the one passed in <code>result</code>).
     **/
    public List listResources (String name, List result, int limit)
    throws IOException {
	if (result.size() < limit) {
	    Enumeration p = super.findResources (name) ;

	    while (p.hasMoreElements() && result.size() < limit)
		result.add (p.nextElement()) ;
	}
	return result ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    public AbstractClassLoader (URL[] urls, ClassLoader parent) {
	super (urls, parent) ;
    }

    public AbstractClassLoader (ClassLoader parent) {
	this (new URL[0], parent) ;
    }

    public AbstractClassLoader () {
	this (ClassLoader.getSystemClassLoader()) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    protected Class defineClassFromStream (String name, InputStream stream)
    throws IOException {

	int dataSize = 0 ;
	byte[] data = new byte [Math.max (stream.available(), 8192)] ;

	int lastRead = stream.read (data, dataSize, data.length - dataSize) ;

	while (lastRead >= 0) {
	    dataSize += lastRead ;

	    // Increase buffer size whenever remaining space is too small:
	    //
	    if (data.length - dataSize < Math.max (stream.available(), 1024)) {
		int growth = Math.max (stream.available(), dataSize) ;
		byte[] newData = new byte [data.length + growth] ;
		System.arraycopy (data, 0, newData, 0, dataSize) ;
		data = newData ;
	    }

	    lastRead = stream.read (data, dataSize, data.length - dataSize) ;
	}

	stream.close() ;

	Class newClass = defineClass (name, data, 0, dataSize) ;
	debug.println ("defined class " + newClass) ;
	return newClass ;
    }

    /**
     * Defines a package from information found via the current search scheme.
     *
     * If the package is already defined, no further action is taken.
     * Otherwise, the package name is converted to a resource name by changing
     * all "." characters to "/" and appending "/META-INF/MANIFEST.MF" (this
     * is analogous to the naming convention for "jar" archives).  The
     * resulting resource is found using {@link #findResource(String)}.  If
     * found and if it's a valid manifest, it is used to create a package
     * entry.
     **/
    protected Package definePackage (String name) {
	Package result = getPackage (name) ;

	if (null != result)
	    return result ;

	try {
	    String res = name.replace ('.', '/') + "/META-INF/MANIFEST.MF" ;
	    URL resURL = getResource (res) ;
	    InputStream stream = resURL.openStream () ;
	    Manifest manifest = new Manifest (stream) ;
	    stream.close() ;

	    result = definePackage (name, manifest, resURL) ;
	    debug.println ("package " + name + " -> " + result) ;

	} catch (IOException except) {
	    result = null ;
	    debug.println ("package " + name + " has no manifest") ;
	}

	return result ;
    }

    /**
     * Finds a class via the current search scheme and loads it.
     *
     * The class name is first converted to a resource name by replacing all
     * "." characters by "/" and appending ".class" (this is analogous to
     * the naming convention for class files on UNIX machines).  The resulting
     * resource name is found using {@link #findResource(String)}.  The data
     * bytes are read from the resource and converted to a class using
     * {@link #defineClass(String,byte[],int,int)}.
     **/
    protected Class findClass (String name) throws ClassNotFoundException {
	definePackage (name.substring (0, 1 + name.lastIndexOf ('.'))) ;

	try {
	    URL classURL = findResource (name.replace ('.', '/') + ".class") ;
	    return defineClassFromStream (name, classURL.openStream()) ;
	} catch (IOException except) {
	    throw new ClassNotFoundException (name, except) ;
	}
    }

    /**
     * Finds first resource with a particular name, over all search prefixes.
     **/
    public URL findResource (String name) {
	List resources = new ArrayList (2) ;

	try {
	    listResources (name, resources, 1) ;
	} catch (IOException except) {
	    // Ignore exception.
	}

	resources.add (null) ;
	
	URL resourceURL = (URL) resources.get (0) ;
	debug.println ("resource " + name + " -> " + resourceURL) ;

	return resourceURL ;
    }

    /**
     * Finds all resources with a particular name, over all search prefixes.
     **/
    public Enumeration findResources (String name) throws IOException {
	List resources = listResources (name) ;
	debug.println ("resources " + name + " == " + resources.size()) ;

	return Collections.enumeration (resources) ;
    }

    /**
     * Finds a directory matching the package name argument.
     *
     * The package name is converted to a resource name by converting all '.'
     * characters to '.' and finding that resource as a directory.
     **/
    public File getPackageDirectory (String packageName) throws IOException {
	return getResourceDirectory (packageName.replace ('.', '/')) ;
    }

    /**
     * Finds resource by a global name via standard delegation search or
     * by a local name via a search only in this {@link ClassLoader}.
     **/
    public URL getResource (String globalName, String localName) {
	URL result = getResource (globalName) ;
	return (null != result) ? result : findResource (localName) ;
    }

    /**
     * Finds a directory matching the resource name argument.
     **/
    public File getResourceDirectory (String resource) throws IOException {

	for (Enumeration p = getResources(resource) ; p.hasMoreElements() ; ) {
	    URL resourceURL = new URL ( (URL) p.nextElement () , resource ) ;
	    File file = URLToFile (resourceURL) ;
	    debug.println ("directory? " + file) ;
	    if (null != file && file.isDirectory())
		return file ;
	}

	return null ;
    }

    /**
     * Finds all resources by a global name via standard delegation search or
     * by a local name via a search only in this {@link ClassLoader}.
     **/
    public List getResources (String globalName, String localName)
    throws IOException {

	List list = new ArrayList () ;
	
	for (Enumeration p = getResources(globalName) ; p.hasMoreElements() ; )
	    list.add (p.nextElement()) ;

	for (Enumeration p = findResources(localName) ; p.hasMoreElements() ; )
	    list.add (p.nextElement()) ;

	return list ;
    }

    /**
     * Appends all resources matching a given name to a given list.
     **/
    public List listResources (String name, List list) throws IOException {
	return listResources (name, list, Integer.MAX_VALUE) ;
    }

    /**
     * Returns a list of all resources matching a given name.
     **/
    public List listResources (String name) throws IOException {
	return listResources (name, new ArrayList ()) ;
    }

    /**
     * Returns {@link File} corresponding to URL, if URL refers to a file.
     * Returns null otherwise.
     **/
    public static File URLToFile (URL url) {
	File file ;

	if (! fileContext.getProtocol().equals (url.getProtocol()))
	    file = null ;
	else if (! fileContext.getHost().equals (url.getHost()))
	    file = null ;
	else {
	    String name = url . getPath() . replace ('/', File.separatorChar) ;
	    file = new File (name) ;
	}

	debug.println ("URLToFile? " + url + " -> " + file) ;
	return file ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    /** List of URLs in search path that refer to local directories. **/
    protected List directoryList = null ;

    /** Number of URLs in search path checked for local directories. **/
    protected int urlsChecked = 0 ;

    /** Specifies defaults for missing URL fields. **/
    protected static URL fileContext ;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private static LineWriter
        debug = Debug.global.getWriter ("debug.abstractclassloader") ;

    static {
	try {
	    fileContext = new URL ("file", "", "") ;
	} catch (MalformedURLException except) {
	    throw new IllegalStateException ("fileContext -- " + except) ;
	}
    }

}
