//                              -*- Mode: Java -*- 
// Symtab.java --- 
// Author          : Bernie Lofaso
// Created On      : Fri Mar 26 10:07:28 1999
// Last Modified By: 
// Last Modified On: Tue May 18 12:25:41 1999
// Update Count    : 51
// Status          : Under Development
// 


package Jakarta.symtab;

import Jakarta.util.Util;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public abstract class Symtab {
    private static final boolean debug = false;

    protected static Symtab theInstance;
    protected static PackageInfo def_pkg;
    protected static PackageInfo javaLangPkg;
    private String classpath;
    private Vector roots;		// objects are File's or ZipFile's

    //Used for building nested scope structures in the symbol table
    protected static Stack scopeStack;	// objects are Scope's

    // Used to same names that are later used to create symbol table
    // entries.
    public boolean collectNames;
    public Vector names;		// objects are SymName's

    public static ClassInfo Boolean;
    public static ClassInfo Byte;
    public static ClassInfo Character;
    public static ClassInfo Short;
    public static ClassInfo Integer;
    public static ClassInfo Long;
    public static ClassInfo Float;
    public static ClassInfo Double;
    public static ClassInfo Void;

    public static ClassInfo javaLangObject;

    protected Symtab() {
	int start, end;
	String root;
	File rfile;

	// Create misc. structures and initialize misc. variables
	names = new Vector();
	roots = new Vector();
	scopeStack = new Stack();
	collectNames = true;

	// get classpath
	classpath = System.getProperty("java.class.path");
	start = 0;
	end = classpath.indexOf(File.pathSeparatorChar, 0);
	while (end > 0) {
	    root = classpath.substring(start, end);
	    rfile = new File(root);
	    if (rfile.isDirectory())
		roots.addElement(rfile);
	    else if (rfile.getName().endsWith(".zip") ||
		     rfile.getName().endsWith(".jar")) {
		try {
		    roots.addElement(new ZipFile(rfile));
		}
		catch (Exception e) {}
	    }

	    // next root directory
	    start = end + 1;
	    end = classpath.indexOf(File.pathSeparatorChar, start);
	}

	// last directory
	root = classpath.substring(start);
	rfile = new File(root);
	if (rfile.isDirectory())
	    roots.addElement(rfile);
	else if (rfile.getName().endsWith(".zip") ||
		 rfile.getName().endsWith(".jar")) {
	    try {
		roots.addElement(new ZipFile(rfile));
	    }
	    catch (Exception e) {}
	}

	// This will add an entry to roots if you're using Java 1.2
	root = System.getProperty("java.home") + File.separator +
	    "lib" + File.separator + "rt.jar";
	rfile = new File(root);
	if (rfile.exists()) {
	    try {
		roots.addElement(new ZipFile(rfile));
	    }
	    catch (Exception e) {}
	}
    }

    protected static void initDefaultPackage() {
	PackageInfo subPkg;

	def_pkg = new PackageInfo("");

	// Create entries for primitive types in default package.
	Boolean = new ClassP("boolean", 0, null);
	def_pkg.addDeclaration(Boolean);

	Byte = new ClassP("byte", 0, null);
	def_pkg.addDeclaration(Byte);

	Character = new ClassP("char", 0, null);
	def_pkg.addDeclaration(Character);

	Short = new ClassP("short", 0, null);
	def_pkg.addDeclaration(Short);

	Integer = new ClassP("int", 0, null);
	def_pkg.addDeclaration(Integer);

	Long = new ClassP("long", 0, null);
	def_pkg.addDeclaration(Long);

	Float = new ClassP("float", 0, null);
	def_pkg.addDeclaration(Float);

	Double = new ClassP("double", 0, null);
	def_pkg.addDeclaration(Double);

	Void = new ClassP("void", 0, null);
	def_pkg.addDeclaration(Void);

	// Not really part of default package ...
	subPkg = def_pkg.findSubPackage("java");
	subPkg = subPkg.findSubPackage("lang");
	javaLangPkg = subPkg;
	javaLangObject = (ClassInfo) subPkg.findClass("Object");
    }

    public static Symtab instance() { return(theInstance); }

    //**************************************************
    // Get default package
    //**************************************************

    public PackageInfo defaultPackage() {
	return(def_pkg);
    }

    //**************************************************
    // Get java.lang package
    //**************************************************

    public PackageInfo getJavaLangPackage() {
	return(javaLangPkg);
    }

    //**************************************************
    // Return the list of root directories extracted from CLASSPATH.
    // The objects stored in the vector are File's.
    //**************************************************

    public Vector getRoots() {
	return(roots);
    }


    //**************************************************
    // NOTE: The symbol table building methods consist of activateScope(),
    // deactivateScope(), and declare(). The first two methods act as push
    // and pop operations on a stack of Scope objects maintained by Symtab.
    // A Declaration inserts itself into the appropriate Scope by calling
    // declare().
    //**************************************************

    //**************************************************
    // Used by AstNode's buildSymbolTable() method to clear the
    // scope stack between the first and second passes of symbol table
    // building.
    //**************************************************
    public void clearScopeStack() {
	try {
	    while (scopeStack.pop() != null) ;
	}
	catch (EmptyStackException e) {}
    }

    //**************************************************
    // Add a Scope to the stack of currently active scopes.
    //**************************************************
    public void activateScope(Scope newScope) {
	scopeStack.push(newScope);
    }

    //**************************************************
    // Deactivate the most recently activated Scope.
    //**************************************************
    public Scope deactivateScope() {
	Scope scope;

	try {
	    scope = (Scope) scopeStack.pop();
	}
	catch (EmptyStackException e) {
	    Util.fatalError(e);
	    scope = null;
	}
	return(scope);
    }

    //**************************************************
    // Return currently active scope.
    //**************************************************
    public Scope currentScope() {
	Scope top;

	try {
	    top = (Scope) scopeStack.peek();
	}
	catch (EmptyStackException e) {
	    Util.fatalError(e);
	    top = null;
	}

	return(top);
    }

    //**************************************************
    // This method helps build the symbol table. When a Declaration is
    // created, it calls declare() to add itself into the appropriate
    // Scope.
    //**************************************************
    public void declare(Declaration decl) {
	Scope top;
	boolean fail = false;

	try {
	    top = (Scope) scopeStack.peek();
	}
	catch (EmptyStackException e) {
	    Util.fatalError(e);
	    top = null;
	}

	if (top instanceof PackageInfo) {
	    if ((decl instanceof MethodInfo) ||
		(decl instanceof FieldInfo))
		fail = true;
	}
	else if (top instanceof ClassInfo) {
	    if (decl instanceof PackageInfo)
		fail = true;
	}
	else if ((top instanceof MethodInfo) || (top instanceof BlockScope)) {
	    if ((decl instanceof PackageInfo) ||
		(decl instanceof MethodInfo))
		fail = true;
	}
	else
	    Util.fatalError("Bad scope object " + top);

	if (fail)
	    Util.fatalError("Improper scope containment: " + decl +
			    " inside " + top);
	top.addDeclaration(decl);
    }


    public abstract Declaration lookup(String expression);

    public abstract Declaration lookup(String expression, Scope context);


    //**************************************************
    // NOTE: This does not actually expunge the instance of Symtab, but
    // instead expunges the singleton, causing a new one to be created.
    // This will work correctly as long as the old singleton instance is
    // not cached by some client.
    //**************************************************
    public void expunge() {
	theInstance = null;
    }


    static public final String indent[] = {
	"",
	"    ",
	"        ",
	"            ",
	"                ",
	"                    ",
	"                        ",
	"                            ",
	"                                "
    };

    public void dump(PrintWriter out) {
	def_pkg.dump(out, 0);
    }

    //**************************************************
    // For converting field or argument list signatures to text.
    //**************************************************
    static public String sigToString(String sig) {
	StringBuffer buffer = new StringBuffer();
	int dimCount = 0;
	int len;
	int semi;

	len = sig.length();
	for (int i=0; i < len; i++) {
	    switch (sig.charAt(i)) {
	    case '[':
		dimCount++;
		continue;
	    case 'B':
		buffer.append("byte");
		break;
	    case 'C':
		buffer.append("char");
		break;
	    case 'D':
		buffer.append("double");
		break;
	    case 'F':
		buffer.append("float");
		break;
	    case 'I':
		buffer.append("int");
		break;
	    case 'J':
		buffer.append("long");
		break;
	    case 'S':
		buffer.append("short");
		break;
	    case 'Z':
		buffer.append("boolean");
		break;
	    case 'V':
		buffer.append("void");
		break;
	    case 'L':
		semi = sig.indexOf(';', i);
		buffer.append(sig.substring(i+1, semi).replace('/', '.'));
		i = semi;
		break;
	    default:
		buffer.append("***Illegal sig char. '");
		buffer.append(sig.charAt(i));
		buffer.append("'***");
	    }

	    while (dimCount > 0) {
		buffer.append("[]");
		dimCount--;
	    }

	    if (i < (len-1))
		buffer.append(", ");
	}

	return(buffer.toString());
    }

    static public Type sigToType(String str) {
	int i;
	int dimCount;
	String type;
	ClassInfo ci;

	if (str == null)
	    return(null);
	str = str.replace('/', '.');
	i = str.indexOf(')') + 1;
	dimCount = 0;
	while (str.charAt(i) == '[') {
	    i++;
	    dimCount++;
	}
	switch (str.charAt(i)) {
	case 'B':
	    ci = Symtab.Byte;
	    break;
	case 'C':
	    ci = Symtab.Character;
	    break;
	case 'D':
	    ci = Symtab.Double;
	    break;
	case 'F':
	    ci = Symtab.Float;
	    break;
	case 'I':
	    ci = Symtab.Integer;
	    break;
	case 'J':
	    ci = Symtab.Long;
	    break;
	case 'L':
	    type = str.substring(i+1, str.indexOf(';', i+2));
	    ci = (ClassInfo) Symtab.instance().lookup(type);
	    if (ci == null)
		return(null);
	    break;
	case 'V':
	    ci = Symtab.Void;
	    break;
	case 'S':
	    ci = Symtab.Short;
	    break;
	case 'Z':
	    ci = Symtab.Boolean;
	    break;
	default:
	    return(null);
	}
	return(ci.getType(dimCount));
    }

    static public ClassInfo classOf(Declaration dcl) {
	if (dcl instanceof ClassInfo)
	    return((ClassInfo) dcl);
	if (dcl instanceof MethodInfo)
	    return(((MethodInfo) dcl).getReturnType());
	if (dcl instanceof FieldInfo)
	    return(((FieldInfo) dcl).getType());
	return(null);
    }
}
