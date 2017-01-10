//                              -*- Mode: Java -*- 
// BaliProduction.java --- 
// Author          : Bernie Lofaso
// Created On      : Fri Oct 16 15:44:35 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Mon Nov 23 10:59:35 1998
// Update Count    : 40
// Status          : Under Development
// 
// $Locker:  $
// $Log: BaliProduction.java,v $
// Revision 1.3  2002-07-16 16:15:16  sarvela
// Renamed interface "Token" to be "AstTokenInterface".  This is so that it
// doesn't clash with JavaCC's use of "Token" as we bootstrap to layers
// implemented as directory hierarchies.
//
// Revision 1.2  2002/02/22 18:20:05  sarvela
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
import java.io.PrintWriter;
import java.util.Properties;

public class BaliProduction extends Production {
    private boolean order[] = { true, false };

    public BaliProduction setParms(Lang.AstToken id, TailList tlist) {
	tok = new Lang.AstTokenInterface[1];
	tok[0] = id;
	arg = new Lang.AstNode[1];
	arg[0] = tlist;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public void print() {
	tok[0].print();		// print rule name
	System.out.print("\n\t:");
	arg[0].print();		// print TailList
	System.out.print("\n\t;\n");
    }

    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	tok[0].print(props);	// print rule name
	pw.print("\n\t:");
	arg[0].print(props);	// print TailList
	pw.print("\n\t;\n");
    }


    public void populateSymbolTable() {
	TailList tlist = (TailList) arg[0];
	SymbolTable st = SymbolTable.getInstance();
	SymbolTableEntry entry;
	String name;
	String error;
	Lang.AstToken thisTok;
	Lang.AstToken prevTok;

	// Create a symbol table entry for the rule name
	name = tok[0].tokenName();
	entry = st.getSymbol(name);
	if (entry != null) {
	    thisTok = (Lang.AstToken) tok[0];
	    error = "Rule name '" + name + "' in line " + thisTok.lineNum() +
		" collides with definition in line ";
	    prevTok = (Lang.AstToken) entry.getNode().tok[0];

	    Util.error(error + prevTok.lineNum());
	}
	else {
	    entry = new SymbolTableEntry(name, this);
	    st.setSymbol(name, entry);
	}

	// Call populateSymbolTable() on the TailList of this BaliProduction
	tlist.populateSymbolTable();
    }

    public void setSuperclass() {
	((TailList) arg[0]).setSuperclass(tok[0].tokenName());
    }

    private static final String NL = "\n";

    // "Arguments" to this template are: ClassName, Superclass, FirstSubclass,
    // and ClassCode.
    private static String tmpl =
    NL +
    "    // Non-leaf virtual class (gen'd by BaliProduction)" + NL +
    "    static public class $(ClassName) extends Lang.$(Superclass) {" + NL +
    "        $(RootDecl)" + NL +
    "        static private int first_subclass = $(FirstSubclass);" + NL +
    "        static private int class_code = $(ClassCode);" + NL +
    NL +
    "        public boolean SyntaxCheck() { return(false); }" + NL +
    "        public boolean[] printorder() { return(null); }" + NL +
    "        public int firstSubclass() { return(first_subclass); }" + NL +
    "        public int classCode() { return(class_code); }" + NL +
    "    }" + NL;

    private static CodeTemplate baseCode = new CodeTemplate(tmpl);

    public void genBase(Lang.AstProperties props, SymbolTableEntry entry) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");
	Properties plist = new Properties();
	String className = entry.getName();
	Tail tail;

	if (entry.isListClass()) {
	    // BaliProduction->TailList->TailListElem->Clause->Tail
	    tail = (Tail) arg[0].arg[0].arg[0].arg[1];
	    tail.genBase(props, entry);
	    return;
	}

	// Set root decl if necessary
	if (className.compareTo(SymbolTable.getStartSymbol()) == 0)
	    plist.put("RootDecl", "        public static " + className + " root;");

	// BaliProduction contains only NamedRule's or UnNamedRule's
	plist.put("ClassName", className);
	plist.put("Superclass", entry.superclass());
	plist.put("FirstSubclass", Integer.toString(entry.firstSubclass()));
	plist.put("ClassCode", Integer.toString(entry.classCode()));
	baseCode.genCode(pw, plist);
    }

    public void genParserSource(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");
	TailList tlist = (TailList) arg[0];
	Tail tail;	// used to see if rule is a list type
	String ruleName;
	int termCount;
	int nonTermCount;
	int i;

	// Generate rule header
	ruleName = tok[0].tokenName();
	pw.print("Lang." + ruleName + " " + ruleName + "() :\n{\n");

	// Generate local variable if rule is a list rule
	tail = (Tail) tlist.arg[0].arg[0].arg[1];
	if (tail.isList()) {
	    pw.print("\tLang." + ruleName + " list = new Lang." + ruleName +
		     "();\n");
	}

	// Mixture of NamedRule's and UnNamedRule's
	nonTermCount = tlist.nonTerminalCount();
	if (nonTermCount != 0) {
	    pw.print("\tLang.AstNode");
	    for (i=0; i < nonTermCount; i++) {
		if (i > 0)
		    pw.print(", nt");
		else
		    pw.print(" nt");
		pw.print(i+1);
		pw.print("=null");
	    }
	    pw.print(";\n");

	    if (tlist.hasOptNonTerminals()) {
		pw.print("\tLang.AstOptNode");
		for (i=0; i < nonTermCount; i++) {
		    if (i > 0)
			pw.print(", ont");
		    else
			pw.print(" ont");
		    pw.print(i+1);
		    pw.print("=null");
		}
		pw.print(";\n");
	    }
	}

	termCount = tlist.terminalCount();
	if (termCount != 0) {
	    pw.print("\tToken");
	    for (i=0; i < termCount; i++) {
		if (i > 0)
		    pw.print(", t");
		else
		    pw.print(" t");
		pw.print(i+1);
		pw.print("=null");
	    }
	    pw.print(";\n");

	    if (tlist.hasOptTerminals()) {
		pw.print("\tLang.AstOptToken");
		for (i=0; i < termCount; i++) {
		    if (i > 0)
			pw.print(", ot");
		    else
			pw.print(" ot");
		    pw.print(i+1);
		    pw.print("=null");
		}
		pw.print(";\n");
	    }
	}

	pw.print("\tboolean justNT;\n");

	pw.print("}\n{\n");

	// Generate rule clauses
	tlist.genParserSource(props);
	pw.print("\n}\n\n");
    }
}
