//                              -*- Mode: Java -*- 
// RegexSpecListElem.java --- 
// Author          : 
// Created On      : Thu Oct 22 14:24:55 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Fri Nov 13 11:15:10 1998
// Update Count    : 4
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;

public class RegexSpecListElem extends Lang.AstListNode {
    public RegexSpecListElem setParms(Lang.AstToken bar, RegexprSpec spec) {
	if (bar != null) {
	    tok = new Lang.AstToken[1];
	    tok[0] = bar;
	}
	arg = new Lang.AstNode[1];
	arg[0] = spec;
	InitChildren();
	return(this);
    }

    public boolean CheckArgType() { return(true); }
    public String ArgType() { return(null); }
}
