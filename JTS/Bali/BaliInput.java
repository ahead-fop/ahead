package Bali;

import JakBasic.Lang;

import Jakarta.loader.Loader ;

import Jakarta.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.net.URL ;

import java.util.Enumeration;
import java.util.Properties;

public class BaliInput extends Lang.AstNode {
    private boolean order[] = { false, false, false };

    public BaliInput setParms(Lang.AstOptNode opts, Lang.AstOptNode pcode,
			      Lang.AstOptNode rules) {
	arg = new Lang.AstNode[3];
	arg[0] = opts;
	arg[1] = pcode;
	arg[2] = rules;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public void populateSymbolTable() {
	Rules rules;
	Enumeration scanPtr;
	SymbolTableEntry entry;

	rules = (Rules) arg[2].arg[0];
	if (rules != null) {
	    rules.populateSymbolTable();
	    rules.setSuperclass();
	}

	// At this point, all rules with null superclass pointers
	// should point to the default, Lang.AstNode.
	scanPtr = SymbolTable.getInstance().symbols();
	while (scanPtr.hasMoreElements()) {
	    entry = (SymbolTableEntry) scanPtr.nextElement();
	    if (entry.superclass() == null)
		entry.superclass("AstNode");
	}
    }

    public void typecheck() {}

    public void genParserSource (Lang.AstProperties props) {
	Options opts = (Options) arg[0].arg[0];
	ParserCode pcode = (ParserCode) arg[1].arg[0];
	Rules rules = (Rules) arg[2].arg[0];
	String languageName = (String) props.getProperty("LanguageName");
	String fname;
	PrintWriter jccSrc;
	CodeTemplate template;
	Properties plist = new Properties();	// used by CodeTemplate
	File genLangDir = (File) props.getProperty ("LanguageDirectory") ;
	Loader loader = (Loader) props.getProperty ("ResourceLoader") ;

	System.out.println("Generating JavaCC source ...");

	// Set 'output' property
	fname = languageName + ".jj";
	jccSrc = Util.backedOutput (new File (genLangDir, fname)) ;
	props.setProperty("output", jccSrc);

	// Set 'LanguageName' property
	plist.put("LanguageName", languageName);

	// Set 'Options' and 'ParserCode' properties if necessary
	if (opts != null)
	    plist.put("Options", opts.toString());
	if (pcode != null)
	    plist.put("ParserCode", pcode.toString());

	// Set 'StartSymbol' property if available:
	//
	String startSymbol = SymbolTable.getStartSymbol () ;
	if (startSymbol != null && startSymbol.length () > 0)
	    plist.put ("StartSymbol", startSymbol) ;

	// Generate first half of .jj file using JavaCC.tmpl
	URL source = loader.getResource ("Bali/JavaCC.tmpl") ;
	try {
	    template = new CodeTemplate (source) ;
	} catch (IOException e) {
	    Util.fatalError("Can't open 'JavaCC.tmpl'", e);
	    template = null;
	}
	template.genCode (jccSrc, plist);

	// Generate second half of .jj file
	if (rules != null)
	    rules.genParserSource(props);

	jccSrc.flush();
	jccSrc.close();
    }
}
