//                              -*- Mode: Java -*- 
// REString.java --- 
// Author          : 
// Created On      : Thu Oct 22 15:42:38 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Tue Nov 10 09:55:11 1998
// Update Count    : 5
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;

public class REString extends RegExpr {
    private boolean order[] = { true };

    public REString setParms(Lang.AstToken str) {
	tok = new Lang.AstToken[1];
	tok[0] = str;
	return(this);
    }

    public boolean[] printorder() { return(order); }

    public void populateSymbolTable() { }
}
