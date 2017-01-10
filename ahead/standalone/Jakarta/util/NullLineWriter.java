package Jakarta.util ;

import java.io.OutputStream ;
import java.io.Writer ;

/**
 * A NullLineWriter is a LineWriter that never produces output.  This is
 * done by overriding the data output methods with null versions.
 **/
public class NullLineWriter extends LineWriter {

    public NullLineWriter (OutputStream out, boolean autoFlush) {
	super (out, autoFlush) ;
    }

    public NullLineWriter (OutputStream out) {
	super (out) ;
    }

    public NullLineWriter (Writer out, boolean autoFlush) {
	super (out, autoFlush) ;
    }

    public NullLineWriter (Writer out) {
	super (out) ;
    }

    public NullLineWriter () {
	super () ;
    }

    public boolean isEnabled () {
	return false ;
    }

    public void print (boolean value) {}
    public void print (char value) {}
    public void print (char[] value) {}
    public void print (double value) {}
    public void print (float value) {}
    public void print (int value) {}
    public void print (long value) {}
    public void print (Object value) {}
    public void print (String value) {}

    public void println () {}
    public void println (boolean value) {}
    public void println (char value) {}
    public void println (char[] value) {}
    public void println (double value) {}
    public void println (float value) {}
    public void println (int value) {}
    public void println (long value) {}
    public void println (Object value) {}
    public void println (String value) {}

    public void write (int value) {}
    public void write (char[] buffer, int offset, int length) {}
    public void write (char[] buffer) {}
    public void write (String string, int offset, int length) {}
    public void write (String string) {}

}
