package Jakarta.loader ;

import java.io.File ;
import java.io.IOException ;
import java.io.OutputStream ;
import java.io.PrintWriter ;

import java.net.URL ;

import java.util.Enumeration ;

public class SmokeTest {

    private SmokeTest () {
	// Private constructor defined to prevent external instantiation.
    }

    static private Loader loader = null ;

    static public void reportResources (String resource, OutputStream stream)
    throws IOException {
	reportResources (resource, new PrintWriter (stream, true)) ;
    }

    static public void reportResources (String resource, PrintWriter writer)
    throws IOException {

	int count = 0 ;

	Enumeration resourceEnum = loader.getResources (resource) ;
	while (resourceEnum.hasMoreElements()) {
	    writer.println ("* " + (URL) resourceEnum.nextElement ()) ;
	    count = 1 + count ;
	}

	writer.println ("= " + count + " resources found.") ;

	File directory = loader.getResourceDirectory (resource) ;
	writer.println ("+ " + directory + " resource directory") ;
    }

    static public void main (String[] args) throws Exception {

	loader = new PrefixClassLoader ("Jakarta/") ;

	for (int n = 0 ; n < args.length ; ++n) {
	    if (n > 0)
		System.out.println () ;
	    reportResources (args[n], System.out) ;
	}

    }

}
