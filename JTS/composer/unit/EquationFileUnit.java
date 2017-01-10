package composer.unit ;

import composer.algebra.Function ;
import composer.algebra.Type ;

import java.io.BufferedReader ;
import java.io.File ;
import java.io.FileInputStream ;
import java.io.InputStreamReader ;
import java.io.IOException ;

import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.List ;

public class EquationFileUnit extends FileUnit {

    public EquationFileUnit (Factory factory, File file, Type type) {
	super (factory, file, type) ;
    }

    public static Function composeFunction (Factory factory, List sources) {
	return new EquationFileUnitComposeFunction (factory, sources) ;
    }

    final private static List convert (String[] strings) {

	List fields = new ArrayList (strings.length) ;
	for (int n = 0 ; n < strings.length ; ++n)
	    if (strings[n].length () > 0)
		fields.add (fieldConvert (strings [n])) ;

	return fields ;
    }

    final private static String fieldConvert (CharSequence field) {

	StringBuffer buffer = new StringBuffer (field.length ()) ;

	for (int index = 0 ; index < field.length () ; ) {

	    char character = field.charAt (index++) ;
	    if (character != '\\') {
		buffer.append (character) ;
		continue ;
	    }

	    character = field.charAt (index++) ;
	    switch (character) {
	    case 'f' :	buffer.append ('\f') ; continue ;
	    case 'n' :	buffer.append ('\n') ; continue ;
	    case 'r' :	buffer.append ('\r') ; continue ;
	    case 't' :	buffer.append ('\t') ; continue ;

	    case 'u' :
		buffer.append (unicode (field.subSequence (index, index+4))) ;
		index += 4 ;
		continue ;

	    default :	buffer.append (character) ; continue ;
	    }
	}

	return buffer.toString () ;
    }

    final public static boolean isComment (CharSequence chars) {

        for (int n = 0 ; n < chars.length () ; ++n) {
            char character = chars.charAt (n) ;
            if (character == '#' || character == '!')
                return true ;
            if (WHITESPACE.indexOf (character) < 0)
                return false ;
        }

        return false ;
    }

    final public static boolean isContinuation (CharSequence chars) {

        int slashCount = 0 ;

        for (int n = chars.length () ; n > 0 && chars.charAt (--n) == '\\' ; )
            ++ slashCount ;

        return slashCount % 2 == 1 ;
    }

    final public List readEquation () throws IOException {
	return readEquation (getFile ()) ;
    }

    final public static List readEquation (File file) throws IOException {

	final String SPACE = "\\s+" ;

	BufferedReader reader = new BufferedReader (
	    new InputStreamReader (new FileInputStream (file), "8859_1")
	) ;

	List equation = new ArrayList () ;

	String line = null ;
	while ((line = readLine (reader)) != null)
	    equation.addAll (convert (line.split (SPACE))) ;
	reader.close () ;

	return equation ;
    }

    final private static String readLine (BufferedReader reader)
    throws IOException {

	String line = reader.readLine () ;
	while (line != null && isComment (line))
	    line = reader.readLine () ;

	if (line == null || ! isContinuation (line))
	    return line ;

	StringBuffer buffer =
	    new StringBuffer (line.substring (0, line.length () - 1)) ;

	while (true) {
	    line = reader.readLine () ;
	    if (line == null)
		return buffer.toString () ;
	    if (isComment (line))
		continue ;
	    if (! isContinuation (line))
		return buffer.append(line).toString () ;
	    buffer.append (line.substring (0, line.length () - 1)) ;
	}
    }

    final public static List superSubstitute (List source, List target) {

	List result = new ArrayList (target.size ()) ;
	for (Iterator p = target.iterator () ; p.hasNext () ; ) {
	    String field = (String) p.next () ;
	    if (field.equals ("super"))
		result.addAll (source) ;
	    else
		result.add (field) ;
	}

	return result ;
    }

    final private static char unicode (CharSequence hex) {

	int value = 0 ;
	for (int index = 0 ; index < hex.length () ; ++index) {

	    char character = hex.charAt (index) ;

	    switch (character) {

	    case '0' : case '1' : case '2' : case '3' : case '4' :
	    case '5' : case '6' : case '7' : case '8' : case '9' :
		value = (value << 4) + (character - '0') ;
		break ;

	    case 'a' : case 'b' : case 'c' :
	    case 'd' : case 'e' : case 'f' :
		value = (value << 4) + (character - 'a' + 10) ;
		break ;

	    case 'A' : case 'B' : case 'C' :
	    case 'D' : case 'E' : case 'F' :
		value = (value << 4) + (character - 'A' + 10) ;
		break ;

	    default :
		throw new IllegalArgumentException ("malformed \\u#### code") ;
	    }
	}

	return (char) (value & 0xFFFF) ;
    }

    final private static String WHITESPACE = " \f\n\r\t" ;

}
