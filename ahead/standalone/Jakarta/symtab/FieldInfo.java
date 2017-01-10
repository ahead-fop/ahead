package Jakarta.symtab;

import java.io.PrintWriter;

public abstract class FieldInfo extends AbstractNamed implements Declaration {
    protected Scope declaringEnv;
    protected Object userData;

    //**************************************************
    // Declaration interface
    //**************************************************

    public abstract String getName();

    public String getFullName() {
	return(declaringEnv.getFullName() + "." + getName());
    }

    public Scope getDeclaringEnv() { return(declaringEnv); }
    public void setDeclaringEnv(Scope env) {
	if (declaringEnv == null)
	    declaringEnv = env;
    }
    public Object getUserData() { return(userData); }
    public void setUserData(Object data) { userData = data; }
    public CompilationUnit getCompilationUnit() {
	return(declaringEnv.getCompilationUnit());
    }

    // end Declaration interface

    public abstract int getModifiers();

    public abstract Type getType();

    public abstract String getFieldSignature();

    //**************************************************
    // Primarily used for debugging. This overrides toString() in Object.
    //**************************************************
    public String toString() {
	StringBuffer buffer = new StringBuffer();
	ClassInfo cls;
	String sig;

	sig = getFieldSignature();
	buffer.append(Symtab.sigToString(sig));
	buffer.append(' ');
	buffer.append(getName());
	buffer.append(';');

	return(buffer.toString());
    }

    //**************************************************
    // Primarily used for debugging.
    //**************************************************
    public void dump(PrintWriter out, int indentLevel) {
	out.print(Symtab.indent[indentLevel]);
	out.println(toString());
    }
}
