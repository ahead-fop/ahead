//                              -*- Mode: Java -*- 
// PrimString.java --- 
// Author          : 
// Created On      : Thu Oct 22 16:08:07 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Wed Feb 10 15:23:14 1999
// Update Count    : 11
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class PrimString extends JavaCCToken {
    private boolean order[] = { true };
    private String varName;

    public PrimString setParms(Lang.AstToken token) {
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
	pw.print("=");
	pw.print(tok[0].tokenName());
    }
}
