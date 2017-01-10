package Bali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream ;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader ;
import java.io.StringReader;

import java.net.URL ;

import java.util.Properties;

public class CodeTemplate {
    private BufferedReader input;

    public CodeTemplate (BufferedReader reader) {
	input = reader ;
	try {
	    input.mark(4096) ;
	}
	catch (IOException e) {}
    }

    public CodeTemplate (Reader reader) {
	this (new BufferedReader (reader)) ;
    }

    public CodeTemplate (InputStream stream) {
	this (new InputStreamReader (stream)) ;
    }

    public CodeTemplate (File file) throws IOException {
	this (new FileInputStream (file)) ;
    }

    public CodeTemplate (String template) {
	this (new StringReader (template)) ;
    }

    public CodeTemplate (URL url) throws IOException {
	this (url.openStream ()) ;
    }

    public void genCode(PrintWriter output, Properties plist) {
	String line;
	int vstart, vend;	// indicies delimiting var reference
	String key;
	String value;

	try {
	    try {
		input.reset();
	    } catch (IOException e) {}
	    line = input.readLine();
	    while (line != null) {
		while ((vstart = line.indexOf('$')) != -1) {
		    // possible variable substitution

		    if ((vstart < (line.length() - 1)) &&
			(line.charAt(vstart+1) != '(')) {
			output.print(line.substring(0, vstart+1));
			line = line.substring(vstart+1);
			continue;
		    }

		    vend = line.indexOf(')', vstart);
		    if (vend == -1)
			break;

		    // variable found
		    output.print(line.substring(0, vstart));
		    key = line.substring(vstart+2, vend);
		    line = line.substring(vend+1);
		    value = plist.getProperty(key);
		    if (value != null)
			output.print(value);
		}

		// Output remainder of line
		output.println(line);

		// Get next line
		line = input.readLine();
	    }
	}
	catch (IOException e) {
	    System.err.println("CodeTemplate.genCode: "+e);
	    return;
	}
    }
}
