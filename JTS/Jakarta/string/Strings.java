package Jakarta.string ;

import Jakarta.util.Debug ;
import Jakarta.util.LineWriter ;

import java.util.ArrayList ;
import java.util.List ;

/**
 * <kbd>Strings</kbd> is a utility class containing static methods that provide
 * useful string operations.
 **/
public class Strings {

    /**
     * Private constructor exists only so that class cannot be instantiated.
     **/
    private Strings () {}

    static public List split (String source, String separator) {
        List result = new ArrayList () ;

        for (int base = 0 ; base < source.length() ; ) {
            int next = source.indexOf (separator, base) ;

	    if (next < base)
		next = source.length () ;

	    result.add (source.substring (base, next)) ;
	    base = next + separator.length() ;
        }

        return result ;
    }

    static public List split
    (String source, String prefix, String separator, String suffix) {
        if (! source.startsWith (prefix) || ! source.endsWith (suffix))
            throw new StringFormatException ("missing prefix or suffix") ;
        if (source.length() < prefix.length() + suffix.length())
            throw new StringFormatException ("prefix/suffix clash") ;

        int interiorEnd = source.length() - suffix.length() ;
        String interior = source.substring (prefix.length(), interiorEnd) ;

        return split (interior, separator) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    static private LineWriter
	debug = Debug.global.getWriter ("debug.strings") ;

}
