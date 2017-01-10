//                              -*- Mode: Java -*- 
// UnNamedRule.java --- 
// Author          : Bernie Lofaso
// Created On      : Tue Oct 20 08:04:14 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Mon Nov 23 14:06:17 1998
// Update Count    : 20
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import Jakarta.util.Util;
import java.io.PrintWriter;

public class UnNamedRule extends Tail {
    private boolean order[] = { false };

    // Set by getPrimitiveInfo() and used by genParserSource()
    private PrimitiveInfo pInfo = null;

    public UnNamedRule setParms(RuleName rname) {
	arg = new Lang.AstNode[1];
	arg[0] = rname;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public boolean isList() { return(false); }

    public void populateSymbolTable() { }

    public void setSuperclass(String ruleName) {
	SymbolTable st = SymbolTable.getInstance();
	SymbolTableEntry entry;
	String nameInPattern;
	String error;

	// Mark the SymbolTableEntry of the RuleName in the clause pattern
	// such that the RuleName of this UnNamedRule is its superclass
	nameInPattern = arg[0].tok[0].tokenName();
	entry = st.getSymbol(nameInPattern);
	if (entry == null)
	    Util.fatalError("Non-terminal " + nameInPattern +
			    " referenced but not defined in line " +
			    ((RuleName) arg[0]).lineNum());

	if (entry.superclass() == null) {
	    entry.superclass(ruleName);
	    return;
	}

	// Else two superclasses are defined for this symbol
	error = "Class " + nameInPattern + " has two superclasses: " +
	    entry.superclass() + " and " + ruleName + " in line ";
	Util.error(error + ((RuleName) arg[0]).lineNum());

    }

    public void genBase(Lang.AstProperties props, SymbolTableEntry entry) { }

    public PrimitiveInfo getPrimitiveInfo() {
	if (pInfo == null) {
	    pInfo = new PrimitiveInfo();
	    ((Primitive) arg[0]).setPrimitiveType(pInfo);
	}
	return(pInfo);
    }

    public void genParserSource(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");
	String ruleName;

	// NamedRule->Clause->TailListElem->TailList->BaliProduction
	ruleName = up.up.up.up.tok[0].tokenName();

	pw.print("nt1=");
	arg[0].print(props);	// print RuleName
	pw.print("()\n\t{ return((Lang." + ruleName + ") nt1); }\n");

	// Free for GC
	pInfo = null;
    }
}
