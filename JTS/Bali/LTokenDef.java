//                              -*- Mode: Java -*- 
// LTokenDef.java --- 
// Author          : 
// Created On      : Thu Oct 22 15:44:46 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Mon Nov 16 14:59:01 1998
// Update Count    : 8
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class LTokenDef extends RegExpr {
    private boolean order[] = { false, false };

    public LTokenDef setParms(Lang.AstNode optLabel, CmpREChoice choice) {
	arg = new Lang.AstNode[2];
	arg[0] = optLabel;
	arg[1] = choice;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }

    public void print() {
	System.out.print("\t<");
	arg[0].print();		// print optional label
	arg[1].print();		// print CmpREChoice

	// NOTE: the closing ">" is printed in the choices (arg[1]).
    }

    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print("\t<");
	arg[0].print(props);	// print optional label
	arg[1].print(props);	// print CmpREChoice

	// NOTE: the closing ">" is printed in the choices (arg[1]).
    }

    public void populateSymbolTable() {
	OptLabel optLab = (OptLabel) arg[0].arg[0];

	if (optLab != null)
	    optLab.populateSymbolTable();
    }
}
