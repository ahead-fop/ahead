package Jakarta.symtab;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

//**********************************************************************
// ClassInfo is abstract and therefore has no constructors. Also, some of the
// methods implementing the Scope and Declaration interfaces appear in
// subclasses of ClassInfo.
//**********************************************************************
public abstract class ClassInfoBase extends DeclaredScope implements ClassInfo
{
    protected Scope declaringEnv;
    protected Object userData;
    private ClassType typeCache = null;

    //**************************************************
    // Nested class for creating Type instances
    //**************************************************
    class ClassType extends AbstractNamed implements Type {
	private int dim;
	ClassType next;

	ClassType(int dim) {
	    this.dim = dim;
	}

	public ClassInfo baseType() { return(ClassInfoBase.this); }

	// Now "echo" all methods of Type by calling enclosing class.

	public Type getType(int dim) {
	    return(ClassInfoBase.this.getType(dim));
	}

	// Methods in both Scope and Declaration

	public Scope getDeclaringEnv() {
	    return(ClassInfoBase.this.getDeclaringEnv());
	}

	public String getName() {
	    return(ClassInfoBase.this.getName());
	}

	public String getFullName() {
	    return(ClassInfoBase.this.getFullName());
	}

	public CompilationUnit getCompilationUnit() {
	    return(ClassInfoBase.this.getCompilationUnit());
	}

	public void dump(PrintWriter out, int indentLevel) {
	    ClassInfoBase.this.dump(out, indentLevel);
	}

	// Methods only in Scope

	public void addDeclaration(Declaration decl) {
	    ClassInfoBase.this.addDeclaration(decl);
	}

	public void expunge() {
	    ClassInfoBase.this.expunge();
	}

	// Methods only in Declaration

	public void setDeclaringEnv(Scope declEnv) {
	    ClassInfoBase.this.setDeclaringEnv(declEnv);
	}

	public Object getUserData() {
	    return(ClassInfoBase.this.getUserData());
	}

	public void setUserData(Object data) {
	    ClassInfoBase.this.setUserData(data);
	}

	// Methods unique to ClassInfo

	public ClassInfo getSuperclass() {
	    return(ClassInfoBase.this.getSuperclass());
	}

	public int getModifiers() {
	    return(ClassInfoBase.this.getModifiers());
	}

	public int getInterfaceCount() {
	    return(ClassInfoBase.this.getInterfaceCount());
	}

	public Enumeration getInterfaceCursor() {
	    return(ClassInfoBase.this.getInterfaceCursor());
	}

	public ClassInfo[] getInterfaces() {
	    return(ClassInfoBase.this.getInterfaces());
	}

	public int getNestedInterfaceCount() {
	    return(ClassInfoBase.this.getNestedInterfaceCount());
	}

	public Enumeration getNestedInterfaceCursor() {
	    return(ClassInfoBase.this.getNestedInterfaceCursor());
	}

	public ClassInfo[] getNestedInterfaces() {
	    return(ClassInfoBase.this.getNestedInterfaces());
	}

	public int getFieldCount() {
	    return(ClassInfoBase.this.getFieldCount());
	}

	public Enumeration getFieldCursor() {
	    return(ClassInfoBase.this.getFieldCursor());
	}

	public FieldInfo[] getFields() {
	    return(ClassInfoBase.this.getFields());
	}

	public int getMethodCount() {
	    return(ClassInfoBase.this.getMethodCount());
	}

	public Enumeration getMethodCursor() {
	    return(ClassInfoBase.this.getMethodCursor());
	}

	public MethodInfo[] getMethods() {
	    return(ClassInfoBase.this.getMethods());
	}

	public int getClassCount() {
	    return(ClassInfoBase.this.getClassCount());
	}

	public Enumeration getClassCursor() {
	    return(ClassInfoBase.this.getClassCursor());
	}

	public ClassInfo[] getClasses() {
	    return(ClassInfoBase.this.getClasses());
	}

	public FieldInfo findField(String fieldName)
	    throws BadSymbolNameException {
	    return(ClassInfoBase.this.findField(fieldName));
	}

