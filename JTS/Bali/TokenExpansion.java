//                              -*- Mode: Java -*- 
// TokenExpansion.java --- 
// Author          : 
// Created On      : Thu Oct 22 15:49:49 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Tue Nov 10 10:39:43 1998
// Update Count    : 4
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class TokenExpansion extends RegExpr {
    private boolean order[] = { true };

    public TokenExpansion setParms(Lang.AstToken name) {
	tok = new Lang.AstToken[1];
	tok[0] = name;
	return(this);
    }

    public boolean[] printorder() { return(order); }

    public void print() {
	System.out.print("<");
	tok[0].print();
	System.out.print(">");
    }

    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print("<");
	tok[0].print(props);
	pw.print(">");
    }

    public void populateSymbolTable() { }
}
