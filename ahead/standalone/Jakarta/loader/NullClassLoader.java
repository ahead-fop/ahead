package Jakarta.loader ;

import Jakarta.util.Debug ;
import Jakarta.util.LineWriter ;

import java.io.File ;

import java.net.URL ;

import java.util.List ;

public class NullClassLoader extends AbstractClassLoader {

    public NullClassLoader (ClassLoader parent) {
	super (new URL[0], parent) ;
	debug.println ("constructed " + toString()) ;
    }

    public NullClassLoader () {
	this (ClassLoader.getSystemClassLoader()) ;
    }

    public List listResources (String name, List result, int limit) {
	debug.println ("listResources for " + name + " returns none") ;
	return result ;
    }

    public String toString () {
	if (null == toStringValue) {
	    toStringValue = getClass() . getName() ;

	    String parent = ClassLoaders.toString (getParent()) ;
	    toStringValue = toStringValue + " (" + parent + ")" ;
	}
	return toStringValue ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    protected String toStringValue ;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    static private LineWriter
	debug = Debug.global.getWriter ("debug.nullclassloader") ;

}
