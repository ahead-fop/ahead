//                              -*- Mode: Java -*- 
// Options.java --- 
// Author          : Bernie Lofaso
// Created On      : Fri Oct 16 10:00:25 1998
// Last Modified By: 
// Last Modified On: Thu Aug 19 12:35:09 1999
// Update Count    : 8
// Status          : Under Development
// 
// $Locker:  $
// $Log: Options.java,v $
// Revision 1.3  2002-09-25 18:09:07  sarvela
// Corrected "options" printout for generated Bali and JavaCC code.
// Updated "JakBasic" and "Jak" in TELib to add Ant tasks.
//
// Revision 1.2  2002/02/22 18:20:08  sarvela
// Removed carriage returns.
//
// Revision 1.1.1.1  1999/08/19 19:26:58  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.2  1999/08/19 19:26:58  lofaso
// Corrected various problems with the Bali input format not accepting or not
// properly generating code relating to options, parser code, token manager
// declarations, and javacode.
//
// Revision 1.1.1.1  1999/02/18 16:15:25  lofaso
// Snapshot 2-18-99
//
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class Options extends Lang.AstNode {
    private String options;
    private boolean order[] = new boolean[0];

    public Options setParms(String opts) {
	options = opts;
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public void print() {
	System.out.print(options);
    }

    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.println ("options {") ;
	pw.println (options) ;
	pw.println ("} options") ;
    }

    public String toString() {
	return("options {\n" + options + "\n}\n");
    }

    // Used when concatenating options
    public String getOptions() {
	return(options);
    }
}
