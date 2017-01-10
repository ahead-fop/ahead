//                              -*- Mode: Java -*- 
// MethodCF.java --- 
// Author          : Bernie Lofaso
// Created On      : Tue May  6 16:43:38 1997
// Last Modified By: 
// Last Modified On: Mon May 17 08:42:38 1999
// Update Count    : 30
// Status          : Under Development
// 
// $Locker:  $
// $Log: MethodCF.java,v $
// Revision 1.4  2007-08-13 19:39:26  dsb
// reinstalling deleted files (sigh).
//
// Revision 1.2  2001/12/04 15:53:55  sarvela
// Removed redundant and error-prone carriage returns.
//
// Revision 1.1.1.1  1999/05/19 21:25:02  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.2  1999/05/19 21:25:01  lofaso
// Revised symbol table implementation.
//
// Revision 1.1.1.1  1999/02/18 16:15:44  lofaso
// Snapshot 2-18-99
//
// Revision 1.3  1998/07/07 18:58:37  lofaso
// Removed some carriage returns accidentally checkin into archive.
//
// Revision 1.1.1.1  1997/12/15 21:00:27  lofaso
// Imported sources
//
// Revision 1.1.1.1  1997/08/25 20:06:32  lofaso
// Imported Java 1.1 sources
//
// Revision 1.2  1997/07/07 13:54:07  lofaso
// Modifications to add symbol table support.
//
// Revision 1.1.1.1  1997/05/22 21:30:13  lofaso
// baseline sources
//
// 

package Jakarta.symtab;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Enumeration;

//**********************************************************************
// NOTE: For the moment we do not support classes and blocks nested in
// MethodCF objects. It is unclear how such entities are encoded in the
// classfile format (if they are encoded at all).
//**********************************************************************

public class MethodCF extends MethodInfo {
    private ClassFile class_file;
    private int access_flags;
    private int name_index;
    private int signature_index;
    private String argSig;
    private FieldInfo[] fields;		// actually type MethodField
    private ClassInfo[] classes;

    public MethodCF(DataInputStream dis, ClassFile cf) throws IOException {
	int attr_count;
	int code_attr_count;
	int len;
	int attr_name_index;
	String attr_name;
	int lvtab_len;

	class_file = cf;
	access_flags = dis.readUnsignedShort();
	name_index = dis.readUnsignedShort();
	signature_index = dis.readUnsignedShort();

	// In some cases this might not get set otherwise
	fields = new FieldInfo[0];

	// Read attributes of method
	attr_count = dis.readUnsignedShort();
	for (int i=0; i < attr_count; i++) {
	    attr_name_index = dis.readUnsignedShort();
	    attr_name = class_file.getString(attr_name_index);
	    if (attr_name.compareTo("Code") == 0) {
		// Code attribute

		// skip attribute_length, max_stack, and max_locals
		dis.skipBytes(8);

		// skip byte codes
		len = dis.readInt();
		dis.skipBytes(len);

		// skip exception table
		len = dis.readUnsignedShort();
		dis.skipBytes(len * 8);

		// Read attributes of Code
		code_attr_count = dis.readUnsignedShort();
		for (int j=0; j < code_attr_count; j++) {
		    attr_name_index = dis.readUnsignedShort();
		    attr_name = class_file.getString(attr_name_index);
		    if (attr_name.compareTo("LocalVariableTable") == 0) {
			// LocalVariableTable attribute
			dis.skipBytes(4);	// skip attr length

			// Read and process local variable table
			lvtab_len = dis.readUnsignedShort();
			fields = new MethodField[lvtab_len];
			for (int k=0; k < lvtab_len; k++) {
			    fields[k] = new MethodField(dis, class_file);
			    fields[k].setDeclaringEnv(this);
			}
		    }
		    else {
			// non-LocalVariableTable attributes
			len = dis.readInt();
			dis.skipBytes(len);
		    }
		}
	    }
	    else {
		// non-Code attributes
		len = dis.readInt();
		dis.skipBytes(len);
	    }
	}

	// Set argument signature
	argSig = getMethodSignature();
	argSig = argSig.substring(0, argSig.indexOf(')') + 1);
	argSig = argSig.replace('/', '.');
    }

    //**************************************************
    // Declaration interface
    //**************************************************

    public String getName() {
	return(class_file.getString(name_index));
    }

    public String getFullName() {
	return(class_file.getFullName() + "." + getName());
    }

    //**************************************************
    // Scope interface
    //**************************************************

    public void addDeclaration(Declaration decl) {
	// There is no way to add members to a class file method except
	// at construction time, so this method should do nothing.
    }

    public void expunge() {
	for (int i=0; i < classes.length; i++)
	    classes[i].expunge();

	class_file = null;
	fields = null;
	classes = null;
    }

    // end Scope interface

    public int getModifiers() { return(access_flags); }

    // Includes return type as well as argument types
    public String getMethodSignature() {
	return(class_file.getString(signature_index));
    }

    public String getArgumentSignature() { return(argSig); }

    public Type getReturnType() {
	return(Symtab.sigToType(class_file.getString(signature_index)));
    }

    public Type[] getParameterTypes() {
	return(getParameterTypes(argSig));
    }

    //**************************************************
    // Scan fields
    //**************************************************

    public int getFieldCount() { return(fields.length); }

    public Enumeration getFieldCursor() {
	return(new ArrayEnumeration(fields));
    }

    public FieldInfo[] getFields() { return(fields); }

    //**************************************************
    // Scan classes
    //**************************************************

    public int getClassCount() { return(0); }
    public Enumeration getClassCursor() {
	return(new dummyEnumerator());
    }
    public ClassInfo[] getClasses() { return(new ClassInfo[0]); }

    //**************************************************
    // Locate a specific field
    //**************************************************

    public FieldInfo findField(String name) {
	for (int i=0; i < fields.length; i++)
	    if (name.compareTo(fields[i].getName()) == 0)
		return(fields[i]);
	return(null);
    }

    //**************************************************
    // Locate a specific class
    //**************************************************

    public ClassInfo findClass(String name) { return(null); }
}


//**********************************************************************
// Since the fields of a method are in a different format from fields of
// a class, this class is necessary to implement the FieldInfo abstract
// class in a different fashion.
//**********************************************************************
class MethodField extends FieldInfo {
    ClassFile class_file;
    int name_index;
    int signature_index;

    public MethodField(DataInputStream dis, ClassFile cf) throws IOException {
	class_file = cf;

	// Skip the start_pc and length fields
	dis.skipBytes(4);

	name_index = dis.readUnsignedShort();
	signature_index = dis.readUnsignedShort();

	// Skip slot field
	dis.skipBytes(2);
    }

    //**************************************************
    // Start of FieldInfo implementation
    //**************************************************

    public String getName() {
	return(class_file.getString(name_index));
    }

    // modifiers don't make sense for a method field.
    public int getModifiers() { return(0); }

    public Type getType() {
	return(Symtab.sigToType(class_file.getString(signature_index)));
    }

    // Mainly used to determine if an array and perhaps dimensions
    public String getFieldSignature() {
	return(class_file.getString(signature_index));
    }
}
