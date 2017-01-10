//                              -*- Mode: Java -*- 
// ComplexList.java --- 
// Author          : Bernie Lofaso
// Created On      : Tue Oct 20 08:04:14 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Mon Nov 23 15:33:05 1998
// Update Count    : 33
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;
import java.util.Properties;

public class ComplexList extends Tail {
    private boolean order[] = { false, false, false, false };

    // Set by getPrimitiveInfo() and used by genParserSource()
    private PrimitiveInfo pInfo = null;

    public ComplexList setParms(Primitive prim1, Lang.AstOptNode opt_la,
				Primitive prim2, Primitive prim3) {
	arg = new Lang.AstNode[4];
	arg[0] = prim1;
	arg[1] = opt_la;
	arg[2] = prim2;
	arg[3] = prim3;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public boolean isList() { return(true); }

    public void print() {
	arg[0].print();		// print first primitive
	System.out.print("(");
	arg[1].print();		// print optional lookahead
	arg[2].print();		// print second primitive
	arg[3].print();		// print third primitive
	System.out.print(")*");
    }


    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	arg[0].print(props);	// print first primitive
	pw.print("(");
	arg[1].print(props);	// print optional lookahead
	arg[2].print(props);	// print second primitive
	arg[3].print(props);	// print third primitive
	pw.print(")*");
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
    "    // ComplexList" + NL +
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
    "    // ComplexList (Elem)" + NL +
    "    static public class $(ClassName)Elem extends Lang.AstListNode {" + NL +
    "        static private int first_subclass = $(FirstSubclass);" + NL +
    "        static private int class_code = $(ClassCode);" + NL +
    NL +
    "        public Lang.$(ClassName)Elem setParms(Lang.AstToken _arg1," + NL +
    "                                          Lang.AstNode _arg2) {" + NL +
    "            if (_arg1 == null)" + NL +
    "                _arg1 = new Lang.AstToken().setParms(\"\", \"\", 0);" + NL +
    "            setParms(_arg2);" + NL +
    "            tok = new Lang.AstToken[1];" + NL +
    "            tok[0] = _arg1;" + NL +
    "            return((Lang.$(ClassName)Elem) this);" + NL +
    "        }" + NL +
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
	    ((Primitive) arg[0]).setPrimitiveType(pInfo);
	    ((Primitive) arg[2]).setPrimitiveType(pInfo);
	    ((Primitive) arg[3]).setPrimitiveType(pInfo);
	}
	return(pInfo);
    }

    public void genParserSource(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");
	String ruleName;
	String varName;
	String phrase;	// cached because it's printed twice
	Lookahead la;

	// ComplexList->Clause->TailListElem->TailList->BaliProduction
	ruleName = up.up.up.up.tok[0].tokenName();

	// Output first primitive
	((Primitive) arg[0]).genParserSource(props);
	phrase = "\n\t\t{ list.add(new Lang." + ruleName + "Elem().setParms(";
	pw.print(phrase);
	pw.print("null, ");
	pw.print(pInfo.variableName(0));
	pw.print(")); }\n\t(");

	// optional lookahead
	la = (Lookahead) arg[1].arg[0];
	if (la != null)
	    la.print(props);

	// prim2
	((Primitive) arg[2]).genParserSource(props);

	// prim3
	((Primitive) arg[3]).genParserSource(props);

	pw.print(phrase);
	pw.print("t2at(");
	pw.print(pInfo.variableName(1));
	pw.print("), ");
	pw.print(pInfo.variableName(2));
	pw.print(")); }\n\t)*\n");

	// final action
	pw.print("\t\t{ return(list); }\n");

	// Free for GC
	pInfo = null;
    }
}
