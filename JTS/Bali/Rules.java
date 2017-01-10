//                              -*- Mode: Java -*- 
// Rules.java --- 
// Author          : Bernie Lofaso
// Created On      : Fri Oct 16 14:38:13 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Wed Nov 11 10:19:22 1998
// Update Count    : 8
// Status          : Under Development
// 
// $Locker:  $
// $Log: Rules.java,v $
// Revision 1.2  2002-02-22 18:20:11  sarvela
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

public class Rules extends Lang.AstList {
    public boolean CheckElemType(Lang.AstNode l) { return(true); }
    public String ElemType() { return(null); }

    public void populateSymbolTable() {
	Lang.AstNode lnode;
	Production prod;

	lnode = arg[0];
	while (lnode != null) {
	    prod = (Production) lnode.arg[0];
	    prod.populateSymbolTable();

	    lnode = lnode.right;
	}
    }

    public void genParserSource(Lang.AstProperties props) {
	Lang.AstNode lnode;
	Production prod;

	lnode = arg[0];
	while (lnode != null) {
	    prod = (Production) lnode.arg[0];
	    prod.genParserSource(props);

	    lnode = lnode.right;
	}
    }

    public void setSuperclass() {	
	Lang.AstNode lnode;
	Production prod;

	lnode = arg[0];
	while (lnode != null) {
	    prod = (Production) lnode.arg[0];
	    if (prod instanceof BaliProduction)
		((BaliProduction) prod).setSuperclass();

	    lnode = lnode.right;
	}
    }
}
