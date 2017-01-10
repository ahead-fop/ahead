/** Loader.java
 * Loads a resource using a Class loader.
 * AHEAD Project
 * @author Roberto E. Lopez-Herrejon
 * Last Update: July 10, 2003 4:00 pm
 */

package XC;

import java.util.*;
import java.io.*;

public class Loader {

    public BufferedReader getResource(String resource) {
	InputStream stream = null;

	try {
	    stream = getClass().getResourceAsStream(resource);
	    if (stream == null) {
		ClassLoader loader = getClass().getClassLoader() ;
		stream = loader.getResourceAsStream (resource) ;
	    }
	    BufferedReader br = 
		new BufferedReader(new InputStreamReader(stream));

	    // @pending for further details
	    // IO.loadProperties (stream, appProperties) ;
	    // stream.close();
	    
	    return br;
	} catch (NullPointerException except) {
	    System.out.println("warning resource \"" + resource 
			       + "\" not found") ;
	} 
	/* catch (IOException except) {
	    System.out.println("error loading resource \"" + resource 
			       + '"' + except) ;
	}
	*/

	return null;
    } // of getResource

} // of Loader
