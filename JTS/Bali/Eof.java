//                              -*- Mode: Java -*- 
// Eof.java --- 
// Author          : 
// Created On      : Thu Oct 22 15:51:46 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Tue Nov 10 10:41:36 1998
// Update Count    : 4
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class Eof extends RegExpr {
    private boolean order[] = new boolean[0];

    public boolean[] printorder() { return(order); }

    public void print() {
	System.out.print("<EOF>");
    }

    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print("<EOF>");
    }

    public void populateSymbolTable() { }
}
