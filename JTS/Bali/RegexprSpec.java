//                              -*- Mode: Java -*- 
// RegexprSpec.java --- 
// Author          : 
// Created On      : Thu Oct 22 14:36:42 1998
// Last Modified By: 
// Last Modified On: Mon Nov 09 12:46:10 1998
// Update Count    : 3
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;

public class RegexprSpec extends Lang.AstNode {
    private boolean order[] = { false, false, false };

    public RegexprSpec setParms(RegExpr re, Lang.AstOptNode block,
				Lang.AstOptNode optState) {
	arg = new Lang.AstNode[3];
	arg[0] = re;
	arg[1] = block;
	arg[2] = optState;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public void populateSymbolTable() {
	RegExpr re = (RegExpr) arg[0];

	re.populateSymbolTable();
    }
}
