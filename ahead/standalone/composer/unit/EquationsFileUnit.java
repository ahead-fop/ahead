package composer.unit ;

import composer.algebra.Function ;
import composer.algebra.Type ;

import composer.Checks ;

import java.io.BufferedInputStream ;
import java.io.File ;
import java.io.FileInputStream ;
import java.io.InputStream ;
import java.io.IOException ;

import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.HashSet ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Map ;
import java.util.Properties ;
import java.util.Set ;

import java.util.regex.Pattern ;

public class EquationsFileUnit extends PropertiesFileUnit {

    public EquationsFileUnit (Factory factory, File file, Type type) {
	super (factory, file, type) ;
    }

    public static Function composeFunction (Factory factory, List sources) {
	return new EquationsFileUnitComposeFunction (factory, sources) ;
    }

    final public static Properties loadProperties (File file)
    throws IOException {

	Checks.inputFile (file) ;

	Properties properties = new Properties () ;
	InputStream input = null ;
	try {
	    input = new BufferedInputStream (new FileInputStream (file)) ;
	    properties.load (input) ;

	} finally {
	    if (input != null) 
		input.close () ;
	}

	return properties ;
    }

    final public List readEquation () throws EquationException, IOException {
	return readEquation (getFile ()) ;
    }

    final public static List readEquation (File file)
    throws EquationException, IOException {
	return readEquation (file, null) ;
    }

    final public static List readEquation (File file, String thisName)
    throws EquationException, IOException {
	Properties properties = loadProperties (file) ;
	return expandThis (file.getName (), properties, thisName) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private static List expand (Map map, String key)
    throws EquationException {
	return expand (map, key, new HashSet ()) ;
    }

    final private static List expand (Map map, String key, Set visited)
    throws EquationException {

	if (visited.contains (key))
	    throw new EquationException (key, "causes a cycle") ;

	visited.add (key) ;

	String value = (String) map.get (key) ;

	List operands = Arrays.asList (SPACE.split (value + " ")) ;
	List result = new ArrayList (operands.size ()) ;
	for (Iterator p = operands.iterator () ; p.hasNext () ; ) {

	    String operand = (String) p.next () ;
	    
	    if (map.containsKey (operand))
		result.addAll (expand (map, operand, visited)) ;
	    else
		result.add (operand) ;
	}

	visited.remove (key) ;
	return result ;
    }

    final private static List expandThis (String key, Map map, String thisName)
    throws EquationException {

	if (thisName != null && thisName.length () > 0) {
	    if (! map.containsKey (thisName))
		throw new EquationException (thisName, "is undefined") ;
	    return expand (map, thisName) ;
	}

	if (map.containsKey (THIS))
	    return expand (map, THIS) ;

	int dot = key.lastIndexOf ('.') ;
	if (dot > 0)
	    key = key.substring (0, dot) ;

	return expand (map, key) ;
    }

    final public static String superSubstitute
    (String value, Map map, String key)
    throws EquationException {

	List operands = Arrays.asList (SPACE.split (value + " ")) ;
	List results = new ArrayList (operands.size ()) ;

	for (Iterator p = operands.iterator () ; p.hasNext () ; ) {

	    String operand = (String) p.next () ;
	    Object result = operand ;

	    if (operand.equals (SUPER))
		results.add (map.get (map.containsKey(THIS) ? THIS : key)) ;
	    else if (operand.startsWith (SUPER_DOT))
		results.add (map.get (operand.substring(SUPER_DOT.length()))) ;
	    else
		results.add (operand) ;
	}

	StringBuffer buffer = new StringBuffer (value.length ()) ;
	for (Iterator p = results.iterator () ; p.hasNext () ; ) {
	    String field = (String) p.next () ;
	    if (buffer.length () > 0 && field.length () > 0)
		buffer.append (' ') ;
	    buffer.append (field) ;
	}

	return buffer.toString () ;
    }

    final private static Pattern SPACE = Pattern.compile ("\\s+") ;
    final private static String SUPER = "super" ;
    final private static String SUPER_DOT = SUPER + '.' ;
    final private static String THIS = "this" ;

}