	public MethodInfo findMethod(String name, String arg_sig)
	    throws BadSymbolNameException {
	    return(ClassInfoBase.this.findMethod(name, arg_sig));
	}

	public MethodInfo findMethod(String name, ClassInfo[] arg_types)
	    throws BadSymbolNameException {
	    return(ClassInfoBase.this.findMethod(name, arg_types));
	}

	public ClassInfo findInterface(String name)
	    throws BadSymbolNameException {
	    return(ClassInfoBase.this.findInterface(name));
	}

	public ClassInfo findNestedInterface(String name)
	    throws BadSymbolNameException {
	    return(ClassInfoBase.this.findNestedInterface(name));
	}

	public ClassInfo findClass(String name)
	    throws BadSymbolNameException {
	    return(ClassInfoBase.this.findClass(name));
	}

	//**************************************************
	// Determine inheritance and assignment relationships
	//**************************************************

	public boolean instanceOf(ClassInfo parent) {
	    return(ClassInfoBase.this.instanceOf(parent));
	}

	public boolean ancestorOf(ClassInfo child) {
	    return(ClassInfoBase.this.ancestorOf(child));
	}

	public boolean assignableTo(ClassInfo parent) {
	    return(ClassInfoBase.this.assignableTo(parent, getDim()));
	}

	public ClassInfo promotesTo(ClassInfo b) {
	    return(ClassInfoBase.this.promotesTo(b, ClassInfoBase.this,
						 getDim()));
	}

	// From Type
	public int getDim() { return(dim); }

	public String toString() { return(ClassInfoBase.this.toString()); }
    }

    //**************************************************
    // Method for interning Type objects in this ClassInfo instance's
    // typeCache.
    //**************************************************
    public Type getType(int dim) {
	ClassType cachePtr;

	cachePtr = typeCache;
	while (cachePtr != null) {
	    if (cachePtr.getDim() == dim)
		return(cachePtr);
	    cachePtr = cachePtr.next;
	}

	cachePtr = this.new ClassType(dim);
	cachePtr.next = typeCache;
	typeCache = cachePtr;
	return(cachePtr);
    }

    //**************************************************
    // Declaration interface
    //**************************************************

    // getName(), getFullName() and getCompilationUnit are implemented
    // in derived classes.

    public Scope getDeclaringEnv() { return(declaringEnv); }
    public void setDeclaringEnv(Scope declEnv) {
	if (declaringEnv == null)
	    declaringEnv = declEnv;
    }
    public Object getUserData() { return(userData); }
    public void setUserData(Object data) { userData = data; }


    //**************************************************
    // Scope interface is implemented in subclasses
    //**************************************************

    //**************************************************
    // Shared (by subclasses) implementations that search inheritance
    // and enclosing chains.
    //**************************************************

    protected FieldInfo findFieldNonLocal(String fieldName)
        throws BadSymbolNameException {
	FieldInfo field;
	ClassInfo ancestor;
	Scope scope;

	// Search superclass (inheritance chain) for field
	ancestor = getSuperclass();
	if (ancestor != null) {
	    field = ancestor.findField(fieldName);
	    if (field != null)
		return(field);
	}

	// We do not search containment chain for top-level nested
	// classes (declared static).
	if ((getModifiers() & ACC_STATIC) == 0) {
	    // Search containment chain for field
	    scope = declaringEnv;
	    while (scope != null) {
		if (! (scope instanceof ClassInfo))
		    break;
		field = ((ClassInfo) scope).findField(fieldName);
		if (field != null)
		    return(field);
		scope = scope.getDeclaringEnv();
	    }
	}

	return(null);
    }


    //**************************************************
    // Shared implementation
    //**************************************************
    public MethodInfo findMethod(String name, ClassInfo[] arg_types)
        throws BadSymbolNameException {
	return(findMethod(name, signature(arg_types)));
    }

