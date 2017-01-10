//                              -*- Mode: Java -*- 
// NamedRule.java --- 
// Author          : Bernie Lofaso
// Created On      : Tue Oct 20 08:04:14 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Mon Nov 23 10:24:52 1998
// Update Count    : 45
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import Jakarta.util.Util;
import java.io.PrintWriter;
import java.util.Properties;

public class NamedRule extends Tail {
    private boolean order[] = { false, true };
    private SymbolTableEntry entry = null;

    // Set by getPrimitiveInfo() and used by genParserSource()
    private PrimitiveInfo pInfo = null;

    // See description of hasNTPrefix() below for explanation
    private boolean prefixFound = false;

    public NamedRule setParms(Pattern pat, Lang.AstToken id) {
	arg = new Lang.AstNode[1];
	arg[0] = pat;
	tok = new Lang.AstToken[1];
	tok[0] = id;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public boolean isList() { return(false); }

    public void print() {
	arg[0].print();		// print pattern
	System.out.print("\t::");
	tok[0].print();		// print label name
    }


    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	arg[0].print(props);	// print pattern
	pw.print("\t::");
	tok[0].print(props);	// print label name
    }


    public void populateSymbolTable() {
	SymbolTable st = SymbolTable.getInstance();
	String name;
	String error;
	Lang.AstToken thisTok;
	Lang.AstToken prevTok;

	// Create a symbol table entry for the "Label" name
	name = tok[0].tokenName();
	entry = st.getSymbol(name);
	if (entry != null) {
	    thisTok = (Lang.AstToken) tok[0];
	    error = "Rule label name '" + name + "' in line " +
		thisTok.lineNum() + " collides with definition in line ";
	    prevTok = (Lang.AstToken) entry.getNode().tok[0];

	    Util.error(error + prevTok.lineNum());
	}
	else {
	    entry = new SymbolTableEntry(name, this);
	    st.setSymbol(name, entry);
	}
    }

    public void setSuperclass(String ruleName) {
	if (entry == null)
	    Util.fatalError("setSuperclass() called before populateSymbolTable()");
	if (entry.superclass() == null) {
	    entry.superclass(ruleName);
	    return;
	}

	// Else two superclasses are defined for this symbol
	String name = tok[0].tokenName();
	String error = "Class " + name + " has two superclasses: " +
	    entry.superclass() + " and " + ruleName + " in line ";
	Util.error(error + ((Lang.AstToken) tok[0]).lineNum());
    }

    private static final String NL = "\n";

    // "Arguments" to this template are: ClassName, SName, RootDecl,
    // OrderArrayInit, FirstSubclass, ClassCode, ParmList and ArgAssigns.
    private static String tmpl =
    NL +
    "    // NamedRule" + NL +
    "    static public class $(ClassName) extends Lang.$(SName) {" + NL +
    "        $(RootDecl)" + NL +
    "        boolean order[] = { $(OrderArrayInit) };" + NL +
    "        static private int first_subclass = $(FirstSubclass);" + NL +
    "        static private int class_code = $(ClassCode);" + NL +
    NL +
    "        public Lang.$(ClassName) setParms($(ParmList)) {" + NL +
    "            $(ArgAssigns);" + NL +
    "            InitChildren();" + NL +
    "            return((Lang.$(ClassName)) this);" + NL +
    "        }" + NL +
    NL +
    "        public boolean[] printorder() { return(order); }" + NL +
    "        public boolean SyntaxCheck() { return(true); }" + NL +
    "        public int firstSubclass() { return(first_subclass); }" + NL +
    "        public int classCode() { return(class_code); }" + NL +
    "    }" + NL;

    private static CodeTemplate baseCode = new CodeTemplate(tmpl);

    public void genBase(Lang.AstProperties props, SymbolTableEntry entry) {
	PrintWriter output;
	Properties plist = new Properties();
	String className = entry.getName();
	String lname = (String) props.getProperty("LanguageName");
	boolean[] order;
	String tmp;
	int i, arg_cnt, tok_cnt;

	output = (PrintWriter) props.getProperty("output");
	plist.put("ClassName", className);

	// Set superclass property
	plist.put("SName", entry.superclass());

	// Set root decl if necessary
	if (className.compareTo(SymbolTable.getStartSymbol()) == 0)
	    plist.put("RootDecl", "        public static " + className + " root;");

	// Set OrderArrayInit property
	order = ((Pattern) arg[0]).terminalList();
	tmp = order[0] ? "true" : "false";
	for (i=1; i < order.length; i++)
	    tmp += order[i] ? ", true" : ", false";
	plist.put("OrderArrayInit", tmp);

	// Set the ParmList property
	if (order[0]) {
	    tmp = "Lang.AstTokenInterface _arg0";
	    tok_cnt = 1;
	    arg_cnt = 0;
	}
	else {
	    tmp = "Lang.AstNode _arg0";
	    tok_cnt = 0;
	    arg_cnt = 1;
	}
	for (i=1; i < order.length; i++) {
	    if (order[i]) {
		tmp += ", Lang.AstTokenInterface _arg" + i;
		tok_cnt++;
	    }
	    else {
		tmp += ", Lang.AstNode _arg" + i;
		arg_cnt++;
	    }
	}
	plist.put("ParmList", tmp);

	// Set ArgAssigns property
	if (arg_cnt == 0)
	    arg_cnt = 1;
	tmp = "arg = new Lang.AstNode[" + arg_cnt + "];";
	if (tok_cnt > 0)
	    tmp += "\n\t\ttok = new Lang.AstTokenInterface[" + tok_cnt + "];";

	tok_cnt = 0;
	arg_cnt = 0;
	for (i=0; i < order.length; i++) {
	    if (order[i]) {
		tmp += "\n\t\ttok[" + tok_cnt + "] = _arg" + i + ";";
		tok_cnt++;
	    }
	    else {
		tmp += "\n\t\targ[" + arg_cnt + "] = _arg" + i + ";";
		arg_cnt++;
	    }
	}
	plist.put("ArgAssigns", tmp);

	// Set FirstSubclass and ClassCode
	plist.put("FirstSubclass", Integer.toString(entry.firstSubclass()));
	plist.put("ClassCode", Integer.toString(entry.classCode()));

	// generate code
	baseCode.genCode(output, plist);
    }


