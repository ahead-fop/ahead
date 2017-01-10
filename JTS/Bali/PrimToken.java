//                              -*- Mode: Java -*- 
// PrimToken.java --- 
// Author          : 
// Created On      : Thu Oct 22 12:59:20 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Wed Feb 10 15:22:52 1999
// Update Count    : 13
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class PrimToken extends JavaCCToken {
    private boolean order[] = { true };
    private String varName;

    public PrimToken setParms(Lang.AstToken token) {
	tok = new Lang.AstToken[1];
	tok[0] = token;
	return(this);
    }

    public boolean[] printorder() { return(order); }

    public boolean isTerminal() { return(true); }

    public void setPrimitiveType(PrimitiveInfo pInfo) {
	int index;

	// "register" with the PrimitiveInfo object.
	index = pInfo.terminal();

	// Cache our variable name
	varName = pInfo.variableName(index);
    }

    public void genParserSource(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print(" ");
	pw.print(varName);
	pw.print("=<");
	pw.print(tok[0].tokenName());
	pw.print(">");
    }
}
