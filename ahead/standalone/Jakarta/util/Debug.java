package Jakarta.util ;

import java.io.File ;
import java.io.FileInputStream ;
import java.io.FileNotFoundException ;
import java.io.InputStream ;
import java.io.IOException ;
import java.io.OutputStream ;
import java.io.Writer ;

import java.util.Properties ;

public class Debug extends Properties {

    public Debug (Properties defaults) {
	super (defaults) ;
    }

    public Debug () {
	this (System.getProperties()) ;
    }

    /**
     * For debugging purposes, a property is <KBD>false</KBD> only if it is
     * either not defined or explicitly defined to be <KBD>"false"</KBD>.
     **/
    public boolean getBoolean (String key) {
	String value = getProperty (key) ;
	return (value != null && ! value.equalsIgnoreCase("false")) ;
    }

    public LineWriter getWriter (String key, OutputStream out) {
	LineWriter writer =
	    getBoolean(key)
	    ? (new LineWriter (out, true))
	    : (new NullLineWriter ()) ;
	writer.setLabel(key) ;
	return writer ;
    }

    public LineWriter getWriter (String key, Writer out) {
	LineWriter writer =
	    getBoolean(key)
	    ? (new LineWriter (out, true))
	    : (new NullLineWriter ()) ;
	writer.setLabel(key) ;
	return writer ;
    }

    public LineWriter getWriter (String key) {
	return getWriter (key, System.err) ;
    }

    public void load (File loadFile)
    throws IOException {
	load (new FileInputStream (loadFile)) ;
    }

    public void load (String fileName)
    throws IOException {
	File loadFile = new File (fileName) ;
	if (! loadFile.exists() && ! loadFile.isAbsolute())
	    loadFile = new File (System.getProperty("user.home"), fileName) ;
	load (loadFile) ;
    }

    public static final Debug global ;
    public static final LineWriter writer ;

    static {
	global = new Debug() ;
	writer = global.getWriter("debug") ;
	try {
	    global.load ("debug.properties") ;
	} catch (FileNotFoundException except) {
	    /* ignored */
	} catch (IOException except) {
	    except.printStackTrace (writer) ;
	}
    }

}
