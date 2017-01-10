//                              -*- Mode: Java -*- 
// SymbolTable.java --- 
// Author          : Bernie Lofaso
// Created On      : Thu Oct 29 14:45:23 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Fri Nov 13 12:47:37 1998
// Update Count    : 13
// Status          : Under Development
// 
// $Locker:  $
// $Log: SymbolTable.java,v $
// Revision 1.2  2002-02-22 18:20:11  sarvela
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
import Jakarta.util.Util;
import java.util.Enumeration;
import java.util.Hashtable;

public class SymbolTable {
    // Hold singleton instance
    private static SymbolTable instance = null;

    // A special "symbol"
    private static String start = "";

    private Hashtable htab = new Hashtable();

    private SymbolTable() { }

    // Retrieves singleton instance
    public static SymbolTable getInstance() {
	if (instance == null)
	    instance = new SymbolTable();
	return(instance);
    }

    public static String getStartSymbol() { return(start); }
    public static void setStartSymbol(String s) {
	if (start != "")
	    Util.warning("Warning: StartSymbol set more than once");
	start = s;
    }

    public SymbolTableEntry getSymbol(String name) {
	return((SymbolTableEntry) htab.get(name));
    }

    public void setSymbol(String name, SymbolTableEntry entry) {
	htab.put(name, entry);
    }


    //**************************************************
    // For scanning SymbolTable.
    //**************************************************
    public Enumeration symbols() { return(htab.elements()); }

    //**************************************************
    // Scan through all of the entries in the symbol table. If the node
    // is associated with a BaliProduction or a NamedRule, then we call
    // the genBase() method of those classes to generate code for Base.java.
    // Entries which are associated with OptLabel nodes need not generate
    // code for Base.java.
    //**************************************************
    public void genBase(Lang.AstProperties props) {
	Enumeration scanPtr;
	SymbolTableEntry entry;
	Lang.AstNode node;

	scanPtr = htab.elements();
	while (scanPtr.hasMoreElements()) {
	    entry = (SymbolTableEntry) scanPtr.nextElement();
	    node = entry.getNode();
	    if (node instanceof BaliProduction)
		((BaliProduction) node).genBase(props, entry);
	    else if (node instanceof NamedRule)
		((NamedRule) node).genBase(props, entry);
	}
    }
}

    
