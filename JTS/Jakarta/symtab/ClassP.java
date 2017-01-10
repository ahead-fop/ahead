//                              -*- Mode: Java -*- 
// ClassP.java --- 
// Author          : Bernie Lofaso
// Created On      : Tue May 13 12:22:26 1997
// Last Modified By: 
// Last Modified On: Thu May 13 16:23:55 1999
// Update Count    : 43
// Status          : Under Development
// 
// $Locker:  $
// $Log: ClassP.java,v $
// Revision 1.4  2007-08-13 19:39:26  dsb
// reinstalling deleted files (sigh).
//
// Revision 1.2  2001/12/04 15:53:53  sarvela
// Removed redundant and error-prone carriage returns.
//
// Revision 1.1.1.1  1999/05/19 21:24:54  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.2  1999/05/19 21:24:54  lofaso
// Revised symbol table implementation.
//
// Revision 1.1.1.1  1999/02/18 16:15:44  lofaso
// Snapshot 2-18-99
//
// Revision 1.1.1.1  1997/12/15 21:00:26  lofaso
// Imported sources
//
// Revision 1.2  1997/09/08 15:29:11  lofaso
// Modified code to use Util.fatalError() to report errors. Fixed whitespace
// problem.
//
// Revision 1.1.1.1  1997/08/25 20:06:31  lofaso
// Imported Java 1.1 sources
//
// Revision 1.3  1997/07/16 18:54:42  lofaso
// Fields added to ClassP objects are now order dependent (i.e. the cursor
// retrieves in the same order as they were added). A bug was corrected in
// Symtab.internPackage() which was creating two packages every time.
//
// Revision 1.2  1997/07/07 13:54:03  lofaso
// Modifications to add symbol table support.
//
// Revision 1.1.1.1  1997/05/22 21:30:13  lofaso
// baseline sources
//
// 

package Jakarta.symtab;

