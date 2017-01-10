//                              -*- Mode: Java -*- 
// Tail.java --- 
// Author          : Bernie Lofaso
// Created On      : Tue Oct 20 07:41:28 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Fri Nov 20 14:20:41 1998
// Update Count    : 11
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;

public abstract class Tail extends Lang.AstNode {
    private boolean order[] = new boolean[0];

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public abstract boolean isList();
    public abstract void populateSymbolTable();
    public abstract void setSuperclass(String ruleName);
    public abstract void genBase(Lang.AstProperties props,
				 SymbolTableEntry entry);
    public abstract void genParserSource(Lang.AstProperties props);
    public abstract PrimitiveInfo getPrimitiveInfo();
}
