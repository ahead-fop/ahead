package Jakarta.util ;

import java.io.OutputStream ;
import java.io.OutputStreamWriter ;
import java.io.PrintWriter ;
import java.io.Writer ;

/**
 * A LineWriter is a line-buffered PrintWriter that can, optionally, label
 * output lines.  It has methods for accessing the label.
 **/
public class LineWriter extends PrintWriter {

    public LineWriter (Writer out, boolean autoFlush) {
	super (out, autoFlush) ;
    }

    public LineWriter (Writer out) {
	this (out, true) ;
    }

    public LineWriter (OutputStream out, boolean autoFlush) {
	this (new OutputStreamWriter(out), autoFlush) ;
    }

    public LineWriter (OutputStream out) {
	this (out, true) ;
    }

    public LineWriter () {
	this (System.err) ;
    }

    protected void finalize ()
    throws Throwable {
	if (lineBuffer.length() > 0)
	    println("...; LineWriter finalized") ;
	super.finalize() ;
    }

    /**
     * A LineWriter is enabled if it produces output.
     **/
    public boolean isEnabled () {
	return true ;
    }

    public String getLabel () {
	return lineLabel ;
    }

    public void setLabel (String label) {
	lineLabel = label ;
    }

    public void print (boolean value) {lineBuffer.append (value) ;}
    public void print (char value)    {lineBuffer.append (value) ;}
    public void print (char[] value)  {lineBuffer.append (value) ;}
    public void print (double value)  {lineBuffer.append (value) ;}
    public void print (float value)   {lineBuffer.append (value) ;}
    public void print (int value)     {lineBuffer.append (value) ;}
    public void print (long value)    {lineBuffer.append (value) ;}
    public void print (Object value)  {lineBuffer.append (value) ;}
    public void print (String value)  {lineBuffer.append (value) ;}

    public void println (boolean value) {print (value) ; println () ;}
    public void println (char value)    {print (value) ; println () ;}
    public void println (char[] value)  {print (value) ; println () ;}
    public void println (double value)  {print (value) ; println () ;}
    public void println (float value)   {print (value) ; println () ;}
    public void println (int value)     {print (value) ; println () ;}
    public void println (long value)    {print (value) ; println () ;}
    public void println (Object value)  {print (value) ; println () ;}
    public void println (String value)  {print (value) ; println () ;}

    public void println () {
	if (lineLabel != null)
	    lineBuffer.insert(0,": ").insert(0,lineLabel) ;
	lineBuffer.append(lineSeparator) ;

	// Ensure atomicity when accessing underlying output object:
	//
	synchronized (lock) {
	    write (lineBuffer.toString()) ;
	    flush () ;
	}

	lineBuffer.delete (0, lineBuffer.length()) ;
    }

    protected StringBuffer lineBuffer = new StringBuffer (80) ;
    protected String lineLabel = null ;
    protected String lineSeparator = System.getProperty ("line.separator") ;

}
