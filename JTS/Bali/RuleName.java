//                              -*- Mode: Java -*- 
// RuleName.java --- 
// Author          : 
// Created On      : Thu Oct 22 13:08:32 1998
// Last Modified By: 
// Last Modified On: Fri Nov 20 23:16:28 1998
// Update Count    : 14
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class RuleName extends Primitive {
    private boolean order[] = { true };
    private String varName;

    public RuleName setParms(Lang.AstToken n) {
	tok = new Lang.AstToken[1];
	tok[0] = n;
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public boolean isTerminal() { return(false); }

    public void setPrimitiveType(PrimitiveInfo pInfo) {
	int index;

	// "register" with the PrimitiveInfo object.
	index = pInfo.nonTerminal();

	// Cache our variable name
	varName = pInfo.variableName(index);
    }

    public void genParserSource(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print(" ");
	pw.print(varName);
	pw.print("=");
	pw.print(tok[0].tokenName());
	pw.print("()");
    }

    public int lineNum() { return(((Lang.AstToken) tok[0]).lineNum()); }
}
