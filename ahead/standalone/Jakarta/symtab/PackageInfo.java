package Jakarta.symtab;

import Jakarta.util.Debug ;
import Jakarta.util.LineWriter ;
import Jakarta.util.Util ;

import java.io.File;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class PackageInfo extends DeclaredScope {
    private Scope declaringEnv = null;
    private Object userData;
    private String pkgName;	// simple name
    private String pkgNameFull;	// fully qualified name
    private String[] nameComps;	// fully qualified name
    PackageInfo next = null;	// for subpackage list

    // The following is a list of directories which correspond to this
    // package or Zip files with appropriate directory entries.
    protected Vector roots;

    protected Hashtable classes;

    PackageInfo subPackages = null;

    //**************************************************
    // The package name (n) is fully qualified.
    //**************************************************
    public PackageInfo(String n) {
	classes = new Hashtable();

	// Special case for default package
	n = n.trim();
	if ((n == null) || (n.length() == 0)) {
	    roots = Symtab.instance().getRoots();
	    pkgName = "";
	    pkgNameFull = pkgName;
	    nameComps = new String[] { pkgName };
	    return;
	}

	nameComps = Util.findComponents(n);
	pkgName = nameComps[nameComps.length - 1];
	pkgNameFull = n;
    }


    //**************************************************
    // Used by ClassInfo objects in this scope to locate their disk
    // files (if any present).
    //**************************************************
    public Vector getRoots() { return(roots); }


    //**************************************************
    // Declaration interface
    //**************************************************

    public String getName() { return(pkgName); }
    public String getFullName() { return(pkgNameFull); }
    public Scope getDeclaringEnv() { return(declaringEnv); }
    public void setDeclaringEnv(Scope declEnv) {
	Vector declEnvRoots;
	Enumeration root_csr;
	Object rootObj;
	File dir_file;
	ZipFile zipFile;
	String fname;
	int i;

	// Can only set declaring environment once.
	if (declaringEnv == null)
	    declaringEnv = declEnv;

	// We can't set our 'roots' until we know our declaring environment.

	// This is to avoid recalculating 'roots' for the default package
	// when it is re-declared on pass 2 of building the symbol table.
	if (roots != null)
	    return;

	// build 'roots'
	roots = new Vector();
	declEnvRoots = ((PackageInfo) declEnv).getRoots();
	root_csr = declEnvRoots.elements();
	while (root_csr.hasMoreElements()) {
	    rootObj = root_csr.nextElement();
	    if (rootObj instanceof File) {
		dir_file = (File) rootObj;
		dir_file = new File(dir_file, pkgName);
		if (! (dir_file.exists() && dir_file.isDirectory()))
		    continue;

		// We've found a dir for this package
		roots.addElement(dir_file);
	    }
	    else {	// must be instance of ZipFile
		zipFile = (ZipFile) rootObj;
		fname = pkgNameFull.replace('.', '/') + "/";

		if (! hasZipDirectory (zipFile, fname))
		    continue ;

		roots.addElement(zipFile);
	    }
	}
    }

    public Object getUserData() { return(userData); }
    public void setUserData(Object data) { userData = data; }

    public CompilationUnit getCompilationUnit() { return(null); }

    //**************************************************
    // Scope interface
    //**************************************************

    public void addDeclaration(Declaration decl) {
	if (decl instanceof PackageInfo) {
	    PackageInfo sp = (PackageInfo) decl;
	    PackageInfo ptr;

	    // Check if we already have this declared (from first pass)
	    ptr = subPackages;
	    while (ptr != null) {
		if (ptr == sp)
		    return;
		ptr = ptr.next;
	    }
	    sp.next = subPackages;
	    subPackages = sp;
	}
	else if (decl instanceof ClassInfo) {
	    ClassInfo ci = (ClassInfo) decl;

	    // Check if we already have this declared (from first pass)
	    if (classes.contains(ci))
		return;
	    classes.put(ci.getName(), ci);
	}
	else
	    Util.fatalError("Attempt to add improper type '" +
			    decl.getClass().getName() +
			    "' to PackageInfo " + pkgName);
	decl.setDeclaringEnv(this);
    }

    //**************************************************
    // Delete this symbol table object and all in its context.
    //**************************************************
    public void expunge() {
	Enumeration scanPtr;
	Scope scope;

	// Expunge classes
	scanPtr = getClassCursor();
	while (scanPtr.hasMoreElements()) {
	    scope = (Scope) scanPtr.nextElement();
	    scope.expunge();
	}

	// Expunge sub-packages
	scanPtr = getSubPackageCursor();
	while (scanPtr.hasMoreElements()) {
	    scope = (Scope) scanPtr.nextElement();
	    scope.expunge();
	}
    }


    //**************************************************
    // Scan classes
    //**************************************************

    public int getClassCount() { return(classes.size()); }
    public Enumeration getClassCursor() { return(classes.elements()); }
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
    // Scan subpackages
    //**************************************************

    public int getSubPackageCount() {
	int count = 0;
	PackageInfo ptr = subPackages;

	while (ptr != null) {
	    count++;
	    ptr = ptr.next;
	}
	return(count);
    }

    public Enumeration getSubPackageCursor() {
	return(new spEnum(subPackages));
    }

    public PackageInfo[] getSubPackages() {
	int count = getSubPackageCount();
	PackageInfo[] result = new PackageInfo[count];
	PackageInfo ptr = subPackages;

	for (int i=0; i < result.length; i++) {
	    result[i] = ptr;
	    ptr = ptr.next;
	}
	return(result);
    }


    //**************************************************
    // Find declarations. These return null if no class/package can be found.
    //**************************************************

    public ClassInfo findClass(String cname) {

	debug.println (
	    "findClass scanning \""
	    + getFullName ()
	    + "\" for \""
	    + cname
	    + '"'
	) ;

	ClassInfo cls;
	Enumeration dir_csr;
	Object dirObj;
	File class_file;
	File dir_file;
	ZipEntry zipEntry;
	ZipFile zipFile;
	String fname;
	String cf_name;

	// Check cache first
	cls = (ClassInfo) classes.get(cname);

	// If necessary, see if class resides on disk
	if (cls == null) {
	    dir_csr = roots.elements();
	    cf_name = cname + ".class";

	    // iterate over components of 'roots'
	    while (dir_csr.hasMoreElements()) {
		dirObj = dir_csr.nextElement();
		if (dirObj instanceof File) {
		    dir_file = (File) dirObj;
		    class_file = new File(dir_file, cf_name);
		    if ((! class_file.exists()) || class_file.isDirectory())
			continue;

		    if (pkgName.length() != 0)
			fname = pkgNameFull + "." + cname;
		    else
			fname = cname;

		    // File exists. Set component's type to new ClassInfo
		    // object. Also add new ClassInfo object to local table.
		    try {
			cls = new ClassFile(class_file, fname);
		    }
		    catch (Exception e) {
			e.printStackTrace(System.err);
			cls = null;	// compiler pacifier.
			break;
		    }
		    addDeclaration(cls);
		    break;
		}
		else {	// must be instance of ZipFile
		    zipFile = (ZipFile) dirObj;
		    if (pkgName.length() != 0)
			fname = pkgNameFull + "." + cname;
		    else
			fname = cname;
		    zipEntry = zipFile.getEntry(fname.replace('.', '/') +
						".class");
		    if ((zipEntry == null) || zipEntry.isDirectory())
			continue;

		    // Class entry exists. Set component's type to new
		    // ClassInfo object. Also add new ClassInfo object to
		    // local table.
		    try {
			cls = new ClassFile(zipFile, zipEntry, fname);
		    }
		    catch (Exception e) {
			e.printStackTrace(System.err);
			cls = null;	// compiler pacifier.
			break;
		    }
		    addDeclaration(cls);
		    break;
		}
	    }
	}

	debug.println (
	    "findClass found "
	    + (cls != null ? "\"" + cls.getFullName () +'"' : "null")
	) ;

	return(cls);
    }

    public PackageInfo findSubPackage(String pname) {

	debug.println (
	    "findSubPackage scanning \""
	    + getFullName ()
	    + "\" for \""
	    + pname
	    + '"'
	) ;

	PackageInfo pkg;
	Enumeration dir_csr;
	Object dirObj;
	File pkgDir;
	File dir_file;
	ZipFile zipFile;
	String fname;
	String pnameFull;

	// Check cache first
	pkg = subPackages;
	while (pkg != null) {
	    if (pname.compareTo(pkg.getName()) == 0)
		break;
	    pkg = pkg.next;
	}

	// If necessary, see if sub-package resides on disk
	if (pkg == null) {
	    dir_csr = roots.elements();

	    // iterate over components of 'roots'
	    while (dir_csr.hasMoreElements()) {
		dirObj = dir_csr.nextElement();
		if (dirObj instanceof File) {
		    dir_file = (File) dirObj;
		    debug.println ("findSubPackage in directory " + dir_file);
		    pkgDir = new File(dir_file, pname);
		    if (! (pkgDir.exists() && pkgDir.isDirectory())) {
			debug.println (pkgDir.toString() + " not found") ;
			continue;
		    }

		    debug.println (pkgDir.toString() + " found as directory") ;

		    // File exists. Add new PackageInfo object to local table.
		    if (pkgNameFull.length() == 0)
			pnameFull = pname;
		    else
			pnameFull = pkgNameFull + "." + pname;
		    try {
			pkg = new PackageInfo(pnameFull);
		    }
		    catch (Exception e) {
			e.printStackTrace(System.err);
			break;
		    }
		    addDeclaration(pkg);
		    break;
		}
		else {	// must be instance of ZipFile
		    zipFile = (ZipFile) dirObj;
		    String zipName = zipFile.getName () ;
		    debug.println ("findSubPackage in zip file " + zipName) ;
		    if (pkgName.length() != 0)
			fname = pkgNameFull + "." + pname;
		    else
			fname = pname;
		    fname = fname.replace('.', '/') + "/";
		    // fname = fname.replace('.', '/') ;

		    if (! hasZipDirectory (zipFile, fname))
			continue ;

		    debug.println (fname + " found as zip entry") ;

		    // Class entry exists. Set component's type to new
		    // ClassInfo object. Also add new ClassInfo object to
		    // local table.
		    if (pkgNameFull.length() == 0)
			pnameFull = pname;
		    else
			pnameFull = pkgNameFull + "." + pname;
		    try {
			pkg = new PackageInfo(pnameFull);
		    }
		    catch (Exception e) {
			e.printStackTrace(System.err);
			break;
		    }
		    addDeclaration(pkg);
		    break;
		}
	    }
	}

	debug.println (
	    "findSubPackage found "
	    + (pkg != null ? "\"" + pkg.getFullName () +'"' : "null")
	) ;

	return(pkg);
    }

    //**************************************************
    // Primarily used for debugging. This overrides toString() in Object.
    //**************************************************
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append("Package: ");
	buffer.append(getFullName());
	buffer.append("\n\tContains ");
	buffer.append(getSubPackageCount());
	buffer.append(" sub-packages and ");
	buffer.append(getClassCount());
	buffer.append(" classes.\n");

	return(buffer.toString());
    }

    //**************************************************
    // Primarily used for debugging.
    //**************************************************
    public void dump(PrintWriter out, int indentLevel) {
	Enumeration scanPtr;
	ClassInfo ci;
	PackageInfo pi;
	String name;
	String indent = Symtab.indent[indentLevel];

	// Increase indent for contained objects
	indentLevel++;

	if (pkgNameFull.length() == 0)
	    name = "DEFAULT_PACKAGE";
	else
	    name = pkgNameFull;
	out.println("\n" + indent + "Package: " + name);
	scanPtr = sortCursor (getClassCursor()) ;
	if (scanPtr.hasMoreElements()) {
	    do {
		ci = (ClassInfo) scanPtr.nextElement();
		ci.dump(out, indentLevel);
	    } while (scanPtr.hasMoreElements());
	}

	scanPtr = sortCursor (getSubPackageCursor()) ;
	if (scanPtr.hasMoreElements()) {
	    do {
		pi = (PackageInfo) scanPtr.nextElement();
		pi.dump(out, indentLevel);
	    } while (scanPtr.hasMoreElements());
	}
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private static boolean hasZipDirectory (ZipFile zipFile, String fullName) {

	// First, try the standard lookup method:
	//
	ZipEntry entry = zipFile.getEntry (fullName) ;
	if (entry != null && entry.isDirectory ())
	    return true ;

	// Else, try scanning for an entry with a prefix matching the given
	// name and containing a ".class" file:
	//
	for (Enumeration p = zipFile.entries () ; p.hasMoreElements () ; ) {
	    entry = (ZipEntry) p.nextElement () ;
	    String name = entry.getName () ;

	    if (name.length () >= 1 + fullName.length () + ".class".length ()
		&& name.startsWith (fullName)
		&& name.endsWith (".class"))
		return true ;
	}

	return false ;
    }

    private static LineWriter
        debug = Debug.global.getWriter ("debug.PackageInfo") ;

}

class spEnum implements Enumeration {
    private PackageInfo scanPtr;

    public spEnum(PackageInfo pi) { scanPtr = pi; }

    public boolean hasMoreElements() {
	return(scanPtr != null);
    }

    public Object nextElement() {
	Object result;

	result = scanPtr;
	scanPtr = scanPtr.next;
	return(result);
    }

}