    protected MethodInfo findMethodNonLocal(String name, String arg_sig)
        throws BadSymbolNameException {
	MethodInfo method;
	ClassInfo ancestor;
	Scope scope;

	// Search superclass (inheritance chain) for method
	ancestor = getSuperclass();
	if (ancestor != null) {
	    method = ancestor.findMethod(name, arg_sig);
	    if (method != null)
		return(method);
	}

	// We do not search containment chain if in a top-level nested class.
	if ((getModifiers() & ACC_STATIC) == 0) {
	    // Search containment chain for method
	    scope = declaringEnv;
	    while (scope != null) {
		if (! (scope instanceof ClassInfo))
		    break;
		method = ((ClassInfo) scope).findMethod(name, arg_sig);
		if (method != null)
		    return(method);
		scope = scope.getDeclaringEnv();
	    }
	}

	return(null);
    }

    protected ClassInfo findInterfaceNonLocal(String name)
        throws BadSymbolNameException {
	ClassInfo intface;
	ClassInfo ancestor;
	Scope scope;

	// Search superclass (inheritance chain) for interface
	ancestor = getSuperclass();
	if (ancestor != null) {
	    intface = ancestor.findInterface(name);
	    if (intface != null)
		return(intface);
	}

	// We do not search containment chain if in a top-level nested class.
	if ((getModifiers() & ACC_STATIC) == 0) {
	    // Search containment chain for interface
	    scope = declaringEnv;
	    while (scope != null) {
		if (! (scope instanceof ClassInfo))
		    break;
		intface = ((ClassInfo) scope).findInterface(name);
		if (intface != null)
		    return(intface);
		scope = scope.getDeclaringEnv();
	    }
	}

	return(null);
    }

    protected ClassInfo findNestedInterfaceNonLocal(String name)
        throws BadSymbolNameException {
	ClassInfo intface;
	ClassInfo ancestor;
	Scope scope;

	// Search superclass (inheritance chain) for interface
	ancestor = getSuperclass();
	if (ancestor != null) {
	    intface = ancestor.findNestedInterface(name);
	    if (intface != null)
		return(intface);
	}

	// We do not search containment chain if in a top-level nested class.
	if ((getModifiers() & ACC_STATIC) == 0) {
	    // Search containment chain for interface
	    scope = declaringEnv;
	    while (scope != null) {
		if (! (scope instanceof ClassInfo))
		    break;
		intface = ((ClassInfo) scope).findNestedInterface(name);
		if (intface != null)
		    return(intface);
		scope = scope.getDeclaringEnv();
	    }
	}

	return(null);
    }

    protected ClassInfo findClassNonLocal(String name)
        throws BadSymbolNameException {
	ClassInfo cls;
	ClassInfo ancestor;
	Scope scope;

	// Search superclass (inheritance chain) for class
	ancestor = getSuperclass();
	if (ancestor != null) {
	    cls = ancestor.findClass(name);
	    if (cls != null)
		return(cls);
	}

	// We do not search containment chain if in a top-level nested class.
	if ((getModifiers() & ACC_STATIC) == 0) {
	    // Search containment chain for class
	    scope = declaringEnv;
	    while (scope != null) {
		if (! (scope instanceof ClassInfo))
		    break;
		cls = ((ClassInfo) scope).findClass(name);
		if (cls != null)
		    return(cls);
		scope = scope.getDeclaringEnv();
	    }
	}

	return(null);
    }


    //**************************************************
    // Compares a parameter type list with an argument type list and
    // determines if the arguments can be converted to the appropriate
    // parameter type.
    //**************************************************
    protected boolean sigsCompatible(String parmSigs, String argSigs) {
	int pidx, aidx;
	Type parm, arg;
	String pstr, astr;

	// NOTE: parms are what the method req., args are what's supplied.

	if ((parmSigs == null) || (parmSigs.length() == 0)) {
	    if ((argSigs == null) || (argSigs.length() == 0))
		return(true);
	    return(false);
	}

	pidx = 0;
	aidx = 0;

	do {
	    pstr = getOne(parmSigs, pidx);
	    pidx += pstr.length();
	    parm = Symtab.sigToType(pstr);

	    if (aidx >= argSigs.length())
		return(false);
	    astr = getOne(argSigs, aidx);
	    aidx += astr.length();
	    arg = Symtab.sigToType(astr);

	    if (! (arg.assignableTo(parm)))
		return(false);
	} while (pidx < parmSigs.length());

	if (aidx != argSigs.length())
	    return(false);

	return(true);
    }