    //**************************************************
    // This method allows the detection of a special form of a NamedRule.
    // This special form occurs when the NamedRule shares a single non-terminal
    // prefix with an UnNamedRule in the same rule. This special form
    // requires the NamedRule to generate parser source for itself and
    // the UnNamedRule. If this method returns true, then the special form
    // exists and this NamedRule will generate the special parser source
    // (so the UnNamedRule need not generate any parser source).
    //**************************************************
    public boolean hasNTPrefix(String prefix) {
	String firstRuleName;
	Primitive prim;

	// First primitive of pattern must be a RuleName object
	// NamedRule->Pattern->PatternElem->Primitive
	prim = (Primitive) arg[0].arg[0].arg[0];
	if (!(prim instanceof RuleName))
	    return(false);

	firstRuleName = prim.tok[0].tokenName();
	if (prefix.compareTo(firstRuleName) == 0) {
	    prefixFound = true;
	    return(true);
	}
	return(false);
    }

    public PrimitiveInfo getPrimitiveInfo() {
	Lang.AstNode lnode;

	if (pInfo == null) {
	    pInfo = new PrimitiveInfo();

	    // pt to first PatternElem;
	    lnode = arg[0].arg[0];
	    while (lnode != null) {
		((Primitive) lnode.arg[0]).setPrimitiveType(pInfo);

		lnode = lnode.right;
	    }
	}
	return(pInfo);
    }

    public void genParserSource(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");
	String ruleName;
	Lang.AstNode scanPtr;	// scans through the PatternElems
	int i;

	// NamedRule->Clause->TailListElem->TailList->BaliProduction
	ruleName = up.up.up.up.tok[0].tokenName();

	// pt to first PatternElem
	scanPtr = arg[0].arg[0];

	// Generate code for first Primitive
	((Primitive) scanPtr.arg[0]).genParserSource(props);
	if (prefixFound)
	    pw.print(" {justNT=true;} [ LOOKAHEAD(2) ");

	// Continue generating other Primitives
	scanPtr = scanPtr.right;
	while (scanPtr != null) {
	    ((Primitive) scanPtr.arg[0]).genParserSource(props);

	    scanPtr = scanPtr.right;
	}

	// Finish code for optional part (special form)
	if (prefixFound)
	    pw.print(" {justNT=false;} ]");

	// Now generate action line
	pw.print("\n\t\t{\n");
	if (prefixFound)
	    pw.print("\t\tif (justNT)\n\t\t\treturn((Lang." + ruleName +
		     ") nt1);\n");
	for (i=0; i < pInfo.variableCount(); i++) {
	    if (pInfo.isOptional(i)) {
		if (pInfo.isTerminal(i)) {
		    pw.print("\t\to");
		    pw.print(pInfo.variableName(i));
		    pw.print(" = new Lang.AstOptToken().setParms(t2at(");
		    pw.print(pInfo.variableName(i));
		    pw.print(")");
		}
		else {
		    pw.print("\t\to");
		    pw.print(pInfo.variableName(i));
		    pw.print(" = new Lang.AstOptNode().setParms(");
		    pw.print(pInfo.variableName(i));
		}

		pw.print(");\n");
	    }
	}

	pw.print("\t\treturn(new Lang.");
	pw.print(tok[0].tokenName());
	pw.print("().setParms(");
	for (i=0; i < pInfo.variableCount(); i++) {
	    if (i > 0)
		pw.print(", ");

	    if (pInfo.isOptional(i)) {
		pw.print("o");
		pw.print(pInfo.variableName(i));
	    }
	    else if (pInfo.isTerminal(i)) {
		pw.print("t2at(");
		pw.print(pInfo.variableName(i));
		pw.print(")");
	    }
	    else
		pw.print(pInfo.variableName(i));
	}
	pw.print("));\n\t\t}\n");
    }
}
