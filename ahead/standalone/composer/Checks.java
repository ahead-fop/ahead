package composer ;

import java.io.File ;

/**
 * Utility class with static methods for required pre-condition checks.  These
 * are different from assertions since they check for conditions that may not
 * be under the control of the developer, such as input arguments to methods.
 **/
public class Checks {

    public static File directoryFile (File file) throws FileException {

	if (! file.isDirectory ())
	    throw new FileException (file, "is not a directory") ;

	return file ;
    }

    public static File inputFile (File file) throws FileException {

	if (! file.exists ())
	    throw new FileException (file, "does not exist") ;

	if (! file.canRead ())
	    throw new FileException (file, "is not readable") ;

	return file ;
    }

    public static File inputNonDirectory (File file) throws FileException {

	if (file.isDirectory ())
	    throw new FileException (file, "is a directory") ;

	return inputFile (file) ;
    }

    public static Object nonNull (Object object, String name) {

	if (object == null)
	    throw new NullPointerException ("object \"" + name + "\"") ;

	return object ;
    }

    public static Object nonNullArgument (Object argument, String name) {

	if (argument == null)
	    throw new NullPointerException ("argument \"" + name + "\"") ;

	return argument ;
    }

    public static Object nonNullArray (Object[] array, String name) {

	if (array == null)
	    throw new NullPointerException ("array \"" + name + "\"") ;

	for (int n = array.length ; --n >= 0 ; )
	    if (array [n] == null)
		throw new NullPointerException (
		    "array entry \"" + name + '[' + n + "]\""
		) ;

	return array ;
    }

    public static File outputFile (File file) throws FileException {

	if (file.exists () && ! file.canWrite ())
	    throw new FileException (file, "can't be written") ;

	return file ;
    }

    public static File outputRegularFile (File file) throws FileException {

	if (! file.exists ())
	    return file ;

	if (! file.isFile ())
	    throw new FileException (file, "is not a regular file") ;

	if (! file.canWrite ())
	    throw new FileException (file, "can't be written") ;

	return file ;
    }

    public static boolean require (boolean testValue, String message)
    throws IllegalStateException {

	if (! testValue)
	    throw new IllegalStateException (message) ;

	return testValue ;
    }

}
