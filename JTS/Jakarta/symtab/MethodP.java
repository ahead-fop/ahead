//                              -*- Mode: Java -*- 
// MethodP.java --- 
// Author          : Bernie Lofaso
// Created On      : Thu May 15 11:50:47 1997
// Last Modified By: 
// Last Modified On: Fri May 14 09:57:24 1999
// Update Count    : 31
// Status          : Under Development
// 
// $Locker:  $
// $Log: MethodP.java,v $
// Revision 1.4  2007-08-13 19:39:26  dsb
// reinstalling deleted files (sigh).
//
// Revision 1.2  2001/12/04 15:53:55  sarvela
// Removed redundant and error-prone carriage returns.
//
// Revision 1.1.1.1  1999/05/19 21:25:02  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.2  1999/05/19 21:25:02  lofaso
// Revised symbol table implementation.
//
// Revision 1.1.1.1  1999/02/18 16:15:45  lofaso
// Snapshot 2-18-99
//
// Revision 1.1.1.1  1997/12/15 21:00:27  lofaso
// Imported sources
//
// Revision 1.2  1997/09/08 15:29:13  lofaso
// Modified code to use Util.fatalError() to report errors. Fixed whitespace
// problem.
//
// Revision 1.1.1.1  1997/08/25 20:06:32  lofaso
// Imported Java 1.1 sources
//
// Revision 1.2  1997/07/07 13:54:10  lofaso
// Modifications to add symbol table support.
//
// Revision 1.1.1.1  1997/05/22 21:30:14  lofaso
// baseline sources
//
// 

package Jakarta.symtab;

import Jakarta.util.Util;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class MethodP extends MethodInfo {
    private String methodName;		// simple name
    private String methodNameFull;	// fully qualified name
    private int access_flags;
    private String method_sig;
    private String arg_sig;
    private Hashtable fields;
    private Vector classes;
    private Vector blocks;

    // n is simple name
    public MethodP(String n, int af) {
	methodName = n.trim();
	access_flags = af;
	fields = new Hashtable();
	classes = new Vector();
	blocks = new Vector();
    }

    public void setMethodSignature(SymName return_type, Scope context) {
	Symtab st = Symtab.instance();
	String returnSig;
	SymName typeName;
	ClassInfo type;
	int dims;

	// Calculate returnSig. If return type is an object, we must
	// make sure we get a fully qualified name. To do this we must
	// lookup the type name.
	type = (ClassInfo) st.lookup(return_type.getFullName(), context);
	dims = return_type.getDimCount();	// save dim count
	return_type = new SymName(type.getFullName());
	return_type.setDimCount(dims);		// set dim count in new SymName
	returnSig = return_type.getSignature();

	arg_sig = "";
	for (int i=0; i < st.names.size(); i++) {
	    typeName = (SymName) st.names.elementAt(i);
	    dims = typeName.getDimCount();
	    type = (ClassInfo) st.lookup(typeName.getFullName(), context);
	    typeName = new SymName(type.getFullName());
	    typeName.setDimCount(dims);
	    arg_sig += typeName.getSignature();
	}

	method_sig = "(" + arg_sig + ")" + returnSig;
    }


    //**************************************************
    // These next three methods complete or modify the Declaration
    // interface.
    //**************************************************

    public String getName() { return(methodName); }

    public String getFullName() { return(methodNameFull); }

    public void setDeclaringEnv(Scope declEnv) {
	super.setDeclaringEnv(declEnv);
	methodNameFull = declEnv.getFullName() + "." + methodName;
    }

    //**************************************************
    // These next two methods complete the Scope interface.
    //**************************************************

    public void addDeclaration(Declaration decl) {
	if (decl instanceof FieldInfo) {
	    decl.setDeclaringEnv(this);
	    fields.put(decl.getName(), decl);
	}
	else if (decl instanceof BlockScope) {
	    decl.setDeclaringEnv(this);
	    blocks.addElement(decl);
	}
	else if (decl instanceof ClassInfo) {
	    decl.setDeclaringEnv(this);
	    classes.addElement(decl);
	}
	else
	    Util.fatalError("Unknown Declaration: " + decl);
    }

    public void expunge() {
	Enumeration scanPtr;
	Scope scope;

	scanPtr = classes.elements();
	while (scanPtr.hasMoreElements()) {
	    scope = (Scope) scanPtr.nextElement();
	    scope.expunge();
	}

	scanPtr = blocks.elements();
	while (scanPtr.hasMoreElements()) {
	    scope = (Scope) scanPtr.nextElement();
	    scope.expunge();
	}

	declaringEnv = null;
	fields = null;
	classes = null;
	blocks = null;
    }

    public int getModifiers() { return(access_flags); }

    // Includes return type as well as argument types
    public String getMethodSignature() { return(method_sig); }

    public String getArgumentSignature() { return(arg_sig); }

    public Type getReturnType() {
	return(Symtab.sigToType(method_sig));
    }

    public Type[] getParameterTypes() {
	return(getParameterTypes(arg_sig));
    }

    //**************************************************
    // Scan fields
    //**************************************************

    public int getFieldCount() { return(fields.size()); }

    public Enumeration getFieldCursor() {
	BlockScope bodyBlock;

	if (blocks.size() == 0)
	    return(fields.elements());
	bodyBlock = (BlockScope) blocks.elementAt(0);
	return(new dualEnum(fields.elements(), bodyBlock.getFieldCursor()));
    }

    public FieldInfo[] getFields() {
	FieldInfo[] result;
	int i;
	Enumeration csr;

	result = new FieldInfo[fields.size()];
	if (result.length > 0) {
	    try {
		csr = fields.elements();
		for (i=0; i < result.length; i++)
		    result[i] = (FieldInfo) csr.nextElement();
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

    public int getClassCount() { return(classes.size()); }

    public Enumeration getClassCursor() {
	return(classes.elements());
    }

    public ClassInfo[] getClasses() {
	ClassInfo[] result;
	Enumeration scanPtr;
	int i = 0;

	result = new ClassInfo[classes.size()];
	scanPtr = classes.elements();
	while (scanPtr.hasMoreElements())
	    result[i++] = (ClassInfo) scanPtr.nextElement();
	return(result);
    }

    //**************************************************
    // Locate a specific field
    //**************************************************

    public FieldInfo findField(String name) {
	return((FieldInfo) fields.get(name));
    }

    //**************************************************
    // Locate a specific class
    //**************************************************

    public ClassInfo findClass(String name) {
	Enumeration scanPtr;
	String cname;
	ClassInfo ci;

	scanPtr = classes.elements();
	while (scanPtr.hasMoreElements()) {
	    ci = (ClassInfo) scanPtr.nextElement();
	    cname = ci.getName();
	    if (cname.compareTo(name) == 0)
		return(ci);
	}
	return(null);
    }
}

class dualEnum implements Enumeration {
    protected Enumeration e1;
    protected Enumeration e2;
    protected boolean firstEnum;

    public dualEnum(Enumeration e1, Enumeration e2) {
	this.e1 = e1;
	this.e2 = e2;
	firstEnum = true;
    }

    public boolean hasMoreElements() {
	return(e1.hasMoreElements() | e2.hasMoreElements());
    }

    public Object nextElement() {
	if (firstEnum) {
	    if (e1.hasMoreElements())
		return(e1.nextElement());
	    else
		firstEnum = false;
	}
	return(e2.nextElement());
    }
}
