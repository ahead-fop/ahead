package Jakarta.loader ;

import Jakarta.util.Debug ;
import Jakarta.util.LineWriter ;

import java.io.InputStream ;
import java.io.IOException ;

import java.net.URL ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.Enumeration ;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Map ;
import java.util.Properties ;

/**
 * A <code>PrefixClassLoader</code> extends the standard {@link ClassLoader}
 * by adding searchs for a resource along search paths that are specified by
 * the prefixes of the resource name.  For example, suppose a
 * <code>PrefixClassLoader</code> is created for the prefix "JTS/".  Such a
 * loader will search for resources named with this prefix.  Here's how the
 * search works:
 *
 * <ul>
 *
 * <li>
 * First, following the standard delegation model, parent
 * <code>ClassLoader</code>s check for the resource.
 * </li>
 *
 * <li>
 * If the resource is not found by the parent <code>ClassLoader</code>s, the
 * <code>PrefixClassLoader</code> delegates the search to subsidiary
 * <code>ClassLoader</code>s identified by prefixes of the resource name,
 * starting from the longest prefix and progressing to the shortest prefix.
 * For example, a resource named "JTS/realms/J/Java/CodeTemplate" would be
 * delegated to loaders identified by "JTS/realms/J/Java", "JTS/realms/J",
 * "JTS/realms" and "JTS", in that order.
 * </li>
 *
 * <li>
 * If a <code>ClassLoader</code> is not associated with a prefix, then one is
 * created for that prefix.  First, the prefix is converted to a property name
 * by replacing all "/" characters by ".", converting upper-case letters to
 * lower-case, and appending ".path".  If the property name is defined, its
 * definition is taken to be a search path and a {@link PathClassLoader} is
 * created for it.  Otherwise, a {@link NullClassLoader} is defined.
 * </li>
 *
 * </ul>
 *
 * Additional methods are provided to locate writable directories in the host
 * system via a search path.
 **/
public class PrefixClassLoader extends AbstractClassLoader {

    public PrefixClassLoader (String prefix, ClassLoader parent) {
	super (new URL[0], parent) ;

	this.classLoaderMap = new HashMap () ;
	this.prefix = prefix ;
	this.properties = new Properties (System.getProperties()) ;

	// Convert the prefix to a resource naming a properties file:
	//
	String resource = prefix.replace ('/', '.') ;

	while (resource.endsWith("."))
	    resource = resource.substring (0, resource.length() - 1) ;
	resource = resource + ".properties" ;

	// Check for an alias to another resource.  If a property with the
	// same name as the resource exists, then its value is taken to be the
	// true name of the resource to use.  Otherwise, the original resource
	// name is used.
	//
	resource = this.properties.getProperty (resource, resource) ;

	// If the resource exists, read property definitions from it:
	//
	InputStream stream = getResourceAsStream (resource) ;
	if (null != stream)
	    try {
		this.properties.load (stream) ;
		debug.println ("loaded properties from " + resource) ;
	    } catch (IOException except) {
		throw new IllegalStateException ("error reading " + resource) ;
	    }

	debug.println ("constructed " + toString()) ;
    }

    public PrefixClassLoader (String prefix) {
	this (prefix, ClassLoader.getSystemClassLoader()) ;
    }

    /**
     * Lists resources with a given name, up to a certain number.
     **/
    public List listResources (String name, List list, int maxSize)
    throws IOException {

	if (name.startsWith (this.prefix)) {

	    debug.println ("list resources for " + name) ;

	    int slash = name.lastIndexOf ('/') ;
	    while (slash >= 0 && list.size() < maxSize) {

		String prefix = name.substring (0, slash) ;
		String suffix = name.substring (1 + slash) ;

		// Get the class loader for prefix, if any:
		//
		AbstractClassLoader loader
		    = (AbstractClassLoader) classLoaderMap.get (prefix) ;

		// If no class loader previously defined, create one to use
		// the search path defined by a property name based on prefix:
		//
		if (null == loader) {
		    String base = prefix.replace ('/', '.') ;

		    String property = base + ".path" ;
		    String path = properties.getProperty (property) ;
		    debug.println ("property " + property + " -> " + path) ;

		    if (null != path)
			loader = new PathClassLoader (path, getParent()) ;
		    else
			loader = new NullClassLoader (getParent()) ;

		    classLoaderMap.put (prefix, loader) ;
		    debug.println ("map " + prefix + " -> " + loader) ;
		}

		// Extract resources from class loader associated with prefix:
		// (global name first, then local name)
		//
		loader.listResources (name, list, maxSize) ;
		loader.listResources (suffix, list, maxSize) ;

		slash = name.lastIndexOf ('/', slash - 1) ;
	    }
	}

	return list ;
    }

    public String toString () {
	if (null == toStringValue) {
	    toStringValue = getClass() . getName() + " (" ;
	    toStringValue = toStringValue + "\"" + prefix + "\"" ;

	    String parent = ClassLoaders.toString (getParent()) ;
	    toStringValue = toStringValue + ", " + parent + ")" ;
	}
	return toStringValue ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    protected Map classLoaderMap ;
    protected String prefix ;
    protected Properties properties ;
    protected String toStringValue = null ;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    static private LineWriter
	debug = Debug.global.getWriter ("debug.prefixclassloader") ;

}
