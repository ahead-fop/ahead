//                              -*- Mode: Java -*- 
// OptLabel.java --- 
// Author          : 
// Created On      : Thu Oct 22 15:53:32 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Tue Nov 17 10:43:47 1998
// Update Count    : 7
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import Jakarta.util.Util;
import java.io.PrintWriter;

public class OptLabel extends Lang.AstNode {
    private boolean order[] = { true };
    private boolean privateLabel;

    public OptLabel setParms(boolean plFlag, Lang.AstToken label) {
	privateLabel = plFlag;
	tok = new Lang.AstToken[1];
	tok[0] = label;
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public void print() {
	if (privateLabel)
	    System.out.print("#");
	tok[0].print();		// print label
	System.out.print(": ");
    }

    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	if (privateLabel)
	    pw.print("#");
	tok[0].print(props);	// print label
	pw.print(": ");
    }

    public void populateSymbolTable() {
	SymbolTableEntry entry;
	SymbolTable st = SymbolTable.getInstance();
	String name;
	String error;
	Lang.AstToken thisTok;
	Lang.AstToken prevTok;

	// Create a symbol table entry for the token name
	name = tok[0].tokenName();
	entry = st.getSymbol(name);
	if (entry != null) {
	    thisTok = (Lang.AstToken) tok[0];
	    error = "Token name '" + name + "' in line " + thisTok.lineNum() +
		" collides with definition in line ";
	    prevTok = (Lang.AstToken) entry.getNode().tok[0];

	    Util.error(error + prevTok.lineNum());
	}
	else {
	    entry = new SymbolTableEntry(name, this);
	    st.setSymbol(name, entry);
	}
    }
}
