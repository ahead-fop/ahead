//                              -*- Mode: Java -*- 
// TailList.java --- 
// Author          : Bernie Lofaso
// Created On      : Fri Oct 16 15:55:23 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Mon Nov 23 10:39:45 1998
// Update Count    : 24
// Status          : Under Development
// 
// $Locker:  $
// $Log: TailList.java,v $
// Revision 1.2  2002-02-22 18:20:12  sarvela
// Removed carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:28  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:28  lofaso
// Snapshot 2-18-99
//
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;
import Jakarta.util.Util;

public class TailList extends Lang.AstList {
    public boolean CheckElemType(Lang.AstNode l) { return(true); }
    public String ElemType() { return(null); }

    // These are set by terminalCount(), nonTerminalCount(),
    // hasOptTerminals(), and hasOptNonTerminals()
    private PrimitiveInfo pInfo = null;
    private int termCount = 0;
    private int nonTermCount = 0;
    private boolean hasOptTerm = false;
    private boolean hasOptNonTerm = false;

    public void populateSymbolTable() {
	Lang.AstNode lnode;
	Clause clause;

	lnode = arg[0];
	while (lnode != null) {
	    clause = (Clause) lnode.arg[0];
	    clause.populateSymbolTable();

	    lnode = lnode.right;
	}
    }

    public void setSuperclass(String ruleName) {
	Lang.AstNode lnode;
	Clause clause;

	lnode = arg[0];
	while (lnode != null) {
	    clause = (Clause) lnode.arg[0];
	    clause.setSuperclass(ruleName);

	    lnode = lnode.right;
	}
    }



    //**************************************************
    // The following two methods (terminalCount() and nonTerminalCount())
    // return the maximum number of terminals or non-terminals, respectively,
    // used in any of the Tail's associated with this TailList.
    //**************************************************
    public int terminalCount() {
	if (pInfo == null)
	    getCounts();
	return(termCount);
    }

    public int nonTerminalCount() {
	if (pInfo == null)
	    getCounts();
	return(nonTermCount);
    }

    public boolean hasOptTerminals() {
	if (pInfo == null)
	    getCounts();
	return(hasOptTerm);
    }

    public boolean hasOptNonTerminals() {
	if (pInfo == null)
	    getCounts();
	return(hasOptNonTerm);
    }

    private void getCounts() {
	int t = 0;
	int nt = 0;
	int varCnt;
	Tail tail;
	Lang.AstNode lnode;

	termCount = 0;
	nonTermCount = 0;
	lnode = arg[0];
	while (lnode != null) {
	    tail = (Tail) lnode.arg[0].arg[1];
	    pInfo = tail.getPrimitiveInfo();
	    t = pInfo.terminalCount();
	    if (t > termCount)
		termCount = t;
	    nt = pInfo.nonTerminalCount();
	    if (nt > nonTermCount)
		nonTermCount = nt;

	    varCnt = pInfo.variableCount();
	    if (! hasOptTerm) {
		for (int i=0; i < varCnt; i++) {
		    if (pInfo.isOptional(i) && pInfo.isTerminal(i)) {
			hasOptTerm = true;
			break;
		    }
		}
	    }
	    if (! hasOptNonTerm) {
		for (int i=0; i < varCnt; i++) {
		    if (pInfo.isOptional(i) && !pInfo.isTerminal(i)) {
			hasOptNonTerm = true;
			break;
		    }
		}
	    }

	    lnode = lnode.right;
	}
    }


    //**************************************************
    // As we generate source for the parser, we examine the Tail associated
    // with each Clause. If the Tail is an UnNamedRule, then we search for
    // NamedRule's that share a common prefix, as specified by the
    // RuleName of the UnNamedRule. If a prefix is found, we do not
    // generate code for the UnNamedRule (the NamedRule containing the
    // prefix will do that). An UnNamedRule can have at most one NamedRule
    // with a matching prefix.
    //**************************************************
    public void genParserSource(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");
	Lang.AstNode lnode;
	Lang.AstNode prefixScan;	// scans for NamedRule with prefix
	int prefixCount;
	String prefixName;
	Tail tail;
	Clause clause;
	boolean first = true;

	lnode = arg[0];
	while (lnode != null) {
	    clause = (Clause) lnode.arg[0];
	    tail = (Tail) clause.arg[1];
	    if (tail instanceof UnNamedRule) {
		// Tail->RuleName
		prefixName = tail.arg[0].tok[0].tokenName();

		prefixScan = lnode.right;
		prefixCount = 0;
		while (prefixScan != null) {
		    tail = (Tail) prefixScan.arg[0].arg[1];
		    if (tail instanceof NamedRule) {
			if (((NamedRule) tail).hasNTPrefix(prefixName))
			    prefixCount++;
		    }

		    prefixScan = prefixScan.right;
		}
		if (prefixCount == 0) {
		    if (first) {
			pw.print("\t");
			first = false;
		    }
		    else
			pw.print("|\t");
		    clause.genParserSource(props);
		}
		else if (prefixCount > 1)
		    Util.error("Error: Rule " + up.tok[0].tokenName() +
			       " has multiple NamedRules with common prefix");
	    }
	    else {	// tail is not an UnNamedRule, therefore generate
		if (first) {
		    pw.print("\t");
		    first = false;
		}
		else
		    pw.print("|\t");
		clause.genParserSource(props);
	    }

	    lnode = lnode.right;
	}

	// "Free" this for GC since we don't need it anymore.
	pInfo = null;
    }
}
