package Bali;

import JakBasic.Lang;

public class RequireRuleListElem extends Lang.AstListNode {

    public RequireRuleListElem setParms(Lang.AstToken sep, RequireRule rule) {
	if (sep != null) {
	    tok = new Lang.AstToken[1];
	    tok[0] = sep;
	}
	arg = new Lang.AstNode[1];
	arg[0] = rule;
	InitChildren();
	return(this);
    }

    public boolean CheckArgType() { return(true); }
    public String ArgType() { return(null); }

}
