//                              -*- Mode: Java -*- 
// SimpleList.java --- 
// Author          : Bernie Lofaso
// Created On      : Tue Oct 20 08:04:14 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Mon Nov 23 13:30:23 1998
// Update Count    : 31
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;
import java.util.Properties;

public class SimpleList extends Tail {
    private boolean order[] = { false, false };

    // Set by getPrimitiveInfo() and used by genParserSource()
    private PrimitiveInfo pInfo = null;

    public SimpleList setParms(Lang.AstOptNode opt_la, Primitive prim) {
	arg = new Lang.AstNode[2];
	arg[0] = opt_la;
	arg[1] = prim;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public boolean isList() { return(true); }

    public void print() {
	System.out.print("(");
	arg[0].print();
	arg[1].print();
	System.out.print(")+");
    }


    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print("(");
	arg[0].print(props);
	arg[1].print(props);
	pw.print(")+");
    }


    public void populateSymbolTable() {
	BaliProduction prod;
	SymbolTable st = SymbolTable.getInstance();
	SymbolTableEntry entry;

	// Mark the SymbolTableEntry associated with the rule name
	// as pertaining to a list class. Both ComplexList and SimpleList
	// mark the STE's of their rule in such a fashion.

	// ComplexList->Clause->TailListElem->TailList->BaliProduction
	prod = (BaliProduction) up.up.up.up;
	entry = st.getSymbol(prod.tok[0].tokenName());
	entry.markListClass();
    }

    public void setSuperclass(String ruleName) { }

    private static final String NL = "\n";

    // "Arguments" to this template are: ClassName, RootDecl, FirstSubclass,
    // and ClassCode.
    private static String tmpl =
    NL +
    "    // SimpleList" + NL +
    "    static public class $(ClassName) extends Lang.AstList {" + NL +
    "        $(RootDecl)" + NL +
    "        static private int first_subclass = $(FirstSubclass);" + NL +
    "        static private int class_code = $(ClassCode);" + NL +
    NL +
    "        public boolean CheckElemType(Lang.AstNode l)" + NL +
    "            { return(true); }" + NL +
    "        public String ElemType() { return(\"\"); }" + NL +
    "        public int firstSubclass() { return(first_subclass); }" + NL +
    "        public int classCode() { return(class_code); }" + NL +
    "    }" + NL +
    NL +
    "    // SimpleList (Elem)" + NL +
    "    static public class $(ClassName)Elem extends Lang.AstListNode {" + NL +
    "        static private int first_subclass = $(FirstSubclass);" + NL +
    "        static private int class_code = $(ClassCode);" + NL +
    NL +
    "        public boolean CheckArgType() { return(true); }" + NL +
    "        public String ArgType() { return(\"\"); }" + NL +
    "        public int firstSubclass() { return(first_subclass); }" + NL +
    "        public int classCode() { return(class_code); }" + NL +
    "    }" + NL;

    private static CodeTemplate baseCode = new CodeTemplate(tmpl);

    public void genBase(Lang.AstProperties props, SymbolTableEntry entry) {
	PrintWriter output;
	Properties plist = new Properties();
	String className = entry.getName();

	output = (PrintWriter) props.getProperty("output");
	plist.put("ClassName", className);
	if (className.compareTo(SymbolTable.getStartSymbol()) == 0)
	    plist.put("RootDecl", "        public static " + className +
		      " root;");
	plist.put("FirstSubclass", Integer.toString(entry.firstSubclass()));
	plist.put("ClassCode", Integer.toString(entry.classCode()));
	baseCode.genCode(output, plist);
    }


    public PrimitiveInfo getPrimitiveInfo() {
	if (pInfo == null) {
	    pInfo = new PrimitiveInfo();
	    ((Primitive) arg[1]).setPrimitiveType(pInfo);
	}
	return(pInfo);
    }

    public void genParserSource(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");
	String ruleName;
	Lookahead la;

	// SimpleList->Clause->TailListElem->TailList->BaliProduction
	ruleName = up.up.up.up.tok[0].tokenName();

	pw.print("(");

	// optional lookahead
	la = (Lookahead) arg[0].arg[0];
	if (la != null)
	    la.print(props);

	// primitive
	((Primitive) arg[1]).genParserSource(props);
	pw.print(" { list.add(new Lang.");
	pw.print(ruleName);
	pw.print("Elem().setParms(");
	pw.print(pInfo.variableName(0));
	pw.print(")); } )+\n");
	pw.print("\t\t{ return(list); }\n");

	// Free for GC
	pInfo = null;
    }
}
