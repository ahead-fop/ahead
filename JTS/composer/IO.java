package composer ;

import java.io.BufferedInputStream ;
import java.io.File ;
import java.io.FileInputStream ;
import java.io.InputStream ;
import java.io.IOException ;

import java.util.Properties ;

/**
 * <code>IO</code> is a utility class containing static methods to deal with
 * common input and output tasks.
 **/
public class IO {

    /**
     * Given a {@link File}, returns the corresponding canonical {@link File},
     * if possible, or the corresponding absolute {@link File} otherwise.
     **/
    final public static File getCanonicalFile (File file) {
	try {
	    return file.getCanonicalFile () ;
	} catch (IOException exception) {
	    return file.getAbsoluteFile () ;
	}
    }

    /**
     * Returns the {@link Properties} object, <code>properties</code>, after
     * loading it with additional property definitions from the input file
     * <code>inpFile</code>.  Each new property definition, if it has a name
     * matching one already in <code>properties</code>, will override the old.
     **/
    final public static Properties loadProperties
    (File inpFile, Properties properties) throws FileException, IOException {

	Checks.inputFile (inpFile) ;

	InputStream inpStream =
	    new BufferedInputStream (new FileInputStream (inpFile)) ;

	return loadProperties (inpStream, properties) ;
    }

    /**
     * Returns a new {@link Properties} object after loading it from the input
     * file <code>inpFile</code>.
     **/
    final public static Properties loadProperties (File inpFile)
    throws FileException, IOException {
	return loadProperties (inpFile, new Properties ()) ;
    }

    /**
     * Returns the {@link Properties} object, <code>properties</code>, after
     * loading it with additional property definitions from the input stream
     * <code>inpStream</code>.  Each new property definition, if it has a name
     * matching one already in <code>properties</code>, will override the old.
     *
     * <p>
     * <em>Caution:</em> This method closes <code>inpStream</code> before
     * returning.
     *
     * @throws IOException for failures during loading.
     **/
    final public static Properties loadProperties
    (InputStream inpStream, Properties properties) throws IOException {

	Checks.nonNullArgument (inpStream, "inpStream") ;
	Checks.nonNullArgument (properties, "properties") ;

	try {
	    properties.load (inpStream) ;
	} finally {
	    inpStream.close () ;
	}

	return properties ;
    }

    /**
     * Returns a new {@link Properties} object after loading it from the input
     * stream <code>inpStream</code>.
     **/
    final public static Properties loadProperties (InputStream inpStream)
    throws IOException {
	return loadProperties (inpStream, new Properties ()) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    /**
     * Private constructor to prevent external instantiation.
     **/
    private IO () {
	/* Empty body. */
    }

}
