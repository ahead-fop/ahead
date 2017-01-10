package Jakarta.symtab;

import Jakarta.util.Util;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

public abstract class MethodInfo extends DeclaredScope {
    protected Scope declaringEnv;
    protected Object userData;

    //**************************************************
    // Declaration interface
    //**************************************************

    // getName() and getFullName() implemented in subclasses

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

    public abstract void addDeclaration(Declaration decl);
    public abstract void expunge();


    public abstract int getModifiers();

    // Includes return type as well as argument types
    public abstract String getMethodSignature();

    public abstract String getArgumentSignature();

    public abstract Type getReturnType();

    protected Type[] getParameterTypes(String argSig) {
	int i, j;
	int dimCount;
	char ch;
	String type;
	Vector typeVec = new Vector();
	ClassInfo ci;
	Type[] result;

	if (argSig == null)
	    return(new Type[0]);
	argSig = argSig.replace('/', '.');
	i = argSig.indexOf('(') + 1;
	for (; i < argSig.length(); i++) {
	    dimCount = 0;
	    ch = argSig.charAt(i);
	    while (ch == '[') {
		dimCount++;
		ch = argSig.charAt(++i);
	    }
	    switch (ch) {
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
		j = argSig.indexOf(';', i+2);
		type = argSig.substring(i+1, j);
		i=j;
		ci = (ClassInfo) Symtab.instance().lookup(type);
		break;
	    case 'S':
		ci = Symtab.Short;
		break;
	    case 'Z':
		ci = Symtab.Boolean;
		break;
	    default:
		continue;
	    }

	    typeVec.addElement(ci.getType(dimCount));
	}
	result = new Type[typeVec.size()];
	typeVec.copyInto(result);
	return(result);
    }

    public abstract Type[] getParameterTypes();

    //**************************************************
    // Scan fields
    //**************************************************

    public abstract int getFieldCount();
    public abstract Enumeration getFieldCursor();
    public abstract FieldInfo[] getFields();

    //**************************************************
    // Scan classes
    //**************************************************

    public abstract int getClassCount();
    public abstract Enumeration getClassCursor();
    public abstract ClassInfo[] getClasses();

    //**************************************************
    // Locate a specific field
    //**************************************************

    public abstract FieldInfo findField(String name);

    //**************************************************
    // Locate a specific class
    //**************************************************

    public abstract ClassInfo findClass(String name);

    public String toString() {
	StringBuffer buffer = new StringBuffer();
	String msig;
	String sig;
	int cp;

	msig = getMethodSignature();
	cp = msig.indexOf(')');
	sig = msig.substring(cp + 1);
	buffer.append(Symtab.sigToString(sig));
	buffer.append(" ");
	buffer.append(getName());
	buffer.append("(");
	sig = msig.substring(1, cp);
	buffer.append(Symtab.sigToString(sig));
	buffer.append(")");

	return(buffer.toString());
    }


    //**************************************************
    // Primarily used for debugging.
    //**************************************************
    public void dump(PrintWriter out, int indentLevel) {
	Enumeration scanPtr;
	ClassInfo ci;
	FieldInfo fi;
	String indent = Symtab.indent[indentLevel];

	// Increase indent for contained objects
	indentLevel++;

	out.print(indent);
	out.println(toString());
	scanPtr = sortCursor (getFieldCursor()) ;
	if (scanPtr.hasMoreElements()) {
	    do {
		fi = (FieldInfo) scanPtr.nextElement();
		fi.dump(out, indentLevel);
	    } while (scanPtr.hasMoreElements());
	}

	scanPtr = sortCursor (getClassCursor()) ;
	if (scanPtr.hasMoreElements()) {
	    do {
		ci = (ClassInfo) scanPtr.nextElement();
		ci.dump(out, indentLevel);
	    } while (scanPtr.hasMoreElements());
	}
    }
}
