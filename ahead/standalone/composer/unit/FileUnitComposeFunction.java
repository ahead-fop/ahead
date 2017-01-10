package composer.unit ;

import composer.algebra.AlgebraException ;
import composer.algebra.AlgebraFactory ;
import composer.algebra.Function ;
import composer.algebra.Type ;
import composer.algebra.Unit ;

import composer.unit.Factory ;

import composer.Checks ;
import composer.FileException ;

import java.io.BufferedInputStream ;
import java.io.BufferedOutputStream ;
import java.io.File ;
import java.io.FileInputStream ;
import java.io.FileNotFoundException ;
import java.io.FileOutputStream ;
import java.io.InputStream ;
import java.io.IOException ;
import java.io.OutputStream ;
import java.io.PrintWriter ;

import java.lang.reflect.Array ;

import java.net.URI ;

import java.util.Arrays ;
import java.util.Collections ;
import java.util.Iterator ;
import java.util.List ;

import java.util.logging.Logger ;

public class FileUnitComposeFunction implements Function {

    /**
     * This method should be overridden by sub-classes.
     **/
    public void invoke (Unit targetUnit) throws AlgebraException {
	File target = ((FileUnit) targetUnit) . getFile () ;
	getLogger().warning (fileMessage (target, "skipped -- unknown type")) ;
    }

    final public AlgebraFactory getFactory () {
	return factory ;
    }

    final public static Logger getLogger () {
	return Logger.getLogger ("composer") ;
    }

    final public List getSources () {
	return sources ;
    }

    final public Type getType () {
	return factory.classType (getClass ()) ;
    }

    public void writeEquation (File target) {

	if (equationsWriter == null)
	    return ;

	equationsWriter.print (file2normal (target) + " =") ;

	for (Iterator p = getSources().iterator () ; p.hasNext () ; ) {
	    File source = ((FileUnit) p.next ()) . getFile () ;
	    equationsWriter.print (" " + file2normal (source)) ;
	}

	equationsWriter.println () ;
    }

    final public static void endOutput () {
	if (equationsWriter != null)
	    equationsWriter.close () ;
    }

    final public static void setOutput (String equationsName)
    throws FileException {

	equationsWriter = null ;

	if (equationsName == null || equationsName.length () < 1)
	    return ;

	File equationsFile = new File (equationsName) ;
	Checks.outputRegularFile (equationsFile) ;

	try {
	    equationsWriter = new PrintWriter (
		new FileOutputStream (equationsFile)
	    ) ;

	} catch (FileNotFoundException exception) {
	    throw new FileException (equationsFile, exception.getMessage ()) ;
	}
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    protected FileUnitComposeFunction
    (Factory factory, List sources, Class sourceClass) {

	Checks.nonNullArgument (factory, "factory") ;
	Checks.nonNullArgument (sources, "sources") ;
	Checks.nonNullArgument (sourceClass, "sourceClass") ;

	this.factory = factory ;

	Object[] array =
	    (Object[]) Array.newInstance (sourceClass, sources.size ()) ;

	sources.toArray (array) ;
	this.sources = Collections.unmodifiableList (Arrays.asList (array)) ;
    }

    FileUnitComposeFunction (Factory factory, List sources) {
	this (factory, sources, FileUnit.class) ;
    }

    /**
     * Calculate the aspect name of a {@link File} as the base aspect, if any,
     * followed by the relative path to the given {@link File}.
     **/
    final String aspectName (File file) {
       File target = ((Factory) getFactory ()) . getTarget () ;
       return target.getName();
    }

/*
	StringBuffer buffer = new StringBuffer () ;
	File target = ((Factory) getFactory ()) . getTarget () ;

System.err.println( "file "+file.getName() + " target " + target.getName() );

	File ancestor = file.getParentFile () ;
	while (ancestor != null && ! ancestor.equals (target)) {
	    buffer.insert(0,'.').insert (0, ancestor.getName()) ;
	    ancestor = ancestor.getParentFile () ;
	}

	if (aspectProperty != null && aspectProperty.length () > 0)
	    buffer.insert(0,'.').insert (0, aspectProperty) ;

	return trimPeriods (buffer.toString ()) ;
    }
*/

    final protected static boolean backupFile (File oldFile) {

	return
	    oldFile.exists ()
	    ? renameFile (oldFile, oldFile.getName () + '~')
	    : true ;
    }

    final protected static void copyFile (File source, File target)
    throws IOException {

	getLogger().info (fileMotion ("copy", source, target)) ;

	backupFile (target) ;

	BufferedInputStream sourceStream =
	    new BufferedInputStream (new FileInputStream (source)) ;

	BufferedOutputStream targetStream =
	    new BufferedOutputStream (new FileOutputStream (target)) ;

	copyStream (sourceStream, targetStream) ;

	targetStream.close () ;
	sourceStream.close () ;
    }

    final protected static void copyStream (InputStream inp, OutputStream out)
    throws IOException {

	byte[] buffer = new byte [8192] ;
	for (int length ; (length = inp.read (buffer)) > 0 ; )
            out.write (buffer, 0, length) ;
    }

    final private static boolean equals (Object one, Object two) {
	return (one == null) ? (two == null) : one.equals (two) ;
    }

    final private String file2normal (File file) {
	Factory factory = (Factory) getFactory () ;
	URI prefix = new File (factory.getModelPath ()) . toURI () ;
	URI uri = file.toURI () ;
	return prefix.relativize(uri).toString () ;
    }

    final private static String fileLabel (File file) {

	return
	    file.isDirectory ()
	    ? "directory " + quote (file)
	    : "file " + quote (file) ;
    }

    final protected static String fileMessage (File file, String message) {
	return fileLabel (file) + ' ' + message ;
    }

    final protected static String fileMotion (String msg, File from, File to) {

	String toName =
	    equals (from.getParent (), to.getParent ())
	    ? to.getName ()
	    : to.getPath () ;

	return msg + ' ' + fileLabel (from) + " -> " + toName ;
    }

    final protected static String filesComposed
    (String msg, List sources, File target) {
	return msg + ' ' + sources + " -> " + quote (target) ;
    }

    final protected static String quote (Object object) {
	return quote (object.toString ()) ;
    }

    final protected static String quote (String string) {
	return "\"" + string + '"' ;
    }

    final protected static boolean renameFile (File oldFile, String newName) {

	File newFile = new File (oldFile.getParentFile (), newName) ;

	if (! oldFile.renameTo (newFile))
	    return false ;

	getLogger().fine (fileMotion ("renamed", oldFile, newFile)) ;

	return true ;
    }

    final protected static String trim (String string) {
	return
	    (string != null && string.length () > 0)
	    ? string.trim()
	    : string ;
    }

    final private static String trimPeriods (String string) {

	if (string == null || string.length () < 1)
	    return string ;

	int tail = string.length () - 1 ;
	while (tail >= 0 && string.charAt (tail) == '.')
	    -- tail ;
	++ tail ;

	int head = 0 ;
	while (head < tail && string.charAt (head) == '.')
	    ++ head ;

	return
	    (head > 0 || tail < string.length ())
	    ? string.substring (head, tail)
	    : string ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private Factory factory ;
    final private List sources ;

    final private static String aspectProperty =
        trim (System.getProperty ("composer.layer.base")) ;

    private static PrintWriter equationsWriter = null ;

}
