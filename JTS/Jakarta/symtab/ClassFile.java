//                              -*- Mode: Java -*- 
// ClassFile.java --- 
// Author          : Bernie Lofaso
// Created On      : Tue May  6 16:10:01 1997
// Last Modified By: 
// Last Modified On: Wed May 19 10:13:44 1999
// Update Count    : 87
// Status          : Under Development
// 
// $Locker:  $
// $Log: ClassFile.java,v $
// Revision 1.4  2007-08-13 19:39:26  dsb
// reinstalling deleted files (sigh).
//
// Revision 1.2  2001/12/04 15:53:48  sarvela
// Removed redundant and error-prone carriage returns.
//
// Revision 1.1.1.1  1999/05/19 21:24:52  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.2  1999/05/19 21:24:52  lofaso
// Revised symbol table implementation.
//
//
// 

package Jakarta.symtab;

import Jakarta.util.Util;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

public class ClassFile extends ClassInfoBase {
    private Object[] constant_pool;
    private int access_flags;
    private int this_class_index;
    private int super_class_index;
    private int[] interfaces;
    private FieldCF[] fields;
    private MethodCF[] methods;

    private boolean scannedForNested = false;
    private Vector nestedInterfaces;
    private Vector nestedClasses;

    private File classfile;
    private ZipFile zipFile;
    private ZipEntry zipEntry;
    private String className;		// simple name
    private String classNameFull;	// fully qualified name
    private boolean initialized = false;
    private DataInputStream dis;

    // ClassFile objects are initially created as uninitialized. The
    // initialization is performed when necessary. Class names passed to
    // constructors are fully qualified.

    public ClassFile(ZipFile zf, ZipEntry ze, String cname)
    throws FileNotFoundException, IOException {
	int lastDot;

	classfile = null;
	zipFile = zf;
	zipEntry = ze;
	classNameFull = cname;
	lastDot = cname.lastIndexOf('.');
	className = classNameFull.substring(lastDot+1);
	nestedInterfaces = null;
	nestedClasses = null;
    }


    public ClassFile(File cf, String cname)
    throws FileNotFoundException, IOException {
	int lastDot;

	classfile = cf;
	zipFile = null;
	zipEntry = null;
	classNameFull = cname;
	lastDot = cname.lastIndexOf('.');
	className = classNameFull.substring(lastDot+1);
	nestedInterfaces = null;
	nestedClasses = null;
    }


    //**********************************************************************
    // Initialization
    //**********************************************************************
    private void init()
    throws FileNotFoundException, IOException {
	int temp;
	int i;
	InputStream is;

	if (classfile != null)
	    is = new FileInputStream(classfile);
	else
	    is = zipFile.getInputStream(zipEntry);
	dis = new DataInputStream(is);

	// We set this flag now because creating MethodCF objects (and
	// perhaps other objects) will make call-backs to ClassFile
	// functions that shouldn't call init again.
	initialized = true;

	// Skip over the magic and version numbers
	dis.skip(8);

	// Read constant pool
	constant_pool = readConstantPool();

	// Read access flags and indicies for this class and superclass
	access_flags = dis.readUnsignedShort();
	this_class_index = dis.readUnsignedShort();
	super_class_index = dis.readUnsignedShort();

	// Read interfaces
	temp = dis.readUnsignedShort();	// interfaces count
	interfaces = new int[temp];
	for (i=0; i < temp; i++)
	    interfaces[i] = dis.readUnsignedShort();

	// Read fields
	fields = readFields();

	// Read methods
	methods = readMethods();

	is.close();
	// System.err.println(">>> Loaded class file: "+getName());
    }


