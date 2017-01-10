//                              -*- Mode: Java -*- 
// Lookahead.java --- 
// Author          : Bernie Lofaso
// Created On      : Tue Oct 20 07:37:56 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Tue Nov 17 10:06:57 1998
// Update Count    : 4
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class Lookahead extends Lang.AstNode {
    private String lookAhead;
    private boolean order[] = new boolean[0];

    public Lookahead setParms(String la) {
	lookAhead = la;
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public void print() {
	System.out.print("LOOKAHEAD(");
	System.out.print(lookAhead);
	System.out.print(") ");
    }


    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print("LOOKAHEAD(");
	pw.print(lookAhead);
	pw.print(") ");
    }
}
