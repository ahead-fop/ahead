package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class RequireProduction extends Production {

    private boolean[] order = { false };

    public RequireProduction setParms (RequireRuleList b) {
	arg = new Lang.AstNode[1];
	arg[0] = b;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }

    public void populateSymbolTable() { }

    public void genParserSource (Lang.AstProperties props) { }

}
