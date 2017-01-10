//                              -*- Mode: Java -*- 
// Clause.java --- 
// Author          : Bernie Lofaso
// Created On      : Tue Oct 20 07:31:01 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Mon Nov 16 16:53:04 1998
// Update Count    : 6
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;

public class Clause extends Lang.AstNode {
    private boolean order[] = { false, false };

    public Clause setParms(Lang.AstNode lookahead, Tail tail) {
	arg = new Lang.AstNode[2];
	arg[0] = lookahead;
	arg[1] = tail;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public void populateSymbolTable() {
	Tail tail = (Tail) arg[1];

	tail.populateSymbolTable();
    }

    public void setSuperclass(String ruleName) {
	Tail tail = (Tail) arg[1];

	tail.setSuperclass(ruleName);
    }

    public void genParserSource(Lang.AstProperties props) {
	Tail tail = (Tail) arg[1];
	Lookahead la;

	// optional lookahead
	la = (Lookahead) arg[0].arg[0];
	if (la != null)
	    la.print(props);

	tail.genParserSource(props);
    }
}