    //**************************************************
    // Scan for nested classes/interfaces
    //**************************************************
    private void classInterfaceScan() {
	File directory;
	String[] fileNames;
	nestedClassFilter filter;
	String classNameRoot;
	int dot, i;
	String nclassName;
	int start, end;
	ClassFile cf;
	Enumeration scanPtr;
	String entryName;
	String prefix;
	int plen;
	ZipEntry entry;

	nestedClasses = new Vector();
	nestedInterfaces = new Vector();

	if (classfile != null) {
	    directory = new File(classfile.getParent());
	    dot = classfile.getName().length() - 6;	// strip ".class" off
	    classNameRoot = classfile.getName().substring(0, dot);
	    filter = new nestedClassFilter(classNameRoot + "$");
	    fileNames = directory.list(filter);
	    for (i=0; i < fileNames.length; i++) {
		start = classNameRoot.length() + 1;
		end = fileNames[i].length() - 6;
		nclassName = fileNames[i].substring(start, end);
		nclassName = getFullName() + "." + nclassName;
		try {
		    cf = new ClassFile(new File(directory, fileNames[i]),
				       nclassName);
		}
		catch (Exception e) {
		    Util.fatalError(e);
		    cf = null;
		}

		if ((cf.getModifiers() & ACC_INTERFACE) == 0)
		    nestedClasses.addElement(cf);
		else
		    nestedInterfaces.addElement(cf);
	    }
	}
	else {
	    // must be in a Zip file

	    // prefix is name of class of this zipEntry less the ".class"
	    entryName = zipEntry.getName();
	    prefix = entryName.substring(0, entryName.length() - 6);
	    plen = prefix.length();

	    // Now scan through the zip entries in the zip file. Matching
	    // entries have the prefix calculated above, a suffix of ".class",
	    // and middle which contains exactly one '$' in the first character
	    // position.
	    scanPtr = zipFile.entries();
	    while (scanPtr.hasMoreElements()) {
		entry = (ZipEntry) scanPtr.nextElement();
		entryName = entry.getName();
		if (! (entryName.startsWith(prefix) &&
		       entryName.endsWith(".class")))
		    continue;
		if (entryName.charAt(plen) != '$')
		    continue;
		if (entryName.indexOf('$', plen + 1) > 0)
		    continue;

		// Matches pattern
		nclassName = entryName.substring(plen+1,
						 entryName.length() - 6);
		if (! Character.isJavaIdentifierStart(nclassName.charAt(0)))
		    continue;
		nclassName = getFullName() + "." + nclassName;
		try {
		    cf = new ClassFile(zipFile, entry, nclassName);
		}
		catch (Exception e) {
		    Util.fatalError(e);
		    cf = null;
		}

		if ((cf.getModifiers() & ACC_INTERFACE) == 0)
		    nestedClasses.addElement(cf);
		else
		    nestedInterfaces.addElement(cf);
	    }
	}
    }

    private static class nestedClassFilter implements FilenameFilter {
	private String prefix;

	public nestedClassFilter(String pf) {
	    prefix = pf;
	}

	public boolean accept(File dir, String name) {
	    if (! (name.startsWith(prefix) && name.endsWith(".class")))
		return(false);
	    if (name.indexOf('$', prefix.length()) > 0)
		return(false);
	    if (Character.isJavaIdentifierStart(name.charAt(prefix.length())))
		return(true);
	    return(false);
	}
    }


    //**************************************************
    // Support routines
    //**************************************************

