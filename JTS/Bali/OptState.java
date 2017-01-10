//                              -*- Mode: Java -*- 
// OptState.java --- 
// Author          : 
// Created On      : Thu Oct 22 14:42:23 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Tue Nov 10 09:51:50 1998
// Update Count    : 3
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class OptState extends Lang.AstNode {
    private boolean order[] = { true };

    public OptState setParms(Lang.AstToken state) {
	tok = new Lang.AstToken[1];
	tok[0] = state;
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public void print() {
	System.out.print("\t:");
	tok[0].print();
    }

    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print("\t:");
	tok[0].print(props);
    }
}
