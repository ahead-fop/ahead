//                              -*- Mode: Java -*- 
// ClassInfo.java --- 
// Author          : Bernie Lofaso
// Created On      : Mon Mar 29 15:40:34 1999
// Last Modified By: 
// Last Modified On: Thu May 13 11:28:38 1999
// Update Count    : 37
// Status          : Under Development
// 
// $Locker:  $
// $Log: ClassInfo.java,v $
// Revision 1.4  2007-08-13 19:39:26  dsb
// reinstalling deleted files (sigh).
//
// Revision 1.2  2001/12/04 15:53:51  sarvela
// Removed redundant and error-prone carriage returns.
//
// Revision 1.1.1.1  1999/05/19 21:24:54  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.2  1999/05/19 21:24:53  lofaso
// Revised symbol table implementation.
//
// 

package Jakarta.symtab;

import java.util.Enumeration;

public interface ClassInfo extends Scope, Declaration {
    // Access flags constants
    public static final int ACC_PUBLIC =	0x0001;
    public static final int ACC_PRIVATE =	0x0002;
    public static final int ACC_PROTECTED =	0x0004;
    public static final int ACC_STATIC =	0x0008;
    public static final int ACC_FINAL =		0x0010;
    public static final int ACC_SYNCHRONIZED =	0x0020;
    public static final int ACC_THREADSAFE =	0x0040;
    public static final int ACC_TRANSIENT =	0x0080;
    public static final int ACC_NATIVE =	0x0100;
    public static final int ACC_INTERFACE =	0x0200;
    public static final int ACC_ABSTRACT =	0x0400;

    //**************************************************
    // Get a Type object
    //**************************************************
    public Type getType(int dim);

    //**************************************************
    // Class specific methods
    //**************************************************

    public ClassInfo getSuperclass();

    public int getModifiers();

    //**************************************************
    // Scan interfaces implemented
    //**************************************************

    public int getInterfaceCount();
    public Enumeration getInterfaceCursor();
    public ClassInfo[] getInterfaces();

    //**************************************************
    // Scan nested interfaces
    //**************************************************

    public int getNestedInterfaceCount();
    public Enumeration getNestedInterfaceCursor();
    public ClassInfo[] getNestedInterfaces();

    //**************************************************
    // Scan fields
    //**************************************************

    public int getFieldCount();
    public Enumeration getFieldCursor();
    public FieldInfo[] getFields();

    //**************************************************
    // Scan methods
    //**************************************************

    public int getMethodCount();
    public Enumeration getMethodCursor();
    public MethodInfo[] getMethods();

    //**************************************************
    // Scan classes
    //**************************************************

    public int getClassCount();
    public Enumeration getClassCursor();
    public ClassInfo[] getClasses();

    //**************************************************
    // Locate specific sub-objects.
    //**************************************************

    public FieldInfo findField(String fieldName)
	throws BadSymbolNameException;

    public MethodInfo findMethod(String name, String arg_sig)
	throws BadSymbolNameException;
    public MethodInfo findMethod(String name, ClassInfo[] arg_types)
	throws BadSymbolNameException;

    public ClassInfo findInterface(String name)
	throws BadSymbolNameException;

    public ClassInfo findNestedInterface(String name)
	throws BadSymbolNameException;

    public ClassInfo findClass(String name)
	throws BadSymbolNameException;

    //**************************************************
    // Determine inheritance and assignment relationships
    //**************************************************

    public boolean instanceOf(ClassInfo parent);

    public boolean ancestorOf(ClassInfo child);

    public boolean assignableTo(ClassInfo parent);

    public ClassInfo promotesTo(ClassInfo b);
}