    private Object[] readConstantPool() throws IOException {
	int constant_pool_count;
	int i;
	Object[] cpool;
	int tag;
	int t1, t2;	// temp or utility variables
	byte[] cbuff = new byte[256];
	String str;

	constant_pool_count = dis.readUnsignedShort();
	cpool = new Object[constant_pool_count];
	cpool[0] = null;
	for (i=1; i < constant_pool_count; i++) {
	    tag = dis.readUnsignedByte();
	    switch (tag) {
	    case 7:	// CONSTANT_Class
		t1 = dis.readUnsignedShort();	// class index
		cpool[i] = new CONSTANT_Class(t1);
		break;
	    case 9:	// CONSTANT_Fieldref
		t1 = dis.readUnsignedShort();	// class index
		t2 = dis.readUnsignedShort();	// name/type index
		cpool[i] = new CONSTANT_Fieldref(t1, t2);
		break;
	    case 10:	// CONSTANT_Methodref
		t1 = dis.readUnsignedShort();	// class index
		t2 = dis.readUnsignedShort();	// name/type index
		cpool[i] = new CONSTANT_Methodref(t1, t2);
		break;
	    case 8:	// CONSTANT_String
		t1 = dis.readUnsignedShort();	// string index
		cpool[i] = new CONSTANT_String(t1);
		break;
	    case 3:	// CONSTANT_Integer
		t1 = dis.readInt();	// integer value
		cpool[i] = new CONSTANT_Integer(t1);
		break;
	    case 4:	// CONSTANT_Float
		cpool[i] = new CONSTANT_Float(dis.readFloat());
		break;
	    case 5:	// CONSTANT_Long
		cpool[i] = new CONSTANT_Long(dis.readLong());
		i++;	// takes up two entries
		break;
	    case 6:	// CONSTANT_Double
		cpool[i] = new CONSTANT_Double(dis.readDouble());
		i++;	// takes up two entries
		break;
	    case 11:	// CONSTANT_InterfaceMethodref
		t1 = dis.readUnsignedShort();	// class index
		t2 = dis.readUnsignedShort();	// name/type index
		cpool[i] = new CONSTANT_InterfaceMethodref(t1, t2);
		break;
	    case 12:	// CONSTANT_NameandType
		t1 = dis.readUnsignedShort();	// name index
		t2 = dis.readUnsignedShort();	// signature index
		cpool[i] = new CONSTANT_NameAndType(t1, t2);
		break;
	    case 1:	// CONSTANT_Asciz
		t1 = dis.readUnsignedShort();	// string length
		if (cbuff.length < t1)
		    cbuff = new byte[t1];
		dis.read(cbuff, 0, t1);
		str = new String(cbuff, 0, t1);
		cpool[i] = new CONSTANT_Asciz(str);
		break;
	    default:
		Util.fatalError("Invalid constant pool tag!");
	    }
	}

	// Need to find class objects and translate their fully qualified
	// name to use '.' as the hierarchical separator rather than '/'.
	CONSTANT_Class cc;
	CONSTANT_Asciz ca;

	for (i=1; i < constant_pool_count; i++) {
	    if (cpool[i] instanceof CONSTANT_Class) {
		cc = (CONSTANT_Class) cpool[i];
		ca = (CONSTANT_Asciz) cpool[cc.name_index];
		ca.value = ca.value.replace('/', '.');
	    }
	}

	return(cpool);
    }