import Jakarta.util.Util;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ClassP extends ClassInfoBase {
    private String clsName;	// simple name
    private String clsNameFull;	// fully qualified name
    private ClassInfo superclass;
    private int access_flags;
    private Vector interfaces;	// holds interfaces implemented
    private Vector nestedInterfaces;
    private Hashtable fields;
    private Vector fieldOrder;	// used to make field list ordered.
    private Hashtable methods;
    private Hashtable classes;
    private Vector blocks;	// only used to expunge
    private CompilationUnit cu;

    static private ClassInfo objectClass = null;

    //**************************************************
    // Constructor
    //**************************************************

    public ClassP(String n, int af, CompilationUnit cu) {
	// n is simple name. Set full name when setDeclaringEnv() is called.
	n = n.trim();
	clsName = n;
	access_flags = af;
	this.cu = cu;
	interfaces = new Vector();
	fields = new Hashtable();
	fieldOrder = new Vector();
	methods = new Hashtable();
	classes = new Hashtable();
	blocks = new Vector();
    }

    public void setDeclaringEnv(Scope declEnv) {
	String declName;

	super.setDeclaringEnv(declEnv);
	declName = declEnv.getFullName();
	if (declName.length() == 0)
	    clsNameFull = clsName;
	else
	    clsNameFull = declName + "." + clsName;
    }

    // Completes implementation of Declaration interface
    public String getName() { return(clsName); }
    public String getFullName() { return(clsNameFull); }
    public CompilationUnit getCompilationUnit() { return(cu); }

    //**************************************************
    // Scope implementation
    //**************************************************
    public String getScopeName() { return(getName()); }

    //**************************************************
    // NOTE: use setInterfaceImplemented() to indicate interfaces which
    // are implemented by this class. Nested interfaces are added by
    // addDeclaration().
    //**************************************************
    public void addDeclaration(Declaration decl) {
	if (decl instanceof ClassInfo) {
	    ClassInfo newCls = (ClassInfo) decl;

	    newCls.setDeclaringEnv(this);
	    if ((newCls.getModifiers() & ACC_INTERFACE) == 0) {
		// Check if we have declaration from first pass
		if (classes.contains(newCls))
		    return;
		classes.put(newCls.getName(), newCls);
	    }
	    else {
		// interface declaration
		if (nestedInterfaces.contains(newCls))
		    return;
		nestedInterfaces.addElement(newCls);
	    }
	}
	else if (decl instanceof MethodInfo) {
	    MethodInfo newMethod = (MethodInfo) decl;
	    String key;

	    key = newMethod.getName() + "(" +
		newMethod.getArgumentSignature() + ")";
	    newMethod.setDeclaringEnv(this);
	    methods.put(key, newMethod);
	}
	else if (decl instanceof FieldInfo) {
	    decl.setDeclaringEnv(this);
	    fields.put(decl.getName(), decl);
	    fieldOrder.addElement(decl);
	}
	else if (decl instanceof BlockScope) {
	    decl.setDeclaringEnv(this);
	    blocks.addElement(decl);
	}
	else
	    Util.fatalError("Unknown Declaration: " + decl);
    }

    public void expunge() {
	Enumeration scanPtr;
	Scope scope;

	scanPtr = interfaces.elements();
	while (scanPtr.hasMoreElements()) {
	    scope = (Scope) scanPtr.nextElement();
	    scope.expunge();
	}

	scanPtr = classes.elements();
	while (scanPtr.hasMoreElements()) {
	    scope = (Scope) scanPtr.nextElement();
	    scope.expunge();
	}

	scanPtr = methods.elements();
	while (scanPtr.hasMoreElements()) {
	    scope = (Scope) scanPtr.nextElement();
	    scope.expunge();
	}

	scanPtr = blocks.elements();
	while (scanPtr.hasMoreElements()) {
	    scope = (Scope) scanPtr.nextElement();
	    scope.expunge();
	}

	superclass = null;
	interfaces = null;
	fields = null;
	fieldOrder = null;
	methods = null;
	classes = null;
	blocks = null;
    }


    public ClassInfo getSuperclass() {
	if (superclass == null) {
	    if (objectClass == null)
		objectClass = Symtab.javaLangObject;
	    superclass = objectClass;
	}
	return(superclass);
    }

    // This is required because we must create a ClassP object before we
    // know its superclass.
    public void setSuperclass(ClassInfo sc) {
	if (superclass == null)
	    superclass = sc;
    }

    // Used to indicate which interfaces this class implements. Nested
    // interfaces are indicated by calling addDeclaration.
    public void setInterfaceImplemented(ClassInfo intface) {
	if (! (interfaces.contains(intface)))
	    interfaces.addElement(intface);
    }

    public int getModifiers() { return(access_flags); }

    //**************************************************
    // Scan interfaces implemented
    //**************************************************

    public int getInterfaceCount() {
	return(interfaces.size());
    }

    public Enumeration getInterfaceCursor() {
	return(interfaces.elements());
    }

    public ClassInfo[] getInterfaces() {
	ClassInfo[] result;
	int i;
	Enumeration csr;

	result = new ClassInfo[interfaces.size()];
	if (result.length > 0) {
	    try {
		csr = interfaces.elements();
		for (i=0; i < result.length; i++)
		    result[i] = (ClassInfo) csr.nextElement();
	    }
	    catch (Exception e) {
		Util.fatalError(e);
	    }
	}
	return(result);
    }


    //**************************************************
    // Scan nested interfaces
    //**************************************************

    public int getNestedInterfaceCount() {
	return(interfaces.size());
    }

    public Enumeration getNestedInterfaceCursor() {
	return(interfaces.elements());
    }

    public ClassInfo[] getNestedInterfaces() {
	ClassInfo[] result;
	int i;
	Enumeration csr;

	result = new ClassInfo[interfaces.size()];
	if (result.length > 0) {
	    try {
		csr = interfaces.elements();
		for (i=0; i < result.length; i++)
		    result[i] = (ClassInfo) csr.nextElement();
	    }
	    catch (Exception e) {
		Util.fatalError(e);
	    }
	}
	return(result);
    }


    //**************************************************
    // Scan fields
    //**************************************************

    public int getFieldCount() {
	return(fields.size());
    }

    public Enumeration getFieldCursor() {
	return(fieldOrder.elements());
    }

    public FieldInfo[] getFields() {
	FieldInfo[] result;
	int i;

	result = new FieldInfo[fieldOrder.size()];
	if (result.length > 0) {
	    for (i=0; i < result.length; i++)
		result[i] = (FieldInfo) fieldOrder.elementAt(i);
	}
	return(result);
    }

    //**************************************************
    // Scan methods
    //**************************************************

    public int getMethodCount() {
	return(methods.size());
    }

    public Enumeration getMethodCursor() {
	return(methods.elements());
    }

    public MethodInfo[] getMethods() {
	MethodInfo[] result;
	int i;
	Enumeration csr;

	result = new MethodInfo[methods.size()];
	if (result.length > 0) {
	    try {
		csr = methods.elements();
		for (i=0; i < result.length; i++)
		    result[i] = (MethodInfo) csr.nextElement();
	    }
	    catch (Exception e) {
		Util.fatalError(e);
	    }
	}
	return(result);
    }

    //**************************************************
    // Scan classes
    //**************************************************

    public int getClassCount() {
	return(classes.size());
    }

    public Enumeration getClassCursor() {
	return(classes.elements());
    }

    public ClassInfo[] getClasses() {
	ClassInfo[] result;
	int i;
	Enumeration csr;

	result = new ClassInfo[classes.size()];
	if (result.length > 0) {
	    try {
		csr = classes.elements();
		for (i=0; i < result.length; i++)
		    result[i] = (ClassInfo) csr.nextElement();
	    }
	    catch (Exception e) {
		Util.fatalError(e);
	    }
	}
	return(result);
    }

    //**************************************************
    // Locate specific sub-objects.
    //**************************************************

    public FieldInfo findField(String fieldName)
	throws BadSymbolNameException {
	FieldInfo field;

	if (fieldName.indexOf('.') >= 0)
	    throw new BadSymbolNameException("Field name " + fieldName +
					     " is not simple.");

	// Test for field local to this class
	field = (FieldInfo) fields.get(fieldName);
	if (field != null)
	    return(field);

	// Return result of non-local search (inheritance and enclosing chains)
	return(findFieldNonLocal(fieldName));
    }

    public MethodInfo findMethod(String name, String arg_sig)
	throws BadSymbolNameException {
	MethodInfo method;
	Enumeration scanPtr;

	if (name.indexOf('.') >= 0)
	    throw new BadSymbolNameException("Method name " + name +
					     " is not simple.");

	// Test for method local to this class
	method = (MethodInfo) methods.get(name+"("+arg_sig+")");
	if (method != null)
	    return(method);

	// Check local methods with coercion of args
	scanPtr = methods.elements();
	while (scanPtr.hasMoreElements()) {
	    method = (MethodInfo) scanPtr.nextElement();
	    if (name.compareTo(method.getName()) != 0)
		continue;
	    if (sigsCompatible(method.getArgumentSignature(), arg_sig))
		return(method);
	}

	// Return result of non-local search (inheritance and enclosing chains)
	return(findMethodNonLocal(name, arg_sig));
    }

    public ClassInfo findInterface(String name)
	throws BadSymbolNameException {
	ClassInfo intface;
	Enumeration scanPtr;

	if (name.indexOf('.') >= 0)
	    throw new BadSymbolNameException("Interface name " + name +
					     " is not simple.");

	// Test for interface local to this class
	scanPtr = interfaces.elements();
	while (scanPtr.hasMoreElements()) {
	    intface = (ClassInfo) scanPtr.nextElement();
	    if (intface.getName().compareTo(name) == 0)
		return(intface);
	}

	// Return result of non-local search (inheritance and enclosing chains)
	return(findInterfaceNonLocal(name));
    }

    public ClassInfo findNestedInterface(String name)
	throws BadSymbolNameException {
	ClassInfo intface;

	if (name.indexOf('.') >= 0)
	    throw new BadSymbolNameException("Interface name " + name +
					     " is not simple.");

	for (int i=0; i < nestedInterfaces.size(); i++) {
	    intface = (ClassInfo) nestedInterfaces.elementAt(i);
	    if (intface.getName().compareTo(name) == 0)
		return(intface);
	}
	return(null);
    }

    public ClassInfo findClass(String name)
	throws BadSymbolNameException {
	ClassInfo cls;

	if (name.indexOf('.') >= 0)
	    throw new BadSymbolNameException("Class name " + name +
					     " is not simple.");

	// Test for class local to this class
	cls = (ClassInfo) classes.get(name);
	if (cls != null)
	    return(cls);

	// Return result of non-local search (inheritance and enclosing chains)
	return(findClassNonLocal(name));
    }

    public boolean instanceOf(ClassInfo c) {
	ClassInfo ci;
	Enumeration scanPtr;

	if (clsNameFull == c.getFullName())
	    return(true);

	scanPtr = interfaces.elements();
	while (scanPtr.hasMoreElements()) {
	    ci = (ClassInfo) scanPtr.nextElement();
	    if (c == ci)
		return(true);
	}

	if (superclass != null)
	    return(superclass.instanceOf(c));

	return(false);
    }
}
