//                              -*- Mode: Java -*- 
// RegExprProd.java --- 
// Author          : Bernie Lofaso
// Created On      : Mon Oct 19 09:55:01 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Tue Nov 17 10:26:36 1998
// Update Count    : 15
// Status          : Under Development
// 
// $Locker:  $
// $Log: RegExprProd.java,v $
// Revision 1.2  2002-02-22 18:20:10  sarvela
// Removed carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:28  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:27  lofaso
// Snapshot 2-18-99
//
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class RegExprProd extends Production {
    private boolean[] order = { false };
    public String states;	// opt. state list
    public boolean caseFlag;
    public int kind;

    public RegExprProd setParms(String st, int k, boolean cf,
				RegexSpecList rslist) {
	states = st;
	kind = k;
	caseFlag = cf;
	arg = new Lang.AstNode[1];
	arg[0] = rslist;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }

    public void print() {
	System.out.println();
	if (states != null)
	    System.out.print(states);
	switch (kind) {
	case 0:
	    System.out.print("TOKEN");
	    break;
	case 1:
	    System.out.print("SPECIAL_TOKEN");
	    break;
	case 2:
	    System.out.print("SKIP");
	    break;
	case 3:
	    System.out.print("MORE");
	    break;
	}
	if (caseFlag)
	    System.out.print("[ IGNORE_CASE ]");
	System.out.print(" :\n{");
	arg[0].print();
	System.out.print("\n}\n");
    }

    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.println();
	if (states != null)
	    pw.print(states);
	switch (kind) {
	case 0:
	    pw.print("TOKEN");
	    break;
	case 1:
	    pw.print("SPECIAL_TOKEN");
	    break;
	case 2:
	    pw.print("SKIP");
	    break;
	case 3:
	    pw.print("MORE");
	    break;
	}
	if (caseFlag)
	    pw.print("[ IGNORE_CASE ]");
	pw.print(" :\n{");
	arg[0].print(props);
	pw.print("\n}\n");
    }

    public void populateSymbolTable() {
	RegexSpecList rslist = (RegexSpecList) arg[0];

	rslist.populateSymbolTable();
    }

    public void genParserSource(Lang.AstProperties props) {
	print(props);
    }
}