    public void printConstant(PrintWriter ps, int index) {
	Object obj;
	CONSTANT_Asciz string;

	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		Util.fatalError(e);
	    }
	}

	obj = constant_pool[index];
	if (obj instanceof CONSTANT_Class) {
	    CONSTANT_Class cc = (CONSTANT_Class) obj;
	    string = (CONSTANT_Asciz) constant_pool[cc.name_index];
	}
	else if (obj instanceof CONSTANT_Asciz) {
	    string = (CONSTANT_Asciz) obj;
	}
	else if (obj instanceof CONSTANT_Fieldref) {
	    CONSTANT_Fieldref cf = (CONSTANT_Fieldref) obj;
	    string = (CONSTANT_Asciz) constant_pool[cf.name_and_type_index];
	}
	else if (obj instanceof CONSTANT_Methodref) {
	    CONSTANT_Methodref cm = (CONSTANT_Methodref) obj;
	    string = (CONSTANT_Asciz) constant_pool[cm.name_and_type_index];
	}
	else if (obj instanceof CONSTANT_String) {
	    CONSTANT_String cs = (CONSTANT_String) obj;
	    string = (CONSTANT_Asciz) constant_pool[cs.string_index];
	}
	else if (obj instanceof CONSTANT_Integer) {
	    CONSTANT_Integer ci = (CONSTANT_Integer) obj;
	    ps.print(String.valueOf(ci.value));
	    return;
	}
	else if (obj instanceof CONSTANT_Float) {
	    CONSTANT_Float cf = (CONSTANT_Float) obj;
	    ps.print(String.valueOf(cf.value));
	    return;
	}
	else if (obj instanceof CONSTANT_Long) {
	    CONSTANT_Long cl = (CONSTANT_Long) obj;
	    ps.print(String.valueOf(cl.value));
	    return;
	}
	else if (obj instanceof CONSTANT_Double) {
	    CONSTANT_Double cd = (CONSTANT_Double) obj;
	    ps.print(String.valueOf(cd.value));
	    return;
	}
	else if (obj instanceof CONSTANT_InterfaceMethodref) {
	    CONSTANT_InterfaceMethodref ci = (CONSTANT_InterfaceMethodref) obj;
	    string = (CONSTANT_Asciz) constant_pool[ci.name_and_type_index];
	}
	else if (obj instanceof CONSTANT_NameAndType) {
	    CONSTANT_NameAndType cn = (CONSTANT_NameAndType) obj;
	    string = (CONSTANT_Asciz) constant_pool[cn.name_index];
	}
	else {
	    Util.fatalError("Invalid constant");
	    string = null;
	}

	ps.print(string.value);
    }

    private FieldCF[] readFields() throws IOException {
	int fields_count;
	int i;
	FieldCF[] result;

	fields_count = dis.readUnsignedShort();
	result = new FieldCF[fields_count];
	for (i=0; i < fields_count; i++) {
	    result[i] = new FieldCF(dis, this);
	    result[i].setDeclaringEnv(this);
	}
	return(result);
    }

    private MethodCF[] readMethods() throws IOException {
	int methods_count;
	int i;
	MethodCF[] result;

	methods_count = dis.readUnsignedShort();
	result = new MethodCF[methods_count];
	for (i=0; i < methods_count; i++) {
	    result[i] = new MethodCF(dis, this);
	    result[i].setDeclaringEnv(this);
	}
	return(result);
    }

    public Object getConstant(int index) {
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(null);
	    }
	}
	return(constant_pool[index]);
    }

    public String getString(int index) {
	CONSTANT_Asciz str;

	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return("");
	    }
	}
	str = (CONSTANT_Asciz) constant_pool[index];
	return(str.value);
    }

    //**************************************************
    // Start of ClassInfo implementation
    //**************************************************

    // Part of declaration interface
    public String getName() { return(className); }
    public String getFullName() { return(classNameFull); }
    public CompilationUnit getCompilationUnit() { return(null); }

    //**************************************************
    // Scope interface (addDeclaration() and expunge())
    //**************************************************

    // addDeclaration is only called when executing one of the symbol table
    // creation routines of an AST, therefore it is not applicable to a
    // ClassFile object. We do not need to implement any sort of add methods
    // for this context.
    public void addDeclaration(Declaration decl) {}

    // We probably don't need to do anything for an expunge, but we'll null out
    // a few pointers just in case. This would allow the reclamation of most
    // of the memory associated with this object even if someone keeps a
    // pointer to it.
    public void expunge() {
	constant_pool = null;
	interfaces = null;
	fields = null;
	methods = null;
    }


    public ClassInfo getSuperclass() {
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(null);
	    }
	}

	if (super_class_index == 0)
	    return(null);	// only for java.lang.Object
	CONSTANT_Class cc = (CONSTANT_Class) constant_pool[super_class_index];
	CONSTANT_Asciz ca = (CONSTANT_Asciz) constant_pool[cc.name_index];

	Declaration dcl = Symtab.instance().lookup(ca.value);
	return((ClassInfo) dcl);
    }

    public int getModifiers() {
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(0);
	    }
	}
	return(access_flags);
    }

    //**************************************************
    // Scan interfaces implemented
    //**************************************************

    public int getInterfaceCount() {
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(0);
	    }
	}
	return(interfaces.length);
    }

    public Enumeration getInterfaceCursor() {
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(null);
	    }
	}
	return(new interfaceEnumeration(interfaces, constant_pool));
    }

    public ClassInfo[] getInterfaces() {
	ClassInfo[] result;
	CONSTANT_Class cc;
	CONSTANT_Asciz ca;
	Declaration dcl;
	int i;

	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(null);
	    }
	}

	result = new ClassInfo[interfaces.length];
	for (i=0; i < interfaces.length; i++) {
	    cc = (CONSTANT_Class) constant_pool[interfaces[i]];
	    ca = (CONSTANT_Asciz) constant_pool[cc.name_index];
	    dcl = Symtab.instance().lookup(ca.value);
	    result[i] = (ClassInfo) dcl;
	}
	return(result);
    }

    //**************************************************
    // Scan nested interfaces
    //**************************************************

    public int getNestedInterfaceCount() {
	if (! scannedForNested) {
	    classInterfaceScan();
	    scannedForNested = true;
	}
	return(nestedInterfaces.size());
    }

    public Enumeration getNestedInterfaceCursor() {
	if (! scannedForNested) {
	    classInterfaceScan();
	    scannedForNested = true;
	}
	return(nestedInterfaces.elements());
    }

    public ClassInfo[] getNestedInterfaces() {
	ClassInfo[] ci;

	if (! scannedForNested) {
	    classInterfaceScan();
	    scannedForNested = true;
	}

	ci = new ClassInfo[nestedInterfaces.size()];
	nestedInterfaces.copyInto(ci);
	return(ci);
    }


    //**************************************************
    // Scan fields
    //**************************************************

    public int getFieldCount() {
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(0);
	    }
	}
	return(fields.length);
    }

    public Enumeration getFieldCursor() {
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(null);
	    }
	}
	return(new ArrayEnumeration(fields));
    }

    public FieldInfo[] getFields() {
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(null);
	    }
	}
	return(fields);
    }

    //**************************************************
    // Scan methods
    //**************************************************

    public int getMethodCount() {
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(0);
	    }
	}
	return(methods.length);
    }

    public Enumeration getMethodCursor() {
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(null);
	    }
	}
	return(new ArrayEnumeration(methods));
    }

    public MethodInfo[] getMethods() {
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(null);
	    }
	}
	return(methods);
    }

    //**************************************************
    // Scan classes
    //**************************************************

    public int getClassCount() {
	if (! scannedForNested) {
	    classInterfaceScan();
	    scannedForNested = true;
	}
	return(nestedClasses.size());
    }

    public Enumeration getClassCursor() {
	if (! scannedForNested) {
	    classInterfaceScan();
	    scannedForNested = true;
	}
	return(nestedClasses.elements());
    }

    public ClassInfo[] getClasses() {
	ClassInfo[] ci;

	if (! scannedForNested) {
	    classInterfaceScan();
	    scannedForNested = true;
	}

	ci = new ClassInfo[nestedClasses.size()];
	nestedClasses.copyInto(ci);
	return(ci);
    }

    //**************************************************
    // Locate specific sub-objects.
    //**************************************************

    //**************************************************
    // findField
    //**************************************************
    public FieldInfo findField(String fieldName)
        throws BadSymbolNameException {
	if (fieldName.indexOf('.') >= 0)
	    throw new BadSymbolNameException("Field name " + fieldName +
					     " is not simple.");
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(null);
	    }
	}

	// Search for local definition
	for (int i=0; i < fields.length; i++)
	    if (fieldName.compareTo(fields[i].getName()) == 0)
		return(fields[i]);

	// Return search of inheritance and enclosing chains
	return(findFieldNonLocal(fieldName));
    }


    //**************************************************
    // findMethod
    //**************************************************
    public MethodInfo findMethod(String name, String arg_sig)
        throws BadSymbolNameException {
	int i;

	if (name.indexOf('.') >= 0)
	    throw new BadSymbolNameException("Method name " + name +
					     " is not simple.");
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace();
		return(null);
	    }
	}

	// Search for local definition
	for (i=0; i < methods.length; i++)
	    if ((name.compareTo(methods[i].getName()) == 0) &&
		(arg_sig.compareTo(methods[i].getArgumentSignature()) == 0))
		return(methods[i]);

	// Check local methods with coercion of args
	for (i=0; i < methods.length; i++) {
	    if (name.compareTo(methods[i].getName()) != 0)
		continue;
	    if (sigsCompatible(methods[i].getArgumentSignature(), arg_sig))
		return(methods[i]);
	}

	// Return search of inheritance and enclosing chains
	return(findMethodNonLocal(name, arg_sig));
    }


    //**************************************************
    // findInterface
    //**************************************************
    public ClassInfo findInterface(String name)
        throws BadSymbolNameException {
	CONSTANT_Class cc;
	CONSTANT_Asciz ca;
	Declaration dcl;

	if (name.indexOf('.') >= 0)
	    throw new BadSymbolNameException("Interface name " + name +
					     " is not simple.");
	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(null);
	    }
	}

	// Search for local definition
	for (int i=0; i < interfaces.length; i++) {
	    cc = (CONSTANT_Class) constant_pool[interfaces[i]];
	    ca = (CONSTANT_Asciz) constant_pool[cc.name_index];
	    if (name.compareTo(ca.value) == 0) {
		dcl = Symtab.instance().lookup(ca.value);
		return((ClassInfo) dcl);
	    }
	}

	// Return search of inheritance and enclosing chains
	return(findInterfaceNonLocal(name));
    }


    //**************************************************
    // findNestedInterface
    //**************************************************
    public ClassInfo findNestedInterface(String name)
	throws BadSymbolNameException {
	Enumeration scanPtr;
	ClassInfo ci;

	if (name.indexOf('.') >= 0)
	    throw new BadSymbolNameException("Nested interface name " + name +
					     " is not simple.");
	if (! scannedForNested) {
	    classInterfaceScan();
	    scannedForNested = true;
	}

	scanPtr = nestedInterfaces.elements();
	while (scanPtr.hasMoreElements()) {
	    ci = (ClassInfo) scanPtr.nextElement();
	    if (name.compareTo(ci.getName()) == 0)
		return(ci);
	}
	return(findNestedInterfaceNonLocal(name));
    }


    //**************************************************
    // findClass
    //**************************************************
    public ClassInfo findClass(String name)
        throws BadSymbolNameException {
	Enumeration scanPtr;
	ClassInfo ci;

	if (name.indexOf('.') >= 0)
	    throw new BadSymbolNameException("Class name " + name +
					     " is not simple.");

	if (! scannedForNested) {
	    classInterfaceScan();
	    scannedForNested = true;
	}

	scanPtr = nestedClasses.elements();
	while (scanPtr.hasMoreElements()) {
	    ci = (ClassInfo) scanPtr.nextElement();
	    if (name.compareTo(ci.getName()) == 0)
		return(ci);
	}
	return(findClassNonLocal(name));

    }

    public boolean instanceOf(ClassInfo c) {
	ClassInfo ci;
	Enumeration scanPtr;

	if (! initialized) {
	    try {
		init();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		return(false);
	    }
	}

	if (getFullName() == c.getFullName())
	    return(true);

	scanPtr = getInterfaceCursor();
	while (scanPtr.hasMoreElements()) {
	    ci = (ClassInfo) scanPtr.nextElement();
	    if (c == ci)
		return(true);
	}

	ci = getSuperclass();
	if (ci != null)
	    return(ci.instanceOf(c));

	return(false);
    }
}

class interfaceEnumeration implements Enumeration {
    int[] interfaces;
    int index;
    Object[] constant_pool;

    public interfaceEnumeration(int[] a, Object[] cp) {
	interfaces = a;
	constant_pool = cp;
	index = 0;
    }

    public boolean hasMoreElements() {
	return(index < interfaces.length);
    }

    public Object nextElement() {
	CONSTANT_Class cc;
	CONSTANT_Asciz ca;
	Declaration dcl;

	if (index == interfaces.length)
	    throw new NoSuchElementException();
	cc = (CONSTANT_Class) constant_pool[interfaces[index++]];
	ca = (CONSTANT_Asciz) constant_pool[cc.name_index];
	dcl = Symtab.instance().lookup(ca.value);
	return(dcl);
    }
}