    private String getOne(String arglist, int index) {
	int start = index;

	if (start >= arglist.length())
	    return("");

	while (index < arglist.length()) {
	    switch(arglist.charAt(index)) {
	    case 'B':
	    case 'C':
	    case 'D':
	    case 'F':
	    case 'I':
	    case 'J':
	    case 'S':
	    case 'Z':
	    case 'V':
		return(arglist.substring(start, index+1));
	    case 'L':
		index = arglist.indexOf(';', index);
		return(arglist.substring(start, index+1));
	    }

	    index++;
	}
	return(arglist.substring(index+1));
    }

    // Convert an array of types to a method signature
    public String signature(ClassInfo[] arg_types) {
	int i;
	String sig;
	ClassInfo ci;

	sig = "";
	for (i=0; i < arg_types.length; i++) {
	    ci = arg_types[i];
	    if (ci == Symtab.Byte)
		sig += "B";
	    else if (ci == Symtab.Character)
		sig += "C";
	    else if (ci == Symtab.Double)
		sig += "D";
	    else if (ci == Symtab.Float)
		sig += "F";
	    else if (ci == Symtab.Integer)
		sig += "I";
	    else if (ci == Symtab.Long)
		sig += "J";
	    else if (ci == Symtab.Short)
		sig += "S";
	    else if (ci == Symtab.Boolean)
		sig += "Z";
	    else if (ci == Symtab.Void)
		sig += "V";
	    else {
		// Component is an object
		sig += "L" + ci.getFullName() + ";";
	    }
	}

	return(sig);
    }

    public String ToString() {
	StringBuffer buffer = new StringBuffer();
	Enumeration scanPtr;
	FieldInfo field;
	MethodInfo method;

	buffer.append("Class: ");
	buffer.append(getFullName());
	scanPtr = getFieldCursor();
	while (scanPtr.hasMoreElements()) {
	    field = (FieldInfo) scanPtr.nextElement();
	    buffer.append("\n\t");
	    buffer.append(field.toString());
	}
	scanPtr = getMethodCursor();
	while (scanPtr.hasMoreElements()) {
	    method = (MethodInfo) scanPtr.nextElement();
	    buffer.append("\n\t");
	    buffer.append(method.toString());
	}
	buffer.append("\n");

	return(buffer.toString());
    }

    //**************************************************
    // Primarily used for debugging.
    //**************************************************
    public void dump(PrintWriter out, int indentLevel) {
	Enumeration scanPtr;
	ClassInfo ci;
	MethodInfo mi;
	FieldInfo fi;
	String indent = Symtab.indent[indentLevel];

	// Increase indent for contained objects
	indentLevel++;

	out.print(indent + "Class: " + getName());
	ci = getSuperclass();
	if (ci == Symtab.javaLangObject)
	    ci = null;
	if (ci != null)
	    out.print(" extends " + ci.getFullName());

	// print interfaces implemented
	scanPtr = sortCursor (getInterfaceCursor()) ;
	if (scanPtr.hasMoreElements()) {
	    out.print(" implements ");
	    boolean first = true;
	    do {
		if (! first)
		    out.print(", ");
		else
		    first = false;
		ci = (ClassInfo) scanPtr.nextElement();
		out.print(ci.getFullName());
	    } while (scanPtr.hasMoreElements());
	}
	out.println();

	scanPtr = sortCursor (getClassCursor()) ;
	if (scanPtr.hasMoreElements()) {
	    do {
		ci = (ClassInfo) scanPtr.nextElement();
		ci.dump(out, indentLevel);
	    } while (scanPtr.hasMoreElements());
	}

	scanPtr = sortCursor (getNestedInterfaceCursor()) ;
	if (scanPtr.hasMoreElements()) {
	    do {
		ci = (ClassInfo) scanPtr.nextElement();
		ci.dump(out, indentLevel);
	    } while (scanPtr.hasMoreElements());
	}

	scanPtr = sortCursor (getMethodCursor()) ;
	if (scanPtr.hasMoreElements()) {
	    do {
		mi = (MethodInfo) scanPtr.nextElement();
		mi.dump(out, indentLevel);
	    } while (scanPtr.hasMoreElements());
	}

	scanPtr = sortCursor (getFieldCursor());
	if (scanPtr.hasMoreElements()) {
	    do {
		fi = (FieldInfo) scanPtr.nextElement();
		fi.dump(out, indentLevel);
	    } while (scanPtr.hasMoreElements());
	}
    }

