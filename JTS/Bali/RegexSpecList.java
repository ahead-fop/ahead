//                              -*- Mode: Java -*- 
// RegexSpecList.java --- 
// Author          : 
// Created On      : Thu Oct 22 14:23:10 1998
// Last Modified By: 
// Last Modified On: Mon Nov 09 12:45:45 1998
// Update Count    : 5
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;

public class RegexSpecList extends Lang.AstList {
    public boolean CheckElemType(Lang.AstNode l) { return(true); }
    public String ElemType() { return(null); }

    public void populateSymbolTable() {
	Lang.AstNode lnode;
	RegexprSpec spec;

	lnode = arg[0];
	while (lnode != null) {
	    spec = (RegexprSpec) lnode.arg[0];
	    spec.populateSymbolTable();

	    lnode = lnode.right;
	}
    }
}
