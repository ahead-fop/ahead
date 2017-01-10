package Bali;

import JakBasic.Lang;

public class RequireRuleList extends Lang.AstList {

    public boolean CheckElemType(Lang.AstNode l) { return(true); }

    public String ElemType() { return(null); }

    public void populateSymbolTable() {

	RequireRule rule ;
	for (Lang.AstNode lnode = arg[0]; lnode != null; lnode = lnode.right) {
	    rule = (RequireRule) lnode.arg[0];
	    rule.populateSymbolTable();
	}
    }

}
