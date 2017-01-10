package composer.unit ;

import composer.algebra.AlgebraException ;
import composer.algebra.Unit ;

import java.io.BufferedWriter ;
import java.io.File ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.OutputStream ;
import java.io.OutputStreamWriter ;

import java.util.ArrayList ;
import java.util.Date ;
import java.util.Iterator ;
import java.util.List ;

final public class EquationFileUnitComposeFunction
extends FileUnitComposeFunction {

    public EquationFileUnitComposeFunction
    (Factory factory, List source, Class klass) {
	super (factory, source, klass) ;
    }

    public void invoke (Unit targetUnit) throws AlgebraException {

	File target = ((FileUnit) targetUnit) . getFile () ;
	writeEquation (target) ;

	List sources = new ArrayList (getSources().size ()) ;
	for (Iterator p = getSources().iterator () ; p.hasNext () ; )
	    sources.add (((FileUnit) p.next ()) . getFile ()) ;

	try {
	    composeEquation (sources, target) ;
	    getLogger().info (filesComposed ("equation", sources, target)) ;

	} catch (Exception except) {
	    throw new AlgebraException (
		fileMessage (target, "can't create equation file"),
		except
	    ) ;
	}
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    EquationFileUnitComposeFunction (Factory factory, List sources) {
	this (factory, sources, EquationFileUnit.class) ;
    }

    final private static void composeEquation (List sources, File target)
    throws EquationException, IOException {

	// Compose multiple equation files by loading them in order:
	//
	Iterator sourceIterator = sources.iterator () ;
	List composition = readEquation ((File) sourceIterator.next ()) ;

	while (sourceIterator.hasNext ()) {
	    List equation = readEquation ((File) sourceIterator.next ()) ;
	    composition = superSubstitute (composition, equation) ;
	}

	backupFile (target) ;

	// Write the composed equation map to the target file:
	//
	OutputStream output = null ;
	try {
	    output = new FileOutputStream (target) ;
	    writeEquation (composition, output, "Generated from " + sources) ;
	    output = null ;

	} finally {
	    if (output != null)
		output.close () ;
	}
    }

    final private static List readEquation (File file)
    throws EquationException, IOException {
	return EquationFileUnit.readEquation (file) ;
    }

    final private static String saveConvert (CharSequence field) {

	if (field.length () < 1)
	    return "" ;

	final String HEX = "0123456789ABCDEF" ;
	StringBuffer buffer = new StringBuffer (field.length ()) ;

	char character = field.charAt (0) ;
	if (character == '#' || character == '!')
	    buffer.append ('\\') ;

	for (int index = 0 ; index < field.length () ; ++index) {

	    character = field.charAt (index) ;
	    switch (character) {

	    case '\\' : buffer.append('\\').append ('\\') ; break ;
	    case '\f' : buffer.append('\\').append ('f')  ; break ;
	    case '\n' : buffer.append('\\').append ('n')  ; break ;
	    case '\r' : buffer.append('\\').append ('r')  ; break ;
	    case '\t' : buffer.append('\\').append ('t')  ; break ;

	    default :
		if (character > 0x0020 && character <= 0x007E) {
		    buffer.append (character) ;
		    break ;
		}

		buffer.append ("\\u") ;
		buffer.append (HEX.charAt (0xF & (character >> 12))) ;
		buffer.append (HEX.charAt (0xF & (character >>  8))) ;
		buffer.append (HEX.charAt (0xF & (character >>  4))) ;
		buffer.append (HEX.charAt (0xF &  character)) ;
		break ;
	    }
	}

	return buffer.toString () ;
    }

    final public static List superSubstitute (List source, List target) {
	return EquationFileUnit.superSubstitute (source, target) ;
    }

    final public static void writeEquation
    (List fields, OutputStream stream, String comment)
    throws IOException {

	BufferedWriter writer =
	    new BufferedWriter (new OutputStreamWriter (stream, "8859_1")) ;

	if (comment != null)
	    writeLine (writer, "# " + comment.trim ()) ;
	writeLine (writer, "# " + (new Date ()).toString ()) ;

	for (Iterator p = fields.iterator () ; p.hasNext () ; ) {
	    Object operand = p.next() ;
	    if (operand == null)
		continue ;
	    String field = operand.toString() ;
	    if (field != null && field.length () > 0)
		writeLine (writer, saveConvert (field)) ;
	}

	writer.close () ;
    }

    final private static void writeLine (BufferedWriter writer, String line)
    throws IOException {
	writer.write (line) ;
	writer.newLine () ;
    }

}