    //**************************************************
    // Define other inheritance/assignment tests in terms of instanceOf()
    //**************************************************

    public boolean ancestorOf(ClassInfo child) {
	return(child.instanceOf(this));
    }

    // For parent to be assignable to this, this is instanceOf parent
    // and dimensions must be the same. If this or parent is a ClassInfo
    // and not a Type, we treat as a zero-dimensioned Type.
    public boolean assignableTo(ClassInfo parent) {
	return(assignableTo(parent, 0));
    }

    private boolean assignableTo(ClassInfo parent, int thisDim) {
	int parentDim;

	if (! (this.instanceOf(parent))) {
	    // Is promotable?
	    if (promotesTo(parent) != parent)
		return(false);
	    return(true);
	}

	parentDim = parent instanceof Type ? ((Type) parent).getDim() : 0;
	if (thisDim != parentDim)
	    return(false);

	return(true);
    }


    // Answers the question: "Can I promote this to b?"
    public ClassInfo promotesTo(ClassInfo b) {
	return(promotesTo(b, this, 0));
    }

    private ClassInfo promotesTo(ClassInfo b, ClassInfo thisBase,
				 int thisDim) {
	ClassInfo bBase;
	int dimB;

	//**************************************************
	// Check dimensionality first
	//**************************************************

	dimB = b instanceof Type ? ((Type) b).getDim() : 0;
	if (thisDim != dimB)
	    return(null);

	//**************************************************
	// Now check that base type of this is promotable to b.
	//**************************************************

	bBase = b instanceof Type ? ((Type) b).baseType() : b;
	if (thisBase == Symtab.Double) {
	    if (bBase == Symtab.Double)
		return(b);
	    if ((bBase == Symtab.Character) ||
		(bBase == Symtab.Byte) ||
		(bBase == Symtab.Short) ||
		(bBase == Symtab.Integer) ||
		(bBase == Symtab.Long) ||
		(bBase == Symtab.Float))
		return(this);
	}
	else if (thisBase == Symtab.Float) {
	    if (bBase == Symtab.Double)
		return(b);
	    if (bBase == Symtab.Float)
		return(b);
	    if ((bBase == Symtab.Character) ||
		(bBase == Symtab.Byte) ||
		(bBase == Symtab.Short) ||
		(bBase == Symtab.Integer) ||
		(bBase == Symtab.Long))
		return(this);
	}
	else if (thisBase == Symtab.Long) {
	    if (bBase == Symtab.Double)
		return(b);
	    if (bBase == Symtab.Float)
		return(b);
	    if (bBase == Symtab.Long)
		return(b);
	    if ((bBase == Symtab.Character) ||
		(bBase == Symtab.Byte) ||
		(bBase == Symtab.Short) ||
		(bBase == Symtab.Integer))
		return(this);
	}
	else if ((thisBase == Symtab.Character) ||
		 (thisBase == Symtab.Byte) ||
		 (thisBase == Symtab.Short) ||
		 (thisBase == Symtab.Integer)) {
	    if (bBase == Symtab.Double)
		return(b);
	    if (bBase == Symtab.Float)
		return(b);
	    if (bBase == Symtab.Long)
		return(b);
	    if (bBase == Symtab.Integer)
		return(b);
	    if ((bBase == Symtab.Character) ||
		(bBase == Symtab.Byte) ||
		(bBase == Symtab.Short))
		return(Symtab.Integer.getType(thisDim));
	}
	else if (thisBase == bBase)
	    return(b);

	if (instanceOf(b))
	    return(b);

	// No promotion possible
	return(null);
    }
}
