//                              -*- Mode: Java -*- 
// RegExpr.java --- 
// Author          : 
// Created On      : Thu Oct 22 15:40:48 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Tue Nov 10 09:55:25 1998
// Update Count    : 7
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;

public abstract class RegExpr extends Lang.AstNode {
    public boolean SyntaxCheck() { return(true); }

    public abstract void populateSymbolTable();
}
