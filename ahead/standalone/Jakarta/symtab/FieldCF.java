//                              -*- Mode: Java -*- 
// FieldCF.java --- 
// Author          : Bernie Lofaso
// Created On      : Tue May  6 16:34:23 1997
// Last Modified By: 
// Last Modified On: Fri May 07 10:53:33 1999
// Update Count    : 25
// Status          : Under Development
// 
// $Locker:  $
// $Log: FieldCF.java,v $
// Revision 1.4  2007-08-13 19:39:26  dsb
// reinstalling deleted files (sigh).
//
// Revision 1.2  2001/12/04 15:53:54  sarvela
// Removed redundant and error-prone carriage returns.
//
// Revision 1.1.1.1  1999/05/19 21:24:58  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.2  1999/05/19 21:24:58  lofaso
// Revised symbol table implementation.
//
// Revision 1.1.1.1  1999/02/18 16:15:44  lofaso
// Snapshot 2-18-99
//
// Revision 1.1.1.1  1997/12/15 21:00:27  lofaso
// Imported sources
//
// Revision 1.1.1.1  1997/08/25 20:06:31  lofaso
// Imported Java 1.1 sources
//
// Revision 1.2  1997/07/07 13:54:04  lofaso
// Modifications to add symbol table support.
//
// Revision 1.1.1.1  1997/05/22 21:30:13  lofaso
// baseline sources
//
// 

package Jakarta.symtab;

import java.io.DataInputStream;
import java.io.IOException;

public class FieldCF extends FieldInfo {
    private ClassFile class_file;
    private int access_flags;
    private int name_index;
    private int signature_index;
    private boolean constant_value;	// is value_index valid?
    private int value_index;

    public FieldCF(DataInputStream dis, ClassFile cf) throws IOException {
	int i;
	int attr_count;
	int attr_name_index;
	int attr_len;
	String attr_name;

	class_file = cf;
	access_flags = dis.readUnsignedShort();
	name_index = dis.readUnsignedShort();
	signature_index = dis.readUnsignedShort();

	attr_count = dis.readUnsignedShort();
	constant_value = false;
	for (i=0; i < attr_count; i++) {
	    attr_name_index = dis.readUnsignedShort();
	    attr_len = dis.readInt();
	    attr_name = ((CONSTANT_Asciz)
			 cf.getConstant(attr_name_index)).value;
	    if (attr_name.compareTo("ConstantValue") == 0) {
		value_index = dis.readUnsignedShort();
		constant_value = true;
	    }
	    else
		dis.skipBytes(attr_len);
	}
    }

    //**************************************************
    // Start of FieldInfo implementation
    //**************************************************

    public String getName() {
	return(class_file.getString(name_index));
    }

    public int getModifiers() { return(access_flags); }

    public Type getType() {
	return(Symtab.sigToType(class_file.getString(signature_index)));
    }

    // Mainly used to determine if an array and perhaps dimensions
    public String getFieldSignature() {
	return(class_file.getString(signature_index).replace('/', '.'));
    }
}
