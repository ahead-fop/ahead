package Jakarta.symtab;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

public class BlockScope extends DeclaredScope {
    protected Scope declaringEnv;
    protected Object userData;
    protected Vector scopes;
    protected Vector fields;

    public BlockScope() {
	scopes = new Vector();
	fields = new Vector();
    }

    //**************************************************
    // Declaration interface
    //**************************************************
    
    public String getName() { return(""); }
    public String getFullName() { return(""); }
    public Scope getDeclaringEnv() { return(declaringEnv); }
    public void setDeclaringEnv(Scope declEnv) {
	if (declaringEnv == null)
	    declaringEnv = declEnv;
    }
    public Object getUserData() { return(userData); }
    public void setUserData(Object data) { userData = data; }
    public CompilationUnit getCompilationUnit() {
	return(declaringEnv.getCompilationUnit());
    }

    //**************************************************
    // Scope interface
    //**************************************************

    public String getScopeName() { return(""); }

    public void addDeclaration(Declaration decl) {
	if (decl instanceof Scope)
	    scopes.addElement(decl);
	else if (decl instanceof FieldInfo)
	    fields.addElement(decl);
	decl.setDeclaringEnv(this);
    }

    public void expunge() {
	Enumeration scanPtr;
	Scope scope;

	scanPtr = scopes.elements();
	while (scanPtr.hasMoreElements()) {
	    scope = (Scope) scanPtr.nextElement();
	    scope.expunge();
	}

	scopes = null;
	declaringEnv = null;
    }


    //**************************************************
    // Scan fields - this is used by MethodInfo getFieldCursor().
    //**************************************************
    public Enumeration getFieldCursor() {
	return(fields.elements());
    }

    //**************************************************
    // Locate a specific field
    //**************************************************

    public FieldInfo findField(String name)
        throws BadSymbolNameException {
	Enumeration scanPtr;
	FieldInfo item;

	if (name.indexOf('.') >= 0)
	    throw new BadSymbolNameException("Field name " + name +
					     " is not simple.");

	scanPtr = fields.elements();
	while (scanPtr.hasMoreElements()) {
	    item = (FieldInfo) scanPtr.nextElement();
	    if (name.compareTo(item.getName()) == 0)
		return(item);
	}

	return(null);
    }

    //**************************************************
    // Locate a specific class
    //**************************************************

    public ClassInfo findClass(String name)
        throws BadSymbolNameException {
	Enumeration scanPtr;
	Scope scope;

	if (name.indexOf('.') >= 0)
	    throw new BadSymbolNameException("Class name " + name +
					     " is not simple.");

	scanPtr = scopes.elements();
	while (scanPtr.hasMoreElements()) {
	    scope = (Scope) scanPtr.nextElement();
	    if (! (scope instanceof ClassInfo))
		continue;
	    if (name.compareTo(scope.getName()) == 0)
		return((ClassInfo) scope);
	}

	return(null);
    }

    public void dump(PrintWriter out, int indentLevel) {}
}
