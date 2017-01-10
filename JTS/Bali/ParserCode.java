//                              -*- Mode: Java -*- 
// ParserCode.java --- 
// Author          : Bernie Lofaso
// Created On      : Fri Oct 16 15:09:56 1998
// Last Modified By: 
// Last Modified On: Thu Aug 19 12:20:54 1999
// Update Count    : 6
// Status          : Under Development
// 
// $Locker:  $
// $Log: ParserCode.java,v $
// Revision 1.3  2003-03-21 17:58:37  sarvela
// Fixed bug in "ParserCode.print" where the "code" brackets weren't printed;
// added new tool "extractToken" to "TELib".
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

public class ParserCode extends Lang.AstNode {
    private String parserCode;
    private boolean order[] = new boolean[0];

    public ParserCode setParms(String pcode) {
	parserCode = pcode;
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public void print() {
	System.out.print(parserCode);
    }

    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.println () ;
	pw.println ("code {") ;
	pw.print (parserCode);
	pw.println () ;
	pw.println ("} code") ;
	pw.println () ;
    }

    public String toString() {
	return (parserCode) ;
    }

    // Used when concatenating parser code
    public String getParserCode() {
	return(parserCode);
    }

}
